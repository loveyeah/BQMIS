package power.ejb.run.securityproduction.danger;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCDangerAssess entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_C_DANGER_ASSESS")
public class SpCDangerAssess implements java.io.Serializable {

	// Fields

	private Long assessId;//id
	private String fileName;//名称
	private String assessSort;//类别
	private String annex;//附件
	private String entryBy;//上传人
	private Date entryDate;//上传时间
	private String isUse;//是否使用
	private String enterpriseCode;//企业编码

	// Constructors

	/** default constructor */
	public SpCDangerAssess() {
	}

	/** minimal constructor */
	public SpCDangerAssess(Long assessId) {
		this.assessId = assessId;
	}

	/** full constructor */
	public SpCDangerAssess(Long assessId, String fileName, String assessSort,
			String annex, String entryBy, Date entryDate, String isUse,
			String enterpriseCode) {
		this.assessId = assessId;
		this.fileName = fileName;
		this.assessSort = assessSort;
		this.annex = annex;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ASSESS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAssessId() {
		return this.assessId;
	}

	public void setAssessId(Long assessId) {
		this.assessId = assessId;
	}

	@Column(name = "FILE_NAME", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "ASSESS_SORT", length = 1)
	public String getAssessSort() {
		return this.assessSort;
	}

	public void setAssessSort(String assessSort) {
		this.assessSort = assessSort;
	}

	@Column(name = "ANNEX", length = 200)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}