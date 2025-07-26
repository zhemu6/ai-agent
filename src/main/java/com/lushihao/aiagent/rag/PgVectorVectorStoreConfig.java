//package com.lushihao.aiagent.rag;
//
//import jakarta.annotation.Resource;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.List;
//
//import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
//import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;
//
//
// * 基于Pg数据库的向量存储实现
// * @author: lushihao
// * @version: 1.0
// * create:   2025-07-23   09:53
// */
//@Configuration
//public class PgVectorVectorStoreConfig {
//    @Resource
//    private LoveAppDocumentLoader loveAppDocumentLoader;
//
//    /**
//     *
//     * @param jdbcTemplate 数据库连接的模板
//     * @param dashscopeEmbeddingModel 嵌入模型
//     * @return 返回一个向量存储的
//     */
//    @Bean
//    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel){
//        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
//                // Optional: defaults to model dimensions or 1536
//                .dimensions(1536)
//                // Optional: defaults to COSINE_DISTANCE
//                .distanceType(COSINE_DISTANCE)
//                // Optional: defaults to HNSW
//                .indexType(HNSW)
//                // 是否在启动时自动创建向量表结构（true 表示自动建表）。
//                .initializeSchema(true)
//                // 设置使用的数据库 schema 名称。
//                .schemaName("public")
//                // 设置存储向量数据的表名。
//                .vectorTableName("vector_store")
//                // 最大支持一次插入 10000 条文档数据，防止内存溢出。
//                .maxDocumentBatchSize(10000)
//                .build();
//        // 使用loveAppDocumentLoader加载md文档
//        List<Document> documents  = loveAppDocumentLoader.loadMarkdowns();
//        // 将documents文档添加到向量存储中
//        vectorStore.add(documents);
//        return vectorStore;
//    }
//}
