


/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.action.bqmis;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;


import power.web.comm.AbstractAction;

import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ContractFullInfo;




public class GCContractMeetSign extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private ConJContractInfoFacadeRemote remote;
	
	/**
	 * 构造函数
	 */
	public GCContractMeetSign(){
		remote = (ConJContractInfoFacadeRemote)factory.getFacadeRemote("ConJContractInfoFacade");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#afterFactory(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void afterFactory(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#afterRender(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void afterRender(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#beforeFactory(org.eclipse.birt.report.engine.api.script.element.IReportDesign,
	 *      org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void beforeFactory(IReportDesign design, IReportContext context) {

	}

	/**项目合同id
	 * @param id
	 * 项目合同会签审批记录
	 * @return
	 * @throws ParseException
	 */
	public List<ConApproveForm> setApprove(String id) throws ParseException {
		
		ConJContractInfo baseInfo = remote.findById(Long.parseLong(id));
		if (baseInfo.getWorkflowStatus() != 0) {
			List<ConApproveForm> list = remote.getApproveList(Long.parseLong(id));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			for(ConApproveForm data:list){
				data.setOpinionTime(sdf.format(sdf1.parse(data.getOpinionTime())));
				if(data.getComeTime() != null)
				data.setComeTime(sdf.format(sdf1.parse(data.getComeTime())));
			}
				
			return list;
		} else
			return null;

	}
	

	public List<ConApproveForm> setApproveTable(String id) throws ParseException {
		
		ConJContractInfo baseInfo = remote.findById(Long.parseLong(id));
		if (baseInfo.getWorkflowStatus() != 0) {
			List<ConApproveForm> list = remote.getApproveTableList(Long.parseLong(id));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			for(ConApproveForm data:list){
				data.setOpinionTime(sdf.format(sdf1.parse(data.getOpinionTime())));
				if(data.getComeTime() !=null)
				data.setComeTime(sdf.format(sdf1.parse(data.getComeTime())));
				
			}
			
			return list;
		} else
			return null;
		
	}

	/**
	 * 委托审批
	 * @throws ParseException 
	 */
	
public List<ConApproveForm> delegationApprove(String id) throws ParseException{
	ConJContractInfo baseInfo = remote.findById(Long.parseLong(id));
	if (baseInfo.getWorkflowStatus() != 0) {
		List<ConApproveForm> list = remote.getdelegationApprove(Long.parseLong(id));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		for(ConApproveForm data:list){
			data.setOpinionTime(sdf.format(sdf1.parse(data.getOpinionTime())));
		}
		return list;
	} else
		return null;
	
}
	
	
	/**项目合同id
	 * @param conId
	 * 项目合同对象
	 * @return
	 */
	public ContractFullInfo setBaseInfo(String conId) {
		// String id = (String)context.getParameterValue("id");
		// Long projectId = Long.valueOf(id);
		ContractFullInfo baseInfo = remote.getConFullInfoById(Long.parseLong(conId));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String entryDate="";
		String operateDate="";
		String jyjhDate="";
		String itemName = "";
		try {
			if(baseInfo.getEntryDate()!=null)
			 entryDate=sdf.format(sdf1.parse(baseInfo.getEntryDate()));
			if(baseInfo.getOperateDate()!=null){
				String temp[] = baseInfo.getOperateDate().split("-");
				operateDate = temp[0]+"年"+temp[1]+"月"+temp[2]+"日";
			}
			if(baseInfo.getJyjhDate()!=null){
				String temp[] = baseInfo.getJyjhDate().split("-");
				jyjhDate = temp[0]+"年"+temp[1]+"月"+temp[2]+"日";
			}
			itemName=baseInfo.getItemName();
//			if(baseInfo.getItemId()!=null)
//				itemName = baseInfo.getItemId()=="zzfy"?"制造费用":"劳务成本";
			
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		baseInfo.setEntryDate(entryDate);
		baseInfo.setOperateDate(operateDate);
		baseInfo.setJyjhDate(jyjhDate);
		baseInfo.setItemName(itemName);
		baseInfo.setCurrencyName(remote.getCurrencyNameByItsCode(baseInfo.getCurrencyType()));

	
		return baseInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#beforeRender(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void beforeRender(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#initialize(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void initialize(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 电气一工作票所需数据
	 * 
	 * @return electricOneBean 电气一工作票所需数据
	 */
	// public ElectricOneBean setElectricOneBean(String id) {
	// Long projectId = Long.valueOf(id);
	// PrjJInfo baseInfo = remote.findById(projectId);
	// if(baseInfo.getPrjStatus() != 3 && baseInfo.getPrjStatus() != 0) {
	// List list = remote.getApplyApprovelist(projectId);
	// }
	// }
}