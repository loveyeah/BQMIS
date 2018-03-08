/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.web.administration.directoroutquest.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJOutQuest;
import power.ejb.administration.AdJOutQuestFacadeRemote;
import power.ejb.administration.AdJOutQuestFileFacadeRemote;
import power.ejb.administration.AdJOutQuestReaderFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.administration.form.QuestReaderInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 董事会签报申请
 * 
 * @author sufeiyu
 */
@SuppressWarnings("serial")
public class DirectorOutQuestAction extends AbstractAction {

	/**取得当前用户**/
	public Employee employee;
	/**取得内部签报申请实现类**/
	private AdJOutQuestFacadeRemote oRemote;
	/**取得内部签报申请号实现类**/
	private CodeCommonFacadeRemote cRemote;
	/** 获得上次修改时间* */
	public String strUpdateTime;
	/** 获得要增加的数据* */
	public String strAdd;
	/** 获得要更新的数据* */
	public String strUpdate;
	/** 获得要删除的数据* */
	public String strDelete;
	/** *画面参数开始页 */
	public Long start;
	/** 画面参数页面最大值* */
	public Long limit;
	/** 画面签报申请序号* */
	public Long lngId;
	/** 画面签报申请单号* */
	public String strApplyId;
	/** 签报编号* */
	public String strRes;
	/**取得内部签报抄送人实现类**/
	AdJOutQuestReaderFacadeRemote qRemote;
	/**取得内部签报申请附件实现类**/
	AdJOutQuestFileFacadeRemote fRemote;
	/**entityform**/
	AdJOutQuest quest;
    /**抄送人code**/
	String strReaderMan;

	
	
	/** 构造函数* */
	public DirectorOutQuestAction() {
		oRemote = (AdJOutQuestFacadeRemote) factory
				.getFacadeRemote("AdJOutQuestFacade");
		cRemote = (CodeCommonFacadeRemote) factory
				.getFacadeRemote("CodeCommonFacade");
		qRemote = (AdJOutQuestReaderFacadeRemote) factory
		.getFacadeRemote("AdJOutQuestReaderFacade");
		fRemote = (AdJOutQuestFileFacadeRemote) factory
		.getFacadeRemote("AdJOutQuestFileFacade");
	}
	
