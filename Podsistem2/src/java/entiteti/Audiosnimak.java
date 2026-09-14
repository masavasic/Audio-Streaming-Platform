/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Masa
 */
@Entity
@Table(name = "audiosnimak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audiosnimak.findAll", query = "SELECT a FROM Audiosnimak a"),
    @NamedQuery(name = "Audiosnimak.findByIdAudSni", query = "SELECT a FROM Audiosnimak a WHERE a.idAudSni = :idAudSni"),
    @NamedQuery(name = "Audiosnimak.findByNaziv", query = "SELECT a FROM Audiosnimak a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "Audiosnimak.findByTrajanje", query = "SELECT a FROM Audiosnimak a WHERE a.trajanje = :trajanje"),
    @NamedQuery(name = "Audiosnimak.findByDatumVreme", query = "SELECT a FROM Audiosnimak a WHERE a.datumVreme = :datumVreme")})
public class Audiosnimak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAudSni")
    private Integer idAudSni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Trajanje")
    @Temporal(TemporalType.TIME)
    private Date trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @JoinColumn(name = "IdVlasnika", referencedColumnName = "IdKor")
    @ManyToOne(optional = false)
    private Korisnik idVlasnika;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAudSni")
    private List<Pripada> pripadaList;

    public Audiosnimak() {
    }

    public Audiosnimak(Integer idAudSni) {
        this.idAudSni = idAudSni;
    }

    public Audiosnimak(Integer idAudSni, String naziv, Date trajanje, Date datumVreme) {
        this.idAudSni = idAudSni;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datumVreme = datumVreme;
    }

    public Integer getIdAudSni() {
        return idAudSni;
    }

    public void setIdAudSni(Integer idAudSni) {
        this.idAudSni = idAudSni;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Date getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(Date trajanje) {
        this.trajanje = trajanje;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public Korisnik getIdVlasnika() {
        return idVlasnika;
    }

    public void setIdVlasnika(Korisnik idVlasnika) {
        this.idVlasnika = idVlasnika;
    }

    @XmlTransient
    public List<Pripada> getPripadaList() {
        return pripadaList;
    }

    public void setPripadaList(List<Pripada> pripadaList) {
        this.pripadaList = pripadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAudSni != null ? idAudSni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audiosnimak)) {
            return false;
        }
        Audiosnimak other = (Audiosnimak) object;
        if ((this.idAudSni == null && other.idAudSni != null) || (this.idAudSni != null && !this.idAudSni.equals(other.idAudSni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Audiosnimak[idAudSni=" + idAudSni +" naziv="+naziv+" trajanje="+trajanje+" datumVreme="+datumVreme+" idVlasnika="+idVlasnika+ "]";
    }
    
}
