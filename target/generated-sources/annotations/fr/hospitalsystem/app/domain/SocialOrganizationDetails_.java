package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SocialOrganizationDetails.class)
public abstract class SocialOrganizationDetails_ {

	public static volatile SingularAttribute<SocialOrganizationDetails, Long> registrationNumber;
	public static volatile SingularAttribute<SocialOrganizationDetails, String> matriculeNumber;
	public static volatile SingularAttribute<SocialOrganizationDetails, Long> id;
	public static volatile SingularAttribute<SocialOrganizationDetails, SocialOrganizationRegimen> socialOrganizationRegimen;

	public static final String REGISTRATION_NUMBER = "registrationNumber";
	public static final String MATRICULE_NUMBER = "matriculeNumber";
	public static final String ID = "id";
	public static final String SOCIAL_ORGANIZATION_REGIMEN = "socialOrganizationRegimen";

}

