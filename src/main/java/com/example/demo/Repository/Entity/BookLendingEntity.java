package com.example.demo.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookLendingEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private Date borrowDate;
    private Date expiryDate;
    private String status;
    private Integer countBooks;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private ReaderEntity reader;
}
