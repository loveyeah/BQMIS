/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketPrint;
import power.ejb.workticket.form.SecurityMeasureForPrint;
import power.ejb.workticket.form.WorkticketBaseForPrint;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketPrintModel;
import power.web.birt.bean.bqmis.ElectricTwoBean;
import power.web.birt.bean.bqmis.ElectricTwoDetailBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;

/**
 * 电气二种票
 * 
 * @author LiuYingwen
 * 
 */
public class ElectricTwo {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
    public static Ejb3Factory factory;
	
	static{
		factory = Ejb3Factory.getInstance();
	}

	/** 电气票二种 */
	private WorkticketPrint remote;
	/** 工作票号 */
	private String workticketNo;

	/**
	 * 构造函数
	 */
	public ElectricTwo() {
		remote = (WorkticketPrint) factory
				.getFacadeRemote("WorkticketPrintImp");
	}

	/**
	 * 电气二工作票所需数据
	 * 
	 * @return entityBean 电气二工作票所需数据
	 */
	public ElectricTwoBean setElectricTwoBean(String no) {

		ElectricTwoBean entity = new ElectricTwoBean();
		this.setWorkticketNo(no);
		int dealSort = 0;
		int resumeSort = 0;
		// 调用EJB获得所需的所有数据
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo(workticketNo);
		// 基本信息
		WorkticketBaseForPrint workBasePrint = model.getBase();
		// 安全措施列表
		List<SecurityMeasureForPrint> securityPrintList = model.getSafety();
		// 审批列表
		List<WorkticketHisForPrint> workHisPrintList = model.getHistory();
		// 标题信息
//		entity.setTitle21(Constant.SECURITY_TITLE_21);
//		entity.setTitle211(Constant.EXCUTE_TITLE);
//		entity.setTitle22(Constant.SECURITY_TITLE_22);
//		entity.setTitle221(Constant.EXCUTE_TITLE);
		
		
		// 工作票号
		entity.setWorkticketNo(no);
		// 机组名
		entity.setMachineClass(commUtils.checkString(workBasePrint
				.getRepairSpecail()));
		// 工作负责人
		entity.setChargeBy(commUtils.checkString(workBasePrint.getChargeBy()));
		// 工作监护人
		entity.setWorkWacth(workBasePrint.getWatcher());
		// 班组
		entity.setEquAttributeName(commUtils.checkString(workBasePrint.getChargeDept()));
				
		// 部门
//		entity.setChargeDept(commUtils.checkString(workBasePrint
//				.getChargeDept()));
		// TODO 工作班成员
		String members = commUtils.checkString(workBasePrint.getMembers());
		if (members != null) {
			if (members.length() >= Constant.TLineMemberCount) {
				entity.setMembers(members
						.substring(0, Constant.TLineMemberCount));

			}
			else
			{
				entity.setMembers(members);
				
			}
			// 工作班成员数
			entity.setMemberCount(commUtils.checkString(workBasePrint
					.getMemberCount()));
			// 工作点名称及设备双重名称
			//entity.setPlaceAndMachineName(commUtils.checkString(workBasePrint
					//.getWorkticketContent()));
			
			//工作地点
			entity.setWorkPlace(workBasePrint.getLocationName());
			// TODO 工作内容
			String workticketContent = commUtils.replaceWithBR(workBasePrint.getWorkticketContent());
			entity.setWorkTask(workticketContent);
//			if (workticketContent != null) {
//				if (commUtils.countLineTotal(workticketContent,
//						Constant.WORK_CONTENT_LINE_MAX_TWO)<=6) {
//					entity.setWorkTask(commUtils
//							.replaceWithBR(workticketContent));
//				} else{
//					entity.setWorkTask(Constant.MEETFUYE);
//					entity.setContentFlg(false);
//					entity.setWorkPlaceTwo(commUtils
//							.replaceWithBR(workticketContent));
//				}
//					
//
//			}
			// 计划开始时间
			String planStartDate = commUtils.formatTime(workBasePrint
					.getPlanStartDate());
			String planEndDate = commUtils.formatTime(workBasePrint
					.getPlanEndDate());
			if (planStartDate != null) {
				entity.setPlanStartDate(planStartDate);
			}
			// 计划结束时间
			if (planStartDate != null) {
				entity.setPlanEndDate(planEndDate);
			}
			// 工作条件
			entity.setWorkCondition(workBasePrint.getWorkCondition());

			// 5、安全措施
			if (securityPrintList != null && securityPrintList.size() > 0) {
				Iterator<SecurityMeasureForPrint> it = securityPrintList
						.iterator();
				List<ElectricTwoDetailBean> electirTwoList = new ArrayList<ElectricTwoDetailBean>();
				String safetyOne = "";
				String safetyOneFlag = "";
				String executeOne = "";
				String executeOneFlag = "";
				int countSafeOne = 0;
				while (it.hasNext()) {
					SecurityMeasureForPrint secMea = it.next();
					String safetyCode = secMea.getSafetyCode();
					if (Constant.SAFETY_CODE_21.equals(safetyCode)) {
						countSafeOne++;
						if(countSafeOne < 13) {
							if (secMea.getSafetyContent() != null) {
								String safeContent =secMea.getSafetyContent();
								safetyOne += "("+countSafeOne+")"+safeContent;
								safetyOne += Constant.HTML_CHANGE_LINE;
							}
							if (secMea.getSafetyExeContent() != null) {
								String safeExeContent = secMea.getSafetyExeContent();
								executeOne += "("+countSafeOne+")"+safeExeContent;
								executeOne += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMea.getSafetyContent() != null) {
								String safeContent =secMea.getSafetyContent();
								safetyOneFlag += "("+countSafeOne+")"+safeContent;
								safetyOneFlag += Constant.HTML_CHANGE_LINE;
							}
							if (secMea.getSafetyExeContent() != null) {
								String safeExeContent = secMea.getSafetyExeContent();
								executeOneFlag += "("+countSafeOne+")"+safeExeContent;
								executeOneFlag += Constant.HTML_CHANGE_LINE;
							}
						}
					}
//					if (Constant.SAFETY_CODE_22.equals(safetyCode)) {
//						if (secMea.getSafetyContent() != null) {
//							String safeContent = commUtils.replaceWithBR(secMea
//									.getSafetyContent());
//							safetyTwo += safeContent;
//						}
//						if (secMea.getSafetyContent() != null) {
//							String safeExeContent = commUtils
//									.replaceWithBR(secMea.getSafetyExeContent());
//							executeTwo += safeExeContent;
//						}
//					}

				}
				if (!"".equals(safetyOneFlag)) {
					// entity.setSafetyOneFlag("F");
					// fuYeFlag1 = true;
//					entity.setSafetyOne(Constant.MEETFUYE);
//					entity.setExecuteOne(Constant.MEETFUYE);
					entity.setFuYeFlag(false);
//					ElectricTwoDetailBean electriTwoDetailBeenTitle = new ElectricTwoDetailBean();
//					electriTwoDetailBeenTitle.setSafeMethod(Constant.ELECTIRONE_SAFEONE);
//					electriTwoDetailBeenTitle.setExecuMethod(Constant.ELECTIRONE_EXECUONE);
//					electirTwoList.add(electriTwoDetailBeenTitle);
					ElectricTwoDetailBean electriTwoDetailBeen = new ElectricTwoDetailBean();
					electriTwoDetailBeen.setSafeMethod(safetyOneFlag);
					electriTwoDetailBeen.setExecuMethod(executeOneFlag);
					electirTwoList.add(electriTwoDetailBeen);
				} 
//				if(!"".equals(safetyOne)) {
				if("".equals(safetyOne)) {
					safetyOne = "无。";
				}
				entity.setSafetyOne(safetyOne);
				entity.setExecuteOne(executeOne);
//				}
//				if (commUtils.countLineTotal(safetyTwo,
//						Constant.ELECTWO_SAFETY_LINE_MAX) > 10
//						|| commUtils.countLineTotal(executeTwo,
//								Constant.ELECTWO_SAFETY_LINE_MAX) > 10) {
//					// entity.setSafetyTwoFlag("F");
//					// fuYeFlag2 = true;
//					entity.setSafetyTwo(Constant.MEETFUYE);
//					entity.setExecuteTwo(Constant.MEETFUYE);
//					ElectricTwoDetailBean electriTwoDetailBeenTitle = new ElectricTwoDetailBean();
//					electriTwoDetailBeenTitle.setSafeMethod(Constant.ELECTIRONE_SAFETWO);
//					electriTwoDetailBeenTitle.setExecuMethod(Constant.ELECTIRONE_EXECUTWO);
//					electirTwoList.add(electriTwoDetailBeenTitle);
//					ElectricTwoDetailBean electriTwoDetailBeen = new ElectricTwoDetailBean();
//					electriTwoDetailBeen.setSafeMethod(safetyTwo);
//					electriTwoDetailBeen.setExecuMethod(executeTwo);
//					electirTwoList.add(electriTwoDetailBeen);
//				} else {
//					// entity.setSafetyTwoFlag("T");
//					entity.setSafetyTwo(safetyTwo);
//					entity.setExecuteTwo(executeTwo);
//				}
				entity.setElectricTwoDetailList(electirTwoList);
				// if(fuYeFlag1==true||fuYeFlag2==true||fuYeFlag3==true||fuYeFlag4==true){
				if (electirTwoList.size() > 0) {
					entity.setFuYeFlag(false);
				} else {
					entity.setFuYeFlag(true);
				}
			}

			if ((workHisPrintList != null) && (workHisPrintList.size() > 0)) {
				WorkticketHisForPrint printSource = null;
				for (int i = 0; i < workHisPrintList.size(); i++) {
					printSource = workHisPrintList.get(i);
					if (printSource != null) {
						// 被判定为已接票状态
						if (Constant.STATUS_ACCEPT.equals(printSource
								.getApproveStatusId())) {
							entity.setAcceptDate(commUtils
									.formatTime(printSource.getApproveDate()));
							entity.setApproveMan4(commUtils
									.checkString(printSource.getApproveMan()));
						}
						// 被判定为已签发状态
						if (Constant.STATUS_SIGN.equals(printSource
								.getApproveStatusId())) {
							entity.setSignDate(commUtils
									.formatTime(printSource.getApproveDate()));
							entity.setSignMan(commUtils
									.checkString(printSource.getApproveMan()));
						}
						// 被判定为点检状态
						if ("17".equals(printSource.getApproveStatusId())) {
							// 点检签发人
							entity.setCheckSignBy(printSource.getApproveMan());
							entity.setCheckDate(printSource.getApproveDate());
						}
						if(Constant.STATUS_ADMISSION.equals(printSource.getApproveStatusId())) {
							if("".equals(entity.getExecuteOne())
									|| "(1)无。<br>".equals(entity.getSafetyOne().replace(" ", ""))
									|| "(1)无安措。<br>".equals(entity.getSafetyOne().replace(" ", ""))
									|| "(1)安全措施无。<br>".equals(entity.getSafetyOne().replace(" ", ""))
									|| "(1)。<br>".equals(entity.getSafetyOne().replace(" ", ""))
									|| "(1)无安全措施。<br>".equals(entity.getSafetyOne().replace(" ", ""))
									|| "(1)无补充。<br>".equals(entity.getSafetyOne().replace(" ", ""))) {
								if("".equals(entity.getExecuteOne())) {
									entity.setExecuteOne("无。");
								} else {
									entity.setExecuteOne(entity.getSafetyOne());
								}
							}
						}
						// 被判定为工作已许可状态
						if ("permit".equals(printSource
								.getApproveStatusId())) {
							// 许可开工时间
							String strPermitDate = commUtils
									.formatTime(printSource.getOldApprovedFinishDate());
							entity.setAdmissionDate(strPermitDate);
							// TODO 值班负责人
							entity.setStartUnitManager(printSource.getDutyChargeBy());
							// 工作许可人(值班员)
							entity.setApproveMan7(commUtils
									.checkString(printSource.getApproveMan()));
							// TODO 工作负责人
							entity.setChargeBy7(printSource.getOldChargeBy());

						}
						// 已交回
						if ("return".equals(printSource.getApproveStatusId())) {
							dealSort++;
							// 已交回1
							if (dealSort == 1) {
								entity.setTestRunDate1(commUtils
										.formatTime(printSource.getOldApprovedFinishDate()));
								entity.setRunLicensor1(printSource
										.getNewChargeBy());
								entity.setRunChargeBy1(printSource.getOldChargeBy());
							}
							// 已交回2
							if (dealSort == 2) {
								entity.setTestRunDate2(commUtils
										.formatTime(printSource.getOldApprovedFinishDate()));
								entity.setRunLicensor2(printSource
										.getNewChargeBy());
								entity.setRunChargeBy2(printSource.getOldChargeBy());
							}
							// 已交回3
							if (dealSort == 3) {
								entity.setTestRunDate3(commUtils
										.formatTime(printSource.getOldApprovedFinishDate()));
								entity.setRunLicensor3(printSource
										.getNewChargeBy());
								entity.setRunChargeBy3(printSource.getOldChargeBy());
							}
						}
						// 已恢复
						if ("back".equals(printSource.getApproveStatusId())) {
							resumeSort++;
							// 已恢复1
							if (resumeSort == 1) {
								entity.setRecoveryWorkDate1(commUtils
										.formatTime(printSource.getOldApprovedFinishDate()));
								entity.setRecoveryLicensor1(printSource
										.getNewChargeBy());
								entity.setRecoveryChargeBy1(printSource.getOldChargeBy());
							}
							// 已恢复2
							if (resumeSort == 2) {
								entity.setRecoveryWorkDate2(commUtils
										.formatTime(printSource.getOldApprovedFinishDate()));
								entity.setRecoveryLicensor2(printSource
										.getNewChargeBy());
								entity.setRecoveryChargeBy2(printSource.getOldChargeBy());
							}
							// 已恢复3
							if (resumeSort == 3) {
								entity.setRecoveryWorkDate3(commUtils
										.formatTime(printSource.getOldApprovedFinishDate()));
								entity.setRecoveryLicensor3(printSource
										.getNewChargeBy());
								entity.setRecoveryChargeBy3(printSource.getOldChargeBy());
							}
						}
						// 被判定为工作已结束状态
						if (Constant.STATUS_END.equals(printSource
								.getApproveStatusId())) {
							// 工作结束时间
							String strEndDate = commUtils
									.formatTime(printSource.getOldApprovedFinishDate());
							entity.setActualEndDate(strEndDate);
							// 工作许可人(值班员)
							entity.setApproveMan8(commUtils
									.checkString(printSource.getFireBy()));
							entity.setRemoveCount(printSource.getBackoutLine());
							// TODO 工作负责人
							entity.setChargeBy8(printSource.getOldChargeBy());
							//点检验收人
							entity.setCheckBy(printSource.getNewChargeBy());
							//共？组已拆除
							entity.setRemoveCount(printSource.getBackoutLine());
							entity.setContentFlg(false);
						}
						// 判断工作票是否作废
						if ("14".equals(printSource.getApproveStatusId())) {
							// 票面显示已作废
							entity.setDelete(false);
						}
					}
				}
				// 备注
				String strWorkticketMemo = workBasePrint.getWorkticketMemo();
				if (strWorkticketMemo != null) {
					if (strWorkticketMemo.length() >= Constant.LineMemberCount) {
						entity.setWorkticketMemoOne(strWorkticketMemo
								.substring(0, Constant.LineMemberCount));
						entity.setWorkticketMemoTwo(strWorkticketMemo
								.substring(Constant.LineMemberCount,
										strWorkticketMemo.length()));
					} else {
						entity.setWorkticketMemoOne(strWorkticketMemo
								.substring(0));
					}
				}
				// 危险点分析（附后）
				entity.setDangerContent(workBasePrint.getDangerContent());
				// 工作票考核人

				// 考核情况

				// 考核时间

			}
		}
		return entity;
	}

	/**
	 * 工作票编号取得
	 * 
	 * @return workticketNo
	 */
	public String getWorkticketNo() {
		return workticketNo;
	}

	/**
	 * 工作票编号设置
	 * 
	 * @param argWorkticketNo
	 */
	public void setWorkticketNo(String argWorkticketNo) {
		this.workticketNo = argWorkticketNo;
	}

}
