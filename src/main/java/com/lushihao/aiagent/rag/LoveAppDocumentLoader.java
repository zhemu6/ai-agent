package com.lushihao.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档加载类 用于读取所有的MarkDown文档并且转换成Document列表
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-22   16:13
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {
    // 用于通过通配符方式加载多个资源文件。
    private final ResourcePatternResolver resourcePatternResolver;

    // 快速加载多个文档
    LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns() {
        List<Document> allDocument = new ArrayList<>();
        // 加载多篇文档
        try {
            // 遍历resources目录下所有.md结尾的文档资源
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            // 遍历每个资源
            for (Resource resource : resources) {
                // 获取文件名
                String filename = resource.getFilename();
                // 提取文件名的倒数第二个、第三个字作为标签
                String status = filename.substring(filename.length() - 6, filename.length() - 4);
                // 创建MarkdownDocumentReader的配置对象
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        // 遇到 Markdown 中的 --- 水平分隔线就创建新 Document
                        .withHorizontalRuleCreateDocument(true)
                        // 忽略代码块
                        .withIncludeCodeBlock(false)
                        // 忽略引用块
                        .withIncludeBlockquote(false)
                        // 将文件名中作为元信息添加到加载的文件中
                        .withAdditionalMetadata("filename", filename)
                        .withAdditionalMetadata("status",status)
                        .build();
                // 利用该配置床键一个读取器读取这些资源 并将其转换成 Document对象 添加到allDocument 列表中
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, config);
                allDocument.addAll(markdownDocumentReader.get());
            }
        } catch (IOException e) {
            log.error("Markdown文档加载失败", e);
        }
        return allDocument;
    }

}
