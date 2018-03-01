package com.maxbin.fileoperation.repository;

import com.maxbin.fileoperation.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    List<UserEntity> findByUsername(String name);

    List<UserEntity> findByUsernameAndPassword(String name,String password);
}
