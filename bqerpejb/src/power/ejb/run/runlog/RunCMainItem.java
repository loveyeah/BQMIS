package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 重点事项维护
 * @author 
 *
 */
@Entity
@Table(name = "RUN_C_MAIN_ITEM")
public class RunCMainItem implements java.io.Serializable { 
	private Long itemId;
	private String mainItemCode;
	private String mainItemName;
	private Long diaplayNo;
	private String isUse;
	private String enterpriseCode; 
	public RunCMainItem() {
	}

	/** minimal constructor */
	public RunCMainItem(Long itemId) {
		this.itemId = itemId;
	}

	/** full constructor */
	public RunCMainItem(Long itemId, String mainItemCode, String mainItemName,
			Long diaplayNo, String isUse, String enterpriseCode) {
		this.itemId = itemId;
		this.mainItemCode = mainItemCode;
		this.mainItemName = mainItemName;
		this.diaplayNo = diaplayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "MAIN_ITEM_CODE", length = 10)
	public String getMainItemCode() {
		return this.mainItemCode;
	}

	public void setMainItemCode(String mainItemCode) {
		this.mainItemCode = mainItemCode;
	}

	@Column(name = "MAIN_ITEM_NAME", length = 20)
	public String getMainItemName() {
		return this.mainItemName;
	}

	public void setMainItemName(String mainItemName) {
		this.mainItemName = mainItemName;
	}

	@Column(name = "DIAPLAY_NO", precision = 10, scale = 0)
	public Long getDiaplayNo() {
		return this.diaplayNo;
	}

	public void setDiaplayNo(Long diaplayNo) {
		this.diaplayNo = diaplayNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}