package com.lushihao.aiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库Bean）
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-22   16:44
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnriching myKeywordEnriching;
    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        // 自助切分文档
//        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
        List<Document> enrichingDocuments = myKeywordEnriching.enrichingDocuments(documents);
        // 使用增强器
        simpleVectorStore.add(enrichingDocuments);
        return simpleVectorStore;
    }

}
