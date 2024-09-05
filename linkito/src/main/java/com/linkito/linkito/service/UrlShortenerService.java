package com.linkito.linkito.service;

import com.linkito.linkito.model.ShortenedUrl;
import com.linkito.linkito.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortenedUrlRepository repository;

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();
        while (shortUrl.length() < 8) {
            int index = random.nextInt(characters.length());
            shortUrl.append(characters.charAt(index));
        }
        return shortUrl.toString();
    }

    public ShortenedUrl shortenUrl(String originalUrl, String userId, String password, String tag) {
        Optional<ShortenedUrl> existingUrl = repository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        } else {
            String shortUrl;
            do {
                shortUrl = generateShortUrl();
            } while (repository.findByShortUrl(shortUrl).isPresent());

            ShortenedUrl shortenedUrl = new ShortenedUrl();
            shortenedUrl.setOriginalUrl(originalUrl.trim());
            shortenedUrl.setShortUrl(shortUrl);
            shortenedUrl.setUserId(userId);

            if (userId == null || userId.trim().isEmpty()) {
                shortenedUrl.setExpirationTime(LocalDateTime.now().plusHours(24));  // Expira en 24 horas
            }

            shortenedUrl.setPassword((password != null && !password.trim().isEmpty()) ? password : null);
            shortenedUrl.setTag((tag != null && !tag.trim().isEmpty()) ? tag : null);

            return repository.save(shortenedUrl);
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredUrls() {
        List<ShortenedUrl> expiredUrls = repository.findAllByExpirationTimeBefore(LocalDateTime.now());
        for (ShortenedUrl url : expiredUrls) {
            repository.delete(url);
        }
    }
}
