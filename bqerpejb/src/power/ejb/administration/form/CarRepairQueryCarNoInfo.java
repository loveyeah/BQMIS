/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * @author chaihao
 * 
 */
public class CarRepairQueryCarNoInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 车牌号 */
	private String carNo;

	/**
	 * @return 车牌号
	 */
	public String getCarNo() {
		return carNo;
	}

	/**
	 * @param 车牌号
	 *            设置车牌号
	 */
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
}
