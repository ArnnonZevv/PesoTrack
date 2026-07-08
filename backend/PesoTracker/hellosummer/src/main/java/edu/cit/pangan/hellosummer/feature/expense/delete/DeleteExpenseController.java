package edu.cit.pangan.hellosummer.feature.expense.delete;

import edu.cit.pangan.hellosummer.shared.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/expenses")
public class DeleteExpenseController {

    private final DeleteExpenseService deleteExpenseService;

    public DeleteExpenseController(DeleteExpenseService deleteExpenseService) {
        this.deleteExpenseService = deleteExpenseService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long id) {
        try {
            deleteExpenseService.delete(id, currentUser.userId());
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}