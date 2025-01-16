package com.project.easyBuild.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.authority.biz.MemberBoardBiz;
import com.project.easyBuild.authority.biz.ProductBiz;
import com.project.easyBuild.authority.dto.MemberBoardDto;
import com.project.easyBuild.authority.dto.ProductDto;
import com.project.easyBuild.board.entity.Announcement;
import com.project.easyBuild.board.entity.Faq;
import com.project.easyBuild.board.entity.Qna;
import com.project.easyBuild.board.service.AnnouncementService;
import com.project.easyBuild.board.service.FaqService;
import com.project.easyBuild.board.service.QnaService;
import com.project.easyBuild.entire.biz.OrderBiz;
import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.member.biz.MemberBiz;
import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.user.biz.CartBiz;
import com.project.easyBuild.user.biz.QnaBiz;
import com.project.easyBuild.user.biz.ReviewBiz;
import com.project.easyBuild.user.dto.CartDto;
import com.project.easyBuild.user.dto.QnaDto;
import com.project.easyBuild.user.dto.ReviewDto;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
    // 공통 권한 확인 메서드
	@ModelAttribute("isAdmin")
	public boolean isAdmin(HttpSession session) {
	    MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user != null) {
	        System.out.println("User in session: " + user.getUserId() + ", AuthId: " + user.getAuthId());
	        return user.getAuthId() == 2;
	    }
	    System.out.println("No user in session or user is null.");
	    return false;
	}

	@GetMapping("/")
	public String index(Model model) {
        // 최신 공지사항 5개
        List<Announcement> latestAnnouncements = announcementService.getLatestAnnouncements(5);
        // 최신 faq 5개
        List<Faq> latestFaqs = faqService.getLatestFaqs(5);

        // 모델에 데이터를 추가
        model.addAttribute("latestAnnouncements", latestAnnouncements);
        model.addAttribute("latestFaqs", latestFaqs);
		return "index";
	}

	@GetMapping("/example")
	public String example() {
		return "example/example";
	}

	//관리자페이지 관련
	@Autowired
	private ProductBiz productbiz;
	@Autowired
	private AnnouncementService announcementService;
	@Autowired
    private FaqService faqService;
	@Autowired
    private QnaService qnaService;


	@GetMapping("/auth-index")
	public String authIndex(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page) {
		int pageSize = 5;
		
	    MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null || user.getAuthId() != 2) {
	        System.out.println("Access denied. User: " + (user != null ? user.getUserId() : "null"));
	        return "error/403"; // 권한 없음 에러 페이지
	    }
	    System.out.println("Access granted for admin: " + user.getUserId());
	    List<ProductDto> res = productbiz.listAll();        
	    model.addAttribute("list", res);
	    
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
        
        // review
        List<ReviewDto> reviewLists = reviewbiz.listAll();
        model.addAttribute("reviewLists", reviewLists);

	    return "pages/authority/auth-index";
	}

	

	@GetMapping("/auth-product")
	public String authProduct(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page) {
        if (!isAdmin(session)) {
            return "error/403";
        }
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<ProductDto> productPage = productbiz.listAllPaginated(pageable);

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("totalItems", productPage.getTotalElements());

		return "pages/authority/auth-product";
	}

	@GetMapping("/auth-product-insert")
	public String authProductInsert(HttpSession session, Model model) {
		MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null || user.getAuthId() != 2) {
	        System.out.println("Access denied. User: " + (user != null ? user.getUserId() : "null"));
	        return "error/403"; // 권한 없음 에러 페이지
	    }
	    System.out.println("Access granted for admin: " + user.getUserId());
		
		model.addAttribute("userId", user.getUserId());
		model.addAttribute("authId", user.getAuthId());
		
		return "pages/authority/auth-product-insert";
	}

	@GetMapping("/auth-order")
	public String authOrder(HttpSession session, Model model) {
		MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null || user.getAuthId() != 2) {
	        System.out.println("Access denied. User: " + (user != null ? user.getUserId() : "null"));
	        return "error/403"; // 권한 없음 에러 페이지
	    }
	    System.out.println("Access granted for admin: " + user.getUserId());
		
		model.addAttribute("userId", user.getUserId());
		model.addAttribute("authId", user.getAuthId());
		
	    List<OrderDto> orders = orderbiz.listAll(); // 모든 주문 데이터 가져오기
	    model.addAttribute("orders", orders); // 주문 리스트 추가
	    model.addAttribute("orderDto", new OrderDto()); // 빈 orderDto 객체 추가
	    return "pages/authority/auth-order";
	}


	@GetMapping("/auth-category")
	public String authCategory() {
		return "pages/authority/auth-category";
	}

	//마이페이지 관련
	@Autowired
	private ReviewBiz reviewbiz;

	@GetMapping("/my/review")
    public String myReview(HttpSession session, Model model) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        if (user == null) {
            return "redirect:/loginform"; // 로그인 페이지로 리다이렉트
        }

        List<ReviewDto> writeReviews = reviewbiz.writeListAll(user.getUserId());
        model.addAttribute("writeReviews", writeReviews);
        List<ReviewDto> writtenReviews = reviewbiz.writtenListAll(user.getUserId());
        model.addAttribute("writtenReviews", writtenReviews);
        
        return "pages/mypage/my-review";
    }

	@Autowired
	private QnaBiz qnabiz;

	@GetMapping("/my/qna")
	public String myQna(HttpSession session, Model model) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        if (user == null) {
            return "redirect:/loginform";
        }
        
		List<QnaDto> qnas = qnabiz.mylistAll(user.getUserId());
		model.addAttribute("qnas", qnas);
		return "pages/mypage/my-qna";
	}
	
	@Autowired
	private OrderBiz orderbiz;

	@GetMapping("/my/order")
	public String myOrder(HttpSession session, Model model) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        if (user == null) {
            return "redirect:/loginform";
        }
        
		List<OrderDto> orders = orderbiz.mylistAll(user.getUserId());
		model.addAttribute("orders", orders);
		return "pages/mypage/my-order";
	}
	
	@Autowired
	private CartBiz cartbiz;
	
	@GetMapping("/my/cart")
	public String myCart(HttpSession session, Model model) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        if (user == null) {
            return "redirect:/loginform";
        }
        
		List<CartDto> carts = cartbiz.mylistAll(user.getUserId());
		model.addAttribute("carts", carts);
		return "pages/mypage/my-cart";
	}

    //회원 관리
    @Autowired
    private MemberBoardBiz memberBoardBiz;

    @GetMapping("/auth-member")
    public String authMember(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page) {
        if (!isAdmin(session)) {
            return "error/403";
        }
        
    	int pageSize = 10; // 한 페이지에 표시할 회원 수
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<MemberBoardDto> memberPage = memberBoardBiz.listAllWithPagination(pageable);

        // 디버깅: 페이지 콘텐츠 출력
        System.out.println("Fetched members: " + memberPage.getContent());
        System.out.println("Total members: " + memberPage.getTotalElements());

        // 총 회원 수
        long totalMembers = memberPage.getTotalElements();

        // View로 데이터 전달
        model.addAttribute("page", memberPage);  // Thymeleaf에서 참조할 이름
        model.addAttribute("totalMembers", totalMembers); // 총 회원 수 전달

        return "pages/authority/auth-member";
    }

    // 회원 상세 정보 페이지
    @GetMapping("/auth-member/{userId}")
    public String authMemberDetail(@PathVariable String userId, Model model) {
        System.out.println("Received userId: " + userId);

        // 회원 정보를 가져옴
        MemberBoardDto member = memberBoardBiz.getMemberById(userId);

        // 예외 처리: 회원 정보를 찾지 못한 경우
        if (member == null) {
            System.out.println("회원 정보를 찾을 수 없습니다. userId: " + userId);
            model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
            return "pages/authority/error-page"; // 에러 페이지 반환
        }

        // 주문 내역을 가져옴
        List<OrderDto> orders = orderbiz.mylistAll(userId);

        // 모델에 회원 정보와 주문 내역을 추가
        model.addAttribute("member", member); // 회원 정보 전달
        model.addAttribute("orders", orders); // 주문 내역 전달

        return "pages/authority/auth-member-detail"; // Thymeleaf 템플릿 반환
    }

    
    
    
    // 회원 탈퇴
    @DeleteMapping("/auth-member/{userId}")
    public ResponseEntity<Void> deleteMember(@PathVariable String userId) {
        try {
            memberBoardBiz.deleteMember(userId); // 비즈니스 로직 호출
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/auth-member/{userId}/orders")
    public ResponseEntity<?> getUserOrders(@PathVariable String userId) {
        try {
            List<OrderDto> memberOrders = orderbiz.mylistAll(userId); // 주문 내역 가져오기
            return ResponseEntity.ok(memberOrders); // 성공 시 리스트 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 주문 내역 조회 중 오류가 발생했습니다.");
        }
    }

    
    @Autowired
    private MemberBiz memberbiz;
    
	@GetMapping("/loginform")
    public String login() {
    	return "pages/member/login";
    }
    
	@PostMapping("/member/login")
	public String login(Model model, MemberDto dto, HttpSession session) {
	    MemberDto result = memberbiz.login(dto);
	    
	    if (result != null) {
	        session.setAttribute("dto", result);  // 로그인 정보 세션에 저장
	        System.out.println("Logged in user: " + result.getUserId() + ", AuthId: " + result.getAuthId());
	        return "redirect:/";  // 로그인 성공
	    } else {
	        return "redirect:/loginform";  // 로그인 실패
	    }
	}

	@GetMapping("/find_idform")
	public String find_idform() {
		return "pages/member/find_id";
	}
	
	@GetMapping("/find_pwform")
	public String find_pwform() {
		return "pages/member/find_pw";
	}
	
	@PostMapping("/find_id")
    public String find_id(Model model, String userName, String email, MemberDto dto) {
        try {
            dto.setUserName(userName);
            dto.setEmail(email);

            MemberDto id = memberbiz.find_id(dto); // 서비스에서 DAO 호출

            if (id == null) {
                model.addAttribute("msg", "아이디를 찾을 수 없습니다.");
            } else {
                model.addAttribute("findId", id); // findId가 null이 아닐 때 객체 전달
            }
        } catch (Exception e) {
            model.addAttribute("msg", "오류가 발생되었습니다.");
            e.printStackTrace();
        }
        return "pages/member/findIdResult"; // Thymeleaf 템플릿을 반환
    }

	@PostMapping("/find_pw")
	public String findPw(MemberDto dto, Model model) throws Exception {
		System.out.println("memberPw"+dto.getUserId() + dto.getEmail());
	    if (memberbiz.findPwCheck(dto) == 0) {
	        model.addAttribute("msg", "아이디와 이메일을 확인해주세요");
	        return "pages/member/find_pw";  
	    } else {
	        memberbiz.findPw(dto.getEmail(), dto.getUserId());
	        model.addAttribute("member", dto.getEmail());
	        return "redirect:/loginform";
	    }
	}
	
    @GetMapping("/sign_up")
    public String sign_up() {
    	return "pages/member/sign_up";
    }
    
    @GetMapping("/membermy")
    public String membermy(Model model, @SessionAttribute(name = "dto", required = false) MemberDto dto) {
        // 로그인된 회원 정보가 없을 경우 loginform으로 리다이렉트
        if (dto == null) {
            return "redirect:/loginform";
        }

        // 로그인된 회원 정보가 있을 경우, dto를 모델에 추가
        model.addAttribute("dto", dto);
        return "pages/member/membermy";
    }

    @GetMapping("delete_member")
    public String delete_member(String userId, RedirectAttributes redirectAttributes) {
        try {
            int res = memberbiz.delete(userId);
            if (res > 0) {
                redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 성공적으로 처리되었습니다.");
                return "redirect:/member/logout";
            } else {
                redirectAttributes.addFlashAttribute("error", "해당 회원 정보를 찾을 수 없습니다.");
                return "pages/member/membermy";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 탈퇴 처리 중 오류가 발생했습니다.");
            return "pages/member/membermy";
        }
        
    }
    
    @GetMapping("/my/mydetail")
    public String mydetail(Model model, String userId) {
    	MemberDto res = memberbiz.selectOne(userId);
    	model.addAttribute("dto",res);
    	
    	return "pages/member/mydetail";
    }
    
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "redirect:/";  // 로그아웃 후 홈 페이지로 리다이렉트
    }
    
    @GetMapping("/my/member/update")
    public String update(@RequestParam String userId, Model model) {
        if (userId == null || userId.isEmpty()) {
            return "redirect:/loginform";  // 예시: 유효하지 않은 userId가 있을 경우
        }
        MemberDto res = memberbiz.selectOne(userId);
        model.addAttribute("dto", res);
        return "pages/member/update";
    }

    
    @PostMapping("/my/member/updateform")
    public String updateform(String userName,String password, String phone,String userId) {
    	MemberDto dto = new MemberDto();
    	dto.setUserName(userName);
    	dto.setPassword(password);
    	dto.setPhone(phone);
    	dto.setUserId(userId);
    	int res = memberbiz.update(dto);
    	if(res>0) {
    		return "redirect:/my/mydetail?userId=" + dto.getUserId();
    	}else {
    		return "redirect:/my/member/update?userId=" + userId;
    	}
    }
}