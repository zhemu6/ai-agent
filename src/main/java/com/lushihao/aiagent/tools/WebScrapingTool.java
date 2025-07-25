package com.lushihao.aiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 用于网页抓取的工具实现
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   15:38
 */
public class WebScrapingTool {

    @Tool(description = "Scrape the content of a web page")
    public String scrapeWrbPage(@ToolParam(description = "URL of the web page to scrape") String url){
        try{
            Document doc = Jsoup.connect(url).get();
            return doc.html();
        }catch (Exception e){
            return "Error scrape web page:" + e.getMessage();
        }
    }

}
