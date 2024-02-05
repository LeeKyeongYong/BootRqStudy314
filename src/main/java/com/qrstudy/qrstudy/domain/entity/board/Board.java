package com.qrstudy.qrstudy.domain.entity.board;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Board extends BaseEntity {
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String code;
    private String icon;

    // 오직 관리자만이 쓸 수 있는 게시판인지 여부
    public boolean isAdminOnlyWritable() {
        return code.startsWith("notice");
    }
}