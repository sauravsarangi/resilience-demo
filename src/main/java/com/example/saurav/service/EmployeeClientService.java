package com.example.saurav.service;

import com.example.saurav.model.EmployeeDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

/**
 * Calls the downstream endpoint safely using:
 * - CircuitBreaker (stop calling when clearly failing)
 * - Retry (controlled re-tries for transient issues)
 * - TimeLimiter (overall time budget)
 */
@Service
public class EmployeeClientService {

    private final WebClient webClient;

    public EmployeeClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * How this works in real life:
     * 1) CircuitBreaker decides if call is allowed (OPEN = blocked)
     * 2) If allowed, WebClient call is made
     * 3) Retry retries on configured exceptions
     * 4) TimeLimiter stops the whole operation if it exceeds timeoutDuration
     */
    @CircuitBreaker(name = "employeeService", fallbackMethod = "fallback")
    @Retry(name = "employeeService")
    @TimeLimiter(name = "employeeService")
    public CompletableFuture<EmployeeDto> fetchEmployee(String id, long delayMs, boolean fail) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/downstream/employees/{id}")
                        .queryParam("delayMs", delayMs)
                        .queryParam("fail", fail)
                        .build(id))
                .retrieve()
                .bodyToMono(EmployeeDto.class)
                .toFuture();
    }

    /**
     * Fallback is called when:
     * - circuit breaker is OPEN (blocked)
     * - retries are exhausted
     * - time limiter times out
     *
     * For demo purposes we return a degraded response.
     * In production you might return cache, or throw a 503, etc.
     */
    private CompletableFuture<EmployeeDto> fallback(String id, long delayMs, boolean fail, Throwable ex) {
        EmployeeDto degraded = new EmployeeDto(id, "UNKNOWN (fallback)", "UNKNOWN", 0.0);
        return CompletableFuture.completedFuture(degraded);
    }
}
