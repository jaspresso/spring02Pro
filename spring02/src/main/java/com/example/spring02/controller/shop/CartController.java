package com.example.spring02.controller.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring02.model.shop.dto.CartDTO;
import com.example.spring02.service.shop.CartService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Controller
@RequestMapping("shop/cart/*") //공통 url
public class CartController {
	@Inject
	CartService cartService;
	
	@RequestMapping("list.do")
	public ModelAndView list(HttpSession session, ModelAndView mav) {
		Map<String, Object> map = new HashMap<>();
		//세션변수 확인
		String userid=(String)session.getAttribute("userid");
		if(userid != null) {//로그인한 경우
			List<CartDTO> list = cartService.listCart(userid);
			//장바구니 합계 계산
			int sumMoney = cartService.sumMoney(userid);
			//배송료 계산
			int fee = sumMoney >= 30000 ? 0 : 2500;//합계3만원이상이면 배송료 0원
			map.put("sumMoney", sumMoney);//장바구니 합계금액
			map.put("fee", fee);//배송료
			map.put("sum", sumMoney + fee); //총합계금액
			
			map.put("list", list);//맵에 자료 추가
			map.put("count", list.size());
			mav.setViewName("shop/cart_list"); //포워딩할 뷰
			mav.addObject("map", map); //전달할 데이터
			return mav;
		} else { //로그인하지 않은 경우 userid <- null
			return new ModelAndView("member/login", "", null);
		}
	}

	@RequestMapping("insert.do") //세부 url
	public String insert(HttpSession session, @ModelAttribute CartDTO dto) {
		// 세션에 userid 변수가 존재하는지 확인
		String userid = (String) session.getAttribute("userid");
//		if (userid == null) { // 로그인 하지 않은 상태
//			return "redirect:/member/login.do"; //로그인 페이지로
//		}
		// 장바구니에 insert 처리 후 장바구니 목록으로 이동
		dto.setUserid(userid);
		cartService.insert(dto);
		return "redirect:/shop/cart/list.do";
	}//insert()
	
	//장바구니 개별 상품 삭제
	@RequestMapping("delete.do")
	public String delete(@RequestParam int cart_id, HttpSession session) {
		if(session.getAttribute("userid") != null)
			cartService.delete(cart_id);
		return "redirect:/shop/cart/list.do";
	}
	
	@RequestMapping("deleteAll.do")
	public String deleteAll(HttpSession session) {
		//세션변수 조회
		String userid=(String)session.getAttribute("userid");
		if(userid != null) {
			//장바구니를 비우고
			cartService.deleteAll(userid);
		}
		//장바구니 목록으로 이동
		return "redirect:/shop/cart/list.do";
	}
	
	@RequestMapping("update.do")
	public String update(@RequestParam int[] amount, 
			@RequestParam int[] cart_id, HttpSession session) {
		String userid=(String)session.getAttribute("userid");
		if(userid != null) {
			//hidden으로 넘어오는 cart_id는 배열값으로 넘어온다.
			for(int i=0; i<cart_id.length; i++) {
				if(amount[i] == 0) {//수량이 0이면 레코드 삭제
					cartService.delete(cart_id[i]);
				}else {//0이 아니면 수정
					CartDTO dto = new CartDTO();
					dto.setUserid(userid);
					dto.setCart_id(cart_id[i]);
					dto.setAmount(amount[i]);
					cartService.modifyCart(dto);
				}
			}
		}
		return "redirect:/shop/cart/list.do";
	}

	@RequestMapping("pay.do")
	public ResponseEntity<?> callback_receive(@RequestBody Map<String, Object> model) {
		String process_result = "결제성공!";
		//응답 header 생성
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		JSONObject responseObj = new JSONObject();
	    try {
	    	String imp_uid = (String)model.get("imp_uid");
	    	String merchant_uid = (String)model.get("merchant_uid");
	    	boolean success = (Boolean)model.get("success");
	    	String error_msg = (String)model.get("error_msg");
	    	
	    	System.out.println("---callback receive---");
	    	System.out.println("----------------------");
	    	System.out.println("imp_uid : " + imp_uid);
	    	System.out.println("merchant_uid : " + merchant_uid);
	    	System.out.println("success : " + success);
	    	
	    	if(success == true) {
	    		//db select(select amount from order_table where merchant_uid = ?)
	    		
	    		//STEP 5
	    		String api_key ="8214571337467313";//본인이 생성한 api key
	    		String api_secret = "5rSQROrAqxLRdiFhUybDQLE6s1TKqZ4yDzY6u549QHzfvE8NTFtUZ4ovsYehXS1NYG97ffj32mBJzCg7";//본인이 생성한 api secret key
	    		
	    		IamportClient ic = new IamportClient(api_key, api_secret);
	    		IamportResponse<Payment> response = ic.paymentByImpUid(imp_uid);
	    		
	    		BigDecimal iamport_amount = response.getResponse().getAmount();
	    		
	    		responseObj.put("process_result", "결제성공");
	    		
	    	}else {
	    		System.out.println("error_msg : " + error_msg);
	    		responseObj.put("process_result", "결제실패 : " + error_msg);
	    	}

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseObj.put("process_result", "결제실패 : 관리자에게 문의해 주세요.");
	    }
	    return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
	}
	
}//end class
