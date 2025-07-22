package com.lushihao.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-22   16:38
 */
@SpringBootTest
class LovaAppDocumentLoaderTest {

    @Resource
    private LovaAppDocumentLoader lovaAppDocumentLoaderl;

    @Test
    void loadMarkdowns() {
        lovaAppDocumentLoaderl.loadMarkdowns();
    }
}