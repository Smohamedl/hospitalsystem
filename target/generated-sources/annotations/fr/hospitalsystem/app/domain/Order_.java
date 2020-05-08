package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ {

	public static volatile SingularAttribute<Order, Provider> provider;
	public static volatile SingularAttribute<Order, Long> id;
	public static volatile SetAttribute<Order, QuantityPrice> quantityPrices;

	public static final String PROVIDER = "provider";
	public static final String ID = "id";
	public static final String QUANTITY_PRICES = "quantityPrices";

}

