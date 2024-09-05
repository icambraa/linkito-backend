package com.linkito.linkito.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    @Column(nullable = true)
    private String userId;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String tag;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int clicks = 0;

    @Column(nullable = true)  // Nuevo campo para la fecha de expiración
    private LocalDateTime expirationTime;
}
