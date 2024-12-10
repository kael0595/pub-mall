package com.example.demo.base.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @CreatedDate
    private LocalDateTime createDt;

    @LastModifiedDate
    private LocalDateTime updateDt;

    @Setter
    @Getter
    @Transient
    private boolean skipAuditing = false;

    @PrePersist
    @PreUpdate
    public void preUpdate() {

        if (skipAuditing) {
            updateDt = null;
        }
    }

}
