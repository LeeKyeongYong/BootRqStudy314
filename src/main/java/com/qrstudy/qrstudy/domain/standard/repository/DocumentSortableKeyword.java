package com.qrstudy.qrstudy.domain.standard.repository;

import java.util.Set;

public interface DocumentSortableKeyword {

    long getTotal();

    void _setTotal(long total);

    Set<? extends DocumentSortableTag> _getTags();

    // 특정 postTag 의 순서를 바꾸고, 그것과 관련된 후 처리도 한다.
    default void _applySortNo(DocumentSortableTag tag, long newSortNo) {
        // 기존 순서를 저장
        long oldSortNo = tag.getSortNo();

        // 새 순서와 기존 순서가 같으면 패스
        if (oldSortNo == newSortNo) return;

        if (newSortNo < oldSortNo)
            // 새 순서가 기존 순서보다 작으면 기존 순서보다 크거나 같은 순서들의 순서번호를 1씩 증가
            _getTags().stream().filter(t -> t.getSortNo() >= newSortNo && t.getSortNo() < oldSortNo).forEach(t -> t._setSortNo(t.getSortNo() + 1));
        else
            // 새 순서가 기존 순서보다 크면 기존 순서보다 작거나 같은 순서들의 순서번호를 1씩 감소
            _getTags().stream().filter(t -> t.getSortNo() <= newSortNo && t.getSortNo() > oldSortNo).forEach(t -> t._setSortNo(t.getSortNo() - 1));

        // 새 순서를 적용
        tag._setSortNo(newSortNo);
    }

    default void _addTag(DocumentSortableTag tag) {
        tag._setKeyword(this);
        boolean added = __addTag(tag);

        if (added) {
            tag._setSortNo(getTotal() + 1);
            _setTotal(getTotal() + 1);
        }
    }

    // 여기서 __ 를 붙인 이유 : 위에 addTag 메서드가 있어서 이름이 겹친다.
    boolean __addTag(DocumentSortableTag tag);

    default void _removeTag(DocumentSortableTag tag) {
        _getTags().stream().filter(t -> t.getSortNo() > tag.getSortNo()).forEach(t -> t._setSortNo(t.getSortNo() - 1));

        _getTags().remove(tag);
        _setTotal(getTotal() - 1);
    }
}