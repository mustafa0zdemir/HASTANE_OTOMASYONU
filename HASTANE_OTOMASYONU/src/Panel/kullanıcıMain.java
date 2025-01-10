package Panel;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import kullaniciBilgileriHasta.SessionManager;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class kullanıcıMain extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea randevuBilgisi; // Randevu bilgisini göstermek için JTextArea
    private String kullaniciTC; // Giriş yapan kullanıcının TC'si

    // Giriş yapan kullanıcının TC'sini belirleyen metot
    public void setKullaniciTC(String tc) {
        this.kullaniciTC = tc;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    kullanıcıMain frame = new kullanıcıMain();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public kullanıcıMain() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 733, 423);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        setLocationRelativeTo(null);

        JPanel butonlar = new JPanel();
        butonlar.setBounds(32, 24, 306, 335);
        contentPane.add(butonlar);
        butonlar.setLayout(null);

        JLabel lblNewLabel = new JLabel("Randevuları Sırala");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(10, 29, 133, 25);
        butonlar.add(lblNewLabel);

        JComboBox<String> secmeSecenegi = new JComboBox<>();
        secmeSecenegi.setFont(new Font("Tahoma", Font.BOLD, 13));
        secmeSecenegi.setModel(new DefaultComboBoxModel<>(new String[]{"Sıralama Türü", "Doktora Göre", "Tarihe Göre"}));
        secmeSecenegi.setBounds(153, 31, 143, 21);
        butonlar.add(secmeSecenegi);

        JButton btnNewButton_2 = new JButton("Randevularımı görüntüle");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String secim = (String) secmeSecenegi.getSelectedItem();

                if ("Sıralama Türü".equals(secim)) {
                    randevuBilgisi.setText("Lütfen bir sıralama türü seçiniz.");
                } else {
                    // Get TC from the session manager
                    String tc = SessionManager.getCurrentUser().getTc(); // Get TC from current user

                    if (tc != null && !tc.trim().isEmpty()) {
                        // Retrieve appointments based on the selected sorting type and current TC
                        String[] randevular = randevulariGetir(secim, tc.trim());

                        // Display the appointments in JTextArea
                        StringBuilder sb = new StringBuilder();
                        for (String randevu : randevular) {
                            sb.append(randevu).append("\n");
                        }

                        randevuBilgisi.setText(sb.toString());
                    } else {
                        randevuBilgisi.setText("Geçerli bir TC kimlik numarası girmediniz.");
                    }
                }
            }
        });
        btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnNewButton_2.setBounds(26, 149, 198, 34);
        butonlar.add(btnNewButton_2);
        JButton btnNewButton = new JButton("Randevu Al");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HastaRandevuAlma guiKullaniciMain = new HastaRandevuAlma(); // kullanıcı kayıt panel türünden nesne oluşturuldu
                guiKullaniciMain.setVisible(true); // kullanıcı kayıt panel görünür hale getirildi
                dispose(); // ilk panel öldürüldü
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnNewButton.setBounds(26, 189, 198, 34);
        butonlar.add(btnNewButton);

        JButton btnBilgilerimiDzenle = new JButton("Bilgilerimi Düzenle");
        btnBilgilerimiDzenle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tc = JOptionPane.showInputDialog("TC Kimlik Numaranızı Giriniz:"); // Kullanıcıdan TC al
                if (tc != null && !tc.trim().isEmpty()) {
                    String[] bilgiler = hastaBilgisiGetir(tc); // Hasta bilgilerini al
                    if (bilgiler != null) {
                        // Kullanıcı bilgilerini düzenlemek için popup ekranını çağır
                        String[] yeniBilgiler = bilgilerimiDuzenlePopup(bilgiler);
                        if (yeniBilgiler != null) {
                            // Yeni bilgileri dosyaya kaydet
                            bilgilerDosyayaKaydet(tc, yeniBilgiler);
                            JOptionPane.showMessageDialog(contentPane, "Bilgiler başarıyla güncellendi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Kayıt bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnBilgilerimiDzenle.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnBilgilerimiDzenle.setBounds(26, 229, 198, 34);
        butonlar.add(btnBilgilerimiDzenle);

        // Randevu bilgisi JTextArea ve JScrollPane ekleniyor
        randevuBilgisi = new JTextArea();
        randevuBilgisi.setFont(new Font("Tahoma", Font.PLAIN, 13));
        randevuBilgisi.setEditable(false); // Kullanıcı tarafından düzenlenemez

        JScrollPane scrollPane = new JScrollPane(randevuBilgisi);
        scrollPane.setBounds(348, 24, 330, 335);
        contentPane.add(scrollPane);
    }

    

    // Hasta bilgilerini dosyadan çeken metot
    private String[] hastaBilgisiGetir(String tc) {
        try (BufferedReader reader = new BufferedReader(new FileReader("hastalar.txt"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] bilgiler = satir.split(", "); // Bilgileri ayır
                if (bilgiler[0].replace("TC: ", "").equals(tc)) { // TC eşleşmesini kontrol et
                    return bilgiler; // Bulunan bilgileri döndür
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Eşleşme bulunmazsa null döndür
    }

    // Kullanıcı bilgilerini düzenlemek için popup ekranı
    private String[] bilgilerimiDuzenlePopup(String[] bilgiler) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Mevcut bilgilerden değerleri al
        JTextField adField = new JTextField(bilgiler[1].replace("Ad: ", ""));
        JTextField soyadField = new JTextField(bilgiler[2].replace("Soyad: ", ""));
        
        // Cinsiyet için JComboBox
        String[] cinsiyetSecenekleri = {"Erkek", "Kadın"};
        JComboBox<String> cinsiyetComboBox = new JComboBox<>(cinsiyetSecenekleri);
        String mevcutCinsiyet = bilgiler[3].replace("Cinsiyet: ", "");
        cinsiyetComboBox.setSelectedItem(mevcutCinsiyet);

        JTextField telefonField = new JTextField(bilgiler[4].replace("Cep Tel: ", ""));
        JTextField emailField = new JTextField(bilgiler[5].replace("E-Posta: ", ""));
        JTextField parolaField = new JTextField(bilgiler[6].replace("Parola: ", ""));

        // Panel'e her bir alan için Label ve TextField/JComboBox ekleniyor
        panel.add(new JLabel("Ad:"));
        panel.add(adField);
        panel.add(new JLabel("Soyad:"));
        panel.add(soyadField);
        panel.add(new JLabel("Cinsiyet:"));
        panel.add(cinsiyetComboBox);
        panel.add(new JLabel("Cep Tel:"));
        panel.add(telefonField);
        panel.add(new JLabel("E-Posta:"));
        panel.add(emailField);
        panel.add(new JLabel("Parola:"));
        panel.add(parolaField);

        // Popup ekranı gösteriliyor
        int result = JOptionPane.showConfirmDialog(null, panel, "Bilgilerimi Düzenle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return new String[]{
                bilgiler[0], // TC değişmeyecek
                "Ad: " + adField.getText(),
                "Soyad: " + soyadField.getText(),
                "Cinsiyet: " + cinsiyetComboBox.getSelectedItem(),
                "Cep Tel: " + telefonField.getText(),
                "E-Posta: " + emailField.getText(),
                "Parola: " + parolaField.getText()
            };
        }
        return null; // Kullanıcı iptal ettiyse null döndür
    }


    // Güncellenen bilgileri dosyaya kaydetme
    private void bilgilerDosyayaKaydet(String tc, String[] yeniBilgiler) {
        try (BufferedReader reader = new BufferedReader(new FileReader("hastalar.txt"))) {
            StringBuilder yeniDosya = new StringBuilder();
            String satir;

            while ((satir = reader.readLine()) != null) {
                if (satir.contains("TC: " + tc)) {
                    // Eski bilgilerin yerine yenilerini koy
                    yeniDosya.append(String.join(", ", yeniBilgiler)).append("\n");
                } else {
                    yeniDosya.append(satir).append("\n");
                }
            }

            // Dosyayı güncelle
            try (java.io.FileWriter writer = new java.io.FileWriter("hastalar.txt")) {
                writer.write(yeniDosya.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(contentPane, "Bilgiler kaydedilirken hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Randevuları dosyadan okuma ve sıralama işlemi
 // Randevuları dosyadan okuma ve sıralama işlemi
 // Randevuları dosyadan okuma ve sıralama işlemi
    private String[] randevulariGetir(String sıralamaTürü, String tc) {
        if (tc == null || tc.trim().isEmpty()) {
            return new String[]{"Geçerli bir TC kimlik numarası bulunamadı."};
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("randevular.txt"))) {
            String satir;
            java.util.List<String> randevuListesi = new java.util.ArrayList<>();

            // Kullanıcının randevularını dosyadan okuma
            while ((satir = reader.readLine()) != null) {
                System.out.println("Okunan satır: " + satir); // Debug için
                if (satir.contains("T.C.: " + tc)) {
                    String[] parcalar = satir.split(", "); // Satırı parçalarına ayır
                    
                    // Gerekli bilgileri ayıklama
                    String branş = parcalar[1].replace("Branş: ", "");
                    String doktor = parcalar[2].replace("Doktor: ", "").split(" \\(")[0]; // Doktor adını ayıkla
                    String tarih = parcalar[3].replace("Tarih: ", "");
                    String saat = parcalar[4].replace("Saat: ", "");

                    // İlgili bilgileri listeye ekle
                    randevuListesi.add("Branş: " + branş + ", Doktor: " + doktor + ", Tarih: " + tarih + ", Saat: " + saat);
                }
            }

            if (randevuListesi.isEmpty()) {
                return new String[]{"Randevunuz bulunmamaktadır."};
            }

            // Sıralama türüne göre sıralama
            if ("Doktora Göre".equals(sıralamaTürü)) {
                randevuListesi.sort((r1, r2) -> {
                    String doktor1 = r1.split(", Doktor: ")[1].split(", ")[0];
                    String doktor2 = r2.split(", Doktor: ")[1].split(", ")[0];
                    return doktor1.compareTo(doktor2);
                });
            } else if ("Tarihe Göre".equals(sıralamaTürü)) {
                randevuListesi.sort((r1, r2) -> {
                    String tarihSaat1 = r1.split(", Tarih: ")[1];
                    String tarih1 = tarihSaat1.split(", Saat: ")[0];
                    String saat1 = tarihSaat1.split(", Saat: ")[1];
                    
                    String tarihSaat2 = r2.split(", Tarih: ")[1];
                    String tarih2 = tarihSaat2.split(", Saat: ")[0];
                    String saat2 = tarihSaat2.split(", Saat: ")[1];

                    // Önce tarih, ardından saat sıralaması
                    int tarihKıyas = tarih1.compareTo(tarih2);
                    if (tarihKıyas == 0) {
                        return saat1.compareTo(saat2);
                    }
                    return tarihKıyas;
                });
            }

            return randevuListesi.toArray(new String[0]);

        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{"Randevular okunurken bir hata oluştu."};
        }
    }


}