package com.project.easyBuild.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.easyBuild.board.repository.FaqRepository;
import com.project.easyBuild.board.entity.Faq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    public Page<Faq> findAll(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdDate")
            );
        }
        return faqRepository.findAll(pageable);
    }

    // FAQ 상세 조회
    public Faq getFaqById(Long id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FAQ not found with ID: " + id));
    }

    // 최신순 정렬
    public List<Faq> getAllFaqsSortedByDate() {
        return faqRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    // faq 저장
    public void saveFaq(Faq faq) {
        if (faq.getTitle() == null || faq.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (faq.getContent() == null || faq.getContent().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        if (faq.getCreatedDate() == null) {
            faq.setCreatedDate(LocalDateTime.now());
        }
        faqRepository.save(faq);
    }

    // faq 수정
    public void updateFaq(Faq faq) {
        Faq existingFaq = faqRepository.findById(faq.getFaqId())
                .orElseThrow(() -> new NoSuchElementException("FAQ not found with ID: " + faq.getFaqId()));

        if (faq.getTitle() == null || faq.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (faq.getContent() == null || faq.getContent().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        existingFaq.setTitle(faq.getTitle());
        existingFaq.setContent(faq.getContent());
        faqRepository.save(existingFaq);
    }

    // faq 삭제
    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }
}
