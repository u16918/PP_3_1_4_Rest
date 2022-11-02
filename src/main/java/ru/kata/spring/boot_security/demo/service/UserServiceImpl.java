package ru.kata.spring.boot_security.demo.service;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kata.spring.boot_security.demo.Exception.ExceptionInfo;
import ru.kata.spring.boot_security.demo.Exception.UserUsernameExistException;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User passwordCoder(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public ResponseEntity<ExceptionInfo> create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new ExceptionInfo(error), HttpStatus.BAD_REQUEST);
        }
        try {
            save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUsernameExistException u) {
            throw new UserUsernameExistException("User with username exist");
        }
    }

    @Override
    public User getById(long id) {
        User user = null;
        Optional<User> optional = userDAO.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }


    @Override
    @Transactional
    public void save(User user) {
        userDAO.save(passwordCoder(user));
    }

    @Override
    @Transactional
    public void update(User user) {
        userDAO.save(user);
    }


    @Override
    @Transactional
    public void deleteById(long id) {
        userDAO.deleteById(id);
    }

    @Override
    public ResponseEntity<ExceptionInfo> edit(@PathVariable("id") long id,
                                              @Valid @RequestBody User user,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new ExceptionInfo(error), HttpStatus.BAD_REQUEST);
        }
        try {
            String oldPassword = getById(id).getPassword();
            if (oldPassword.equals(user.getPassword())) {
                user.setPassword(oldPassword);
                update(user);
            } else {
                save(user);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUsernameExistException u) {
            throw new UserUsernameExistException("User with username exist");
        }
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }


    @Override
    @PostConstruct
    public void addDefaultUser() {
        Set<Role> roles1 = new HashSet<>();
        roles1.add(roleDAO.findById(1L).orElse(null));
        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleDAO.findById(1L).orElse(null));
        roles2.add(roleDAO.findById(2L).orElse(null));
        User user1 = new User("User", "LastUser", (byte) 25, "user@mail.com", "user", "12345", roles1);
        User user2 = new User("Admin", "LastAdmin", (byte) 30, "admin@mail.com", "admin", "admin", roles2);
        save(user1);
        save(user2);
    }

    @Override
    public String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserServiceImpl)) return false;
        UserServiceImpl that = (UserServiceImpl) o;
        return userDAO.equals(that.userDAO) && roleDAO.equals(that.roleDAO) && passwordEncoder.equals(that.passwordEncoder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDAO, roleDAO, passwordEncoder);
    }
}

