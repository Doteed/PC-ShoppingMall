package com.project.easyBuild.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.easyBuild.board.entity.Announcement;
import com.project.easyBuild.board.entity.Faq;
import com.project.easyBuild.board.entity.Qna;
import com.project.easyBuild.board.service.AnnouncementService;
import com.project.easyBuild.board.service.FaqService;
import com.project.easyBuild.board.service.QnaService;

@Controller
public class MainController {

    private final AnnouncementService announcementService;
    private final FaqService faqService;
    private final QnaService qnaService;

    public MainController(AnnouncementService announcementService, FaqService faqService, QnaService qnaService) {
        this.announcementService = announcementService;
        this.faqService = faqService;
        this.qnaService = qnaService;
    }


    @GetMapping("/main")
    public String showHomePage(Model model) {
        // 최신 공지사항 5개
        List<Announcement> latestAnnouncements = announcementService.getLatestAnnouncements(5);
        // 최신 faq 5개
        List<Faq> latestFaqs = faqService.getLatestFaqs(5);

        // 모델에 데이터를 추가
        model.addAttribute("latestAnnouncements", latestAnnouncements);
        model.addAttribute("latestFaqs", latestFaqs);

        return "main";
    }
    
    @GetMapping("/auth-board")
    public String showAuthBoard(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;

        // 공지사항
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Announcement> announcements = announcementService.getAllAnnouncements(pageable);
        model.addAttribute("notices", announcements);

        // FAQ
        Pageable faqPageable = PageRequest.of(page, pageSize);
        Page<Faq> faqs = faqService.getAllFaqs(faqPageable);
        model.addAttribute("faqs", faqs);

        // QnA
        Pageable qnaPageable = PageRequest.of(page, pageSize);
        Page<Qna> qnas = qnaService.getAllQnas(qnaPageable);
        model.addAttribute("qnas", qnas);

        return "pages/authority/auth-board"; 
    }
}
