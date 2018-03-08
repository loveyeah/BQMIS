

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


import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ConJModify;
import power.ejb.manage.contract.business.ConJModifyFacadeRemote;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ConModifyForm;

import power.ejb.manage.contract.form.ContractFullInfo;



public class GCContractModifyMeetSign extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private ConJModifyFacadeRemote MRemote;
	
	private ConJContractInfoFacadeRemote remote;
	
	/**
	 * 构造函数
	 */
	public GCContractModifyMeetSign(){
		MRemote = (ConJModifyFacadeRemote)factory.getFacadeRemote("ConJModifyFacade");
		
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

	/**项目合同变更id
	 * @param id
	 * 项目合同变更审批记录
	 * @return
	 * @throws ParseException
	 */
	public List<ConApproveForm> setApprove(String id) throws ParseException {
		
		ConJModify baseInfo = MRemote.findById(Long.parseLong(id));
		if (baseInfo.getWorkflowStatus()!= 3 && baseInfo.getWorkflowStatus() != 0) {
			List<ConApproveForm> list = MRemote.getApproveList(Long.parseLong(id));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			for(ConApproveForm data:list){
				if(data.getOpinionTime() != null)
				data.setOpinionTime(sdf.format(sdf1.parse(data.getOpinionTime())));
			}
			return list;
		} else
			return null;

	}

	/**项目合同变更id
	 * @param modId
	 * 项目合同对象
	 * @return
	 */
	public ContractFullInfo setBaseInfoAdd(String modId) {
		// String id = (String)context.getParameterValue("id");
		// Long projectId = Long.valueOf(id);
		ContractFullInfo obj = remote.getConFullInfoById(MRemote.findById(Long.parseLong(modId)).getConId());
		
		
	
		return obj;
	}
	
	/**项目合同变更id
	 * @param modId
	 * 项目合同变更对象
	 * @return
	 */
	public ConModifyForm setBaseInfo(String modId) {
		// String id = (String)context.getParameterValue("id");
		// Long projectId = Long.valueOf(id);
		ConModifyForm baseInfo = MRemote.findConModifyModel(Long.parseLong(modId));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String entryDate="";
	
		try {
			if(baseInfo.getEntryDate()!=null)
		 entryDate=sdf.format(sdf1.parse(baseInfo.getEntryDate()));
		
			
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		baseInfo.setEntryDate(entryDate);
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