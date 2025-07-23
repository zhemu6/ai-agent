package com.lushihao.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   20:37
 */
@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expand() {
        List<Query> expand = multiQueryExpanderDemo.expand("啥是程序员鱼皮啊啊啊啊啊啊啊啊？！请回答我哈哈哈哈哈");
        Assertions.assertNotNull(expand);
    }
}