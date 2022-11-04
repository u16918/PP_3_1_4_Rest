package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Pattern(regexp = "[A-Za-zа-яёА-ЯЁ]{2,15}", message = "Name should be between 2 and 15 characters without space")
    private String name;

    @Pattern(regexp = "[A-Za-zа-яёА-ЯЁ]{2,15}", message = "Surname should be between 2 and 15 characters without space")
    private String surname;

    @Min(value = 0, message = "Age should be >= 0 & < 128")
    @Max(value = 127, message = "Age should be >= 0 & < 128")
    private byte age;

    @Pattern(regexp = "([A-z0-9_.-]+)@([A-z0-9_.-]+).([A-z]{2,8})", message = "Enter correct email")
    private String email;

    @NotEmpty(message = "Username cannot be empty")
    @Pattern(regexp = "[A-Za-z]{2,15}", message = "Name should be between 2 and 15 latin characters without space")
    @Size(min = 2, max = 15, message = "Username should be between 2 and 15 latin characters")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, message = "Password should be greater then 4 symbols")
    private String password;

    @NotEmpty(message = "The role cannot be omitted")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_Id"),
            inverseJoinColumns = @JoinColumn(name = "roles_Id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String name, String surname, byte age, String email, String username, String password, Set<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) && getPassword().equals(user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}