package power.ejb.administration.comm;

/**
 * AdJCarfile entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class ComAdJCarfile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	/** 车牌号 */
	private String carNo;
	/** 司机编码 */
	private String driver;
	/** 司机名 */
	private String driverName;
	/** ID */
	private String id;
	/** 车名 */
	private String carName;
	/** 车种 */
	private String carKind;
	/** 车型 */
	private String carType;
	/** 行车里程 */
	private Double runMile;
	/** 更新时间 */
	private Long updateTime;

	// Constructors

	/** default constructor */
	public ComAdJCarfile() {
	}

	/**
	 * @return 车牌号
	 */
	public String getCarNo() {
		return carNo;
	}

	/**
	 * @param 车牌号
	 */
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	/**
	 * @return 车名
	 */
	public String getCarName() {
		return carName;
	}

	/**
	 * @param 车名
	 */
	public void setCarName(String carName) {
		this.carName = carName;
	}

	/**
	 * @return 车种
	 */
	public String getCarKind() {
		return carKind;
	}

	/**
	 * @param 车种
	 */
	public void setCarKind(String carKind) {
		this.carKind = carKind;
	}

	/**
	 * @return 车型
	 */
	public String getCarType() {
		return carType;
	}

	/**
	 * @param 车型
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}

	/**
	 * @return 行车里程
	 */
	public Double getRunMile() {
		return runMile;
	}

	/**
	 * @param 行车里程
	 */
	public void setRunMile(Double runMile) {
		this.runMile = runMile;
	}

	/**
	 * @return 司机编码
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param 司机编码
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 更新时间
	 */
	public Long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param 更新时间
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the 司机名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param 司机名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
}