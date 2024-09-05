package com.linkito.linkito.controller;

import com.linkito.linkito.model.ShortenedUrl;
import com.linkito.linkito.repository.ShortenedUrlRepository;
import com.linkito.linkito.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private ShortenedUrlRepository repository;

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenedUrl> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");
        String userId = request.get("userId");
        String password = request.get("password");
        String tag = request.get("tag");

        ShortenedUrl shortenedUrl = urlShortenerService.shortenUrl(originalUrl, userId,
                (password != null && !password.trim().isEmpty()) ? password : null,
                (tag != null && !tag.trim().isEmpty()) ? tag : null);

        return ResponseEntity.ok(shortenedUrl);
    }

    @PostMapping("/api/links/{shortUrl}/access")
    public ResponseEntity<?> accessLink(@PathVariable String shortUrl, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        Optional<ShortenedUrl> shortenedUrlOpt = repository.findByShortUrl(shortUrl);

        if (shortenedUrlOpt.isPresent()) {
            ShortenedUrl shortenedUrl = shortenedUrlOpt.get();

            if (shortenedUrl.getPassword() != null && !shortenedUrl.getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Contraseña incorrecta.");
            }

            shortenedUrl.setClicks(shortenedUrl.getClicks() + 1);
            repository.save(shortenedUrl);

            return ResponseEntity.ok(Map.of("originalUrl", shortenedUrl.getOriginalUrl()));
        } else {
            return ResponseEntity.status(404).body("Enlace no encontrado.");
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<ShortenedUrl> shortenedUrl = repository.findByShortUrl(shortUrl);

        if (shortenedUrl.isPresent()) {
            ShortenedUrl url = shortenedUrl.get();

            if (url.getPassword() != null) {
                return ResponseEntity.status(302).header("Location", "http://localhost:5173/password-protected/" + shortUrl).build();
            }

            url.setClicks(url.getClicks() + 1);
            repository.save(url);
            return ResponseEntity.status(302).header("Location", url.getOriginalUrl()).build();
        } else {
            return ResponseEntity.status(404).body("Enlace no encontrado.");
        }
    }

    @GetMapping("/api/links")
    public ResponseEntity<?> getUserLinks(@RequestParam String userId) {
        List<ShortenedUrl> urls = repository.findAllByUserId(userId);

        if (urls.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontraron enlaces para este usuario.");
        } else {
            return ResponseEntity.ok(urls);
        }
    }

    @GetMapping("/api/stats/total-clicks")
    public ResponseEntity<?> getTotalClicks(@RequestParam String userId) {
        int totalClicks = repository.findAllByUserId(userId).stream().mapToInt(ShortenedUrl::getClicks).sum();
        return ResponseEntity.ok(Map.of("totalClicks", totalClicks));
    }

    @GetMapping("/api/stats/most-popular")
    public ResponseEntity<?> getMostPopularLink(@RequestParam String userId) {
        Optional<ShortenedUrl> mostPopularUrl = repository.findAllByUserId(userId).stream()
                .max((url1, url2) -> Integer.compare(url1.getClicks(), url2.getClicks()));

        if (mostPopularUrl.isPresent()) {
            return ResponseEntity.ok(mostPopularUrl.get());
        } else {
            return ResponseEntity.status(404).body("No se encontraron enlaces para este usuario.");
        }
    }

    @DeleteMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> deleteLink(@PathVariable String shortUrl) {
        Optional<ShortenedUrl> shortenedUrlOpt = repository.findByShortUrl(shortUrl);

        if (shortenedUrlOpt.isPresent()) {
            repository.delete(shortenedUrlOpt.get());
            return ResponseEntity.ok().body("Enlace eliminado exitosamente.");
        } else {
            return ResponseEntity.status(404).body("Enlace no encontrado.");
        }
    }
}
