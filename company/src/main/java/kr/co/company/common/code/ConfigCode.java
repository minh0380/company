package kr.co.company.common.code;

public enum ConfigCode {
	
	UPLOAD_PATH("C:/wisenut/workspace/company/upload/");
	
	public String desc;
	
	ConfigCode(String desc) {
		this.desc = desc;
	}
	
	public String toString() {
		return desc;
	}

}
