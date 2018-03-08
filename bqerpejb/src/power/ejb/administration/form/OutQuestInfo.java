package power.ejb.administration.form;

public class OutQuestInfo {
	/**序号**/
	private Long id;
	/**申请单号**/
	private String applyId;
	/**申报人**/
	private String applyMan;
	/**申报人名**/
	private String applyManName;
	/**申报人部门名**/
	private String applyManDeptName;
	/**单据状态**/
	private String dcmStatus;
	/**呈报主题**/
	private String applyTopic;
	/**呈报内容**/
	private String applyText;
	/**呈报日期**/
	private String applyDate;
	/**核稿人**/
	private String checkedMan;
	/**修改时间**/
	private String updateTime;
	/**签报种类**/
	private String appType;
	/**签报编号**/
	private String reportId;
	/**
	 * @return 序号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param 序号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return 申请单号
	 */
	public String getApplyId() {
		return applyId;
	}
	/**
	 * @param 申请单号
	 */
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	/**
	 * @return 申请人
	 */
	public String getApplyMan() {
		return applyMan;
	}
	/**
	 * @param 申请人
	 */
	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}
	/**
	 * @return 申请人名
	 */
	public String getApplyManName() {
		return applyManName;
	}
	/**
	 * @param 申请人名
	 */
	public void setApplyManName(String applyManName) {
		this.applyManName = applyManName;
	}
	/**
	 * @return 申请人部门名
	 */
	public String getApplyManDeptName() {
		return applyManDeptName;
	}
	/**
	 * @param 申请人部门名
	 */
	public void setApplyManDeptName(String applyManDeptName) {
		this.applyManDeptName = applyManDeptName;
	}
	/**
	 * @return 单据状态
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}
	/**
	 * @param 单据状态
	 */
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	/**
	 * @return 呈报主题
	 */
	public String getApplyTopic() {
		return applyTopic;
	}
	/**
	 * @param 呈报主题
	 */
	public void setApplyTopic(String applyTopic) {
		this.applyTopic = applyTopic;
	}
	/**
	 * @return 呈报内容
	 */
	public String getApplyText() {
		return applyText;
	}
	/**
	 * @param 呈报内容
	 */
	public void setApplyText(String applyText) {
		this.applyText = applyText;
	}
	/**
	 * @return 呈报日期
	 */
	public String getApplyDate() {
		return applyDate;
	}
	/**
	 * @param 呈报日期
	 */
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	/**
	 * @return 核稿人
	 */
	public String getCheckedMan() {
		return checkedMan;
	}
	/**
	 * @param 核稿人
	 */
	public void setCheckedMan(String checkedMan) {
		this.checkedMan = checkedMan;
	}
	/**
	 * @return 修改时间
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param 修改时间
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return 签报种类
	 */
	public String getAppType() {
		return appType;
	}
	/**
	 * @param 签报种类
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}
	/**
	 * @return 签报编号
	 */
	public String getReportId() {
		return reportId;
	}
	/**
	 * @param 签报编号
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
