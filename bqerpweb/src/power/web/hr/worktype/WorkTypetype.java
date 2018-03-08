/**
 * 工种类别设置
 */
package power.web.hr.worktype;

/**
 * @author Administrator
 * 
 */
public class WorkTypetype {
	private Long EMP_TYPE_ID;
	private String EMP_TYPE_NAME;
	private String isUse;
	private String RETRIEVE_CODE;
	
	public Long getEMP_TYPE_ID() {
		return EMP_TYPE_ID;
	}
	public void setEMP_TYPE_ID(Long emp_type_id) {
		EMP_TYPE_ID = emp_type_id;
	}
	public String getEMP_TYPE_NAME() {
		return EMP_TYPE_NAME;
	}
	public void setEMP_TYPE_NAME(String emp_type_name) {
		EMP_TYPE_NAME = emp_type_name;
	}
	public String getIsUse() {
		if (isUse.equals("U"))
			return "启用";
		else
			return "禁用";
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getRETRIEVE_CODE() {
		return RETRIEVE_CODE;
	}
	public void setRETRIEVE_CODE(String retrieve_code) {
		RETRIEVE_CODE = retrieve_code;
	}

}