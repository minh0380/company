package kr.co.company.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.company.common.file.FileEntity;
import kr.co.company.common.file.FileUtil;

@Service
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final FileUtil fileUtil;
	
	public BoardService(BoardRepository boardRepository, FileUtil fileUtil) {
		this.boardRepository = boardRepository;
		this.fileUtil = fileUtil;
	}
	
	@Transactional
	public void saveBoard(Board board, List<MultipartFile> files) throws IllegalStateException, IOException {
		List<FileEntity> fileEntities = fileUtil.fileUpload(files);
		board.setFileList(fileEntities);
		boardRepository.save(board);
	}
	
	public Map<String, Object> findBoardList(PageRequest pageRequest) {
		Map<String, Object> boardMap = new HashMap<>();
		Page<Board> boards = boardRepository.findByIsDel(false, pageRequest);
		
		boardMap.put("total", boards.getTotalElements());
		boardMap.put("boards", boards.getContent());
		
		return boardMap;
	}

}
