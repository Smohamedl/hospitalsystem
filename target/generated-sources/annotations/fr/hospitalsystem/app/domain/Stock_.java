package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Stock.class)
public abstract class Stock_ {

	public static volatile SingularAttribute<Stock, Product> product;
	public static volatile SingularAttribute<Stock, Integer> quantity;
	public static volatile SingularAttribute<Stock, Long> id;

	public static final String PRODUCT = "product";
	public static final String QUANTITY = "quantity";
	public static final String ID = "id";

}

