package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, Stock> stock;
	public static volatile SingularAttribute<Product, Category> category;
	public static volatile SetAttribute<Product, QuantityPrice> quantityPrices;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String STOCK = "stock";
	public static final String CATEGORY = "category";
	public static final String QUANTITY_PRICES = "quantityPrices";

}

