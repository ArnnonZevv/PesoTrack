package edu.cit.pangan.hellosummer.dto;

import edu.cit.pangan.hellosummer.entities.Expense;

import java.time.LocalDate;

public record ExpenseResponse(Long id, Double amount, String category, LocalDate date, String note) {

    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate(),
                expense.getNote()
        );
    }
}