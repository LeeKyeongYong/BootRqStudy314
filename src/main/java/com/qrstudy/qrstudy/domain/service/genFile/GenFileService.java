package com.qrstudy.qrstudy.domain.service.genFile;

import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import com.qrstudy.qrstudy.domain.repository.genFile.GenFileRepository;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenFileService {

    private final GenFileRepository genFileRepository;

    @Transactional
    public GenFile save(String relTypeCode, Long relId, String typeCode, String type2Code, int fileNo, MultipartFile multipartFile){

        String originFileName = multipartFile.getOriginalFilename();
        String fileExt = Ut.file.getExt(originFileName);
        String fileExtTypeCode = Ut.file.getfileExtTypeCodeFromFileExt(fileExt);
        String fileExtType2Code = Ut.file.getFileExtType2CodeFromFileExt(fileExt);
        int fileSize = (int)multipartFile.getSize();
        String fileDir = getCurrentDirName(relTypeCode);

        GenFile genFile = GenFile.builder()
                .relTypeCode(relTypeCode)
                .relId(relId)
                .typeCode(typeCode)
                .type2Code(type2Code)
                .fileExtTypeCode(fileExtTypeCode)
                .fileExtType2Code(fileExtType2Code)
                .originFileName(originFileName)
                .fileSize(fileSize)
                .fileNo(fileNo)
                .fileExt(fileExt)
                .fileDir(fileDir)
                .build();

        genFileRepository.save(genFile);
        return genFile;
    }

    private String getCurrentDirName(String relTypeCode){
        return relTypeCode+"/"+ Ut.date.getCurrentDateFormatted("yyyy_MM_dd");
    }
}
