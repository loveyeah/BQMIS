package power.ejb.productiontec.report;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJJdbhjd1 entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_JDBHJD1", schema = "POWER")
public class PtJJdbhjd1 implements java.io.Serializable {

	// Fields

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

	// Constructors

	/** default constructor */
	public PtJJdbhjd1() {
	}

	/** minimal constructor */
	public PtJJdbhjd1(Long jdbhjd1Id) {
		this.jdbhjd1Id = jdbhjd1Id;
	}

	/** full constructor */
	public PtJJdbhjd1(Long jdbhjd1Id, String strMonth, String countType,
			Double dynamoNum, Double transformerNum, Double fbzProtectNum,
			Double factoryProctectNum, Double blockNum, String engineer,
			String equDept, String entryBy, Date entryDate,
			String enterpriseCode, String tableFlag) {
		this.jdbhjd1Id = jdbhjd1Id;
		this.strMonth = strMonth;
		this.countType = countType;
		this.dynamoNum = dynamoNum;
		this.transformerNum = transformerNum;
		this.fbzProtectNum = fbzProtectNum;
		this.factoryProctectNum = factoryProctectNum;
		this.blockNum = blockNum;
		this.engineer = engineer;
		this.equDept = equDept;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.tableFlag = tableFlag;
	}

	// Property accessors
	@Id
	@Column(name = "JDBHJD1_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdbhjd1Id() {
		return this.jdbhjd1Id;
	}

	public void setJdbhjd1Id(Long jdbhjd1Id) {
		this.jdbhjd1Id = jdbhjd1Id;
	}

	@Column(name = "STR_MONTH", length = 10)
	public String getStrMonth() {
		return this.strMonth;
	}

	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}

	@Column(name = "COUNT_TYPE", length = 1)
	public String getCountType() {
		return this.countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	@Column(name = "DYNAMO_NUM", precision = 10, scale = 4)
	public Double getDynamoNum() {
		return this.dynamoNum;
	}

	public void setDynamoNum(Double dynamoNum) {
		this.dynamoNum = dynamoNum;
	}

	@Column(name = "TRANSFORMER_NUM", precision = 10, scale = 4)
	public Double getTransformerNum() {
		return this.transformerNum;
	}

	public void setTransformerNum(Double transformerNum) {
		this.transformerNum = transformerNum;
	}

	@Column(name = "FBZ_PROTECT_NUM", precision = 10, scale = 4)
	public Double getFbzProtectNum() {
		return this.fbzProtectNum;
	}

	public void setFbzProtectNum(Double fbzProtectNum) {
		this.fbzProtectNum = fbzProtectNum;
	}

	@Column(name = "FACTORY_PROCTECT_NUM", precision = 10, scale = 4)
	public Double getFactoryProctectNum() {
		return this.factoryProctectNum;
	}

	public void setFactoryProctectNum(Double factoryProctectNum) {
		this.factoryProctectNum = factoryProctectNum;
	}

	@Column(name = "BLOCK_NUM", precision = 10, scale = 4)
	public Double getBlockNum() {
		return this.blockNum;
	}

	public void setBlockNum(Double blockNum) {
		this.blockNum = blockNum;
	}

	@Column(name = "ENGINEER", length = 30)
	public String getEngineer() {
		return this.engineer;
	}

	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}

	@Column(name = "EQU_DEPT", length = 30)
	public String getEquDept() {
		return this.equDept;
	}

	public void setEquDept(String equDept) {
		this.equDept = equDept;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "TABLE_FLAG", length = 1)
	public String getTableFlag() {
		return this.tableFlag;
	}

	public void setTableFlag(String tableFlag) {
		this.tableFlag = tableFlag;
	}

}