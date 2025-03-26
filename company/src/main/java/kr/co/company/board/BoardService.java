package kr.co.company.board;

import org.springframework.transaction.annotation.Transactional;

public class BoardService {
	
	private final BoardRepository boardRepository;
	
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	@Transactional
	public void saveBoard(Board board) {
		boardRepository.save(board);
	}

}
