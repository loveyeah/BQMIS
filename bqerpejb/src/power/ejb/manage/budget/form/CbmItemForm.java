package power.ejb.manage.budget.form;

import power.ejb.manage.budget.CbmCItem;

@SuppressWarnings("serial")
public class CbmItemForm {
	private CbmCItem item;
	private Long zbbmtxId;
	private String zbbmtxCode;
	private String zbbmtxName;
	private String isItem;
	// 财务科目名称
	private String finaceName;
	private Long finaceId;
	// 借方贷方
	private String debitCredit;
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDebitCredit() {
		return debitCredit;
	}

	public void setDebitCredit(String debitCredit) {
		this.debitCredit = debitCredit;
	}

	public CbmCItem getItem() {
		return item;
	}

	public void setItem(CbmCItem item) {
		this.item = item;
	}

	public Long getZbbmtxId() {
		return zbbmtxId;
	}

	public void setZbbmtxId(Long zbbmtxId) {
		this.zbbmtxId = zbbmtxId;
	}

	public String getZbbmtxCode() {
		return zbbmtxCode;
	}

	public void setZbbmtxCode(String zbbmtxCode) {
		this.zbbmtxCode = zbbmtxCode;
	}

	public String getZbbmtxName() {
		return zbbmtxName;
	}

	public void setZbbmtxName(String zbbmtxName) {
		this.zbbmtxName = zbbmtxName;
	}

	public String getIsItem() {
		return isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	public String getFinaceName() {
		return finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}

	public Long getFinaceId() {
		return finaceId;
	}

	public void setFinaceId(Long finaceId) {
		this.finaceId = finaceId;
	}
}
