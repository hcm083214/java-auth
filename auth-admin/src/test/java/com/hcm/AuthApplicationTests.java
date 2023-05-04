package com.hcm;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.vo.CaptchaVo;
import com.hcm.controller.common.CaptchaController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthApplicationTests {

    @Autowired
    private CaptchaController captchaController;

    @Test
    public void captchaTest() {
        ResultVO<CaptchaVo> math = captchaController.getCaptchaImg("math");
        System.out.println(math);
    }
}
