package power.ejb.productiontec.report;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJJdbhjd2 entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_JDBHJD2", schema = "POWER")
public class PtJJdbhjd2 implements java.io.Serializable {

	// Fields

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

	// Constructors

	/** default constructor */
	public PtJJdbhjd2() {
	}

	/** minimal constructor */
	public PtJJdbhjd2(Long jdbhjd2Id) {
		this.jdbhjd2Id = jdbhjd2Id;
	}

	/** full constructor */
	public PtJJdbhjd2(Long jdbhjd2Id, String strMonth, String countType,
			Double kv35Num, Double kv110Num, Double kv220Num, Double kv330Num,
			Double mcProtectNum, Double safeDeviceNum, Double otherNum,
			String engineer, String equDept, String entryBy, Date entryDate,
			String enterpriseCode, String tableFlag) {
		this.jdbhjd2Id = jdbhjd2Id;
		this.strMonth = strMonth;
		this.countType = countType;
		this.kv35Num = kv35Num;
		this.kv110Num = kv110Num;
		this.kv220Num = kv220Num;
		this.kv330Num = kv330Num;
		this.mcProtectNum = mcProtectNum;
		this.safeDeviceNum = safeDeviceNum;
		this.otherNum = otherNum;
		this.engineer = engineer;
		this.equDept = equDept;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.tableFlag = tableFlag;
	}

	// Property accessors
	@Id
	@Column(name = "JDBHJD2_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdbhjd2Id() {
		return this.jdbhjd2Id;
	}

	public void setJdbhjd2Id(Long jdbhjd2Id) {
		this.jdbhjd2Id = jdbhjd2Id;
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

	@Column(name = "KV35_NUM", precision = 10, scale = 4)
	public Double getKv35Num() {
		return this.kv35Num;
	}

	public void setKv35Num(Double kv35Num) {
		this.kv35Num = kv35Num;
	}

	@Column(name = "KV110_NUM", precision = 10, scale = 4)
	public Double getKv110Num() {
		return this.kv110Num;
	}

	public void setKv110Num(Double kv110Num) {
		this.kv110Num = kv110Num;
	}

	@Column(name = "KV220_NUM", precision = 10, scale = 4)
	public Double getKv220Num() {
		return this.kv220Num;
	}

	public void setKv220Num(Double kv220Num) {
		this.kv220Num = kv220Num;
	}

	@Column(name = "KV330_NUM", precision = 10, scale = 4)
	public Double getKv330Num() {
		return this.kv330Num;
	}

	public void setKv330Num(Double kv330Num) {
		this.kv330Num = kv330Num;
	}

	@Column(name = "MC_PROTECT_NUM", precision = 10, scale = 4)
	public Double getMcProtectNum() {
		return this.mcProtectNum;
	}

	public void setMcProtectNum(Double mcProtectNum) {
		this.mcProtectNum = mcProtectNum;
	}

	@Column(name = "SAFE_DEVICE_NUM", precision = 10, scale = 4)
	public Double getSafeDeviceNum() {
		return this.safeDeviceNum;
	}

	public void setSafeDeviceNum(Double safeDeviceNum) {
		this.safeDeviceNum = safeDeviceNum;
	}

	@Column(name = "OTHER_NUM", precision = 10, scale = 4)
	public Double getOtherNum() {
		return this.otherNum;
	}

	public void setOtherNum(Double otherNum) {
		this.otherNum = otherNum;
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