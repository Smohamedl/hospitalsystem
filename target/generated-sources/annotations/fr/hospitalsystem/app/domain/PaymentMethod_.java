package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentMethod.class)
public abstract class PaymentMethod_ {

	public static volatile SingularAttribute<PaymentMethod, String> name;
	public static volatile SingularAttribute<PaymentMethod, Long> id;
	public static volatile SetAttribute<PaymentMethod, Act> acts;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String ACTS = "acts";

}

