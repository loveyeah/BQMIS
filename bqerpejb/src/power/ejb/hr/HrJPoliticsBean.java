/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

/**
 * 员工档案综合查询维护 员工政治面貌登记自定义Bean
 * 
 * @author huyou
 * @version 1.0
 */
public class HrJPoliticsBean implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 参加地点
	 */
	private String joinPlace = null;

	/**
	 * 备注
	 */
	private String memo = null;

	/**
	 * 是否最新标志
	 */
	private String isNewMark = null;

	/**
	 * 退出日期
	 */
	private String exitDate = null;

	/**
	 * 介绍人单位
	 */
	private String introducerUnit = null;

	/**
	 * 参加日期
	 */
	private String joinDate = null;

	/**
	 * 政治面貌登记Id
	 */
	private Long politicsid = null;

	/**
	 * 政治面貌.政治面貌名称
	 */
	private String politicsName = null;

	/**
	 * 所属组织
	 */
	private String belongUnit = null;

	/**
	 * 介绍人
	 */
	private String introducer = null;

	/**
	 * 上次修改时间
	 */
	private String lastModifiedDate = null;

	/**
	 * 政治面貌.政治面貌
	 */
	private Long politicsId = null;

	/**
	 * 参加单位
	 */
	private String joinUnit = null;

	/**
	 * 取得参加地点
	 *
	 * @return 参加地点
	 */
	public String getJoinPlace() {
		return joinPlace;
	}

	/**
	 * 设置参加地点
	 *
	 * @param argJoinPlace 参加地点
	 */
	public void setJoinPlace(String argJoinPlace) {
		joinPlace = argJoinPlace;
	}

	/**
	 * 取得备注
	 *
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 *
	 * @param argMemo 备注
	 */
	public void setMemo(String argMemo) {
		memo = argMemo;
	}

	/**
	 * 取得是否最新标志
	 *
	 * @return 是否最新标志
	 */
	public String getIsNewMark() {
		return isNewMark;
	}

	/**
	 * 设置是否最新标志
	 *
	 * @param argIsNewMark 是否最新标志
	 */
	public void setIsNewMark(String argIsNewMark) {
		isNewMark = argIsNewMark;
	}

	/**
	 * 取得退出日期
	 *
	 * @return 退出日期
	 */
	public String getExitDate() {
		return exitDate;
	}

	/**
	 * 设置退出日期
	 *
	 * @param argExitDate 退出日期
	 */
	public void setExitDate(String argExitDate) {
		exitDate = argExitDate;
	}

	/**
	 * 取得介绍人单位
	 *
	 * @return 介绍人单位
	 */
	public String getIntroducerUnit() {
		return introducerUnit;
	}

	/**
	 * 设置介绍人单位
	 *
	 * @param argIntroducerUnit 介绍人单位
	 */
	public void setIntroducerUnit(String argIntroducerUnit) {
		introducerUnit = argIntroducerUnit;
	}

	/**
	 * 取得参加日期
	 *
	 * @return 参加日期
	 */
	public String getJoinDate() {
		return joinDate;
	}

	/**
	 * 设置参加日期
	 *
	 * @param argJoinDate 参加日期
	 */
	public void setJoinDate(String argJoinDate) {
		joinDate = argJoinDate;
	}

	/**
	 * 取得政治面貌登记Id
	 *
	 * @return 政治面貌登记Id
	 */
	public Long getPoliticsid() {
		return politicsid;
	}

	/**
	 * 设置政治面貌登记Id
	 *
	 * @param argPoliticsid 政治面貌登记Id
	 */
	public void setPoliticsid(Long argPoliticsid) {
		politicsid = argPoliticsid;
	}

	/**
	 * 取得政治面貌.政治面貌名称
	 *
	 * @return 政治面貌.政治面貌名称
	 */
	public String getPoliticsName() {
		return politicsName;
	}

	/**
	 * 设置政治面貌.政治面貌名称
	 *
	 * @param argPoliticsName 政治面貌.政治面貌名称
	 */
	public void setPoliticsName(String argPoliticsName) {
		politicsName = argPoliticsName;
	}

	/**
	 * 取得所属组织
	 *
	 * @return 所属组织
	 */
	public String getBelongUnit() {
		return belongUnit;
	}

	/**
	 * 设置所属组织
	 *
	 * @param argBelongUnit 所属组织
	 */
	public void setBelongUnit(String argBelongUnit) {
		belongUnit = argBelongUnit;
	}

	/**
	 * 取得介绍人
	 *
	 * @return 介绍人
	 */
	public String getIntroducer() {
		return introducer;
	}

	/**
	 * 设置介绍人
	 *
	 * @param argIntroducer 介绍人
	 */
	public void setIntroducer(String argIntroducer) {
		introducer = argIntroducer;
	}

	/**
	 * 取得上次修改时间
	 *
	 * @return 上次修改时间
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * 设置上次修改时间
	 *
	 * @param argLastModifiedDate 上次修改时间
	 */
	public void setLastModifiedDate(String argLastModifiedDate) {
		lastModifiedDate = argLastModifiedDate;
	}

	/**
	 * 取得政治面貌.政治面貌
	 *
	 * @return 政治面貌.政治面貌
	 */
	public Long getPoliticsId() {
		return politicsId;
	}

	/**
	 * 设置政治面貌.政治面貌
	 *
	 * @param argPoliticsId 政治面貌.政治面貌
	 */
	public void setPoliticsId(Long argPoliticsId) {
		politicsId = argPoliticsId;
	}

	/**
	 * 取得参加单位
	 *
	 * @return 参加单位
	 */
	public String getJoinUnit() {
		return joinUnit;
	}

	/**
	 * 设置参加单位
	 *
	 * @param argJoinUnit 参加单位
	 */
	public void setJoinUnit(String argJoinUnit) {
		joinUnit = argJoinUnit;
	}


}
