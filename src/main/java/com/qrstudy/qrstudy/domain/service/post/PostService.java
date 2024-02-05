package com.qrstudy.qrstudy.domain.service.post;

import com.qrstudy.qrstudy.base.app.AppConfig;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.entity.post.Post;
import com.qrstudy.qrstudy.domain.entity.postkeyword.PostKeyword;
import com.qrstudy.qrstudy.domain.repository.post.PostRepository;
import com.qrstudy.qrstudy.domain.repository.postkeyword.PostKeywordRepository;
import com.qrstudy.qrstudy.domain.service.document.DocumentService;
import com.qrstudy.qrstudy.domain.service.genFile.GenFileService;
import com.qrstudy.qrstudy.domain.standard.repository.DocumentHavingSortableTags;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final GenFileService genFileService;
    private final DocumentService documentService;

    @Transactional
    public RsData<Post> write(Member author, String subject, String tagsStr, String body, boolean isPublic) {
        return write(author, subject, tagsStr, body, Ut.markdown.toHtml(body), isPublic);
    }

    @Transactional
    public RsData<Post> write(Member author, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {
        Post post = Post.builder()
                .author(author)
                .subject(subject)
                .body(body)
                .bodyHtml(bodyHtml)
                .isPublic(isPublic)
                .build();

        postRepository.save(post);

        Map<String, PostKeyword> postKeywordsMap = findPostKeywordsMap(author, tagsStr);
        post.addTags(tagsStr, postKeywordsMap);

        documentService.updateTempGenFilesToInBody(post);

        return new RsData<Post>("S-1", post.getId() + "번 글이 생성되었습니다.", post);
    }

    public Page<Post> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable) {
        return postRepository.findByKw(kwType, kw, isPublic, pageable);
    }

    public Page<Post> findByKw(Member author, String kwType, String kw, Pageable pageable) {
        return postRepository.findByKw(author, kwType, kw, pageable);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public RsData<?> checkActorCanModify(Member actor, Post post) {
        if (actor == null || !actor.equals(post.getAuthor())) {
            return new RsData<>("F-1", "권한이 없습니다.", null);
        }

        return new RsData<>("S-1", "가능합니다.", null);
    }

    public RsData<?> checkActorCanRemove(Member actor, Post post) {
        return checkActorCanModify(actor, post);
    }

    @Transactional
    public RsData<Post> modify(Post post, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {

        Map<String, PostKeyword> postKeywordsMap = findPostKeywordsMap(post.getAuthor(), tagsStr);
        post.modifyTags(tagsStr, postKeywordsMap);

        post.setSubject(subject);
        post.setBody(body);
        post.setBodyHtml(bodyHtml);
        post.setPublic(isPublic);

        documentService.updateTempGenFilesToInBody(post);

        return new RsData<>("S-1", post.getId() + "번 글이 수정되었습니다.", post);
    }

    private Map<String, PostKeyword> findPostKeywordsMap(Member author, String tagsStr) {
        tagsStr = tagsStr.replaceAll(DocumentHavingSortableTags.TAGS_STR_SORT_REGEX, "");

        return Arrays.stream(tagsStr.split(DocumentHavingSortableTags.TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .map(tag -> postKeywordRepository.findByAuthorAndContent(author, tag)
                        .orElseGet(() -> postKeywordRepository.save(PostKeyword.builder()
                                .author(author)
                                .content(tag)
                                .build())))
                .collect(Collectors.toMap(PostKeyword::getContent, postKeyword -> postKeyword));
    }

    @Transactional
    public RsData<?> remove(Post post) {
        findGenFiles(post).forEach(genFileService::remove);

        postRepository.delete(post);

        return new RsData<>("S-1", post.getId() + "번 게시물이 삭제되었습니다.", null);
    }

    private List<GenFile> findGenFiles(Post post) {
        return genFileService.findByRelId(post.getModelName(), post.getId());
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Post post, MultipartFile attachmentFile, long fileNo) {
        String attachmentFilePath = Ut.file.toFile(attachmentFile, AppConfig.getTempDirPath());
        return saveAttachmentFile(post, attachmentFilePath, fileNo);
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Post post, String attachmentFile, long fileNo) {
        GenFile genFile = genFileService.save(post.getModelName(), post.getId(), "common", "attachment", fileNo, attachmentFile);

        return new RsData<>("S-1", genFile.getId() + "번 파일이 생성되었습니다.", genFile);
    }

    public Map<String, GenFile> findGenFilesMapKeyByFileNo(Post post, String typeCode, String type2Code) {
        return genFileService.findGenFilesMapKeyByFileNo(post.getModelName(), post.getId(), typeCode, type2Code);
    }

    @Transactional
    public void removeAttachmentFile(Post post, long fileNo) {
        genFileService.remove(post.getModelName(), post.getId(), "common", "attachment", fileNo);
    }

    public Page<Post> findByTag(String tagContent, boolean isPublic, Pageable pageable) {
        return postRepository.findByPostTags_contentAndIsPublic(tagContent, isPublic, pageable);
    }

    public Page<Post> findByTag(Member author, String tagContent, Pageable pageable) {
        return postRepository.findByAuthorAndPostTags_content(author, tagContent, pageable);
    }

    public Page<Post> findByTag(Member author, String tagContent, boolean isPublic, Pageable pageable) {
        return postRepository.findByAuthorAndPostTags_contentAndIsPublic(author, tagContent, isPublic, pageable);
    }

    public Optional<PostKeyword> findKeywordById(long postKeywordId) {
        return postKeywordRepository.findById(postKeywordId);
    }

    public List<PostKeyword> findPostKeywordsByMemberId(Member actor) {
        return postKeywordRepository.findByAuthorOrderByContent(actor);
    }

    public Optional<PostKeyword> findPostKeywordById(long postKeywordId) {
        return postKeywordRepository.findById(postKeywordId);
    }
}