package power.ejb.opticket.form;

public class OptickectStatuStat implements java.io.Serializable{
	//操作票类别
	private String optickectType;
//	开始号
	private String beginNo;
//	结束号
	private String endNo;
//	开始工作数量
	private int beginWorkCount;
//	完成数量
	private int endCount;
//	作废数量
	private int invaliCount;
//	总计
	private int totlaNum;
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
	public int getBeginWorkCount() {
		return beginWorkCount;
	}
	public void setBeginWorkCount(int beginWorkCount) {
		this.beginWorkCount = beginWorkCount;
	}
	public int getEndCount() {
		return endCount;
	}
	public void setEndCount(int endCount) {
		this.endCount = endCount;
	}
	public int getInvaliCount() {
		return invaliCount;
	}
	public void setInvaliCount(int invaliCount) {
		this.invaliCount = invaliCount;
	}
	public int getTotlaNum() {
		return totlaNum;
	}
	public void setTotlaNum(int totlaNum) {
		this.totlaNum = totlaNum;
	}
}
