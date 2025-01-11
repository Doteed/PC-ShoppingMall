package com.project.easyBuild.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.board.entity.Qna;
import com.project.easyBuild.board.service.QnaService;
import com.project.easyBuild.member.dto.MemberDto;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

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
    public String getQnaDetail(@PathVariable Long id, HttpSession session, Model model) {
        Qna qna = qnaService.getQnaById(id);
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");

        model.addAttribute("qna", qna);
        model.addAttribute("loggedInUser", loggedInUser); // 사용자 정보 추가
        model.addAttribute("isAuthenticated", false); // 초기값 설정

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

    // QnA 작성 페이지
    @GetMapping("/new")
    public String showCreateForm(Model model,
    		@RequestParam(value = "source", required = false) String source,
    		HttpSession session) {
        model.addAttribute("qna", new Qna());
        
        if (source != null) {
            session.setAttribute("source", source); //게시판 접근이 아니면 세션에 저장
        }
        
        return TEMPLATE_DIR + "create";
    }

    // QnA 작성 처리
    @PostMapping
    public String createQna(
            @ModelAttribute @Valid Qna qna,
            BindingResult result,
            HttpSession session) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "create";
        }

        // 로그인한 사용자 정보 가져오기
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null) {
            return "redirect:/loginform"; // 로그인되지 않은 경우 로그인 페이지로 이동
        }

        // 로그인한 사용자 ID 설정
        qna.setUserId(loggedInUser.getUserId());
        qna.setBoardId(103L); // BOARD_ID 고정
        qna.setAuthId(1L); // 사용자 권한 1 설정

        qnaService.saveQna(qna);

        String source = (String)session.getAttribute("source");
        session.removeAttribute("source"); // 세션 값 삭제
        
        if ("my".equals(source)) {
        	return "redirect:/my/qna"; // 마이페이지에서 요청시 마이페이지로
        }
        
        return "redirect:/qnas";
    }

    // 답변 작성 페이지
    @GetMapping("/answer/{id}")
    public String showAnswerForm(@PathVariable Long id, HttpSession session, Model model) {
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403"; // 권한 없는 사용자 접근 차단
        }
        model.addAttribute("qna", qnaService.getQnaById(id));
        return TEMPLATE_DIR + "answer";
    }


    // 답변 작성/수정 처리
    @PostMapping("/answer/{id}")
    public String saveAnswer(@PathVariable Long id, @RequestParam("answer") String answer, HttpSession session) {
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null || loggedInUser.getAuthId() != 2) {
            return "error/403"; // 권한 없는 사용자 접근 차단
        }
        qnaService.saveAnswer(id, answer);
        return "redirect:/qnas/" + id;
    }


    // QnA 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("qna", qnaService.getQnaById(id));
        return TEMPLATE_DIR + "edit";
    }

    // QnA 수정 처리
    @PostMapping("/update/{id}")
    public String updateQna(
            @PathVariable Long id,
            @ModelAttribute @Valid Qna qna,
            BindingResult result,
            HttpSession session) {

        if (result.hasErrors()) {
            return TEMPLATE_DIR + "edit";
        }

        // 로그인한 사용자 정보 가져오기
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
        if (loggedInUser == null) {
            return "redirect:/loginform"; // 로그인되지 않은 경우 로그인 페이지로 이동
        }

        // 로그인한 사용자 ID 설정
        qna.setUserId(loggedInUser.getUserId());
        qna.setBoardId(103L); // BOARD_ID 고정
        qna.setAuthId(1L); // 사용자 권한 1 설정

        qnaService.updateQna(id, qna);
        return "redirect:/qnas";
    }

    // QnA 삭제 처리
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
