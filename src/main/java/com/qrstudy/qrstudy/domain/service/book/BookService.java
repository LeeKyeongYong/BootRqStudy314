package com.qrstudy.qrstudy.domain.service.book;

import com.qrstudy.qrstudy.base.app.AppConfig;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import com.qrstudy.qrstudy.domain.entity.postkeyword.PostKeyword;
import com.qrstudy.qrstudy.domain.service.document.DocumentService;
import com.qrstudy.qrstudy.domain.service.genFile.GenFileService;
import com.qrstudy.qrstudy.domain.service.post.PostService;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final DocumentService documentService;
    private final GenFileService genFileService;
    private final PostService postService;

    public Page<Book> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable) {
        return bookRepository.findByKw(kwType, kw, isPublic, pageable);
    }

    public RsData<Book> write(Member author, long postKeywordId, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {
        return write(author, postService.findKeywordById(postKeywordId).get(), subject, tagsStr, body, bodyHtml, isPublic);
    }

    public RsData<Book> write(Member author, PostKeyword postKeyword, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {
        Book book = Book.builder()
                .postKeyword(postKeyword)
                .author(author)
                .subject(subject)
                .body(body)
                .bodyHtml(bodyHtml)
                .isPublic(isPublic)
                .build();

        bookRepository.save(book);

        book.addTags(tagsStr);

        documentService.updateTempGenFilesToInBody(book);

        return new RsData<>("S-1", book.getId() + "번 책이 생성되었습니다.", book);
    }

    @Transactional
    public RsData<Book> modify(Book book, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {

        book.modifyTags(tagsStr);
        book.setSubject(subject);
        book.setBody(body);
        book.setBodyHtml(bodyHtml);
        book.setPublic(isPublic);

        documentService.updateTempGenFilesToInBody(book);

        return new RsData<>("S-1", book.getId() + "번 책이 수정되었습니다.", book);
    }

    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    public Map<String, GenFile> findGenFilesMapKeyByFileNo(Book book, String typeCode, String type2Code) {
        return genFileService.findGenFilesMapKeyByFileNo(book.getModelName(), book.getId(), typeCode, type2Code);
    }

    public RsData<?> checkActorCanModify(Member actor, Book book) {
        if (actor == null || !actor.equals(book.getAuthor())) {
            return new RsData<>("F-1", "권한이 없습니다.", null);
        }

        return new RsData<>("S-1", "가능합니다.", null);
    }

    public RsData<?> checkActorCanRemove(Member actor, Book book) {
        return checkActorCanModify(actor, book);
    }

    public RsData<?> checkActorCanWrite(Member author) {
        return author.isProducer() ? new RsData<>("S-1", "가능합니다.", null) : new RsData<>("F-1", "권한이 없습니다.", null);
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Book book, MultipartFile attachmentFile, long fileNo) {
        String attachmentFilePath = Ut.file.toFile(attachmentFile, AppConfig.getTempDirPath());
        return saveAttachmentFile(book, attachmentFilePath, fileNo);
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Book book, String attachmentFile, long fileNo) {
        GenFile genFile = genFileService.save(book.getModelName(), book.getId(), "common", "attachment", fileNo, attachmentFile);

        return new RsData<>("S-1", genFile.getId() + "번 파일이 생성되었습니다.", genFile);
    }

    @Transactional
    public void removeAttachmentFile(Book book, long fileNo) {
        genFileService.remove(book.getModelName(), book.getId(), "common", "attachment", fileNo);
    }

    public Page<Book> findByTag(String tagContent, Pageable pageable) {
        return bookRepository.findByBookTags_content(tagContent, pageable);
    }

    @Transactional
    public RsData<?> remove(Book book) {
        findGenFiles(book).forEach(genFileService::remove);

        bookRepository.delete(book);

        return new RsData<>("S-1", book.getId() + "번 책이 삭제되었습니다.", null);
    }

    private List<GenFile> findGenFiles(Book book) {
        return genFileService.findByRelId(book.getModelName(), book.getId());
    }
}