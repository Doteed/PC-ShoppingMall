package com.project.easyBuild.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.board.entity.Announcement;
import com.project.easyBuild.board.service.AnnouncementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private static final String TEMPLATE_DIR = "announcement/";
    private final AnnouncementService announcementService;

    // 공지사항 리스트 (페이징 및 최신 날짜순 정렬)
    @GetMapping
    public String listAnnouncements(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<Announcement> page = announcementService.findAll(pageable);

        // 모델에 데이터를 추가
        model.addAttribute("page", page);
        model.addAttribute("announcements", page.getContent());
        model.addAttribute("emptyPage", page.isEmpty());

        return TEMPLATE_DIR + "list";
    }

    // 공지사항 상세보기
    @GetMapping("/{id}")
    public String getAnnouncementDetail(@PathVariable Long id, Model model) {
        model.addAttribute("announcement", announcementService.getAnnouncementById(id));
        return TEMPLATE_DIR + "detail";
    }

    // 공지사항 작성 페이지
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("announcement", new Announcement());
        return TEMPLATE_DIR + "create";
    }

    // 공지사항 작성 처리
    @PostMapping
    public String createAnnouncement(@ModelAttribute @Valid Announcement announcement, BindingResult result) {
        if (result.hasErrors()) {
            return TEMPLATE_DIR + "create";
        }
        announcement.setAuthId(2L); //관리자 권한 2 설정
        announcement.setBoardId(101L); // BOARD_ID를 101으로 설정
        announcementService.saveAnnouncement(announcement);
        return "redirect:/announcements";
    }

    // 공지사항 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("announcement", announcementService.getAnnouncementById(id));
        return TEMPLATE_DIR + "edit";
    }

    // 공지사항 수정 처리
    @PostMapping("/update/{id}")
    public String updateAnnouncement(
            @PathVariable Long id,
            @ModelAttribute @Valid Announcement announcement,
            BindingResult result) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "edit";
        }

        // 기본값 설정
        if (announcement.getBoardId() == null) {
            announcement.setBoardId(101L);
        }

        announcement.setAnnouncId(id);
        announcementService.updateAnnouncement(announcement);

        return "redirect:/announcements";
    }

    // 공지사항 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            announcementService.deleteAnnouncement(id);
            redirectAttributes.addFlashAttribute("message", "Announcement deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete announcement.");
        }
        return "redirect:/announcements";
    }
}
