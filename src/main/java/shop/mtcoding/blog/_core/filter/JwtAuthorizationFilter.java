package shop.mtcoding.blog._core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.utils.JwtEnum;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog.domain.user.application.port.out.UserRepositoryPort;
import shop.mtcoding.blog.domain.user.domain.SessionUser;
import shop.mtcoding.blog.domain.user.domain.User;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {

    private final UserRepositoryPort userRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String jwt = request.getHeader("Authorization");
        if (jwt == null || jwt.isEmpty()) {
            onError(response, JwtEnum.ACCESS_TOKEN_NOT_FOUND);
            return;
        }

        try {
            User user = JwtUtil.verify(jwt);
            SessionUser sessionUser = new SessionUser(user.getId(), user.getRole(), jwt, null);
            request.setAttribute("sessionUser", sessionUser);
            chain.doFilter(request, response);
        } catch (SignatureVerificationException | JWTDecodeException e) {
            onError(response, JwtEnum.ACCESS_TOKEN_INVALID);
        } catch (TokenExpiredException e) {
            onError(response, JwtEnum.ACCESS_TOKEN_TIMEOUT);
        } catch (RuntimeException e) {
            onError(response, JwtEnum.ACCESS_TOKEN_ROLE_ERROR);
        }
    }


    private void onError(HttpServletResponse response, JwtEnum jwtEnum) throws IOException {
        Exception401 e401 = new Exception401(jwtEnum.name());
        String body = new ObjectMapper().writeValueAsString(e401.body());
        response.setStatus(e401.status().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(body);
        ;
    }
}
