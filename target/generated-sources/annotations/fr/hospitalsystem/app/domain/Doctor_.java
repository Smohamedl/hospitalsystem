package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Doctor.class)
public abstract class Doctor_ {

	public static volatile SingularAttribute<Doctor, String> firstname;
	public static volatile SingularAttribute<Doctor, String> address;
	public static volatile SingularAttribute<Doctor, Boolean> specialist;
	public static volatile SingularAttribute<Doctor, Boolean> partOfHospitalIncome;
	public static volatile SingularAttribute<Doctor, MedicalService> medicalService;
	public static volatile SingularAttribute<Doctor, String> name;
	public static volatile SingularAttribute<Doctor, String> tel;
	public static volatile SingularAttribute<Doctor, Long> id;
	public static volatile SingularAttribute<Doctor, String> job;
	public static volatile SingularAttribute<Doctor, Double> salary;

	public static final String FIRSTNAME = "firstname";
	public static final String ADDRESS = "address";
	public static final String SPECIALIST = "specialist";
	public static final String PART_OF_HOSPITAL_INCOME = "partOfHospitalIncome";
	public static final String MEDICAL_SERVICE = "medicalService";
	public static final String NAME = "name";
	public static final String TEL = "tel";
	public static final String ID = "id";
	public static final String JOB = "job";
	public static final String SALARY = "salary";

}

