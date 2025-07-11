package shop.mtcoding.blog.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.blog.core.filter.CorsFilter;
import shop.mtcoding.blog.core.filter.JwtAuthorizationFilter;
import shop.mtcoding.blog.domain.user.UserRepository;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final UserRepository userRepository;

    @Bean
    public FilterRegistrationBean<?> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new CorsFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<?> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> bean =
                new FilterRegistrationBean<>(new JwtAuthorizationFilter(userRepository));
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);
        return bean;
    }
}
