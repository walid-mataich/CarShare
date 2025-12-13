package com.example.Backend.Service;



import com.example.Backend.dto.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendMail(MailBody mailBody) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(mailBody.to());
            helper.setSubject(mailBody.subject());
            helper.setFrom("j75611986@gmail.com");
            helper.setText(mailBody.text(), true); // true = send as HTML

            javaMailSender.send(message);

        } catch (MessagingException e) {
            // Optional: log the error or throw a custom exception
            e.printStackTrace();
        }
    }


    public void sendSimpleMail(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setFrom("j75611986@gmail.com");
        message.setText(mailBody.text());

        javaMailSender.send(message);
    }
}
