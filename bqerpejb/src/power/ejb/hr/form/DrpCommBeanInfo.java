/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.form;
/**
 * 业务共通dropdownlist store bean
 * @author qzhang
 *
 */
public class DrpCommBeanInfo implements java.io.Serializable{
	/** 流水号*/
	private Long id;
	/** 显示值*/
	private String text;
	/**
	 * 获取流水号
	 * @return id 流水号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置流水号
	 * @param id 流水号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取流水号
	 * @return text 流水号
	 */
	public String getText() {
		return text;
	}
	/**
	 * 设置流水号
	 * @param text 流水号
	 */
	public void setText(String text) {
		this.text = text;
	}
}
