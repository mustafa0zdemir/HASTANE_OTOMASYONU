package mailSunucu;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail {
    public static void main(String[] args) {
        // Gönderen e-posta bilgileri
        String host = "smtp.gmail.com"; // Gmail SMTP sunucusu
        String port = "587"; // SMTP portu (TLS)
        final String username = "ozadahospital@gmail.com"; // E-posta adresiniz
        final String password = "jtgv vlti jtmj asne"; // uygulama şifresi

        // Alıcı e-posta adresi
        String to = "mustafa2017ozdemir@gmail.com"; 

        // E-posta özelliklerini tanımlama
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // TLS kullanımı
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        // Mail kimlik doğrulama
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // E-posta oluşturma
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Gönderen adres
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Alıcı adres
            message.setSubject("Randevu Onayı"); // Konu
            message.setText("Sayın kullanıcı,\n\nRandevunuz başarıyla oluşturulmuştur.\n\nSağlıklı günler dileriz."); // Mesaj içeriği

            // E-posta gönderme
            Transport.send(message);
            System.out.println("E-posta başarıyla gönderildi.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
