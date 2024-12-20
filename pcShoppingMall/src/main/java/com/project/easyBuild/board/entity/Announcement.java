package com.project.easyBuild.board.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
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
@Table(name = "ANNOUNCEMENT")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANNOUNCEMENT_SEQ_GEN")
    @SequenceGenerator(name = "ANNOUNCEMENT_SEQ_GEN", sequenceName = "ANNOUNCEMENT_SEQ", allocationSize = 1)
    @Column(name = "ANNOUNC_ID")
    private Long announcId;

    @Column(name = "BOARD_ID", nullable = false)
    private Long boardId;

    @Column(name = "USER_ID", nullable = false, length = 30)
    private String userId;

    @Column(name = "AUTH_ID", nullable = false)
    private Long authId;

    @Column(name = "TITLE", nullable = false, length = 300)
    private String title;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "CREATED_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
}
