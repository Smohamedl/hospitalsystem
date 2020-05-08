package fr.hospitalsystem.app.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Session.class)
public abstract class Session_ {

	public static volatile SingularAttribute<Session, Double> totalCheck;
	public static volatile SingularAttribute<Session, Double> total;
	public static volatile SingularAttribute<Session, Double> totalCash;
	public static volatile SingularAttribute<Session, Double> totalPC;
	public static volatile SingularAttribute<Session, Long> id;
	public static volatile SingularAttribute<Session, Instant> created_date;
	public static volatile SingularAttribute<Session, User> jhi_user;
	public static volatile SingularAttribute<Session, String> created_by;

	public static final String TOTAL_CHECK = "totalCheck";
	public static final String TOTAL = "total";
	public static final String TOTAL_CASH = "totalCash";
	public static final String TOTAL_PC = "totalPC";
	public static final String ID = "id";
	public static final String CREATED_DATE = "created_date";
	public static final String JHI_USER = "jhi_user";
	public static final String CREATED_BY = "created_by";

}

