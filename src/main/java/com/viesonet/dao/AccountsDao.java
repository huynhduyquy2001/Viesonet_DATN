package com.viesonet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;

import java.util.List;

public interface AccountsDao extends JpaRepository<Accounts, String> {
	Accounts findByPhoneNumber(String phoneNumber);
	Accounts findByUserId(String userId);
	boolean existsByEmail(String email);
	Accounts findByEmail(String email);
}
