package Panel;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import kullaniciBilgileriDoktor.SessionManagerD;
import kullaniciBilgileriHasta.KullaniciInfo;
import kullaniciBilgileriHasta.SessionManager;
//import javax.swing.JSplitPane;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
//import Helper.Helper.*;
import javax.swing.JPasswordField;

public class MainPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel giris_paneli;               // Ana panel
	private JTextField field_hasta_tc;         // Hasta T.C alanı
	private JPasswordField field_hasta_sifre;  // Hasta şifre alanı
	private JTextField field_doktor_tc;        // Doktor T.C alanı
	private JPasswordField field_doktor_sifre; // Doktor şifre alanı

	// Ana metod: Programın başlaması için ana pencereyi başlatır
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPanel frame = new MainPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Ana pencereyi oluşturur
	public MainPanel() {
		setResizable(false);
		setFont(new Font("Arial", Font.BOLD, 12));
		setTitle("KULLANICI GİRİŞ PANELİ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 700);
		
		setLocationRelativeTo(null); //açılan panelin bilgisayarın tam orta noktasında açılmasını sağlar 

		// Ana panel ayarları
		giris_paneli = new JPanel();
		giris_paneli.setBackground(new Color(231, 255, 250));
		giris_paneli.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(giris_paneli);
		giris_paneli.setLayout(null);
		
		// Logo ekleme
		JLabel logo = new JLabel(new ImageIcon(getClass().getResource("ozada.png")));
		logo.setBounds(174, 0, 323, 210);
		giris_paneli.add(logo);

		// Başlık etiketi
		JLabel lblNewLabel = new JLabel("HASTANE YÖNETİM SİSTEMİNE HOŞGELDİNİZ\n");
		lblNewLabel.setBounds(116, 272, 457, 33);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
		giris_paneli.add(lblNewLabel);

		// Sekme paneli ekleme
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 331, 688, 335);
		tabbedPane.setBackground(new Color(255, 255, 255));
		giris_paneli.add(tabbedPane);

		// Hasta Giriş Paneli
		JPanel hastaLogin = new JPanel();
		hastaLogin.setBackground(new Color(247, 248, 248));
		tabbedPane.addTab("Hasta Girişi", null, hastaLogin, null);
		hastaLogin.setLayout(null);

		// Hasta T.C etiketi ve alanı
		JLabel lblTcNumaras = new JLabel("T.C Numarası :\n");
		lblTcNumaras.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTcNumaras.setBounds(60, 40, 119, 33);
		hastaLogin.add(lblTcNumaras);

		field_hasta_tc = new JTextField();
		field_hasta_tc.setBounds(199, 44, 220, 26);
		field_hasta_tc.setColumns(10);
		hastaLogin.add(field_hasta_tc);
		
		 // T.C. numarasının sadece rakamlardan oluşmasını sağlamak ve uzunluğunu 11 ile sınırlamak için KeyListener ekliyoruz
        field_hasta_tc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Sadece rakamların girilmesini sağla
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                // T.C. numarasının uzunluğunu 11 karakterle sınırla
                if (field_hasta_tc.getText().length() >= 11) {
                    e.consume();
                }
            }
        });

		

		// Hasta Şifre etiketi ve alanı
		JLabel lblifre = new JLabel("Şifre :\n");
		lblifre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblifre.setBounds(61, 72, 119, 33);
		hastaLogin.add(lblifre);

		field_hasta_sifre = new JPasswordField();
		field_hasta_sifre.setBounds(199, 82, 220, 26);
		hastaLogin.add(field_hasta_sifre);

		// Hasta Giriş Butonu ve işlevi
		JButton Hasta_Giris_btn = new JButton("GİRİŞ YAP\n");
		Hasta_Giris_btn.setBackground(new Color(145, 145, 145));
		// Hasta Giriş Butonu ve işlevi
		// Hasta Giriş Butonu ve işlevi
		Hasta_Giris_btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String tc = field_hasta_tc.getText(); // Kullanıcıdan girilen T.C
		        @SuppressWarnings("deprecation")
		        String password = new String(field_hasta_sifre.getPassword()); // Kullanıcıdan girilen şifre

		        if (tc.isEmpty() || password.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurunuz.");
		        } else {
		            boolean valid = false; // Girişin geçerliliğini belirlemek için flag
		            String email = null; // Kullanıcının e-posta adresini tutar

		            try (BufferedReader reader = new BufferedReader(new FileReader("hastalar.txt"))) {
		                String line;
		                while ((line = reader.readLine()) != null) {
		                    String[] parts = line.split(", ");
		                    String fileTC = null;
		                    String filePassword = null;
		                    String fileEmail = null;

		                    for (String part : parts) {
		                        if (part.startsWith("TC: ")) {
		                            fileTC = part.substring(4).trim();
		                        }
		                        if (part.startsWith("Parola: ")) {
		                            filePassword = part.substring(8).trim();
		                        }
		                        if (part.startsWith("Email: ")) {
		                            fileEmail = part.substring(7).trim();
		                        }
		                    }

		                    // T.C ve şifre eşleşiyorsa bilgileri al
		                    if (tc.equals(fileTC) && password.equals(filePassword)) {
		                        KullaniciInfo currentUser = new KullaniciInfo(tc);
		                        SessionManager.setCurrentUser(currentUser);
		                        email = fileEmail; // E-posta adresini al
		                        valid = true;
		                        break;
		                    }
		                }

		                if (valid) {
		                    JOptionPane.showMessageDialog(null, "Giriş başarılı! E-posta: " + email);
		                    kullanıcıMain guiKullaniciMain = new kullanıcıMain();
		                    guiKullaniciMain.setVisible(true);
		                    dispose();
		                } else {
		                    JOptionPane.showMessageDialog(null, "Geçersiz T.C veya şifre.");
		                }
		            } catch (IOException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(null, "Dosya bulunamadı: hastalar.txt. Lütfen dosyanın doğru konumda olduğundan emin olun.");
		            }
		        }
		    }
		});

		Hasta_Giris_btn.setBounds(199, 130, 220, 52);
		hastaLogin.add(Hasta_Giris_btn);

		Hasta_Giris_btn.setBounds(199, 130, 220, 52);
		hastaLogin.add(Hasta_Giris_btn);
		
		JButton hasta_kayıt_btn = new JButton("KAYIT OL\n");
		hasta_kayıt_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("kayıt girişi başarılı");   // ********* Paneller arası geçiş yapacak fonksiyon **********
				
				
				kullanıcıKayıtPanel guiKullanici = new kullanıcıKayıtPanel(); // kullanıcı kayıt panel türünden nesne oluşturuldu 
				guiKullanici.setVisible(true); // kullanıcı kayıt panel görğnür kırıldı 
				dispose(); // ilk panel öldürüldü
				
			}
		});
		hasta_kayıt_btn.setBackground(new Color(145, 145, 145));
		hasta_kayıt_btn.setBounds(199, 208, 220, 52);
		hastaLogin.add(hasta_kayıt_btn);

		// Doktor Giriş Paneli
		JPanel doktorLogin = new JPanel();
		doktorLogin.setBackground(new Color(232, 232, 232));
		tabbedPane.addTab("Doktor Girişi", null, doktorLogin, null);
		doktorLogin.setLayout(null);

		// Doktor T.C etiketi ve alanı
		JLabel lblTcNumaras_1 = new JLabel("T.C Numarası :\n");
		lblTcNumaras_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTcNumaras_1.setBounds(61, 41, 119, 33);
		doktorLogin.add(lblTcNumaras_1);

		field_doktor_tc = new JTextField();
		field_doktor_tc.setColumns(10);
		field_doktor_tc.setBounds(199, 45, 220, 26);
		doktorLogin.add(field_doktor_tc);
		
		
		field_doktor_tc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Sadece rakamların girilmesini sağla
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                // T.C. numarasının uzunluğunu 11 karakterle sınırla
                if (field_doktor_tc.getText().length() >= 11) {
                    e.consume();
                }
            }
        });
		// Doktor Şifre etiketi ve alanı
		JLabel lblifre_1 = new JLabel("Şifre :\n");
		lblifre_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblifre_1.setBounds(61, 72, 119, 33);
		doktorLogin.add(lblifre_1);

		field_doktor_sifre = new JPasswordField();
		field_doktor_sifre.setBounds(199, 76, 220, 26);
		doktorLogin.add(field_doktor_sifre);
		
		// Doktor Şifre "Göster" Butonu
				JButton btnDoktorSifreGoster = new JButton("Göster");
				btnDoktorSifreGoster.addActionListener(new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent e) {
				        if (field_doktor_sifre.getEchoChar() != '\u0000') {
				            field_doktor_sifre.setEchoChar('\u0000'); // Şifreyi göster
				            btnDoktorSifreGoster.setText("Gizle");
				        } else {
				            field_doktor_sifre.setEchoChar((Character) UIManager.get("PasswordField.echoChar")); // Şifreyi gizle (UIManager.get : belirli bir bileşen veya özelliğin varsayılan değerini alır.)
				            btnDoktorSifreGoster.setText("Göster");
				        }
				    }
				});
				btnDoktorSifreGoster.setBounds(429, 76, 80, 26);
				doktorLogin.add(btnDoktorSifreGoster);

				// Doktor Giriş Butonu ve işlevi
				JButton doktor_giris_btn_1 = new JButton("GİRİŞ YAP\n");
				doktor_giris_btn_1.setBackground(new Color(145, 145, 145));
				doktor_giris_btn_1.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        String tc = field_doktor_tc.getText(); // Kullanıcıdan girilen T.C
				        @SuppressWarnings("deprecation")
				        String password = field_doktor_sifre.getText(); // Kullanıcıdan girilen şifre
				        
				        // T.C ve şifre alanlarının boş olup olmadığını kontrol et
				        if (tc.isEmpty() || password.isEmpty()) {
				            JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurunuz.");
				        } else {
				            boolean valid = false; // Girişin geçerliliğini belirlemek için flag

				            // T.C. ve şifreye özel admin kontrolü ekleyelim
				            if ("000".equals(tc) && "admin".equals(password)) {
				                // Admin paneline yönlendir
				                adminPanel adminPanel = new adminPanel(); // adminPanel adında yeni bir panel aç
				                adminPanel.setVisible(true);
				                dispose(); // Mevcut pencereyi kapat
				            } else {
				                // Dosyadan doktorları kontrol et
				                try (BufferedReader reader = new BufferedReader(new FileReader("doktorlar.txt"))) {
				                    String line;
				                    while ((line = reader.readLine()) != null) {
				                        // Her satırı ayrıştır ve T.C ile parolayı bul
				                        String[] parts = line.split(", ");
				                        String fileTC = null;
				                        String filePassword = null;

				                        for (String part : parts) {
				                            if (part.startsWith("TC: ")) {
				                                fileTC = part.substring(4).trim(); // "TC: " kısmını çıkar
				                            }
				                            if (part.startsWith("Parola: ")) {
				                                filePassword = part.substring(8).trim(); // "Parola: " kısmını çıkar
				                            }
				                        }

				                        // T.C ve şifre eşleşiyorsa geçerli kabul et
				                        if (tc.equals(fileTC) && password.equals(filePassword)) {
				                            valid = true;
				                            SessionManagerD.setCurrentDoctorTC(tc);

				                            break;
				                        }
				                    }

				                    // Giriş doğrulama sonucu
				                    if (valid) {
				                        // Geçerli giriş -> doktor paneline geç
				                        doktorPanel guiDoktorPanel = new doktorPanel();
				                        guiDoktorPanel.setVisible(true);
				                        dispose(); // Mevcut pencereyi kapat
				                    } else {
				                        JOptionPane.showMessageDialog(null, "Geçersiz T.C veya şifre.");
				                    }
				                } catch (IOException ex) {
				                    ex.printStackTrace();
				                    JOptionPane.showMessageDialog(null, "Bir hata oluştu. Lütfen tekrar deneyin.");
				                }
				            }
				        }
				    }
				});
				doktor_giris_btn_1.setBounds(199, 130, 220, 52);
				doktorLogin.add(doktor_giris_btn_1);

		// Hasta Şifre "Göster" Butonu
				JButton btnHastaSifreGoster = new JButton("Göster");
				btnHastaSifreGoster.addActionListener(new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent e) {
				        if (field_hasta_sifre.getEchoChar() != '\u0000') {
				            field_hasta_sifre.setEchoChar('\u0000'); // Şifreyi göster
				            btnHastaSifreGoster.setText("Gizle");
				        } else {
				            field_hasta_sifre.setEchoChar((Character) UIManager.get("PasswordField.echoChar")); // Şifreyi gizle (UIManager.get : belirli bir bileşen veya özelliğin varsayılan değerini alır.)
				            btnHastaSifreGoster.setText("Göster");
				        }
				    }
				});
				btnHastaSifreGoster.setBounds(429, 82, 80, 26);
				hastaLogin.add(btnHastaSifreGoster);
				
				JButton btnifremiUnuttum = new JButton("Şifremi Unuttum");
				btnifremiUnuttum.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						

						SifremiUnuttum SifremiUnuttum = new SifremiUnuttum(); 
						SifremiUnuttum.setVisible(true); // kullanıcı kayıt panel görğnür kırıldı 
						dispose(); // ilk panel öldürüldü
						
					}
				});
				
				
				
				
				btnifremiUnuttum.setForeground(Color.RED);
				btnifremiUnuttum.setBounds(519, 221, 129, 26);
				hastaLogin.add(btnifremiUnuttum);

		doktor_giris_btn_1.setBounds(199, 130, 220, 52);
		doktorLogin.add(doktor_giris_btn_1);

		doktor_giris_btn_1.setBounds(199, 130, 220, 52);
		doktorLogin.add(doktor_giris_btn_1);

		doktor_giris_btn_1.setBounds(199, 130, 220, 52);
		doktorLogin.add(doktor_giris_btn_1);
		
		JLabel lblNewLabel_1 = new JLabel("ÖZADA");
		lblNewLabel_1.setForeground(Color.GRAY);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 19));
		lblNewLabel_1.setBounds(304, 244, 80, 16);
		giris_paneli.add(lblNewLabel_1);
		
		JButton button = new JButton("New button");
		button.setBounds(284, 85, 117, 29);
		giris_paneli.add(button);
	}
}