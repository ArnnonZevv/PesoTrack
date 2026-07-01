package edu.cit.pangan.hellosummer.dto;

import java.time.LocalDate;

public record ExpenseRequest(Double amount, String category, LocalDate date, String note) {
}