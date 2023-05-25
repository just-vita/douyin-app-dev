package top.vita.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.vita.base.BaseInfoProperties;
import top.vita.exceptions.GraceException;
import top.vita.grace.result.ResponseStatusEnum;
import top.vita.utils.IPUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author vita
 * @Date 2023/5/24 14:30
 */
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIp = IPUtil.getRequestIp(request);
        boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);
        if (keyIsExist){
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
