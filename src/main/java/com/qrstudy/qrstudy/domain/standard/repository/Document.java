package com.qrstudy.qrstudy.domain.standard.repository;

// 배경지식 시작
// 이 인터페이스가 standard 패키지에 있는 이유 : 이 프로젝트에서 standard 는 일종의 규약이라는 뜻 입니다.
// Document 는 텍스트에디터를 통해서 만들어지는 문서를 의미합니다.
// 이 인터페이스는 텍스트에디터를 통해 작성되는 글들이 지켜야하는 규약을 담았습니다.
// 이 인터페이스가 적용 대상 : Post, Article
// 이를 통해서 TextEditorService 에서 Article 과 Post 를 구분해서 처리할 필요가 없어졌습니다.
// 배경지식 끝
public interface Document {
    void setBody(String body);

    String getBody();

    void setBodyHtml(String bodyHtml);

    String getBodyHtml();

    // XSS 공격을 막기 위해서 script 는 t-script 로 치환해서 리턴한다.
    // 이 함수의 결과는 실제로 HTML으로 렌더링 된다. 그렇게 쓰이고 있다.
    default String getBodyForEditor() {
        return getBody()
                .replaceAll("(?i)(</?)script", "$1t-script");
    }

    // HTML 결과물에서 toastui-editor-ww-code-block-highlighting 클래스를 제거한다.
    // 그래야 뷰어에서 코드블럭에 쓸데없는 버튼이 안보인다.
    default String getBodyHtmlForPrint() {
        return getBodyHtml()
                .replace("toastui-editor-ww-code-block-highlighting", "");
    }
}