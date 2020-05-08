package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SocialOrganization.class)
public abstract class SocialOrganization_ {

	public static volatile SingularAttribute<SocialOrganization, String> name;
	public static volatile SingularAttribute<SocialOrganization, Long> id;
	public static volatile SetAttribute<SocialOrganization, SocialOrganizationRegimen> socialOrganizationRegimen;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String SOCIAL_ORGANIZATION_REGIMEN = "socialOrganizationRegimen";

}

