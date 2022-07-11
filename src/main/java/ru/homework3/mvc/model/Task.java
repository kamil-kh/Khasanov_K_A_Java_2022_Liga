package ru.homework3.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Cloneable {
    private Integer id;
    private Integer idUser;
    private String header;
    private String description;
    private String date;
    private String status;

    @Override
    public String toString() {
        return "ID задачи: " +
                id +
                " | Заголовок: " +
                header +
                " | Описание: " +
                description +
                " | Дата: " +
                date +
                " | Статус: " +
                status;
    }

    public String toStringCsv() {
        return id + "," + idUser + "," + header + "," + description + "," + date + "," + status;
    }

    public Task clone() {
        return new Task(id, idUser, header, description, date, status);
    }
}
