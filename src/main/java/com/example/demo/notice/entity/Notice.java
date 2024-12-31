package com.example.demo.notice.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice extends BaseEntity {

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;
}
