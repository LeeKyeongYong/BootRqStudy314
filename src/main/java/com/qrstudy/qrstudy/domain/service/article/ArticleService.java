package com.qrstudy.qrstudy.domain.service.article;

import com.qrstudy.qrstudy.base.app.AppConfig;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import com.qrstudy.qrstudy.domain.entity.article.Article;
import com.qrstudy.qrstudy.domain.entity.board.Board;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.repository.article.ArticleRepository;
import com.qrstudy.qrstudy.domain.service.board.BoardService;
import com.qrstudy.qrstudy.domain.service.document.DocumentService;
import com.qrstudy.qrstudy.domain.service.genFile.GenFileService;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final GenFileService genFileService;
    private final DocumentService documentService;
    private final BoardService boardService;

    @Transactional
    public RsData<Article> write(Board board, Member author, String subject, String tagsStr, String body) {
        return write(board, author, subject, tagsStr, body, Ut.markdown.toHtml(body));
    }

    @Transactional
    public RsData<Article> write(Board board, Member author, String subject, String tagsStr, String body, String bodyHtml) {
        Article article = Article.builder()
                .board(board)
                .author(author)
                .subject(subject)
                .body(body)
                .bodyHtml(bodyHtml)
                .build();

        articleRepository.save(article);

        article.addTags(tagsStr);

        documentService.updateTempGenFilesToInBody(article);

        return new RsData<>("S-1", article.getId() + "번 게시물이 생성되었습니다.", article);
    }

    public Page<Article> findByKw(Board board, String kwType, String kw, Pageable pageable) {
        return articleRepository.findByKw(board, kwType, kw, pageable);
    }

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    public RsData<?> checkActorCanWrite(Member actor, Board board) {
        if (actor == null) {
            return new RsData<>("F-1", "로그인 후 이용해주세요.", null);
        }

        if (board.isAdminOnlyWritable() && !actor.isAdmin()) {
            return new RsData<>("F-2", "관리자만 작성이 가능합니다.", null);
        }

        return new RsData<>("S-1", "가능합니다.", null);
    }

    public RsData<?> checkActorCanModify(Member actor, Article article) {
        if (actor == null || !actor.equals(article.getAuthor())) {
            return new RsData<>("F-1", "권한이 없습니다.", null);
        }

        return new RsData<>("S-1", "가능합니다.", null);
    }

    public RsData<?> checkActorCanRemove(Member actor, Article article) {
        return checkActorCanModify(actor, article);
    }

    @Transactional
    public RsData<Article> modify(Article article, String subject, String tagsStr, String body, String bodyHtml) {

        article.modifyTags(tagsStr);

        article.setSubject(subject);
        article.setBody(body);
        article.setBodyHtml(bodyHtml);

        documentService.updateTempGenFilesToInBody(article);

        return new RsData<>("S-1", article.getId() + "번 게시물이 수정되었습니다.", article);
    }

    @Transactional
    public RsData<?> remove(Article article) {
        findGenFiles(article).forEach(genFileService::remove);

        articleRepository.delete(article);

        return new RsData<>("S-1", article.getId() + "번 게시물이 삭제되었습니다.", null);
    }

    private List<GenFile> findGenFiles(Article article) {
        return genFileService.findByRelId(article.getModelName(), article.getId());
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Article article, MultipartFile attachmentFile, long fileNo) {
        String attachmentFilePath = Ut.file.toFile(attachmentFile, AppConfig.getTempDirPath());
        return saveAttachmentFile(article, attachmentFilePath, fileNo);
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Article article, String attachmentFile, long fileNo) {
        GenFile genFile = genFileService.save(article.getModelName(), article.getId(), "common", "attachment", fileNo, attachmentFile);

        return new RsData<>("S-1", genFile.getId() + "번 파일이 생성되었습니다.", genFile);
    }

    public Map<String, GenFile> findGenFilesMapKeyByFileNo(Article article, String typeCode, String type2Code) {
        return genFileService.findGenFilesMapKeyByFileNo(article.getModelName(), article.getId(), typeCode, type2Code);
    }

    @Transactional
    public void removeAttachmentFile(Article article, long fileNo) {
        genFileService.remove(article.getModelName(), article.getId(), "common", "attachment", fileNo);
    }

    public Page<Article> findByTag(String tagContent, Pageable pageable) {
        return articleRepository.findByArticleTags_content(tagContent, pageable);
    }
}