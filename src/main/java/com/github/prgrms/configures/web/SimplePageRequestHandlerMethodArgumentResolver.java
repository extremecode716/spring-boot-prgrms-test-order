package com.github.prgrms.configures.web;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SimplePageRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_OFFSET_PARAMETER = "offset";

    private static final String DEFAULT_SIZE_PARAMETER = "size";

    private static final long DEFAULT_OFFSET = 0;

    private static final long MIN_OFFSET = 0;

    private static final long MAX_OFFSET = Long.MAX_VALUE;

    private static final int DEFAULT_SIZE = 5;

    private static final int MIN_SIZE = 1;

    private static final int MAX_SIZE = 5;

    private String offsetParameterName = DEFAULT_OFFSET_PARAMETER;

    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String offsetString = webRequest.getParameter(offsetParameterName);
        String sizeString = webRequest.getParameter(sizeParameterName);

        // TODO 구현이 필요 합니다. (완료)
        // throw new UnsupportedOperationException("SimplePageRequest 인스턴스를 리턴하도록 구현 필요");
        long offset = parseOffsetOrSize(MIN_OFFSET, MAX_OFFSET, DEFAULT_OFFSET, offsetString);
        int size = (int) parseOffsetOrSize(MIN_SIZE, MAX_SIZE, DEFAULT_SIZE, sizeString);
        return new SimplePageRequest(offset, size);
    }

    public void setOffsetParameterName(String offsetParameterName) {
        this.offsetParameterName = offsetParameterName;
    }

    public void setSizeParameterName(String sizeParameterName) {
        this.sizeParameterName = sizeParameterName;
    }

    public long parseOffsetOrSize(long min, long max, long defaultValue, String target) {
        if (target == null) {
            return defaultValue;
        }
        try {
            long result = Long.parseLong(target);
            return (result < min || result > max) ? defaultValue : result;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}