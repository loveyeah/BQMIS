/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.util.List;

import power.ejb.resource.ReceiveGoodsListBean;

/**
* 定期工作安排表表头Bean
* @author zhaozhijie
*
*/
public class FixedJobArrangeBean {

	/** 工作类别 */
	private String workTypeCode;
    /** 定期工作安排表明细 */
    private List<FixedJobArrangeListBean> fixedJobArrangeList;


	/**
	 * @return the workTypeCode
	 */
	public String getWorkTypeCode() {
		return workTypeCode;
	}
	/**
	 * @param workTypeCode the workTypeCode to set
	 */
	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}
	/**
	 * @return the fixedJobArrangeList
	 */
	public List<FixedJobArrangeListBean> getFixedJobArrangeList() {
		return fixedJobArrangeList;
	}
	/**
	 * @param fixedJobArrangeList the fixedJobArrangeList to set
	 */
	public void setFixedJobArrangeList(
			List<FixedJobArrangeListBean> fixedJobArrangeList) {
		this.fixedJobArrangeList = fixedJobArrangeList;
	}
}
