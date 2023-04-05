package com.hcm.framework.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.enums.ResultCodeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 响应处理程序
 *
 * @author pc
 * @date 2023/03/31
 */
public class ResponseHandler {
    public static void handler(HttpServletResponse response,Integer code,String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ResultVO<String> resultVO = new ResultVO<>(code, message);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(resultVO);
        out.write(jsonObject.toJSONString());
        out.flush();
        out.close();
    }
}
