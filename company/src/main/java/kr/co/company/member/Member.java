package kr.co.company.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@DynamicInsert //엔티티의 필드 중 null인 필드는 insert 쿼리에서 생략 -> default 값으로 insert
@DynamicUpdate //엔티티의 필드 중 변경이 감지되지 않은 필드는 update 쿼리에서 생략
public class Member {
	
	@Parameter(
			name = "userId",
			description = "아이디",
			example = "mhcho"
	)
	@Id
	private String userId;
	@Parameter(
			name = "password",
			description = "비밀번호",
			example = "mhchopassword"
	)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@Parameter(
			name = "name",
			description = "이름",
			example = "조민희"
	)
	private String name;
	@Parameter(
			name = "tel",
			description = "전화번호",
			example = "01012345678"
	)
	private String tel;
	@Parameter(
			name = "position",
			description = "직급",
			example = "대리"
	)
	private String position;
	@Parameter(
			name = "jobDetail",
			description = "직무 상세 정보",
			example = "프로젝트 수행"
	)
	private String jobDetail;
	@Parameter(hidden = true)
	@Column(columnDefinition = "varchar(20) default 'ROLE_USER'")
	private String role;
	@Parameter(hidden = true)
	@Column(columnDefinition = "number default 0")
	private Boolean isLeave;
	@Parameter(hidden = true)
	@Column(columnDefinition = "date default current_timestamp")
	private Date regDate;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public List<SimpleGrantedAuthority> getAuthority() {
		List<SimpleGrantedAuthority> auth = new ArrayList<>();
		auth.add(new SimpleGrantedAuthority(role));
		return auth;
	}

}
