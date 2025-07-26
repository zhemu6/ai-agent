package com.lushihao.imagesearchmcp.tools;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   10:47
 */
@Slf4j
@SpringBootTest
class ImageSearchToolTest {


    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void searchImage() {
        String searchImage = imageSearchTool.searchImage("computer");
        Assertions.assertNotNull(searchImage);
        log.info(searchImage);
    }

}