package com.example.demo.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LibraryManagerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double salary;
    private Integer workingDays;

    @OneToOne(mappedBy = "libraryManagerEntity", cascade = CascadeType.ALL)
    private AccountEntity account;
}
