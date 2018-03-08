package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class CbmCItemFormulaForm implements java.io.Serializable {
	private String FormulaId;
	private String ItemId;
	private String FormulaNo;
	private String RowItemCode;
	private String FormulaContent;
	private String fornulaType;
	private String IsUse;

	public String getFormulaId() {
		return FormulaId;
	}

	public void setFormulaId(String formulaId) {
		FormulaId = formulaId;
	}

	public String getFormulaNo() {
		return FormulaNo;
	}

	public void setFormulaNo(String formulaNo) {
		FormulaNo = formulaNo;
	}

	public String getRowItemCode() {
		return RowItemCode;
	}

	public void setRowItemCode(String rowItemCode) {
		RowItemCode = rowItemCode;
	}

	public String getFormulaContent() {
		return FormulaContent;
	}

	public void setFormulaContent(String formulaContent) {
		FormulaContent = formulaContent;
	}

	public String getFornulaType() {
		return fornulaType;
	}

	public void setFornulaType(String fornulaType) {
		this.fornulaType = fornulaType;
	}

	public String getItemId() {
		return ItemId;
	}

	public void setItemId(String itemId) {
		ItemId = itemId;
	}

	public String getIsUse() {
		return IsUse;
	}

	public void setIsUse(String isUse) {
		IsUse = isUse;
	}

}
