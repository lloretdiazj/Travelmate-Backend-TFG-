package travelm;

import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;
import travelm.Gastos;
import travelm.Lugares;
import travelm.Usuarios;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-05-24T11:05:37", comments="EclipseLink-4.0.2.v20230616-r3bfa6ac6ddf76d7909adc5ea7ecaa47c02c007ed")
@StaticMetamodel(Viajes.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class Viajes_ { 

    public static volatile CollectionAttribute<Viajes, Lugares> lugaresCollection;
    public static volatile CollectionAttribute<Viajes, Gastos> gastosCollection;
    public static volatile SingularAttribute<Viajes, String> fechaInicio;
    public static volatile SingularAttribute<Viajes, String> imagen;
    public static volatile SingularAttribute<Viajes, Integer> kilometrosRealizados;
    public static volatile SingularAttribute<Viajes, Integer> id;
    public static volatile SingularAttribute<Viajes, String> nombre;
    public static volatile SingularAttribute<Viajes, String> fechaFin;
    public static volatile SingularAttribute<Viajes, Usuarios> usuarioId;

}