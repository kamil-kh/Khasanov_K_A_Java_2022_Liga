package ru.homework3.mvc.model.metamodel;

import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Task.class)
public abstract class Task_ {

	public static volatile SingularAttribute<Task, String> date;
	public static volatile SingularAttribute<Task, String> header;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, Integer> id;
	public static volatile SingularAttribute<Task, User> user;
	public static volatile SingularAttribute<Task, String> status;

	public static final String DATE = "date";
	public static final String HEADER = "header";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String STATUS = "status";

}

