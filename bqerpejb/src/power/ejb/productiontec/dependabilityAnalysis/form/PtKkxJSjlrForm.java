package power.ejb.productiontec.dependabilityAnalysis.form;

@SuppressWarnings("serial")
public class PtKkxJSjlrForm implements java.io.Serializable{

	private Long sjlrId;
	private String blockCode;
	private Long jzztId;
	private String startDate;
	private String endDate;
	private Double keepTime;
	private Double reduceExert;
	private Long stopTimes;
	private Long successTimes;
	private Long failureTimes;
	private Double repairMandays;
	private Double repairCost;
	private String stopReason;
	private String jzztCode;
	private String jzztName;
	
	public String getJzztCode() {
		return jzztCode;
	}
	public void setJzztCode(String jzztCode) {
		this.jzztCode = jzztCode;
	}
	public String getJzztName() {
		return jzztName;
	}
	public void setJzztName(String jzztName) {
		this.jzztName = jzztName;
	}
	public Long getSjlrId() {
		return sjlrId;
	}
	public void setSjlrId(Long sjlrId) {
		this.sjlrId = sjlrId;
	}
	public String getBlockCode() {
		return blockCode;
	}
	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}
	public Long getJzztId() {
		return jzztId;
	}
	public void setJzztId(Long jzztId) {
		this.jzztId = jzztId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Double getKeepTime() {
		return keepTime;
	}
	public void setKeepTime(Double keepTime) {
		this.keepTime = keepTime;
	}
	public Double getReduceExert() {
		return reduceExert;
	}
	public void setReduceExert(Double reduceExert) {
		this.reduceExert = reduceExert;
	}
	public Long getStopTimes() {
		return stopTimes;
	}
	public void setStopTimes(Long stopTimes) {
		this.stopTimes = stopTimes;
	}
	public Long getSuccessTimes() {
		return successTimes;
	}
	public void setSuccessTimes(Long successTimes) {
		this.successTimes = successTimes;
	}
	public Long getFailureTimes() {
		return failureTimes;
	}
	public void setFailureTimes(Long failureTimes) {
		this.failureTimes = failureTimes;
	}
	public Double getRepairMandays() {
		return repairMandays;
	}
	public void setRepairMandays(Double repairMandays) {
		this.repairMandays = repairMandays;
	}
	public Double getRepairCost() {
		return repairCost;
	}
	public void setRepairCost(Double repairCost) {
		this.repairCost = repairCost;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
}
