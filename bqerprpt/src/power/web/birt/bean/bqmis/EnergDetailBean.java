/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

/**
 * 热机热控票附页数据
 * @author LiuYingwen 
 */
public class EnergDetailBean {

	/** 安全措施 */
    private String safeMethod="";
    /** 执行情况 */
    private String excuteResult="";

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
	 * @return the excuteResult
	 */
	public String getExcuteResult() {
		return excuteResult;
	}

	/**
	 * @param excuteResult the excuteResult to set
	 */
	public void setExcuteResult(String excuteResult) {
		this.excuteResult = excuteResult;
	}
	
	
}
