package power.ejb.manage.plan.trainplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name = "BP_C_TRAINING_TYPE")
public class BpCTrainingType implements java.io.Serializable {

	// Fields

	private Long trainingTypeId;
	private String trainingTypeName;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCTrainingType() {
	}

	/** minimal constructor */
	public BpCTrainingType(Long trainingTypeId) {
		this.trainingTypeId = trainingTypeId;
	} 

	// Property accessors
	@Id
	@Column(name = "TRAINING_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainingTypeId() {
		return this.trainingTypeId;
	}

	public void setTrainingTypeId(Long trainingTypeId) {
		this.trainingTypeId = trainingTypeId;
	}

	@Column(name = "TRAINING_TYPE_NAME", length = 20)
	public String getTrainingTypeName() {
		return this.trainingTypeName;
	}

	public void setTrainingTypeName(String trainingTypeName) {
		this.trainingTypeName = trainingTypeName;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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