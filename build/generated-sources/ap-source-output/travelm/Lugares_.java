package travelm;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import travelm.Viajes;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-05-24T11:05:37", comments="EclipseLink-4.0.2.v20230616-r3bfa6ac6ddf76d7909adc5ea7ecaa47c02c007ed")
@StaticMetamodel(Lugares.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class Lugares_ { 

    public static volatile SingularAttribute<Lugares, Viajes> viajeId;
    public static volatile SingularAttribute<Lugares, BigDecimal> latitud;
    public static volatile SingularAttribute<Lugares, BigDecimal> longitud;
    public static volatile SingularAttribute<Lugares, Integer> id;
    public static volatile SingularAttribute<Lugares, String> nombre;

}