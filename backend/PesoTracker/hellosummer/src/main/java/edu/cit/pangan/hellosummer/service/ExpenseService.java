package edu.cit.pangan.hellosummer.service;

import edu.cit.pangan.hellosummer.dto.ExpenseRequest;
import edu.cit.pangan.hellosummer.entities.Expense;
import edu.cit.pangan.hellosummer.entities.User;
import edu.cit.pangan.hellosummer.repository.ExpenseRepository;
import edu.cit.pangan.hellosummer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Expense> getAllForUser(Long userId) {
        return expenseRepository.findByUser_IdOrderByDateDesc(userId);
    }

    public Expense add(Long userId, ExpenseRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Expense expense = new Expense(req.amount(), req.category(), req.date(), req.note(), user);
        return expenseRepository.save(expense);
    }

    public Expense update(Long id, Long userId, ExpenseRequest req) {
        Expense expense = expenseRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new NoSuchElementException("Expense not found"));

        expense.setAmount(req.amount());
        expense.setCategory(req.category());
        expense.setDate(req.date());
        expense.setNote(req.note());
        return expenseRepository.save(expense);
    }

    public void delete(Long id, Long userId) {
        Expense expense = expenseRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new NoSuchElementException("Expense not found"));

        expenseRepository.delete(expense);
    }
}
