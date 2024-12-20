package com.project.easyBuild.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.easyBuild.board.entity.Faq;
import com.project.easyBuild.board.entity.Qna;

import java.util.List;
import org.springframework.data.domain.Sort;

public interface QnaRepository extends JpaRepository<Qna, Long> {
}
