package power.ejb.manage.client.form;

@SuppressWarnings("serial")
public class ConJAppraiseForm implements java.io.Serializable{
	/** 汇总ID*/
	private Long appraisalId;
	/**合作伙伴ID **/
	private Long cliendId;
	/**区间ID**/
	private Long intervalId;
	/**总分 **/
	private Double totalScore;
	/**汇总标志 **/
	private String gatherFlag;
	/**评价人 **/
	private String appraiseBy;
	/**评价人名称 **/
	private String appraiseName;
	/**评价日期 **/
	private String appraiseDate;
	/**总评 **/
	private String appraisalResult;
	/**汇总人 **/
	private String gatherBy;
	/**汇总人名称 **/
	private String gatherName;
	/**汇总日期 **/
	private String gatherDate;
	/**合作伙伴名称 **/
	private String clientName;
	/**开始日期（区间） **/
	private String beginDate;
	/**结束日期（区间） **/
	private String endDate;
	/**评价区间 **/
	private String intervalDate;
	
	public Long getAppraisalId() {
		return appraisalId;
	}
	public void setAppraisalId(Long appraisalId) {
		this.appraisalId = appraisalId;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public Long getIntervalId() {
		return intervalId;
	}
	public void setIntervalId(Long intervalId) {
		this.intervalId = intervalId;
	}
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public String getGatherFlag() {
		return gatherFlag;
	}
	public void setGatherFlag(String gatherFlag) {
		this.gatherFlag = gatherFlag;
	}
	public String getAppraiseBy() {
		return appraiseBy;
	}
	public void setAppraiseBy(String appraiseBy) {
		this.appraiseBy = appraiseBy;
	}
	public String getAppraiseDate() {
		return appraiseDate;
	}
	public void setAppraiseDate(String appraiseDate) {
		this.appraiseDate = appraiseDate;
	}
	public String getAppraisalResult() {
		return appraisalResult;
	}
	public void setAppraisalResult(String appraisalResult) {
		this.appraisalResult = appraisalResult;
	}
	public String getGatherBy() {
		return gatherBy;
	}
	public void setGatherBy(String gatherBy) {
		this.gatherBy = gatherBy;
	}
	public String getGatherDate() {
		return gatherDate;
	}
	public void setGatherDate(String gatherDate) {
		this.gatherDate = gatherDate;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIntervalDate() {
		return intervalDate;
	}
	public void setIntervalDate(String intervalDate) {
		this.intervalDate = intervalDate;
	}
	public String getAppraiseName() {
		return appraiseName;
	}
	public void setAppraiseName(String appraiseName) {
		this.appraiseName = appraiseName;
	}
	public String getGatherName() {
		return gatherName;
	}
	public void setGatherName(String gatherName) {
		this.gatherName = gatherName;
	}
	
}
