package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCRealatequout implements java.io.Serializable {

	private EquCRealatequ realatequInfo;
	private String equname;

	/**
	 * @return the realatequInfo
	 */
	public EquCRealatequ getRealatequInfo() {
		return realatequInfo;
	}

	/**
	 * @param realatequInfo
	 *            the realatequInfo to set
	 */
	public void setRealatequInfo(EquCRealatequ realatequInfo) {
		this.realatequInfo = realatequInfo;
	}

	/**
	 * @return the equname
	 */
	public String getEquname() {
		return equname;
	}

	/**
	 * @param equname
	 *            the equname to set
	 */
	public void setEquname(String equname) {
		this.equname = equname;
	}

}