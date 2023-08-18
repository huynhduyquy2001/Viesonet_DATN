package com.viesonet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TotalPosts {
	@Id
	private int stt;
	private String Avatar;
	private String Username;
	private int TotalPosts;
}
