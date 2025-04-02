package kr.co.company.common.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.company.common.code.ConfigCode;

@Component
public class FileUtil {
	
	public List<FileEntity> fileUpload(List<MultipartFile> files) throws IllegalStateException, IOException {
		List<FileEntity> fileDatas = new ArrayList<>();
		String savePath = getSavePath();
		
		if(files != null && files.size() >= 1 && !files.get(0).getOriginalFilename().equals("")) {
			for (MultipartFile multipartFile : files) {
				String saveFileName = UUID.randomUUID().toString();
				String orgFileName = multipartFile.getOriginalFilename();
				
				FileEntity file = new FileEntity();
				file.setOrgFileName(orgFileName);
				file.setSaveFileName(saveFileName);
				file.setSavePath(savePath);
				fileDatas.add(file);
				
				saveFile(multipartFile, file);
			}
		}
		
		return fileDatas;
	}
	
	private String getSavePath() {
		Calendar cal = Calendar.getInstance();
		
		return ConfigCode.UPLOAD_PATH.toString()
				+ cal.get(Calendar.YEAR)
				+ "/" + (cal.get(Calendar.MONTH) + 1)
				+ "/" + cal.get(Calendar.DAY_OF_MONTH)
				+ "/";
	}
	
	private void saveFile(MultipartFile multipartFile, FileEntity fileEntity) throws IllegalStateException, IOException {
		File file = new File(fileEntity.getSavePath() + fileEntity.getSaveFileName());
		if(!file.exists()) {
			new File(fileEntity.getSavePath()).mkdirs();
		}
		
		multipartFile.transferTo(file);
	}

}
