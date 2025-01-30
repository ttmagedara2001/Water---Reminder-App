package com.example.h2obuddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/tips")
public class HydrationTipsController {

    @GetMapping
    public ResponseEntity<String> getHydrationTip() {
        // Static list of hydration tips
        List<String> tips = List.of(
            "Drink at least 8 glasses of water a day.",
            "Drink water before and after exercise.",
            "Always carry a water bottle with you."
        );
        
        // Randomly select a tip
        Random rand = new Random();
        String randomTip = tips.get(rand.nextInt(tips.size()));

        return ResponseEntity.ok(randomTip);
    }
}
