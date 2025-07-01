package shop.mtcoding.blog._core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserType;

import java.util.Date;

public class JwtUtil {

    public static String create(User user) {
        return JWT.create()
                .withSubject("accessToken")
                .withClaim("userId", user.getId())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30분
                .sign(Algorithm.HMAC512("METACODING"));
    }

    public static String createRefresh(User user) {
        return JWT.create()
                .withSubject("refreshToken")
                .withClaim("userId", user.getId())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14)) // 2주
                .sign(Algorithm.HMAC512("METACODING"));
    }


    public static User verify(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("METACODING")).build().verify(jwt);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        return User.builder()
                .id(id)
                .role(UserType.valueOf(role))
                .build();
    }
}