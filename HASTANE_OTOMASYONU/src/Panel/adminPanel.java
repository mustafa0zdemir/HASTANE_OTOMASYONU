package Panel;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kullaniciBilgileriDoktor.doktorDepolama;
//import kullaniciBilgileriDoktor.doktorInfo;
import kullaniciBilgileriDoktor.doktorInfo;

public class adminPanel extends JFrame {
	
	//private Map<String, String> hastalarMap = new HashMap<>(); // 
	//private Map<String, List<String>> randevularMap = new HashMap<>();
    
	
	
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private doktorDepolama doktorDepo; // doktorDepolama nesnesi

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    adminPanel frame = new adminPanel();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public adminPanel() {
        setResizable(false);
        setTitle("Admin Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 487, 400);
        
    	setLocationRelativeTo(null); // panelin tam ortada açılmasını sağlar
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // doktorDepolama nesnesini başlat
        doktorDepo = new doktorDepolama();
        
        // HashMap veri yapısı: Anahtar: Ad Soyad, Değer: Randevu Listesi
        //Map<String, List<String>> randevuMap = new HashMap<>();
       // dosyadakiVeriyiHashMapeAktar("hastalar.txt", "randevular.txt");

        // Ekle Butonu
        JButton btnEkle = new JButton("Ekle");
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ekleAdmin ekleAdminPaneli = new ekleAdmin();
                ekleAdminPaneli.setVisible(true);
                dispose(); // Admin panelini kapat
            }
        });
        btnEkle.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnEkle.setBounds(132, 87, 232, 32);
        contentPane.add(btnEkle);

        // Düzenle Butonu
        JButton btnDzenle = new JButton("Düzenle");
        btnDzenle.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnDzenle.setBounds(132, 168, 232, 32);
        contentPane.add(btnDzenle);
        btnDzenle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                duzenleAdmin duzenleAdmin = new duzenleAdmin();
                duzenleAdmin.setVisible(true);
                dispose(); // Admin panelini kapat
            }
        });

        // Sil Butonu
        JButton btnSil = new JButton("Sil");
        btnSil.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnSil.setBounds(132, 129, 232, 32);
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                silAdmin silAdminPaneli = new silAdmin();
                silAdminPaneli.setVisible(true);
                dispose(); // Admin panelini kapat
            }
        });
        contentPane.add(btnSil);
        
        
     // Doktorları Listele Butonu
        JButton btnDoktorlarListele = new JButton("Doktorları Listele");
        btnDoktorlarListele.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // İlk olarak dosyadaki verileri bağlı listeye aktar
                doktorDepo.dosyadanVeriEkle("doktorlar.txt"); // Dosyadan bağlı listeye veri aktarımı

                // Yeni bir JFrame ile listeleme paneli oluştur
                JFrame listeFrame = new JFrame("Doktor Listesi");
                listeFrame.setSize(500, 400);
                listeFrame.setLocationRelativeTo(null); // Ortada açılması için

                // JTextArea içine doktorları listele
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false); // Kullanıcı tarafından düzenlenemez
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                // Doktor bilgilerini JTextArea'ya ekle
                StringBuilder doktorBilgileri = new StringBuilder();
                doktorBilgileri.append("Doktor Listesi (Bağlı Liste):\n");

                // Bağlı listeden bilgileri al
                if (doktorDepo.bosMu()) {
                    doktorBilgileri.append("Doktor listesi bağlı listede boş.\n");
                } else {
                    doktorInfo current = doktorDepo.on;
                    while (current != null) {
                        doktorBilgileri.append("TC: ").append(current.tc)
                                .append(", Ad: ").append(current.ad)
                                .append(", Soyad: ").append(current.soyad)
                                .append(", Cinsiyet: ").append(current.cinsiyet)
                                .append(", Tel: ").append(current.cepTel)
                                .append(", E-posta: ").append(current.email)
                                .append(", Parola: ").append(current.parola) 
                                .append(", Bölüm: ").append(current.bolum)
           
                                .append("\n");
                        current = current.sonraki;
                    }
                }

                textArea.setText(doktorBilgileri.toString());

                // Paneli ayarla ve göster
                listeFrame.getContentPane().add(scrollPane);
                listeFrame.setVisible(true);
            }
        });
        btnDoktorlarListele.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnDoktorlarListele.setBounds(132, 209, 232, 32);
        contentPane.add(btnDoktorlarListele);
        
        
        
     // Sorgulama Butonu
        JButton btnSorgulama = new JButton("Sorgulama");
        btnSorgulama.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Doktor ad-soyadına göre sorgulama
                String doktorAdSoyad = JOptionPane.showInputDialog("Doktor Ad-Soyadı:");
                if (doktorAdSoyad != null && !doktorAdSoyad.trim().isEmpty()) {
                    doktorAdSoyad = doktorAdSoyad.trim();
                    List<String> randevular = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new FileReader("randevular.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("Doktor: " + doktorAdSoyad)) {
                                String randevuDetaylari = line.substring(line.indexOf(",") + 1).trim();
                                randevular.add(randevuDetaylari);
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Sonuçları göster
                    if (randevular.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Bu doktor için randevu bulunamadı.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder randevuBilgileri = new StringBuilder("Randevular:\n");
                        for (String randevu : randevular) {
                            randevuBilgileri.append(randevu).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, randevuBilgileri.toString(), "Randevu Bilgileri", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Doktor Ad-Soyadı boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSorgulama.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnSorgulama.setBounds(132, 244, 232, 32);
        contentPane.add(btnSorgulama);

/*
    // Dosyadaki veriyi HashMap'e aktar
 // Dosyadaki veriyi HashMap'e aktar
    public void dosyadakiVeriyiHashMapeAktar(String hastalarDosyasi, String randevularDosyasi) {
        try {
            // Hastalar dosyasını oku ve hastaların ad soyad -> TC numarası eşleşmesini yap
            BufferedReader reader = new BufferedReader(new FileReader(hastalarDosyasi));
            String line;
            while ((line = reader.readLine()) != null) {
                // Satırı parse et
                String[] parts = line.split(", ");
                if (parts.length >= 2) {
                    String adSoyad = parts[1].replace("Ad: ", "").replace("Soyad: ", "").trim();
                    String tc = parts[0].replace("T.C.: ", "").trim();
                    hastalarMap.put(adSoyad, tc);
                }
            }
            reader.close();

            // Randevular dosyasını oku ve TC -> Randevu bilgileri eşleşmesini yap
            reader = new BufferedReader(new FileReader(randevularDosyasi));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("T.C.:")) {
                    // TC numarasını ayıkla
                    String tc = line.split(", ")[0].replace("T.C.: ", "").trim();
                    // Randevu detaylarını al
                    String randevuDetaylari = line.substring(line.indexOf(",") + 1).trim();
                    // TC'ye göre randevuyu listeye ekle
                    randevularMap.computeIfAbsent(tc, k -> new ArrayList<>()).add(randevuDetaylari);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
     
        
        }
    
    */
    }}
    

    


