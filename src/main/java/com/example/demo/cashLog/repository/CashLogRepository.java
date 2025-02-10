package com.example.demo.cashLog.repository;

import com.example.demo.cashLog.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
    @Query("select coalesce(sum(c.price), 0) from CashLog c where c.createDt between :start and :end")
    BigDecimal getTotalRevenue(@Param("start") LocalDateTime startOfDay, @Param("end") LocalDateTime endOfDay);
}
