/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.bqmis;

/**
 * 电气一种票附页数据
 * @author LiuYingwen 
 */
public class ElectricOneDetailBean {

	/** 安全措施 */
    private String safeMethod="";
    /** 执行措施 */
    private String execuMethod="";
	/**
	 * @return the safeMethod
	 */
	public String getSafeMethod() {
		return safeMethod;
	}
	/**
	 * @param safeMethod the safeMethod to set
	 */
	public void setSafeMethod(String safeMethod) {
		this.safeMethod = safeMethod;
	}
	/**
	 * @return the execuMethod
	 */
	public String getExecuMethod() {
		return execuMethod;
	}
	/**
	 * @param execuMethod the execuMethod to set
	 */
	public void setExecuMethod(String execuMethod) {
		this.execuMethod = execuMethod;
	}
    
}
