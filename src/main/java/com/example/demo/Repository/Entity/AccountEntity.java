package com.example.demo.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String note;
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "library_manager_id")
    private LibraryManagerEntity libraryManagerEntity;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReaderEntity> readerEntities;
}
