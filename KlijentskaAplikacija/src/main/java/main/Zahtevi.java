/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface Zahtevi {
    @POST("mesto/{Naziv}") Call<String> kreirajGrad(@Path("Naziv") String Naziv);
    @POST("korisnik/{ime}/{email}/{godiste}/{polStr}/{mesto}") Call<String> kreirajKorisnika(@Path("ime") String ime, @Path("email") String email, @Path("godiste") int godiste, @Path("polStr") String polStr,@Path("mesto") String mesto);
    @PUT("promeniEmail/{IdKor}/{email}") Call<String> promeniEmail(@Path("IdKor") int IdKor, @Path("email") String email);
    @PUT("korisnik/{IdKor}/{IdMes}") Call<String> promeniMesto(@Path("IdKor") int IdKor, @Path("IdMes") int IdMes);
    @POST("kategorija/{naziv}") Call<String> kreirajKategoriju(@Path("naziv") String naziv);
    @POST("audiosnimak/{naziv}/{trajanjeStr}/{datumvremeStr}/{idkor}") Call<String> kreirajAudioSnimak(@Path("naziv") String naziv, @Path("trajanjeStr") String trajanjeStr, @Path("datumvremeStr") String datumvremeStr, @Path("idkor") int idkor);
    @PUT("audiosnimak/{idAud}/{naziv}") Call<String> promeniNazivAudioSnimka(@Path("idAud") int idAud, @Path("naziv") String naziv);
    @POST("audiosnimak/{idAud}/{idKat}") Call<String> dodajKategorijuAudioSnimku(@Path("idAud") int idAud, @Path("idKat") int idKat);
    @POST("paket/{cena}/{naziv}") Call<String> kreirajPaket(@Path("cena") double cena, @Path("naziv") String naziv);
    @PUT("paket/{idPak}/{cena}") Call<String> promeniCenuPaketa(@Path("idPak") int idPak, @Path("cena") double cena);
    @POST("paket/{datumvreme}/{cena}/{idkor}/{idpak}") Call<String> kreirajPretplatu(@Path("datumvreme") String datumvremeStr, @Path("idkor") int idkor, @Path("idpak") int idpak);
    @POST("slusanje/{datumvreme}/{sek1}/{sek2}/{idkor}/{idaud}") Call<String> kreirajSlusanje(@Path("datumvreme") String datumvremeStr, @Path("sek1") int sek1, @Path("sek2")int sek2, @Path("idkor") int idkor, @Path("idaud") int idaud);
    @POST("omiljeni/{idkor}/{idaud}") Call<String> dodajUOmiljene(@Path("idkor") int idkor, @Path("idaud") int idaud);
    @POST("ocena/{ocena}/{datumvreme}/{idkor}/{idaud}") Call<String> kreirajOcenu(@Path("ocena") int o,@Path("datumvreme") String datumvremeStr, @Path("idkor") int idkor, @Path("idaud") int idaud);
    @PUT("ocena/{o}/{idkor}/{idaud}") Call<String> promeniOcenu(@Path("o") int o, @Path("idkor") int idkor, @Path("idaud") int idaud);
    @DELETE("ocena/{idkor}/{idaud}") Call<String> obrisiOcenu(@Path("idkor") int idkor, @Path("idaud") int idaud);
    @DELETE("audiosnimak/{idaud}/{idkor}") Call<String> obrisiAudioSnimak(@Path("idaud") int idaud, @Path("idkor") int idkor);
    @GET("mesto") Call<String> dohvatiMesta();
    @GET("korisnik") Call<String> dohvatiKorisnike();
    @GET("kategorija") Call<String> dohvatiKategorije();
    @GET("audiosnimak") Call<String> dohvatiAudioSnimke();
    @GET("kategorija/{idAud}") Call<String> dohvatiKategorijuZaOdredjeniAudioSnimak(@Path("idAud") int idAud);
    @GET("paket") Call<String> dohvatiPakete();
    @GET("pretplata/{idKor}") Call<String> dohvatiPretplate(@Path("idKor") int idKor);
    @GET("slusanja/{idAud}") Call<String> dohvatiSlusanjaZaAudioSnimak(@Path("idAud") int idAud);
    @GET("ocena/{idAud}") Call<String> dohvatiOcenuZaAudioSnimak(@Path("idAud") int idAud);
    @GET("omiljeni/{idKor}") Call<String> dohvatiOmiljeneSnimkeZaKorisnika(@Path("idKor") int idKor);   
}
