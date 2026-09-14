/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Masa
 */
@Entity
@Table(name = "slusanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slusanje.findAll", query = "SELECT s FROM Slusanje s"),
    @NamedQuery(name = "Slusanje.findByIdSlu", query = "SELECT s FROM Slusanje s WHERE s.idSlu = :idSlu"),
    @NamedQuery(name = "Slusanje.findByDatumVreme", query = "SELECT s FROM Slusanje s WHERE s.datumVreme = :datumVreme"),
    @NamedQuery(name = "Slusanje.findBySekundZapoceto", query = "SELECT s FROM Slusanje s WHERE s.sekundZapoceto = :sekundZapoceto"),
    @NamedQuery(name = "Slusanje.findBySekundOdslusano", query = "SELECT s FROM Slusanje s WHERE s.sekundOdslusano = :sekundOdslusano")})
public class Slusanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdSlu")
    private Integer idSlu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundZapoceto")
    private int sekundZapoceto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundOdslusano")
    private int sekundOdslusano;
    @JoinColumn(name = "IdAudSni", referencedColumnName = "IdAudSni")
    @ManyToOne(optional = false)
    private Audiosnimak idAudSni;
    @JoinColumn(name = "IdKor", referencedColumnName = "IdKor")
    @ManyToOne(optional = false)
    private Korisnik idKor;

    public Slusanje() {
    }

    public Slusanje(Integer idSlu) {
        this.idSlu = idSlu;
    }

    public Slusanje(Integer idSlu, Date datumVreme, int sekundZapoceto, int sekundOdslusano) {
        this.idSlu = idSlu;
        this.datumVreme = datumVreme;
        this.sekundZapoceto = sekundZapoceto;
        this.sekundOdslusano = sekundOdslusano;
    }

    public Integer getIdSlu() {
        return idSlu;
    }

    public void setIdSlu(Integer idSlu) {
        this.idSlu = idSlu;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public int getSekundZapoceto() {
        return sekundZapoceto;
    }

    public void setSekundZapoceto(int sekundZapoceto) {
        this.sekundZapoceto = sekundZapoceto;
    }

    public int getSekundOdslusano() {
        return sekundOdslusano;
    }

    public void setSekundOdslusano(int sekundOdslusano) {
        this.sekundOdslusano = sekundOdslusano;
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
        hash += (idSlu != null ? idSlu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slusanje)) {
            return false;
        }
        Slusanje other = (Slusanje) object;
        if ((this.idSlu == null && other.idSlu != null) || (this.idSlu != null && !this.idSlu.equals(other.idSlu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Slusanje[idSlu=" + idSlu +" datumVreme="+datumVreme+" sekundZapoceto="+sekundZapoceto+" sekundOdslusano="+sekundOdslusano+" IdKor="+idKor+" IdAudSni="+idAudSni+ "]";
    }
    
}
