package com.lushihao.aiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网络搜素工具
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   15:03
 */
public class WebSearchTool {
    // 搜索接口地址
    public final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";
    // 定义一个apiKey 用于保存search的key
    private final String apiKey;
    // 构造函数
    public WebSearchTool(String apiKey){
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(@ToolParam(description = "Search query keyword") String query){
        Map<String ,Object> paramMap = new HashMap<>();
        paramMap.put("q",query);
        paramMap.put("api_key",apiKey);
        paramMap.put("engine","baidu");
        try{
            String response = HttpUtil.get(SEARCH_API_URL,paramMap);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取Organic_results的部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            // 取出结果前五条
            List<Object> objects = organicResults.subList(0,5);
            // 拼接搜索
            return objects.stream().map(obj -> ((JSONObject) obj).toString()).collect(Collectors.joining(","));
        }catch (Exception e){
            return "Error reading file:" + e.getMessage();
        }

    }

}
