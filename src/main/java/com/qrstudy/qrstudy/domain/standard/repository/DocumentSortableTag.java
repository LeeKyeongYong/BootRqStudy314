package com.qrstudy.qrstudy.domain.standard.repository;

public interface DocumentSortableTag extends DocumentTag {
    long getSortNo();

    void _setSortNo(long sortNo);

    DocumentSortableKeyword _getKeyword();

    void _setKeyword(DocumentSortableKeyword keyword);

    // 순서번호도 중복이 되면 안된다.
    // 그래서 순서변경이 있을 때, 대규모 순서변경이 발생한다.
    // 그것들이 잘 처리되게 하는 역할은 postKeyword 엔티티가 전담한다.
    default void _applySortNo(long newSortNo) {
        if (getSortNo() == newSortNo) return;

        // postKeyword 에게 현재 postTag 의 순서 번호가 newSortNo 와 같이 변했다고 알린다.
        // 이제 postKeyword 가 자신에게 딸려있는 모든 postTag 들의 순서번호를 재정렬하게 된다.
        _getKeyword()._applySortNo(_getDocumentSortableTag(), newSortNo);
    }

    DocumentSortableTag _getDocumentSortableTag();
}