package com.hcm.controller;


import com.google.code.kaptcha.Producer;
import com.hcm.common.constants.CacheConstants;
import com.hcm.common.constants.CommonConstants;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.redis.RedisCache;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.BaseUtils;
import com.hcm.common.utils.uuid.IdUtils;
import com.hcm.common.vo.CaptchaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author pc
 */
@Slf4j
@RestController
@RequestMapping("/captcha")
@Api(tags = "验证码管理")
public class CaptchaController {


    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    /**
     * @param type 验证码的类型，string 和 math 两种
     */
    @GetMapping("/image")
    @ApiOperation(value = "验证码查询", notes = "获取验证码")
    public ResultVO<CaptchaVo> getCaptchaImg(@RequestParam("type") String type) {
        if (BaseUtils.isEmptyString(type)) {
            throw new BadRequestException("type 未传");
        }
        CaptchaVo captchaVo = new CaptchaVo();
        String code = null;
        BufferedImage image = null;
        String base64ImgUrl;

        if (CommonConstants.CAPTCHA_TYPE_STRING.equals(type)) {
            code = captchaProducer.createText();
            image = captchaProducer.createImage(code);
        } else if (CommonConstants.CAPTCHA_TYPE_MATH.equals(type)) {
            String codeMath = captchaProducerMath.createText();
            int index = codeMath.indexOf("@");
            code = codeMath.substring(index + 1);
            String prefix = codeMath.substring(0, index);
            log.info("codeMath:{},prefix:{},code:{}", codeMath, prefix, code);
            image = captchaProducer.createImage(prefix);
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();
            final Base64.Encoder encoder = Base64.getEncoder();
            base64ImgUrl = encoder.encodeToString(bytes);
            captchaVo.setBase64Url(base64ImgUrl);
            captchaVo.setCaptchaEnabled(true);

        } catch (Exception e) {
            log.error("CaptchaController ---> getCaptchaImg,验证码获取失败:" + e.getMessage());
        }
        // 将验证码信息保存到缓存中
        String uuid = IdUtils.simpleUUID();
        redisCache.setCacheObject(CacheConstants.CACHE_CAPTCHA_CODE + uuid, code, CacheConstants.CACHE_CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        log.info("CaptchaController ---> getCaptchaImg,captchaCode:" + code);
        captchaVo.setUuid(uuid);
        return ResultVO.success(captchaVo);
    }
}
