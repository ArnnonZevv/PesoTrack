package edu.cit.pangan.hellosummer.feature.expense.edit;

import java.time.LocalDate;

public record EditExpenseRequest(
    Double    amount,
    String    category,
    LocalDate date,
    String    note
) {}