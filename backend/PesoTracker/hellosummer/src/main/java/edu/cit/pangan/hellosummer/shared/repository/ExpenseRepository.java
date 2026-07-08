package edu.cit.pangan.hellosummer.shared.repository;

import edu.cit.pangan.hellosummer.shared.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense>     findByUser_IdOrderByDateDesc(Long userId);
    Optional<Expense> findByIdAndUser_Id(Long id, Long userId);
}