package kr.co.company.member;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class MemberAccount extends User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Member member;
	
	public MemberAccount(Member member) {
		super(member.getUserId(), member.getPassword(), member.getAuthority());
		this.member = member;
	}

}
