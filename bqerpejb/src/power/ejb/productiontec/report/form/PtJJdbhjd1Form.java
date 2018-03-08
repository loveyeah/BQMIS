package power.ejb.productiontec.report.form;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class PtJJdbhjd1Form implements Serializable
{
	private Long jdbhjd1Id;
	private String strMonth;
	private String countType;
	private Double dynamoNum;
	private Double transformerNum;
	private Double fbzProtectNum;
	private Double factoryProctectNum;
	private Double blockNum;
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
	
	public PtJJdbhjd1Form()
	{
		this.jdbhjd1Id = null;
		this.strMonth = null;
		this.countType= "";
		this.dynamoNum = 0.0;
		this.transformerNum = 0.0;
		this.fbzProtectNum = 0.0;
		this.factoryProctectNum = 0.0;
		this.blockNum = 0.0;
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
	public Long getJdbhjd1Id() {
		return jdbhjd1Id;
	}
	public void setJdbhjd1Id(Long jdbhjd1Id) {
		this.jdbhjd1Id = jdbhjd1Id;
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
	public Double getDynamoNum() {
		return dynamoNum;
	}
	public void setDynamoNum(Double dynamoNum) {
		this.dynamoNum = dynamoNum;
	}
	public Double getTransformerNum() {
		return transformerNum;
	}
	public void setTransformerNum(Double transformerNum) {
		this.transformerNum = transformerNum;
	}
	public Double getFbzProtectNum() {
		return fbzProtectNum;
	}
	public void setFbzProtectNum(Double fbzProtectNum) {
		this.fbzProtectNum = fbzProtectNum;
	}
	public Double getFactoryProctectNum() {
		return factoryProctectNum;
	}
	public void setFactoryProctectNum(Double factoryProctectNum) {
		this.factoryProctectNum = factoryProctectNum;
	}
	public Double getBlockNum() {
		return blockNum;
	}
	public void setBlockNum(Double blockNum) {
		this.blockNum = blockNum;
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