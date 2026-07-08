package edu.cit.pangan.hellosummer.feature.expense.edit;

import edu.cit.pangan.hellosummer.shared.entity.Expense;
import edu.cit.pangan.hellosummer.shared.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EditExpenseService {

    private final ExpenseRepository expenseRepository;

    public EditExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public EditExpenseResponse edit(Long expenseId, Long userId, EditExpenseRequest request) {
        // findByIdAndUser_Id enforces ownership — users can only edit their own expenses
        Expense expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
            .orElseThrow(() -> new NoSuchElementException("Expense not found."));

        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setDate(request.date());
        expense.setNote(request.note());

        return EditExpenseResponse.from(expenseRepository.save(expense));
    }
}