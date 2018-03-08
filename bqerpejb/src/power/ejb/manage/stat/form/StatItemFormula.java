package power.ejb.manage.stat.form;

@SuppressWarnings("serial")
public class StatItemFormula implements java.io.Serializable {
	private String itemCode;
	private String formulaNo;
	private String rowItemCode;
	private String formulaContent;
	private String fornulaType;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getFormulaNo() {
		return formulaNo;
	}

	public void setFormulaNo(String formulaNo) {
		this.formulaNo = formulaNo;
	}

	public String getRowItemCode() {
		return rowItemCode;
	}

	public void setRowItemCode(String rowItemCode) {
		this.rowItemCode = rowItemCode;
	}

	public String getFormulaContent() {
		return formulaContent;
	}

	public void setFormulaContent(String formulaContent) {
		this.formulaContent = formulaContent;
	}

	public String getFornulaType() {
		return fornulaType;
	}

	public void setFornulaType(String fornulaType) {
		this.fornulaType = fornulaType;
	}
}
