/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Masa
 */
@Entity
@Table(name = "omiljenisnimak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Omiljenisnimak.findAll", query = "SELECT o FROM Omiljenisnimak o"),
    @NamedQuery(name = "Omiljenisnimak.findByIdOmiSni", query = "SELECT o FROM Omiljenisnimak o WHERE o.idOmiSni = :idOmiSni")})
public class Omiljenisnimak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdOmiSni")
    private Integer idOmiSni;
    @JoinColumn(name = "IdAudSni", referencedColumnName = "IdAudSni")
    @ManyToOne(optional = false)
    private Audiosnimak idAudSni;
    @JoinColumn(name = "IdKor", referencedColumnName = "IdKor")
    @ManyToOne(optional = false)
    private Korisnik idKor;

    public Omiljenisnimak() {
    }

    public Omiljenisnimak(Integer idOmiSni) {
        this.idOmiSni = idOmiSni;
    }

    public Integer getIdOmiSni() {
        return idOmiSni;
    }

    public void setIdOmiSni(Integer idOmiSni) {
        this.idOmiSni = idOmiSni;
    }

    public Audiosnimak getIdAudSni() {
        return idAudSni;
    }

    public void setIdAudSni(Audiosnimak idAudSni) {
        this.idAudSni = idAudSni;
    }

    public Korisnik getIdKor() {
        return idKor;
    }

    public void setIdKor(Korisnik idKor) {
        this.idKor = idKor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOmiSni != null ? idOmiSni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Omiljenisnimak)) {
            return false;
        }
        Omiljenisnimak other = (Omiljenisnimak) object;
        if ((this.idOmiSni == null && other.idOmiSni != null) || (this.idOmiSni != null && !this.idOmiSni.equals(other.idOmiSni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Omiljenisnimak[idOmiSni=" + idOmiSni +" IdKor="+idKor+" IdAudSni="+idAudSni+ "]";
    }
    
}
