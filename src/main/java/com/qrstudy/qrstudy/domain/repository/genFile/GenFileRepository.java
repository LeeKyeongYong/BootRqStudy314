package com.qrstudy.qrstudy.domain.repository.genFile;

import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenFileRepository extends JpaRepository<GenFile,Long> {
    List<GenFile> findByRelTypeCodeAndRelIdOrderByTypeCodeAscType2CodeAscFileNoAsc(String relTypeCode,Long relId);
    Optional<GenFile> findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeAndFileNo(String relTypeCode,long relId,String typeCode,String type2Code,int fileNo);
    List<GenFile> findAllByRelTypeCodeAndRelIdInOrderByTypeCodeAscType2CodeAscFileNoAsc(String relTypeCode,long[] relIds);
}
