# resilience-demo

A small Spring Boot demo app that showcases **Resilience4j** patterns (Retry, Circuit Breaker, Rate Limiter, Bulkhead, Time Limiter) around outbound calls (typically via **WebClient**), with simple endpoints you can hit to see failure-handling in action.

---

## What this demo covers

- **Retry**: automatic re-attempts for transient failures (timeouts, 5xx, IO errors)
- **Circuit Breaker**: stops calling a failing downstream temporarily (Open → Half-Open → Closed)
- **Rate Limiter**: throttles requests to protect a dependency
- **Bulkhead**: limits concurrent calls to prevent thread/connection exhaustion
- **Time Limiter** *(optional in reactive)*: enforces max duration per call
- **Fallbacks**: controlled graceful degradation (default response, cached response, etc.)
- **Observability**: health + metrics via Spring Boot Actuator

---

## Tech stack

- Java 17+ (works with Java 11+ in many setups, but Java 17 recommended)
- Spring Boot (Web / WebFlux depending on repo)
- Resilience4j (Spring Boot starter)
- Spring Boot Actuator (health/metrics)

---

## Project layout (typical)

