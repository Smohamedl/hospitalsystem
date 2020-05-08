package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Patient.class)
public abstract class Patient_ {

	public static volatile SingularAttribute<Patient, String> firstname;
	public static volatile SingularAttribute<Patient, String> address;
	public static volatile SingularAttribute<Patient, String> name;
	public static volatile SingularAttribute<Patient, SocialOrganizationDetails> socialOrganizationDetails;
	public static volatile SingularAttribute<Patient, String> tel;
	public static volatile SingularAttribute<Patient, Long> id;

	public static final String FIRSTNAME = "firstname";
	public static final String ADDRESS = "address";
	public static final String NAME = "name";
	public static final String SOCIAL_ORGANIZATION_DETAILS = "socialOrganizationDetails";
	public static final String TEL = "tel";
	public static final String ID = "id";

}

