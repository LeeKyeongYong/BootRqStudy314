package com.qrstudy.qrstudy.domain.service.document;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
import com.qrstudy.qrstudy.domain.service.genFile.GenFileService;
import com.qrstudy.qrstudy.domain.standard.repository.Document;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {
    private final GenFileService genFileService;
    private static final String TEMP_FILE_PATH = "/gen/temp_member";

    // 사용처 : 게시글 작성, 게시글 수정, 글 작성, 글 수정

    // 배경지식 시작
    // 텍스트에디터로 이미지를 업로드하면 해당 이미지는 본문이 저장되기 전까지 임시파일 상태이다.
    // 임시파일은 저장 될 때 relTypeCode 가 temp_member 이다.
    // 임시파일을 특정 member 와 연관지어서 저장한 이유는 혹시나 있을지 모르는 대량 업로드 공격발생시 추적을 위해서 이다.
    // relTypeCode 가 temp_ 로 시작하는 이유 : DB 인덱스 특성상 이렇게 해야 임시 파일들을 빠르게 걸러낼 수 있다.
    // 배경지식 끝

    // 하는 일 : 실제로 본문에 포함된 임시파일들을 inBody 파일로 승격시킨다.
    @Transactional
    public void updateTempGenFilesToInBody(Document doc) {
        // body 와 bodyHtml 에서 똑같은 일을 해야 한다.
        // body 에서 발견된 임시 파일들은 바로바로 inBody 파일로 승격시켜야 하지만
        // bodyHtml 에서 발견된 임시 파일들은 body 에서 작업하면서 생긴 inBody 파일들을 적용만 해주면 된다.
        // 그것을 위한 일종의 캐시 저장소이다.
        final Map<String, String> urlsMap = new HashMap<>();

        String newBody = Ut.str.replace(doc.getBody(), "\\(" + TEMP_FILE_PATH + "/([^)]+)\\?type=temp\\)", (String url) -> {
            url = TEMP_FILE_PATH + "/" + url;
            String newUrl = genFileService.tempToFile(url, (BaseEntity) doc, "common", "inBody", 0).getUrl();
            urlsMap.put(url, newUrl);
            return "(" + newUrl + ")";
        });

        doc.setBody(newBody);

        String newBodyHtml = Ut.str.replace(doc.getBodyHtml(), "=\"" + TEMP_FILE_PATH + "/([^\" ]+)\\?type=temp\"", (String url) -> {
            url = TEMP_FILE_PATH + "/" + url;
            String newUrl = urlsMap.get(url);
            return "=\"" + newUrl + "\"";
        });

        doc.setBodyHtml(newBodyHtml);
    }
}