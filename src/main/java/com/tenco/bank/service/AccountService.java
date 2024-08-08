package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.dto.WithdrawalDTO;
import com.tenco.bank.handler.exception.DataDeliveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.History;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.utils.Define;

@Service // IoC 대상( 싱글톤으로 관리) 
public class AccountService {
	
	private final AccountRepository accountRepository;
	private final HistoryRepository historyRepository;
	
	@Autowired // 생략 가능 - DI 처리
	public AccountService(AccountRepository accountRepository, HistoryRepository historyRepository) {
		this.accountRepository = accountRepository; 
		this.historyRepository = historyRepository;
	}
	
	/**
	 * 계좌 생성 기능
	 * @param dto
	 * @param integer
	 */
	
	// 트랜 잭션 처리를 해야한다. (한번에 반영되거나, 아예 반영안되거나)  
	@Transactional
	public void createAccount(SaveDTO dto, Integer principalId) {
		
		int result = 0;
		
		try {
			result = accountRepository.insert(dto.toAccount(principalId));
			
		} catch (DataAccessException e) {
			throw new DataDeliveryException("잘못된 요청입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			throw new RedirectException("알 수 없는 오류 입니다.", HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		if(result == 0) {
			throw new DataDeliveryException("정상 처리 되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * 계좌 몇개 있는지 확인
	 * @param principal
	 */
	
	@Transactional
	public List<Account> readAccountListByUserId(Integer userId) {
		// TODO Auto-generated method stub
		List<Account> accountListEntity = null;
		
		try {
			accountListEntity = accountRepository.findByUserId(userId);
		} catch (DataDeliveryException e) {
			// TODO: handle exception
			throw new DataDeliveryException("잘못된 처리 입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new RedirectException("알 수 없는 오류", HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return accountListEntity; 
		
	}
	
	
	 // 한번에 모든 기능을 생각하는 것은 힘듬
	 // 1. 사용자가 던진 계좌번호가 존재하는지 여부를 확인해야 한다. --> select 
	 // 2. 본인 계좌 여부를 확인해야 한다. --> 객체 상태값에서 비교한다.
	 // 3. 계좌 비번 확인 --> 객체 상태값에서 일치 여부 확인 account에서 userid가 있고 principal에서도 확인가능
	 // 4. 잔액 여부 확인 --> 객체 상태값에서 확인
	 // 5. 출금 처리 --> update 쿼리 발생
	 // 6. history 테이블에 거래내역 등록 --> insert(history)
	 // 7. 트랜잭션 처리 ex) insert 하다가 오류나면 뒤로 update 가야 되기 때문에 트랙잭션 사용
	
	@Transactional
	public void updateAccountWithdraw(WithdrawalDTO dto, Integer principalId) {
		
		// 1. 사용자가 던진 계좌번호가 존재하는지 여부를 확인해야 한다. 
		// (퍼시스턴스 계층에서 긁어서 entity 붙임)
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if(accountEntity == null) {
			throw new DataDeliveryException(Define.NOT_EXIST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		
		// 2.
		accountEntity.checkOwner(principalId);
		
		// 3.
		accountEntity.checkPassword(dto.getWAccountPassword());
		
		// 4.
		accountEntity.checkBalance(dto.getAmount());
		
		// 5. 출금 처리 -- accountRepository 객체의 잔액을 변경하고 업데이트 처리해야 한다.
		accountEntity.withdraw(dto.getAmount());
		
		// update 처리
		accountRepository.updateById(accountEntity);
		
		// 6 - 거래 내역 등록
		/**
		 * <insert id="insert">
			insert into history_tb(amount, w_Balance, d_Balance, w_Account_id, d_account_id )
			values(#{amount}, #{wBalance}, #{dBalance}, #{wAccountId}, #{dAccountId} )
			</insert>
		 */
		
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(accountEntity.getBalance()); // 그 시점에 대한 잔액
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);
		
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new DataDeliveryException(Define.FAILED_PROCESSING, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
