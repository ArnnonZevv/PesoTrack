package edu.cit.pangan.hellosummer.feature.expense.delete;

import edu.cit.pangan.hellosummer.shared.entity.Expense;
import edu.cit.pangan.hellosummer.shared.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeleteExpenseService {

    private final ExpenseRepository expenseRepository;

    public DeleteExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void delete(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
            .orElseThrow(() -> new NoSuchElementException("Expense not found."));

        expenseRepository.delete(expense);
    }
}