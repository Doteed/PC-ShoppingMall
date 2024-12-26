package com.project.easyBuild.board.controller;

import com.project.easyBuild.board.entity.Announcement;
import com.project.easyBuild.board.entity.Faq;
import com.project.easyBuild.board.service.AnnouncementService;
import com.project.easyBuild.board.service.FaqService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final AnnouncementService announcementService;
    private final FaqService faqService;

    public MainController(AnnouncementService announcementService, FaqService qnaService) {
        this.announcementService = announcementService;
        this.faqService = qnaService;
    }

    @GetMapping("/main")
    public String showHomePage(Model model) {
        // 최신 공지사항 5개
        List<Announcement> latestAnnouncements = announcementService.getLatestAnnouncements(5);
        // 최신 QnA 5개
        List<Faq> latestFaqs = faqService.getLatestFaqs(5);

        // 모델에 데이터를 추가
        model.addAttribute("latestAnnouncements", latestAnnouncements);
        model.addAttribute("latestFaqs", latestFaqs);

        return "main";
    }
}
