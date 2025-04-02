package com.example.demo.Repository.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvalidationTokenEntity {
    @Id
    String id;
    Date expiryTime;
}
