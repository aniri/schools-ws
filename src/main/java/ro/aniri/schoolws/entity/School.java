package ro.aniri.schoolws.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "schools")
@XmlRootElement
public class School implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "SIIIR")
    private Long siiir;
    
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "Denumire")
    private String denumire;
    
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Localitate")
    private String localitate;
    
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "Judet")
    private String judet;
    
    @NotNull
    @Column(name = "Lat")
    private double lat;
    
    @NotNull
    @Column(name = "Lng")
    private double lng;

    public School() {
    }

    public School(Long siiir) {
        this.siiir = siiir;
    }

    public School(Long siiir, String denumire, String localitate, String judet, double lat, double lng) {
        this.siiir = siiir;
        this.denumire = denumire;
        this.localitate = localitate;
        this.judet = judet;
        this.lat = lat;
        this.lng = lng;
    }

    public Long getSiiir() {
        return siiir;
    }

    public void setSiiir(Long siiir) {
        this.siiir = siiir;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siiir != null ? siiir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof School)) {
            return false;
        }
        School other = (School) object;
        if ((this.siiir == null && other.siiir != null) || (this.siiir != null && !this.siiir.equals(other.siiir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.aniri.schoolsws.School[ siiir=" + siiir + " ]";
    }
    
}
