package com.lushihao.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   16:52
 */
@Component
public class MyKeywordEnriching {

    @Resource
    private ChatModel dashscopeModel;

    public List<Document> enrichingDocuments(List<Document> documents) {
        KeywordMetadataEnricher metadataEnricher = new KeywordMetadataEnricher(dashscopeModel, 5);
        return metadataEnricher.apply(documents);
    }
}
