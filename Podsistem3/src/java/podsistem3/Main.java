/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.Audiosnimak;
import entiteti.Korisnik;
import entiteti.Ocena;
import entiteti.Omiljenisnimak;
import entiteti.Paket;
import entiteti.Pretplata;
import entiteti.Slusanje;
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
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");
            EntityManager em = emf.createEntityManager();
            EntityTransaction et = em.getTransaction();
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueZahtev, "zahtev=9 or zahtev=10 or zahtev=11 or zahtev=12 or zahtev=13 or zahtev=14 or zahtev=15 or zahtev=16 or zahtev=23 or zahtev=24 or zahtev=25 or zahtev=26 or zahtev=27");
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
                    case 9:{
                        try{
                            
                            String Naziv = txtMsg.getStringProperty("naziv");
                            double cena= txtMsg.getDoubleProperty("cena");
                            try {
                                Paket paket = em.createNamedQuery("Paket.findByNaziv", Paket.class).setParameter("naziv", Naziv).getSingleResult();
                                producer.send(queueOdgovor, context.createTextMessage("Paket sa ovim nazivom vec postoji!"));
                            } catch (NoResultException e) {
                                et.begin();
                                Paket paket = new Paket();
                                paket.setCena(cena);
                                paket.setNaziv(Naziv);
                                em.persist(paket);
                                em.flush();
                                et.commit();
                                producer.send(queueOdgovor, context.createTextMessage("Paket je uspesno kreiran!"));
                            }
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 10:{
                        try{
                            
                            double cena = txtMsg.getDoubleProperty("cena");
                            int IdPak = txtMsg.getIntProperty("IdPak");
                            Paket paket = em.find(Paket.class, IdPak);
                            if(paket==null){
                                producer.send(queueOdgovor, context.createTextMessage("Ne postoji paket!"));
                                break;
                            }
                            et.begin();
                            paket.setCena(cena);
                            em.merge(paket);
                            producer.send(queueOdgovor, context.createTextMessage("Cena paketa je uspesno promenjena!"));
                            et.commit();
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 11:{
                        try{
                            //double cena = txtMsg.getDoubleProperty("cena");
                            String datumvremeStr = txtMsg.getStringProperty("datumvremeStr");
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            int IdPak = txtMsg.getIntProperty("IdPak");
                            Korisnik korisnik = em.find(Korisnik.class, IdKor);
                            if(korisnik==null){
                                producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                break;
                            }
                            Paket paket = em.find(Paket.class, IdPak);
                            if(paket==null){
                                 producer.send(queueOdgovor, context.createTextMessage("Paket ne postoji!"));
                                 break;
                            }
                            double cena = paket.getCena();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.parse(datumvremeStr, formatter);
                            Timestamp datumvreme = Timestamp.valueOf(localDateTime);
                            List<?> pretplate = em.createNativeQuery("SELECT 1 FROM pretplata WHERE IdKor = ? AND IdPak = ? AND DATEDIFF(?, DatumVreme) < 30").setParameter(1, IdKor).setParameter(2, IdPak).setParameter(3, datumvreme).getResultList();

                            if (!pretplate.isEmpty()) {
                                producer.send(queueOdgovor, context.createTextMessage("Postoji aktivna pretplata na ovaj paket u poslednjih mesec dana!"));
                                return;
                            }
                            et.begin();
                            Korisnik vlasnik = em.createNamedQuery("Korisnik.findByIdKor",Korisnik.class).setParameter("idKor", IdKor).getSingleResult();
                             
                            Pretplata pretplata = new Pretplata();
                            pretplata.setDatumVreme(datumvreme);
                            pretplata.setCena(cena);
                            pretplata.setIdKor(korisnik);
                            pretplata.setIdPak(paket);
                            em.persist(pretplata);
                            em.flush();
                            et.commit();
                            producer.send(queueOdgovor, context.createTextMessage("Pretplata je uspesno kreirana!"));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 12:{
                        try{
                            System.out.println("usla");
                            String datumvremeStr = txtMsg.getStringProperty("datumvremeStr");
                            int sek1 = txtMsg.getIntProperty("Sek1");
                            int sek2 = txtMsg.getIntProperty("Sek2");
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            Korisnik korisnik = em.find(Korisnik.class, IdKor);
                            if(korisnik==null){
                                System.out.println("nemakor");
                                producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                break;
                            }
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                            if(audiosnimak==null){
                                System.out.println("nemaaudsn");
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }
                            et.begin();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.parse(datumvremeStr, formatter);
                            Timestamp datumvreme = Timestamp.valueOf(localDateTime);
                                
                            Slusanje slusanje = new Slusanje();
                            slusanje.setDatumVreme(datumvreme);
                            slusanje.setSekundZapoceto(sek1);
                            slusanje.setSekundOdslusano(sek2);
                            slusanje.setIdKor(korisnik);
                            slusanje.setIdAudSni(audiosnimak);
                            em.persist(slusanje);
                            em.flush();
                            et.commit();
                            System.out.println("pocelodasesalje");
                            producer.send(queueOdgovor, context.createTextMessage("Slusanje je uspesno kreirano!"));
                            System.out.println("poslato");
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 13:{
                        try{
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            Korisnik korisnik = em.find(Korisnik.class, IdKor);
                            if(korisnik==null){
                                producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                break;
                            }
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                            if(audiosnimak==null){
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }
                            List<?> snimci = em.createNativeQuery("SELECT 1 FROM omiljenisnimak WHERE IdAudSni = ? AND IdKor = ?").setParameter(1, IdAud).setParameter(2, IdKor).getResultList();

                            if (!snimci.isEmpty()) {
                                producer.send(queueOdgovor, context.createTextMessage("Vec postoji u omiljene!"));
                                break;
                            }

                            et.begin();
                            Omiljenisnimak omiljeni = new Omiljenisnimak();
                            omiljeni.setIdAudSni(audiosnimak);
                            omiljeni.setIdKor(korisnik);
                            em.persist(omiljeni);
                            em.flush();
                            et.commit();
                            producer.send(queueOdgovor, context.createTextMessage("Omiljeni snimak je uspesno kreiran!"));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 14:{
                        try{
                            String datumvremeStr = txtMsg.getStringProperty("datumvremeStr");
                            int o = txtMsg.getIntProperty("ocena");
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            if(o<1 || o>5){
                                producer.send(queueOdgovor, context.createTextMessage("Vrednost ocene nije ispravna!"));
                            }
                            Korisnik korisnik = em.find(Korisnik.class, IdKor);
                            if(korisnik==null){
                                producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                break;
                            }
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                            if(audiosnimak==null){
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }
                            et.begin();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.parse(datumvremeStr, formatter);
                            Timestamp datumvreme = Timestamp.valueOf(localDateTime);
                                
                            Ocena ocena = new Ocena();
                            ocena.setOcena(o);
                            ocena.setDatumVreme(datumvreme);
                            ocena.setIdAudSni(audiosnimak);
                            ocena.setIdKor(korisnik);
                            em.persist(ocena);
                            em.flush();
                            et.commit();
                            producer.send(queueOdgovor, context.createTextMessage("Ocena je uspesno kreirana!"));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 15:{
                        try{
                            int o = txtMsg.getIntProperty("ocena");
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            if(o<1 || o>5){
                                producer.send(queueOdgovor, context.createTextMessage("Vrednost ocene nije ispravna!"));
                           }
                            Korisnik korisnik = em.find(Korisnik.class, IdKor);
                            if(korisnik==null){
                                producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                                break;
                            }
                            Audiosnimak audiosnimak = em.find(Audiosnimak.class, IdAud);
                            if(audiosnimak==null){
                                producer.send(queueOdgovor, context.createTextMessage("Audio snimak ne postoji!"));
                                break;
                            }
                            List<Ocena> rezultati = em.createQuery("SELECT o FROM Ocena o WHERE o.idKor.idKor = :idkor AND o.idAudSni.idAudSni = :idaud", Ocena.class).setParameter("idkor", IdKor).setParameter("idaud", IdAud).getResultList();
                            
                            if (rezultati.isEmpty()) {
                                producer.send(queueOdgovor, context.createTextMessage("Ocena ne postoji!"));
                                break;
                            }
                            
                            et.begin();
                            Ocena ocena = rezultati.get(0);
                            ocena.setOcena(o);
                            em.merge(ocena);
                            et.commit();
                            producer.send(queueOdgovor, context.createTextMessage("Ocena je uspesno promenjena!"));
                            
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 16:{
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
                            try {
                                // Pronađi ocenu za korisnika i audio snimak
                                List<Ocena> rezultati = em.createQuery("SELECT o FROM Ocena o WHERE o.idKor.idKor = :idkor AND o.idAudSni.idAudSni = :idaud", Ocena.class).setParameter("idkor", IdKor).setParameter("idaud", IdAud).getResultList();
                                if (rezultati.isEmpty()) {
                                    producer.send(queueOdgovor, context.createTextMessage("Ocena ne postoji!"));
                                    break;
                                }
                                Ocena ocena = rezultati.get(0);
                                et.begin();
                                em.remove(ocena);
                                em.flush();
                                producer.send(queueOdgovor, context.createTextMessage("Ocena je obrisana!"));
                                et.commit();
                            } catch (Exception e) {
                                producer.send(queueOdgovor, context.createTextMessage("Greska!"));
                            }
                            break;
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 23:{
                        try{
                         
                            List<Paket> paketi = em.createNamedQuery("Paket.findAll", Paket.class).getResultList();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Paketi:\n");
                            for(Paket p: paketi){
                                sb.append(p);
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
                    
                    case 24:{
                        try{
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            try{ 
                                List<Pretplata> pretplate = em.createQuery("Select p From Pretplata p where p.idKor.idKor=:idkor", Pretplata.class).setParameter("idkor", IdKor).getResultList();
                                if (pretplate.isEmpty()) {
                                    producer.send(queueOdgovor, context.createTextMessage("Nema pretplata za korisnika"));
                                    break;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("Pretplate:\n");
                                for(Pretplata p: pretplate){
                                    sb.append(p);
                                    sb.append("\n");
                                }
                                producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                                break;
                            } catch (Exception e) {
                                producer.send(queueOdgovor, context.createTextMessage("Greska!"));
                                break;
                            }
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    
                    case 25:{
                        try{
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            try{ 
                                List<Slusanje> slusanja = em.createQuery("SELECT s FROM Slusanje s WHERE s.idAudSni.idAudSni = :idaud", Slusanje.class).setParameter("idaud", IdAud).getResultList();
                                if (slusanja.isEmpty()) {
                                    producer.send(queueOdgovor, context.createTextMessage("Nema slusanja za audio snimak"));
                                    break;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("Slusanja:\n");
                                for(Slusanje s: slusanja){
                                    sb.append(s);
                                    sb.append("\n");
                                }
                                producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                                break;
                            } catch (Exception e) {
                                producer.send(queueOdgovor, context.createTextMessage("Greska!"));
                                break;
                            }
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    
                    case 26:{
                        try{
                            int IdAud = txtMsg.getIntProperty("IdAud");
                            try{ 
                                List<Ocena> ocene = em.createQuery("SELECT o FROM Ocena o WHERE o.idAudSni.idAudSni = :idaud", Ocena.class).setParameter("idaud", IdAud).getResultList();
                                if (ocene.isEmpty()) {
                                    producer.send(queueOdgovor, context.createTextMessage("Nema ocena za audio snimak"));
                                    break;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("Ocene:\n");
                                for(Ocena o: ocene){
                                    sb.append(o);
                                    sb.append("\n");
                                }
                                producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                                break;
                            } catch (Exception e) {
                                producer.send(queueOdgovor, context.createTextMessage("Greska!"));
                                break;
                            }
                        }finally{
                            if(et.isActive()){
                                et.rollback();
                            }
                            break;
                        }
                    }
                    case 27:{
                        try{
                            int IdKor = txtMsg.getIntProperty("IdKor");
                            try{ 
                                List<Omiljenisnimak> omiljeni = em.createQuery("SELECT o FROM Omiljenisnimak o WHERE o.idKor.idKor = :idkor", Omiljenisnimak.class).setParameter("idkor", IdKor).getResultList();
                                if (omiljeni.isEmpty()) {
                                    producer.send(queueOdgovor, context.createTextMessage("Nema omiljenih snimaka za korisnika"));
                                    break;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("Omiljeni snimci:\n");
                                for(Omiljenisnimak o: omiljeni){
                                    sb.append(o);
                                    sb.append("\n");
                                }
                                producer.send(queueOdgovor, context.createTextMessage(sb.toString()));
                                break;
                            } catch (Exception e) {
                                producer.send(queueOdgovor, context.createTextMessage("Greska!"));
                                break;
                            }
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
