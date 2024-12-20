package com.project.easyBuild.board.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.easyBuild.util.BooleanToNumberConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "QNA")
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QNA_SEQ_GEN")
    @SequenceGenerator(name = "QNA_SEQ_GEN", sequenceName = "QNA_SEQ", allocationSize = 1)
    @Column(name = "QNA_ID")
    private Long qnaId;

    @Column(name = "BOARD_ID", nullable = false)
    private Long boardId;

    @Column(name = "USER_ID", nullable = false, length = 30)
    private String userId;

    @Column(name = "TITLE", nullable = false, length = 300)
    private String title;

    @Column(name = "AUTH_ID", nullable = false)
    private Long authId;
    
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Lob
    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "CREATED_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "IS_SECRET", nullable = false)
    @Convert(converter = BooleanToNumberConverter.class)
    private Boolean isSecret;


}
