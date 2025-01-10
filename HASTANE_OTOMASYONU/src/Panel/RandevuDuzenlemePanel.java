package Panel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JCalendar;

public class RandevuDuzenlemePanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RandevuDuzenlemePanel frame = new RandevuDuzenlemePanel();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public RandevuDuzenlemePanel() {
        setTitle("Randevu Güncelleme");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setLocationRelativeTo(null);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Branş Label
        JLabel lblBrans = new JLabel("Branş");
        lblBrans.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblBrans.setBounds(29, 50, 80, 28);
        contentPane.add(lblBrans);

        // Doktor Label
        JLabel lblDoktor = new JLabel("Doktor");
        lblDoktor.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblDoktor.setBounds(29, 84, 80, 28);
        contentPane.add(lblDoktor);

        // Branş ComboBox
        JComboBox<String> comboBoxBrans = new JComboBox<>();
        comboBoxBrans.setModel(new DefaultComboBoxModel<>(new String[] {
            "Branş seçiniz",
            "Dahiliye",
            "Genel Cerrahi",
            "Kadın Hastalıkları ve Doğum",
            "Kardiyoloji",
            "Kulak Burun Boğaz",
            "Nöroloji",
            "Ortopedi"
        }));
        comboBoxBrans.setFont(new Font("Tahoma", Font.BOLD, 13));
        comboBoxBrans.setBounds(119, 55, 182, 21);
        contentPane.add(comboBoxBrans);

        // Doktor ComboBox
        JComboBox<String> comboBoxDoktor = new JComboBox<>();
        comboBoxDoktor.setFont(new Font("Tahoma", Font.BOLD, 13));
        comboBoxDoktor.setModel(new DefaultComboBoxModel<>(new String[] { "Doktor seçiniz" }));
        comboBoxDoktor.setBounds(119, 89, 182, 21);
        contentPane.add(comboBoxDoktor);

        // JCalendar ile tarih seçimi
        JLabel lblTarih = new JLabel("Tarih");
        lblTarih.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTarih.setBounds(29, 122, 80, 28);
        contentPane.add(lblTarih);

        JCalendar calendar = new JCalendar();
        calendar.setBounds(119, 122, 300, 200);
        contentPane.add(calendar);

        // Seçilen Tarihi Gösteren JLabel
        JLabel lblSelectedDate = new JLabel("Seçilen Tarih: ");
        lblSelectedDate.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblSelectedDate.setForeground(Color.BLUE);
        lblSelectedDate.setBounds(119, 340, 300, 28);
        contentPane.add(lblSelectedDate);

        // Randevuyu Güncelle Button
        JButton btnNewButton = new JButton("Randevuyu Güncelle");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnNewButton.setBounds(119, 380, 182, 28);
        contentPane.add(btnNewButton);

        // Calendar Seçimi Değiştiğinde JLabel'i Güncelle
        calendar.addPropertyChangeListener("calendar", e -> {
            Date selectedDate = calendar.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            lblSelectedDate.setText("Seçilen Tarih: " + sdf.format(selectedDate));
        });

        // Button'a Tıklanıldığında Seçilen Tarihi Göster
        btnNewButton.addActionListener(e -> {
            Date selectedDate = calendar.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String selectedDateStr = sdf.format(selectedDate);

            // Seçilen tarihi bir mesaj kutusunda gösterme
            JOptionPane.showMessageDialog(null, "Seçilen tarih: " + selectedDateStr, "Seçilen Tarih", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
