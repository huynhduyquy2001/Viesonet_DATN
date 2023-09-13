package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viesonet.dao.AccountsDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.AccountStatus;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Roles;

@Service
public class AccountsService {
	@Autowired
	AccountsDao accountsDao;

	@Autowired
	UsersDao usersDao;
	//
	// @Autowired
	// BCryptPasswordEncoder pe;

	public Accounts getAccountByUsers(String userId) {
		return accountsDao.findByUserId(userId);
	}

	public Accounts findByPhoneNumber(String phoneNumber) {
		return accountsDao.findByPhoneNumber(phoneNumber);
	}

	public boolean existById(String phoneNumber) {
		return accountsDao.existsById(phoneNumber);
	}

	public boolean existByEmail(String email) {
		return accountsDao.existsByEmail(email);
	}

	public Accounts save(Accounts accounts) {
		return accountsDao.save(accounts);
	}

	public Accounts setRole(String sdt, int role, String roleName) {
		Roles roles = new Roles();
		roles.setRoleId(role);
		roles.setRoleName(roleName);
		Accounts accounts = accountsDao.findByPhoneNumber(sdt);
		accounts.setRole(roles);
		return accountsDao.saveAndFlush(accounts);
	}

	public void updateAccInfo(String userId, String email, int statusId) {
		Accounts currentAcc = accountsDao.findByUserId(userId);
		AccountStatus status = new AccountStatus();
		currentAcc.setEmail(email);
		status.setStatusId(statusId);
		currentAcc.setAccountStatus(status);
		accountsDao.saveAndFlush(currentAcc);
	}

	public Accounts getAccountById(String userId) {
		return accountsDao.findById(userId).orElse(null);
	}

	public Accounts findByUserId(String userId) {
		return accountsDao.findByUserId(userId);
	}

	public Accounts findByEmail(String email) {
		return accountsDao.findByEmail(email);
	}
	// public List<String> getRolesByUsername(String username){
	//
	// List<String> roleNames = new ArrayList<>();
	//
	// List<Users> authorities = usersDao.findAll();
	//
	// for (Users authority : authorities) {
	// if(authority.getAccount().getUserId().equals(username)){
	// roleNames.add(authority.getAccount().getRole().getRoleId());
	// }
	// }
	// return roleNames;
	// }
	//
	// @Override
	// public UserDetails loadUserByUsername(String username) throws
	// UsernameNotFoundException {
	// try {
	// Accounts account = accountsDao.findById(username).get();
	// // Tạo UserDetails từ Account
	// String password = account.getPassword();
	// String[] roles = account.getAuthorities().stream()
	// .map(au -> au.getRole().getId())
	// .collect(Collectors.toList()).toArray(new String[0]);
	//
	//
	//
	// Map<String, Object> authentication = new HashMap<>();
	// authentication.put("user", account);
	// byte[] token = (username + ":" + account.getPassword()).getBytes();
	// authentication.put("token", "Basic " +
	// Base64.getEncoder().encodeToString(token));
	// //session.setAttribute("authentication", authentication);
	//
	//
	// return User.withUsername(username)
	// .password(pe.encode(password))
	// .roles(roles).build();
	// } catch (Exception e) {
	// throw new UsernameNotFoundException(username + " not found!");
	// }
	// }
}
