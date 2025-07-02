package shop.mtcoding.blog.domain.user.application.port.out;

public interface SendEmailPort {
    void sendEmail(String to, String subject, String body);
}
