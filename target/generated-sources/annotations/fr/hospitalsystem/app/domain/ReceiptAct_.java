package fr.hospitalsystem.app.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReceiptAct.class)
public abstract class ReceiptAct_ {

	public static volatile SingularAttribute<ReceiptAct, LocalDate> date;
	public static volatile SingularAttribute<ReceiptAct, Double> total;
	public static volatile SingularAttribute<ReceiptAct, Act> act;
	public static volatile SingularAttribute<ReceiptAct, Boolean> paidDoctor;
	public static volatile SingularAttribute<ReceiptAct, Boolean> paid;
	public static volatile SingularAttribute<ReceiptAct, Long> id;

	public static final String DATE = "date";
	public static final String TOTAL = "total";
	public static final String ACT = "act";
	public static final String PAID_DOCTOR = "paidDoctor";
	public static final String PAID = "paid";
	public static final String ID = "id";

}

