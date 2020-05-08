package fr.hospitalsystem.app.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GuardSchedule.class)
public abstract class GuardSchedule_ {

	public static volatile SingularAttribute<GuardSchedule, Double> payement;
	public static volatile SingularAttribute<GuardSchedule, Integer> start;
	public static volatile SingularAttribute<GuardSchedule, String> name;
	public static volatile SingularAttribute<GuardSchedule, Integer> end;
	public static volatile SingularAttribute<GuardSchedule, Long> id;

	public static final String PAYEMENT = "payement";
	public static final String START = "start";
	public static final String NAME = "name";
	public static final String END = "end";
	public static final String ID = "id";

}

