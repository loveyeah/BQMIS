package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJWorktickets entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_WORKTICKETS")
public class EquJWorktickets implements java.io.Serializable {

	// Fields

	private Long reId;
	private String woCode;
	private String operationStep;
	private String woticketCode;

	// Constructors

	/** default constructor */
	public EquJWorktickets() {
	}

	/** minimal constructor */
	public EquJWorktickets(Long reId, String woCode, String operationStep) {
		this.reId = reId;
		this.woCode = woCode;
		this.operationStep = operationStep;
	}

	/** full constructor */
	public EquJWorktickets(Long reId, String woCode, String operationStep,
			String woticketCode) {
		this.reId = reId;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.woticketCode = woticketCode;
	}

	// Property accessors
	@Id
	@Column(name = "RE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getReId() {
		return this.reId;
	}

	public void setReId(Long reId) {
		this.reId = reId;
	}

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "OPERATION_STEP", nullable = false, length = 20)
	public String getOperationStep() {
		return this.operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

	@Column(name = "WOTICKET_CODE", length = 30)
	public String getWoticketCode() {
		return this.woticketCode;
	}

	public void setWoticketCode(String woticketCode) {
		this.woticketCode = woticketCode;
	}

}