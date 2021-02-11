package com.la.service.impl;

import com.la.model.User;
import com.la.model.dtos.UserDTO;
import com.la.repository.UserRepository;
import com.la.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository<User> userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserDTO> getAll() {
       List<User> users = userRepository.findAll();
       List<UserDTO> userDTOs = new ArrayList<>();
       for(User user : users) {
           UserDTO userDTO = new UserDTO();
           userDTO.setId(user.getId());
           userDTO.setUsername(user.getUsername());
           userDTO.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
           userDTO.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : "");
           userDTO.setLastLogin(user.getLastLogin() != null ? user.getLastLogin().toString() : "");
           userDTO.setType(user.getType());
           userDTO.setActive(user.getActive() ? "ACTIVE" : "SUSPENDED");
           userDTOs.add(userDTO);
       }
       return userDTOs;
    }

    @Override
    public boolean activate(Long userId) {
        if (userRepository.findById(userId).isPresent()){
            User user = (User) userRepository.findById(userId).get();
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivate(Long userId) {
        if (userRepository.findById(userId).isPresent()){
            User user = (User) userRepository.findById(userId).get();
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username '%s' was not found", username));
        } else {
            return user;
        }
    }
}
