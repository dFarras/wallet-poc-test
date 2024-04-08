package com.playtomic.tests.wallet.filters;

import com.playtomic.tests.wallet.exceptions.ErrorCatalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class RequestLogger implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        log.info(
                "Request to : {} {}",
                req.getMethod(),
                req.getRequestURI()
        );

        try {
            chain.doFilter(request, response);
        } catch (IOException e) {
            log.error("Error logging request");
            throw ErrorCatalog.INTERNAL_SERVER_ERROR.getException();
        }
    }

}
