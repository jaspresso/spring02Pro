package com.example.spring02.controller.message;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring02.model.message.dto.MessageDTO;
import com.example.spring02.service.message.MessageService;

@RestController
@RequestMapping("messages/*")
public class MessageController {
	@Inject
	MessageService messageService;
	@RequestMapping(value = "/")
	public ResponseEntity<String> addMessage(@RequestBody MessageDTO dto){
		//웹페이지에서 json으로 request한 파라미터들을 java에서 받으려면 java object로의 변환이
		//필요하며 마찬가지로 response시에도 java object에서 json으로 변환이 필요함.
		//이런 작업을 해주는게 @RequestBody, @ResponseBody이다. 컨트롤러에서 두 어노테이션을
		//추가해주면 json이나 key/value 방식의 xml등으로 송수신 할 수 있다.
		//@RequestBody : HTTP 요청의 body 내용을 자바 객체로 매핑하는 역할
		//@ResponseBody : 자바 객체를 HTTP 요청의 body 내용으로 매핑
		//ResponseEntity : 리턴값을 json+에러메시지를 함께 처리
		ResponseEntity<String> entity=null;
		try {
			messageService.addMessage(dto);
			entity=new ResponseEntity<>("success",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity=new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
			//ex)400에러 : 상호간 변수가 안맞을 때
		}
		return entity;
	}
}
