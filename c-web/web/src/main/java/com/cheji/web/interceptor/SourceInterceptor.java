package com.cheji.web.interceptor;

import com.cheji.web.constant.SourceEnum;
import com.cheji.web.context.SourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SourceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SourceInterceptor.class);
    private static final String SOURCE_HEADER = "X-Source";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String source = request.getHeader(SOURCE_HEADER);
        if (source == null || source.isEmpty()) {
            // 兼容模式：小程序端未传 X-Source 时放行，不设置 SourceContext
            logger.debug("请求未携带 X-Source header, uri={}, 放行", request.getRequestURI());
            return true;
        }
        if (!SourceEnum.isValid(source)) {
            logger.warn("请求携带无效的 X-Source header, source={}, uri={}", source, request.getRequestURI());
            response.setStatus(400);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":400,\"msg\":\"无效的来源标识\"}");
            return false;
        }
        SourceContext.setSource(source);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SourceContext.clear();
    }
}
