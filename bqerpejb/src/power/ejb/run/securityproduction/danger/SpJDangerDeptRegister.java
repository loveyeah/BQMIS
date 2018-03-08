package power.ejb.run.securityproduction.danger;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJDangerDeptRegister entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_DANGER_DEPT_REGISTER")
public class SpJDangerDeptRegister implements java.io.Serializable {

	// Fields

	private Long dangerId;//ID
	private String dangerYear;//年度
	private String dangerName;//危险源名称
	private Date finishDate;//完成时间
	private String assessDept;//评估部门
	private String chargeBy;//责任人
	private String memo;//备注
	private Long workFlowNo;//工作流实例号
	private String status;//状态
	private Long orderBy;//排序
	private Double DValue;//D值
	private Double d1Value;//D1值
	private Long valueLevel;//级别
	private String annex;//评估报告附件
	private String lastModifiedBy;//最后修改人
	private Date lastModifiedDate;//最后修改时间
	private String isUse;//是否使用
	private String enterpriseCode;//企业编码

	// Constructors

	/** default constructor */
	public SpJDangerDeptRegister() {
	}

	/** minimal constructor */
	public SpJDangerDeptRegister(Long dangerId) {
		this.dangerId = dangerId;
	}

	/** full constructor */
	public SpJDangerDeptRegister(Long dangerId, String dangerYear,
			String dangerName, Date finishDate, String assessDept,
			String chargeBy, String memo, Long workFlowNo, String status,
			Long orderBy, Double DValue, Double d1Value, Long valueLevel,
			String annex, String lastModifiedBy, Date lastModifiedDate,
			String isUse, String enterpriseCode) {
		this.dangerId = dangerId;
		this.dangerYear = dangerYear;
		this.dangerName = dangerName;
		this.finishDate = finishDate;
		this.assessDept = assessDept;
		this.chargeBy = chargeBy;
		this.memo = memo;
		this.workFlowNo = workFlowNo;
		this.status = status;
		this.orderBy = orderBy;
		this.DValue = DValue;
		this.d1Value = d1Value;
		this.valueLevel = valueLevel;
		this.annex = annex;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DANGER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDangerId() {
		return this.dangerId;
	}

	public void setDangerId(Long dangerId) {
		this.dangerId = dangerId;
	}

	@Column(name = "DANGER_YEAR", length = 4)
	public String getDangerYear() {
		return this.dangerYear;
	}

	public void setDangerYear(String dangerYear) {
		this.dangerYear = dangerYear;
	}

	@Column(name = "DANGER_NAME", length = 100)
	public String getDangerName() {
		return this.dangerName;
	}

	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FINISH_DATE", length = 7)
	public Date getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	@Column(name = "ASSESS_DEPT", length = 20)
	public String getAssessDept() {
		return this.assessDept;
	}

	public void setAssessDept(String assessDept) {
		this.assessDept = assessDept;
	}

	@Column(name = "CHARGE_BY", length = 30)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "D_VALUE", precision = 15, scale = 4)
	public Double getDValue() {
		return this.DValue;
	}

	public void setDValue(Double DValue) {
		this.DValue = DValue;
	}

	@Column(name = "D1_VALUE", precision = 15, scale = 4)
	public Double getD1Value() {
		return this.d1Value;
	}

	public void setD1Value(Double d1Value) {
		this.d1Value = d1Value;
	}

	@Column(name = "VALUE_LEVEL", precision = 10, scale = 0)
	public Long getValueLevel() {
		return this.valueLevel;
	}

	public void setValueLevel(Long valueLevel) {
		this.valueLevel = valueLevel;
	}

	@Column(name = "ANNEX", length = 200)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 20)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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