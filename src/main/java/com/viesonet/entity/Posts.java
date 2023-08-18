package com.viesonet.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "Posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Posts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date postDate ;

	private String content;
	
	private Boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private Users user;

	private Integer likeCount;

	private Integer commentCount;
	
	@JsonIgnore
	@OneToMany(mappedBy = "post")
	List<Comments> comments;
	@JsonIgnore
	@OneToMany(mappedBy = "post")
	List<Favorites> favorites;
	@JsonIgnore
	@OneToMany(mappedBy = "post")
	List<Notifications> notifications;
	
	@JsonIgnore
	@OneToMany(mappedBy = "post")
	private List<Violations> violations;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
	private List<Images> images;
}
