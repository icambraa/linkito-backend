package com.linkito.linkito.repository;

import com.linkito.linkito.model.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByShortUrl(String shortUrl);
    Optional<ShortenedUrl> findByOriginalUrl(String originalUrl);
}
