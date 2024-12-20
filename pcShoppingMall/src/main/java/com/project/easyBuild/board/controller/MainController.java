package com.project.easyBuild.board.controller;

import com.project.easyBuild.board.entity.Announcement;
import com.project.easyBuild.board.entity.Qna;
import com.project.easyBuild.board.service.AnnouncementService;
import com.project.easyBuild.board.service.QnaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final AnnouncementService announcementService;
    private final QnaService qnaService;

    public MainController(AnnouncementService announcementService, QnaService qnaService) {
        this.announcementService = announcementService;
        this.qnaService = qnaService;
    }

    @GetMapping("/main")
    public String showHomePage(Model model) {
        // 최신 공지사항 5개
        List<Announcement> latestAnnouncements = announcementService.getLatestAnnouncements(5);
        // 최신 QnA 5개
        List<Qna> latestQnas = qnaService.getLatestQnas(5);

        // 모델에 데이터를 추가
        model.addAttribute("latestAnnouncements", latestAnnouncements);
        model.addAttribute("latestQnas", latestQnas);

        return "main";
    }
}
