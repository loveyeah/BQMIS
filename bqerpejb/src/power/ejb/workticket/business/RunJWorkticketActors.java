package power.ejb.workticket.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJWorkticketActors entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_ACTORS")
public class RunJWorkticketActors implements java.io.Serializable {

	// Fields

	private Long id;
	private String workticketNo;
	private Long actorType;
	private String actorCode;
	private String actorName;
	private String actorDept;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunJWorkticketActors() {
	}

	/** minimal constructor */
	public RunJWorkticketActors(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJWorkticketActors(Long id, String workticketNo, Long actorType,
			String actorCode, String actorName, String actorDept,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.workticketNo = workticketNo;
		this.actorType = actorType;
		this.actorCode = actorCode;
		this.actorName = actorName;
		this.actorDept = actorDept;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
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

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "ACTOR_TYPE", precision = 10, scale = 0)
	public Long getActorType() {
		return this.actorType;
	}

	public void setActorType(Long actorType) {
		this.actorType = actorType;
	}

	@Column(name = "ACTOR_CODE", length = 30)
	public String getActorCode() {
		return this.actorCode;
	}

	public void setActorCode(String actorCode) {
		this.actorCode = actorCode;
	}

	@Column(name = "ACTOR_NAME", length = 30)
	public String getActorName() {
		return this.actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	@Column(name = "ACTOR_DEPT", length = 20)
	public String getActorDept() {
		return this.actorDept;
	}

	public void setActorDept(String actorDept) {
		this.actorDept = actorDept;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}