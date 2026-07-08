package edu.cit.pangan.hellosummer.shared.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double    amount;
    private String    category;
    private LocalDate date;
    private String    note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Expense() {}

    public Expense(Double amount, String category, LocalDate date, String note, User user) {
        this.amount   = amount;
        this.category = category;
        this.date     = date;
        this.note     = note;
        this.user     = user;
    }

    public Long      getId()          { return id; }
    public void      setId(Long id)   { this.id = id; }

    public Double    getAmount()      { return amount; }
    public void      setAmount(Double v)   { this.amount = v; }

    public String    getCategory()    { return category; }
    public void      setCategory(String v){ this.category = v; }

    public LocalDate getDate()        { return date; }
    public void      setDate(LocalDate v) { this.date = v; }

    public String    getNote()        { return note; }
    public void      setNote(String v){ this.note = v; }

    public User      getUser()        { return user; }
    public void      setUser(User v)  { this.user = v; }
}