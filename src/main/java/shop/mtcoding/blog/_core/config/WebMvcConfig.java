package shop.mtcoding.blog._core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final SessionUserResolver sessionUserResolver;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("file:" + "./images/")
                .setCachePeriod(60 * 60) // 초 단위 => 한시간
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionUserResolver);
    }
}
