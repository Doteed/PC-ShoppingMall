package com.project.easyBuild.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.easyBuild.board.repository.QnaRepository;
import com.project.easyBuild.board.entity.Qna;

@Service
public class QnaService {

    private final QnaRepository qnaRepository;
    private final PasswordEncoder passwordEncoder;

    public QnaService(QnaRepository qnaRepository, PasswordEncoder passwordEncoder) {
        this.qnaRepository = qnaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // qna 저장
    public void saveQna(Qna qna) {
        validateQna(qna); // 제목, 내용 확인

        // 비밀번호 암호화
        if (qna.getIsSecret() && qna.getPassword() != null && !qna.getPassword().trim().isEmpty()) {
            qna.setPassword(passwordEncoder.encode(qna.getPassword()));
        }

        if (qna.getCreatedDate() == null) {
            qna.setCreatedDate(LocalDateTime.now());
        }

        qnaRepository.save(qna);
    }

    // qna 업데이트
    public void updateQna(Long id, Qna qna) {
        Qna existingQna = qnaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("QnA not found with ID: " + id));

        validateQna(qna); // 제목, 내용 확인
        existingQna.setTitle(qna.getTitle());
        existingQna.setContent(qna.getContent());

        qnaRepository.save(existingQna);
    }

    // qna 삭제
    public void deleteQna(Long id) {
        if (!qnaRepository.existsById(id)) {
            throw new NoSuchElementException("Cannot delete non-existing QnA with ID: " + id);
        }
        qnaRepository.deleteById(id);
    }

    // 답변 저장/업데이트
    public void saveAnswer(Long id, String answer) {
        Qna existingQna = qnaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("QnA not found with ID: " + id));

        existingQna.setAnswer(answer); // 답변 저장
        qnaRepository.save(existingQna);
    }

    // 비밀번호 확인
    public boolean verifyPassword(Long qnaId, String rawPassword) {
        Qna qna = getQnaById(qnaId);

        // 공개글이거나 비밀번호 없는 경우 인증 성공
        if (!qna.getIsSecret() || qna.getPassword() == null || qna.getPassword().trim().isEmpty()) {
            return true;
        }

        return passwordEncoder.matches(rawPassword, qna.getPassword());
    }

    // qna 조회
    public Qna getQnaById(Long id) {
        return qnaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("QnA not found with ID: " + id));
    }

    // qna 전체 리스트 조회
    public Page<Qna> findAll(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    getDefaultSort()
            );
        }
        return qnaRepository.findAll(pageable);
    }

    // qna 전체 리스트 정렬 (최신순, 비페이징)
    public List<Qna> getAllQnaSortedByDate() {
        return qnaRepository.findAll(getDefaultSort());
    }

    // 최신 목록 가져오기
    public List<Qna> getLatestQnas(int limit) {
        return qnaRepository.findAll(
                PageRequest.of(0, limit, getDefaultSort())
        ).getContent();
    }

    // 공통 정렬 방식 메서드
    private Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, "createdDate");
    }

    // qna 제목, 내용 검증 메서드
    private void validateQna(Qna qna) {
        if (qna.getTitle() == null || qna.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (qna.getContent() == null || qna.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
    }
    
    public Page<Qna> getAllQnas(Pageable pageable) {
        return qnaRepository.findAll(pageable);
    }

}
