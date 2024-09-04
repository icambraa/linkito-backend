package com.linkito.linkito.service;

import com.linkito.linkito.model.ShortenedUrl;
import com.linkito.linkito.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String shortenUrl(String originalUrl) {
        Optional<ShortenedUrl> existingUrl = repository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        } else {
            String shortUrl;
            do {
                shortUrl = generateShortUrl();
            } while (repository.findByShortUrl(shortUrl).isPresent());
    
            // Aquí asegúrate de que solo guardas el valor de la URL, no un objeto JSON
            ShortenedUrl shortenedUrl = new ShortenedUrl();
            shortenedUrl.setOriginalUrl(originalUrl.trim());  // Solo guarda el texto de la URL
            shortenedUrl.setShortUrl(shortUrl);
            repository.save(shortenedUrl);
    
            return shortUrl;
        }
    }
    
    
    
}
