package edu.cit.pangan.hellosummer.feature.expense.view;

import edu.cit.pangan.hellosummer.shared.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewExpensesService {

    private final ExpenseRepository expenseRepository;

    public ViewExpensesService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseItem> getForUser(Long userId) {
        return expenseRepository.findByUser_IdOrderByDateDesc(userId)
            .stream()
            .map(ExpenseItem::from)
            .toList();
    }
}