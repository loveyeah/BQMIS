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
import power.web.birt.bean.bqmis.EnergBean;
import power.web.birt.bean.bqmis.EnergDetailBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;

/**
 * 热力机械，热控票报表填充数据类
 * 
 * @author LiuYingwen
 * @version 1.0
 */
public class Energ {

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
	public Energ() {
		remote = (WorkticketPrint) factory
				.getFacadeRemote("WorkticketPrintImp");
	}

	/**
	 * 热力机械,热控工作票所需数据
	 * 
	 * @return energBean 热力机械,热控工作票所需数据
	 */
	public EnergBean setEnergBean(String no, String flag) {

		EnergBean energBean = new EnergBean();
		int dealSort = 0;
		int resumeSort = 0;
		workticketNo = no;
		energBean.setWorkticketNo(workticketNo);
		// 调用EJB获得所需的所有数据
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo(workticketNo);
		// 基本信息
		WorkticketBaseForPrint workBasePrint = model.getBase();
		// 安全措施列表
		List<SecurityMeasureForPrint> securityPrintList = model.getSafety();
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
					.getEquAttributeName()));
			// 班组
			energBean.setChargeDept(checkString(workBasePrint.getChargeDept()));
			energBean.setRepairSpecail(workBasePrint.getRepairSpecail());
			// 工作班成员
			String members = checkString(workBasePrint.getMembers());
