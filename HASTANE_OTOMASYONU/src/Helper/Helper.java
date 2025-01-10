package Helper;

import javax.swing.JOptionPane;

public class Helper {
	
	public static void mesajGoster(String str) {
		String mesaj;
		switch(mesaj){
		case  "hepsi dolu mu":
			mesaj = "Lütfen tüm alanları doldurnuz.";
			break;
			default:
				mesaj = str;
			
		}
		JOptionPane.showMessageDialog(null,mesaj,"Mesaj",JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	
	

}
