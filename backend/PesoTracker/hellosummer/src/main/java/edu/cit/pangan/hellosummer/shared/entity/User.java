package edu.cit.pangan.hellosummer.shared.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    private String username;
    private String password;
    private String role;

    public User() {}

    public User(String fullname, String email, String username, String password, String role) {
        this.fullname = fullname;
        this.email    = email;
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    public Long   getId()       { return id; }
    public void   setId(Long id){ this.id = id; }

    public String getFullname()              { return fullname; }
    public void   setFullname(String v)      { this.fullname = v; }

    public String getEmail()                 { return email; }
    public void   setEmail(String v)         { this.email = v; }

    public String getUsername()              { return username; }
    public void   setUsername(String v)      { this.username = v; }

    public String getPassword()              { return password; }
    public void   setPassword(String v)      { this.password = v; }

    public String getRole()                  { return role; }
    public void   setRole(String v)          { this.role = v; }
}