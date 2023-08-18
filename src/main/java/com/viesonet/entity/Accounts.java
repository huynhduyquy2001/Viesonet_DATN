package com.viesonet.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Accounts")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accounts{
    @Id
    private String phoneNumber;
    private String password;
    private String email;
  
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "userId")
    private Users user;
    
    @Column(name = "userId", insertable=false, updatable=false) 
	private String userId;
    
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Roles role;
    
    @ManyToOne
    @JoinColumn(name = "StatusId")
    private AccountStatus accountStatus;

}
