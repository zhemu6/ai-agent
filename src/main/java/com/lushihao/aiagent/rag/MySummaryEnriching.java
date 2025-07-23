package com.lushihao.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通过调用AI 提取摘要总结 并添加到元数据中
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   16:52
 */
@Component
public class MySummaryEnriching {

    @Resource
    private ChatModel dashscopeModel;

    public List<Document> enrichingDocuments(List<Document> documents) {
        // 传递两个参数 一个是AI模型 一个需要保存的摘要的范围（前一个、当前、后一个）
        SummaryMetadataEnricher enricher = new SummaryMetadataEnricher(dashscopeModel, List.of(SummaryMetadataEnricher.SummaryType.PREVIOUS, SummaryMetadataEnricher.SummaryType.CURRENT, SummaryMetadataEnricher.SummaryType.NEXT));
        return enricher.apply(documents);
    }
}
