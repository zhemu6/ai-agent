//package com.lushihao.aiagent.rag;
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.ai.vectorstore.SearchRequest;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.ai.document.Document;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//

//**
// * @author: lushihao
// * @version: 1.0
// * create:   2025-07-23   10:00
// */
//@Slf4j
//@SpringBootTest
//class PgVectorVectorStoreConfigTest {
//
//    @Resource
//    private VectorStore pgVectorVectorStore;
//
//    @Test
//    void pgVectorVectorStore(){
//        List<Document> documents = List.of(
//                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
//                new Document("The World is Big and Salvation Lurks Around the Corner"),
//                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
//
//        // Add the documents to PGVector
//        pgVectorVectorStore.add(documents);
//
//        // Retrieve documents similar to a query
//        List<Document> results = this.pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
//
//        Assertions.assertNotNull(results);
//
//
//
//    }
//
//}