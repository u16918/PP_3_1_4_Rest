package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleDAO roleDAO;

    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public List<Role> findAllRole() {
        return roleDAO.findAll();
    }

    @Override
    @PostConstruct
    public void addDefaultRole() {
        roleDAO.save(new Role("ROLE_USER"));
        roleDAO.save(new Role("ROLE_ADMIN"));
    }

    @Override
    public Set<Role> findByIdRoles(List<Long> roles) {
      return new HashSet<>(roleDAO.findAllById(roles));
    }
}
