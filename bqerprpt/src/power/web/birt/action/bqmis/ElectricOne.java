/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.filefilter.SuffixFileFilter;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketPrint;
import power.ejb.workticket.form.SecurityMeasureForPrint;
import power.ejb.workticket.form.WorkticketBaseForPrint;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketPrintModel;
import power.web.birt.bean.bqmis.ElectricOneBean;
import power.web.birt.bean.bqmis.ElectricOneDetailBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;

/**
 * 电气一种票
 * 
 * @author LiuYingwen
 * 
 */
public class ElectricOne {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 电气票一种 */
	private WorkticketPrint remote;
	/** 工作票号 */
	private String workticketNo;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public ElectricOne() {
		remote = (WorkticketPrint) factory
				.getFacadeRemote("WorkticketPrintImp");
	}

	/**
	 * 电气一工作票所需数据
	 * 
	 * @return electricOneBean 电气一工作票所需数据
	 */
	public ElectricOneBean setElectricOneBean(String no) {
		ElectricOneBean entity = new ElectricOneBean();
		int dealSort = 0;
		int resumeSort = 0;
		this.setWorkticketNo(no);
		// 调用EJB获得所需的所有数据
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo(workticketNo);
		// 基本信息
		WorkticketBaseForPrint workBasePrint = model.getBase();
		// 安全措施列表
		List<SecurityMeasureForPrint> securityPrintList = model.getSafety();
		// 审批列表
		List<WorkticketHisForPrint> workHisPrintList = model.getHistory();
		// 工作负责人
		entity.setChargeBy(workBasePrint.getChargeBy());
		// 工作监护人
		entity.setWorkWacth(workBasePrint.getWatcher());
		// 机组
		// modified by liuyi 091231
//		entity.setEquAttributeName(workBasePrint.getEquAttributeName());
		entity.setEquAttributeName(workBasePrint.getRepairSpecail());
		// 班组
		entity.setMachineClass(workBasePrint.getChargeDept());
		entity.setTitle10(Constant.ELECTIRONE_SAFEONE);
		entity.setTitle101(Constant.ELECTIRONE_EXECUONE);
		entity.setTitle11(commUtils.newLine(Constant.ELECTIRONE_SAFETWO,
				Constant.safetyActionLCount));
		entity.setTitle111(Constant.ELECTIRONE_EXECUTWO);
		entity.setTitle12(commUtils.newLine(Constant.ELECTIRONE_SAFETHREE,
				Constant.safetyActionLCount));
		entity.setTitle121(Constant.ELECTIRONE_EXECUTHREE);
		entity.setTitle13(Constant.ELECTIRONE_SAFEFOUR);
		// 工作班成员
		String members = workBasePrint.getMembers();
		if (members != null) {
			if (members.length() >= Constant.TLineMemberCount) {
				entity.setMembersOne(members.substring(0,
						Constant.LineMemberCount));
				entity.setMembersTwo(members.substring(
						Constant.LineMemberCount, Constant.TLineMemberCount));
				entity.setMembersThree(members.substring(
						Constant.TLineMemberCount, members.length()));
			} else if (members.length() >= Constant.LineMemberCount) {
				entity.setMembersOne(members.substring(0,
						Constant.LineMemberCount));
				entity.setMembersTwo(members.substring(
						Constant.LineMemberCount, members.length()));
			} else {
				entity.setMembersOne(members);
			}
		}
		// 工作班成员数
		entity.setMemberCount(workBasePrint.getMemberCount());
		// 工作地点
		entity.setWorkSpace(workBasePrint.getLocationName());
		// TODO 工作内容
		String workticketContent = commUtils.replaceWithBR(workBasePrint
				.getWorkticketContent());
		entity.setWorkticketContentOne(workticketContent);
		// if (workticketContent != null) {
		// if (commUtils.countLineTotal(workticketContent,
		// Constant.WORK_CONTENT_LINE_MAX) <= 7)
		// entity.setWorkticketContentOne(workticketContent);
		// else {
		// entity.setWorkticketContentOne(Constant.MEETFUYE);
		// entity.setContentFlg(false);

		// }
		//
		// }

		// 计划开始时间
		String planStartDate = workBasePrint.getPlanStartDate();
		if (planStartDate != null)
			entity.setPlanStartDate(commUtils.formatTime(planStartDate));
		// 计划结束时间
		String planEndDate = workBasePrint.getPlanEndDate();
		if (planEndDate != null) {
			entity.setPlanEndDate(commUtils.formatTime(planEndDate));
		}
		if (securityPrintList != null) {
			Iterator<SecurityMeasureForPrint> it = securityPrintList.iterator();
			List<ElectricOneDetailBean> electirOneList = new ArrayList<ElectricOneDetailBean>();
			String safetyOne = "";
			String safetyOneFlag = "";
			String safetyTwo = "";
			String safetyTwoFlag = "";
			String executeOne = "";
			String executeOneFlag = "";
			String executeTwo = "";
			String executeTwoFlag = "";
			String safetyThree = "";
			String safetyThreeFlag = "";
			String executeThree = "";
			String executeThreeFlag = "";
			String safetyFour = "";
			String safetyFourFlag = "";
			// 号码
			String strNumber = "";
			// 共 组
			int intNumber = 0;
			int countSafeOne = 0;
			int countSafeTwo = 0;
			int countSafeThree = 0;
			int countSafeFour = 0;
			// boolean safetyOneFlag = false;
			// boolean safetyTwoFlag = false;
			// boolean safetyThreeFlag = false;
			// boolean safetyFourFlag = false;
			// boolean executeOneFlag = false;
			// boolean executeTwoFlag = false;
			// boolean executeThreeFlag = false;
			// boolean fuYeFlag1 = false;
			// boolean fuYeFlag2 = false;
			// boolean fuYeFlag3 = false;
			// boolean fuYeFlag4 = false;
			while (it.hasNext()) {
				SecurityMeasureForPrint secMea = it.next();
				String safetyCode = secMea.getSafetyCode();
				if (Constant.ELECTRIONE_SAFEONE_CODE.equals(safetyCode)) {
					countSafeOne++;
					if (countSafeOne < 7) {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyOne += "(" + countSafeOne + ")" + safeContent;

							safetyOne += Constant.HTML_CHANGE_LINE;
							// safetyOneFlag = true;
						}
						if (secMea.getSafetyExeContent() != null) {
							String safeExeContent = secMea
									.getSafetyExeContent();
							executeOne += "(" + countSafeOne + ")"
									+ safeExeContent;
							executeOne += Constant.HTML_CHANGE_LINE;
							// executeOneFlag = true;
						}
					} else {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyOneFlag += "(" + countSafeOne + ")"
									+ safeContent;

							safetyOneFlag += Constant.HTML_CHANGE_LINE;
							// safetyOneFlag = true;
						}
						if (secMea.getSafetyExeContent() != null) {
							String safeExeContent = secMea
									.getSafetyExeContent();
							executeOneFlag += "(" + countSafeOne + ")"
									+ safeExeContent;
							executeOneFlag += Constant.HTML_CHANGE_LINE;
							// executeOneFlag = true;
						}
					}
				}
				if (Constant.ELECTRIONE_SAFETWO_CODE.equals(safetyCode)) {
					countSafeTwo++;
					if (countSafeTwo < 7) {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyTwo += "(" + countSafeTwo + ")" + safeContent;
							safetyTwo += Constant.HTML_CHANGE_LINE;
							// safetyTwoFlag = true;
						}
						if (secMea.getSafetyExeContent() != null) {
							// TODO 号码
							if (secMea.getMarkCard() != null) {
								strNumber = secMea.getMarkCard().replace(
										"\\n", "<br>");
							}
							// TODO 组数
							if (secMea.getCardCount() != null) {
								intNumber = Integer.parseInt(secMea
										.getCardCount());
							}
							String safeExeContent = secMea
									.getSafetyExeContent();
							executeTwo += "(" + countSafeTwo + ")"
									+ safeExeContent;
							executeTwo += Constant.HTML_CHANGE_LINE;
							// executeTwoFlag = true;
						}
					} else {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyTwoFlag += "(" + countSafeTwo + ")"
									+ safeContent;
							safetyTwoFlag += Constant.HTML_CHANGE_LINE;
							// safetyTwoFlag = true;
						}
						if (secMea.getSafetyExeContent() != null) {
							String safeExeContent = secMea
									.getSafetyExeContent();
							executeTwoFlag += "(" + countSafeTwo + ")"
									+ safeExeContent;
							executeTwoFlag += Constant.HTML_CHANGE_LINE;
							// executeTwoFlag = true;
						}
					}
				}
				if (Constant.ELECTRIONE_SAFETHREE_CODE.equals(safetyCode)) {
					countSafeThree++;
					if (countSafeThree < 4) {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyThree += "(" + countSafeThree + ")"
									+ safeContent;
							safetyThree += Constant.HTML_CHANGE_LINE;
							// safetyThreeFlag = true;
						}
						if (secMea.getSafetyExeContent() != null) {
							String safeExeContent = secMea
									.getSafetyExeContent();
							executeThree += "(" + countSafeThree + ")"
									+ safeExeContent;
							executeThree += Constant.HTML_CHANGE_LINE;
							// executeThreeFlag = true;
						}
					} else {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyThreeFlag += "(" + countSafeThree + ")"
									+ safeContent;
							safetyThreeFlag += Constant.HTML_CHANGE_LINE;
							// safetyThreeFlag = true;
						}
						if (secMea.getSafetyExeContent() != null) {
							String safeExeContent = secMea
									.getSafetyExeContent();
							executeThreeFlag += "(" + countSafeThree + ")"
									+ safeExeContent;
							executeThreeFlag += Constant.HTML_CHANGE_LINE;
							// executeThreeFlag = true;
						}
					}
				}
				// 工作地点保留带电部分和补充安措
				if (Constant.ELECTRIONE_SAFEFOUR_CODE.equals(safetyCode)) {
					countSafeFour++;
					if (countSafeFour < 2) {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyFour += "(" + countSafeFour + ")"
									+ safeContent;
							safetyFour += Constant.HTML_CHANGE_LINE;
							// safetyFourFlag = true;
						}
					} else {
						if (secMea.getSafetyContent() != null) {
							String safeContent = secMea.getSafetyContent();
							safetyFourFlag += "(" + countSafeFour + ")"
									+ safeContent;
							safetyFourFlag += Constant.HTML_CHANGE_LINE;
							// safetyFourFlag = true;
						}
					}
				}
			}
			// TODO 号码
			entity.setNumber(commUtils.removeLastBR(strNumber));
			// TODO 组数
			if ("0".equals(String.valueOf(intNumber))) {
				entity.setNumberCount("");
			} else {
				entity.setNumberCount(String.valueOf(intNumber));
			}
			// if (safetyOneFlag == true) {
			// safetyOne = safetyOne.substring(0, safetyOne.length() - 4);
			// }
			// if (safetyTwoFlag == true) {
			// safetyTwo = safetyTwo.substring(0, safetyTwo.length() - 4);
			// }
			// if (executeOneFlag == true) {
			// executeOne = executeOne.substring(0,
			// executeOne.length() - 4);
			// }
			// if (executeTwoFlag == true) {
			// executeTwo = executeTwo.substring(0,
			// executeTwo.length() - 4);
			// }
			// if (safetyThreeFlag == true) {
			// safetyThree = safetyThree.substring(0,
			// safetyThree.length() - 4);
			// }
			// if (executeThreeFlag == true) {
			// executeThree = executeThree.substring(0, executeThree
			// .length() - 4);
			// }
			// if (safetyFourFlag == true) {
			// safetyFour = safetyFour.substring(0,
			// safetyFour.length() - 4);
			// }
			// //判断安措1是否要见附页
			if (!"".equals(safetyOneFlag)) {
				entity.setFuYeFlag(false);
				// entity.setSafetyOneFlag(false);
				// // fuYeFlag1 = true;
				// entity.setSafetyOne(Constant.MEETFUYE);
				// entity.setExecuteOne(Constant.MEETFUYE);
				// ElectricOneDetailBean electriOneDetailBeenTitle = new
				// ElectricOneDetailBean();
				// electriOneDetailBeenTitle.setSafeMethod(Constant.ELECTIRONE_SAFEONE);
				// electriOneDetailBeenTitle.setExecuMethod(Constant.ELECTIRONE_EXECUONE);
				// electirOneList.add(electriOneDetailBeenTitle);
				// ElectricOneDetailBean electriOneDetailBeen = new
				// ElectricOneDetailBean();
				// electriOneDetailBeen.setSafeMethod(safetyOne);
				// electriOneDetailBeen.setExecuMethod(executeOne);
				// electirOneList.add(electriOneDetailBeen);
				entity.setSafetyOneFuYe(safetyOneFlag);
				entity.setExecuteOneFuYe(executeOneFlag);
			}
			if ("".equals(safetyOne)) {
				safetyOne = "无。";
			}
			entity.setSafetyOne(safetyOne);
			entity.setExecuteOne(executeOne);
			// 判断安措2是否要见附页
			if (!"".equals(safetyTwoFlag)) {
				entity.setFuYeFlag(false);
				// entity.setSafetyTwoFlag("F");
				// fuYeFlag2 = true;
				// entity.setSafetyTwo(Constant.MEETFUYE);
				// entity.setExecuteTwo(Constant.MEETFUYE);
				// ElectricOneDetailBean electriOneDetailBeenTitle = new
				// ElectricOneDetailBean();
				// electriOneDetailBeenTitle.setSafeMethod(commUtils.newLine(
				// Constant.ELECTIRONE_SAFETWO, Constant.A3_FIFTY));
				// electriOneDetailBeenTitle.setExecuMethod(commUtils.newLine(
				// Constant.ELECTIRONE_EXECUTWO, Constant.A3_FIFTY));
				// electirOneList.add(electriOneDetailBeenTitle);
				// ElectricOneDetailBean electriOneDetailBeen = new
				// ElectricOneDetailBean();
				// electriOneDetailBeen.setSafeMethod(safetyTwo);
				// electriOneDetailBeen.setExecuMethod(executeTwo);
				// electirOneList.add(electriOneDetailBeen);
				entity.setSafetyTwoFuYe(safetyTwoFlag);
				entity.setExecuteTwoFuYe(executeTwoFlag);
			}
			if ("".equals(safetyTwo)) {
				safetyTwo = "无。";
			}
			entity.setSafetyTwo(safetyTwo);
			entity.setExecuteTwo(executeTwo);
			// 判断安措3是否要见附页
			if (!"".equals(safetyThreeFlag)) {
				entity.setFuYeFlag(false);
				entity.setSafetyThreeFuYe(safetyThreeFlag);
				entity.setExecuteThreeFuYe(executeThreeFlag);
				// entity.setSafetyTwoFlag("F");
				// fuYeFlag3 = true;
				// entity.setSafetyThree(Constant.MEETFUYE);
				// entity.setExecuteThree(Constant.MEETFUYE);
				// ElectricOneDetailBean electriOneDetailBeenTitle = new
				// ElectricOneDetailBean();
				// electriOneDetailBeenTitle.setSafeMethod(commUtils.newLine(
				// Constant.ELECTIRONE_SAFETHREE, Constant.A3_FIFTY));
				// electriOneDetailBeenTitle.setExecuMethod(commUtils.newLine(
				// Constant.ELECTIRONE_EXECUTHREE, Constant.A3_FIFTY));
				// electirOneList.add(electriOneDetailBeenTitle);
				// ElectricOneDetailBean electriOneDetailBeen = new
				// ElectricOneDetailBean();
				// electriOneDetailBeen.setSafeMethod(safetyThree);
				// electriOneDetailBeen.setExecuMethod(executeThree);
				// electirOneList.add(electriOneDetailBeen);
			}
			if ("".equals(safetyThree)) {
				safetyThree = "无。";
			}
			entity.setSafetyThree(safetyThree);
			entity.setExecuteThree(executeThree);
			// 判断安措4是否要见附页
			if (!"".equals(safetyFourFlag)) {
				// fuYeFlag4 = true;
				entity.setFuYeFlag(false);
				entity.setSupplyFuYe(safetyFourFlag);
				// entity.setSupply(Constant.MEETFUYE);
				// ElectricOneDetailBean electriOneDetailBeenTitle = new
				// ElectricOneDetailBean();
				// electriOneDetailBeenTitle.setSafeMethod(commUtils.newLine(
				// Constant.ELECTIRONE_SAFEFOUR, Constant.A3_FIFTY));
				// electriOneDetailBeenTitle.setExecuMethod("");
				// electirOneList.add(electriOneDetailBeenTitle);
				// ElectricOneDetailBean electriOneDetailBeen = new
				// ElectricOneDetailBean();
				// electriOneDetailBeen.setSafeMethod(safetyFour);
				// electriOneDetailBeen.setExecuMethod("");
				// electirOneList.add(electriOneDetailBeen);
			}
			entity.setSupply(safetyFour);
			// entity.setElectricOneDetailList(electirOneList);
			// if(fuYeFlag1==true||fuYeFlag2==true||fuYeFlag3==true||fuYeFlag4==true){
			// if (electirOneList.size() > 0) {
			// entity.setFuYeFlag(false);
			// } else {
			// entity.setFuYeFlag(true);
			// }

		}

		// TODO 值班负责人（保留措施）
		// entity.setWatchCharge(workBasePrint.getWatcher());
		// 
		// entity.setWatcher(workBasePrint.getWatcher());

		// 批准工作结束时间
		String approvedFinishDate = workBasePrint.getApprovedFinishDate();
		if (approvedFinishDate != null) {
			entity.setApprovedFinishDate(commUtils
					.formatTime(approvedFinishDate));
		}
		if ((workHisPrintList != null) && (workHisPrintList.size() > 0)) {
			WorkticketHisForPrint printSource = null;
			for (int i = 0; i < workHisPrintList.size(); i++) {
				printSource = workHisPrintList.get(i);
				String approveStatusId = printSource.getApproveStatusId();

				if (Constant.STATUS_SIGN.equals(approveStatusId)) {
					// 被判定为已签发状态
					// 工作票签发人
					entity.setSignBy1(printSource.getApproveMan());
					// 签发日期
					entity.setSignDate(printSource.getApproveDate());

				}

				// 被判定为点检状态
				if ("17".equals(approveStatusId)) {
					// 点检签发人
					entity.setCheckSignBy(printSource.getApproveMan());
					entity.setCheckDate(printSource.getApproveDate());
				}

				// 被判定为已接票状态
				if (Constant.STATUS_ACCEPT.equals(approveStatusId)) {
					entity.setReceiveMan(printSource.getApproveMan());
					String acceptDate = printSource.getApproveDate();
					if (acceptDate != null) {
						entity.setAcceptDate(commUtils.formatTime(acceptDate));
					}
				}

				// 被判定为已批准状态
				if (Constant.STATUS_APPROVE.equals(approveStatusId)) {
					// // 批准工作结束时间
					// String approvedStartDate = printSource.getApproveDate();
					// if (approvedStartDate != null)
					// entity.setApprovedFinishDate(commUtils
					// .formatTime(approvedStartDate));
					// 值长
					entity.setApproveMan5(commUtils.checkString(printSource
							.getApproveMan()));

				}

				// 被判定为已办理状态
				if ("6".equals(approveStatusId)) {
					if ("".equals(entity.getExecuteOne())
							|| "(1)无。<br>".equals(entity.getSafetyOne().replace(" ", ""))
							|| "(1)无安措。<br>".equals(entity.getSafetyOne().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(entity.getSafetyOne().replace(" ", ""))
							|| "(1)。<br>".equals(entity.getSafetyOne().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(entity.getSafetyOne().replace(" ", ""))
							|| "(1)无补充。<br>".equals(entity.getSafetyOne().replace(" ", ""))) {
						entity.setExecuteOne("无。");
					}
					if ("".equals(entity.getExecuteTwo())
							|| "(1)无。<br>".equals(entity.getSafetyTwo().replace(" ", ""))
							|| "(1)无安措。<br>".equals(entity.getSafetyTwo().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(entity.getSafetyTwo().replace(" ", ""))
							|| "(1)。<br>".equals(entity.getSafetyTwo().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(entity.getSafetyTwo().replace(" ", ""))
							|| "(1)无补充。<br>".equals(entity.getSafetyTwo().replace(" ", ""))) {
						entity.setExecuteTwo("无。");
					}
					if ("".equals(entity.getExecuteThree())
							|| "(1)无。<br>".equals(entity.getSafetyThree().replace(" ", ""))
							|| "(1)无安措。<br>".equals(entity.getSafetyThree().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(entity.getSafetyThree().replace(" ", ""))
							|| "(1)。<br>".equals(entity.getSafetyThree().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(entity.getSafetyThree().replace(" ", ""))
							|| "(1)无补充。<br>".equals(entity.getSafetyThree().replace(" ", ""))) {
						entity.setExecuteThree("无。");
					}
					if ("".equals(entity.getSupply())
							|| "(1)无。<br>".equals(entity.getSupply().replace(" ", ""))
							|| "(1)无安措。<br>".equals(entity.getSupply().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(entity.getSupply().replace(" ", ""))
							|| "(1)。<br>".equals(entity.getSupply().replace(" ", ""))
							|| "(1)无补充。<br>".equals(entity.getSupply().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(entity.getSupply().replace(" ", ""))) {
						entity.setSupply("无补充。");
					}
				}

				// 被判定为已办理选人状态
				if ("safetyexe".equals(approveStatusId)) {
					// 批准工作结束时间
					// 工作许可人签名 补充措施
					entity.setAdmissionMan(commUtils.checkString(printSource
							.getOldChargeBy()));
					// 值班负责人签名 补充措施
					entity.setWatchCharge(printSource.getDutyChargeBy());
				}

				// 被判定为工作许可状态
				if ("permit".equals(approveStatusId)) {
					// 工作许可人
					entity.setApproveMan7(printSource.getApproveMan());
					// TODO 工作负责人
					entity.setChargeBy7(printSource.getOldChargeBy());
					String admissionDate = printSource
							.getOldApprovedFinishDate();
					if (admissionDate != null) {
						entity.setAdmissionDate(commUtils
								.formatTime(admissionDate));
					}
				}

				// 被判定为工作变更状态
				if ("changecharge".equals(approveStatusId)) {

					// 原工作负责人
					entity.setOldChargeBy(printSource.getOldChargeBy());
					// 新工作负责人
					entity.setNewChargeBy(printSource.getNewChargeBy());
					// 工作负责人变更时间
					String modifyDate = printSource.getOldApprovedFinishDate();
					if (modifyDate != null) {
						entity.setModifyDate(commUtils.formatTime(modifyDate));
					}
					// 工作签发人
					entity.setModifySignMan(commUtils.checkString(printSource
							.getApproveMan()));
					// 值班负责人
					entity.setWatchCharge2(printSource.getDutyChargeBy());
				}

				// 被判定为工作延期状态
				if ("delay".equals(approveStatusId)) {
					// 延期工作批准结束时间
					String newApprovedFinishDate = printSource
							.getNewApprovedFinishDate();
					if (newApprovedFinishDate != null) {
						entity.setNewApprovedFinishDate(commUtils
								.formatTime(newApprovedFinishDate));
					}

					// TODO 工作负责人
					entity.setChargeByDelay(printSource.getOldChargeBy());
					// TODO 运行值班负责人
					entity.setRunWatchCharge(printSource.getDutyChargeBy());
					// TODO 值长
					entity.setWatcher(commUtils.checkString(printSource
							.getNewChargeBy()));
				}

				// 已交回
				if ("return".equals(approveStatusId)) {
					dealSort++;
					// 已交回1
					if (dealSort == 1) {
						entity.setTestRunDate1(commUtils.formatTime(printSource
								.getOldApprovedFinishDate()));
						entity.setRunLicensor1(checkString(printSource
								.getNewChargeBy()));
						entity.setRunChargeBy1(printSource.getOldChargeBy());
					}
					// 已交回2
					if (dealSort == 2) {
						entity.setTestRunDate2(commUtils.formatTime(printSource
								.getOldApprovedFinishDate()));
						entity.setRunLicensor2(checkString(printSource
								.getNewChargeBy()));
						entity.setRunChargeBy2(printSource.getOldChargeBy());
					}
					// 已交回3
					if (dealSort == 3) {
						entity.setTestRunDate3(commUtils.formatTime(printSource
								.getOldApprovedFinishDate()));
						entity.setRunLicensor3(checkString(printSource
								.getNewChargeBy()));
						entity.setRunChargeBy3(printSource.getOldChargeBy());
					}
				}
				// 已恢复
				if ("back".equals(approveStatusId)) {
					resumeSort++;
					// 已恢复1
					if (resumeSort == 1) {
						entity.setRecoveryWorkDate1(commUtils
								.formatTime(printSource
										.getOldApprovedFinishDate()));
						entity.setRecoveryLicensor1(checkString(printSource
								.getNewChargeBy()));
						entity.setRecoveryChargeBy1(printSource
								.getOldChargeBy());
					}
					// 已恢复2
					if (resumeSort == 2) {
						entity.setRecoveryWorkDate2(commUtils
								.formatTime(printSource
										.getOldApprovedFinishDate()));
						entity.setRecoveryLicensor2(checkString(printSource
								.getNewChargeBy()));
						entity.setRecoveryChargeBy2(printSource
								.getOldChargeBy());
					}
					// 已恢复3
					if (resumeSort == 3) {
						entity.setRecoveryWorkDate3(commUtils
								.formatTime(printSource
										.getOldApprovedFinishDate()));
						entity.setRecoveryLicensor3(checkString(printSource
								.getNewChargeBy()));
						entity.setRecoveryChargeBy3(printSource
								.getOldChargeBy());
					}
				}

				// 被判定为工作已结束状态
				if (Constant.STATUS_END.equals(approveStatusId)) {

					// 工作结束时间
					String actualEndDate = printSource
							.getOldApprovedFinishDate();
					if (actualEndDate != null) {
						entity.setActualEndDate(commUtils
								.formatTime(actualEndDate));
					}
					// 工作许可人
					entity.setApproveMan8(commUtils.checkString(printSource
							.getFireBy()));

					// TODO 工作负责人
					entity.setChargeBy8(printSource.getOldChargeBy());
					// 点检验收人
					entity.setCheckBy(printSource.getNewChargeBy());
					// TODO 地线组数
					if (workBasePrint.getMarkCardNum() != null) {
						entity.setLineCount(printSource.getTotalLine());
					}
					// 已拆除
					entity.setRemoveCount(checkString(printSource
							.getBackoutLine()));
					// 未拆除
					entity.setRemainCount(checkString(printSource
							.getNobackoutLine()));
					// 未拆除编号
					entity.setRemainNumber(checkString(printSource
							.getNobackoutNum()));
					// TODO 值班负责人
					entity.setWatchCharge3(checkString(printSource
							.getDutyChargeBy()));
					// 票面显示已结束
					entity.setContentFlg(false);
				}

				// 判断工作票是否作废
				if ("14".equals(approveStatusId)) {
					// 票面显示已作废
					entity.setDelete(false);
				}
			}
		}

		// 备注
		String workticketMemo = workBasePrint.getWorkticketMemo();
		if (workticketMemo != null) {
//			if (workticketMemo.length() >= Constant.LineMemberCount) {
//				entity.setWorkticketMemoOne(workticketMemo.substring(0,
//						Constant.LineMemberCount));
//				entity.setWorkticketMemoTwo(workticketMemo.substring(
//						Constant.LineMemberCount, workticketMemo.length()));
//			} else {
			entity.setWorkticketMemoOne(workticketMemo);
//			}
		}

		entity.setWorkticketNo(no);

		return entity;
	}

	public String getWorkticketNo() {
		return workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	public String checkString(String s) {
		if (s != null) {
			return s;
		}
		return " ";
	}
}
