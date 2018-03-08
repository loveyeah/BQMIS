package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCCarfile entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_C_CARFILE", schema = "POWER")
public class SpCCarfile implements java.io.Serializable {

	// Fields

	private Long carId;
	private String carNo;
	private String belongTo;
	private String rightCode;
	private String factoryType;
	private String carType;
	private String seeSize;
	private String carColor;
	private String fuelType;
	private String tireType;
	private String wheelbase;
	private Long passergerCapacity;
	private String inOut;
	private Date outFactoryDate;
	private Date firstRegisterDate;
	private String engineCode;
	private String discernCode;
	private String supplier;
	private String lastModifiedBy;
	private Date lastModifiedTime;
	private String isUse;
	private String enterpriseCode;
	private String deptCode;

	// Constructors

	/** default constructor */
	public SpCCarfile() {
	}

	/** minimal constructor */
	public SpCCarfile(Long carId) {
		this.carId = carId;
	}

	/** full constructor */
	public SpCCarfile(Long carId, String carNo, String belongTo,
			String rightCode, String factoryType, String carType,
			String seeSize, String carColor, String fuelType, String tireType,
			String wheelbase, Long passergerCapacity, String inOut,
			Date outFactoryDate, Date firstRegisterDate, String engineCode,
			String discernCode, String supplier, String lastModifiedBy,
			Date lastModifiedTime, String isUse, String enterpriseCode,
			String deptCode) {
		this.carId = carId;
		this.carNo = carNo;
		this.belongTo = belongTo;
		this.rightCode = rightCode;
		this.factoryType = factoryType;
		this.carType = carType;
		this.seeSize = seeSize;
		this.carColor = carColor;
		this.fuelType = fuelType;
		this.tireType = tireType;
		this.wheelbase = wheelbase;
		this.passergerCapacity = passergerCapacity;
		this.inOut = inOut;
		this.outFactoryDate = outFactoryDate;
		this.firstRegisterDate = firstRegisterDate;
		this.engineCode = engineCode;
		this.discernCode = discernCode;
		this.supplier = supplier;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedTime = lastModifiedTime;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.deptCode = deptCode;
	}

	// Property accessors
	@Id
	@Column(name = "CAR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCarId() {
		return this.carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	@Column(name = "CAR_NO", length = 10)
	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	@Column(name = "BELONG_TO", length = 80)
	public String getBelongTo() {
		return this.belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	@Column(name = "RIGHT_CODE", length = 30)
	public String getRightCode() {
		return this.rightCode;
	}

	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
	}

	@Column(name = "FACTORY_TYPE", length = 30)
	public String getFactoryType() {
		return this.factoryType;
	}

	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}

	@Column(name = "CAR_TYPE", length = 30)
	public String getCarType() {
		return this.carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	@Column(name = "SEE_SIZE", length = 30)
	public String getSeeSize() {
		return this.seeSize;
	}

	public void setSeeSize(String seeSize) {
		this.seeSize = seeSize;
	}

	@Column(name = "CAR_COLOR", length = 30)
	public String getCarColor() {
		return this.carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	@Column(name = "FUEL_TYPE", length = 30)
	public String getFuelType() {
		return this.fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	@Column(name = "TIRE_TYPE", length = 30)
	public String getTireType() {
		return this.tireType;
	}

	public void setTireType(String tireType) {
		this.tireType = tireType;
	}

	@Column(name = "WHEELBASE", length = 30)
	public String getWheelbase() {
		return this.wheelbase;
	}

	public void setWheelbase(String wheelbase) {
		this.wheelbase = wheelbase;
	}

	@Column(name = "PASSERGER_CAPACITY", precision = 4, scale = 0)
	public Long getPassergerCapacity() {
		return this.passergerCapacity;
	}

	public void setPassergerCapacity(Long passergerCapacity) {
		this.passergerCapacity = passergerCapacity;
	}

	@Column(name = "IN_OUT", length = 1)
	public String getInOut() {
		return this.inOut;
	}

	public void setInOut(String inOut) {
		this.inOut = inOut;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OUT_FACTORY_DATE", length = 7)
	public Date getOutFactoryDate() {
		return this.outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FIRST_REGISTER_DATE", length = 7)
	public Date getFirstRegisterDate() {
		return this.firstRegisterDate;
	}

	public void setFirstRegisterDate(Date firstRegisterDate) {
		this.firstRegisterDate = firstRegisterDate;
	}

	@Column(name = "ENGINE_CODE", length = 30)
	public String getEngineCode() {
		return this.engineCode;
	}

	public void setEngineCode(String engineCode) {
		this.engineCode = engineCode;
	}

	@Column(name = "DISCERN_CODE", length = 30)
	public String getDiscernCode() {
		return this.discernCode;
	}

	public void setDiscernCode(String discernCode) {
		this.discernCode = discernCode;
	}

	@Column(name = "SUPPLIER", length = 80)
	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 20)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_TIME", length = 7)
	public Date getLastModifiedTime() {
		return this.lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
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

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

}