package shop.mtcoding.blog.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.blog.core.filter.CorsFilter;
import shop.mtcoding.blog.core.filter.JwtAuthorizationFilter;

@Slf4j
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/*"); // 모든 요청에 적용
        registrationBean.setOrder(0); // 필터 순서 설정
        return registrationBean;
    }

    @Bean
    FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> bean =
                new FilterRegistrationBean<>(new JwtAuthorizationFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);
        return bean;
    }

}
