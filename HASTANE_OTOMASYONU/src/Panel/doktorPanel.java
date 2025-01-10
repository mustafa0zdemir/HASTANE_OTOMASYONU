package Panel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import kullaniciBilgileriDoktor.SessionManagerD;

public class doktorPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel hosgeldiniz_lbl;
    private JButton btnCikisYap, btnListele, btnMuayeneEt;
    private JList<String> hastaListesi;
    private DefaultListModel<String> hastaModeli;
    private List<String> hastalar;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    doktorPanel frame = new doktorPanel();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public doktorPanel() {
        setResizable(false);
        setTitle("Doktor Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 600);

        setLocationRelativeTo(null); // Panelin tam ortada açılması

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        hosgeldiniz_lbl = new JLabel("Hoşgeldiniz Sayın Doktor");
        hosgeldiniz_lbl.setFont(new Font("Tahoma", Font.BOLD, 13));
        hosgeldiniz_lbl.setBounds(23, 20, 200, 16);
        contentPane.add(hosgeldiniz_lbl);

        btnCikisYap = new JButton("Çıkış Yap");
        btnCikisYap.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCikisYap.setBounds(475, 15, 117, 29);
        contentPane.add(btnCikisYap);

        btnListele = new JButton("Hastaları Listele");
        btnListele.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnListele.setBounds(23, 60, 150, 29);
        contentPane.add(btnListele);

        btnMuayeneEt = new JButton("Muayene Et");
        btnMuayeneEt.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnMuayeneEt.setBounds(200, 60, 150, 29);
        contentPane.add(btnMuayeneEt);

        hastaModeli = new DefaultListModel<>();
        hastaListesi = new JList<>(hastaModeli);
        JScrollPane scrollPane = new JScrollPane(hastaListesi);
        scrollPane.setBounds(23, 100, 569, 400);
        contentPane.add(scrollPane);

        hastalar = new ArrayList<>();
     // Giriş yapan doktorun TC'sini al
        String doktorTC = SessionManagerD.getCurrentDoctorTC();
        System.out.println("Giriş yapan doktorun TC'si: " + doktorTC);

        try (BufferedReader reader = new BufferedReader(new FileReader("randevular.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Use regular expression to extract doctor's TC from the line
                Pattern pattern = Pattern.compile("Doktor: (.*?\\(TC: (\\d+)\\))");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    // Extract the doctor's TC from the match
                    String extractedDoktorTC = matcher.group(2);
                    if (extractedDoktorTC.equals(doktorTC)) {
                        hastalar.add(line);  // Add the line if it matches the doctor's TC
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Print the result (for debugging)
        for (String randevu : hastalar) {
            System.out.println(randevu);
        }

        // Çıkış yapma işlevi
        btnCikisYap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Hasta listesini güncelleme işlevi
        btnListele.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hastaModeli.clear();
                if (hastalar.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Herhangi bir randevunuz bulunmamaktadır.");
                } else {
                    for (String hasta : hastalar) {
                        hastaModeli.addElement(hasta);
                    }
                }
            }
        });

        // Muayene edilen hastayı listeden ve dosyadan çıkarma işlevi
        btnMuayeneEt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = hastaListesi.getSelectedIndex();
                if (selectedIndex != -1) {
                    String muayeneEdilenHasta = hastaModeli.get(selectedIndex);

                    // Listeden çıkar
                    hastaModeli.remove(selectedIndex);
                    hastalar.remove(muayeneEdilenHasta);

                    // Dosyadan sil
                    try {
                        removeLineFromFile("randevular.txt", muayeneEdilenHasta);
                        JOptionPane.showMessageDialog(null, muayeneEdilenHasta + " muayene edildi ve listeden/dosyadan çıkarıldı.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Hata: Dosya güncellenemedi.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen muayene edilen hastayı seçin.");
                }
            }
        });
    }

    private void removeLineFromFile(String filePath, String lineToRemove) throws IOException {
        File inputFile = new File(filePath);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                // Satırı dosyaya yalnızca eşleşmiyorsa yaz
                if (!currentLine.trim().equals(lineToRemove.trim())) {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
        }

        // Orijinal dosyayı sil ve geçici dosyayı yeniden adlandır
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Dosya işlemi başarısız.");
        }
    }
}
