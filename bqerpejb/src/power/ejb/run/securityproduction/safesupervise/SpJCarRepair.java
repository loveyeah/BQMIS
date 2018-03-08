package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJCarRepair entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_CAR_REPAIR")
public class SpJCarRepair implements java.io.Serializable {

	// Fields

	private Long repairId;
	private Long carId;
	private Double nowKmNum;
	private String sendPerson;
	private Date repairDate;
	private String repairContend;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJCarRepair() {
	}

	/** minimal constructor */
	public SpJCarRepair(Long repairId) {
		this.repairId = repairId;
	}

	/** full constructor */
	public SpJCarRepair(Long repairId, Long carId, Double nowKmNum,
			String sendPerson, Date repairDate, String repairContend,
			String isUse, String enterpriseCode) {
		this.repairId = repairId;
		this.carId = carId;
		this.nowKmNum = nowKmNum;
		this.sendPerson = sendPerson;
		this.repairDate = repairDate;
		this.repairContend = repairContend;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPAIR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRepairId() {
		return this.repairId;
	}

	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	@Column(name = "CAR_ID", precision = 10, scale = 0)
	public Long getCarId() {
		return this.carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	@Column(name = "NOW_KM_NUM", precision = 15, scale = 4)
	public Double getNowKmNum() {
		return this.nowKmNum;
	}

	public void setNowKmNum(Double nowKmNum) {
		this.nowKmNum = nowKmNum;
	}

	@Column(name = "SEND_PERSON", length = 20)
	public String getSendPerson() {
		return this.sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPAIR_DATE", length = 7)
	public Date getRepairDate() {
		return this.repairDate;
	}

	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}

	@Column(name = "REPAIR_CONTEND", length = 2000)
	public String getRepairContend() {
		return this.repairContend;
	}

	public void setRepairContend(String repairContend) {
		this.repairContend = repairContend;
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