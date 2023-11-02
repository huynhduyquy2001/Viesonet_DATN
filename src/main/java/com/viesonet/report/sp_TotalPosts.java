package com.viesonet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import com.viesonet.entity.TotalPosts;

public interface sp_TotalPosts extends JpaRepository<TotalPosts, Integer> {

    @Procedure("sp_TotalPosts")
    List<TotalPosts> executeTotalPosts();
}