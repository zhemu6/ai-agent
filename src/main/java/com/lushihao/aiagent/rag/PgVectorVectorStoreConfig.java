package com.lushihao.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   09:53
 */
@Configuration
public class PgVectorVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    /**
     *
     * @param jdbcTemplate 数据库连接的模板
     * @param dashscopeEmbeddingModel 嵌入模型
     * @return 返回一个向量存储的
     */
    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel){
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                // Optional: defaults to model dimensions or 1536
                .dimensions(1536)
                // Optional: defaults to COSINE_DISTANCE
                .distanceType(COSINE_DISTANCE)
                // Optional: defaults to HNSW
                .indexType(HNSW)
                // Optional: defaults to false
                .initializeSchema(true)
                // Optional: defaults to "public"
                .schemaName("public")
                // Optional: defaults to "vector_store"
                .vectorTableName("vector_store")
                // Optional: defaults to 10000 最大批量插入文档数据
                .maxDocumentBatchSize(10000)
                .build();
        List<Document> documents  = loveAppDocumentLoader.loadMarkdowns();
        vectorStore.add(documents);
        return vectorStore;
    }
}
