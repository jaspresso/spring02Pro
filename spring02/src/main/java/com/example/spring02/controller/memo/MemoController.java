package com.example.spring02.controller.memo;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring02.model.memo.dto.MemoDTO;
import com.example.spring02.service.memo.MemoService;

@Controller //스프링에게 컨트럴러빈으로 등록
@RequestMapping("memo/*") //공통적인 url pattern
public class MemoController {
	
	@Inject
	MemoService memoService;
	
	//memo/list.do
	@RequestMapping("list.do") //세부적인 url pattern
	public ModelAndView list(ModelAndView mav) {
		List<MemoDTO> items=memoService.list();
		//views/memo/memo_list.jsp
		mav.setViewName("memo/memo_list"); //포워딩할 뷰의 이름
		mav.addObject("list", items); //전달할 데이터(모델)
		return mav; // return new ModelAndView("memo/memo_list", "list",items);
	}
	
	@RequestMapping("insert.do")
	public String insert(@ModelAttribute MemoDTO dto) {
		memoService.insert(dto);
		return "redirect:/memo/list.do";//목록갱신요청
	}
	
	@RequestMapping("view/{idx}")
	public ModelAndView view(@PathVariable int idx, ModelAndView mav) {
		mav.setViewName("memo/view");//포워딩하는 뷰이름
		mav.addObject("dto", memoService.memo_view(idx));
		return mav;
	}
	
	@RequestMapping("update/{idx}")
	public String update(@PathVariable int idx, @ModelAttribute MemoDTO dto) {
		memoService.update(dto);
		return "redirect:/memo/list.do";//목록갱신요청
	}
	
	@RequestMapping("delete/{idx}")
	public String delete(@PathVariable int idx) {
		memoService.delete(idx);
		return "redirect:/memo/list.do";//목록갱신요청
	}

}
