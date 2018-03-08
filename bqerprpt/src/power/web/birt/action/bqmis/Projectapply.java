package power.web.birt.action.bqmis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjApproveForm;
import power.ejb.manage.project.PrjJInfo;
import power.ejb.manage.project.PrjJInfoAdd;
import power.ejb.manage.project.PrjJInfoFacadeRemote;
import power.web.birt.bean.bqmis.ProjectApplyBean;

/**
 * @author Administrator
 * 
 */

public class Projectapply implements IReportEventHandler {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private PrjJInfoFacadeRemote remote;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public Projectapply() {
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

	/**
	 * 工程项目id
	 * 
	 * @param id
	 *            该项目立项审批审批记录
	 * @return
	 */
	public ProjectApplyBean setApprove(String id) {

		PrjJInfo baseInfo = remote.findById(Long.parseLong(id));
		if (baseInfo.getPrjStatus() != 3 && baseInfo.getPrjStatus() != 0) {
			List<PrjApproveForm> list = remote.getApplyApprovelist(Long
					.parseLong(id));
		
			ProjectApplyBean model = new ProjectApplyBean();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			if (list.size() > 0) {
				String temptime = "";
				try {

					temptime = sdf.format(sdf1.parse(list.get(0)
							.getOpinionTime()));

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.setProjectChargeAdvice(list.get(0).getOpinion());
				model.setProjectChargeSign(list.get(0).getCaller());

				model.setProjectChargeSignDate(temptime);

			}
			//判断审批是否有专工审批
			if (list.size() > 1  && !list.get(1).getActionId().equals("512") && list.size() > 2 && list.get(2).getStepId().equals("12")) {
				if (list.size() > 1) {
					String temptime = "";
					try {
						temptime = sdf.format(sdf1.parse(list.get(2)
								.getOpinionTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (list.get(2).getActionId() != null)
						model.setProduceDeptAdvice(list.get(1).getOpinion()
								+ "(" + list.get(2).getOpinion() + "      "
								+ list.get(2).getCaller() + ")");
					else
						model.setProduceDeptAdvice(list.get(1).getOpinion());
					model.setProduceDeptSign(list.get(1).getCaller());

					model.setProduceDeptSignDate(temptime);
				}
				if (list.size() > 3) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(3)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setBusinessDeptAdvice(list.get(3).getOpinion());
					model.setBusinessDeptSign(list.get(3).getCaller());

					model.setBusinessDeptSignDate(temptime);
				}
				if (list.size() > 4) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(4)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setFinanceDeptAdvice(list.get(4).getOpinion());
					model.setFinanceDeptSign(list.get(4).getCaller());

					model.setFinanceDeptSignDate(temptime);
				}
				if (list.size() > 5) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(5)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setOverseeAdvice(list.get(5).getOpinion());
					model.setOverseeSign(list.get(5).getCaller());

					model.setOverseeSignDate(temptime);
				}
				if (list.size() > 6) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(6)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setLeaderAdvice(list.get(6).getOpinion());
					model.setLeaderSign(list.get(6).getCaller());

					model.setLeaderSignDate(temptime);
				}
				if (list.size() > 7) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(7)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setGeneralManagerAdvice(list.get(7).getOpinion());
					model.setGeneralManagerSign(list.get(7).getCaller());

					model.setGeneralManagerSignDate(temptime);
				}
			} else if(list.size() > 1 && !list.get(1).getActionId().equals("512") && list.get(1).getStepId().equals("5")) {
				if (list.size() > 1) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(1)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					model.setProduceDeptAdvice(list.get(1).getOpinion());
					model.setProduceDeptSign(list.get(1).getCaller());

					model.setProduceDeptSignDate(temptime);
				}
				
				if (list.size() > 2) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(2)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setBusinessDeptAdvice(list.get(2).getOpinion());
					model.setBusinessDeptSign(list.get(2).getCaller());

					model.setBusinessDeptSignDate(temptime);
				}
				if (list.size() > 3) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(3)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setFinanceDeptAdvice(list.get(3).getOpinion());
					model.setFinanceDeptSign(list.get(3).getCaller());

					model.setFinanceDeptSignDate(temptime);
				}
				if (list.size() > 4) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(4)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setOverseeAdvice(list.get(4).getOpinion());
					model.setOverseeSign(list.get(4).getCaller());

					model.setOverseeSignDate(temptime);
				}
				if (list.size() > 5) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(5)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setLeaderAdvice(list.get(5).getOpinion());
					model.setLeaderSign(list.get(5).getCaller());

					model.setLeaderSignDate(temptime);
				}
				if (list.size() > 6) {
					String temptime = "";
					try {

						temptime = sdf.format(sdf1.parse(list.get(6)
								.getOpinionTime()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setGeneralManagerAdvice(list.get(6).getOpinion());
					model.setGeneralManagerSign(list.get(6).getCaller());

					model.setGeneralManagerSignDate(temptime);
				}
			}
			return model;
		} else
			return null;

	}

	//
	/**
	 * 工程项目id
	 * 
	 * @param id
	 *            工程项目对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PrjJInfoAdd setBaseInfo(String id) {

		PageObject obj = remote.FindByMoreCondition("", "", "", "", remote
				.findById(Long.parseLong(id)).getPrjNo(), "", "", "", "", "",
				"", "", "");

		List<PrjJInfoAdd> list = obj.getList();
		PrjJInfoAdd baseInfo = new PrjJInfoAdd();
		baseInfo = list.get(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String entryDate = "";
		String planStartDate = "";
		String planEndDate = "";
		try {
			if (baseInfo.getEntryDate() != null)
				entryDate = sdf.format(sdf1.parse(baseInfo.getEntryDate()));
			if (baseInfo.getPlanStartDate() != null)
				planStartDate = sdf.format(sdf1.parse(baseInfo
						.getPlanStartDate()));
			if (baseInfo.getPlanEndDate() != null)
				planEndDate = sdf.format(sdf1.parse(baseInfo.getPlanEndDate()));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseInfo.setEntryDate(entryDate);
		baseInfo.setPlanStartDate(planStartDate);
		baseInfo.setPlanEndDate(planEndDate);
		if (baseInfo.getPrjjInfo().getItemId().equals("lwcb")) {
			baseInfo.getPrjjInfo().setItemId("劳务成本");
		} else if (baseInfo.getPrjjInfo().getItemId().equals("zzfy")) {
			baseInfo.getPrjjInfo().setItemId("生产成本");
		}
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
