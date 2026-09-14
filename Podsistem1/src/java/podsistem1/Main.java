/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.Korisnik;
import entiteti.Mesto;
import java.sql.Date;
import java.time.LocalDate;
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

    
    @Resource(lookup = "ConnFac")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "RequestPro")
    private static Queue queueZahtev;
    
    @Resource(lookup = "ResponsePro")
    private static Queue queueOdgovor;

    public static void main(String[] args) {
        try {
            // TODO code application logic here
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
            EntityManager em = emf.createEntityManager();
            EntityTransaction et = em.getTransaction();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queueZahtev, "zahtev=1 or zahtev=2 or zahtev=3 or zahtev=4 or zahtev=18 or zahtev=19");
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
                case 1: {
                    try{
                        String Naziv = txtMsg.getStringProperty("naziv");
                        
                        if (Naziv == null || Naziv.isEmpty()) {
                            producer.send(queueOdgovor, context.createTextMessage("Nije dobar naziv"));
                            return;
                        }
                        
                        try {
                           
                            Mesto mesto = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", Naziv).getSingleResult();
                            producer.send(queueOdgovor, context.createTextMessage("Mesto sa ovim nazivom vec postoji!"));
                        } catch (NoResultException e) {
                            et.begin(); // ovde pocinje transakcija, a promene u bazi se vide tek kad se izvrsi commit
                            Mesto grad = new Mesto();
                            grad.setNaziv(Naziv);
                            em.persist(grad);
                            em.flush(); //odmah salje promene u bazu bez zatvaranja commita
                            et.commit();
                            producer.send(queueOdgovor, context.createTextMessage("Mesto uspesno sacuvano!"));
                        }
                        break;
                    }finally{
                        if(et.isActive()){
                            et.rollback(); //ponistava sve izmene od poslednjeg begin
                        }
                        break;
                    }
                }
                case 2:{
                    try{
                        String ime = txtMsg.getStringProperty("ime");
                        String email = txtMsg.getStringProperty("email");
                        int godiste = txtMsg.getIntProperty("godiste");
                        String polStr = txtMsg.getStringProperty("polStr");
                        String mesto = txtMsg.getStringProperty("mesto");
                        if (polStr.length() != 1) {
                            producer.send(queueOdgovor, context.createTextMessage("Pol mora biti jedan karakter."));
                            break;
                            
                        }
                        boolean postoji = !em.createQuery("SELECT k FROM Korisnik k WHERE k.email = :email", Korisnik.class).setParameter("email", email).getResultList().isEmpty();
                        if (postoji) {
                            producer.send(queueOdgovor, context.createTextMessage("Korisnik sa ovim emailom vec postoji."));
                            break;
                        }
                        List<Mesto> gradovi = em.createQuery("SELECT m FROM Mesto m where m.naziv = :naziv", Mesto.class).setParameter("naziv", mesto).getResultList();
                        if(gradovi.isEmpty()) {
                            producer.send(queueOdgovor, context.createTextMessage("Ne postoji grad."));
                            break;
                        }
                        Mesto grad = gradovi.get(0);
                        char pol = polStr.charAt(0);
                        et.begin();
                        Korisnik korisnik = new Korisnik();
                        korisnik.setIme(ime);
                        korisnik.setEmail(email);
                        korisnik.setGodiste(godiste);
                        korisnik.setPol(pol);
                        korisnik.setIdMes(grad);
                        em.persist(korisnik);
                        em.flush();
                        et.commit();
                        producer.send(queueOdgovor, context.createTextMessage("Korisnik je uspesno kreiran!"));
                        
                        break;
                    }finally{
                        if(et.isActive()){
                            et.rollback();
                        }
                        break;
                    }
                    
                }
                case 3:{
                    try{
                        et.begin();
                        String email = txtMsg.getStringProperty("email");
                        int IdKor = txtMsg.getIntProperty("IdKor");
                        Korisnik korisnik = em.find(Korisnik.class, IdKor);
                        if(korisnik == null){
                            producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                            et.commit();
                            break;
                        }
                        korisnik.setEmail(email);
                        em.persist(korisnik);
                        em.flush();
                        producer.send(queueOdgovor, context.createTextMessage("Email adresa korisnika je uspesno promenjena!"));
                        et.commit();
                        break;
                    }finally{
                        if(et.isActive()){
                            et.rollback();
                        }
                        break;
                    }
                    
                }
                case 4:{
                    try{
                        et.begin();
                        int IdMes = txtMsg.getIntProperty("IdMes");
                        int IdKor = txtMsg.getIntProperty("IdKor");
                        Korisnik korisnik = em.find(Korisnik.class, IdKor);
                        if(korisnik == null){
                            producer.send(queueOdgovor, context.createTextMessage("Korisnik ne postoji!"));
                            et.commit();
                            break;
                        }
                        Mesto mesto = em.find(Mesto.class, IdMes);
                        if (mesto == null) {
                            producer.send(queueOdgovor, context.createTextMessage("Mesto ne postoji!"));
                            et.commit();
                            break;
                        }
                        
                        korisnik.setIdMes(mesto);
                        em.merge(korisnik);
                        producer.send(queueOdgovor, context.createTextMessage("Mesto korisnika je uspesno promenjeno!"));
                        et.commit();
                        break;
                    }finally{
                        if(et.isActive()){
                            et.rollback();
                        }
                        break;
                    }
                }
                case 18:{
                    
                    try{
                        List<Mesto> mesta = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Mesta:\n");
                        for(Mesto m: mesta){
                            sb.append(m);
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
                case 19:{
                    try{
                        List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Korisnici:\n");
                        for(Korisnik k: korisnici){
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
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
