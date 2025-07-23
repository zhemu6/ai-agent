package com.lushihao.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通过调用AI 提取关键词并添加到元数据中
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   16:52
 */
@Component
public class MyKeywordEnriching {

    @Resource
    private ChatModel dashscopeModel;

    public List<Document> enrichingDocuments(List<Document> documents) {
        // 传递两个参数 一个是AI模型 一个是生成关键词的数量
        KeywordMetadataEnricher metadataEnricher = new KeywordMetadataEnricher(dashscopeModel, 5);
        return metadataEnricher.apply(documents);
    }
}
