package com.qrstudy.qrstudy.domain.standard.repository;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// HavingSortableTags 가 접미사로 붙은 이유
// Document 중에서 순서가 있는 태그기능을 가진 Document 들이 있다.
// 순서가 있는 태그기능을 가진 Document 들은 HavingSortableTags 인터페이스를 구현한다.
public interface DocumentHavingSortableTags extends DocumentHavingTags {
    // 태그를 입력받을 때 #태그명[순서/전체] 형식으로 입력받는다.
    String TAGS_STR_SORT_REGEX = "\\[-?\\d*\\/?-?\\d*\\]";

    Set<? extends DocumentSortableTag> _getTags();

    // 태그들을 # 를 구분자로 하여 하나의 문자열로 만든 후 리턴
    // 각 태그의 순서와 개수를 [순서/전체] 형식으로 뒤에 붙여서 표시한다.
    default String getTagsWithSortNoStr() {
        if (_getTags().isEmpty()) return "";

        return "#" + _getTags()
                .stream()
                .map(tag -> tag.getContent() + "[" + tag.getSortNo() + "/" + tag._getKeyword().getTotal() + "]")
                .sorted()
                .collect(Collectors.joining(" #"));
    }

    default void modifyTags(String newTagsStr, Map<String, ? extends DocumentSortableKeyword> keywordsMap) {
        String inputedNewTagsStr = newTagsStr;
        newTagsStr = newTagsStr.replaceAll(DocumentHavingSortableTags.TAGS_STR_SORT_REGEX, "");

        Set<String> newTags = Arrays.stream(newTagsStr.split(DocumentHavingSortableTags.TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tagContent -> !tagContent.isEmpty())
                .collect(Collectors.toSet());

        // getTags() 에서 newTagsStr 에 없는 것들은 삭제
        _getTags().removeIf(tag -> {
            boolean remove = !newTags.contains(tag.getContent());

            // 순서를 관장하는 키워드에게도 삭제되었다는 소식을 알린다.
            if (remove) tag._getKeyword()._removeTag(tag);

            return remove;
        });

        addTags(inputedNewTagsStr, keywordsMap);
    }

    default void _addTag(String tagContent, Map<String, ? extends DocumentSortableKeyword> keywordsMap) {
        DocumentSortableTag tag = (DocumentSortableTag) _addTag(tagContent);

        DocumentSortableKeyword keyword = keywordsMap.get(tagContent);
        keyword._addTag(tag);
    }

    default void addTags(String tagsStr, Map<String, ? extends DocumentSortableKeyword> keywordsMap) {
        String inputedTagsStr = tagsStr;
        tagsStr = tagsStr.replaceAll(DocumentHavingSortableTags.TAGS_STR_SORT_REGEX, "");

        // 일단 등록
        Arrays.stream(tagsStr.split(DocumentHavingSortableTags.TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tagContent -> !tagContent.isEmpty())
                .distinct()
                .forEach(tagContent -> _addTag(tagContent, keywordsMap));

        // 입력받은 순서정보를 토대로 순서 재정렬
        Arrays.stream(inputedTagsStr.split(DocumentHavingSortableTags.TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tagContent -> !tagContent.isEmpty())
                .distinct()
                .forEach(tagContent -> {
                    String[] tagContentBits = tagContent.split("\\[", 2);

                    if (tagContentBits.length == 1) return;

                    tagContent = tagContentBits[0];

                    tagContentBits = tagContentBits[1].split("/", 2);

                    long newSortNo;

                    try {
                        newSortNo = Long.parseLong(tagContentBits[0].replace("]", "").trim());
                    } catch (Exception ignored) {
                        return;
                    }

                    if (newSortNo < 1) newSortNo = 1;
                    if (newSortNo > keywordsMap.get(tagContent).getTotal())
                        newSortNo = keywordsMap.get(tagContent).getTotal();

                    final long _newSortNo = newSortNo;
                    final String _tagContent = tagContent;

                    _getTags()
                            .stream()
                            .filter(tag -> tag.getContent().equals(_tagContent))
                            .findFirst()
                            .ifPresent(tag -> tag._applySortNo(_newSortNo));
                });
    }
}