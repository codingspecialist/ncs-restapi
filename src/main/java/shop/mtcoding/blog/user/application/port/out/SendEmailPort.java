package shop.mtcoding.blog.user.application.port.out;


public interface SendEmailPort {
    void sendEmail(String to, String subject, String text);
}
