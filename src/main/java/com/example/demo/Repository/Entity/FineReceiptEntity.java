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
public class FineReceiptEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private Double amount;
    private String status;
    private String note;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private ReaderEntity reader;
}
