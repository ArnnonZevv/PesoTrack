package edu.cit.pangan.hellosummer.feature.expense.view;

import edu.cit.pangan.hellosummer.shared.entity.Expense;

import java.time.LocalDate;

public record ExpenseItem(
    Long      id,
    Double    amount,
    String    category,
    LocalDate date,
    String    note
) {
    public static ExpenseItem from(Expense e) {
        return new ExpenseItem(
            e.getId(), e.getAmount(), e.getCategory(), e.getDate(), e.getNote()
        );
    }
}