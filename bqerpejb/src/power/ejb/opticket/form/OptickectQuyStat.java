package power.ejb.opticket.form;

public class OptickectQuyStat implements java.io.Serializable{
	//操作票类别
	private String optickectType;
//	开始号
	private String beginNo;
//	结束号
	private String endNo;
//	统计数量
	private int statCount;
//	合格数量
	private int quyCount;
//	作废数量
	private int invaliCount;
//	使用标准票数量
	private int useStandOpCount;
//	使用标准票合格数量
	private int useStandOpQuyCount;
	public String getOptickectType() {
		return optickectType;
	}
	public void setOptickectType(String optickectType) {
		this.optickectType = optickectType;
	}
	public String getBeginNo() {
		return beginNo;
	}
	public void setBeginNo(String beginNo) {
		this.beginNo = beginNo;
	}
	public String getEndNo() {
		return endNo;
	}
	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}
	public int getStatCount() {
		return statCount;
	}
	public void setStatCount(int statCount) {
		this.statCount = statCount;
	}
	public int getQuyCount() {
		return quyCount;
	}
	public void setQuyCount(int quyCount) {
		this.quyCount = quyCount;
	}
	public int getInvaliCount() {
		return invaliCount;
	}
	public void setInvaliCount(int invaliCount) {
		this.invaliCount = invaliCount;
	}
	public int getUseStandOpCount() {
		return useStandOpCount;
	}
	public void setUseStandOpCount(int useStandOpCount) {
		this.useStandOpCount = useStandOpCount;
	}
	public int getUseStandOpQuyCount() {
		return useStandOpQuyCount;
	}
	public void setUseStandOpQuyCount(int useStandOpQuyCount) {
		this.useStandOpQuyCount = useStandOpQuyCount;
	}
}
