package com.qrstudy.qrstudy.domain.standard.repository;

import com.qrstudy.qrstudy.domain.standard.util.Ut;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

// HavingTags 가 접미사로 붙은 이유
// Document 중에서 태그기능을 가진 Document 들이 있다.
// 태그기능을 가진 Document 들은 DocumentHavingTags 인터페이스를 구현한다.
public interface DocumentHavingTags extends Document {
    // 태그를 입력받을 때 # 혹은 , 로 구분한다.
    String TAGS_STR_DIVISOR_REGEX = "#|,";

    // 이 메서드는 default 로 못 빼는 이유 : 인터페이스에서는 this 에 접근할 수 없기 때문이다.
    // <DocumentTag> 대신 <? extends DocumentTag> 를 쓰는 이유 : 제너릭은 공변이 아니다.
    Set<? extends DocumentTag> _getTags();

    // 태그들을 # 를 구분자로 하여 하나의 문자열로 만든 후 리턴
    default String getTagsStr() {
        if (_getTags().isEmpty()) return "";

        return "#" + _getTags()
                .stream()
                .map(DocumentTag::getContent)
                .collect(Collectors.joining(" #"));
    }

    DocumentTag _addTag(String tagContent);

    default void addTags(String tagsStr) {
        Arrays.stream(tagsStr.split(TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet())
                .forEach(this::_addTag);
    }

    default void modifyTags(String newTagsStr) {
        Set<String> newTags = Arrays.stream(newTagsStr.split(TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());

        // getTags() 에서 newTagsStr 에 없는 것들은 삭제
        _getTags().removeIf(tag -> !newTags.contains(tag.getContent()));

        addTags(newTagsStr);
    }

    default String getTagLinks(String __linkTemplate, String urlTemplate) {
        if (_getTags().isEmpty()) return "-";

        final String _linkTemplate = __linkTemplate.replace("`", "\"");

        return _getTags()
                .stream()
                .map(tag -> _linkTemplate
                        .formatted(urlTemplate.formatted(Ut.url.encode(tag.getContent())), tag.getContent()))
                .sorted()
                .collect(Collectors.joining(" "));
    }
}