package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bank.repository.model.Account;

// AccountRepository 인터페이스와 account.xml 파일을 매칭 시킨다.
@Mapper
public interface AccountRepository {
	public int insert(Account account);
	public int updateById(Account account);
	public int deleteBtId(Integer id, String name);
	
	// 고민! - 계좌 조회 
	// --> 한 사람의 유저는 여러개의 계좌번호를 가질 수 있다. (리스트로 뽑아야 한다.) 
	// @Param 사용이유 : interface 파라미터명과 xml에 사용할 변수명을 다르게 사용해야 된다면
	// @param 애노테이션을 사용할 수 있다. 그리고 2개 이상에 파라미터를 사용할 경우 반드시 사용.
	public List<Account> findByUserId(@Param("userId") Integer principalId);
	
	// --> account id 값으로 계좌정보 조회. (필요하다.)
	public Account findByNumber(@Param("number") String id);
	
	
}
