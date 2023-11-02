package com.viesonet.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import com.viesonet.entity.Last7DaySumAccounts;

public interface sp_Last7DaySumAccounts extends JpaRepository<Last7DaySumAccounts, Integer> {
    @Procedure("sp_Last7DaySumAccounts")
    Last7DaySumAccounts executeLast7DaySumAcc();
}