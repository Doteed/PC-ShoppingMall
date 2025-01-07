package com.project.easyBuild.board.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.board.entity.Announcement;
import com.project.easyBuild.board.service.AnnouncementService;
import com.project.easyBuild.member.dto.MemberDto;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
            HttpSession session,
            Model model) {

        // 로그인된 사용자 정보 가져오기
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        model.addAttribute("loggedInUser", loggedInUser);

        // 공지사항 데이터 추가
        Page<Announcement> page = announcementService.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("announcements", page.getContent());
        model.addAttribute("emptyPage", page.isEmpty());

        return TEMPLATE_DIR + "list";
    }

    // 공지사항 상세보기
    @GetMapping("/{id}")
    public String getAnnouncementDetail(@PathVariable Long id, HttpSession session, Model model) {
        // 공지사항 데이터 추가
        model.addAttribute("announcement", announcementService.getAnnouncementById(id));

        // 세션 사용자 정보 추가
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        model.addAttribute("loggedInUser", loggedInUser);

        return TEMPLATE_DIR + "detail";
    }

    // 공지사항 작성 페이지
    @GetMapping("/new")
    public String showCreateForm(HttpSession session, Model model) {
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403"; // 접근 권한 없음 페이지로 이동
        }
        model.addAttribute("announcement", new Announcement());
        return TEMPLATE_DIR + "create";
    }

    // 공지사항 작성 처리
    @PostMapping
    public String createAnnouncement(
            @ModelAttribute @Valid Announcement announcement,
            BindingResult result,
            HttpSession session) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "create";
        }

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        announcement.setUserId(loggedInUser.getUserId());
        // 기본값 설정
        announcement.setAuthId(2L); // 관리자 권한 ID
        announcement.setBoardId(101L); // 공지사항 게시판 ID
        announcementService.saveAnnouncement(announcement);
        return "redirect:/announcements";
    }

    // 공지사항 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, HttpSession session, Model model) {
        // 공지사항 가져오기
        Announcement announcement = announcementService.getAnnouncementById(id);

        // 세션에서 사용자 정보 가져오기
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        // 모델에 데이터 추가
        model.addAttribute("announcement", announcement);
        model.addAttribute("loggedInUser", loggedInUser);

        return "announcement/edit"; // 수정 페이지 템플릿
    }


    // 공지사항 수정 처리
    @PostMapping("/update/{id}")
    public String updateAnnouncement(
            @PathVariable Long id,
            @ModelAttribute @Valid Announcement announcement,
            BindingResult result,
            HttpSession session) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "edit";
        }

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        // 명시적으로 ID 설정
        announcement.setAnnouncId(id);
        announcement.setUserId(loggedInUser.getUserId());

        // 서비스 호출
        announcementService.updateAnnouncement(announcement);

        return "redirect:/announcements";
    }


    // 공지사항 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        try {
            announcementService.deleteAnnouncement(id);
            redirectAttributes.addFlashAttribute("message", "Announcement deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete announcement.");
        }

        return "redirect:/announcements";
    }
}
