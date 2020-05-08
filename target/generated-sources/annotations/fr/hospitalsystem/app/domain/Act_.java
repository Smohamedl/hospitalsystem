package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Act.class)
public abstract class Act_ {

	public static volatile SingularAttribute<Act, Doctor> doctor;
	public static volatile SingularAttribute<Act, ReceiptAct> receiptAct;
	public static volatile SetAttribute<Act, Actype> actypes;
	public static volatile SingularAttribute<Act, String> patientName;
	public static volatile SingularAttribute<Act, MedicalService> medicalService;
	public static volatile SingularAttribute<Act, Patient> patient;
	public static volatile SingularAttribute<Act, Long> id;

	public static final String DOCTOR = "doctor";
	public static final String RECEIPT_ACT = "receiptAct";
	public static final String ACTYPES = "actypes";
	public static final String PATIENT_NAME = "patientName";
	public static final String MEDICAL_SERVICE = "medicalService";
	public static final String PATIENT = "patient";
	public static final String ID = "id";

}

