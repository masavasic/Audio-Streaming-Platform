/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.Audiosnimak;
import entiteti.Kategorija;
import entiteti.Korisnik;
import entiteti.Pripada;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 *
 * @author Masa
 */
public class Main {

    /**
     * @param args the command line arguments
     */
     
    @Resource(lookup = "ConnFac")
    private static ConnectionFactory connectionFactory;
    
     @Resource(lookup = "RequestPro")
    private static Queue queueZahtev;
    
    @Resource(lookup = "ResponsePro")
    private static Queue queueOdgovor;

    public static void main(String[] args) {
        // TODO code application logic here
        try{
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
            EntityManager em = emf.createEntityManager();
            EntityTransaction et = em.getTransaction();
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueZahtev, "zahtev=5 or zahtev=6 or zahtev=7 or zahtev=8 or zahtev=17 or zahtev=20 or zahtev=21 or zahtev=22");
            JMSProducer producer = context.createProducer();
            while(true){
                Message msg = consumer.receive();
                System.out.println("proslo"+ msg.getIntProperty("zahtev"));

                int zahtev = msg.getIntProperty("zahtev");
                TextMessage txtMsg=null;
                if(msg instanceof TextMessage){
                    txtMsg = (TextMessage) msg;
                }
                
                switch(zahtev){
                    case 5:{
                        try{
                            String Naziv = txtMsg.getStringProperty("naziv");

                            if (Naziv == null || Naziv.isEmpty()) {
                                producer.send(queueOdgovor, context.createTextMessage("Nije dobar naziv"));
                                return;
                            }

                            try {

                                Kategorija kategorija = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).setParameter("naziv", Naziv).getSingleResult();
                                producer.send(queueOdgovor, context.createTextMessage("Kategorija sa ovim nazivom vec postoji!"));
                            } catch (NoResultException e) {
                                et.begin();
                                Kategorija kategorija= new Kategorija();
                                kategorija.setNaziv(Naziv);
                                em.persist(kategorija);
                                em.flush();
                                et.commit();
                                producer.send(queueOdgovor, context.createTextMessage("Kategorija je uspesno kreirana!"));
                            }
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                        
                    }
                    case 6:{
                        try{
                            
                            String Naziv = txtMsg.getStringProperty("naziv");
                            String trajanjeStr = txtMsg.getStringProperty("trajanjeStr");
                            String datumvremeStr = txtMsg.getStringProperty("datumvremeStr");
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            try {

                                Audiosnimak audiosnimak = em.createNamedQuery("Audiosnimak.findByNaziv", Audiosnimak.class).setParameter("naziv", Naziv).getSingleResult();
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak sa ovim nazivom vec postoji!"));
                            } catch (NoResultException e) {
                                et.begin();
                                List<Korisnik> vlasnici = em.createNamedQuery("Korisnik.findByIdKor",Korisnik.class).setParameter("idKor", IdKor).getResultList();
                                if(vlasnici.isEmpty()||vlasnici==null){
                                    producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                    et.commit();
                                    break;
                                }
                                Korisnik vlasnik=vlasnici.get(0);
                                Time trajanje = Time.valueOf(trajanjeStr); // Format: HH:mm:ss
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                                LocalDateTime localDateTime = LocalDateTime.parse(datumvremeStr, formatter);
                                Timestamp datumvreme = Timestamp.valueOf(localDateTime);
                                Audiosnimak audiosnimak= new Audiosnimak();
                                audiosnimak.setNaziv(Naziv);
                                audiosnimak.setDatumVreme(datumvreme);
                                audiosnimak.setTrajanje(trajanje);
                                audiosnimak.setIdVlasnika(vlasnik);
                                em.persist(audiosnimak);
                                em.flush();
                                et.commit();
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak je uspesno kreiran!"));
                            }
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 7:{
                        try{
                            et.begin();
                            String Naziv = txtMsg.getStringProperty("naziv");
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                            if(audiosnimak==null){
                                producer.send(queueOdgovor, context.createTextMessage("Ne postoji audio snimak!"));
                                et.commit();
                                break;
                            }
                            audiosnimak.setNaziv(Naziv);
                            em.merge(audiosnimak);
                            producer.send(queueOdgovor, context.createTextMessage("Naziv audio snimka je uspesno promenjen!"));
                            et.commit();
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 8:{
                        try {
                            int IdKat = txtMsg.getIntProperty("IdKat");
                            int IdAud = txtMsg.getIntProperty("IdAud");

                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                            if (audiosnimak == null) {
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }

                            Kategorija kategorija = em.find(Kategorija.class, IdKat);
                            if (kategorija == null) {
                                producer.send(queueOdgovor, context.createTextMessage("Kategorija ne postoji!"));
                                break;
                            }

                            List<?> postoji = em.createNativeQuery("SELECT 1 FROM pripada WHERE IdAudSni = ? AND IdKat = ?").setParameter(1, IdAud).setParameter(2, IdKat).getResultList();

                            if (!postoji.isEmpty()) {
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak vec pripada toj kategoriji!"));
                                break;
                            }
                            et.begin();
                            Pripada pripada = new Pripada();
                            pripada.setIdAudSni(audiosnimak);
                            pripada.setIdKat(kategorija);
                            em.persist(pripada);
                            em.flush();
                            et.commit();

                            producer.send(queueOdgovor, context.createTextMessage("Dodeljena je kategorija audio snimku!"));

                        } finally {
                            if (et.isActive()) {
                                et.rollback();
                            }
                        }
                        break;
                       
            
            
                    }
                    case 17:{
                        try{
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                           
                            if (audiosnimak == null) {
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }
                            Korisnik korisnik = em.find(Korisnik.class, IdKor);
                            if (korisnik == null) {
                                producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                break;
                            }
                            
                                if(!audiosnimak.getIdVlasnika().equals(korisnik)){
                                    producer.send(queueOdgovor, context.createTextMessage("Korisnik nije vlasnik audio snimka!"));
                                    break;
                                }
                                et.begin();
                                em.remove(audiosnimak);
                                em.flush();
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak je obrisan!"));
                                et.commit();
                            
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 20:{
                        try{
                         
                            List<Kategorija> kategorije = em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Kategorije:\n");
                            for(Kategorija k: kategorije){
                                sb.append(k);
                                sb.append("\n");
                            }

                            producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 21:{
                        try{
                         
                            List<Audiosnimak> audiosnimci = em.createNamedQuery("Audiosnimak.findAll", Audiosnimak.class).getResultList();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Audio snimci:\n");
                            for(Audiosnimak a: audiosnimci){
                                sb.append(a);
                                sb.append("\n");
                            }

                            producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    
                    case 22:{
                        try{
                            int idAud = txtMsg.getIntProperty("IdAud");
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, idAud);
                            if (audiosnimak == null) {
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }
                            List<Kategorija> kategorije = em.createQuery("SELECT p.idKat FROM Pripada p WHERE p.idAudSni.idAudSni = :idaud", Kategorija.class).setParameter("idaud", idAud).getResultList();
                            if (kategorije.isEmpty()) {
                                producer.send(queueOdgovor, context.createTextMessage("Nema kategorija."));
                                break;
                            }
                            
                            StringBuilder sb = new StringBuilder();
                            sb.append("Kategorije:\n");
                            for(Kategorija k: kategorije){
                                sb.append(k);
                                sb.append("\n");
                            }

                            producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                             
                }
            }
        }catch(JMSException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);

        } 
    }
    
}
