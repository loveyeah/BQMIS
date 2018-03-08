package power.web.birt.bean.bqmis;

public class SafetyContentBean {
  
   
    /* 安全措施项目 */
    private String safetyDesc="";
    /* 安措内容 */
    private String safetyContent="";
    /* 挂牌编号 */
    private String markCard="";
    /* 挂牌块数 */
    private String cardCount="";
    /* 是否执行（拆除） */
    private String operateFlg="";
    /* 执行（拆除）顺序 */
    private String operateOrder="";
    /* 执行（拆除）情况 */
    private String operateResult="";
    
    public SafetyContentBean()
    {
    	
    }
	public String getSafetyDesc() {
		return safetyDesc;
	}
	public void setSafetyDesc(String safetyDesc) {
		this.safetyDesc = safetyDesc;
	}
	public String getSafetyContent() {
		return safetyContent;
	}
	public void setSafetyContent(String safetyContent) {
		this.safetyContent = safetyContent;
	}
	public String getMarkCard() {
		return markCard;
	}
	public void setMarkCard(String markCard) {
		this.markCard = markCard;
	}
	public String getCardCount() {
		return cardCount;
	}
	public void setCardCount(String cardCount) {
		this.cardCount = cardCount;
	}
	public String getOperateOrder() {
		return operateOrder;
	}
	public void setOperateOrder(String operateOrder) {
		this.operateOrder = operateOrder;
	}
	public String getOperateResult() {
		return operateResult;
	}
	public void setOperateResult(String operateResult) {
		this.operateResult = operateResult;
	}
	public String getOperateFlg() {
		return operateFlg;
	}
	public void setOperateFlg(String operateFlg) {
		this.operateFlg = operateFlg;
	}
}
  