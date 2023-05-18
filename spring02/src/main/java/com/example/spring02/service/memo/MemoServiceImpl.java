package com.example.spring02.service.memo;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.example.spring02.model.memo.dao.MemoDAO;
import com.example.spring02.model.memo.dto.MemoDTO;

@Service
public class MemoServiceImpl implements MemoService {

	//Service가 DAO를 호출
	@Inject //의존관계 주입(DI)
	MemoDAO memoDao;

	@Override
	public List<MemoDTO> list() {
		return memoDao.list(); //list()는 완성된 인터페이스가 아닌데도
		//쓸 수 있는 이유는 스프링이 root-context.xml에서 지정한 mybatis객체를
		//메모리에 올려 셋팅에 의해 생성된 객체를 자동 결합시키며 ArrayList로 받아 처리한다.
	}

	@Override
	public void insert(MemoDTO dto) {
		memoDao.insert(dto.getWriter(), dto.getMemo());
	}

	@Override
	public MemoDTO memo_view(int idx) {
		return memoDao.memo_view(idx);
	}

	@Override
	public void update(MemoDTO dto) {
		memoDao.update(dto);
	}

	@Override
	public void delete(int idx) {
		memoDao.delete(idx);
	}

}