	/**
	 *  取得所有的董事会申报申请
	 */
	public void getDirectorOutQuest() {
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strWorkerCode = employee.getWorkerCode();
		String strEnterpriseCode = employee.getEnterpriseCode();
		PageObject objResult = new PageObject();

		LogUtil.log("Action:取得全部申报申请开始", Level.INFO, null);

		try {
			// 设置页面参数
			int intStart = Integer.parseInt(String.valueOf(start));
			int intLimit = Integer.parseInt(String.valueOf(limit));

			objResult = oRemote.getDirectorOutQuest(strEnterpriseCode, strWorkerCode, intStart, intLimit);

			// 要写出的数据
			String strRecord = "";
			if (objResult.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(objResult);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action:取得全部申报申请正常结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得全部申报申请异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		} catch (SQLException e) {
			LogUtil.log("Action:取得全部申报申请异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}

	}	

    
    /**
	 * 取得新规的董事会签报申请单号
	 */
    public void getDirectorOutNewApplyId() {
		LogUtil.log("Action:取得新规的内部签报申请单号开始", Level.INFO, null);
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strWorkerCode = employee.getWorkerCode();
		String strWorkerCodeName = employee.getWorkerName();
		String strDeptCode = employee.getDeptCode();

		// 新规的申请单号
		String strNewApplyId = "";
		// 新规的签报编号
		String strRes = "";
		try {
			strNewApplyId = cRemote.getReportAppNoCode("D", strWorkerCode);
			
			strRes = cRemote.getReportNoCode("D", strDeptCode);

			// 所在部门名取得
			String strDeptName = "";
			strDeptName = oRemote.getDeptNameById(strWorkerCode);

			// 编码转换
			StringBuffer JSONStr = new StringBuffer();
			JSONStr.append("[");
			JSONStr.append("{strWorkerCode:'" + strWorkerCode
					+ "', strWorkerCodeName:'" + strWorkerCodeName
					+ "', strRes:'" + strRes
					+ "',deptName:'" + strDeptName + "',applyId:'"
					+ strNewApplyId + "'}");
			JSONStr.append("]");
			write("{\"list\":" + JSONStr + "}");
			LogUtil.log("Action:取得新规的内部签报申请单号正常结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:取得新规的内部签报申请单号异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}
    
    
    
    /**
	 * 弹出窗口保存按下的操作
	 */
	@SuppressWarnings("unchecked")
	public void saveDirectorOutQuost() {

		LogUtil.log("Action:弹出窗口保存按下的操作开始", Level.INFO, null);

		try {
			// 得到当前用户的code
			employee = (Employee) session.getAttribute("employee");
			String strWorkerCode = employee.getWorkerCode();
			String strEnterpriseCode = employee.getEnterpriseCode();
			String strDeptCode = employee.getDeptCode();

			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			try {
				// 事务处理起点
				tx.begin();
				if (quest != null) {
					// 签报申请表
					if ((quest.getId() != null) && !(quest.getId().equals(""))) {
						// 更新时的操作
						AdJOutQuest objUpdate = oRemote.findById(quest.getId());
						objUpdate.setApplyId(strApplyId);
						objUpdate.setApplyMan(strWorkerCode);
						objUpdate.setApplyDate(quest.getApplyDate());
						objUpdate.setCheckedMan(quest.getCheckedMan());
						objUpdate.setApplyTopic(quest.getApplyTopic());
						objUpdate.setApplyText(quest.getApplyText());
						objUpdate.setReportId(strRes);
						objUpdate.setAppType("D");
						objUpdate.setUpdateUser(strWorkerCode);
						objUpdate.setDcmStatus("0");
						objUpdate.setIsUse("Y");
						objUpdate.setEnterpriseCode(strEnterpriseCode);

						oRemote.update(objUpdate, strUpdateTime);
					} else {
						// 增加时的操作
						AdJOutQuest objAdd = new AdJOutQuest();
						objAdd.setId(null);
						objAdd.setApplyId(strApplyId);
						objAdd.setApplyMan(strWorkerCode);
						objAdd.setApplyDate(quest.getApplyDate());
						objAdd.setCheckedMan(quest.getCheckedMan());
						objAdd.setApplyTopic(quest.getApplyTopic());
						objAdd.setApplyText(quest.getApplyText());
						String strRes = cRemote.getReportNoCode("D", strDeptCode);
						objAdd.setReportId(strRes);
						objAdd.setAppType("D");
						objAdd.setUpdateUser(strWorkerCode);
						objAdd.setDcmStatus("0");
						objAdd.setIfRead("N");
						objAdd.setIsUse("Y");
						objAdd.setEnterpriseCode(strEnterpriseCode);

						oRemote.save(objAdd);
					}

					// 签报申请抄送人
					// 删除的数据
					// 转换从前台获取的数据
					Object objDelete = JSONUtil.deserialize(strDelete);
					if (objDelete != null) {
						Map temMap = null;
						QuestReaderInfo objDelData = new QuestReaderInfo();
						if (List.class.isAssignableFrom(objDelete.getClass())) {
							List lstObj = (List) objDelete;
							for (int i = 0; i < lstObj.size(); i++) {
								temMap = (Map) (lstObj.get(i));
								// 设置entity
								objDelData = new QuestReaderInfo();
								// ID
								objDelData = qRemote.findByPhyId(Long
										.parseLong(temMap.get("idReader")
												.toString()));
								// 是否使用
								objDelData.setIsUseReader("N");
								// 修改人
								objDelData.setUpdateUserReader(strWorkerCode);
								// 删除数据
								qRemote.update(objDelData, temMap.get("updateTimeReader")
										.toString());
							}
						}
					}

					// 更新的数据
					// 转换从前台获取的数据
					Object objUpdate = JSONUtil.deserialize(strUpdate);
					if (objUpdate != null) {
						Map temMap = null;
						QuestReaderInfo objUpData = new QuestReaderInfo();
						if (List.class.isAssignableFrom(objUpdate.getClass())) {
							List lstObj = (List) objUpdate;
							for (int i = 0; i < lstObj.size(); i++) {
								temMap = (Map) (lstObj.get(i));
								// 设置entity
								objUpData = new QuestReaderInfo();
								// ID
								objUpData = qRemote.findByPhyId(Long
										.parseLong(temMap.get("idReader")
												.toString()));
								// 抄送人ID
								objUpData.setReadManReader(temMap.get(
										"readManReader").toString());
								// 修改人
								objUpData.setUpdateUserReader(strWorkerCode);
								// 更新数据
								qRemote.update(objUpData, temMap.get("updateTimeReader")
										.toString());
							}
						}
					}

					// 增加数据
					// 转换从前台获取的数据
					Object objAdd = JSONUtil.deserialize(strAdd);
					if (objAdd != null) {
						Map temMap = null;
						QuestReaderInfo objAddData = new QuestReaderInfo();
						if (List.class.isAssignableFrom(objAdd.getClass())) {
							List lstObj = (List) objAdd;
							for (int i = 0; i < lstObj.size(); i++) {
								temMap = (Map) (lstObj.get(i));
								// 设置entity
								objAddData = new QuestReaderInfo();
								// 序号
								objAddData.setIdReader(null);
								// 申请单号
								objAddData.setApplyIdReader(strApplyId);
								// 抄送人ID
								if (temMap.get("readManReader") != null) {
									objAddData.setReadManReader(temMap.get(
											"readManReader").toString());
								} else {
									objAddData.setReadManReader("");
								}
								// 修改人
								objAddData.setUpdateUserReader(strWorkerCode);
								// 是否使用
								objAddData.setIsUseReader("Y");
								qRemote.save(objAddData);
							}
						}
					}
				}
				tx.commit();
				write(Constants.ADD_SUCCESS);
				LogUtil.log("Action:弹出窗口保存按下的操作正常结束", Level.INFO, null);
			} catch (DataChangeException e) {
				tx.rollback();
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				tx.rollback();
				write(Constants.SQL_FAILURE);
			} catch (JSONException e) {
				tx.rollback();
				write(Constants.DATA_FAILURE);
			} catch (Exception e) {
				tx.rollback();
			}
		} catch (Exception e) {
			LogUtil.log("Action:弹出窗口保存按下的操作异常结束", Level.SEVERE, null);
		}
	}
	

	
	/**
	 * @return 上次修改时间
	 */
	public String getStrUpdateTime() {
		return strUpdateTime;
	}

	/**
	 * @param 上次修改时间
	 */
	public void setStrUpdateTime(String strUpdateTime) {
		this.strUpdateTime = strUpdateTime;
	}

	/**
	 * @return 要增加的数据
	 */
	public String getStrAdd() {
		return strAdd;
	}

	/**
	 * @param 要增加的数据
	 */
	public void setStrAdd(String strAdd) {
		this.strAdd = strAdd;
	}

	/**
	 * @return 要更新的数据
	 */
	public String getStrUpdate() {
		return strUpdate;
	}

	/**
	 * @param 要更新的数据
	 */
	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

	/**
	 * @return 要删除的数据
	 */
	public String getStrDelete() {
		return strDelete;
	}

	/**
	 * @param 要删除的数据
	 */
	public void setStrDelete(String strDelete) {
		this.strDelete = strDelete;
	}

	/**
	 * @return 参数开始页
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param 参数开始页
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * @return 参数页面最大值
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param 参数页面最大值
	 */
	public void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * @return 签报申请序号
	 */
	public Long getLngId() {
		return lngId;
	}

	/**
	 * @param 签报申请序号
	 */
	public void setLngId(Long lngId) {
		this.lngId = lngId;
	}

	/**
	 * @return 申请单号
	 */
	public String getStrApplyId() {
		return strApplyId;
	}

	/**
	 * @param 申请单号
	 */
	public void setStrApplyId(String strApplyId) {
		this.strApplyId = strApplyId;
	}

	/**
	 * @return entityform
	 */
	public AdJOutQuest getQuest() {
		return quest;
	}

	/**
	 * @param entityform
	 */
	public void setQuest(AdJOutQuest quest) {
		this.quest = quest;
	}

    /**
	 * @return 抄送人code
	 */
	public String getStrReaderMan() {
		return strReaderMan;
	}

	/**
	 * @param 抄送人code
	 */
	public void setStrReaderMan(String strReaderMan) {
		this.strReaderMan = strReaderMan;
	}

	/**
	 * @return 签报编号
	 */
	public String getStrRes() {
		return strRes;
	}

	/**
	 * @param strRes 签报编号
	 */
	public void setStrRes(String strRes) {
		this.strRes = strRes;
	}


}
