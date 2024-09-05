package com.linkito.linkito.repository;

import com.linkito.linkito.model.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByShortUrl(String shortUrl);
    Optional<ShortenedUrl> findByOriginalUrl(String originalUrl);
    List<ShortenedUrl> findAllByUserId(String userId);

    // Nuevo método para encontrar URLs expiradas
    List<ShortenedUrl> findAllByExpirationTimeBefore(LocalDateTime dateTime);
}
