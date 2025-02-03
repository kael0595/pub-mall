package com.example.demo.cashLog.repository;

import com.example.demo.cashLog.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
