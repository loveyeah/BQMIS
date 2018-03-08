package power.ejb.hr;

@SuppressWarnings("serial")
public class EmpOrderbyMaitenInfo implements java.io.Serializable {
	private Long empId;
	private String chsName;
	private Long orderBy;
	private Long orderByBak;
	private String flag;

	/**
	 * @return the empId
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * @param empId
	 *            the empId to set
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}

	/**
	 * @param chsName
	 *            the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}

	/**
	 * @return the orderBy
	 */
	public Long getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return the orderByBak
	 */
	public Long getOrderByBak() {
		return orderByBak;
	}

	/**
	 * @param orderByBak the orderByBak to set
	 */
	public void setOrderByBak(Long orderByBak) {
		this.orderByBak = orderByBak;
	}
}
