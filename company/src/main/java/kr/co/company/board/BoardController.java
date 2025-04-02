package kr.co.company.board;

import java.io.IOException;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.company.common.dto.ResultDto;
import kr.co.company.member.Member;
import kr.co.company.member.MemberService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "3. 게시판", description = "게시판 관련 API")
@Slf4j
@RequestMapping("/api/board")
@RestController
public class BoardController {
	
	private final MemberService memberService;
	private final BoardService boardService;
	
	public BoardController(MemberService memberService, BoardService boardService) {
		this.memberService = memberService;
		this.boardService = boardService;
	}
	
	@PostMapping(value = "write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시판 글 작성", description = "게시판 글 작성 API")
	public ResponseEntity<ResultDto<Object>> write(@ParameterObject Board board
			, @RequestPart(name = "files", required = false) List<MultipartFile> files
			, @AuthenticationPrincipal UserDetails user) {
		Member member = memberService.findByUserIdAndIsLeave(user.getUsername());
		board.setMember(member);
		ResultDto<Object> result;
		
		try {
			boardService.saveBoard(board, files);
			
			result = ResultDto.builder()
					.status(HttpStatus.OK)
					.message("게시글이 저장되었습니다.")
					.resultData(board)
					.build();
			
			return ResponseEntity.ok(result);
		} catch (IllegalStateException | IOException | NullPointerException e) {
			result = ResultDto.builder()
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.message("게시글 저장 중 에러가 발생했습니다.")
					.resultData(e.getMessage())
					.build();
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@GetMapping("list")
	@Operation(summary = "게시판 글 목록 조회", description = "게시판 글 목록 조회 API")
	public ResponseEntity<ResultDto<Object>> list(@Parameter(name = "page", description = "페이지", example = "0") @RequestParam(value = "page", defaultValue = "0") int page
			, @Parameter(name = "size", description = "개수", example = "10") @RequestParam("size") int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		ResultDto<Object> result = ResultDto.builder()
				.status(HttpStatus.OK)
				.resultData(boardService.findBoardList(pageRequest))
				.build();
		
		return ResponseEntity.ok(result);
	}

}
