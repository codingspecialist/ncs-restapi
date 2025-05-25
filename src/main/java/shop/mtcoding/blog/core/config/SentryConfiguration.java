package shop.mtcoding.blog.core.config;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://a383ce4ae9f38918b131a0ad8e1a199e@o4507648359858176.ingest.us.sentry.io/4507648362676224")
@Configuration
public class SentryConfiguration {
}
