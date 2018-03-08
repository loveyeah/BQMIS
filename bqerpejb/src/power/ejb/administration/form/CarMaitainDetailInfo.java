/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 车辆检修申请单List详细信息
 * @author zhujie 
 */
public class CarMaitainDetailInfo implements Serializable{

	/** 费用预算 */
	private String price = "0";
	/** 实际费用 */
	private String realPrice = "0";
	/** 项目名称 */
	private String proName;
	/** cntRow */
	private String cntRow ="1";
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the realPrice
	 */
	public String getRealPrice() {
		return realPrice;
	}
	/**
	 * @param realPrice the realPrice to set
	 */
	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}
	/**
	 * @return the proName
	 */
	public String getProName() {
		return proName;
	}
	/**
	 * @param proName the proName to set
	 */
	public void setProName(String proName) {
		this.proName = proName;
	}
	/**
	 * @return the cntRow
	 */
	public String getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(String cntRow) {
		this.cntRow = cntRow;
	}
}
