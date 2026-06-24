package com.taskflow.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    Map<String, String> health() {
        return Map.of("status", "UP", "service", "taskflow-server");
    }
}
