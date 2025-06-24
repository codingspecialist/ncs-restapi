package shop.mtcoding.blog.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import shop.mtcoding.blog.domain.user.User;

import java.util.Date;

public class JwtUtil {
    public static String createRefresh(User user) {
        String jwt = JWT.create()
                .withSubject("blog")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512("metacoding"));
        return jwt;
    }

    public static String create(User user) {
        String jwt = JWT.create()
                .withSubject("blog")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512("metacoding"));
        return jwt;
    }

    public static User verify(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("metacoding")).build().verify(jwt);
        Long id = decodedJWT.getClaim("id").asLong();
        String username = decodedJWT.getClaim("username").asString();

        return User.builder()
                .id(id)
                .username(username)
                .build();
    }
}