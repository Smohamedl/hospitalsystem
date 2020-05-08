package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Provider.class)
public abstract class Provider_ {

	public static volatile SingularAttribute<Provider, String> name;
	public static volatile SingularAttribute<Provider, String> tel;
	public static volatile SingularAttribute<Provider, String> adress;
	public static volatile SingularAttribute<Provider, Long> id;

	public static final String NAME = "name";
	public static final String TEL = "tel";
	public static final String ADRESS = "adress";
	public static final String ID = "id";

}

