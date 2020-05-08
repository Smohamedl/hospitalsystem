package fr.hospitalsystem.app.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DoctorPartPayment.class)
public abstract class DoctorPartPayment_ {

	public static volatile SingularAttribute<DoctorPartPayment, String> reference;
	public static volatile SingularAttribute<DoctorPartPayment, LocalDate> date;
	public static volatile SingularAttribute<DoctorPartPayment, Doctor> doctor;
	public static volatile SingularAttribute<DoctorPartPayment, Double> total;
	public static volatile SingularAttribute<DoctorPartPayment, Long> id;

	public static final String REFERENCE = "reference";
	public static final String DATE = "date";
	public static final String DOCTOR = "doctor";
	public static final String TOTAL = "total";
	public static final String ID = "id";

}

