package com.example.demo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface IUserDAO extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