//			energBean.setMembers(members);
			// 工作地点
			energBean.setWorkSpace(workBasePrint.getLocationName());
			// 工作内容
			String workticketContent = commUtils.replaceWithBR(workBasePrint.getWorkticketContent());
			energBean.setWorkticketContent(workticketContent);
			// 热力机械工作票
			if ("1".equals(flag)) {
				energBean.setMembers(members);
//				if (commUtils.countLineTotal(workticketContent,
//						Constant.WORK_CONTENT_LINE_MAX_TWO) <= 6) {
//
//					energBean.setWorkticketContent(workticketContent);
//				} else {
//					energBean.setWorkticketContent(Constant.MEETFUYE);
//					energBean.setWorkticketContentTwo(workticketContent);
//					energBean.setContentFlg(false);
//				}
				// 热控工作票
			} else {
//				energBean.setMembers(members);
				if (members.length() > Constant.ControlMemLCount) {
					energBean.setMembers(members.substring(0,
							Constant.ControlMemLCount));
					energBean.setMembersTwo(members
							.substring(Constant.ControlMemLCount));
				} else {
					energBean.setMembers(members);
				}
//				if (commUtils.countLineTotal(workticketContent,
//						Constant.WORK_CONTENT_LINE_MAX_TWO) <= 6) {

//					energBean.setWorkticketContent(workticketContent);
//				} else {
//					energBean.setWorkticketContentOne(Constant.MEETFUYE);
//					energBean.setWorkticketContentTwo(workticketContent);
//					energBean.setContentFlg(false);
//				}

			}
			// 工作班成员数
			energBean.setMemberCount(checkString(workBasePrint.getMemberCount()));
			// 计划工作开始时间
			energBean.setPlanStartDate(commUtils.formatTime(workBasePrint
					.getPlanStartDate()));
			// 计划工作结束时间
			energBean.setPlanEndDate(commUtils.formatTime(workBasePrint
					.getPlanEndDate()));
			//需要退出热工保护或自动装置名称
			energBean.setNeedEquipmentName(workBasePrint.getAutoDeviceName());
			
			// 备注
			energBean.setWorkticketMemo(workBasePrint.getWorkticketMemo());
			// 工作终结时间
			String actualEndDate = commUtils.formatTime(workBasePrint
					.getActualEndDate());
		}
		// 安全措施列表的设置
		if (securityPrintList != null) {
			Iterator<SecurityMeasureForPrint> secIt = securityPrintList.iterator();
			List<EnergDetailBean> energList = new ArrayList<EnergDetailBean>();
			String safetyOne = "";
			String executeOne = "";
			String safetyOneFlag = "";
			String executeOneFlag = "";
			int safetyOneCount = 0;
			String safetyTwo = "";
			String executeTwo = "";
			String safetyTwoFlag = "";
			String executeTwoFlag = "";
			int safetyTwoCount = 0;
			String safetyThree = "";
			String executeThree = "";
			String safetyThreeFlag = "";
			String executeThreeFlag = "";
			int safetyThreeCount = 0;
			String safetyFour = "";
			String executeFour = "";
			String safetyFourFlag = "";
			String executeFourFlag = "";
			int safetyFourCount = 0;
			String safetyFive = "";
			String executeFive = "";
			String safetyFiveFlag = "";
			String executeFiveFlag = "";
			int safetyFiveCount = 0;
			String safetyContentRepair = "";
			String executeContentRepair = "";
			String safetyContentRepairFlag = "";
			String executeContentRepairFlag = "";
			int safetyRepairCount = 0;
			while (secIt.hasNext()) {
				SecurityMeasureForPrint secMeas = (SecurityMeasureForPrint) secIt
						.next();
				String safetyCode = checkString(secMeas.getSafetyCode());
				// 热力机械工作票
				if ("1".equals(flag)) {
					// 5、（1）应拉开下列开关、刀闸和保险等，并在操作把手（按钮）上设置“禁止合闸，有人工作”警告牌
					if (Constant.MACHINE_SAFETY_ONE.equals(safetyCode)) {
						safetyOneCount ++;
						// energBean.setSafetyOne(checkString(secMeas.getSafetyContent()));
						if(safetyOneCount < 3) {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyOne += "("+safetyOneCount+")"+safeContent;
								safetyOne += Constant.HTML_CHANGE_LINE;
								// //用safetyExeContent替换了safetyContent
								// String safeContent =
								// commUtils.replaceWithBR(secMeas.getSafetyExeContent());
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								executeOne += "("+safetyOneCount+")"+safeExeContent;
								executeOne += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
//								if(safeContent.length()>Constant.ControlTentLCount) {
//									safetyOneFlag += "("+safetyOneCount+")"+safeContent.substring(0, Constant.ControlTentLCount)
//													+Constant.HTML_CHANGE_LINE +safeContent.substring(Constant.ControlTentLCount, safeContent.length());
//								} else {
								safetyOneFlag += "("+safetyOneCount+")"+safeContent;
//								}
								safetyOneFlag += Constant.HTML_CHANGE_LINE;
								// //用safetyExeContent替换了safetyContent
								// String safeContent =
								// commUtils.replaceWithBR(secMeas.getSafetyExeContent());
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								if(secMeas.getSafetyContent().length()>50) {
									executeOneFlag += Constant.HTML_CHANGE_LINE;
								}
								executeOneFlag += "("+safetyOneCount+")"+safeExeContent;
								executeOneFlag += Constant.HTML_CHANGE_LINE;
							}
//							if(safetyOneCount == 4) {
//								EnergDetailBean energDetailBeanOne = new EnergDetailBean();
//								energDetailBeanOne.setSafeMethod(safetyOneFlag);
//								energDetailBeanOne.setExcuteResult(executeOneFlag);
//								energList.add(energDetailBeanOne);
//								safetyOneFlag = "";
//								executeOneFlag = "";
//							} else {
//								EnergDetailBean energDetailBeanOne = new EnergDetailBean();
//								energDetailBeanOne.setSafeMethod(safetyOneFlag);
//								energDetailBeanOne.setExcuteResult(executeOneFlag);
//								energList.add(energDetailBeanOne);
//								safetyOneFlag = "";
//								executeOneFlag = "";
//							}
						}
					}
					// 5、（2）应开启下列阀门、挡板（闸板），使燃烧室、管道、容器内余汽、水、油、灰、烟排放尽，并将温度降至规程规定值：
					if (Constant.MACHINE_SAFETY_Two.equals(safetyCode)) {
						safetyTwoCount ++;
						// energBean.setSafetyTwo(checkString(secMeas.getSafetyContent()));
						if(safetyTwoCount < 3) {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyTwo += "("+safetyTwoCount+")"+safeContent;
								safetyTwo += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								executeTwo += "("+safetyTwoCount+")"+safeExeContent;
								executeTwo += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyTwoFlag += "("+safetyTwoCount+")"+safeContent;
								safetyTwoFlag += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								if(secMeas.getSafetyContent().length()>49) {
									executeTwoFlag += Constant.HTML_CHANGE_LINE;
								}
								String safeExeContent = secMeas.getSafetyExeContent();
								executeTwoFlag += "("+safetyTwoCount+")"+safeExeContent;
								executeTwoFlag += Constant.HTML_CHANGE_LINE;
							}
//							EnergDetailBean energDetailBeanTwo = new EnergDetailBean();
//							energDetailBeanTwo.setSafeMethod(safetyTwoFlag);
//							energDetailBeanTwo.setExcuteResult(executeTwoFlag);
//							energList.add(energDetailBeanTwo);
//							safetyTwoFlag = "";
//							executeTwoFlag = "";
						}	
					}
					// 5、（3）应关闭下列截门、挡板（闸板），并挂“禁止操作”警告牌：
					if (Constant.MACHINE_SAFETY_Three.equals(safetyCode)) {
						safetyThreeCount ++;
						// energBean.setSafetyThree(checkString(secMeas.getSafetyContent()));
						if(safetyThreeCount < 3) {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyThree += "("+safetyThreeCount+")"+safeContent;
								safetyThree += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								executeThree += "("+safetyThreeCount+")"+safeExeContent;
								executeThree += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyThreeFlag += "("+safetyThreeCount+")"+safeContent;
								safetyThreeFlag += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								if(secMeas.getSafetyContent().length()>52) {
									executeThreeFlag += Constant.HTML_CHANGE_LINE;
								}
								String safeExeContent = secMeas.getSafetyExeContent();
								executeThreeFlag += "("+safetyThreeCount+")"+safeExeContent;
								executeThreeFlag += Constant.HTML_CHANGE_LINE;
							}
//							EnergDetailBean energDetailBeanThree = new EnergDetailBean();
//							energDetailBeanThree.setSafeMethod(safetyThreeFlag);
//							energDetailBeanThree.setExcuteResult(safetyThreeFlag);
//							energList.add(energDetailBeanThree);
//							safetyThreeFlag = "";
//							safetyThreeFlag = "";
						}	
					}
					// 5、（4）应将下列截门停电、加锁：
					if (Constant.MACHINE_SAFETY_Four.equals(safetyCode)) {
						safetyFourCount ++;
						// energBean.setSafetyFour(checkString(secMeas.getSafetyContent()));
						if(safetyFourCount < 3 ) {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyFour += "("+safetyFourCount+")"+safeContent;
								safetyFour += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								executeFour += "("+safetyFourCount+")"+safeExeContent;
								executeFour += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyFourFlag += "("+safetyFourCount+")"+safeContent;
								safetyFourFlag += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								if(secMeas.getSafetyContent().length()>50) {
									executeFourFlag += Constant.HTML_CHANGE_LINE;
								}
								executeFourFlag += "("+safetyFourCount+")"+safeExeContent;
								executeFourFlag += Constant.HTML_CHANGE_LINE;
							}
//							EnergDetailBean energDetailBeanFour = new EnergDetailBean();
//							energDetailBeanFour.setSafeMethod(safetyFourFlag);
//							energDetailBeanFour.setExcuteResult(executeFourFlag);
//							energList.add(energDetailBeanFour);
//							safetyFourFlag = "";
//							executeFourFlag = "";
						}	
					} 
					// 5、（5）其它安全措施（包括拆开连接法兰或加装堵板）：
					if (Constant.MACHINE_SAFETY_Five.equals(safetyCode)) {
						safetyFiveCount ++;
						// energBean.setSafetyFive(checkString(secMeas.getSafetyContent()));
						if(safetyFiveCount < 3 ) {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyFive += "("+safetyFiveCount+")"+safeContent;
								safetyFive += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								executeFive += "("+safetyFiveCount+")"+safeExeContent;
								executeFive += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyFiveFlag += "("+safetyFiveCount+")"+safeContent;
								safetyFiveFlag += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								if(secMeas.getSafetyContent().length()>50) {
									executeFiveFlag += Constant.HTML_CHANGE_LINE;
								}
								executeFiveFlag += "("+safetyFiveCount+")"+safeExeContent;
								executeFiveFlag += Constant.HTML_CHANGE_LINE;
							}
//							EnergDetailBean energDetailBeanFive = new EnergDetailBean();
//							energDetailBeanFive.setSafeMethod(safetyFiveFlag);
//							energDetailBeanFive.setExcuteResult(executeFiveFlag);
//							energList.add(energDetailBeanFive);
//							safetyFiveFlag = "";
//							executeFiveFlag = "";
						}	
					}
					// 运行值班人员补充安全措施：
					if (Constant.MACHINE_SAFETY_REPAIR.equals(safetyCode)) {
						safetyRepairCount ++;
						// energBean.setSafetyContentRepair(checkString(secMeas.getSafetyContent()));
						if(safetyRepairCount < 3 ) {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyContentRepair += "("+safetyRepairCount+")"+ safeContent;
								safetyContentRepair += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								executeContentRepair += "("+safetyRepairCount+")"+safeExeContent;
								executeContentRepair += Constant.HTML_CHANGE_LINE;
							}
						} else {
							if (secMeas.getSafetyContent() != null) {
								String safeContent = secMeas.getSafetyContent();
								safetyContentRepairFlag += "("+safetyRepairCount+")"+ safeContent;
								safetyContentRepairFlag += Constant.HTML_CHANGE_LINE;
							}
							if (secMeas.getSafetyExeContent() != null) {
								String safeExeContent = secMeas.getSafetyExeContent();
								if(secMeas.getSafetyContent().length()>52) {
									executeContentRepairFlag += Constant.HTML_CHANGE_LINE;
								}
								executeContentRepairFlag += "("+safetyRepairCount+")"+safeExeContent;
								executeContentRepairFlag += Constant.HTML_CHANGE_LINE;
							}
//							EnergDetailBean energDetailBeanAdd = new EnergDetailBean();
//							energDetailBeanAdd.setSafeMethod(safetyContentRepairFlag);
//							energDetailBeanAdd.setExcuteResult(executeContentRepairFlag);
//							energList.add(energDetailBeanAdd);
//							safetyContentRepairFlag = "";
//							executeContentRepairFlag = "";
						}	
					}

					// 热控工作票
				} else {
					// （1）由运行人员执行的有：
					if (Constant.CONTROL_SAFETY_ONE.equals(safetyCode)) {
						safetyOneCount++;
						// energBean.setSafetyOne(checkString(secMeas.getSafetyContent()));
						if (secMeas.getSafetyContent() != null) {
							String safeContent = secMeas.getSafetyContent();
							safetyOne += "("+safetyOneCount+")"+safeContent;
							safetyOne += Constant.HTML_CHANGE_LINE;
						}
						if (secMeas.getSafetyExeContent() != null) {
							String safeExeContent = secMeas.getSafetyExeContent();
							executeOne += "("+safetyOneCount+")"+safeExeContent;
							executeOne += Constant.HTML_CHANGE_LINE;
						}
					}
					// （2）运行值班人员补充的安全措施（工作许可人填写）：
					if (Constant.CONTROL_SAFETY_Two.equals(safetyCode)) {
						safetyRepairCount ++;
						if (secMeas.getSafetyContent() != null) {
							String safeContent = secMeas.getSafetyContent();
							safetyContentRepair += "("+safetyRepairCount+")"+safeContent;
							safetyContentRepair += Constant.HTML_CHANGE_LINE;
						}
						if (secMeas.getSafetyExeContent() != null) {
							String safeExeContent = secMeas.getSafetyExeContent();
							executeContentRepair += "("+safetyRepairCount+")"+safeExeContent;
							executeContentRepair += Constant.HTML_CHANGE_LINE;
						}
					}
					// （3）由工作负责人执行的有：
					if (Constant.CONTROL_SAFETY_Three.equals(safetyCode)) {
						// energBean.setSafetyThree(checkString(secMeas.getSafetyContent()));
						safetyThreeCount ++;
						if (secMeas.getSafetyContent() != null) {
							String safeContent =secMeas.getSafetyContent();
							safetyThree += "("+safetyThreeCount+")"+safeContent;
							safetyThree += Constant.HTML_CHANGE_LINE;
						}
						if (secMeas.getSafetyExeContent() != null) {
							String safeExeContent = secMeas.getSafetyExeContent();
							executeThree += "("+safetyThreeCount+")"+safeExeContent;
							executeThree += Constant.HTML_CHANGE_LINE;
							
						}
					}

				}
			}
			// 安措附页
			if ("1".equals(flag)) {
				if (!"".equals(safetyOneFlag)) {
//					energBean.setSafetyOne(Constant.MEETFUYE);
					EnergDetailBean energDetailBeanTitleOne = new EnergDetailBean();
					energDetailBeanTitleOne.setSafeMethod("<B>（1）应断开下列开关、刀闸和保险等，并在操作把手（按钮）上设置“禁止合闸，有人工作”警告牌</B>");
					energList.add(energDetailBeanTitleOne);
					EnergDetailBean energDetailBeanOne = new EnergDetailBean();
					energDetailBeanOne.setSafeMethod(safetyOneFlag);
					energDetailBeanOne.setExcuteResult(executeOneFlag);
					energList.add(energDetailBeanOne);
				} 
				if("".equals(safetyOne)) {
                	safetyOne = "无。";
                }
				energBean.setSafetyOne(safetyOne);
				energBean.setExecuteRightOne(executeOne);
				
				if (!"".equals(safetyTwoFlag)) {
//					energBean.setSafetyTwo(Constant.MEETFUYE);
					EnergDetailBean energDetailBeanTitleTwo = new EnergDetailBean();
					energDetailBeanTitleTwo.setSafeMethod("<B>（2）应关闭下列截门、挡板（闸板），并挂“禁止操作，有人工作”警告牌</B>");
					energList.add(energDetailBeanTitleTwo);
					EnergDetailBean energDetailBeanTwo = new EnergDetailBean();
					energDetailBeanTwo.setSafeMethod(safetyTwoFlag);
					energDetailBeanTwo.setExcuteResult(executeTwoFlag);
					energList.add(energDetailBeanTwo);
				} 
			    if("".equals(safetyTwo)) {
			    	safetyTwo = "无。";
			    }
				energBean.setSafetyTwo(safetyTwo);
				energBean.setExecuteRightTwo(executeTwo);
				
				if (!"".equals(safetyThreeFlag)) {
//					energBean.setSafetyThree(Constant.MEETFUYE);
					EnergDetailBean energDetailBeanTitleThree = new EnergDetailBean();
					energDetailBeanTitleThree.setSafeMethod("<B>（3）应开启下列阀门、挡板（闸板），使燃烧室、管道、容器内余汽、水、油、灰、烟排放尽，并将温度降至规程规定值：</B>");
					energList.add(energDetailBeanTitleThree);
					EnergDetailBean energDetailBeanThree = new EnergDetailBean();
					energDetailBeanThree.setSafeMethod(safetyThreeFlag);
					energDetailBeanThree.setExcuteResult(executeThreeFlag);
					energList.add(energDetailBeanThree);
				} 
				if("".equals(safetyThree)) {
					safetyThree = "无。";
				}
				energBean.setSafetyThree(safetyThree);
				energBean.setExecuteRightThree(executeThree);
				
				if (!"".equals(safetyFourFlag)) {
//					energBean.setSafetyFour(Constant.MEETFUYE);
					EnergDetailBean energDetailBeanTitleFour = new EnergDetailBean();
					energDetailBeanTitleFour.setSafeMethod("<B>（4）应将下列截门停电、加锁，并挂“禁止操作，有人工作”警告牌：</B>");
					energList.add(energDetailBeanTitleFour);
					EnergDetailBean energDetailBeanFour = new EnergDetailBean();
					energDetailBeanFour.setSafeMethod(safetyFourFlag);
					energDetailBeanFour.setExcuteResult(executeFourFlag);
					energList.add(energDetailBeanFour);
				} 
				if("".equals(safetyFour)) {
					safetyFour = "无。";
				}
				energBean.setSafetyFour(safetyFour);
				energBean.setExecuteRightFour(executeFour);
				
				if (!"".equals(safetyFiveFlag)) {
//					energBean.setSafetyFive(Constant.MEETFUYE);
					EnergDetailBean energDetailBeanTitleFive = new EnergDetailBean();
					energDetailBeanTitleFive.setSafeMethod("<B>（5）其它安全措施：</B>");
					energList.add(energDetailBeanTitleFive);
					EnergDetailBean energDetailBeanFive = new EnergDetailBean();
					energDetailBeanFive.setSafeMethod(safetyFiveFlag);
					energDetailBeanFive.setExcuteResult(executeFiveFlag);
					energList.add(energDetailBeanFive);
				} 
				if("".equals(safetyFive)) {
				    safetyFive = "无。";
				}
				energBean.setSafetyFive(safetyFive);
				energBean.setExecuteRightFive(executeFive);
				
				if (!"".equals(safetyContentRepairFlag)) {
//					energBean.setSafetyContentRepair(Constant.MEETFUYE);
					List<EnergDetailBean> runAddList = new ArrayList<EnergDetailBean>();
					EnergDetailBean energDetailBeanSix = new EnergDetailBean();
					energDetailBeanSix.setSafeMethod(safetyContentRepairFlag);
					energDetailBeanSix.setExcuteResult(executeContentRepairFlag);
					runAddList.add(energDetailBeanSix);
					if(runAddList.size() > 0) {
						energBean.setFuYeFlag(false);
						energBean.setEnergDetailAddList(runAddList);
					} else {
						energBean.setFuYeFlag(true);
					}
				}
				
				energBean.setSafetyContentRepair(safetyContentRepair);
				energBean.setSafetyContentRight(executeContentRepair);
				
				energBean.setEnergDetailList(energList);
				if (energList.size() > 0) {
					energBean.setFuYeFlag(false);
				} else {
					energBean.setFuYeFlag(true);
				}
			} else {
//					if (commUtils.countSubStringNumber(safetyOne,
//							Constant.HTML_CHANGE_LINE) > 4
//							|| commUtils.countSubStringNumber(safetyTwo,
//									Constant.HTML_CHANGE_LINE) > 4) {
//						EnergDetailBean energDetailBeanTitleOne = new EnergDetailBean();
//						energDetailBeanTitleOne.setSafeMethod(commUtils
//								.newLine(Constant.ENERGCONTROL_SAFEONE,
//										Constant.A3_FIFTY));
//						energList.add(energDetailBeanTitleOne);
//					}
//					if (commUtils.countSubStringNumber(safetyOne,
//							Constant.HTML_CHANGE_LINE) > 4) {
//						energBean.setSafetyOne(Constant.MEETFUYE);
//						EnergDetailBean energDetailBeanTitleTwo = new EnergDetailBean();
//						energDetailBeanTitleTwo.setSafeMethod(commUtils
//								.newLine(Constant.ENERGCONTROL_SAFETWO,
//										Constant.A3_FIFTY));
//						energList.add(energDetailBeanTitleTwo);
//						EnergDetailBean energDetailBeanTwo = new EnergDetailBean();
//						energDetailBeanTwo.setSafeMethod(safetyOne);
//						energList.add(energDetailBeanTwo);
//					} else {
				if("".equals(safetyOne)) {
                	safetyOne = "无。";
                }
				energBean.setSafetyOne(safetyOne);
				energBean.setExecuteRightOne(executeOne);
//					}
//					if (commUtils.countSubStringNumber(safetyTwo,
//							Constant.HTML_CHANGE_LINE) > 4) {
//						energBean.setSafetyTwo(Constant.MEETFUYE);
//						EnergDetailBean energDetailBeanTitleThree = new EnergDetailBean();
//						energDetailBeanTitleThree.setSafeMethod(commUtils
//								.newLine(Constant.ENERGCONTROL_SAFETHREE,
//										Constant.A3_FIFTY));
//						energList.add(energDetailBeanTitleThree);
//						EnergDetailBean energDetailBeanThree = new EnergDetailBean();
//						energDetailBeanThree.setSafeMethod(safetyTwo);
//						energList.add(energDetailBeanThree);
//					} else {
//				if("".equals(safetyContentRepair)
//						|| "(1)无。<br>".equals(safetyContentRepair.replace(" ", ""))
//						|| "(1)无安措。<br>".equals(safetyContentRepair.replace(" ", ""))
//						|| "(1)安全措施无。<br>".equals(safetyContentRepair.replace(" ", ""))
//						|| "(1)。<br>".equals(safetyContentRepair.replace(" ", ""))
//						|| "(1)无补充。<br>".equals(safetyContentRepair.replace(" ", ""))
//						|| "(1)无安全措施。<br>".equals(safetyContentRepair.replace(" ", ""))) {
//					safetyContentRepair = "无补充。";
//				}
				energBean.setSafetyContentRepair(safetyContentRepair);
				energBean.setSafetyContentRight(executeContentRepair);
//					}
//					if (commUtils.countSubStringNumber(safetyThree,
//							Constant.HTML_CHANGE_LINE) > 2) {
//						energBean.setSafetyThree(Constant.MEETFUYE);
//						EnergDetailBean energDetailBeanTitleFour = new EnergDetailBean();
//						energDetailBeanTitleFour.setSafeMethod(commUtils
//								.newLine(Constant.ENERGCONTROL_SAFEFOUR,
//										Constant.A3_FIFTY));
//						energList.add(energDetailBeanTitleFour);
//						EnergDetailBean energDetailBeanFour = new EnergDetailBean();
//						energDetailBeanFour.setSafeMethod(safetyThree);
//						energList.add(energDetailBeanFour);
//					} else {
				if("".equals(safetyThree)) {
		        	safetyThree = "无。";
		        }
				energBean.setSafetyThree(safetyThree);
				energBean.setExecuteRightThree(executeThree);
//					}
//					if (commUtils.countSubStringNumber(safetyFour,
//							Constant.HTML_CHANGE_LINE) > 2) {
//						energBean.setSafetyFour(Constant.MEETFUYE);
//						EnergDetailBean energDetailBeanTitleFive = new EnergDetailBean();
//						energDetailBeanTitleFive.setSafeMethod(commUtils
//								.newLine(Constant.ENERGCONTROL_SAFEFIVE,
//										Constant.A3_FIFTY));
//						energList.add(energDetailBeanTitleFive);
//						EnergDetailBean energDetailBeanFive = new EnergDetailBean();
//						energDetailBeanFive.setSafeMethod(safetyFour);
//						energList.add(energDetailBeanFive);
//					} else {
//						energBean.setSafetyFour(safetyFour);
//					}
//					if (commUtils.countSubStringNumber(safetyContentRepair,
//							Constant.HTML_CHANGE_LINE) > 2) {
//						energBean.setSafetyContentRepair(Constant.MEETFUYE);
//						EnergDetailBean energDetailBeanTitleSix = new EnergDetailBean();
//						energDetailBeanTitleSix.setSafeMethod(commUtils
//								.newLine(Constant.ENERGMACHE_EXECU,
//										Constant.A3_FIFTY));
//						energList.add(energDetailBeanTitleSix);
//						EnergDetailBean energDetailBeanSix = new EnergDetailBean();
//						energDetailBeanSix.setSafeMethod(safetyContentRepair);
//						energList.add(energDetailBeanSix);
//					} else {
//						energBean.setSafetyContentRepair(safetyContentRepair);
//					}
//					energBean.setEnergDetailList(energList);
//					if (energList.size() > 0) {
//						energBean.setFuYeFlag(false);
//					} else {
//						energBean.setFuYeFlag(true);
//					}
			}
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
					energBean.setApprovedMan(checkString(workHisPrint
							.getApproveMan()));
					energBean.setApprovedStartDate(commUtils
							.formatTime(workHisPrint.getApproveDate()));
					// 批准工作结束时间
					energBean.setApprovedFinishDate(commUtils.formatTime(workBasePrint
							.getApprovedFinishDate()));
				}
				
				//被判定为已办理状态
				if("6".equals(approveStatusId)) {
					if("".equals(energBean.getExecuteRightOne())
							|| "(1)无。<br>".equals(energBean.getSafetyOne().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyOne().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyOne().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyOne().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyOne().replace(" ", ""))
							|| "(1)无补充。<br>".equals(energBean.getSafetyOne().replace(" ", ""))) {
						energBean.setExecuteRightOne("无。");
					}
					if("".equals(energBean.getExecuteRightTwo())
							|| "(1)无。<br>".equals(energBean.getSafetyTwo().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyTwo().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyTwo().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyTwo().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyTwo().replace(" ", ""))
							|| "(1)无补充。<br>".equals(energBean.getSafetyTwo().replace(" ", ""))) {
						energBean.setExecuteRightTwo("无。");
					}
					if("".equals(energBean.getExecuteRightThree())
							|| "(1)无。<br>".equals(energBean.getSafetyThree().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyThree().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyThree().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyThree().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyThree().replace(" ", ""))
							|| "(1)无补充。<br>".equals(energBean.getSafetyThree().replace(" ", ""))) {
						energBean.setExecuteRightThree("无。");
					}
					if("".equals(energBean.getExecuteRightFour())
							|| "(1)无。<br>".equals(energBean.getSafetyFour().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyFour().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyFour().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyFour().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyFour().replace(" ", ""))
							|| "(1)无补充。<br>".equals(energBean.getSafetyFour().replace(" ", ""))) {
						energBean.setExecuteRightFour("无。");
					}
					if("".equals(energBean.getExecuteRightFive())
							|| "(1)无。<br>".equals(energBean.getSafetyFive().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyFive().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyFive().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyFive().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyFive().replace(" ", ""))
							|| "(1)无补充。<br>".equals(energBean.getSafetyFive().replace(" ", ""))) {
						energBean.setExecuteRightFive("无。");
					}
					if("".equals(energBean.getSafetyContentRepair())
							|| "(1)无。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)无补充。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))) {
						energBean.setSafetyContentRepair("无补充。");
					}
					if("".equals(energBean.getSafetyContentRight())
							|| "(1)无。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)无安措。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)安全措施无。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "无补充。".equals(energBean.getSafetyContentRepair().replace(" ", ""))
							|| "(1)无安全措施。<br>".equals(energBean.getSafetyContentRepair().replace(" ", ""))) {
						energBean.setSafetyContentRight("无补充。");
					}
				}
				
				// 已许可
				if ("permit".equals(approveStatusId)) {
					//工作许可人
					energBean.setAdmissionMan(checkString(workHisPrint
							.getApproveMan()));
					//许可时间
					energBean.setAdmissionDate(commUtils
							.formatTime(workHisPrint.getOldApprovedFinishDate()));
					//运行班负责人
					energBean.setUnitChargeBy(workHisPrint.getDutyChargeBy());
					//工作负责人
					energBean.setAdmissionChargeBy(workHisPrint.getOldChargeBy());
				}
				// 变更
				if ("changecharge".equals(approveStatusId)) {
					energBean.setNewChargeBy(checkString(workHisPrint
							.getNewChargeBy()));
					energBean.setModifyMan(checkString(workHisPrint
							.getApproveMan()));
					energBean.setModifyDate(commUtils.formatTime(workHisPrint
							.getOldApprovedFinishDate()));
					//运行班负责人
					energBean.setRunWatchChargeBy2(workHisPrint.getDutyChargeBy());
				}
				// 延期
				if ("delay".equals(approveStatusId)) {
					//值长
					energBean.setNewApprovedFinishMan(checkString(workHisPrint
							.getNewChargeBy()));
					energBean.setNewApprovedFinishDate(commUtils.formatTime(workHisPrint.getNewApprovedFinishDate()));
					//运行班负责人
					energBean.setRunWatchChargeBy3(workHisPrint.getDutyChargeBy());
					//工作负责人
					energBean.setChargeByDelay(workHisPrint.getOldChargeBy());
				}
				// 已交回
				if ("return".equals(approveStatusId)) {
					dealSort++;
					// 已交回1
					if (dealSort == 1) {
						energBean.setDealBackManOne(checkString(workHisPrint
								.getNewChargeBy()));
						energBean.setDealBackDateOne(commUtils
								.formatTime(workHisPrint.getOldApprovedFinishDate()));
						energBean.setRunChargeBy1(workHisPrint.getOldChargeBy());
					}
					// 已交回2
					if (dealSort == 2) {
						energBean.setDealBackManTwo(checkString(workHisPrint
								.getNewChargeBy()));
						energBean.setDealBackDateTwo(commUtils
								.formatTime(workHisPrint.getOldApprovedFinishDate()));
						energBean.setRunChargeBy2(workHisPrint.getOldChargeBy());
					}
					// 已交回3
					if (dealSort == 3) {
						energBean.setDealBackManThree(checkString(workHisPrint
								.getNewChargeBy()));
						energBean.setDealBackDateThree(commUtils
								.formatTime(workHisPrint.getOldApprovedFinishDate()));
						energBean.setRunChargeBy3(workHisPrint.getOldChargeBy());
					}
				}
				// 已恢复
				if ("back".equals(approveStatusId)) {
					resumeSort++;
					// 已恢复1
					if (resumeSort == 1) {
						energBean.setResumeManOne(checkString(workHisPrint
								.getNewChargeBy()));
						energBean.setResumeDateOne(commUtils
								.formatTime(workHisPrint.getOldApprovedFinishDate()));
						energBean.setRecoveryChargeBy1(workHisPrint.getOldChargeBy());
					}
					// 已恢复2
					if (resumeSort == 2) {
						energBean.setResumeManTwo(checkString(workHisPrint
								.getNewChargeBy()));
						energBean.setResumeDateTwo(commUtils
								.formatTime(workHisPrint.getOldApprovedFinishDate()));
						energBean.setRecoveryChargeBy2(workHisPrint.getOldChargeBy());
					}
					// 已恢复3
					if (resumeSort == 3) {
						energBean.setResumeManThree(checkString(workHisPrint
								.getNewChargeBy()));
						energBean.setResumeDateThree(commUtils
								.formatTime(workHisPrint.getOldApprovedFinishDate()));
						energBean.setRecoveryChargeBy3(workHisPrint.getOldChargeBy());
					}
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
					energBean.setActualEndMan(commUtils.checkString(workHisPrint
							.getFireBy()));

					// TODO 工作负责人
					energBean.setEndChargeBy(workHisPrint.getOldChargeBy());
					//点检验收人
					energBean.setCheckBy(workHisPrint.getNewChargeBy());
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
