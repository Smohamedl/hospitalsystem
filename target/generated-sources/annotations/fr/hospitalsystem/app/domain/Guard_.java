package fr.hospitalsystem.app.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Guard.class)
public abstract class Guard_ {

	public static volatile SingularAttribute<Guard, LocalDate> date;
	public static volatile SingularAttribute<Guard, Doctor> doctor;
	public static volatile SingularAttribute<Guard, Long> id;
	public static volatile SingularAttribute<Guard, MedicalService> doctorMedicalService;
	public static volatile SingularAttribute<Guard, GuardSchedule> guardSchedule;

	public static final String DATE = "date";
	public static final String DOCTOR = "doctor";
	public static final String ID = "id";
	public static final String DOCTOR_MEDICAL_SERVICE = "doctorMedicalService";
	public static final String GUARD_SCHEDULE = "guardSchedule";

}

