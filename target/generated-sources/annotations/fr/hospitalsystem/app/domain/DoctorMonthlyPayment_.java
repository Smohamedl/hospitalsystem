package fr.hospitalsystem.app.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DoctorMonthlyPayment.class)
public abstract class DoctorMonthlyPayment_ {

	public static volatile SingularAttribute<DoctorMonthlyPayment, LocalDate> date;
	public static volatile SingularAttribute<DoctorMonthlyPayment, String> reference;
	public static volatile SingularAttribute<DoctorMonthlyPayment, Doctor> doctor;
	public static volatile SingularAttribute<DoctorMonthlyPayment, Boolean> paid;
	public static volatile SingularAttribute<DoctorMonthlyPayment, Long> id;

	public static final String DATE = "date";
	public static final String REFERENCE = "reference";
	public static final String DOCTOR = "doctor";
	public static final String PAID = "paid";
	public static final String ID = "id";

}

