package com.lushihao.aiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-22   20:12
 */
@SpringBootTest
class TuijianAppTest {


    @Resource
    private TuijianApp tuijianApp;


    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好 我现在是单身 我想请你给我推荐一个恋爱对象 我希望他是一个天蝎座的 心里咨询师";
        String answer   = tuijianApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}