/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package travelm;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author jospl
 */
@Entity
@Table(name = "lugares")
@NamedQueries({
    @NamedQuery(name = "Lugares.findAll", query = "SELECT l FROM Lugares l"),
    @NamedQuery(name = "Lugares.findById", query = "SELECT l FROM Lugares l WHERE l.id = :id"),
    @NamedQuery(name = "Lugares.findByNombre", query = "SELECT l FROM Lugares l WHERE l.nombre = :nombre"),
    @NamedQuery(name = "Lugares.findByLatitud", query = "SELECT l FROM Lugares l WHERE l.latitud = :latitud"),
    @NamedQuery(name = "Lugares.findByLongitud", query = "SELECT l FROM Lugares l WHERE l.longitud = :longitud")})
public class Lugares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitud")
    private BigDecimal latitud;
    @Column(name = "longitud")
    private BigDecimal longitud;
    @JoinColumn(name = "viaje_id", referencedColumnName = "id")
    @ManyToOne
    private Viajes viajeId;

    public Lugares() {
    }

    public Lugares(Integer id) {
        this.id = id;
    }

    public Lugares(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Viajes getViajeId() {
        return viajeId;
    }

    public void setViajeId(Viajes viajeId) {
        this.viajeId = viajeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lugares)) {
            return false;
        }
        Lugares other = (Lugares) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "travelm.Lugares[ id=" + id + " ]";
    }
    
}
