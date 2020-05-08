package fr.hospitalsystem.app.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Hospitalization.class)
public abstract class Hospitalization_ {

	public static volatile SingularAttribute<Hospitalization, LocalDate> date;
	public static volatile SingularAttribute<Hospitalization, Patient> patient;
	public static volatile SingularAttribute<Hospitalization, MedicalService> medicalService;
	public static volatile SingularAttribute<Hospitalization, String> description;
	public static volatile SingularAttribute<Hospitalization, Long> id;

	public static final String DATE = "date";
	public static final String PATIENT = "patient";
	public static final String MEDICAL_SERVICE = "medicalService";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";

}

