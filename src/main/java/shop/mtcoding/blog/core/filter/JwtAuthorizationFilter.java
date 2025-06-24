package shop.mtcoding.blog.core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import shop.mtcoding.blog.core.errors.exception.api.Exception401;
import shop.mtcoding.blog.core.utils.JwtEnum;
import shop.mtcoding.blog.core.utils.JwtUtil;
import shop.mtcoding.blog.domain.user.User;

import java.io.IOException;
import java.io.PrintWriter;


public class JwtAuthorizationFilter implements Filter {

    // API 요청마다 동작
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("JwtAuthorizationFilter 필터 작동");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String jwt = request.getHeader("Authorization");
        if (jwt == null || jwt.isEmpty()) {
            chain.doFilter(request, response);
        } else {
            try {
                User sessionUser = JwtUtil.verify(jwt);

                HttpSession session = request.getSession();
                session.setAttribute("sessionUser", sessionUser);

                chain.doFilter(request, response);
            } catch (SignatureVerificationException | JWTDecodeException e1) {
                onError(response, JwtEnum.ACCESS_TOKEN_INVALID);
            } catch (TokenExpiredException e2) {
                onError(response, JwtEnum.ACCESS_TOKEN_TIMEOUT);
            }
        }


    }

    // ExceptionHandler를 호출할 수 없다. 왜? Filter니까!! DS전에 작동하니까!!
    private void onError(HttpServletResponse response, JwtEnum jwtEnum) {
        Exception401 e401 = new Exception401(jwtEnum.name());

        try {
            String body = new ObjectMapper().writeValueAsString(e401.body());
            response.setStatus(e401.status().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter out = response.getWriter();
            out.println(body);
        } catch (Exception e) {
            System.out.println("파싱 에러가 날 수 없음");
        }
    }
}