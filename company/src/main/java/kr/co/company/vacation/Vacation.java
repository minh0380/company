package kr.co.company.vacation;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import kr.co.company.member.Member;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Vacation {
	
	@Parameter(hidden = true)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vId;
	@ManyToOne // 직원 한 명이 휴가 여러 번 신청
	private Member member;
	@Column(columnDefinition = "number default 0")
	private int vacation;
	@Column(columnDefinition = "date default current_timestamp")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Column(columnDefinition = "date default current_timestamp")
	@Temporal(TemporalType.DATE)
	private Date endDate;
	@Column(columnDefinition = "date default current_timestamp")
	private Date regDate;

}
