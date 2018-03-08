/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

/**
 *  电气、热力机械倒闸操作票数据
 * @author LiuYingwen
 *
 */
public class OperateDetailBean {
	
	/* 执行情况 */
    private String execStatus="";
    /* 顺序 */
    private String displayNo="";
    /* 操作项目 */
    private String operateStepName="";
    /* 完成时间 */
    private String finishTime="";
    /* 备注 */
    private String meno = "";
    
	public String getMeno() {
		return meno;
	}
	public void setMeno(String meno) {
		this.meno = meno;
	}
	/**
	 * 执行情况
	 * @return the execStatus
	 */
	public String getExecStatus() {
		return execStatus;
	}
	/**
	 * 执行情况
	 * @param execStatus the execStatus to set
	 */
	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}
	/**
	 * 顺序
	 * @return the displayNo
	 */
	public String getDisplayNo() {
		return displayNo;
	}
	/**
	 * 顺序
	 * @param displayNo the displayNo to set
	 */
	public void setDisplayNo(String displayNo) {
		this.displayNo = displayNo;
	}
	/**
	 * 操作项目
	 * @return the operateStepName
	 */
	public String getOperateStepName() {
		return operateStepName;
	}
	/**
	 * 操作项目
	 * @param operateStepName the operateStepName to set
	 */
	public void setOperateStepName(String operateStepName) {
		this.operateStepName = operateStepName;
	}
	/**
	 * 完成时间
	 * @return the finishTime
	 */
	public String getFinishTime() {
		return finishTime;
	}
	/**
	 * 完成时间
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	
      
}