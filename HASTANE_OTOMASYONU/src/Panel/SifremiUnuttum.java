package Panel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

public class SifremiUnuttum extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField tcField, adField, soyadField, telField;

    // Ana pencereyi oluşturma
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SifremiUnuttum frame = new SifremiUnuttum();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ŞifremiUnuttum penceresini oluşturma
    public SifremiUnuttum() {
        setTitle("Şifremi Unuttum");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 441, 342);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // Kullanıcı bilgileri alacak alanlar
        JLabel lblTc = new JLabel("TC Kimlik No:");
        lblTc.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTc.setBounds(30, 30, 120, 16);
        getContentPane().add(lblTc);

        tcField = new JTextField();
        tcField.setBounds(160, 30, 200, 26);
        getContentPane().add(tcField);

        JLabel lblAd = new JLabel("Ad:");
        lblAd.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblAd.setBounds(30, 70, 120, 16);
        getContentPane().add(lblAd);

        adField = new JTextField();
        adField.setBounds(160, 70, 200, 26);
        getContentPane().add(adField);

        JLabel lblSoyad = new JLabel("Soyad:");
        lblSoyad.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblSoyad.setBounds(30, 110, 120, 16);
        getContentPane().add(lblSoyad);

        soyadField = new JTextField();
        soyadField.setBounds(160, 110, 200, 26);
        getContentPane().add(soyadField);

        JLabel lblTel = new JLabel("Telefon No:");
        lblTel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTel.setBounds(30, 150, 120, 16);
        getContentPane().add(lblTel);

        telField = new JTextField();
        telField.setBounds(160, 150, 200, 26);
        getContentPane().add(telField);

        // "Şifre Gönder" butonunun oluşturulması
        JButton btnGonder = new JButton("Şifre Gönder");
        btnGonder.setBounds(160, 200, 150, 30);
        btnGonder.addActionListener(e -> {
            try {
                String tc = tcField.getText().trim();
                String ad = adField.getText().trim();
                String soyad = soyadField.getText().trim();
                String tel = telField.getText().trim();

                if (tc.isEmpty() || ad.isEmpty() || soyad.isEmpty() || tel.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tüm alanları doldurunuz!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File file = new File("hastalar.txt");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(null, "Kullanıcı kayıt dosyası bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                boolean userFound = false;
                String email = null;
                StringBuilder fileContent = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 7) {
                        continue; // Eksik bilgi varsa atla
                    }

                    String fileTc = parts[0].split(":")[1].trim();
                    String fileAd = parts[1].split(":")[1].trim();
                    String fileSoyad = parts[2].split(":")[1].trim();
                    String fileTel = parts[4].split(":")[1].trim();
                    String fileEmail = parts[5].split(":")[1].trim();

                    if (fileTc.equals(tc) && fileAd.equalsIgnoreCase(ad) && fileSoyad.equalsIgnoreCase(soyad) && fileTel.equals(tel)) {
                        userFound = true;
                        email = fileEmail;

                        // Yeni şifre oluştur
                        String yeniSifre = UUID.randomUUID().toString().substring(0, 8);

                        // Şifreyi güncelle
                        line = String.format("TC: %s, Ad: %s, Soyad: %s, Cinsiyet: %s, Cep Tel: %s, E-Posta: %s, Parola: %s",
                                fileTc, fileAd, fileSoyad, parts[3].split(":")[1].trim(), fileTel, fileEmail, yeniSifre);

                        // Yeni şifreyi e-posta ile gönder
                        sendEmail(email, yeniSifre);
                    }

                    fileContent.append(line).append(System.lineSeparator());
                }

                reader.close();

                if (userFound) {
                    // Dosyayı güncelle
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(fileContent.toString());
                    writer.close();

                    // Kullanıcıya başarı mesajı göster
                    JOptionPane.showMessageDialog(null, "Yeni şifre e-posta adresinize gönderildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

                    // Ana panele geçiş
                    new MainPanel().setVisible(true); // Ana panele geçiş
                    dispose();  // Şifremi Unuttum penceresini kapat

                    // Girilen değerleri temizle
                    tcField.setText("");
                    adField.setText("");
                    soyadField.setText("");
                    telField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Kullanıcı bilgileri bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        getContentPane().add(btnGonder);
    }

    // E-posta gönderme fonksiyonu
    private void sendEmail(String to, String yeniSifre) {
        try {
            String host = "smtp.gmail.com";
            String port = "587";
            final String username = "ozadahospital@gmail.com";
            final String password = "jtgv vlti jtmj asne";

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Yeni Şifreniz");
            message.setText("Sayın kullanıcı,\n\nYeni şifreniz: " + yeniSifre + "\n\nSağlıklı günler dileriz.");

            Transport.send(message);
            System.out.println("E-posta başarıyla gönderildi.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
