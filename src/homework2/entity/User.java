package homework2.entity;

import java.util.HashMap;

public class User implements Cloneable {
    private int id;
    private String name;
    private HashMap<Integer,Task> tasks;

    public User(int id, String name, HashMap<Integer,Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public User clone() {
        User user = new User(id, name, (HashMap<Integer,Task>) tasks.clone());
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setTasks(HashMap<Integer,Task> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer,Task> getTasks() {
        return tasks;
    }

}
