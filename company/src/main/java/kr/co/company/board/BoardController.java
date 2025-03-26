package kr.co.company.board;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.company.common.dto.ResultDto;
import kr.co.company.member.Member;
import kr.co.company.member.MemberAccount;
import kr.co.company.member.MemberService;

@Tag(name = "3. 게시판", description = "게시판 관련 API")
@RequestMapping("/api/board")
@RestController
public class BoardController {
	
	private final MemberService memberService;
	
	public BoardController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@PostMapping("write")
	@Operation(summary = "게시판 글 작성", description = "게시판 글 작성 API")
	public ResponseEntity<ResultDto<Object>> write(@ParameterObject Board board, @AuthenticationPrincipal MemberAccount account) {
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//User user = (User) authentication.getPrincipal();
		//Member member = memberService.findByUserIdAndIsLeave(account.getUsername());
		//board.setMember(member);
		
		ResultDto<Object> result = ResultDto.builder()
				.status(HttpStatus.OK)
				.message("게시글이 저장되었습니다.")
				.resultData(board)
				.build();
		
		return ResponseEntity.ok(result);
	}

}
