package Panel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class duzenleAdmin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JList<String> doktorListesi;
    private DefaultListModel<String> listModel;
    private String selectedDoktor;
    private JTextField tcField, adField, soyadField, cepTelField, emailField, cinsiyetField;
    private JPasswordField parolaField;
    private JComboBox<String> bolumComboBox;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    duzenleAdmin frame = new duzenleAdmin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public duzenleAdmin() {
        setTitle("Doktor Düzenle Paneli");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 665, 550);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // JList için model oluşturuluyor
        listModel = new DefaultListModel<>();
        doktorListesi = new JList<>(listModel);
        doktorListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // List selection listener ekliyoruz, kullanıcı bir doktoru seçtiğinde
        doktorListesi.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedDoktor = doktorListesi.getSelectedValue();
                    if (selectedDoktor != null) {
                        loadDoktorBilgileri(selectedDoktor);
                    }
                }
            }
        });

        // JScrollPane ekliyoruz ve JList'i buna koyuyoruz
        JScrollPane scrollPane = new JScrollPane(doktorListesi);
        scrollPane.setBounds(30, 30, 583, 200);
        contentPane.add(scrollPane);

        // TC, Ad, Soyad, Cinsiyet gibi alanları ekleyelim
        JLabel lblTc = new JLabel("TC:");
        lblTc.setBounds(30, 250, 50, 20);
        contentPane.add(lblTc);
        tcField = new JTextField();
        tcField.setBounds(100, 250, 200, 20);
        contentPane.add(tcField);

        JLabel lblAd = new JLabel("Ad:");
        lblAd.setBounds(30, 280, 50, 20);
        contentPane.add(lblAd);
        adField = new JTextField();
        adField.setBounds(100, 280, 200, 20);
        contentPane.add(adField);

        JLabel lblSoyad = new JLabel("Soyad:");
        lblSoyad.setBounds(30, 310, 50, 20);
        contentPane.add(lblSoyad);
        soyadField = new JTextField();
        soyadField.setBounds(100, 310, 200, 20);
        contentPane.add(soyadField);

        JLabel lblCinsiyet = new JLabel("Cinsiyet:");
        lblCinsiyet.setBounds(30, 340, 50, 20);
        contentPane.add(lblCinsiyet);
        cinsiyetField = new JTextField();
        cinsiyetField.setBounds(100, 340, 200, 20);
        contentPane.add(cinsiyetField);

        JLabel lblCepTel = new JLabel("Tel:");
        lblCepTel.setBounds(30, 370, 50, 20);
        contentPane.add(lblCepTel);
        cepTelField = new JTextField();
        cepTelField.setBounds(100, 370, 200, 20);
        contentPane.add(cepTelField);

        JLabel lblEmail = new JLabel("E-posta:");
        lblEmail.setBounds(30, 400, 58, 20);
        contentPane.add(lblEmail);
        emailField = new JTextField();
        emailField.setBounds(100, 400, 200, 20);
        contentPane.add(emailField);

        JLabel lblBolum = new JLabel("Bölüm:");
        lblBolum.setBounds(30, 430, 50, 20);
        contentPane.add(lblBolum);
        bolumComboBox = new JComboBox<>(new String[]{"Dahiliye", "Genel Cerrahi", "Kardiyoloji"});
        bolumComboBox.setBounds(100, 430, 200, 20);
        contentPane.add(bolumComboBox);

        // Parola Alanı
        JLabel lblParola = new JLabel("Parola:");
        lblParola.setBounds(30, 460, 50, 20);
        contentPane.add(lblParola);
        parolaField = new JPasswordField();
        parolaField.setBounds(100, 460, 200, 20);
        contentPane.add(parolaField);

        // Düzenle butonu
        JButton btnDuzenle = new JButton("Düzenle");
        btnDuzenle.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnDuzenle.setBounds(320, 250, 120, 30);
        btnDuzenle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedDoktor != null) {
                    updateDoktorBilgileri(selectedDoktor);
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen düzenlemek için bir doktor seçin.", "Hata", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        contentPane.add(btnDuzenle);

        // Geri Butonu
        JButton btnGeri = new JButton("Geri");
        btnGeri.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnGeri.setBounds(320, 290, 120, 30);
        btnGeri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminPanel adminPaneli = new adminPanel();
                adminPaneli.setVisible(true);
                dispose(); // Bu paneli kapat
            }
        });
        contentPane.add(btnGeri);

        // Başlangıçta doktorları dosyadan yükle
        loadDoktorlar();
    }

    // Doktorları dosyadan yükleyen metot
    private void loadDoktorlar() {
        try {
            listModel.clear(); // Listeyi temizle
            BufferedReader reader = new BufferedReader(new FileReader("doktorlar.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line); // Her doktoru listeye ekle
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Doktorlar yüklenemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Seçilen doktorun bilgilerini ekrana yükleyen metot
    private void loadDoktorBilgileri(String doktor) {
        String[] doktorBilgileri = doktor.split(", ");
        tcField.setText(doktorBilgileri[0].split(": ")[1]);
        adField.setText(doktorBilgileri[1].split(": ")[1]);
        soyadField.setText(doktorBilgileri[2].split(": ")[1]);
        cinsiyetField.setText(doktorBilgileri[3].split(": ")[1]);
        cepTelField.setText(doktorBilgileri[4].split(": ")[1]);
        emailField.setText(doktorBilgileri[5].split(": ")[1]);
        bolumComboBox.setSelectedItem(doktorBilgileri[6].split(": ")[1]);
        parolaField.setText(doktorBilgileri[7].split(": ")[1]);
    }

    // Doktorun bilgilerini güncelleyen metot
    private void updateDoktorBilgileri(String eskiDoktor) {
        String yeniDoktor = "TC: " + tcField.getText() + ", Ad: " + adField.getText() + ", Soyad: " + soyadField.getText() +
                             ", Cinsiyet: " + cinsiyetField.getText() + ", Tel: " + cepTelField.getText() + ", E-posta: " + emailField.getText() + 
                             ", Bölüm: " + bolumComboBox.getSelectedItem() + ", Parola: " + new String(parolaField.getPassword());

        try {
            File inputFile = new File("doktorlar.txt");
            File tempFile = new File("doktorlar_temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(eskiDoktor)) {
                    writer.write(yeniDoktor + System.lineSeparator());
                } else {
                    writer.write(currentLine + System.lineSeparator());
                }
            }
            writer.close();
            reader.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile); // Eski dosyayı sil ve yeni dosyayı ismiyle değiştir
            }

            loadDoktorlar(); // Listeleri tekrar yükle
            JOptionPane.showMessageDialog(null, "Doktor bilgileri başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Doktor bilgileri güncellenemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
