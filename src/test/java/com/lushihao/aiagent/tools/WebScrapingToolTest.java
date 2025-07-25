package com.lushihao.aiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   15:52
 */
//@SpringBootTest
@Slf4j
class WebScrapingToolTest {

    @Test
    void scrapeWrbPage() {

        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String url = "http://www.ipcl.top";
        String result = webScrapingTool.scrapeWrbPage(url);
        log.info(result);
        Assertions.assertNotNull(result);

    }
}