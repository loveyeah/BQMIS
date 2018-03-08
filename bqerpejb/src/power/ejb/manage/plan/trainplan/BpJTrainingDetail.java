package power.ejb.manage.plan.trainplan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BP_J_TRAINING_DETAIL")
public class BpJTrainingDetail implements java.io.Serializable {

	// Fields

	private Long trainingDetailId;
	private Long trainingMainId;
	private Long trainingTypeId;
	private String trainingName;
	private Long trainingLevel;
	private Long trainingNumber;
	private Double trainingHours;
	private String chargeBy;
	private String fillBy;
	private Date fillDate;
	private String isUse;
	private String enterpriseCode;

	//add by drdu 091210
	private Long finishNumber;

	// Constructors

	/** default constructor */
	public BpJTrainingDetail() {
	}

	/** minimal constructor */
	public BpJTrainingDetail(Long trainingDetailId) {
		this.trainingDetailId = trainingDetailId;
	}

	// Property accessors
	@Id
	@Column(name = "TRAINING_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainingDetailId() {
		return this.trainingDetailId;
	}

	public void setTrainingDetailId(Long trainingDetailId) {
		this.trainingDetailId = trainingDetailId;
	}

	@Column(name = "TRAINING_MAIN_ID", precision = 10, scale = 0)
	public Long getTrainingMainId() {
		return this.trainingMainId;
	}

	public void setTrainingMainId(Long trainingMainId) {
		this.trainingMainId = trainingMainId;
	}

	@Column(name = "TRAINING_TYPE_ID", precision = 10, scale = 0)
	public Long getTrainingTypeId() {
		return this.trainingTypeId;
	}

	public void setTrainingTypeId(Long trainingTypeId) {
		this.trainingTypeId = trainingTypeId;
	}

	@Column(name = "TRAINING_NAME", length = 100)
	public String getTrainingName() {
		return this.trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	@Column(name = "TRAINING_LEVEL", precision = 1, scale = 0)
	public Long getTrainingLevel() {
		return this.trainingLevel;
	}

	public void setTrainingLevel(Long trainingLevel) {
		this.trainingLevel = trainingLevel;
	}
	
	@Column(name = "TRAINING_NUMBER", precision = 5, scale = 0)
	public Long getTrainingNumber() {
		return this.trainingNumber;
	}

	public void setTrainingNumber(Long trainingNumber) {
		this.trainingNumber = trainingNumber;
	}

	@Column(name = "TRAINING_HOURS", precision = 10)
	public Double getTrainingHours() {
		return this.trainingHours;
	}

	public void setTrainingHours(Double trainingHours) {
		this.trainingHours = trainingHours;
	}

	@Column(name = "CHARGE_BY", length = 50)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
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
	
	@Column(name = "FINISH_NUMBER", precision = 5, scale = 0)
	public Long getFinishNumber() {
		return finishNumber;
	}

	public void setFinishNumber(Long finishNumber) {
		this.finishNumber = finishNumber;
	}

}