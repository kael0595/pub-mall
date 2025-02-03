package com.example.demo.cashLog.service;

import com.example.demo.cashLog.entity.CashLog;
import com.example.demo.cashLog.entity.CashLogStatus;
import com.example.demo.cashLog.repository.CashLogRepository;
import com.example.demo.member.entity.Member;
import com.example.demo.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashLogService {

    private final CashLogRepository cashLogRepository;

    @Transactional
    public void addCashLog(Member member, Product product, Integer amount) {

        if (member == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다.");
        }

        if (amount <= 0 ) {
            throw new IllegalArgumentException("상품 수량은 0보다 커야합니다.");
        }

        if (product.getSalePrice() <= 0) {
            throw new IllegalArgumentException("판매 가격은 0보다 커야합니다.");
        }
        CashLog cashLog = CashLog.builder()
                .member(member)
                .product(product)
                .price(product.getSalePrice() * amount)
                .status(CashLogStatus.PAID)
                .build();
        cashLogRepository.save(cashLog);
    }
}
