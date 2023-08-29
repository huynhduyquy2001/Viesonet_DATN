package com.viesonet.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ReturnTypes")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnTypes {
    @Id
    private int returnTypeId;
    private String returnTypeName;
    @JsonIgnore
    @OneToMany(mappedBy = "returnType")
    private List<Returns> returns;
}
