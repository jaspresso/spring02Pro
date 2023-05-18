package com.example.spring02.controller.board;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring02.model.board.dto.ReplyDTO;
import com.example.spring02.service.board.ReplyService;

@RestController
@RequestMapping("reply/*") //공통 url
public class ReplyController {
	@Inject
	ReplyService replyService;
	
	@RequestMapping("insert.do")
	public void insert(ReplyDTO dto, HttpSession session) {
		//세션에 저장된 댓글 작성자 아이디를 가져와 처리
		String userid=(String)session.getAttribute("userid");
		dto.setReplyer(userid);
		//테이블 저장 처리
		replyService.create(dto);
		//Ajax로 값만 넘기고 끝나기 때문에 jsp 페이지로 가거나 데이터를 리턴하지 않음
	}
	
	@RequestMapping("list.do")
	public ModelAndView list(int bno, ModelAndView mav) {
		List<ReplyDTO> list=replyService.list(bno);
		mav.setViewName("board/reply_list"); //포워딩 뷰
		mav.addObject("list", list); //뷰에 전달할 데이터
		return mav;
	}
	
	//댓글 목록을 ArrayList로 리턴
	@RequestMapping("list_json.do")
	public List<ReplyDTO> list_json(int bno){
		return replyService.list(bno);
	}
	

}
