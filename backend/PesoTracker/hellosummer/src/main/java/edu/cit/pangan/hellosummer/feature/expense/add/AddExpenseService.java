package edu.cit.pangan.hellosummer.feature.expense.add;

import edu.cit.pangan.hellosummer.shared.entity.Expense;
import edu.cit.pangan.hellosummer.shared.entity.User;
import edu.cit.pangan.hellosummer.shared.repository.ExpenseRepository;
import edu.cit.pangan.hellosummer.shared.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AddExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository    userRepository;

    public AddExpenseService(ExpenseRepository expenseRepository,
                             UserRepository    userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository    = userRepository;
    }

    public AddExpenseResponse add(Long userId, AddExpenseRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found."));

        Expense expense = new Expense(
            request.amount(), request.category(),
            request.date(), request.note(), user
        );

        return AddExpenseResponse.from(expenseRepository.save(expense));
    }
}