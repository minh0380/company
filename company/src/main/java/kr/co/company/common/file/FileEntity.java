package kr.co.company.common.file;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class FileEntity {
	
	@Parameter(hidden = true)
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String flId;
	@Parameter(
			name = "orgFileName",
			description = "파일 원본명",
			example = "보안검색장 혼잡 안내문"
	)
	private String orgFileName;
	@Parameter(hidden = true)
	private String saveFileName;
	@Parameter(hidden = true)
	private String savePath;
	@Parameter(hidden = true)
	@Column(columnDefinition = "date default current_timestamp")
	@Temporal(TemporalType.DATE)
	private Date regDate;
	@Parameter(hidden = true)
	@Column(columnDefinition = "number default 0")
	private boolean isDel;

}
