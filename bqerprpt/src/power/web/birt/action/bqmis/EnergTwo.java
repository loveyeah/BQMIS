/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketDangerPrint;
import power.ejb.workticket.business.WorkticketPrint;
import power.ejb.workticket.form.WorkticketBaseForPrint;
import power.ejb.workticket.form.WorkticketDangerForPrint;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketPrintModel;
import power.web.birt.bean.bqmis.EnergTwoBean;
import power.web.birt.bean.bqmis.WorkticketDanger;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;

/**
 * 热力机械，热控票报表填充数据类
 * 
 * @author LiuYingwen
 * @version 1.0
 */
public class EnergTwo {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/** 工作票 */
	private WorkticketPrint remote;
	/** 危险点 */
	private WorkticketDangerPrint dangerPrint;
	/** 工作票号 */
	private String workticketNo;

	/**
	 * 构造函数
	 */
	public EnergTwo() {
		remote = (WorkticketPrint) factory
				.getFacadeRemote("WorkticketPrintImp");
		dangerPrint = (WorkticketDangerPrint) factory
				.getFacadeRemote("WorkticketDangerPrintImpl");
	}

	/**
	 * 热力机械,热控第二种工作票所需数据
	 * 
	 * @return energBean 热力机械,热控工作票所需数据
	 */
	public EnergTwoBean setEnergBean(String no, String flag) {

		EnergTwoBean energBean = new EnergTwoBean();
		workticketNo = no;
		energBean.setWorkticketNo(workticketNo);
		// 调用EJB获得所需的所有数据
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo(workticketNo);
		// 基本信息
		WorkticketBaseForPrint workBasePrint = model.getBase();
		// // 安全措施列表
		// List<SecurityMeasureForPrint> securityPrintList = model.getSafety();
		// 危险点列表
		List<WorkticketDanger> birtdlist = new ArrayList<WorkticketDanger>();
		List<WorkticketDanger> addDangerList = new ArrayList<WorkticketDanger>();
		String dangerName = "";
		String dangerNameAdd = "";
		WorkticketDangerForPrint dangerModel = dangerPrint.getDangerInfo(workticketNo);
		energBean.setWorktickStauts(dangerModel.getWorkticketStausId());
		List<power.ejb.workticket.form.WorkticketDanger> ejbDanger = dangerModel.getDanger();
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
		energBean.setDangerList(birtdlist);
		energBean.setAddDangerList(addDangerList);
		// 审批列表
		List<WorkticketHisForPrint> workHisPrintList = model.getHistory();
		// 基本信息的设置
		if (workBasePrint != null) {
			// 工作负责人
			energBean.setChargeBy(checkString(workBasePrint.getChargeBy()));
			// 工作监护人
			energBean.setWorkWacth(workBasePrint.getWatcher());
			// 机组
			energBean.setEquAttributeName(checkString(workBasePrint
					.getRepairSpecail()));
			// 班组
			energBean.setChargeDept(checkString(workBasePrint.getChargeDept()));
			// 工作班成员
			String members = checkString(workBasePrint.getMembers());
			energBean.setMembers(members);
			// 工作地点
			energBean.setWorkSpace(workBasePrint.getLocationName());
			// 工作内容
			String workticketContent = commUtils.replaceWithBR(workBasePrint.getWorkticketContent());
			energBean.setWorkticketContent(workticketContent);
			// 热力机械工作票
//			if ("1".equals(flag)) {
//				energBean.setMembers(members);
//				if (commUtils.countLineTotal(workticketContent,
//						Constant.WORK_CONTENT_LINE_MAX_TWO) <= 6) {
//
//					energBean.setWorkticketContent(workticketContent);
//				} else {
//					energBean.setWorkticketContent(Constant.MEETFUYE);
//					energBean.setWorkticketContentTwo(workticketContent);
//					energBean.setContentFlg(false);
//				}
//				// 热控工作票
//			} else {
//				energBean.setMembers(members);
//				if (members.length() > Constant.ControlMemLCount) {
//					energBean.setMembers(members.substring(0,
//							Constant.ControlMemLCount));
//					energBean.setMembersTwo(members
//							.substring(Constant.ControlMemLCount));
//				} else {
//					energBean.setMembers(members);
//				}
//				if (commUtils.countLineTotal(workticketContent,
//						Constant.WORK_CONTENT_LINE_MAX_TWO) <= 6) {
//
//					energBean.setWorkticketContent(workticketContent);
//				} else {
//					energBean.setWorkticketContentOne(Constant.MEETFUYE);
//					energBean.setWorkticketContentTwo(workticketContent);
//					energBean.setContentFlg(false);
//				}
//
//			}
			// 工作班成员数
			energBean
					.setMemberCount(checkString(workBasePrint.getMemberCount()));
			// 计划工作开始时间
			energBean.setPlanStartDate(commUtils.formatTime(workBasePrint
					.getPlanStartDate()));
			// 计划工作结束时间
			energBean.setPlanEndDate(commUtils.formatTime(workBasePrint
					.getPlanEndDate()));
			// 备注
			energBean.setWorkticketMemo(workBasePrint.getWorkticketMemo());
			// 工作终结时间
			String actualEndDate = commUtils.formatTime(workBasePrint
					.getActualEndDate());
		}

		// 审批列表的设置
		if (workHisPrintList != null) {
			Iterator WHisit = workHisPrintList.iterator();
			while (WHisit.hasNext()) {
				WorkticketHisForPrint workHisPrint = (WorkticketHisForPrint) WHisit
						.next();
				String approveStatusId = checkString(workHisPrint
						.getApproveStatusId());
				// 已签发
				if (Constant.STATUS_SIGN.equals(approveStatusId)) {
					energBean.setSignMan(checkString(workHisPrint
							.getApproveMan()));
					energBean.setSignDate(commUtils.formatTime(workHisPrint
							.getApproveDate()));
				}
				// 被判定为点检状态
				if ("17".equals(approveStatusId)) {
					// 点检签发人
					energBean.setCheckSignBy(workHisPrint.getApproveMan());
					energBean.setCheckDate(workHisPrint.getApproveDate());
				}
				// 已接票
				if (Constant.STATUS_ACCEPT.equals(approveStatusId)) {
					energBean.setAcceptBy(checkString(workHisPrint
							.getApproveMan()));
					energBean.setAcceptDate(commUtils.formatTime(workHisPrint
							.getApproveDate()));
				}
				// 已批准
				if (Constant.STATUS_APPROVE.equals(approveStatusId)) {
					energBean.setChargeBy(checkString(workBasePrint
							.getChargeBy()));
					// energBean.setApprovedMan(checkString(workHisPrint
					// .getApproveMan()));
					// energBean.setApprovedStartDate(commUtils
					// .formatTime(workHisPrint.getApproveDate()));
					// // 批准工作结束时间
					// energBean.setApprovedFinishDate(commUtils.formatTime(workBasePrint
					// .getApprovedFinishDate()));
				}
				// 已许可
				if ("permit".equals(approveStatusId)) {
					// 工作许可人
					energBean.setAdmissionMan(checkString(workHisPrint
							.getApproveMan()));
					// 许可时间
					energBean.setAdmissionDate(commUtils
							.formatTime(workHisPrint.getOldApprovedFinishDate()));
					// 运行班负责人
					energBean.setWatchCharge(workHisPrint.getDutyChargeBy());
					// 工作负责人
					energBean.setAdmissionChargeBy(workHisPrint.getOldChargeBy());
				}
				// 被判定为工作已结束状态
				if (Constant.STATUS_END.equals(approveStatusId)) {

					// 工作结束时间
					String actualEndDate = workHisPrint.getOldApprovedFinishDate();
					if (actualEndDate != null) {
						energBean.setActualEndDate(commUtils
								.formatTime(actualEndDate));
					}
					// 工作许可人
					energBean.setActualEndMan(commUtils
							.checkString(workHisPrint.getFireBy()));

					// TODO 工作负责人
					energBean.setEndChargeBy(workHisPrint.getOldChargeBy());
					// 点检验收人
					energBean.setCheckBy(workHisPrint.getNewChargeBy());
					// 票面显示已结束
					energBean.setContentFlg(false);
				}
				// 判断工作票是否作废
				if ("14".equals(approveStatusId)) {
					// 票面显示已作废
					energBean.setDelete(false);
				}
			}
		}
		return energBean;
	}

	public String checkString(String s) {
		if (s != null) {
			return s;
		}
		return " ";
	}
}
