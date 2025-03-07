package kr.co.company.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.company.jwt.JwtTokenDto;
import kr.co.company.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	// bcrypt를 spring이 MemberService에 넣어줌. PasswordEncoder를 지정해주지 않으면 null이라서 에러남
	private PasswordEncoder encoder;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public MemberService(MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	public boolean existsByUserIdAndIsLeave(String userId) {
		return memberRepository.existsByUserIdAndIsLeave(userId, false);
	}
	
	public Member findByUserIdAndIsLeave(String userId) {
		return memberRepository.findByUserIdAndIsLeave(userId, false);
	}
	
	@Transactional
	public void saveMember(Member member) {
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		memberRepository.save(member);
	}
	
	@Transactional
	public JwtTokenDto signIn(String username, String password) {
		// 1. username + password를 기반으로 Authentication 객체 생성
		// 이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		// 2. 실제 검증 authenticate() 메서드를 통해 요청된 Member에 대한 검증 진행
		// authenticate 메서드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 메서드 실행
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		// 3. 인증 정보를 기반으로 JWT 토큰 생성
		JwtTokenDto jwtToken = jwtTokenProvider.generateToken(authentication);
		
		return jwtToken;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUserIdAndIsLeave(username, false);
		
		return new MemberAccount(member);
	}

}
