package com.viesonet.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import com.viesonet.entity.SumAccountsByDay;

public interface sp_SumAccountsByDay extends JpaRepository<SumAccountsByDay, Integer> {

    @Procedure("sp_SumAccountsByDay")
    SumAccountsByDay executeSumAccByDays();
}