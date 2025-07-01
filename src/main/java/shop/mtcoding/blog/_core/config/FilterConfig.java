package shop.mtcoding.blog._core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.blog._core.filter.CorsFilter;
import shop.mtcoding.blog._core.filter.JwtAuthorizationFilter;
import shop.mtcoding.blog.domain.user.application.port.out.FindUserPort;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final FindUserPort findUserPort;

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
                new FilterRegistrationBean<>(new JwtAuthorizationFilter(findUserPort));
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);
        return bean;
    }
}
