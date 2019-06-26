package com.javaweb.MichaelKai.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.JwtResultEnums;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: project_base
 * @description: jwt工具类
 * @author: YuKai Fan
 * @create: 2019-06-24 10:46
 **/
public class JwtUtil {

    /**
     * 生成JwtToken
     * @param account 用户账号
     * @param secret 密钥
     * @param expire 过期时间
     * @return
     */
    public static String getToken(String account, String secret, int expire) {
        User user = new User();
        user.setAccount(account);
        return getToken(user, secret, expire);
    }


    /**
     * 生成JwtToken
     *
     * 注意:这里需要注意一下。一般是需要两个时间，一个是token过期时间expireTime，还有一个token刷新时间refreshTime。
     *
     * token的过期时间一般是根据token存入redis中的过期时间来判断。如果redis中token未过期就直接刷新token会犯后台, 否则需要前端重新登录
     *
     * @param user 用户对象
     * @param secret 密钥
     * @param expire 过期时间 这里是指token的刷新时间，过期时间是存入redis中过期时间。一般是token刷新时间的几倍
     * @return
     */
    public static String getToken(User user, String secret, int expire) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, expire);

        //随机Claim
        String claim = ToolUtil.getRandomString(6);

        //创建JwtToken对象
        String token = "";
        token = JWT.create()
                .withSubject(user.getAccount())
                .withIssuedAt(new Date())
                .withExpiresAt(calendar.getTime())
                .withClaim("ran", claim)
                .sign(getSecret(secret, claim));

        //将jwt存入redis(过期时间为token失效的两倍)
        RedisUtil redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        redisUtil.set(Constant.JWT_TOKEN + user.getAccount(), token, expire * 2);
        return token;
    }

    /**
     * 验证JwtToken
     * @param token token数据
     * @param secret 密钥
     * @throws JWTVerificationException 令牌无效(验证不通过)
     * @throws TokenExpiredException token过期
     */
    public static void verifyToken(String token, String secret) throws JWTVerificationException {
        String ran = JWT.decode(token).getClaim("ran").asString();
        JWTVerifier jwtVerifier = JWT.require(getSecret(secret, ran)).build();
        jwtVerifier.verify(token);
    }

    /**
     * 生成Secret混淆数据
     * @param secret
     * @param claim
     * @return
     */
    private static Algorithm getSecret(String secret, String claim) {
        //String salt = MD5Util.createSalt();
        return Algorithm.HMAC256(secret + claim);
    }

    /**
     * 获取请求头数据
     * @param request
     * @return
     */
    public static String getRequestToken(HttpServletRequest request, String name) {
        String data = request.getHeader(name);
        if (data == null) {
            throw new ResultException(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getMessage());
        }
        return data;
    }
}