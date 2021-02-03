package com.morse.chatting_server.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RequestResponseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(req);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(res);

        log.info("\n" +
                        "[REQUEST] {}\n " +
                        "url : {}\n" +
                        "Request Headers : {}\n" +
                        "Request Body : {} \n",
                req.getMethod(),
                req.getRequestURI(),
                getHeaders(req),
                getRequestBody(requestWrapper)
        );

        long start = System.currentTimeMillis();
        chain.doFilter(requestWrapper, responseWrapper);
        long end = System.currentTimeMillis();

        log.info("\n" +
                        "ResponseStatus : {}\n" +
                        "ResponseTime : {}\n" +
                        "Response Body: {}\n",
                responseWrapper.getStatus(),
                (end - start) / 1000.0,
                getResponseBody(responseWrapper));
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }

        if (request.getHeaders("X-Forwarded-For") == null) {
            headerMap.put("Client ip ", getClientIP(request));
        } else {
            headerMap.put("Client ip ", request.getHeaders("X-Forwarded-For").toString());
        }

        return headerMap;
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("Proxy-Client-IP");

        if (ip == null)
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null)
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null)
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null)
            ip = request.getRemoteAddr();

        log.info(" >> ip >> " + ip);
        return ip;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }
}
