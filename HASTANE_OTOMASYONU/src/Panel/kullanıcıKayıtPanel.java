package Panel;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.*;

public class kullanıcıKayıtPanel extends JFrame {

    private static final long serialVersionUID = 1L;

    // Alanlar
    private JTextField tcField, adField, soyadField, telefonField, emailField;
    private JPasswordField parolaField;
    private JCheckBox erkekCheckBox, kadınCheckBox;

    // E-posta doğrulama regex deseni
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com)$";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                kullanıcıKayıtPanel frame = new kullanıcıKayıtPanel();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public kullanıcıKayıtPanel() {
        setResizable(false);
        setTitle("Kullanıcı Kayıt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 445, 500);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // TC Kimlik No Alanı
        JLabel tcLabel = new JLabel("TC Kimlik No:");
        tcLabel.setFont(new Font("Arial", Font.BOLD, 13));
        tcLabel.setBounds(30, 30, 100, 20);
        getContentPane().add(tcLabel);

        tcField = new JTextField();
        tcField.setBounds(140, 30, 200, 25);
        applyDigitFilter(tcField, 11);
        getContentPane().add(tcField);

        // Ad Alanı
        JLabel adLabel = new JLabel("Ad:");
        adLabel.setFont(new Font("Arial", Font.BOLD, 13));
        adLabel.setBounds(30, 70, 100, 20);
        getContentPane().add(adLabel);

        adField = new JTextField();
        adField.setBounds(140, 70, 200, 25);
        adField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != KeyEvent.VK_SPACE) {
                    e.consume();
                }
            }
        });
        getContentPane().add(adField);

        // Soyad Alanı
        JLabel soyadLabel = new JLabel("Soyad:");
        soyadLabel.setFont(new Font("Arial", Font.BOLD, 13));
        soyadLabel.setBounds(30, 110, 100, 20);
        getContentPane().add(soyadLabel);

        soyadField = new JTextField();
        soyadField.setBounds(140, 110, 200, 25);
        soyadField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != KeyEvent.VK_SPACE) {
                    e.consume();
                }
            }
        });
        getContentPane().add(soyadField);

        // Cinsiyet Alanı
        JLabel cinsiyetLabel = new JLabel("Cinsiyet:");
        cinsiyetLabel.setFont(new Font("Arial", Font.BOLD, 13));
        cinsiyetLabel.setBounds(30, 150, 100, 20);
        getContentPane().add(cinsiyetLabel);

        erkekCheckBox = new JCheckBox("Erkek");
        erkekCheckBox.setBounds(140, 150, 80, 25);
        kadınCheckBox = new JCheckBox("Kadın");
        kadınCheckBox.setBounds(230, 150, 80, 25);

        erkekCheckBox.addActionListener(e -> kadınCheckBox.setSelected(!erkekCheckBox.isSelected()));
        kadınCheckBox.addActionListener(e -> erkekCheckBox.setSelected(!kadınCheckBox.isSelected()));

        getContentPane().add(erkekCheckBox);
        getContentPane().add(kadınCheckBox);

        // Telefon Alanı
        JLabel telefonLabel = new JLabel("Cep Tel:");
        telefonLabel.setFont(new Font("Arial", Font.BOLD, 13));
        telefonLabel.setBounds(30, 190, 100, 20);
        getContentPane().add(telefonLabel);

        telefonField = new JTextField();
        telefonField.setBounds(140, 190, 200, 25);
        applyDigitFilter(telefonField, 10);
        getContentPane().add(telefonField);

        // E-posta Alanı
        JLabel emailLabel = new JLabel("E-Posta:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        emailLabel.setBounds(30, 230, 100, 20);
        getContentPane().add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(140, 230, 200, 25);
        emailField.setInputVerifier(new EmailVerifier());
        getContentPane().add(emailField);

        // Parola Alanı
        // Parola Alanı
        JLabel parolaLabel = new JLabel("Parola:");
        parolaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        parolaLabel.setBounds(30, 270, 100, 20);
        getContentPane().add(parolaLabel);

        parolaField = new JPasswordField();
        parolaField.setBounds(140, 270, 200, 25);
        getContentPane().add(parolaField);

        JButton parolaGosterButton = new JButton("Göster");
        parolaGosterButton.setBounds(350, 270, 80, 25);
        
        // UIManager ile güncelleme
        parolaGosterButton.addActionListener(e -> {
            if ("Göster".equals(parolaGosterButton.getText())) {
                parolaField.setEchoChar((char) 0); // Şifreyi göster
                parolaGosterButton.setText("Gizle");
            } else {
                // Varsayılan echo karakterini kullan
                parolaField.setEchoChar((Character) UIManager.get("PasswordField.echoChar")); 
                parolaGosterButton.setText("Göster");
            }
        });
        getContentPane().add(parolaGosterButton);


        // Kaydet Butonu
        JButton kaydetButton = new JButton("KAYDET");
        kaydetButton.setBounds(140, 320, 200, 30);
        kaydetButton.addActionListener(e -> handleKaydet());
        getContentPane().add(kaydetButton);
    }

    private void handleKaydet() {
        // Alanların doğruluğunu kontrol et
        if (tcField.getText().length() != 11) {
            JOptionPane.showMessageDialog(this, "TC Kimlik No 11 haneli olmalıdır!", "Hatalı TC Kimlik No", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (telefonField.getText().length() != 10) {
            JOptionPane.showMessageDialog(this, "Cep Telefonu 10 haneli olmalıdır!", "Hatalı Cep Telefonu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Pattern.matches(EMAIL_PATTERN, emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Geçerli bir e-posta adresi giriniz!", "Hatalı E-Posta", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (adField.getText().trim().isEmpty() || soyadField.getText().trim().isEmpty() || 
            parolaField.getPassword().length == 0 || (!erkekCheckBox.isSelected() && !kadınCheckBox.isSelected())) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm seçenekleri doldurunuz!", "Eksik Alan", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kayıt bilgilerini dosyaya yazma
        try {
            String cinsiyet = erkekCheckBox.isSelected() ? "Erkek" : "Kadın";
            String parola = new String(parolaField.getPassword());
            String hastaBilgisi = String.format("TC: %s, Ad: %s, Soyad: %s, Cinsiyet: %s, Cep Tel: %s, E-Posta: %s, Parola: %s%n",
                    tcField.getText(), adField.getText(), soyadField.getText(), cinsiyet, telefonField.getText(),
                    emailField.getText(), parola);

            // "hastalar.txt" dosyasına ekleme
            FileWriter writer = new FileWriter("hastalar.txt", true); // true parametresi ekleme modu için
            writer.write(hastaBilgisi);
            writer.close();

            JOptionPane.showMessageDialog(this, "Kayıt başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            clearFields();

            // Ana panele dön
            MainPanel mainPanel = new MainPanel();
            mainPanel.setVisible(true);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kayıt sırasında bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tcField.setText("");
        telefonField.setText("");
        adField.setText("");
        soyadField.setText("");
        emailField.setText("");
        parolaField.setText("");
        parolaField.setEchoChar('*');
        erkekCheckBox.setSelected(false);
        kadınCheckBox.setSelected(false);
    }

    private static class EmailVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String email = ((JTextField) input).getText();
            if (!Pattern.matches(EMAIL_PATTERN, email)) {
                JOptionPane.showMessageDialog(input, "Geçerli bir e-posta adresi giriniz!", "Hatalı Giriş", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
    }

    private void applyDigitFilter(JTextField textField, int maxLength) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d+") && fb.getDocument().getLength() + string.length() <= maxLength) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d+") && fb.getDocument().getLength() - length + text.length() <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
}