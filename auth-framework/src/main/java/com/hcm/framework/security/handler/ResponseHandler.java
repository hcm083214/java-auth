package com.hcm.framework.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.enums.ResultCodeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author pc
 */
public class ResponseHandler {
    public static void handler(HttpServletResponse response,ResultCodeEnum resultCode) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ResultVO<String> resultVO = new ResultVO<>(resultCode.getCode(), resultCode.getMessage());
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(resultVO);
        out.write(jsonObject.toJSONString());
        out.flush();
        out.close();
    }
}
