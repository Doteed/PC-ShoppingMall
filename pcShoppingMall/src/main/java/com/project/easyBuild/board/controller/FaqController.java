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

import com.project.easyBuild.board.entity.Faq;
import com.project.easyBuild.board.service.FaqService;
import com.project.easyBuild.member.dto.MemberDto;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/faqs")
@RequiredArgsConstructor
public class FaqController {
    private static final String TEMPLATE_DIR = "faq/";
    private final FaqService faqService;

    // FAQ 리스트 (페이징 및 최신 날짜순 정렬)
    @GetMapping
    public String listFaqs(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            HttpSession session,
            Model model) {

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        model.addAttribute("loggedInUser", loggedInUser);

        Page<Faq> page = faqService.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("faqs", page.getContent());
        model.addAttribute("emptyPage", page.isEmpty());

        return TEMPLATE_DIR + "list";
    }

    // FAQ 상세보기
    @GetMapping("/{id}")
    public String getFaqDetail(@PathVariable Long id, HttpSession session, Model model) {
        Faq faq = faqService.getFaqById(id);
        model.addAttribute("faq", faq);

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        model.addAttribute("loggedInUser", loggedInUser);

        return TEMPLATE_DIR + "detail";
    }

    // FAQ 작성 페이지
    @GetMapping("/new")
    public String showCreateForm(HttpSession session, Model model) {
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403"; // 관리자 권한이 없는 경우 접근 제한
        }

        model.addAttribute("faq", new Faq());
        return TEMPLATE_DIR + "create";
    }

    @PostMapping
    public String createFaq(@ModelAttribute @Valid Faq faq, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return TEMPLATE_DIR + "create";
        }

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        faq.setUserId(loggedInUser.getUserId());
        faq.setAuthId(2L); // 관리자 권한
        faq.setBoardId(102L); // FAQ 게시판 ID
        faqService.saveFaq(faq);

        return "redirect:/faqs";
    }

    // FAQ 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, HttpSession session, Model model) {
        Faq faq = faqService.getFaqById(id);

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403"; // 관리자 권한이 없는 경우 접근 제한
        }

        model.addAttribute("faq", faq);
        model.addAttribute("loggedInUser", loggedInUser);

        return TEMPLATE_DIR + "edit";
    }

    // FAQ 수정 처리
    @PostMapping("/update/{id}")
    public String updateFaq(
            @PathVariable Long id,
            @ModelAttribute @Valid Faq faq,
            BindingResult result,
            HttpSession session) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "edit";
        }

        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        faq.setFaqId(id);
        faq.setUserId(loggedInUser.getUserId());
        faqService.updateFaq(faq);

        return "redirect:/faqs";
    }

    // FAQ 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteFaq(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403";
        }

        try {
            faqService.deleteFaq(id);
            redirectAttributes.addFlashAttribute("message", "FAQ deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete FAQ.");
        }

        return "redirect:/faqs";
    }
}
