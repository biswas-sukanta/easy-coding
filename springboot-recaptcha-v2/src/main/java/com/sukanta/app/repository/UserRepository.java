package com.sukanta.app.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sukanta.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{}
