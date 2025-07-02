package shop.mtcoding.blog.user.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.user.application.port.out.SendEmailPort;

@RequiredArgsConstructor
@Component
public class EmailServiceAdapter implements SendEmailPort {

    @Override
    public void sendEmail(String to, String subject, String text) {
        System.out.println(to + " 님에게 인증코드 이메일 발송완료");
    }
}
