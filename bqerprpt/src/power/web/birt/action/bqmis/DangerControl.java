/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketDangerPrint;
import power.web.birt.bean.bqmis.WorkticketDanger;
import power.web.birt.bean.bqmis.WorkticketDangerForPrint;

/**
 * 工作票危险点控制措施票
 * @author LiuYingwen 
 */
public class DangerControl {
	    
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private WorkticketDangerPrint dangerPrint;
	
	private String workticketNo;
	
	public DangerControl() {
		dangerPrint = (WorkticketDangerPrint)Ejb3Factory.getInstance().getFacadeRemote("WorkticketDangerPrintImpl");
	}
    
	/**
	 * 危险点控制措施票所需数据
	 * @param workticketNo
	 * @return WorkticketDangerForPrint
	 */
	public WorkticketDangerForPrint setWorkticketDangerForPrint(String workticketNo) {
		
		power.ejb.workticket.form.WorkticketDangerForPrint ejbWorkticketDangerForPrint =dangerPrint.getDangerInfo(workticketNo);
		WorkticketDangerForPrint bean = new WorkticketDangerForPrint();
		bean.setChargeBy(ejbWorkticketDangerForPrint.getChargeBy());
		bean.setDangerContent(ejbWorkticketDangerForPrint.getDangerContent());
		bean.setSignBy(ejbWorkticketDangerForPrint.getSignBy());
		bean.setSignText(ejbWorkticketDangerForPrint.getSignText());
		bean.setWorkticketNo(workticketNo);
		bean.setWorkticketStausId(ejbWorkticketDangerForPrint.getWorkticketStausId());
		List<WorkticketDanger> birtdlist = new ArrayList<WorkticketDanger>();
		List<power.ejb.workticket.form.WorkticketDanger> ejbDanger = ejbWorkticketDangerForPrint.getDanger();
		// 危险点列表
		List<WorkticketDanger> addDangerList = new ArrayList<WorkticketDanger>();
		String dangerName = "";
		String dangerNameAdd = "";
		for(int i =0;i<ejbDanger.size();i++){
			WorkticketDanger danger = new WorkticketDanger();
			if("N".equals(ejbDanger.get(i).getIsRunadd())) {
			    danger.setDangerId(ejbDanger.get(i).getDangerId());
			    danger.setDangerMeasure(ejbDanger.get(i).getDangerMeasure());
			    if(i>0 && dangerName.equals(ejbDanger.get(i).getDangerName())) {
			    	danger.setDangerName("");
			    } else {
			    	dangerName = ejbDanger.get(i).getDangerName();
			    	danger.setDangerName(ejbDanger.get(i).getDangerName());
			    }	
			    danger.setIsRunadd(ejbDanger.get(i).getIsRunadd());
			    danger.setOrderBy(ejbDanger.get(i).getOrderBy());
			    birtdlist.add(danger);
			} else if("Y".equals(ejbDanger.get(i).getIsRunadd())) {
				    danger.setDangerId(ejbDanger.get(i).getDangerId());
				    danger.setDangerMeasure(ejbDanger.get(i).getDangerMeasure());
				    if(i>0 && dangerNameAdd.equals(ejbDanger.get(i).getDangerName())) {
				    	danger.setDangerName("");
				    } else {
				    	dangerNameAdd = ejbDanger.get(i).getDangerName();
				    	danger.setDangerName(ejbDanger.get(i).getDangerName());
				    }	
				    danger.setIsRunadd(ejbDanger.get(i).getIsRunadd());
				    danger.setOrderBy(ejbDanger.get(i).getOrderBy());
				    addDangerList.add(danger);
				
			}
			
		}
		bean.setDanger(birtdlist);
		bean.setAddDangerList(addDangerList);
		return bean;
	}

	/**
	 * @return the workticketNo
	 */
	public String getWorkticketNo() {
		return workticketNo;
	}

	/**
	 * @param workticketNo the workticketNo to set
	 */
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

}
