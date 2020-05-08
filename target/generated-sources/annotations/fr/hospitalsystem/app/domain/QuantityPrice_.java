package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuantityPrice.class)
public abstract class QuantityPrice_ {

	public static volatile SingularAttribute<QuantityPrice, Product> product;
	public static volatile SingularAttribute<QuantityPrice, Integer> quantity;
	public static volatile SingularAttribute<QuantityPrice, Double> price;
	public static volatile SingularAttribute<QuantityPrice, Long> id;
	public static volatile SingularAttribute<QuantityPrice, Order> order;

	public static final String PRODUCT = "product";
	public static final String QUANTITY = "quantity";
	public static final String PRICE = "price";
	public static final String ID = "id";
	public static final String ORDER = "order";

}

