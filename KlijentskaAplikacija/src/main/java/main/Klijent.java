/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Scanner;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 *
 * @author Masa
 */

public class Klijent {
    
    public static void main(String[] args){
        //Retrofit je Java biblioteka za olakšavanje HTTP komunikacije sa REST API servisima.
        //Retrofit.Builder() Konstruktor za Retrofit objekat.
        //addConverterFactory(ScalarsConverterFactory.create()) Dodaje konverter za jednostavne tipove podataka (npr. String, int, boolean)
        //addConverterFactory(GsonConverterFactory.create()) Dodaje konverter za rad sa JSON formatom
        //baseUrl("http://localhost:8080/CentralniServer/api/zahtev/") Definiše osnovni URL za sve HTTP pozive
        //build() Kreira Retrofit instancu sa svim konfiguracijama 
        /*
           String ret = zahtevi.kreirajKorisnika(ime, email, godiste, pol, mesto).execute().body();
        mozes:
              res = zahtevi.kreirajKorisnika(ime, email,godiste,pol,mesto).execute();
              if(ret.isSuccessfull()) ret = res.body();
        */
        
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(80, TimeUnit.SECONDS).writeTimeout(80, TimeUnit.SECONDS).build(); //povecavamo vreme
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl("http://localhost:8080/CentralniServer/api/zahtev/").client(client).build();
        Zahtevi zahtevi = retrofit.create(Zahtevi.class);//uzme interfejs, pročita anotacije, i napravi runtime implementaciju koja izvršava HTTP pozive umesto nas.
        
        if (zahtevi == null) {
            System.out.println("Greška u inicijalizaciji Retrofit-a!");
            return; // Prekini program ako je Retrofit objekat null
        }
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            ispisiSveZahteve();
            int unos = Integer.parseInt(scanner.nextLine());
            if(unos>27 || unos<1) break;
            switch(unos){
                case 1:{ // pravljenje grada
                    try {
                        System.out.println("Unesite naziv grada: ");
                        String naziv = scanner.nextLine();
                        Call<String> kreirajGrad = zahtevi.kreirajGrad(naziv);
                        Response<String> execute = kreirajGrad.execute();
                        String ret = execute.body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case 2:{ //kreiranje korisnika
                    try {  
                        System.out.println("Unesite ime: ");
                        String ime = scanner.nextLine();
                        System.out.println("Unesite email: ");
                        String email = scanner.nextLine();
                        System.out.println("Unesite godiste: ");
                        int godiste =Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite pol: ");
                        String pol = scanner.nextLine();
                        System.out.println("Unesite naziv mesta: ");
                        String mesto = scanner.nextLine();
                        String ret = zahtevi.kreirajKorisnika(ime, email, godiste, pol, mesto).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case 3:{ //promena email adrese za korisnika
                    try {     
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite email: ");
                        String email = scanner.nextLine();
                        String ret = zahtevi.promeniEmail(idKor, email).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case 4:{ //promena mesta za korisnika
                    try {     
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idMes: ");
                        int idMes = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.promeniMesto(idKor, idMes).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 5:{ //kreiranje kategorije
                    try {     
                        System.out.println("Unesite naziv kategorije: ");
                        String naziv = scanner.nextLine();
                        String ret = zahtevi.kreirajKategoriju(naziv).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 6:{ //kreiranje audio snimka
                    try {     
                        System.out.println("Unesite naziv audio snimka: ");
                        String naziv = scanner.nextLine();
                        System.out.println("Unesite trajanje: ");
                        String trajanje = scanner.nextLine();
                        System.out.println("Unesite datum i vreme: ");
                        String datumvreme = scanner.nextLine();
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.kreirajAudioSnimak(naziv, trajanje, datumvreme, idKor).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 7:{ //promena naziva audio snimka
                     try {     
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite naziv: ");
                        String naziv = scanner.nextLine();
                        String ret = zahtevi.promeniNazivAudioSnimka(idAud, naziv).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 8:{// dodavanje kategorije audio snimku
                    try {     
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idKat: ");
                        int idKat = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dodajKategorijuAudioSnimku(idAud, idKat).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 9:{ //kreiranje paketa
                     try {     
                        System.out.println("Unesite cenu paketa: ");
                        double cena = Double.parseDouble(scanner.nextLine());
                        System.out.println("Unesite naziv paketa: ");
                        String naziv = scanner.nextLine();
                        String ret = zahtevi.kreirajPaket(cena, naziv).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 10:{// promena mesecne cena za paket
                      try {  
                        System.out.println("Unesite idPak: ");
                        int idPak = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite cenu paketa: ");
                        double cena = Double.parseDouble(scanner.nextLine());
                        String ret = zahtevi.promeniCenuPaketa(idPak, cena).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 11:{ // kreiranje pretplate
                    try {  
                        System.out.println("Unesite datum i vreme: ");
                        String datumvreme = scanner.nextLine();
                        /*System.out.println("Unesite cenu: ");
                        double cena = Double.parseDouble(scanner.nextLine());*/
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idPak: ");
                        int idPak = Integer.parseInt(scanner.nextLine()); 
                        String ret = zahtevi.kreirajPretplatu(datumvreme, idKor, idPak).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 12:{ //kreiranje slusanja
                    try {  
                        System.out.println("Unesite datum i vreme: ");
                        String datumvreme = scanner.nextLine();
                        System.out.println("Unesite zapocetu sekundu: ");
                        int sek1 = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite odlusanu sekundu: ");
                        int sek2 = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.kreirajSlusanje(datumvreme, sek1, sek2, idKor, idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 13:{//dodavanje snimka u omiljene
                    try {  
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dodajUOmiljene(idKor, idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 14:{//kreiranje ocene
                    try {
                        System.out.println("Unesite ocenu: ");
                        int ocena = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite datum i vreme: ");
                        String datumvreme = scanner.nextLine();
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.kreirajOcenu(ocena, datumvreme, idKor, idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 15:{ //menjanje ocene
                    try {
                        System.out.println("Unesite ocenu: ");
                        int ocena = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.promeniOcenu(ocena, idKor, idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 16:{// brisanje ocene
                    try {
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine()); 
                        String ret = zahtevi.obrisiOcenu(idKor, idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 17:{ //brisanje audio snimka
                    try {  
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.obrisiAudioSnimak(idAud, idKor).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 18:{ // dohvatanje svih mesta
                    try {  
                        String ret = zahtevi.dohvatiMesta().execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 19:{// dohvati korisnike
                    try {  
                        String ret = zahtevi.dohvatiKorisnike().execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 20:{ //dohvati kategorije
                    try {  
                        String ret = zahtevi.dohvatiKategorije().execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 21:{// dohvati audio snimke
                    try {  
                        String ret = zahtevi.dohvatiAudioSnimke().execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 22:{//dohvati kategoriju za audio snimak
                    try {  
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dohvatiKategorijuZaOdredjeniAudioSnimak(idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 23:{// dohvatanje paketa
                    try {  
                        String ret = zahtevi.dohvatiPakete().execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 24:{ //dohvatanje svih pretplata za korisnika
                    try {  
                        System.out.println("Unesite idKor: ");
                        int idKor = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dohvatiPretplate(idKor).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 25:{// dohvatanje slusanja za audio snimak
                    try {  
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dohvatiSlusanjaZaAudioSnimak(idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 26:{ //dohvatanje ocena za audio snimak
                    try {  
                        System.out.println("Unesite idAudSni: ");
                        int idAud = Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dohvatiOcenuZaAudioSnimak(idAud).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 27:{ //dohvatanje omiljenih snimaka
                    try {  
                        System.out.println("Unesite idKor: ");
                        int idKor =Integer.parseInt(scanner.nextLine());
                        String ret = zahtevi.dohvatiOmiljeneSnimkeZaKorisnika(idKor).execute().body();
                        System.out.println(ret);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }
        System.out.print("Zavrseno\n");
        scanner.close();
          
    }

    private static void ispisiSveZahteve() {
        System.out.println("Izaberite zahtev koji zelite:");
        System.out.println("1. Kreiranje grada.");
        System.out.println("2. Kreiranje korisnika.");
        System.out.println("3. Promena email adrese za korisnika.");
        System.out.println("4. Promena mesta za korisnika.");
        System.out.println("5. Kreiranje kategorije.");
        System.out.println("6. Kreiranje audio snimka.");
        System.out.println("7. Promena naziva audio snimka.");
        System.out.println("8. Dodavanje kategorije audio snimku.");
        System.out.println("9. Kreiranje paketa.");
        System.out.println("10. Promena mesecne cene za paket.");
        System.out.println("11. Kreiranje pretplate korisnika na paket.");
        System.out.println("12. Kreiranje slusanja audio snimka od strane korisnika.");
        System.out.println("13. Dodavanje audio snimka u omiljene od strane korisnika.");
        System.out.println("14. Kreiranje ocene korisnika za audio snimak.");
        System.out.println("15. Menjanje ocene korisnika za audio snimak.");
        System.out.println("16. Brisanje ocene korisnika za audio snimak.");
        System.out.println("17. Brisanje audio snimka od strane korisnika koji ga je kreirao.");
        System.out.println("18. Dohvatanje svih mesta.");
        System.out.println("19. Dohvatanje svih korisnika.");
        System.out.println("20. Dohvatanje svih kategorija.");
        System.out.println("21. Dohvatanje svih audio snimaka.");
        System.out.println("22. Dohvatanje kategorija za odredjeni audio snimak.");
        System.out.println("23. Dohvatanje svih paketa.");
        System.out.println("24. Dohvatanje svih pretplata za korisnika.");
        System.out.println("25. Dohvatanje svih slusanja za audio snimak.");
        System.out.println("26. Dohvatanje svih ocena za audio snimak.");
        System.out.println("27. Dohvatanje liste omiljenih audio snimaka za korisnika.");
        
    }
}
