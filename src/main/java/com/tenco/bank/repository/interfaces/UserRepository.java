package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bank.repository.model.User;


// 마이바티스 설정 확인

// UserRepository 인터페이스와 user.xml 파일을 매칭 시킨다.

@Mapper // 반드시 선언을 해야 동작한다.	
public interface UserRepository {
	// 유저와 관련된 기능들을 설정한다.
	
	public int insert(User user);
	public int updateById(User user);
	public int deleteById(Integer id);
	public User findById(Integer id);
	public List<User> findAll();
	
	// 로그인 처리 기능 X (username, password)를 받아서 유저가 맞는지 확인하는 기능 없다.
	// 주의 ! -- @Mapper를 통해 xml이랑 연결된다.
	// 주의 ! -- 매개변수 2개 이상 시 반드시 @Param 어노테이션을 사용해야 한다. 
	public User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
	
}
