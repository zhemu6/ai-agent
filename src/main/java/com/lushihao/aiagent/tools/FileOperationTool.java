package com.lushihao.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.lushihao.aiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具类 实现的包括文件读取、文件保存
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   14:41
 */


public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    /**
     * 读取文件
     *
     * @param fileName 文件名
     * @return
     */
    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName) {
        // 定义一个文件路径
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file:" + e.getMessage();
        }
    }

    /**
     * 写入文件
     *
     * @param fileName 要写入的文件名
     * @param content  需要写入文件的内容
     * @return
     */
    @Tool(description = "Write content to a file")
    public String writeFile(
            @ToolParam(description = "Name of the file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content) {
        try{
            // 定义需要写入的文件路径
            String filePath = FILE_DIR + "/" + fileName;
            FileUtil.mkdir(filePath);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to:" + filePath;
        } catch (Exception e){
            return "Error writing file:" + e.getMessage();
        }


    }
}
