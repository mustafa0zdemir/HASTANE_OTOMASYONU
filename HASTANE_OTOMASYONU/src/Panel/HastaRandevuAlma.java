package Panel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JCalendar;

import kullaniciBilgileriHasta.KullaniciInfo;
import kullaniciBilgileriHasta.SessionManager;

public class HastaRandevuAlma extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblSelectedTime;
    private JComboBox<String> comboBoxBrans;
    private JComboBox<String> comboBoxDoktor;
    private JCalendar calendar;

    private Map<String, ArrayList<String>> bransDoktorMap;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HastaRandevuAlma frame = new HastaRandevuAlma();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HastaRandevuAlma() {
        bransDoktorMap = new HashMap<>();
        loadDoctorsFromFile();

        setTitle("Randevu Alma");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Kullanıcı bilgisi etiketini kaldırdık.
        // lblKullaniciBilgi.setText("Kullanıcı: ");   // Bu satır kaldırılacak.

        JLabel lblBrans = new JLabel("Branş");
        lblBrans.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblBrans.setBounds(29, 50, 80, 28);
        contentPane.add(lblBrans);

        JLabel lblDoktor = new JLabel("Doktor");
        lblDoktor.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblDoktor.setBounds(29, 84, 80, 28);
        contentPane.add(lblDoktor);

        comboBoxBrans = new JComboBox<>();
        comboBoxBrans.setFont(new Font("Tahoma", Font.BOLD, 13));
        comboBoxBrans.setBounds(119, 55, 182, 21);
        contentPane.add(comboBoxBrans);
        comboBoxBrans.addItem("Branş seçiniz");
        bransDoktorMap.keySet().forEach(comboBoxBrans::addItem);
        comboBoxBrans.setSelectedIndex(0);
        comboBoxBrans.addActionListener(e -> updateDoctors());

        comboBoxDoktor = new JComboBox<>();
        comboBoxDoktor.setFont(new Font("Tahoma", Font.BOLD, 13));
        comboBoxDoktor.setModel(new DefaultComboBoxModel<>(new String[]{"Doktor seçiniz"}));
        comboBoxDoktor.setBounds(119, 89, 182, 21);
        contentPane.add(comboBoxDoktor);

        JLabel lblTarih = new JLabel("Tarih");
        lblTarih.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTarih.setBounds(29, 122, 80, 28);
        contentPane.add(lblTarih);

        calendar = new JCalendar();
        calendar.setBounds(119, 122, 300, 200);
        contentPane.add(calendar);

        JLabel lblSelectedDate = new JLabel("Seçilen Tarih: ");
        lblSelectedDate.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblSelectedDate.setForeground(Color.BLUE);
        lblSelectedDate.setBounds(119, 340, 300, 28);
        contentPane.add(lblSelectedDate);

        lblSelectedTime = new JLabel("Seçilen Saat: ");
        lblSelectedTime.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblSelectedTime.setForeground(Color.RED);
        lblSelectedTime.setBounds(119, 370, 300, 28);
        contentPane.add(lblSelectedTime);

        JButton btnGeri = new JButton("Geri");
        btnGeri.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnGeri.setBounds(358, 450, 182, 28);
        contentPane.add(btnGeri);
        btnGeri.addActionListener(e -> {
            kullanıcıMain guiKullaniciMainRandevu = new kullanıcıMain();
            guiKullaniciMainRandevu.setVisible(true);
            dispose();
        });

        JButton btnShowTimes = new JButton("Randevu Saatleri");
        btnShowTimes.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnShowTimes.setBounds(119, 410, 182, 28);
        contentPane.add(btnShowTimes);
        btnShowTimes.addActionListener(e -> showTimeSlots());

        JButton btnCreateAppointment = new JButton("Randevu Oluştur");
        btnCreateAppointment.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCreateAppointment.setBounds(119, 450, 182, 28);
        contentPane.add(btnCreateAppointment);
        btnCreateAppointment.addActionListener(e -> createAppointment());

        calendar.addPropertyChangeListener("calendar", e -> {
            Date selectedDate = calendar.getDate();
            Date today = new Date();
            Date maxDate = new Date(today.getTime() + (14L * 24 * 60 * 60 * 1000));

            if (selectedDate.before(today)) {
                JOptionPane.showMessageDialog(this, "Geçmiş bir tarihe randevu alınamaz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                calendar.setDate(today);
            } else if (selectedDate.after(maxDate)) {
                JOptionPane.showMessageDialog(this, "Randevu sadece 2 hafta ileriye alınabilir!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                calendar.setDate(maxDate);
            } else {
                lblSelectedDate.setText("Seçilen Tarih: " + new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
            }
        });
    }

    private void loadDoctorsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("doktorlar.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length < 7) continue; // Tüm gerekli alanların varlığını kontrol et.

                String department = null, doctorName = null, doctorSurname = null, doctorTC = null;
                for (String part : parts) {
                    if (part.startsWith("Bölüm: ")) department = part.split(": ")[1];
                    if (part.startsWith("Ad: ")) doctorName = part.split(": ")[1];
                    if (part.startsWith("Soyad: ")) doctorSurname = part.split(": ")[1];
                    if (part.startsWith("TC: ")) doctorTC = part.split(": ")[1];
                }

                if (department != null && doctorName != null && doctorSurname != null && doctorTC != null) {
                    bransDoktorMap.putIfAbsent(department, new ArrayList<>());
                    bransDoktorMap.get(department).add(doctorName + " " + doctorSurname + " (" + doctorTC + ")");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Doktor bilgileri yüklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    
    private void sendAppointmentConfirmationEmail(String email, String branch, String doctor, String date, String time) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("E-posta adresi boş veya null.");
            return;
        }

        String host = "smtp.gmail.com";
        String port = "587";
        final String username = "ozadahospital@gmail.com";
        final String password = "jtgv vlti jtmj asne"; // Güvenlik için şifreyi çevre değişkeninden alın.

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.debug", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Randevu Onayı");
            message.setText(String.format(
                "Sayın Kullanıcı,\n\nRandevunuz başarıyla oluşturulmuştur:\n\n"
                + "Branş: %s\nDoktor: %s\nTarih: %s\nSaat: %s\n\nSağlıklı günler dileriz.",
                branch, doctor, date, time
            ));

            Transport.send(message);
            System.out.println("E-posta başarıyla gönderildi.");
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "E-posta gönderilemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    
    
    
    
    
    
    
    
    

    private void updateDoctors() {
        String selectedBranch = (String) comboBoxBrans.getSelectedItem();
        if (bransDoktorMap.containsKey(selectedBranch)) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Doktor seçiniz");

            for (String doctorInfo : bransDoktorMap.get(selectedBranch)) {
                String doctorName = doctorInfo.split(" \\(")[0]; // Görünecek olan isim
                model.addElement(doctorName); 
            }
            comboBoxDoktor.setModel(model);
            comboBoxDoktor.setSelectedIndex(0);
        } else {
            comboBoxDoktor.setModel(new DefaultComboBoxModel<>(new String[]{"Doktor seçiniz"}));
        }
    }
    private String getSelectedDoctorTC() {
        String selectedBranch = (String) comboBoxBrans.getSelectedItem();
        String selectedDoctor = (String) comboBoxDoktor.getSelectedItem();

        if (bransDoktorMap.containsKey(selectedBranch)) {
            for (String doctorInfo : bransDoktorMap.get(selectedBranch)) {
                if (doctorInfo.startsWith(selectedDoctor + " ")) {
                    return doctorInfo.split("\\(")[1].replace(")", ""); // TC Kimlik Numarası
                }
            }
        }
        return null; // Seçim yapılamazsa null döner.
    }


    private void showTimeSlots() {
        String[] timeSlots = {"09:00", "11:00", "14:00", "16:00"};
        String selectedTime = (String) JOptionPane.showInputDialog(this, "Saat seçiniz", "Randevu Saatleri", JOptionPane.QUESTION_MESSAGE, null, timeSlots, timeSlots[0]);

        if (selectedTime != null) lblSelectedTime.setText("Seçilen Saat: " + selectedTime);
    }

    private void createAppointment() {
        String branch = (String) comboBoxBrans.getSelectedItem();
        String doctor = (String) comboBoxDoktor.getSelectedItem();
        String doctorTC = getSelectedDoctorTC();
        Date date = calendar.getDate();
        String time = lblSelectedTime.getText().replace("Seçilen Saat: ", "").trim();

        if ("Branş seçiniz".equals(branch) || "Doktor seçiniz".equals(doctor) || date == null || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        KullaniciInfo currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Kullanıcı bilgileri bulunamadı! Lütfen giriş yapınız.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        try (BufferedReader reader = new BufferedReader(new FileReader("randevular.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Doktor: " + doctor) && line.contains("Tarih: " + formattedDate) && line.contains("Saat: " + time)) {
                    JOptionPane.showMessageDialog(this, "Seçilen doktorun bu tarihte ve saatte başka bir randevusu var!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Randevu dosyası okunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileWriter writer = new FileWriter("randevular.txt", true)) {
            writer.write("T.C.: " + currentUser.tc + ", Branş: " + branch + ", Doktor: " + doctor + 
                         " (TC: " + doctorTC + "), Tarih: " + formattedDate + ", Saat: " + time + "\n");
            JOptionPane.showMessageDialog(this, "Randevunuz başarıyla oluşturuldu!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            // E-posta gönderimi
            sendAppointmentConfirmationEmail(currentUser.email, branch, doctor, formattedDate, time);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Randevu kaydedilemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }



}