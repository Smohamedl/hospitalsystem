package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SocialOrganizationRegimen.class)
public abstract class SocialOrganizationRegimen_ {

	public static volatile SingularAttribute<SocialOrganizationRegimen, Double> percentage;
	public static volatile SingularAttribute<SocialOrganizationRegimen, SocialOrganization> socialOrganization;
	public static volatile SingularAttribute<SocialOrganizationRegimen, Long> id;

	public static final String PERCENTAGE = "percentage";
	public static final String SOCIAL_ORGANIZATION = "socialOrganization";
	public static final String ID = "id";

}

