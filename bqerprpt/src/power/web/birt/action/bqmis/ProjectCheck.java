package power.web.birt.action.bqmis;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjApproveForm;
import power.ejb.manage.project.PrjJInfo;
import power.ejb.manage.project.PrjJInfoAdd;
import power.ejb.manage.project.PrjJInfoFacadeRemote;



public class ProjectCheck  implements IReportEventHandler {



	private PrjJInfoFacadeRemote remote;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public ProjectCheck() {
		remote = (PrjJInfoFacadeRemote) factory
				.getFacadeRemote("PrjJInfoFacade");
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

	/**工程项目id
	 * @param id
	 * 该项目验收审批记录
	 * @return
	 * @throws ParseException
	 */
	public List<PrjApproveForm> setApprove(String id) throws ParseException {
		// String id = (String)context.getParameterValue("id");
		// Long projectId = Long.valueOf(id);
		PrjJInfo baseInfo = remote.findById(Long.parseLong(id));
		if (baseInfo.getPrjStatus() != 5 && baseInfo.getPrjStatus() != 7 && baseInfo.getPrjStatus() != 4) {
			List<PrjApproveForm> list = remote.getCheckApprovelist(Long.parseLong(id));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		for(PrjApproveForm data:list){
			data.setOpinionTime(sdf.format(sdf1.parse(data.getOpinionTime())));
		}
			
			return list; 
		} else
			return null;

	}

	/**工程项目id
	 * @param id
	 * 工程项目对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PrjJInfoAdd setBaseInfo(String id) {
	
		PageObject obj = remote.FindByMoreCondition("","","","",remote.findById(Long.parseLong(id))
				.getPrjNo(), "", "", "", "", "", "", "", "");

		List<PrjJInfoAdd> list = obj.getList();
		PrjJInfoAdd baseInfo = new PrjJInfoAdd();
		baseInfo = list.get(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String entryDate="";
		String factStartDate="";
		String factEndDate="";
		try {
			if(baseInfo.getEntryDate()!=null)
			entryDate=sdf.format(sdf1.parse(baseInfo.getEntryDate()));
			if(baseInfo.getFactStartDate()!=null)
			factStartDate=sdf.format(sdf1.parse(baseInfo.getFactStartDate()));
			if(baseInfo.getFactEndDate()!=null)
			factEndDate=sdf.format(sdf1.parse(baseInfo.getFactEndDate()));
			
			
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		baseInfo.setEntryDate(entryDate);
		baseInfo.setFactStartDate(factStartDate);
		baseInfo.setFactEndDate(factEndDate);
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
