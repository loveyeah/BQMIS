package power.ejb.monthaward;

import java.util.Date;

public class  monthAwardForm implements java.io.Serializable{
	private Long monthAwardId;
	private Double monthAward;
	private String  effectStartTime;
	private String effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private String status;
	public Long getMonthAwardId() {
		return monthAwardId;
	}
	public void setMonthAwardId(Long monthAwardId) {
		this.monthAwardId = monthAwardId;
	}
	public Double getMonthAward() {
		return monthAward;
	}
	public void setMonthAward(Double monthAward) {
		this.monthAward = monthAward;
	}
	
	public String getEffectStartTime() {
		return effectStartTime;
	}
	public void setEffectStartTime(String effectStartTime) {
		this.effectStartTime = effectStartTime;
	}
	public String getEffectEndTime() {
		return effectEndTime;
	}
	public void setEffectEndTime(String effectEndTime) {
		this.effectEndTime = effectEndTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}