package com.lushihao.aiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   15:25
 */
@Slf4j
@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "你知道谁是鱼皮吗";
        String result = webSearchTool.searchWeb(query);
        log.info(result);
        Assertions.assertNotNull(result);
    }
}