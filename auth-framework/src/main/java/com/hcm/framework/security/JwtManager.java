package com.hcm.framework.security;

import com.alibaba.fastjson.JSON;
import com.hcm.common.constants.CacheConstants;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.core.redis.RedisCache;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.BaseUtils;
import com.hcm.common.core.entity.JwtPayload;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.utils.uuid.UUID;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

/**
 * @author pc
 * @description JWT工具类
 */
@Slf4j
@Component
public class JwtManager {

    @Value("${jwt.secretKey}")
    private String secretKey;

    /**
     * 过期时间目前设置成2天，这个配置随业务需求而定
     */
    @Value("${jwt.expiration}")
    private Integer expiration;

    @Autowired
    private RedisCache redisCache;

    /**
     * 设置有效载荷
     *
     * @param uuid token的唯一id
     * @return {@link JwtPayload}
     */
    private JwtPayload setPayload(String uuid) {
        JwtPayload payload = new JwtPayload();
        Date now = new Date();
        Date exp = new Date(currentTimeMillis());
        payload.setSub("hcm");
        // 时间是以秒为单位的
        payload.setIat(now.getTime() / 1000);
        payload.setExp(exp.getTime() / 1000 + expiration);
        payload.setJti(uuid);
        return payload;
    }

    /**
     * 生成token并且缓存用户信息
     *
     * @param userDetail 用户详细信息
     * @return {@link String}
     * @description 生成JWT
     */
    public String generate(UserDetail userDetail) {
        String tokenId = UUID.fastUUID().toString();
        userDetail.setToken(tokenId);
        saveUserToRedis(userDetail);
        return createToken(tokenId);
    }

    /**
     * 创建令牌
     *
     * @param token 令牌
     * @return {@link String}
     */
    public String createToken(String token) {
        try {
            //准备JWS-header
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT).build();

            //将负载信息封装到Payload中
            Payload payload = new Payload(JSON.toJSONString(setPayload(token)));
            //封装header和payload到JWS对象
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            //创建HMAC签名器
            JWSSigner jwsSigner = new MACSigner(secretKey);
            //签名
            jwsObject.sign(jwsSigner);

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("JwtManager--->generate--->JOSEException:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param token JWT字符串
     * @return 解析成功返回Claims对象，解析失败返回null
     */
    public UserDetail parse(String token) {
        if (BaseUtils.isEmptyString(token)) {
            return null;
        }
        UserDetail userDetail = null;
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            //创建HMAC验证器
            JWSVerifier jwsVerifier = new MACVerifier(secretKey);
            if (!jwsObject.verify(jwsVerifier)) {
                throw new BadRequestException("token签名不合法!");
            }
            String payload = jwsObject.getPayload().toString();
            JwtPayload jwsPayload = JSON.parseObject(payload, JwtPayload.class);
            String tokenId = jwsPayload.getJti();
            userDetail = redisCache.getCacheObject(getTokenCacheKey(tokenId));
        } catch (ParseException | JOSEException e) {
            log.error("JwtManager--->parse--->ParseException | JOSEException:{}", e.getMessage());
            e.printStackTrace();
        }
        return userDetail;
    }

    /**
     * 将用户信息缓存到redis中
     *
     * @param userDetail 用户详细信息
     */
    public void saveUserToRedis(UserDetail userDetail) {
        if (!StringUtils.isNull(userDetail) && StringUtils.isNotEmpty(userDetail.getToken())) {
            refreshUserTokenOnRedis(userDetail);
        }
    }

    /**
     * 刷新用户信息的过期时间
     *
     * @param userDetail 用户详细信息
     */
    private void refreshUserTokenOnRedis(UserDetail userDetail) {
        long now = System.currentTimeMillis() / 1000;
        userDetail.setLoginTime(now);
        userDetail.setExpireTime(now + expiration);
        redisCache.setCacheObject(getTokenCacheKey(userDetail.getToken()), userDetail, expiration, TimeUnit.SECONDS);
    }

    /**
     * 删除用户令牌
     *
     * @param userDetail 用户详细信息
     */
    public void deleteUserTokenOnRedis(UserDetail userDetail) {
        String uuid = userDetail.getToken();
        if (StringUtils.isNotEmpty(uuid)) {
            redisCache.deleteObject(getTokenCacheKey(uuid));
        }
    }

    /**
     * 快要过期则刷新缓存
     *
     * @param userDetail 用户详细信息
     */
    public void verifyUserTokenOnRedis(UserDetail userDetail) {
        long now = System.currentTimeMillis() / 1000;
        long expireTime = userDetail.getExpireTime();
        if (expireTime - now < expiration) {
            refreshUserTokenOnRedis(userDetail);
        }
    }

    /**
     * 获得令牌缓存键
     *
     * @param uuid uuid
     * @return {@link String}
     */
    private String getTokenCacheKey(String uuid) {
        return CacheConstants.CACHE_LOGIN_TOKEN_KEY + uuid;
    }
}
