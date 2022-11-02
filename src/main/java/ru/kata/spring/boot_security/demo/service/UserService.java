package ru.kata.spring.boot_security.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kata.spring.boot_security.demo.Exception.ExceptionInfo;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    List<User> findAll ();
    User getById(long id);
    void save(User user);
    void deleteById(long id);

    ResponseEntity<ExceptionInfo> edit(long id, User user,
                                       BindingResult bindingResult);

    User findByUsername(String username);
    void addDefaultUser();
    void update(User user);
    User passwordCoder(User user);

    String getErrorsFromBindingResult(BindingResult bindingResult);
    ResponseEntity<ExceptionInfo> create(User user, BindingResult bindingResult);

}

