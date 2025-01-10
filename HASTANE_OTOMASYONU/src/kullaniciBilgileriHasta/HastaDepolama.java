package kullaniciBilgileriHasta;

public class HastaDepolama {
	KullaniciInfo on;
	KullaniciInfo arka;
    int elemanSayisi;

    public HastaDepolama() {
        this.on = null;
        this.arka = null;
        this.elemanSayisi = 0;
    }

    public void doktorEkle(KullaniciInfo yeni) { 
        if (on == null) {
            on = yeni;
            arka = yeni;
        } else {
            arka.sonraki = yeni;
            yeni.onceki = arka;
            arka = yeni;
        }
        elemanSayisi++; 
    }

    public boolean bosMu() {
        return elemanSayisi == 0;
    }

    public boolean doktorVarMi(String tc) {
    	KullaniciInfo current = this.on;
        while (current != null) {
            if (current.tc.equals(tc)) {
                return true; // Doktor bulundu
            }
            current = current.sonraki;
        }
        return false; // Doktor bulunamadı
    }

    public void doktorlariListele() {
        if (bosMu()) {
            System.out.println("Doktor listesi boş.");
            return;
        }

        KullaniciInfo current = this.on;
        System.out.println("Doktor Listesi:");
        while (current != null) {
            System.out.println("TC: " + current.tc + ", Ad: " + current.ad + ", Soyad: " + current.soyad +", Cinsiyet: "+current.cinsiyet+", Cep Telefonu: "+current.cepTel+", e-posta: "+current.email+ ", Bölüm: " + current.bolum);
            current = current.sonraki;
        }
    }
}
