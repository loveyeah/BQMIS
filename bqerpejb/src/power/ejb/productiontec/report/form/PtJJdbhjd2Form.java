package power.ejb.productiontec.report.form;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class PtJJdbhjd2Form implements Serializable
{
	private Long jdbhjd2Id;
	private String strMonth;
	private String countType;
	private Double kv35Num;
	private Double kv110Num;
	private Double kv220Num;
	private Double kv330Num;
	private Double mcProtectNum;
	private Double safeDeviceNum;
	private Double otherNum;
	private String engineer;
	private String equDept;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String tableFlag;
	// 类别名称
	private String typeName;
	//总工程师name
	private String engineerName;
	// 设备部name
	private String equDeptName;
	// 填表人name
	private String entryByName;
	//填表时间string
	private String entryDateString;
	
	public PtJJdbhjd2Form()
	{
		this.jdbhjd2Id = null;
		this.strMonth = null;
		this.countType= "";
		this.kv35Num = 0.0;
		this.kv110Num = 0.0;
		this.kv220Num = 0.0;
		this.kv330Num = 0.0;
		this.mcProtectNum = 0.0;
		this.safeDeviceNum = 0.0;
		this.otherNum = 0.0;
		this.engineer = "";
		this.equDept = "";
		this.entryBy = "";
		this.entryDate = null;
		this.enterpriseCode = "";
		this.tableFlag = "";
		this.typeName = "";
		this.engineerName = "";
		this.equDeptName = "";
		this.entryByName = "";
		this.entryDateString = null;
	}
	public Long getJdbhjd2Id() {
		return jdbhjd2Id;
	}
	public void setJdbhjd2Id(Long jdbhjd2Id) {
		this.jdbhjd2Id = jdbhjd2Id;
	}
	public String getStrMonth() {
		return strMonth;
	}
	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
	}
	public Double getKv35Num() {
		return kv35Num;
	}
	public void setKv35Num(Double kv35Num) {
		this.kv35Num = kv35Num;
	}
	public Double getKv110Num() {
		return kv110Num;
	}
	public void setKv110Num(Double kv110Num) {
		this.kv110Num = kv110Num;
	}
	public Double getKv220Num() {
		return kv220Num;
	}
	public void setKv220Num(Double kv220Num) {
		this.kv220Num = kv220Num;
	}
	public Double getKv330Num() {
		return kv330Num;
	}
	public void setKv330Num(Double kv330Num) {
		this.kv330Num = kv330Num;
	}
	public Double getMcProtectNum() {
		return mcProtectNum;
	}
	public void setMcProtectNum(Double mcProtectNum) {
		this.mcProtectNum = mcProtectNum;
	}
	public Double getSafeDeviceNum() {
		return safeDeviceNum;
	}
	public void setSafeDeviceNum(Double safeDeviceNum) {
		this.safeDeviceNum = safeDeviceNum;
	}
	public Double getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(Double otherNum) {
		this.otherNum = otherNum;
	}
	public String getEngineer() {
		return engineer;
	}
	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}
	public String getEquDept() {
		return equDept;
	}
	public void setEquDept(String equDept) {
		this.equDept = equDept;
	}
	public String getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getTableFlag() {
		return tableFlag;
	}
	public void setTableFlag(String tableFlag) {
		this.tableFlag = tableFlag;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getEngineerName() {
		return engineerName;
	}
	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}
	public String getEquDeptName() {
		return equDeptName;
	}
	public void setEquDeptName(String equDeptName) {
		this.equDeptName = equDeptName;
	}
	public String getEntryByName() {
		return entryByName;
	}
	public void setEntryByName(String entryByName) {
		this.entryByName = entryByName;
	}
	public String getEntryDateString() {
		return entryDateString;
	}
	public void setEntryDateString(String entryDateString) {
		this.entryDateString = entryDateString;
	}
}