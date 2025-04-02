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
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date membershipDate;
    private Date expiryTime;
    private Double membershipFee;
    private String note;
    private String status;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private ReaderEntity reader;
}
