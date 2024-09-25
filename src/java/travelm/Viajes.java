/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package travelm;

import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jospl
 */
@Entity
@Table(name = "viajes")
@NamedQueries({
    @NamedQuery(name = "Viajes.findAll", query = "SELECT v FROM Viajes v"),
    @NamedQuery(name = "Viajes.findById", query = "SELECT v FROM Viajes v WHERE v.id = :id"),
    @NamedQuery(name = "Viajes.findByNombre", query = "SELECT v FROM Viajes v WHERE v.nombre = :nombre"),
    @NamedQuery(name = "Viajes.findByFechaInicio", query = "SELECT v FROM Viajes v WHERE v.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Viajes.findByFechaFin", query = "SELECT v FROM Viajes v WHERE v.fechaFin = :fechaFin"),
    @NamedQuery(name = "Viajes.findByKilometrosRealizados", query = "SELECT v FROM Viajes v WHERE v.kilometrosRealizados = :kilometrosRealizados")})
public class Viajes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_inicio")
    private String fechaInicio;
    @Column(name = "fecha_fin")
    private String fechaFin;
    @Column(name = "kilometros_realizados")
    private Integer kilometrosRealizados;
    @Lob
    @Column(name = "imagen")
    private String imagen;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioId;
    @OneToMany(mappedBy = "viajeId")
    @JsonbTransient
    private Collection<Lugares> lugaresCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viajeId")
    @JsonbTransient
    private Collection<Gastos> gastosCollection;

    public Viajes() {
    }

    public Viajes(Integer id) {
        this.id = id;
    }

    public Viajes(Integer id, String nombre) {
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

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getKilometrosRealizados() {
        return kilometrosRealizados;
    }

    public void setKilometrosRealizados(Integer kilometrosRealizados) {
        this.kilometrosRealizados = kilometrosRealizados;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Usuarios getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuarios usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Collection<Lugares> getLugaresCollection() {
        return lugaresCollection;
    }

    public void setLugaresCollection(Collection<Lugares> lugaresCollection) {
        this.lugaresCollection = lugaresCollection;
    }

    public Collection<Gastos> getGastosCollection() {
        return gastosCollection;
    }

    public void setGastosCollection(Collection<Gastos> gastosCollection) {
        this.gastosCollection = gastosCollection;
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
        if (!(object instanceof Viajes)) {
            return false;
        }
        Viajes other = (Viajes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "travelm.Viajes[ id=" + id + " ]";
    }
    
}
