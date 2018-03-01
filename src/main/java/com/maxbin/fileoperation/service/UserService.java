package com.maxbin.fileoperation.service;

import com.maxbin.fileoperation.domain.UserEntity;
import com.maxbin.fileoperation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public boolean verifyUser(UserEntity userEntity) {
//
//        if (userRepository.findByUsernameAndPassword(userEntity.getUsername(), userEntity.getPassword()).isEmpty()) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }

    public boolean verifyLogin(UserEntity userEntity) {
        List<UserEntity> userList = userRepository.findByUsernameAndPassword(userEntity.getUsername(), userEntity.getPassword());
        return userList.size() > 0;
    }

    public String registerUser(UserEntity userEntity) {
        if (userEntity.getPassword().equals(userEntity.getPasswordConfirm())) {
            if (userRepository.findByUsername(userEntity.getUsername()).isEmpty()) {
                userRepository.save(userEntity);
                return "login";
            } else {
                return "register";
            }
        } else {
            return "register";
        }

    }
}
