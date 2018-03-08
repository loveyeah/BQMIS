package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCPlanJobDept entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_PLAN_JOB_DEPT")
public class BpCPlanJobDept implements java.io.Serializable {

	// Fields

	private Long id;
	private String deptCode;
	private Long orderBy;

	// Constructors

	/** default constructor */
	public BpCPlanJobDept() {
	}

	/** minimal constructor */
	public BpCPlanJobDept(Long id) {
		this.id = id;
	}

	/** full constructor */
	public BpCPlanJobDept(Long id, String deptCode, Long orderBy) {
		this.id = id;
		this.deptCode = deptCode;
		this.orderBy = orderBy;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

}