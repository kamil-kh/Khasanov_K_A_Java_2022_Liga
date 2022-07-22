package ru.homework3.mvc.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "header")
    private String header;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private String date;

    @Column(name = "status")
    private String status;
}
