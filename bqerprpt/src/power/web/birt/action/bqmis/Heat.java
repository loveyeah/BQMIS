/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.RunJWorkticketMeasureData;
import power.ejb.workticket.business.WorkticketPrint;
import power.ejb.workticket.form.DhMeasureInfoForPrint;
import power.ejb.workticket.form.SecurityMeasureForPrint;
import power.ejb.workticket.form.WorkticketBaseForPrint;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketPrintModel;
import power.web.birt.bean.bqmis.HeatBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;

/**
 * 动火一种、二种票数据
 * 
 * @author LiuYingwen
 * @version 1.0
 */
public class Heat {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/** 工作票 */
	private WorkticketPrint remote;
	/** 工作票号 */
	private String workticketNo;

	/**
	 * 构造函数
	 */
	public Heat() {
		remote = (WorkticketPrint) factory
				.getFacadeRemote("WorkticketPrintImp");
	}

	/**
	 * 一级动火,二级动火工作票所需数据
	 * 
	 * @param no
	 *            工作票号
	 * @param flag
	 *            判断一，二级动火
	 * @return heatBean 一级动火,二级动火工作票所需数据
	 */
	public HeatBean setHeatBean(String no, String flag) {
		HeatBean heatBean = new HeatBean();
		workticketNo = no;
		heatBean.setWorkticketNo(workticketNo);
		// 标题基本信息
		heatBean.setSecurityTitle41(Constant.SECURITY_TITLE_41);
		heatBean.setSecurityTitle42(Constant.SECURITY_TITLE_42);
		heatBean.setSecurityTitle43(Constant.SECURITY_TITLE_43);
		heatBean.setSecurityTitle44(Constant.SECURITY_TITLE_44);
		heatBean.setSecurityTitle45(Constant.SECURITY_TITLE_45);
		heatBean.setSecurityTitle46(Constant.SECURITY_TITLE_46);

		// 调用EJB获得所需的所有数据
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo(workticketNo);
		// 基本信息
		WorkticketBaseForPrint workBasePrint = model.getBase();
		// 安全措施列表
		List<SecurityMeasureForPrint> securityPrintList = model.getSafety();
		// 审批列表
		List<WorkticketHisForPrint> workHisPrintList = model.getHistory();
		// 测量信息
		DhMeasureInfoForPrint measure = model.getMeasure();
		// 基本信息的设置
		if (workBasePrint != null) {
			// 工作负责人
			// heatBean.setChargeBy(commUtils.checkString(workBasePrint
			// .getChargeBy()));
			// 动火负责人
			heatBean.setFireChargeBy(commUtils.checkString(workBasePrint
					.getChargeBy()));
			// TODO 动火部门
			heatBean.setDhDept(workBasePrint.getRepairSpecail());
			// 班组
			heatBean.setChargeDept(commUtils.checkString(workBasePrint
					.getChargeDept()));
			// 机组
			heatBean.setEquAttributeName(commUtils.checkString(workBasePrint
					.getEquAttributeName()));
			// 动火地点
			heatBean.setWorkSpace(workBasePrint.getLocationName());
			// 设备名称
			heatBean.setMachineName(workBasePrint.getEquAttributeName());
			// 动火工作内容
			String workticketContent = commUtils.replaceWithBR(workBasePrint
					.getWorkticketContent());
			heatBean.setFireWorkContent(workticketContent);
//			if (commUtils.countLineTotal(workticketContent,
//					Constant.A3_FIFTY * 2) <= Constant.FIRE_CONTENT_COUNT_MAX) {
//				heatBean.setFireWorkContent(workticketContent);
//			} else {
//				heatBean.setFireWorkContent(Constant.MEETFUYE);
//				heatBean.setContentFlg(false);
//				heatBean.setFireWorkContentExtra(workticketContent);
//			}
			// 备注
			heatBean.setWorkticketMemo(workBasePrint.getWorkticketMemo());
			// 危险点分析
			heatBean.setDangerContent(workBasePrint.getDangerContent());
			// TODO 申请动火开始时间
			String planStartDate = commUtils.formatTime(workBasePrint
					.getPlanStartDate());
			heatBean.setPlanStartDate(planStartDate);

			// 申请动火结束时间
			String planEndDate = commUtils.formatTime(workBasePrint
					.getPlanEndDate());
			heatBean.setPlanEndDate(planEndDate);

		}
		// 安全措施列表的设置
		if (securityPrintList != null) {
//			boolean safetyNeed = false;
			String safetyNeedSafetyContent = "";
			int safetyNeedCount = 0;
			String repairNeedSafetyContent = "";
			int repairNeedCount = 0;
			String fireNeedSafetyContent = "";
			int fireNeedCount = 0;
//			List<String> safetyContentList = new ArrayList<String>();
			Iterator<SecurityMeasureForPrint> secIt = securityPrintList.iterator();
			// 一级动火工作票
			if (Constant.FIRE_LEVEL_1.equals(flag)) {
				if (secIt.hasNext()) {
					while (secIt.hasNext()) {
						SecurityMeasureForPrint secMeas = (SecurityMeasureForPrint) secIt.next();
						String safetyCode = secMeas.getSafetyCode();
						if (secMeas.getSafetyContent() != null&& secMeas.getSafetyContent().length() > 0) {
							if (safetyCode.equals(Constant.SAFETY_CODE_42)) {
								safetyNeedCount ++;
								safetyNeedSafetyContent += "("+safetyNeedCount+")"+secMeas.getSafetyContent();
								safetyNeedSafetyContent += Constant.HTML_CHANGE_LINE;
							} else if (safetyCode.equals(Constant.SAFETY_CODE_41)) {
								repairNeedCount ++;
								repairNeedSafetyContent += "("+repairNeedCount+")"+secMeas.getSafetyContent();
								repairNeedSafetyContent += Constant.HTML_CHANGE_LINE;
							} else if (safetyCode.equals(Constant.SAFETY_CODE_43)) {
								fireNeedCount ++;
								fireNeedSafetyContent += "("+fireNeedCount+")"+secMeas.getSafetyContent();
								fireNeedSafetyContent += Constant.HTML_CHANGE_LINE;
							}
						}

					}
				}
				// 运行应采取的安全措施：
				if("".equals(safetyNeedSafetyContent)){
					safetyNeedSafetyContent = "无。";
				}
				heatBean.setSafetyNeedSafetyContent(safetyNeedSafetyContent);
				// 检修应采取的安全措施：
				if("".equals(repairNeedSafetyContent)){
					repairNeedSafetyContent = "无。";
				}
				heatBean.setRepairNeedSafetyContent(repairNeedSafetyContent);
				// 消防队应采取的安全措施：
				if("".equals(fireNeedSafetyContent)){
					fireNeedSafetyContent = "无。";
				}
				heatBean.setFireNeedSafetyContent(fireNeedSafetyContent);
				if (measure != null) {
					heatBean.setMeasureSpace(measure.getMeasureLocation());
					heatBean.setUserMachine(measure.getUseTool());
					heatBean.setCombustibleGas(measure.getCombustibleGas());
					if (measure.getMesureData().size() != 0) {
						List<RunJWorkticketMeasureData> list = measure.getMesureData();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
						for (int i = 0; i < list.size(); i++) {
							if (i == 0) {
								if(list.get(i).getMeasureDate() != null) {
									
									heatBean.setMeasureTime1(sdf.format(list.get(i).getMeasureDate()));
								}
								heatBean.setMeasureValue1(list.get(i)
										.getMeasureData());
								heatBean.setSurveyor1(list.get(i)
										.getMeasureMan());
							} else if (i == 1) {
								if(list.get(i).getMeasureDate() != null) {
									heatBean.setMeasureTime2(sdf.format(list.get(i).getMeasureDate()));
								}
								heatBean.setMeasureValue2(list.get(i)
										.getMeasureData());
								heatBean.setSurveyor2(list.get(i)
										.getMeasureMan());
							} else if (i == 2) {
								if(list.get(i).getMeasureDate() != null) {
									heatBean.setMeasureTime3(sdf.format(list.get(i).getMeasureDate()));
								}
								heatBean.setMeasureValue3(list.get(i)
										.getMeasureData());
								heatBean.setSurveyor3(list.get(i)
										.getMeasureMan());
							} else if (i == 3) {
								if(list.get(i).getMeasureDate() != null) {
									heatBean.setMeasureTime4(sdf.format(list.get(i).getMeasureDate()));
								}
								heatBean.setMeasureValue4(list.get(i)
										.getMeasureData());
								heatBean.setSurveyor4(list.get(i)
										.getMeasureMan());
							} else if (i == 4) {
								if(list.get(i).getMeasureDate() != null) {
									heatBean.setMeasureTime5(sdf.format(list.get(i).getMeasureDate()));
								}
								heatBean.setMeasureValue5(list.get(i)
										.getMeasureData());
								heatBean.setSurveyor5(list.get(i)
										.getMeasureMan());
							}
						}
					}
				}

			}
			// 二级动火工作票
			else {
				if (secIt.hasNext()) {
					while (secIt.hasNext()) {
						SecurityMeasureForPrint secMeas = (SecurityMeasureForPrint) secIt
								.next();
						String safetyCode = secMeas.getSafetyCode();
						if (secMeas.getSafetyContent() != null
								&& secMeas.getSafetyContent().length() > 0) {
							if ("62".equals(safetyCode)) {
								safetyNeedCount ++;
								safetyNeedSafetyContent += "("+safetyNeedCount+")"+secMeas.getSafetyContent();
								safetyNeedSafetyContent += Constant.HTML_CHANGE_LINE;
							} else if ("61".equals(safetyCode)) {
								repairNeedCount ++;
								repairNeedSafetyContent += "("+repairNeedCount+")"+secMeas.getSafetyContent();
								repairNeedSafetyContent += Constant.HTML_CHANGE_LINE;
							}
						}

					}
				}
				// 运行应采取的安全措施：
//				if (commUtils.countLineTotal(safetyNeedSafetyContent,
//						Constant.SAFETY_Content_LINE_WORD_COUNT_TWO) > Constant.SAFETY_NEED_MAX_ROW_COUNT_TWO) {
//					heatBean.setSafetyNeedSafetyContent(Constant.MEETFUYE);
////					safetyNeed = true;
//				} else {
				if("".equals(safetyNeedSafetyContent)){
					safetyNeedSafetyContent = "无。";
				}
				heatBean.setSafetyNeedSafetyContent(safetyNeedSafetyContent);
//				}
				// 检修应采取的安全措施：
//				if (commUtils.countLineTotal(repairNeedSafetyContent,
//						Constant.SAFETY_Content_LINE_WORD_COUNT_TWO) > Constant.SAFETY_NEED_MAX_ROW_COUNT_TWO) {
//					heatBean.setRepairNeedSafetyContent(Constant.MEETFUYE);
////					safetyNeed = true;
//				} else {
				if("".equals(repairNeedSafetyContent)){
					repairNeedSafetyContent = "无。";
				}
				heatBean.setRepairNeedSafetyContent(repairNeedSafetyContent);
//				}

			}
		}
		// 审批列表的设置
		if (workHisPrintList != null) {
			Iterator<WorkticketHisForPrint> WHisit = workHisPrintList.iterator();
			while (WHisit.hasNext()) {
				WorkticketHisForPrint workHisPrint = (WorkticketHisForPrint) WHisit
						.next();
				String approveStatusId = workHisPrint.getApproveStatusId();
				// 已签发
				if (Constant.STATUS_SIGN.equals(approveStatusId)) {
					heatBean.setFireSignMan(commUtils.checkString(workHisPrint
							.getApproveMan()));
				}
				// 已许可
				if ("27".equals(approveStatusId)) {
					heatBean.setFireExeStaMan("");
					// 值长
					heatBean.setFireWatcherStaMan(workHisPrint.getApproveMan());
					String admissionDate = commUtils.formatTime(workHisPrint
							.getOldApprovedFinishDate());
					heatBean.setAdmissionDate(admissionDate);
				}
				// 消防部门负责人已审批
				if (Constant.STATUS_FIRECHARGE.equals(approveStatusId)) {
					heatBean.setFireChargeDept(commUtils
							.checkString(workHisPrint.getApproveMan()));
				} 
				if("22".equals(approveStatusId)) {
					heatBean.setFireChargeDept(commUtils
							.checkString(workHisPrint.getApproveMan()));
				}
				// 安监部门已审批
				if (Constant.STATUS_SAFETYCHARGE.equals(approveStatusId)) {
					heatBean.setSafetyChargeDept(commUtils
							.checkString(workHisPrint.getApproveMan()));
				}
				// 厂领导已审批
				if ("23".equals(approveStatusId)) {
					heatBean.setFactoryManager(commUtils
							.checkString(workHisPrint.getApproveMan()));
				}
				// 值长已审批 动火2票状态
				if ("23".equals(approveStatusId)) {
					heatBean.setDutyCharge(workHisPrint.getApproveMan());
				}
				// 班长已审批
				if ("21".equals(approveStatusId)) {
					heatBean.setRunMonitor(commUtils
							.checkString(workHisPrint.getApproveMan()));
				}
				// 值长已审批
				if ("18".equals(approveStatusId)) {
					heatBean.setWatcher(workHisPrint.getApproveMan());
				}
				// 工作负责人安措办理
				if ("24".equals(approveStatusId)) {
					heatBean.setChargeBy(workHisPrint.getApproveMan());
				}
				if ("26".equals(approveStatusId)) {
					heatBean.setFireWatcherMan1(workHisPrint.getApproveMan());
				}
				if ("25".equals(approveStatusId)) {
					heatBean.setApproveMan(workHisPrint.getApproveMan());
				}
				if ("28".equals(approveStatusId)) {
					heatBean.setFireEndAdmissionMan(workHisPrint
							.getApproveMan());
					// 动火结束时间
					String fireEndDate = commUtils.formatTime(workHisPrint
							.getOldApprovedFinishDate());
					heatBean.setFireEndDate(fireEndDate);
					// 动火执行人
					heatBean.setFireExeEndMan(workHisPrint.getOldChargeBy());
					// 消防监护人
					heatBean.setFireWatcherMan3(workHisPrint.getNewChargeBy());
					// 动火工作负责人
					heatBean.setFireEndChargeBy(workHisPrint.getApproveMan());
					// 许可回填动火执行人
					heatBean.setFireExeStaMan(workHisPrint.getFireBy());
					// 值长
					heatBean.setFireWatcherEndMan(workHisPrint
							.getDutyChargeBy());
					heatBean.setContentFlg(false);
				}
				// 判断工作票是否作废
				if ("14".equals(approveStatusId)) {
					// 票面显示已作废
					heatBean.setDelete(false);
				}
			}
		}
		return heatBean;

	}

}
