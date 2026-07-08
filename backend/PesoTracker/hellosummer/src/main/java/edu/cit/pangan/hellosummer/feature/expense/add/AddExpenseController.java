package edu.cit.pangan.hellosummer.feature.expense.add;

import edu.cit.pangan.hellosummer.shared.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class AddExpenseController {

    private final AddExpenseService addExpenseService;

    public AddExpenseController(AddExpenseService addExpenseService) {
        this.addExpenseService = addExpenseService;
    }

    @PostMapping
    public ResponseEntity<?> add(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @RequestBody AddExpenseRequest request) {

        if (request.amount() == null || request.amount() <= 0) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Amount must be a positive number."));
        }
        if (request.category() == null || request.category().isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Category is required."));
        }
        if (request.date() == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Date is required."));
        }

        AddExpenseResponse response = addExpenseService.add(currentUser.userId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}