package power.ejb.workticket.business;

import javax.ejb.Remote;

import power.ejb.workticket.form.WorkticketDangerForPrint;

@Remote 
public interface WorkticketDangerPrint {
	
	/**
	 * 工作票危险点信息
	 * @param workticketNo
	 * @return
	 */
	public WorkticketDangerForPrint getDangerInfo(String workticketNo);
}
