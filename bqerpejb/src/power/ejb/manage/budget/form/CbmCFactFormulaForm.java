package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class CbmCFactFormulaForm implements java.io.Serializable {
	private String FactFormulaId;
	private String ItemId;
	private String FormulaNo;
	private String RowItemCode;
	private String FormulaContent;
	private String FornulaType;
	private String IsUse;

	public String getFactFormulaId() {
		return FactFormulaId;
	}

	public void setFactFormulaId(String factFormulaId) {
		FactFormulaId = factFormulaId;
	}

	public String getItemId() {
		return ItemId;
	}

	public void setItemId(String itemId) {
		ItemId = itemId;
	}

	public String getFormulaNo() {
		return FormulaNo;
	}

	public void setFormulaNo(String formulaNo) {
		FormulaNo = formulaNo;
	}

	public String getFormulaContent() {
		return FormulaContent;
	}

	public void setFormulaContent(String formulaContent) {
		FormulaContent = formulaContent;
	}

	public String getFornulaType() {
		return FornulaType;
	}

	public void setFornulaType(String fornulaType) {
		FornulaType = fornulaType;
	}

	public String getIsUse() {
		return IsUse;
	}

	public void setIsUse(String isUse) {
		IsUse = isUse;
	}

	public String getRowItemCode() {
		return RowItemCode;
	}

	public void setRowItemCode(String rowItemCode) {
		RowItemCode = rowItemCode;
	}

}
