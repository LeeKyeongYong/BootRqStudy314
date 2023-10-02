package com.qrstudy.qrstudy.domain.controller.genFile;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class GenFile extends BaseEntity {
    private String relTypeCode;
    private long relId;
    private String typeCode;
    private String type2Code;
    private String fileExtTypeCode;
    private String fileExtType2Code;
    private int fileSize;
    private int fileNo;
    private String fileExt;
    private String fileDir;
    private String originFileName;
}
