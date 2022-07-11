package ru.homework3.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Integer id;
    private String header;
    private String description;
    private String date;
    private String status = "Новая";
}
