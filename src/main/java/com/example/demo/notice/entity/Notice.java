package com.example.demo.notice.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Notice extends BaseEntity {

    private String title;

    private String content;

    private int viewCount;

    private LocalDateTime updateDt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
