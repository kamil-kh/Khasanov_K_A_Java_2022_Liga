package ru.homework3.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private HashMap<Integer,Task> tasks;

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public String toStringCsv() {
        return id + "," + name;
    }
}
