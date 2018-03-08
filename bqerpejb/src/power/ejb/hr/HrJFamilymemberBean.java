/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

/**
 * 员工档案综合查询维护 员工社会关系登记自定义Bean
 * 
 * @author huyou
 * @version 1.0
 */
public class HrJFamilymemberBean implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 家庭成员ID
	 */
	private Long familymemberid = null;

	/**
	 * 学历编码.学历名称
	 */
	private String educationName = null;

	/**
	 * 婚否
	 */
	private String ifMarried = null;

	/**
	 * 单位
	 */
	private String unit = null;

	/**
	 * 职务名称
	 */
	private String headshipName = null;

	/**
	 * 民族编码表.民族名称
	 */
	private String nationName = null;

	/**
	 * 籍贯编码表.籍贯
	 */
	private Long nativePlaceId = null;

	/**
	 * 是否供养
	 */
	private String ifLineally = null;

	/**
	 * 政治面貌.政治面貌名称
	 */
	private String politicsName = null;

	/**
	 * 籍贯编码表.籍贯名称
	 */
	private String nativePlaceName = null;

	/**
	 * 性别
	 */
	private String sex = null;

	/**
	 * 称谓编码.称谓编码ID
	 */
	private Long callsCodeId = null;

	/**
	 * 是否直系亲属
	 */
	private String zxqsMark = null;

	/**
	 * 上次修改时间
	 */
	private String lastModifiedDate = null;

	/**
	 * 称谓编码.称谓名称
	 */
	private String callsName = null;

	/**
	 * 民族编码表.民族
	 */
	private Long nationCodeId = null;

	/**
	 * 政治面貌.政治面貌
	 */
	private Long politicsId = null;

	/**
	 * 姓名
	 */
	private String name = null;

	/**
	 * 学历编码.学历Id
	 */
	private Long educationId = null;

	/**
	 * 出生日期
	 */
	private String birthday = null;
	
	
	private String memo = null;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 取得家庭成员ID
	 *
	 * @return 家庭成员ID
	 */
	public Long getFamilymemberid() {
		return familymemberid;
	}

	/**
	 * 设置家庭成员ID
	 *
	 * @param argFamilymemberid 家庭成员ID
	 */
	public void setFamilymemberid(Long argFamilymemberid) {
		familymemberid = argFamilymemberid;
	}

	/**
	 * 取得学历编码.学历名称
	 *
	 * @return 学历编码.学历名称
	 */
	public String getEducationName() {
		return educationName;
	}

	/**
	 * 设置学历编码.学历名称
	 *
	 * @param argEducationName 学历编码.学历名称
	 */
	public void setEducationName(String argEducationName) {
		educationName = argEducationName;
	}

	/**
	 * 取得婚否
	 *
	 * @return 婚否
	 */
	public String getIfMarried() {
		return ifMarried;
	}

	/**
	 * 设置婚否
	 *
	 * @param argIfMarried 婚否
	 */
	public void setIfMarried(String argIfMarried) {
		ifMarried = argIfMarried;
	}

	/**
	 * 取得单位
	 *
	 * @return 单位
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * 设置单位
	 *
	 * @param argUnit 单位
	 */
	public void setUnit(String argUnit) {
		unit = argUnit;
	}

	/**
	 * 取得职务名称
	 *
	 * @return 职务名称
	 */
	public String getHeadshipName() {
		return headshipName;
	}

	/**
	 * 设置职务名称
	 *
	 * @param argHeadshipName 职务名称
	 */
	public void setHeadshipName(String argHeadshipName) {
		headshipName = argHeadshipName;
	}

	/**
	 * 取得民族编码表.民族名称
	 *
	 * @return 民族编码表.民族名称
	 */
	public String getNationName() {
		return nationName;
	}

	/**
	 * 设置民族编码表.民族名称
	 *
	 * @param argNationName 民族编码表.民族名称
	 */
	public void setNationName(String argNationName) {
		nationName = argNationName;
	}

	/**
	 * 取得籍贯编码表.籍贯
	 *
	 * @return 籍贯编码表.籍贯
	 */
	public Long getNativePlaceId() {
		return nativePlaceId;
	}

	/**
	 * 设置籍贯编码表.籍贯
	 *
	 * @param argNativePlaceId 籍贯编码表.籍贯
	 */
	public void setNativePlaceId(Long argNativePlaceId) {
		nativePlaceId = argNativePlaceId;
	}

	/**
	 * 取得是否供养
	 *
	 * @return 是否供养
	 */
	public String getIfLineally() {
		return ifLineally;
	}

	/**
	 * 设置是否供养
	 *
	 * @param argIfLineally 是否供养
	 */
	public void setIfLineally(String argIfLineally) {
		ifLineally = argIfLineally;
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
	 * 取得籍贯编码表.籍贯名称
	 *
	 * @return 籍贯编码表.籍贯名称
	 */
	public String getNativePlaceName() {
		return nativePlaceName;
	}

	/**
	 * 设置籍贯编码表.籍贯名称
	 *
	 * @param argNativePlaceName 籍贯编码表.籍贯名称
	 */
	public void setNativePlaceName(String argNativePlaceName) {
		nativePlaceName = argNativePlaceName;
	}

	/**
	 * 取得性别
	 *
	 * @return 性别
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别
	 *
	 * @param argSex 性别
	 */
	public void setSex(String argSex) {
		sex = argSex;
	}

	/**
	 * 取得称谓编码.称谓编码ID
	 *
	 * @return 称谓编码.称谓编码ID
	 */
	public Long getCallsCodeId() {
		return callsCodeId;
	}

	/**
	 * 设置称谓编码.称谓编码ID
	 *
	 * @param argCallsCodeId 称谓编码.称谓编码ID
	 */
	public void setCallsCodeId(Long argCallsCodeId) {
		callsCodeId = argCallsCodeId;
	}

	/**
	 * 取得是否直系亲属
	 *
	 * @return 是否直系亲属
	 */
	public String getZxqsMark() {
		return zxqsMark;
	}

	/**
	 * 设置是否直系亲属
	 *
	 * @param argZxqsMark 是否直系亲属
	 */
	public void setZxqsMark(String argZxqsMark) {
		zxqsMark = argZxqsMark;
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
	 * 取得称谓编码.称谓名称
	 *
	 * @return 称谓编码.称谓名称
	 */
	public String getCallsName() {
		return callsName;
	}

	/**
	 * 设置称谓编码.称谓名称
	 *
	 * @param argCallsName 称谓编码.称谓名称
	 */
	public void setCallsName(String argCallsName) {
		callsName = argCallsName;
	}

	/**
	 * 取得民族编码表.民族
	 *
	 * @return 民族编码表.民族
	 */
	public Long getNationCodeId() {
		return nationCodeId;
	}

	/**
	 * 设置民族编码表.民族
	 *
	 * @param argNationCodeId 民族编码表.民族
	 */
	public void setNationCodeId(Long argNationCodeId) {
		nationCodeId = argNationCodeId;
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
	 * 取得姓名
	 *
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 *
	 * @param argName 姓名
	 */
	public void setName(String argName) {
		name = argName;
	}

	/**
	 * 取得学历编码.学历Id
	 *
	 * @return 学历编码.学历Id
	 */
	public Long getEducationId() {
		return educationId;
	}

	/**
	 * 设置学历编码.学历Id
	 *
	 * @param argEducationId 学历编码.学历Id
	 */
	public void setEducationId(Long argEducationId) {
		educationId = argEducationId;
	}

	/**
	 * 取得出生日期
	 *
	 * @return 出生日期
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * 设置出生日期
	 *
	 * @param argBirthday 出生日期
	 */
	public void setBirthday(String argBirthday) {
		birthday = argBirthday;
	}
}
