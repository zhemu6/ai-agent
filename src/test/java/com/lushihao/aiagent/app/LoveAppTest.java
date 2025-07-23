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
 * create:   2025-07-09   10:34
 */
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;




    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好 放款 我叫卢世豪";
        String answer = loveApp.doChat(message,chatId);
        // 第二轮
        message = "如何让另一半更爱我";
        answer = loveApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "告诉我我的名字 刚才跟你说过 请你帮我回忆一下";
        answer = loveApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChat() {
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好 我叫卢世豪 如何让另一半更爱我";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好 我叫卢世豪 如何让另一半更爱我";
        String answer   = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}