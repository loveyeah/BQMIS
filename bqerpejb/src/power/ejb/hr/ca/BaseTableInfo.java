/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 基础表维护bean
 * 
 * @author zhengzhipeng
 * 
 */
public class BaseTableInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 基础表ID */
	private String recordId;
	/** 加班类别 */
	private String recordOverTimeName;
	/** 是否发放费用 */
	private String recordIfOverTimeFee;
	/** 运行班类别 */
	private String recordShiftName;
	/** 津贴标准 */
	private String recordShiftFee;
	/** 考勤标志 */
	private String recordMark;

	/**
	 * @return 基础表ID
	 */
	public String getRecordId() {
		return recordId;
	}

	/**
	 * @param set
	 *            基础表ID
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	/**
	 * @return 加班类别
	 */
	public String getRecordOverTimeName() {
		return recordOverTimeName;
	}

	/**
	 * @param set
	 *            加班类别
	 */
	public void setRecordOverTimeName(String recordOverTimeName) {
		this.recordOverTimeName = recordOverTimeName;
	}

	/**
	 * @return 是否发放费用
	 */
	public String getRecordIfOverTimeFee() {
		return recordIfOverTimeFee;
	}

	/**
	 * @param set
	 *            是否发放费用
	 */
	public void setRecordIfOverTimeFee(String recordIfOverTimeFee) {
		this.recordIfOverTimeFee = recordIfOverTimeFee;
	}

	/**
	 * @return 运行班类别
	 */
	public String getRecordShiftName() {
		return recordShiftName;
	}

	/**
	 * @param set
	 *            运行班类别
	 */
	public void setRecordShiftName(String recordShiftName) {
		this.recordShiftName = recordShiftName;
	}

	/**
	 * @return 津贴标准
	 */
	public String getRecordShiftFee() {
		return recordShiftFee;
	}

	/**
	 * @param set
	 *            津贴标准
	 */
	public void setRecordShiftFee(String recordShiftFee) {
		this.recordShiftFee = recordShiftFee;
	}

	/**
	 * @return 考勤标志
	 */
	public String getRecordMark() {
		return recordMark;
	}

	/**
	 * @param set
	 *            考勤标志
	 */
	public void setRecordMark(String recordMark) {
		this.recordMark = recordMark;
	}
}
