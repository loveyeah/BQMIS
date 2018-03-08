package power.ejb.manage.stat.form;

@SuppressWarnings("serial")
public class ReportItemFormula implements java.io.Serializable {
	private String relationCode;
	private String formulaNo;
	private String formulaContent;
	private String fornulaType;

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

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

}
