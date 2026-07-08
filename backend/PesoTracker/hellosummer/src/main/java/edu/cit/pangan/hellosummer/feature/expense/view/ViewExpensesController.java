package edu.cit.pangan.hellosummer.feature.expense.view;

import edu.cit.pangan.hellosummer.shared.security.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ViewExpensesController {

    private final ViewExpensesService viewExpensesService;

    public ViewExpensesController(ViewExpensesService viewExpensesService) {
        this.viewExpensesService = viewExpensesService;
    }

    @GetMapping
    public List<ExpenseItem> getAll(
            @AuthenticationPrincipal AuthenticatedUser currentUser) {
        return viewExpensesService.getForUser(currentUser.userId());
    }
}