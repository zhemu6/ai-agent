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
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-22   16:13
 */
@Component
@Slf4j
public class LovaAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    // 快速加载多个文档
    LovaAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns() {
        List<Document> allDocument = new ArrayList<>();
        // 加载多篇文档
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        // 将文件名中作为元信息添加到加载的文件中
                        .withAdditionalMetadata("filename", filename)
                        .build();
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, config);
                allDocument.addAll(markdownDocumentReader.get());
            }
        } catch (IOException e) {
            log.error("Markdown文档加载失败", e);
        }
        return allDocument;
    }

}
