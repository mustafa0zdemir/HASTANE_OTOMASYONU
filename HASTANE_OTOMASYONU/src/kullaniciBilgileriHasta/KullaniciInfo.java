package kullaniciBilgileriHasta;

import kullaniciBilgileriDoktor.doktorInfo;

public class KullaniciInfo {
	public String tc;
	public String ad;
	public String soyad;
    String cinsiyet;
    String cepTel;
    public String email;
    String parola;
    doktorInfo sonraki; 
    doktorInfo onceki;
    
    public KullaniciInfo(String tc, String ad, String soyad, String cinsiyet, String cepTel, String email, String parola) {
    	this.tc = tc;
    	this.ad = ad;
        this.soyad = soyad;
        this.cinsiyet = cinsiyet;
        this.cepTel = cepTel;
        this.email = email;
        this.parola = parola;
        this.sonraki = null;
        this.onceki = null;
    

    }
    public KullaniciInfo(String tc) {
		this.tc = tc;


	}
    
    
    
    
    
	public String getTc() {
		// TODO Auto-generated method stub
		return tc;
	}
}
