package com.project.easyBuild.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.board.entity.Qna;
import com.project.easyBuild.board.service.QnaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/qnas")
@RequiredArgsConstructor
public class QnaController {

    private static final String TEMPLATE_DIR = "qna/";
    private final QnaService qnaService;

    // QnA 리스트 (페이징 및 최신 날짜순 정렬)
    @GetMapping
    public String listQnas(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<Qna> page = qnaService.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("qnas", page.getContent());
        model.addAttribute("emptyPage", page.isEmpty());

        return TEMPLATE_DIR + "list";
    }
    

    // QnA 상세보기
    @GetMapping("/{id}")
    public String getQnaDetail(@PathVariable Long id, Model model) {
        Qna qna = qnaService.getQnaById(id);

        model.addAttribute("qna", qna);
        model.addAttribute("isAuthenticated", false); // 초기값은 인증되지 않음
        return TEMPLATE_DIR + "detail";
    }

    // 비밀번호 검증 처리
    @PostMapping("/verify/{id}")
    public String verifyPassword(
            @PathVariable Long id, 
            @RequestParam("password") String password, 
            Model model) {
        boolean isAuthenticated = qnaService.verifyPassword(id, password);
        Qna qna = qnaService.getQnaById(id);

        model.addAttribute("qna", qna);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (!isAuthenticated) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
        }
        return TEMPLATE_DIR + "detail";
    }

    // qna 작성 페이지
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("qna", new Qna());
        return TEMPLATE_DIR + "create";
    }
    
    // qna 작성 처리
    @PostMapping
    public String createQna(
            @ModelAttribute @Valid Qna qna,
            BindingResult result) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "create"; // 유효성 검사 실패 시 다시 작성 페이지로 이동
        }
        
        qna.setBoardId(103L); // BOARDID 103 고정
        qna.setAuthId(1L); // 사용자 권한 1 설정

        qnaService.saveQna(qna);

        // qna 목록 페이지로 리다이렉트
        return "redirect:/qnas";
    }

    // 답변 작성 페이지
    @GetMapping("/answer/{id}")
    public String showAnswerForm(@PathVariable Long id, Model model) {
        model.addAttribute("qna", qnaService.getQnaById(id));
        return TEMPLATE_DIR + "answer";
    }    

    // 답변 작성/수정 처리
    @PostMapping("/answer/{id}")
    public String saveAnswer(@PathVariable Long id, @RequestParam("answer") String answer) {
        qnaService.saveAnswer(id, answer);
        return "redirect:/qnas/" + id; // 해당 qna 상세 페이지로 리다이렉트
    }

    // qna 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("qna", qnaService.getQnaById(id));
        return TEMPLATE_DIR + "edit";
    }

    // qna 수정 처리
    @PostMapping("/update/{id}")
    public String updateQna(
            @PathVariable Long id,
            @ModelAttribute @Valid Qna qna,
            BindingResult result) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "edit";
        }

        qna.setBoardId(103L); // BOARD_ID 고정
        qna.setAuthId(1L); // 사용자 권한 1 설정

        qnaService.updateQna(id, qna);
        return "redirect:/qnas";
    }


    // qna 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteQna(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            qnaService.deleteQna(id);
            redirectAttributes.addFlashAttribute("message", "QnA deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete QnA.");
        }
        return "redirect:/qnas";
    }
}
