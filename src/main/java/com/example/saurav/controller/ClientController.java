package com.example.saurav.controller;

import com.example.saurav.model.EmployeeDto;
import com.example.saurav.service.EmployeeClientService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * Client-facing API:
 * Calls downstream via EmployeeClientService (which has resilience features).
 */
@RestController
@RequestMapping("/api")
public class ClientController {

    private final EmployeeClientService employeeClientService;

    public ClientController(EmployeeClientService employeeClientService) {
        this.employeeClientService = employeeClientService;
    }

    /**
     * Example:
     * GET /api/employees/E101?delayMs=0&fail=false
     */
    @GetMapping("/employees/{id}")
    public CompletableFuture<EmployeeDto> getEmployee(@PathVariable String id,
                                                      @RequestParam(defaultValue = "0") long delayMs,
                                                      @RequestParam(defaultValue = "false") boolean fail) {
        return employeeClientService.fetchEmployee(id, delayMs, fail);
    }
}
