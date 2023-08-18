package com.viesonet.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Message")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int messId;
	
	@ManyToOne
	@JoinColumn(name = "senderId")
	private Users sender;
	
	@ManyToOne
	@JoinColumn(name = "receiverId")
	private Users receiver;
	
	private String content;
	
	private String image;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendDate;
	
	private String status;
}
