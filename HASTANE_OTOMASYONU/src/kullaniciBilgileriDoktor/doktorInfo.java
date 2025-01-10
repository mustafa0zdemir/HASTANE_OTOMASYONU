package kullaniciBilgileriDoktor;



public class doktorInfo {
	public String tc;
	public String ad;
	public String soyad;
	public String cinsiyet;
	public String cepTel;
	public String email;
	public String parola;
	public String bolum;
    public doktorInfo sonraki; 
    public doktorInfo onceki;

    public String getTc() {
		return tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public String getSoyad() {
		return soyad;
	}

	public void setSoyad(String soyad) {
		this.soyad = soyad;
	}

	public String getCinsiyet() {
		return cinsiyet;
	}

	public void setCinsiyet(String cinsiyet) {
		this.cinsiyet = cinsiyet;
	}

	public String getCepTel() {
		return cepTel;
	}

	public void setCepTel(String cepTel) {
		this.cepTel = cepTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getParola() {
		return parola;
	}

	public void setParola(String parola) {
		this.parola = parola;
	}

	public String getBolum() {
		return bolum;
	}

	public void setBolum(String bolum) {
		this.bolum = bolum;
	}

	public doktorInfo getSonraki() {
		return sonraki;
	}

	public void setSonraki(doktorInfo sonraki) {
		this.sonraki = sonraki;
	}

	public doktorInfo getOnceki() {
		return onceki;
	}

	public void setOnceki(doktorInfo onceki) {
		this.onceki = onceki;
	}

	public doktorInfo(String tc, String ad, String soyad, String cinsiyet, String cepTel, String email, String parola, String bolum) {
        this.tc = tc;
        this.ad = ad;
        this.soyad = soyad;
        this.cinsiyet = cinsiyet;
        this.cepTel = cepTel;
        this.email = email;
        this.parola = parola;
        this.bolum = bolum;
        this.sonraki = null;
        this.onceki = null;
    }
}