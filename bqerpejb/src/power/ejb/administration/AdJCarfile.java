package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJCarfile entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_CARFILE")
public class AdJCarfile implements java.io.Serializable {

	// Fields

	private Long id;
	private String carNo;
	private String carName;
	private String carKind;
	private String carType;
	private String carFrame;
	private String engineNo;
	private Double runMile;
	private Long loadman;
	private Double weight;
	private String equip;
	private String driver;
	private String runLic;
	private String runAllLic;
	private Date buyDate;
	private Double price;
	private String carshop;
	private String invoiceNo;
	private Double oilRate;
	private String isurance;
	private String dep;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String useStatus;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJCarfile() {
	}

	/** minimal constructor */
	public AdJCarfile(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJCarfile(Long id, String carNo, String carName, String carKind,
			String carType, String carFrame, String engineNo, Double runMile,
			Long loadman, Double weight, String equip, String driver,
			String runLic, String runAllLic, Date buyDate, Double price,
			String carshop, String invoiceNo, Double oilRate, String isurance,
			String dep, String isUse, String updateUser, Date updateTime,
			String useStatus, String enterpriseCode) {
		this.id = id;
		this.carNo = carNo;
		this.carName = carName;
		this.carKind = carKind;
		this.carType = carType;
		this.carFrame = carFrame;
		this.engineNo = engineNo;
		this.runMile = runMile;
		this.loadman = loadman;
		this.weight = weight;
		this.equip = equip;
		this.driver = driver;
		this.runLic = runLic;
		this.runAllLic = runAllLic;
		this.buyDate = buyDate;
		this.price = price;
		this.carshop = carshop;
		this.invoiceNo = invoiceNo;
		this.oilRate = oilRate;
		this.isurance = isurance;
		this.dep = dep;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.useStatus = useStatus;
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

	@Column(name = "CAR_NO", length = 10)
	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	@Column(name = "CAR_NAME", length = 40)
	public String getCarName() {
		return this.carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	@Column(name = "CAR_KIND", length = 30)
	public String getCarKind() {
		return this.carKind;
	}

	public void setCarKind(String carKind) {
		this.carKind = carKind;
	}

	@Column(name = "CAR_TYPE", length = 30)
	public String getCarType() {
		return this.carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	@Column(name = "CAR_FRAME", length = 20)
	public String getCarFrame() {
		return this.carFrame;
	}

	public void setCarFrame(String carFrame) {
		this.carFrame = carFrame;
	}

	@Column(name = "ENGINE_NO", length = 20)
	public String getEngineNo() {
		return this.engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	@Column(name = "RUN_MILE", precision = 15)
	public Double getRunMile() {
		return this.runMile;
	}

	public void setRunMile(Double runMile) {
		this.runMile = runMile;
	}

	@Column(name = "LOADMAN", precision = 5, scale = 0)
	public Long getLoadman() {
		return this.loadman;
	}

	public void setLoadman(Long loadman) {
		this.loadman = loadman;
	}

	@Column(name = "WEIGHT", precision = 8)
	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Column(name = "EQUIP", length = 100)
	public String getEquip() {
		return this.equip;
	}

	public void setEquip(String equip) {
		this.equip = equip;
	}

	@Column(name = "DRIVER", length = 6)
	public String getDriver() {
		return this.driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Column(name = "RUN_LIC", length = 20)
	public String getRunLic() {
		return this.runLic;
	}

	public void setRunLic(String runLic) {
		this.runLic = runLic;
	}

	@Column(name = "RUN_ALL_LIC", length = 20)
	public String getRunAllLic() {
		return this.runAllLic;
	}

	public void setRunAllLic(String runAllLic) {
		this.runAllLic = runAllLic;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BUY_DATE", length = 7)
	public Date getBuyDate() {
		return this.buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	@Column(name = "PRICE", precision = 9)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "CARSHOP", length = 100)
	public String getCarshop() {
		return this.carshop;
	}

	public void setCarshop(String carshop) {
		this.carshop = carshop;
	}

	@Column(name = "INVOICE_NO", length = 30)
	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	@Column(name = "OIL_RATE", precision = 4)
	public Double getOilRate() {
		return this.oilRate;
	}

	public void setOilRate(Double oilRate) {
		this.oilRate = oilRate;
	}

	@Column(name = "ISURANCE", length = 30)
	public String getIsurance() {
		return this.isurance;
	}

	public void setIsurance(String isurance) {
		this.isurance = isurance;
	}

	@Column(name = "DEP", length = 6)
	public String getDep() {
		return this.dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "USE_STATUS", length = 1)
	public String getUseStatus() {
		return this.useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}