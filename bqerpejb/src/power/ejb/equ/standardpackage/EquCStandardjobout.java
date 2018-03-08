package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCStandardjobout implements java.io.Serializable {

	private EquCStandardjob standardInfo;
	private String specialityName;

	/**
	 * @return the standardInfo
	 */
	public EquCStandardjob getStandardInfo() {
		return standardInfo;
	}

	/**
	 * @param standardInfo
	 *            the standardInfo to set
	 */
	public void setStandardInfo(EquCStandardjob standardInfo) {
		this.standardInfo = standardInfo;
	}

	/**
	 * @return the specialitynameString
	 */
	public String getSpecialityName() {
		return specialityName;
	}

	/**
	 * @param specialitynameString
	 *            the specialitynameString to set
	 */
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

}