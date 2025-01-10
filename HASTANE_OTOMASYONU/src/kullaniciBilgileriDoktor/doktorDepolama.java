package kullaniciBilgileriDoktor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
public class doktorDepolama {
    public doktorInfo on;
    doktorInfo arka;
    int elemanSayisi;
    
    private HashMap<String, doktorInfo> doktorlarMap;
        
    public boolean doktorVarMi(String tc, String cepTel, String email) {
        // Burada doktorlar dosyasını okuma ve TC, cepTel, email bilgilerini kontrol etme işlemi yapılabilir.
        try (BufferedReader reader = new BufferedReader(new FileReader("doktorlar.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Her satırdaki doktor bilgisini ayıklama (örneğin, TC, cepTel, email'i kontrol etme)
                String[] fields = line.split(", ");
                String existingTc = fields[0].split(": ")[1];
                String existingCepTel = fields[4].split(": ")[1];
                String existingEmail = fields[5].split(": ")[1];

                // Eğer TC, cepTel veya email eşleşiyorsa, doktor zaten kayıtlı demektir
                if (existingTc.equals(tc) || existingCepTel.equals(cepTel) || existingEmail.equals(email)) {
                    return true;  // Doktor zaten var
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // Doktor bulunamadı, yeni kayıt yapılabilir
    }


    public doktorDepolama() {
        this.on = null;
        this.arka = null;
        this.elemanSayisi = 0;
        doktorlarMap = new HashMap<>();
    }

    public void doktorEkle(doktorInfo yeni) { 
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
        doktorInfo current = this.on;
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

        doktorInfo current = this.on;
        System.out.println("Doktor Listesi:");
        while (current != null) {
            System.out.println("TC: " + current.tc + ", Ad: " + current.ad + ", Soyad: " + current.soyad +", Cinsiyet: "+current.cinsiyet+", Cep Telefonu: "+current.cepTel+", e-posta: "+current.email+ ", Bölüm: " + current.bolum);
            current = current.sonraki;
        }
    }




    public void doktorlariListele2() {
        if (bosMu()) {
            System.out.println("Doktor listesi boş.");
            return;
        }

        doktorInfo current = this.on;
        System.out.println("Doktor Listesi:");
        while (current != null) {
            System.out.println("TC: " + current.tc + ", Ad: " + current.ad + ", Soyad: " + current.soyad +
                    ", Cinsiyet: " + current.cinsiyet + ", Cep Telefonu: " + current.cepTel +
                    ", E-posta: " + current.email +", Parola: " + current.parola + ", Bölüm: " + current.bolum);
            current = current.sonraki;
        }
    }

    /*
        // Dosyadan doktorları listele
        System.out.println("\nDoktor Listesi (Dosyadan):");
        try (BufferedReader reader = new BufferedReader(new FileReader("doktorlar.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }*/



    public void dosyadanVeriEkle(String dosyaAdi) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Verileri ayrıştır ve yeni bir doktorInfo nesnesi oluştur
                String[] fields = line.split(", ");
                String tc = fields[0].split(": ")[1];
                String ad = fields[1].split(": ")[1];
                String soyad = fields[2].split(": ")[1];
                String cinsiyet = fields[3].split(": ")[1];
                String cepTel = fields[4].split(": ")[1];
                String email = fields[5].split(": ")[1];
                String bolum = fields[6].split(": ")[1];
                String parola = fields[7].split(": ")[1];

                // Yeni doktor nesnesini bağlı listeye ekle
                doktorInfo yeniDoktor = new doktorInfo(tc, ad, soyad, cinsiyet, cepTel, email, parola, bolum);
                this.doktorEkle(yeniDoktor);
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Satır ayrıştırma hatası. Formatı kontrol edin.");
        }
    }

    
    
    
    
    
    public void veriYukle(String dosyaAdi) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(", ");
                String tc = fields[0].split(": ")[1];
                String ad = fields[1].split(": ")[1];
                String soyad = fields[2].split(": ")[1];
                String cinsiyet = fields[3].split(": ")[1];
                String cepTel = fields[4].split(": ")[1];
                String email = fields[5].split(": ")[1];
                String bolum = fields[6].split(": ")[1];
                String parola = fields[7].split(": ")[1];

                doktorInfo doktor = new doktorInfo(tc, ad, soyad, cinsiyet, cepTel, email, bolum, parola);
                doktorlarMap.put(tc, doktor); // TC numarasını anahtar olarak kullanıyoruz
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    // TC numarası ile doktor arama
    public doktorInfo doktorAra(String tc) {
        return doktorlarMap.get(tc);
    }

    // Ad ve soyad ile doktor arama
    public doktorInfo doktorAra(String ad, String soyad) {
        for (doktorInfo doktor : doktorlarMap.values()) {
            if (doktor.getAd().equalsIgnoreCase(ad) && doktor.getSoyad().equalsIgnoreCase(soyad)) {
                return doktor;
            }
        }
        return null;
    }
    
    
    
    
    
    
    
    
    


}