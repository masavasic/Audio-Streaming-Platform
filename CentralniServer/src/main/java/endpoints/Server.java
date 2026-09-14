/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entiteti.Audiosnimak;
import entiteti.Kategorija;
import entiteti.Korisnik;
import entiteti.Mesto;
import entiteti.Ocena;
import entiteti.Omiljenisnimak;
import entiteti.Paket;
import entiteti.Pretplata;
import entiteti.Pripada;
import entiteti.Slusanje;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author Masa
 */
@Path("zahtev")
public class Server {
    //while(consumer.receiveNoWait()!=null); ovo da se doda pre slanja poruke da ocisti red
    
    @Resource(lookup = "ConnFac")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "RequestPro")
    private Queue queueZahtev;
    
    @Resource(lookup = "ResponsePro")
    private Queue queueOdgovor;

    //zahtev 1. Kreiranje grada
    @POST
    @Path("mesto/{Naziv}")
    public Response kreirajGrad(@PathParam("Naziv") String Naziv)  {
        try {
            System.out.println("dadada");
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            while(consumer.receiveNoWait()!=null);
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setStringProperty("naziv", Naziv);
            txtMsg.setIntProperty("zahtev", 1);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
           
            context.close();
            consumer.close();
            return Response.status(Response.Status.CREATED).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //2.Kreiranje korisnika
    @POST
    @Path("korisnik/{ime}/{email}/{godiste}/{polStr}/{mesto}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajKorisnika(
        @PathParam("ime") String ime,
        @PathParam("email") String email,
        @PathParam("godiste") int godiste,
        @PathParam("polStr") String polStr,
        @PathParam("mesto") String mesto) {

        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 2);
            txtMsg.setStringProperty("ime", ime);
            txtMsg.setStringProperty("email", email);
            txtMsg.setIntProperty("godiste", godiste);
            txtMsg.setStringProperty("polStr", polStr);
            txtMsg.setStringProperty("mesto", mesto);
            producer.send(queueZahtev, txtMsg);
            
             TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();;
            consumer.close();
            return Response.status(Response.Status.CREATED).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //3. Promena email adrese za korisnika
    @PUT
    @Path("promeniEmail/{IdKor}/{email}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniEmail(@PathParam("IdKor") int IdKor, @PathParam("email") String email){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
           
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 3);
            txtMsg.setIntProperty("IdKor", IdKor);
            txtMsg.setStringProperty("email", email);
            producer.send(queueZahtev, txtMsg);
          
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();;
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //4.Promena mesta za korisnika
    @PUT
    @Path("korisnik/{IdKor}/{IdMes}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniMesto(@PathParam("IdKor") int IdKor, @PathParam("IdMes") int IdMes) {
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
           
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 4);
            txtMsg.setIntProperty("IdKor", IdKor);
            txtMsg.setIntProperty("IdMes", IdMes);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();;
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
     //5.Kreiranje kategorije
    @POST
    @Path("kategorija/{naziv}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajKategoriju(@PathParam("naziv") String naziv){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 5);
            txtMsg.setStringProperty("naziv", naziv);
            
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.CREATED).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
     //6.Kreiranje audio snimka
    @POST
    @Path("audiosnimak/{naziv}/{trajanjeStr}/{datumvremeStr}/{idkor}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajAudioSnimak(@PathParam("naziv") String naziv, @PathParam("trajanjeStr") String trajanjeStr, @PathParam("datumvremeStr") String datumvremeStr, @PathParam("idkor") int idkor){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
           
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 6);
            txtMsg.setStringProperty("naziv", naziv);
            txtMsg.setStringProperty("trajanjeStr", trajanjeStr);
            txtMsg.setStringProperty("datumvremeStr", datumvremeStr);
            txtMsg.setIntProperty("IdKor", idkor);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.CREATED).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //7.Promena naziva audio snimka
    @PUT
    @Path("audiosnimak/{idAud}/{naziv}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniNazivAudioSnimka(@PathParam("idAud") int idAud, @PathParam("naziv") String naziv){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 7);
            txtMsg.setStringProperty("naziv", naziv);
            txtMsg.setIntProperty("IdAud", idAud);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
     //8. Dodavanje kategorije audio snimku
    @POST
    @Path("audiosnimak/{idAud}/{idKat}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dodajKategorijuAudioSnimku(@PathParam("idAud") int idAud, @PathParam("idKat") int idKat){
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 8);
            txtMsg.setIntProperty("IdKat", idKat);
            txtMsg.setIntProperty("IdAud", idAud);
            producer.send(queueZahtev, txtMsg);
            
             TextMessage odgovorMsg = (TextMessage) consumer.receive(20000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //9. Kreiranje paketa
    @POST
    @Path("paket/{cena}/{naziv}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajPaket(@PathParam("cena") double cena, @PathParam("naziv") String naziv){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 9);
            txtMsg.setDoubleProperty("cena", cena);
            txtMsg.setStringProperty("naziv", naziv);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
        
    }
    
    //10. Promena mesecne cene za paket
    @PUT
    @Path("paket/{idPak}/{cena}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniCenuPaketa(@PathParam("idPak") int idPak, @PathParam("cena") double cena){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 10);
            txtMsg.setIntProperty("IdPak", idPak);
            txtMsg.setDoubleProperty("cena", cena);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //11. Kreiranje pretplate korisnika na paket
    @POST 
    @Path("paket/{datumvreme}/{cena}/{idkor}/{idpak}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajPretplatu(@PathParam("datumvreme") String datumvremeStr, @PathParam("idkor") int idkor, @PathParam("idpak") int idpak){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 11);
            txtMsg.setStringProperty("datumvremeStr", datumvremeStr);
            //txtMsg.setDoubleProperty("cena", cena);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdPak", idpak);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(20000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //12. Kreiranje slušanja audio snimka od strane korisnika
    @POST
    @Path("slusanje/{datumvreme}/{sek1}/{sek2}/{idkor}/{idaud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajSlusanje(@PathParam("datumvreme") String datumvremeStr, @PathParam("sek1") int sek1, @PathParam("sek2")int sek2, @PathParam("idkor") int idkor, @PathParam("idaud") int idaud){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 12);
            txtMsg.setStringProperty("datumvremeStr", datumvremeStr);
            txtMsg.setIntProperty("Sek1", sek1);
            txtMsg.setIntProperty("Sek2", sek2);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdAud", idaud);
            producer.send(queueZahtev, txtMsg);
            
             TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //13. . Dodavanje audio snimka u omiljene od strane korisnika
     @POST
     @Path("omiljeni/{idkor}/{idaud}")
     @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
     public Response dodajUOmiljene(@PathParam("idkor") int idkor, @PathParam("idaud") int idaud){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 13);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdAud", idaud);
            producer.send(queueZahtev, txtMsg);
            
             TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
     }
     
     //14. Kreiranje ocene korisnika za audio snimak
    @POST
    @Path("ocena/{ocena}/{datumvreme}/{idkor}/{idaud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
     public Response kreirajOcenu(@PathParam("ocena") int o,@PathParam("datumvreme") String datumvremeStr, @PathParam("idkor") int idkor, @PathParam("idaud") int idaud){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 14);
            txtMsg.setIntProperty("ocena", o);
            txtMsg.setStringProperty("datumvremeStr", datumvremeStr);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdAud", idaud);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
     //15. Menjanje ocene korisnika za audio snimak
     @PUT
    @Path("ocena/{o}/{idkor}/{idaud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniOcenu(@PathParam("o") int o, @PathParam("idkor") int idkor, @PathParam("idaud") int idaud) {
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 15);
            txtMsg.setIntProperty("ocena", o);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdAud", idaud);
            producer.send(queueZahtev, txtMsg);
            
             TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //16. Brisanje ocene korisnika za audio snimak
    @DELETE
    @Path("ocena/{idkor}/{idaud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response obrisiOcenu(@PathParam("idkor") int idkor, @PathParam("idaud") int idaud) {
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 16);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdAud", idaud);
            producer.send(queueZahtev, txtMsg);
            
             TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //17. Brisanje audio snimka od strane korisnika koji ga je kreirao
    @DELETE
    @Path("audiosnimak/{idaud}/{idkor}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response obrisiAudioSnimak(@PathParam("idaud") int idaud, @PathParam("idkor") int idkor){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 17);
            txtMsg.setIntProperty("IdKor", idkor);
            txtMsg.setIntProperty("IdAud", idaud);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //18. Dohvatanje svih mesta
    @GET
    @Path("mesto")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiMesta(){
        try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 18);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
        
    
    }
    
    //19. Dohvatanje svih korisnika
    @GET
    @Path("korisnik")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiKorisnike(){
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 19);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //20. Dohvatanje svih kategorija
    @GET
    @Path("kategorija")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiKategorije(){
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 20);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
        
    }
  
    //21. Dohvatanje svih audio snimaka
    @GET
    @Path("audiosnimak")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiAudioSnimke(){
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 21);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //22. Dohvatanje kategorija za određeni audio snimak
    @GET
    @Path("kategorija/{idAud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiKategorijuZaOdredjeniAudioSnimak(@PathParam("idAud") int idAud) {
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 22);
            txtMsg.setIntProperty("IdAud", idAud);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    //23. . Dohvatanje svih paketa
    @GET
    @Path("paket")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiPakete(){
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 23);
            producer.send(queueZahtev, txtMsg);
            
           TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //24. Dohvatanje svih pretplata za korisnika
    @GET
    @Path("pretplata/{idKor}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
     public Response dohvatiPretplate(@PathParam("idKor") int idKor){
          try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 24);
            txtMsg.setIntProperty("IdKor", idKor);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
     
    //25. Dohvatanje svih slušanja za audio snimak
    @GET
    @Path("slusanja/{idAud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSlusanjaZaAudioSnimak(@PathParam("idAud") int idAud) {
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 25);
            txtMsg.setIntProperty("IdAud", idAud);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //26.  Dohvatanje svih ocena za audio snimak
    @GET
    @Path("ocena/{idAud}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiOcenuZaAudioSnimak(@PathParam("idAud") int idAud) {
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 26);
            txtMsg.setIntProperty("IdAud", idAud);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    //27. Dohvatanje liste omiljenih audio snimaka za korisnika
    @GET
    @Path("omiljeni/{idKor}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiOmiljeneSnimkeZaKorisnika(@PathParam("idKor") int idKor) {
         try {
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueOdgovor);
            JMSProducer producer = context.createProducer();
            TextMessage txtMsg = context.createTextMessage();
            txtMsg.setIntProperty("zahtev", 27);
            txtMsg.setIntProperty("IdKor", idKor);
            producer.send(queueZahtev, txtMsg);
            
            TextMessage odgovorMsg = (TextMessage) consumer.receive(10000); // cekaj max 10s
            if (odgovorMsg == null) {
                return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Nema odgovora na vreme").build();
            }
            String odgovor = odgovorMsg.getText();
            context.close();
            consumer.close();
            return Response.status(Response.Status.OK).entity(odgovor).build();
        } catch (JMSException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska").build();
        }
    }
    
    
    
}
