/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

/**
 * 危险点列表数据
 * @author LiuYingwen
 *
 */
public class WorkticketDanger implements java.io.Serializable{
	private String dangerId;
	private String dangerName;
	private String dangerMeasure;
	private String orderBy;
	private String isRunadd;
	public String getDangerId() {
		return dangerId;
	}
	public void setDangerId(String dangerId) {
		this.dangerId = dangerId;
	}
	public String getDangerName() {
		return dangerName;
	}
	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
	}
	public String getDangerMeasure() {
		return dangerMeasure;
	}
	public void setDangerMeasure(String dangerMeasure) {
		this.dangerMeasure = dangerMeasure;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getIsRunadd() {
		return isRunadd;
	}
	public void setIsRunadd(String isRunadd) {
		this.isRunadd = isRunadd;
	}

}
