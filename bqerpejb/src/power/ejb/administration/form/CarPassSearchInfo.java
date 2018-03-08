/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.administration.form;

import java.io.Serializable;


/**
 * 车辆进出查询bean
 * @author zhaomingjian
 *
 */
public class CarPassSearchInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**通行证号*/
	private String passCode;
	/**通行证时间*/
	private String passTime;
	/**车牌号*/
	private String car_no;
	/**证件编码*/
	private String paperType_cd;
	/**证件号*/
	private String paper_id;
	/**所在单位*/
	private String firm;
	/**前经办人*/
	private String preMan;
	/**返证时间*/
	private String give_date;
	/**后经办人*/
	private String postMan;
	/**证件名*/
	private String paperType_name;
	/**
	 * 
	 * @return 证件名
	 */
	public String getPaperType_name() {
		return paperType_name;
	}
	public void setPaperType_name(String paperType_name) {
		this.paperType_name = paperType_name;
	}
	/**
	 * 
	 * @return 通行证号
	 */
	public String getPassCode() {
		return passCode;
	}
	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	/**
	 * 
	 * @return 通行证时间
	 */
	public String getPassTime() {
		return passTime;
	}
	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}
	/**
	 * 
	 * @return 车牌号
	 */
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	/**
	 * 
	 * @return 证件编码
	 */
	public String getPaperType_cd() {
		return paperType_cd;
	}
	public void setPaperType_cd(String paperType_cd) {
		this.paperType_cd = paperType_cd;
	}
	/**
	 * 
	 * @return 证件号
	 */
	public String getPaper_id() {
		return paper_id;
	}
	public void setPaper_id(String paper_id) {
		this.paper_id = paper_id;
	}
	/**
	 * 
	 * @return 所在单位
	 */
	public String getFirm() {
		return firm;
	}
	public void setFirm(String firm) {
		this.firm = firm;
	}
	/**
	 * 
	 * @return 前经办人
	 */
	public String getPreMan() {
		return preMan;
	}
	public void setPreMan(String preMan) {
		this.preMan = preMan;
	}
	/**
	 * 
	 * @return 返证时间
	 */
	public String getGive_date() {
		return give_date;
	}
	public void setGive_date(String give_date) {
		this.give_date = give_date;
	}
	/**
	 * 
	 * @return 后经办人
	 */
	public String getPostMan() {
		return postMan;
	}
	public void setPostMan(String postMan) {
		this.postMan = postMan;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	
	
}
