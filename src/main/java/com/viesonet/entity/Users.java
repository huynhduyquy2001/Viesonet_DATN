package com.viesonet.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {
	@Id
	private String userId;
	private String username;
	private String address;
	private String relationship;
	private String introduction;
	private boolean gender;
	private String avatar;
	private String background;
	private int violationCount;
	private Date createDate;
	private int ticket;
	@Temporal(TemporalType.DATE)
	private Date birthday;
	@Temporal(TemporalType.TIMESTAMP)
	private Date accessTime;

	@OneToOne(mappedBy = "user")
	private Accounts account;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Posts> posts;

	@JsonIgnore
	@OneToMany(mappedBy = "responder")
	private List<Reply> replyResponder;

	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private List<Reply> replyReceiver;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Favorites> favorites;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Comments> comments;

	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private List<Notifications> notifications;

	@JsonIgnore
	@OneToMany(mappedBy = "sender")
	private List<Notifications> senderId;

	@JsonIgnore
	@OneToMany(mappedBy = "follower")
	private List<Follow> followers;

	@JsonIgnore
	@OneToMany(mappedBy = "following")
	private List<Follow> followings;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Violations> violations;

	@JsonIgnore
	@OneToMany(mappedBy = "sender")
	private List<Message> sentMessages;

	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private List<Message> receivedMessages;

	@JsonIgnore
	@OneToMany(mappedBy = "interactingPerson")
	private List<Interaction> interactingPerson;

	@JsonIgnore
	@OneToMany(mappedBy = "interactedPerson")
	private List<Interaction> interactedPerson;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<ShoppingCart> shoppingCart;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<ViolationProducts> violationProducts;

	@JsonIgnore
	@OneToMany(mappedBy = "customer")
	private List<Orders> orders;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Ratings> ratings;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Products> products;

}