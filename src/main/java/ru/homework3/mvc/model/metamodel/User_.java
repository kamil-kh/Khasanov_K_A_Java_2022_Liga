package ru.homework3.mvc.model.metamodel;

import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile ListAttribute<User, Task> tasks;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String TASKS = "tasks";

}

