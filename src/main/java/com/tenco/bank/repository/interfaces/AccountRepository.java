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
	public List<Account> findByUserId(@Param("userId") Integer principalId);
	
	// --> account id 값으로 계좌정보 조회. (필요하다.)
	public Account findByNumber(@Param("number") String id);
	
	
	
}
