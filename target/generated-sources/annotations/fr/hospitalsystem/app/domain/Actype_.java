package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Actype.class)
public abstract class Actype_ {

	public static volatile SingularAttribute<Actype, Double> price;
	public static volatile SingularAttribute<Actype, MedicalService> medicalService;
	public static volatile SingularAttribute<Actype, String> name;
	public static volatile SingularAttribute<Actype, Long> id;

	public static final String PRICE = "price";
	public static final String MEDICAL_SERVICE = "medicalService";
	public static final String NAME = "name";
	public static final String ID = "id";

}

