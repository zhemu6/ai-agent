package com.lushihao.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-09   10:34
 */
@Slf4j
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
        String message = "我已经结婚了 但是婚后关系不太亲密 我该怎么办";
        String answer   = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
//        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");
//
//        // 测试网页抓取：恋爱案例分析
//        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");
//
//        // 测试资源下载：图片下载
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");
//
//        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");
//
//        // 测试文件操作：保存用户档案
//        testMessage("保存我的恋爱档案为文件");
//
//        // 测试 PDF 生成
//        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");

        // 测试邮件发送
        testMessage("请以 1137800445@qq.com 为发送方，发送一封邮件到 zhemu1104@outlook.com，标题是“测试”，内容是“这是一封测试邮件”");

    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }


    @Test
    void doChatWithMCP() {
        String chatId = UUID.randomUUID().toString();
//        String message  =  "我的另一半住在上海静安区，请你帮我找到五公里内合适的约会地点";
        String message  =  "帮我搜索一些图片,图片的内容是:“一台电脑放在桌子上";
        String answer = loveApp.doChatWithMCP(message, chatId);
        Assertions.assertNotNull(answer);

    }



}