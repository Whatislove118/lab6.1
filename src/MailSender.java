import com.sun.mail.smtp.SMTPAddressFailedException;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MailSender {

    public static void sendEmail(String emailToSend,String passwordTo) throws  MessagingException{
        String email="pochikalin@gmail.com";
        String password = "hokage9916";
        String host = "smtp.gmail.com";

        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            InternetAddress toAddress = new InternetAddress(emailToSend);

            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject("Password");

            System.out.println(passwordTo);

            message.setText("Your password is " + passwordTo);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, email, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();


            }catch (AddressException e) {
                    System.out.println("Неизвестный адрес почты!");
            }

    }
}
