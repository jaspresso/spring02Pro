package com.example.spring02.aop;

import org.apache.catalina.tribes.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component //스프링에서 관리하는 범용 bean (참고:DAO에서 @Repository대신 적용가능)
@Aspect //공통적인 업무를 지원하는 aop bean
public class LogAdvice {
	//로깅을 위한 변수
	private static final Logger logger=LoggerFactory.getLogger(LogAdvice.class);
	
	//@Before(핵심업무 전), @After(핵심업무 후),  @Around(핵심업무 전,후 모두 사용)
	// ..은 모든 하위패키지를 의미, *(..)는 모든 메소드를 의미
	//@시점("범위" or "범위" or "범위"...)
	@Around("execution(* com.example.spring02.controller..*Controller.*(..))"
			+ " or execution(* com.example.spring02.service..*Impl.*(..))"
			+ " or execution(* com.example.spring02.model..dao.*Impl.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		//핵심업무가 실행되는 시점에 수행할 메소드
		long start=System.currentTimeMillis();//시스템의 밀리세컨드값
		Object result=joinPoint.proceed();
		String type=joinPoint.getSignature().getDeclaringTypeName();
		String name="";
		if(type.indexOf("Controller") > -1) {
			name="Controller : "; //콘솔창에 Controller : 표시됨
		}else if(type.indexOf("Service") > -1) {
			name="ServiceImpl : ";
		}else if(type.indexOf("DAO") > -1) {
			name="DAOImpl : ";
		}
		//호출한 클래스, method 정보를 로거에 저장
		logger.info(name+type+"."+joinPoint.getSignature().getName()+"()");
		//메소드에 전달되는 매개변수들을 로거에 저장
		logger.info(Arrays.toString(joinPoint.getArgs()));
		long end=System.currentTimeMillis();
		long time=end-start;
		logger.info("실행시간 : " + time);
		return result;
	}
}
