package edu.cit.pangan.hellosummer.controller;

import edu.cit.pangan.hellosummer.dto.ExpenseRequest;
import edu.cit.pangan.hellosummer.dto.ExpenseResponse;
import edu.cit.pangan.hellosummer.security.AuthenticatedUser;
import edu.cit.pangan.hellosummer.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<ExpenseResponse> getAll(@AuthenticationPrincipal AuthenticatedUser currentUser) {
        return expenseService.getAllForUser(currentUser.userId())
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> add(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @RequestBody ExpenseRequest req) {
        var expense = expenseService.add(currentUser.userId(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ExpenseResponse.from(expense));
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long id,
            @RequestBody ExpenseRequest req) {
        return ExpenseResponse.from(expenseService.update(id, currentUser.userId(), req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long id) {
        expenseService.delete(id, currentUser.userId());
        return ResponseEntity.noContent().build();
    }
}