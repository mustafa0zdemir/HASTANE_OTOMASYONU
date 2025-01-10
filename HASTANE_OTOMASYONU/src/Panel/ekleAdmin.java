package Panel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import kullaniciBilgileriDoktor.doktorDepolama;
import kullaniciBilgileriDoktor.doktorInfo;

public class ekleAdmin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField tcField, adField, soyadField, cepTelField, emailField;
    private JPasswordField passwordField;
    private doktorDepolama doktorDepo = new doktorDepolama();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ekleAdmin frame = new ekleAdmin();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ekleAdmin() {
        setTitle("Admin Ekleme Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 473);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblTc = new JLabel("TC Kimlik No:");
        lblTc.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTc.setBounds(30, 30, 120, 16);
        getContentPane().add(lblTc);

        tcField = new JTextField();
        tcField.setBounds(160, 30, 160, 26);
        tcField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || tcField.getText().length() >= 11) e.consume();
            }
        });
        getContentPane().add(tcField);

        JLabel lblAd = new JLabel("Ad:");
        lblAd.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblAd.setBounds(30, 70, 120, 16);
        getContentPane().add(lblAd);

        adField = new JTextField();
        adField.setBounds(160, 70, 160, 26);
        adField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Harf ve boşluk dışında bir giriş yapılırsa tüket
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
        getContentPane().add(adField);

        JLabel lblSoyad = new JLabel("Soyad:");
        lblSoyad.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblSoyad.setBounds(30, 110, 120, 16);
        getContentPane().add(lblSoyad);

        soyadField = new JTextField();
        soyadField.setBounds(160, 110, 160, 26);
        soyadField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Harf dışında bir giriş yapılırsa tüket
                if (!Character.isLetter(c)) {
                    e.consume();
                }
            }
        });
        getContentPane().add(soyadField);

        JLabel lblCinsiyet = new JLabel("Cinsiyet:");
        lblCinsiyet.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblCinsiyet.setBounds(30, 150, 120, 16);
        getContentPane().add(lblCinsiyet);

        JCheckBox erkek = new JCheckBox("Erkek");
        JCheckBox kadin = new JCheckBox("Kadın");
        erkek.setBounds(160, 150, 75, 23);
        kadin.setBounds(240, 150, 75, 23);
        erkek.addActionListener(e -> kadin.setSelected(!erkek.isSelected()));
        kadin.addActionListener(e -> erkek.setSelected(!kadin.isSelected()));
        getContentPane().add(erkek);
        getContentPane().add(kadin);

        JLabel lblCepTel = new JLabel("Cep Tel:");
        lblCepTel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblCepTel.setBounds(30, 190, 120, 16);
        getContentPane().add(lblCepTel);

        cepTelField = new JTextField();
        cepTelField.setBounds(160, 190, 160, 26);
        cepTelField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || cepTelField.getText().length() >= 10) e.consume();
            }
        });
        getContentPane().add(cepTelField);

        JLabel lblEmail = new JLabel("E-posta:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblEmail.setBounds(30, 230, 120, 16);
        getContentPane().add(lblEmail);

        emailField = new JTextField();
        emailField.setBounds(160, 230, 160, 26);
        getContentPane().add(emailField);

        JLabel lblParola = new JLabel("Parola:");
        lblParola.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblParola.setBounds(30, 270, 120, 16);
        getContentPane().add(lblParola);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 270, 160, 26);
        getContentPane().add(passwordField);

        JLabel lblBolum = new JLabel("Bölüm:");
        lblBolum.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblBolum.setBounds(30, 310, 120, 16);
        getContentPane().add(lblBolum);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Dahiliye", "Genel Cerrahi", "Kardiyoloji"});
        comboBox.setBounds(160, 310, 160, 26);
        getContentPane().add(comboBox);

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.setBounds(50, 350, 120, 30);
        btnKaydet.addActionListener(e -> {
            try {
                String tc = tcField.getText();
                String ad = adField.getText();
                String soyad = soyadField.getText();
                String cinsiyet = erkek.isSelected() ? "Erkek" : kadin.isSelected() ? "Kadın" : "";
                String cepTel = cepTelField.getText();
                String email = emailField.getText();
                String parola = new String(passwordField.getPassword());
                String bolum = comboBox.getSelectedItem().toString();

                if (tc.isEmpty() || ad.isEmpty() || soyad.isEmpty() || cinsiyet.isEmpty() || cepTel.isEmpty() || email.isEmpty() || parola.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tc.length() != 11 || cepTel.length() != 10) {
                    JOptionPane.showMessageDialog(null, "TC veya Cep Tel uzunluğu geçersiz!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (doktorDepo.doktorVarMi(tc, cepTel, email)) {
                    JOptionPane.showMessageDialog(null, "Bu TC, Cep Tel veya E-posta adresi zaten kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                doktorInfo yeni = new doktorInfo(tc, ad, soyad, cinsiyet, cepTel, email, parola, bolum);
                doktorDepo.doktorEkle(yeni);

                File file = new File("doktorlar.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.write(String.format(
                        "TC: %s, Ad: %s, Soyad: %s, Cinsiyet: %s, Tel: %s, E-posta: %s, Bölüm: %s, Parola: %s%n",
                        tc, ad, soyad, cinsiyet, cepTel, email, bolum, parola
                    ));
                }


                JOptionPane.showMessageDialog(null, "Doktor kaydedildi ve dosyaya yazıldı.");

                tcField.setText("");
                adField.setText("");
                soyadField.setText("");
                cepTelField.setText("");
                emailField.setText("");
                passwordField.setText("");
                erkek.setSelected(false);
                kadin.setSelected(false);
                comboBox.setModel(new DefaultComboBoxModel(new String[] {"Dahiliye", "Genel Cerrahi", "Kardiyoloji", "Kadın Doğum ", "Nöroloji", "Psikiyatri", "Dermatoloji"}));
                comboBox.setSelectedIndex(0);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        getContentPane().add(btnKaydet);

        JButton btnListele = new JButton("Listele");
        btnListele.setBounds(200, 350, 120, 30);
        btnListele.addActionListener(e -> {
            try {
                File file = new File("doktorlar.txt");
                if (file.exists() && file.length() > 0) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                    JTextArea textArea = new JTextArea(content.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    JOptionPane.showMessageDialog(null, scrollPane, "Doktorlar", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Doktorlar dosyası boş veya mevcut değil.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        getContentPane().add(btnListele);

    
    JButton btnGeri = new JButton("Geri");
   btnGeri.setBounds(346, 351, 120, 30);
    getContentPane().add(btnGeri);
    btnGeri.addActionListener(e -> {
        adminPanel adminPanel = new adminPanel(); // Kullanıcı ana paneline yönlendirme
        adminPanel.setVisible(true);
        dispose(); // Mevcut pencereyi kapatma
    });
}
}











