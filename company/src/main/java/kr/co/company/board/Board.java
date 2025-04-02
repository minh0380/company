package kr.co.company.board;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import kr.co.company.common.file.FileEntity;
import kr.co.company.member.Member;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Board {
	
	@Parameter(hidden = true)
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String bdId;
	@Parameter(
			name = "bdType",
			description = "게시판 종류",
			example = "notice"
	)
	private String bdType;
	@Parameter(
			name = "title",
			description = "제목",
			example = "[공지] 보안검색장 혼잡 안내"
	)
	private String title;
	@Parameter(
			name = "content",
			description = "내용",
			example = "성수기 여객 급증으로 출국장이 혼잡하오니 평소보다 충분한 여유를 두고 도착하시길 바랍니다.\r\n"
					+ "더불어, 관련 규정 및 보안강화 조치로 인해 아래의 경우에는 추가 보안검색을 시행할 수 있습니다.\r\n"
					+ "-굽 높이 3.5cm 이상의 신발\r\n"
					+ "-100ml 초과의 액체류\r\n"
					+ "-두꺼운 겉옷과 주머니 속 소지품\r\n"
					+ "신발검색, 전자기기 검색 등 보안검색에 시간이 다소 소요될 수 있사오니\r\n"
					+ "승객 여러분의 양해와 적극적인 협조 부탁드립니다."
	)
	private String content;
	@Parameter(hidden = true)
	@Column(columnDefinition = "date default current_timestamp")
	@Temporal(TemporalType.DATE)
	private Date regDate;
	@Parameter(hidden = true)
	@Column(columnDefinition = "number default 0")
	private boolean isDel;
	@Parameter(hidden = true)
	@ManyToOne // 작성자 한 명에 게시물은 여러 개
	private Member member;
	@Parameter(hidden = true)
	@OneToMany(cascade = CascadeType.ALL)
	private List<FileEntity> fileList;

}
