package travelm;

import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;
import travelm.Viajes;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-05-24T11:05:37", comments="EclipseLink-4.0.2.v20230616-r3bfa6ac6ddf76d7909adc5ea7ecaa47c02c007ed")
@StaticMetamodel(Usuarios.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class Usuarios_ { 

    public static volatile CollectionAttribute<Usuarios, Viajes> viajesCollection;
    public static volatile SingularAttribute<Usuarios, String> imagen;
    public static volatile SingularAttribute<Usuarios, String> contrasena;
    public static volatile SingularAttribute<Usuarios, Integer> id;
    public static volatile SingularAttribute<Usuarios, String> nombre;
    public static volatile SingularAttribute<Usuarios, String> email;

}