package com.viesonet.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Interaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int InteracId;
	@ManyToOne
	@JoinColumn(name = "interactingPerson")
	private Users interactingPerson;
	
	@ManyToOne
	@JoinColumn(name = "interactedPerson")
	private Users interactedPerson;
	
	private int interactionCount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date mostRecentInteractionDate;
}
