package com.la.service;

import com.la.model.User;
import com.la.model.dtos.UserDTO;

import java.util.List;

public interface UserService {
    User findByUsername(String username);

    List<UserDTO> getAll();

    boolean activate(Long userId);

    boolean deactivate(Long userId);
}
