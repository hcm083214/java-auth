package com.hcm.controller.common;

import com.hcm.common.constants.CommonConstants;
import com.hcm.common.exception.AuthException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pc
 * @description 专门用来处理 filter 传递过来的异常
 */
@RestController
public class ExceptionController {
    @RequestMapping(CommonConstants.SECURITY_AUTH_ERROR_PATH)
    public void handleException(HttpServletRequest request) throws AuthException{
        String message = (String) request.getAttribute(CommonConstants.SECURITY_AUTH_ERROR_ATTRIBUTE);
        throw new AuthException(message);
    }
}
