package ru.homework3.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private int id;
    private int idUser;
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
}
