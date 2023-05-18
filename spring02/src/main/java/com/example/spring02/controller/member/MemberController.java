package com.example.spring02.controller.member;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring02.model.member.dto.MemberDTO;
import com.example.spring02.service.member.MemberService;

@Controller 
@RequestMapping("member/*")//공통 url
public class MemberController {
	//로깅을 위한 변수
	private static final Logger logger=
			LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService memberService;
	
	@RequestMapping("address.do")
	public String address() {
		return "member/join";
	}
	
	//회원리스트
	@RequestMapping("list.do")
	public String memberList(Model model) {
		List<MemberDTO> list=memberService.list();
		model.addAttribute("list", list);
		return "member/member_list";
	}
	
	//회원등록
	@RequestMapping("write.do")
	public String write() {
		return "member/write";
	}
	
	//회원입력
	@RequestMapping("insert.do")
	public String insert(@ModelAttribute MemberDTO dto) {
		memberService.insertMember(dto);
		return "home";
	}
	
	//회원수정관련
	@RequestMapping("view.do")
	public String view(@RequestParam String userid, Model model) {
		//모델에 자료 저장
		model.addAttribute("dto", memberService.viewMember(userid));
		return "member/view";
	}
	
	//회원수정
	@RequestMapping("update.do")
	public ModelAndView update(MemberDTO dto, HttpSession session) {
		//비밀번호 체크
		boolean result=memberService.loginCheck(dto, session);
		ModelAndView mav=new ModelAndView();
		if(result) {//비번이 맞으면
			//회원정보 수정
			memberService.updateMember(dto);
			//수정 후 홈 이동
			mav.setViewName("home");
		}else {//비번이 틀리면
			mav.addObject("dto", dto);
			//실패 후 회원의 정보를 다시 보여주기위한 코드
			mav.addObject("join_date", 
					memberService.viewMember(dto.getUserid()).getJoin_date());
			//getJoin_date()는 회원 정보 수정이 실패했을 때도 이전에 입력된 회원정보를 그대로
			//보여주기 위해 사용된것임.
			mav.addObject("message", "비밀번호를 확인하세요.");
			mav.setViewName("member/view");
		}
		return mav;
	}
	
	//회원삭제
	@RequestMapping("delete.do")
	public String delete(@RequestParam String userid) throws Exception {
		memberService.deleteMember(userid);
		return "redirect:/member/list.do";
	}

	@RequestMapping("login.do")//세부 url
	public String login() {
		return "member/login";
	}
	
	@RequestMapping("login_check.do")
	public ModelAndView login_check(MemberDTO dto, HttpSession session) {
		//로그인 성공 true, 실패 false
		boolean result=memberService.loginCheck(dto, session);
		ModelAndView mav=new ModelAndView();
		if(result) {//로그인 성공
			mav.setViewName("home");
		}else {
			mav.setViewName("member/login");//뷰이름
			mav.addObject("message", "error");//뷰에 전달할 값
		}
		return mav;
	}
	
	@RequestMapping("logout.do")
	public ModelAndView logout(HttpSession session, ModelAndView mav) {
		//세션초기화
		memberService.logout(session);
		//login.jsp로 이동
		mav.setViewName("member/login");
		mav.addObject("message", "logout");
		return mav;
	}
	
}

