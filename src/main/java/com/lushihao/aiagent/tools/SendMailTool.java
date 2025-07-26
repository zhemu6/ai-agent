//package com.lushihao.aiagent.tools;
//
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.ai.tool.annotation.ToolParam;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//
///**
// * 邮件发送工具
// *
// * @author: lushihao
// * @version: 1.0
// * create:   2025-07-25   09:48
// */
//@Component
//public class SendMailTool {
//
//    private final JavaMailSender javaMailSender;
//
//    public SendMailTool(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//
//    @Tool(description = "根据发送方、接收方、标题和正文发送邮件",returnDirect = true)
//    public String sendMail(
//            @ToolParam(description = "邮件的发送方") String from,
//            @ToolParam(description = "邮件的接收方") String to,
//            @ToolParam(description = "邮件的标题") String title,
//            @ToolParam(description = "邮件的正文") String content
//    ){
//        try{
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(from);
//        simpleMailMessage.setTo(to);
//        simpleMailMessage.setSubject(title);
//        simpleMailMessage.setText(content);
//        javaMailSender.send(simpleMailMessage);
//        return "email sent successfully to :" + to;
//    }catch (Exception e ){
//            return "Error sending email: " + e.getMessage();
//
//        }
//    }
//}
