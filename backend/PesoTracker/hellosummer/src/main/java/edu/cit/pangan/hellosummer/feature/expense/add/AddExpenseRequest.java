package edu.cit.pangan.hellosummer.feature.expense.add;

import java.time.LocalDate;

public record AddExpenseRequest(
    Double    amount,
    String    category,
    LocalDate date,
    String    note
) {}