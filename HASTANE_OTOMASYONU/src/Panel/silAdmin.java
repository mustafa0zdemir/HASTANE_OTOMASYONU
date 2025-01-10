package Panel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class silAdmin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JList<String> doktorListesi;
    private DefaultListModel<String> listModel;
    private String selectedDoktor;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    silAdmin frame = new silAdmin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public silAdmin() {
        setTitle("Doktor Silme Paneli");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 665, 340);
        
        // Pencerenin ekranın ortasında açılmasını sağlamak için
        setLocationRelativeTo(null); // Bu satır eklendi
        
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
                }
            }
        });

        // JScrollPane ekliyoruz ve JList'i buna koyuyoruz
        JScrollPane scrollPane = new JScrollPane(doktorListesi);
        scrollPane.setBounds(30, 30, 583, 200); // Scroll panelinin boyutları
        contentPane.add(scrollPane);

        // Silme butonu
        JButton btnSil = new JButton("Sil");
        btnSil.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnSil.setBounds(117, 251, 120, 30);
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedDoktor != null) {
                    // Dosya okuma ve silme işlemi
                    try {
                        File inputFile = new File("doktorlar.txt");
                        File tempFile = new File("doktorlar_temp.txt");
                        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                        String currentLine;
                        while ((currentLine = reader.readLine()) != null) {
                            if (!currentLine.contains(selectedDoktor)) {
                                writer.write(currentLine + System.lineSeparator());
                            }
                        }
                        writer.close();
                        reader.close();

                        // Eski dosyayı silip yeni dosyayı yeniden adlandırma
                        if (inputFile.delete()) {
                            tempFile.renameTo(inputFile);
                        }

                        JOptionPane.showMessageDialog(null, "Doktor başarıyla silindi.");
                        loadDoktorlar(); // Listeyi güncelle
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Silme işlemi sırasında hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silmek için bir doktor seçin.", "Hata", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        contentPane.add(btnSil);
        
        // Geri Butonu
        JButton btnGeri = new JButton("Geri");
        btnGeri.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnGeri.setBounds(247, 251, 120, 30);
        btnGeri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Admin panelini açıyoruz ve bu paneli kapatıyoruz
                adminPanel adminPaneli = new adminPanel();
                adminPaneli.setVisible(true);
                dispose(); // Doktor silme panelini kapat
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
}