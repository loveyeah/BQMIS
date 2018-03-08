package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class ModelFormulaForm implements java.io.Serializable {
	private String modelFormulaId;
	private String modelItemId;
	private String formulaNo;
	private String formulaContent;
	private String fornulaType;
	private String rowItemCode;

	public String getModelFormulaId() {
		return modelFormulaId;
	}

	public void setModelFormulaId(String modelFormulaId) {
		this.modelFormulaId = modelFormulaId;
	}

	public String getModelItemId() {
		return modelItemId;
	}

	public void setModelItemId(String modelItemId) {
		this.modelItemId = modelItemId;
	}

	public String getFormulaNo() {
		return formulaNo;
	}

	public void setFormulaNo(String formulaNo) {
		this.formulaNo = formulaNo;
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

	public String getRowItemCode() {
		return rowItemCode;
	}

	public void setRowItemCode(String rowItemCode) {
		this.rowItemCode = rowItemCode;
	}
}
