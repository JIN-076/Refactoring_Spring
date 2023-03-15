package toby.spring.inha.refactor.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import toby.spring.inha.refactor.user.domain.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailPolicy {

    private final MailSender mailSender;

    @Autowired
    public EmailPolicy(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendUpgradeEmail(User user) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "mail.ksug.org");
        Session session = Session.getInstance(properties, null);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("useradmin@ksug.org"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다");

            Transport.send(message);
        } catch (AddressException ae) {
            throw new RuntimeException(ae);
        } catch (MessagingException me) {
            throw new RuntimeException(me);
        }
    }

    public void sendUpgradeEmailRfc(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다");

        this.mailSender.send(mailMessage);
    }
}
