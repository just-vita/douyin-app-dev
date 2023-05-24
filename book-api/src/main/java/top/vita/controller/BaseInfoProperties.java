package top.vita.controller;

import top.vita.grace.utils.RedisOperator;

/**
 * @Author vita
 * @Date 2023/5/24 14:11
 */
public class BaseInfoProperties {
    public RedisOperator redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";
}
