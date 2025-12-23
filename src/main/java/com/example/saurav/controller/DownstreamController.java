package com.example.saurav.controller;

import com.example.saurav.model.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Fake downstream endpoint.
 *
 * You can control behavior using query params:
 * - delayMs: adds artificial delay (simulates slowness)
 * - fail: if true, throws 503 (simulates an outage)
 */
@RestController
@RequestMapping("/downstream")
public class DownstreamController {

    @GetMapping("/employees/{id}")
    public EmployeeDto getEmployee(@PathVariable String id,
                                   @RequestParam(defaultValue = "0") long delayMs,
                                   @RequestParam(defaultValue = "false") boolean fail) {

        // Simulate slowness
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Simulate failure
        if (fail) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Downstream is failing (simulated)");
        }

        // Normal success
        return new EmployeeDto(id, "Aarav", "IT", 120000);
    }
}
