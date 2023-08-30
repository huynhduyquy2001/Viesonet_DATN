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
@Table(name = "Colors")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Colors {
    @Id
    private int colorId;
    private String colorName;

    @JsonIgnore
    @OneToMany(mappedBy = "color")
    private List<ProductColors> productColors;
}
