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
@Table(name = "gastos")
@NamedQueries({
    @NamedQuery(name = "Gastos.findAll", query = "SELECT g FROM Gastos g"),
    @NamedQuery(name = "Gastos.findById", query = "SELECT g FROM Gastos g WHERE g.id = :id"),
    @NamedQuery(name = "Gastos.findByDescripcion", query = "SELECT g FROM Gastos g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "Gastos.findByGasto", query = "SELECT g FROM Gastos g WHERE g.gasto = :gasto")})
public class Gastos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "gasto")
    private BigDecimal gasto;
    @JoinColumn(name = "viaje_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Viajes viajeId;

    public Gastos() {
    }

    public Gastos(Integer id) {
        this.id = id;
    }

    public Gastos(Integer id, String descripcion, BigDecimal gasto) {
        this.id = id;
        this.descripcion = descripcion;
        this.gasto = gasto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getGasto() {
        return gasto;
    }

    public void setGasto(BigDecimal gasto) {
        this.gasto = gasto;
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
        if (!(object instanceof Gastos)) {
            return false;
        }
        Gastos other = (Gastos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "travelm.Gastos[ id=" + id + " ]";
    }
    
}
