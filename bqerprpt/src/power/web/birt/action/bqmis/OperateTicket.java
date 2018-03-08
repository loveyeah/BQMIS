package power.web.birt.action.bqmis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.RunJOpticketstep;
import power.ejb.opticket.form.OpticketBaseForPrint;
import power.ejb.opticket.form.OpticketPrintModel;
import power.web.birt.bean.bqmis.OperateDetailBean;
import power.web.birt.bean.bqmis.OperateTicketBean;
import power.web.birt.constant.Constant;

/**
 * 操作票
 * @author LiuYingwen 
 */
public class OperateTicket {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
    public static Ejb3Factory factory;
	
	static{
		factory = Ejb3Factory.getInstance();
	}

	/** 安措执行（拆除）卡 */
	private OpticketApprove remote;
	/** 工作票号 */
	private String opticketNo;

	/**
	 * 构造函数
	 */
	public OperateTicket() {
		remote = (OpticketApprove) factory
		    .getFacadeRemote("OpticketApproveImpl");
	}

	/**
	 * 操作票所需数据
	 * @return OperateTicketBean 操作票所需数据
	 */
	public OperateTicketBean setOperateTicketBean(String argOpticketNo){
		OperateTicketBean operateTiceket = new OperateTicketBean();
		opticketNo =argOpticketNo;
		OpticketPrintModel model=remote.getOpticketData(opticketNo);
		// 基本信息
		OpticketBaseForPrint opticketBasePrint = model.getModel();
		// 安全措施列表
		List<RunJOpticketstep> opticketStepList = model.getList();
		// 危险点列表
		
		// 操作票名称
		if(opticketBasePrint.getOpticketName()!=null)
		{
			operateTiceket.setOpticketName(opticketBasePrint.getOpticketName());
		}
	    // 专业
		if(opticketBasePrint.getSpecialityName()!=null)
		{
			operateTiceket.setSpecialityName(opticketBasePrint.getSpecialityName());
		}
	    // 操作票号
	    if(opticketBasePrint.getOpticketCode()!=null)
		{
	    	operateTiceket.setOpticketCode(opticketBasePrint.getOpticketCode());
		}
	    // 起始时间
	    if(opticketBasePrint.getStartTime()!=null)
		{
//	    	operateTiceket.setStartTime(opticketBasePrint.getStartTime());
		}
	    // 终止时间
	    if(opticketBasePrint.getEndTime()!=null)
		{	    	
//	    	operateTiceket.setEndTime(opticketBasePrint.getEndTime());
		}
    	// 执行任务
	    if(opticketBasePrint.getOperateTaskName()!=null)
		{
	    	operateTiceket.setOperateTaskName(opticketBasePrint.getOperateTaskName());
		}
    	// 备注
	    if(opticketBasePrint.getMemo()!=null)
		{
	    	operateTiceket.setMemo(opticketBasePrint.getMemo());
		}
    	// 操作人
	    if(opticketBasePrint.getOperatorName()!=null)
		{
	    	operateTiceket.setOperatorName(opticketBasePrint.getOperatorName());
		}
	    // 监护人 */
	    if(opticketBasePrint.getProtectorName()!=null)
		{
	    	operateTiceket.setChargeBy(opticketBasePrint.getProtectorName());
		}
	    // 值长 */
	    if(opticketBasePrint.getClassLeaderName()!=null)
		{
	    	operateTiceket.setWatchMan(opticketBasePrint.getClassLeaderName());
		}
	    // 操作列表 			
	    List<OperateDetailBean> operateList=new ArrayList<OperateDetailBean>();
		if(opticketStepList!=null && opticketStepList.size()>0)
		{
			Iterator<RunJOpticketstep> it=opticketStepList.iterator();
			
			while(it.hasNext()){
				RunJOpticketstep operateStepPrint = (RunJOpticketstep) it.next();
				OperateDetailBean operateStepBean =new OperateDetailBean();
				// 执行情况 
				if(Constant.DONE_CODE.equals(operateStepPrint.getExecStatus())){
					operateStepBean.setExecStatus(Constant.Right);
				}
				else if (Constant.UNDO_CODE.equals(operateStepPrint.getExecStatus())){
					operateStepBean.setExecStatus(Constant.HAVE_NOT);
				}
				
			    // 顺序 
				if(operateStepPrint.getDisplayNo()!=null)
				{
					operateStepBean.setDisplayNo(operateStepPrint.getDisplayNo().toString());
				}
			    // 操作项目 
				if(operateStepPrint.getOperateStepName()!=null)
				{
					operateStepBean.setOperateStepName(operateStepPrint.getOperateStepName());
				}
			    // 完成时间
			    if(operateStepPrint.getFinishTime()!=null)
				{
//			    	operateStepBean.setFinishTime(operateStepPrint.getFinishTime().toString());
				}
			    
			    operateList.add(operateStepBean);
			    }
			
			}
		
		operateTiceket.setOperateDetailList(operateList);
		return operateTiceket;
			
	}
	
}
