package edu.cit.pangan.hellosummer.feature.expense.edit;

import edu.cit.pangan.hellosummer.shared.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/expenses")
public class EditExpenseController {

    private final EditExpenseService editExpenseService;

    public EditExpenseController(EditExpenseService editExpenseService) {
        this.editExpenseService = editExpenseService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long id,
            @RequestBody EditExpenseRequest request) {

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

        try {
            EditExpenseResponse response = editExpenseService.edit(id, currentUser.userId(), request);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}