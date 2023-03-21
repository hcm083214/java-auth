package com.hcm.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * excel工具类
 *
 * @author pc
 * @date 2023/03/20
 */
public class ExcelUtils {
    /**
     * 导出
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName 表名字
     * @param head      Excel head 头
     * @param data      数据
     */
    public static <T> void export(HttpServletResponse response, String filename,
                                  String sheetName, Class<T> head, List<T> data) throws IOException {
        EasyExcel.write(response.getOutputStream(), head)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName)
                .doWrite(data);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    public static <T> List<T> excel2List(MultipartFile file, Class<T> head)throws IOException{
        return EasyExcel.read(file.getInputStream(),head,null)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                .doReadAllSync();
    }
}
