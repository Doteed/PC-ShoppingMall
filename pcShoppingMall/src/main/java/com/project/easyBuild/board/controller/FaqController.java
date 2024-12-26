package com.project.easyBuild.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.board.entity.Faq;
import com.project.easyBuild.board.service.FaqService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

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
            Model model) {
        // 서비스에서 null 방지 처리
        Page<Faq> page = faqService.findAll(pageable);

        // 모델에 데이터를 추가
        model.addAttribute("page", page);
        model.addAttribute("faqs", page.getContent());
        model.addAttribute("emptyPage", page.isEmpty());

        return TEMPLATE_DIR + "list";
    }
    

    // FAQ 상세보기
    @GetMapping("/{id}")
    public String getFaqDetail(@PathVariable Long id, Model model) {
        model.addAttribute("faq", faqService.getFaqById(id));
        return TEMPLATE_DIR + "detail";
    }

    // FAQ 작성 페이지
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("faq", new Faq());
        return TEMPLATE_DIR + "create";
    }

    // FAQ 작성 처리
    @PostMapping
    public String createFaq(@ModelAttribute @Valid Faq faq, BindingResult result) {
        if (result.hasErrors()) {
            return TEMPLATE_DIR + "create";
        }
        faq.setAuthId(1L); // 권한 번호 1로 설정
        faq.setBoardId(102L); // BOARD_ID 102으로 설정
        faqService.saveFaq(faq);
        return "redirect:/faqs";
    }

    // FAQ 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("faq", faqService.getFaqById(id));
        return TEMPLATE_DIR + "edit";
    }

    // FAQ 수정 처리
    @PostMapping("/update/{id}")
    public String updateFaq(
            @PathVariable Long id,
            @ModelAttribute @Valid Faq faq,
            BindingResult result) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "edit";
        }

        // 기본값 설정
        if (faq.getBoardId() == null) {
            faq.setBoardId(102L); // 기본값 설정
        }

        faq.setFaqId(id);
        faqService.updateFaq(faq);

        return "redirect:/faqs";
    }

    // FAQ 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteFaq(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            faqService.deleteFaq(id);
            redirectAttributes.addFlashAttribute("message", "FAQ deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete FAQ.");
        }
        return "redirect:/faqs";
    }
}
