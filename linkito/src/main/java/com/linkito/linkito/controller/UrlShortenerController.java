package com.linkito.linkito.controller;

import com.linkito.linkito.model.ShortenedUrl;
import com.linkito.linkito.service.UrlShortenerService;
import com.linkito.linkito.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // Para permitir solicitudes desde cualquier origen
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private ShortenedUrlRepository repository;

    @PostMapping("/api/shorten")
    public String shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");  // Extraer la URL del objeto JSON
        return urlShortenerService.shortenUrl(originalUrl);
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<ShortenedUrl> shortenedUrl = repository.findByShortUrl(shortUrl);
    
        if (shortenedUrl.isPresent()) {
            // Redirige a la URL original, asegurando que no haya comillas adicionales
            String originalUrl = shortenedUrl.get().getOriginalUrl().trim();
            return new RedirectView(originalUrl);
        } else {
            // Si no se encuentra, redirige a una página de error o devuelve un 404
            return new RedirectView("/error/404");
        }
    }
    
    
}
