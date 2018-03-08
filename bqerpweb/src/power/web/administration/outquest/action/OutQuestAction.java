/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.web.administration.outquest.action;

import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
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
import power.ejb.administration.form.QuestFileInfo;
import power.ejb.administration.form.QuestReaderInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 内部签报申请
 * 
 * @author sufeiyu
 */
@SuppressWarnings("serial")
public class OutQuestAction extends AbstractAction {

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
	/**文件上传**/
	File fileUpload;
	/**文件上传Id**/
	Long lngFileId;
	/**抄送人code**/
	String strReaderMan;
	/**文件名称**/
	String fileUploadFileName;

	/** 构造函数* */
	public OutQuestAction() {
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
	 *  取得所有的申报申请
	 */
	public void getOutQuest() {
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

			objResult = oRemote.getOutQuest(strEnterpriseCode, strWorkerCode, intStart, intLimit);

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
	 * 删除一条数据
	 */
	@SuppressWarnings("unchecked")
	public void deleteQuost() {
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strWorkerCode = employee.getWorkerCode();

		LogUtil.log("Action:删除申报申请开始", Level.INFO, null);
		try {
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			try {
				// 事务处理起点
				tx.begin();

				// 签报申请表删除
				AdJOutQuest objAdJOutQuest = oRemote.findById(lngId);
				objAdJOutQuest.setUpdateUser(strWorkerCode);
				objAdJOutQuest.setIsUse("N");
				oRemote.update(objAdJOutQuest, strUpdateTime);

				// 签报申请附件删除
				PageObject pobjFileInfo = fRemote.findById(strApplyId);
				if (pobjFileInfo.getTotalCount() > 0) {
					List<QuestFileInfo> list = pobjFileInfo.getList();

					if ((list != null) && (list.size() > 0)) {
						Iterator it = list.iterator();
						while (it.hasNext()) {
							QuestFileInfo objFileInfo = new QuestFileInfo();
							objFileInfo = (QuestFileInfo) it.next();
							objFileInfo.setUpdateUserFile(strWorkerCode);
							objFileInfo.setIsUseFile("N");
							fRemote.update(objFileInfo, objFileInfo.getUpdateTimeFile());
						}
					}
				}

				// 签报申请抄送人删除
				PageObject pobjReaderInfo = qRemote.findById(strApplyId);
				if (pobjReaderInfo.getTotalCount() > 0) {
					List<QuestReaderInfo> list = pobjReaderInfo.getList();

					if ((list != null) && (list.size() > 0)) {
						Iterator it = list.iterator();
						while (it.hasNext()) {
							QuestReaderInfo objReaderInfo = new QuestReaderInfo();
							objReaderInfo = (QuestReaderInfo) it.next();
							objReaderInfo.setUpdateUserReader(strWorkerCode);
							objReaderInfo.setIsUseReader("N");
							qRemote.update(objReaderInfo, objReaderInfo.getUpdateTimeReader());
						}
					}
				}
				tx.commit();
				write(Constants.DELETE_SUCCESS);
				LogUtil.log("Action:删除申报申请正常结束", Level.INFO, null);
			} catch (DataChangeException e) {
				LogUtil.log("Action:删除申报申请异常结束", Level.SEVERE, null);
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action:删除申报申请异常结束", Level.SEVERE, null);
				write(Constants.SQL_FAILURE);
			} catch (Exception e) {
				LogUtil.log("Action:删除申报申请异常结束", Level.SEVERE, null);
				tx.rollback();
			}
		} catch (Exception e) {
			LogUtil.log("Action:删除申报申请异常结束", Level.SEVERE, null);
		}

	}

	/**
	 * 上报一条数据
	 */
	public void reportQuost() {
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strWorkerCode = employee.getWorkerCode();

		LogUtil.log("Action:上报申报申请开始", Level.INFO, null);
		try {
			AdJOutQuest objAdJOutQuest = oRemote.findById(lngId);
			objAdJOutQuest.setUpdateUser(strWorkerCode);
			objAdJOutQuest.setDcmStatus("1");

			oRemote.update(objAdJOutQuest, strUpdateTime);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:上报申报申请正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:上报申报申请异常结束", Level.SEVERE, null);
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action:上报申报申请异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 取得新规的内部签报申请单号
	 */
	public void getNewApplyId() {
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
			strNewApplyId = cRemote.getReportAppNoCode("I", strWorkerCode);
			
			strRes = cRemote.getReportNoCode("I", strDeptCode);

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
	 * 取得抄送人的信息
	 */
	public void getQuestReader() {
		PageObject objResult = new PageObject();

		LogUtil.log("Action:取得全部抄送人开始", Level.INFO, null);

		try {
			// 设置页面参数
			int intStart = Integer.parseInt(String.valueOf(start));
			int intLimit = Integer.parseInt(String.valueOf(limit));

			objResult = qRemote.getAllQuestReader(strApplyId, intStart,
					intLimit);

			// 要写出的数据
			String strRecord = "";
			if (objResult.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(objResult);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action:取得全部抄送人正常结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得全部抄送人异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		}
	}

	/**
	 * 取得附件的信息
	 * @throws SQLException 
	 */
	public void getOutQuestFile() {
		PageObject objResult = new PageObject();

		LogUtil.log("Action:取得全部申请附件开始", Level.INFO, null);

		try {
			// 设置页面参数
			int intStart = Integer.parseInt(String.valueOf(start));
			int intLimit = Integer.parseInt(String.valueOf(limit));

			objResult = fRemote.getAllQuestFile(strApplyId, intStart, intLimit);

			// 要写出的数据
			String strRecord = "";
			if (objResult.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(objResult);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action:取得全部申请附件正常结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得全部申请附件异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		} catch (NumberFormatException e) {
			LogUtil.log("Action:取得全部申请附件异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		} catch (SQLException e) {
			LogUtil.log("Action:取得全部申请附件异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 弹出窗口保存按下的操作
	 */
	@SuppressWarnings("unchecked")
	public void saveQuost() {

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
						objUpdate.setAppType("I");
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
						String strRes = cRemote.getReportNoCode("I", strDeptCode);
						objAdd.setReportId(strRes);
						objAdd.setAppType("I");
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
	 * 文件的上传处理
	 * 
	 * 
	 */
	public void uploadQuestFile() {
		LogUtil.log("Action:文件的上传操作开始", Level.INFO, null);

		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strWorkerCode = employee.getWorkerCode();

		QuestFileInfo objFileInfo = new QuestFileInfo();
		InputStream bis = null;
		if (fileUpload != null) {
			if (fileUpload.exists()) {
				Long lngLength = fileUpload.length();
				int intLength = Integer.parseInt(lngLength.toString());
				try {
					bis = new BufferedInputStream(new FileInputStream(
							fileUpload), 1024);
					byte[] data = new byte[intLength];
					while (bis.read(data) == -1) {

					}
					objFileInfo.setFileTextFile(data);
					objFileInfo.setApplyIdFile(strApplyId);
					objFileInfo.setUpdateUserFile(strWorkerCode);
					objFileInfo.setFileNameFile(fileUploadFileName);
					objFileInfo.setIsUseFile("Y");
					// 功能性bug
					//objFileInfo.setFileTypeFile(fileUploadContentType);
					fRemote.save(objFileInfo);
					write(Constants.UPLOAD_SUCCESS);
					LogUtil.log("Action:文件的上传操作正常结束", Level.INFO, null);
				} catch (IOException e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.IO_FAILURE);
				} catch (SQLException e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				} catch (Exception e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
				} finally {
					if (null != bis) {
						try {
							bis.close();
						} catch (IOException e) {
							LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
							write(Constants.IO_FAILURE);
						}
					}
				}
			} else {
				write(Constants.FILE_NOT_EXIST);
			}
		}
	}

	/**
	 *  删除附件操作
	 */
	public void deleteQuestFile() {
		LogUtil.log("Action:删除附件操作开始", Level.INFO, null);

		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strWorkerCode = employee.getWorkerCode();
		try {
			QuestFileInfo objFileInfo = new QuestFileInfo();
			objFileInfo = fRemote.findByPhyId(lngFileId);
			objFileInfo.setUpdateUserFile(strWorkerCode);
			objFileInfo.setIsUseFile("N");

			fRemote.update(objFileInfo, objFileInfo.getUpdateTimeFile());
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除附件操作正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除附件操作异常结束", Level.SEVERE, null);
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action:删除附件操作异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:删除附件操作异常结束", Level.SEVERE, null);
		}
	}

	/**
	 * 得到抄送人名称
	 */
	public void getReaderManName() {
		// 得到抄送人名称
		String strReaderManName = "";
		strReaderManName = oRemote.getDeptNameById(strReaderMan);
		write("{success:true,readerMan:'" + strReaderManName + "'}");
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
	 * @return the lngApplyId
	 */
	public String getStrApplyId() {
		return strApplyId;
	}

	/**
	 * @param lngApplyId the lngApplyId to set
	 */
	public void setStrApplyId(String strApplyId) {
		this.strApplyId = strApplyId;
	}

	/**
	 * @return the quest
	 */
	public AdJOutQuest getQuest() {
		return quest;
	}

	/**
	 * @param quest the quest to set
	 */
	public void setQuest(AdJOutQuest quest) {
		this.quest = quest;
	}

	/**
	 * @return the file
	 */
	public File getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param file the file to set
	 */
	public void setFileUpload(File file) {
		this.fileUpload = file;
	}

	/**
	 * @return the file
	 */
	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	/**
	 * @param file the file to set
	 */
	public void setFileUploadContentType(String file) {
		this.fileUploadContentType = file;
	}

	private String fileUploadContentType;

	/**
	 * @return the lngFileId
	 */
	public Long getLngFileId() {
		return lngFileId;
	}

	/**
	 * @param lngFileId the lngFileId to set
	 */
	public void setLngFileId(Long lngFileId) {
		this.lngFileId = lngFileId;
	}

	/**
	 * @return the strReaderMan
	 */
	public String getStrReaderMan() {
		return strReaderMan;
	}

	/**
	 * @param strReaderMan the strReaderMan to set
	 */
	public void setStrReaderMan(String strReaderMan) {
		this.strReaderMan = strReaderMan;
	}

	/**
	 * @return the fileUploadFileName
	 */
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	/**
	 * @param fileUploadFileName the fileUploadFileName to set
	 */
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	/**
	 * @return 签报编号
	 */
	public String getStrRes() {
		return strRes;
	}

	/**
	 * @param 签报编号
	 */
	public void setStrRes(String strRes) {
		this.strRes = strRes;
	}
}
