package com.qrstudy.qrstudy.domain.repository.genFile;

import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GenFileRepository extends JpaRepository<GenFile,Long> {
    List<GenFile> findByRelTypeCodeAndRelIdOrderByTypeCodeAscType2CodeAscFileNoAsc(String relTypeCode, Long relId);

    Optional<GenFile> findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeAndFileNo(String relTypeCode, long relId, String typeCode, String type2Code, long fileNo);

    List<GenFile> findByRelTypeCodeAndRelIdInOrderByTypeCodeAscType2CodeAscFileNoAsc(String relTypeCode, long[] relIds);

    List<GenFile> findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeOrderByFileNoAsc(String relTypeCode, long relId, String typeCode, String type2Code);

    List<GenFile> findByRelTypeCodeAndRelId(String relTypeCode, long relId);

    Optional<GenFile> findTop1ByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeOrderByFileNoDesc(String relTypeCode, Long relId, String typeCode, String type2Code);

    List<GenFile> findByRelTypeCodeAndTypeCodeAndType2Code(String relTypeCode, String typeCode, String type2Code);

    List<GenFile> findByRelTypeCode(String relTypeCode);

    List<GenFile> findByRelTypeCodeAndCreateDateBefore(String relTypeCode, LocalDateTime dateTime);
}
