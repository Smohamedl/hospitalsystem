package fr.hospitalsystem.app.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FixedDoctorPayment.class)
public abstract class FixedDoctorPayment_ {

	public static volatile SingularAttribute<FixedDoctorPayment, LocalDate> date;
	public static volatile SingularAttribute<FixedDoctorPayment, String> reference;
	public static volatile SingularAttribute<FixedDoctorPayment, Doctor> doctor;
	public static volatile SingularAttribute<FixedDoctorPayment, Boolean> paid;
	public static volatile SingularAttribute<FixedDoctorPayment, Long> id;

	public static final String DATE = "date";
	public static final String REFERENCE = "reference";
	public static final String DOCTOR = "doctor";
	public static final String PAID = "paid";
	public static final String ID = "id";

}

