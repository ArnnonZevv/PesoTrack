package edu.cit.pangan.hellosummer.feature.expense.edit;

import edu.cit.pangan.hellosummer.shared.entity.Expense;

import java.time.LocalDate;

public record EditExpenseResponse(
    Long      id,
    Double    amount,
    String    category,
    LocalDate date,
    String    note
) {
    public static EditExpenseResponse from(Expense e) {
        return new EditExpenseResponse(
            e.getId(), e.getAmount(), e.getCategory(), e.getDate(), e.getNote()
        );
    }
}