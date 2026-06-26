package com.streaming.telemetria.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/telemetria")
public class TelemetriaController {

    @PostMapping("/evento")
    public ResponseEntity<?> receberEvento(@RequestBody Map<String, Object> estadoPlayer) { return null; }
}
