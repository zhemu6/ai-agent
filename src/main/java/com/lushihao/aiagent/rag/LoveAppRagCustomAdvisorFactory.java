package com.lushihao.aiagent.rag;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建自定义RAG检索增强顾问的工厂
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   21:00
 */

public class LoveAppRagCustomAdvisorFactory {


    public static Advisor createLoveAppRagCustomAdvisor(VectorStore vectorStore,String status){


        //构建一个过滤查询表达式 是状态和我们指定的状态相等
        Filter.Expression expression = new FilterExpressionBuilder().eq("status", status).build();

        // 构建向量数据库
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever
                .builder()
                .vectorStore(vectorStore)
                // 设置过滤条件
                .filterExpression(expression)
                // 相似度阈值
                .similarityThreshold(0.5)
                // 返回检索的内容
                .topK(3)
                .build();


        return RetrievalAugmentationAdvisor
                .builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(LoveAppContextualQueryAugmenterFactory.createInstance())
                .build();

    }
}
