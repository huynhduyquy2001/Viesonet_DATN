package com.viesonet.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserInformation {
	private Users user;
	private String email;
	private String introdution;
	private Date birthday;
	private boolean gender;
	private String relationship;
	
}
