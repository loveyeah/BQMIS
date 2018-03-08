/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.laborborrowregister.action;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJBorrowcontract;
import power.ejb.hr.HrJBorrowcontractFacadeRemote;
import power.ejb.hr.HrJCooperateunitFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJEmployeeborrow;
import power.ejb.hr.HrJEmployeeborrowFacadeRemote;
import power.ejb.hr.LaborBorrowRegisterQueryFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 劳务派遣登记LAction
 * 
 * @author zhaomingjian
 * @version 1.0
 */
public class LaborBorrowRegisterAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/** 删除状态******* */
	private static final String DEF_FLAG_DELETE = "0";
	/** 修改状态******* */
	private static final String DEF_FLAG_UPDATE = "1";
	/** 新增状态******* */
	private static final String DEF_FLAG_ADD = "2";
	/** 日期格式 带时分秒 */
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式 不带时分秒 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 起始* */
	private int start = 0;
	/** 限制* */
	private int limit = 0;
	/** 协作单位之ejb* */
	private HrJCooperateunitFacadeRemote cooperateUnitRemote;
	/** 劳务派遣登记ejb* */
	private LaborBorrowRegisterQueryFacadeRemote laborBorrowRemote;
	/** 劳务合同删除 * */
	private HrJBorrowcontractFacadeRemote borrowconstractRemote;
	/** 派遣人员* */
	private HrJEmployeeborrowFacadeRemote employeeBorrowRemote;
	/** 人员基本信息* */
	private HrJEmpInfoFacadeRemote empInfoRemote;
	/** 签字日期开始* */
	private String strSignatureDateFrom;
	/** 签字日期结束* */
	private String strSignatureDateTo;
	/** 协作单位ID* */
	private String strCooperateUnitId;
	/** 调动类型* */
	private String strTransferType;
	/** 单据状态* */
	private String strDcmStatus;
	/** 合同附件ID* */
	private String strWorkConstractId;
	/** 派遣合同ID* */
	private String strBorrowContractId;
	/** 方法* */
	private String strMethod;
	/** 合同编码* */
	private String strWrokContractNo;
	/** 签字日期* */
	private String strSignatureDate;
	/** 开始日期* */
	private String strStartDate;
	/** 结束日期* */
	private String strEndDate;
	/** 内容* */
	private String strContractContent;
	/** 内容* */
	private String strNote;
	/** json数据* */
	private String strJsonData;
	/** 更新时间* */
	private String strUpdateTime;
	/** 文件ID* */
	private String fileId;
	/** 文件上传* */
	File strFileUpload;
	/** 外借合同Id* */
	private String contractId;
	/** 排他用时间* */
	private String strLastModifiedTime;

	/**
	 * 构造函数
	 */
	public LaborBorrowRegisterAction() {
		// 协作单位
		cooperateUnitRemote = (HrJCooperateunitFacadeRemote) factory
				.getFacadeRemote("HrJCooperateunitFacade");
		// 劳务派遣
		laborBorrowRemote = (LaborBorrowRegisterQueryFacadeRemote) factory
				.getFacadeRemote("LaborBorrowRegisterQueryFacade");
		// 劳务派遣合同
		borrowconstractRemote = (HrJBorrowcontractFacadeRemote) factory
				.getFacadeRemote("HrJBorrowcontractFacade");
		// 派遣人员
		employeeBorrowRemote = (HrJEmployeeborrowFacadeRemote) factory
				.getFacadeRemote("HrJEmployeeborrowFacade");

		// 人员基本信息
		empInfoRemote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");

	}

	/**
	 * 劳务派遣登记查询画面
	 */
	public void getLaborBorrowRegisterInfo() {
		try {
			LogUtil.log("Action:劳务派遣登记查询开始。", Level.INFO, null);
			PageObject obj = new PageObject();
			// 企业code
			obj = laborBorrowRemote.getLaborBorrowRegisterInfo(
					strSignatureDateFrom, strSignatureDateTo,
					strCooperateUnitId, strDcmStatus, employee
							.getEnterpriseCode(),strTransferType, start, limit);
			String str = "";
			if (obj.getTotalCount() <= 0) {
				str = "{\"list\":[],\"totalCount\":null}";
			} else {
				// 序列化PageObject类型对象,转化为字符串类型
				str = JSONUtil.serialize(obj);
			}
			write(str);
			LogUtil.log("Action:劳务派遣登记查询结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:劳务派遣登记查询失败。", Level.SEVERE, e);
		}

	}

	/**
	 * 协作单位ID与名称
	 * 
	 */
	public void getCooperateUnitIDAndName() {
		try {
			LogUtil.log("Action:获取协作单位ID与名称查询开始。", Level.INFO, null);

			PageObject obj = new PageObject();
			// 企业code
			obj = cooperateUnitRemote.getCooperateUnitIDAndName(employee
					.getEnterpriseCode());
			String str = "";
			if (obj.getTotalCount() <= 0) {
				str = "{\"list\":[],\"totalCount\":null}";
			} else {
				// 序列化PageObject类型对象,转化为字符串类型
				str = JSONUtil.serialize(obj);
			}
			write(str);
			LogUtil.log("Action:劳务派遣登记查询结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:获取协作单位ID与名称失败。", Level.SEVERE, e);
		}

	}

	/**
	 * 检索派遣人员信息
	 */
	public void getBorrowEmployeeInfo() {
		try {
			LogUtil.log("Action:检索派遣人员信息开始。", Level.INFO, null);
			PageObject obj = new PageObject();

			obj = laborBorrowRemote.getBorrowEmployeeInfo(strWorkConstractId,
					employee.getEnterpriseCode());
			String str = "";
			if (obj.getTotalCount() <= 0) {

				str = "{\"list\":[],\"totalCount\":null}";
			} else {

				// 序列化PageObject类型对象,转化为字符串类型
				str = JSONUtil.serialize(obj);
			}
			write(str);
			LogUtil.log("Action:检索派遣人员信息结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:检索派遣人员信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 逻辑删除
	 */
	public void logicDelete() {
		try {
			// log 开始
			LogUtil.log("Action:劳务派遣合同删除开始。", Level.INFO, null);
			// 取得更新时间
			String strUpdateTime = request.getParameter("strUpdateTime");
			borrowconstractRemote.logicDelete(strBorrowContractId, employee
					.getWorkerCode(), strUpdateTime);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action: 劳务派遣合同删除正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:劳务派遣合同删除失败。", Level.SEVERE, e);
			write(Constants.DATA_USING);
		} catch (SQLException ee) {
			LogUtil.log("Action:劳务派遣合同删除失败。", Level.SEVERE, ee);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 上报函数
	 */
	public void repoetBorrowContract() {
		try {
			// log 开始
			LogUtil.log("Action:劳务派遣合同上报开始。", Level.INFO, null);
			// 取得更新时间
			String strUpdateTime = request.getParameter("strUpdateTime");
			borrowconstractRemote.repoet(strBorrowContractId, employee
					.getWorkerCode(), strUpdateTime);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action: 劳务派遣合同上报正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:劳务派遣合同上报失败。", Level.SEVERE, e);
			write(Constants.DATA_USING);
		} catch (SQLException ee) {
			LogUtil.log("Action:劳务派遣合同上报失败。", Level.SEVERE, ee);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 保存函数
	 */
	public void doSave2OfRegister() {
		try {
			// 事务操作
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			try {
				LogUtil.log("Action:保存劳务派遣合同开始。", Level.INFO, null);
				// 序列化
				Object jsonData = JSONUtil.deserialize(strJsonData);
				List lstJsonData = (List) jsonData;
				// 新增员工派遣
				List<HrJEmployeeborrow> lstInsert = new ArrayList<HrJEmployeeborrow>();
				// 更新员工派遣
				List<HrJEmployeeborrow> lstUpdate = new ArrayList<HrJEmployeeborrow>();
				// 删除员工派遣
				List<HrJEmployeeborrow> lstDelete = new ArrayList<HrJEmployeeborrow>();

				Iterator it = lstJsonData.iterator();
				while (it.hasNext()) {
					Map map = (Map) it.next();
					// 员工外借登记
					HrJEmployeeborrow entity = new HrJEmployeeborrow();
					// 员工外借ID
					if (map.get("employeeBorrowId") != null) {
						entity.setEmployeeborrowid(Long.parseLong(map.get(
								"employeeBorrowId").toString()));
					} else {
						entity.setEmployeeborrowid(null);
					}
					// 劳务派遣合同ID
					if (strMethod.equals(DEF_FLAG_UPDATE)) {
						entity.setBorrowcontractid(Long
								.parseLong(strBorrowContractId));
					} else {
						entity.setBorrowcontractid(null);
					}
					// 开始日期
					if (map.get("startDate") != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(map.get("startDate")
								.toString());
						entity.setStartDate(date);
					}
					// 是否返回
					if (map.get("ifBack") != null) {
						if (map.get("ifBack").equals("否")) {
							entity.setIfBack("0");
						} else {
							entity.setIfBack("1");
						}
					}

					// 结束日期
					if (map.get("endDate") != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom
								.parse(map.get("endDate").toString());
						entity.setEndDate(date);
					}
					// 停薪日期
					if ((map.get("stopPayDate") != null)
							&& !(map.get("stopPayDate").equals(""))) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(map.get("stopPayDate")
								.toString());
						entity.setStopPayDate(date);
					}
					// 起薪日期
					if ((map.get("startPayDate") != null)
							&& !(map.get("startPayDate").equals(""))) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(map.get("startPayDate")
								.toString());
						entity.setStartPayDate(date);
					}
					// 备注
					if (map.get("memo") != null) {
						entity.setMemo(map.get("memo").toString());
					} else {
						entity.setMemo("");
					}
					// 记录人
					entity.setInsertby(employee.getWorkerCode());
					// 记录日期
					entity.setInsertdate(new Date());
					// 企业编码
					entity.setEnterpriseCode(employee.getEnterpriseCode());
					// 设置是否使用
					if (map.get("flag") != null) {
						if (map.get("flag").equals(DEF_FLAG_ADD)) {
							entity.setIsUse("Y");
						} else if (map.get("flag").equals(DEF_FLAG_UPDATE)) {
							entity.setIsUse("Y");
						} else {
							entity.setIsUse("N");
						}

					}
					// 上次修改人
					entity.setLastModifiedBy(employee.getWorkerCode());
					// 上次修改日期
					if (map.get("updateTime") != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								TIME_FORMAT);
						Date date = sdfFrom.parse(map.get("updateTime")
								.toString());
						entity.setLastModifiedDate(date);
					} else {
						entity.setLastModifiedDate(new Date());
					}

					// 员工ID
					if (map.get("empId") != null
							&& !"".equals(map.get("empId"))) {
						entity.setEmpId(Long.parseLong(map.get("empId")
								.toString()));
					}
					// 部门ID
					if (map.get("deptId") != null
							&& !"".equals(map.get("deptId"))) {
						entity.setDeptId(Long.parseLong(map.get("deptId")
								.toString()));
					}
					// 岗位ID
					if (map.get("stationId") != null
							&& !"".equals(map.get("stationId"))) {
						entity.setStationId(Long.parseLong(map.get("stationId")
								.toString()));
					}
					// 根据flag来判断
					if (DEF_FLAG_DELETE.equals(map.get("flag"))) {
						lstDelete.add(entity);
					} else if (DEF_FLAG_UPDATE.equals(map.get("flag"))) {
						if (entity.getIfBack().equals("1")) {
							HrJEmpInfo info = new HrJEmpInfo();
							info.setEmpId(entity.getEmpId());
							// modify by liuyi 090914 无该属性
							// info.setEnterpriseCode(employee.getEnterpriseCode());
							// modify by liuyi 090914 存id,而非编码
							// info.setLastModifiyBy(employee.getWorkerCode());
							info.setLastModifiyBy(employee.getWorkerId());
							empInfoRemote.update2(info);
						}
						lstUpdate.add(entity);
					} else {
						// 如果员工重复则进行异常处理
						try {
							employeeBorrowRemote.checkEmployeeRepeat(entity);
						} catch (CodeRepeatException e) {
							HrJEmpInfo empInfo = empInfoRemote.findByEmpId(
									entity.getEmpId(), employee
											.getEnterpriseCode());
							write("{success:true,msg:'EMPRP',empId:'"
									+ empInfo.getEmpCode() + "'}");
							return;
						}
						lstInsert.add(entity);
					}

				}
				// 数据组装结束
				// 事务处理起点
				tx.begin();

				// 更新劳务派遣合同表
				if (strMethod.equals(DEF_FLAG_UPDATE)) {
					HrJBorrowcontract bean = new HrJBorrowcontract();
					bean.setBorrowcontractid(Long
							.parseLong(strBorrowContractId));
					if (strWrokContractNo != null) {
						bean.setWrokContractNo(strWrokContractNo);
					}
					if (strCooperateUnitId != null) {
						bean.setCooperateUnitId(Long
								.parseLong(strCooperateUnitId));
					}
					/** 调动类型* */
					if (strTransferType != null) {
						bean.setTransferType(strTransferType);
					}
					
					if (strSignatureDate != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(strSignatureDate);
						bean.setSignatureDate(date);
					}
					if (strStartDate != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(strStartDate);
						bean.setStartDate(date);
					}
					if (strEndDate != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(strEndDate);
						bean.setEndDate(date);
					}

					if (strContractContent != null) {
						bean.setContractContent(strContractContent);
					}
					if (strNote != null) {
						bean.setNote(strNote);
					}
					// 最后修改人
					bean.setLastModifiedBy(employee.getWorkerCode());
					// 最新修改时间
					if (strUpdateTime != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								TIME_FORMAT);
						Date date = sdfFrom.parse(strUpdateTime);
						bean.setLastModifiedDate(date);
					}

					bean = borrowconstractRemote.update2(bean);
					SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
					strLastModifiedTime = sdf
							.format(bean.getLastModifiedDate());
				} else {
					// 新增劳务派遣合同表
					HrJBorrowcontract bean = new HrJBorrowcontract();
					// 合同编码
					if (strWrokContractNo != null) {
						bean.setWrokContractNo(strWrokContractNo);
					}
					// 协作单位ID
					if (strCooperateUnitId != null
							&& !"".equals(strCooperateUnitId)) {
						bean.setCooperateUnitId(Long
								.parseLong(strCooperateUnitId));
					}
					/** 调动类型* */
					if (strTransferType != null) {
						bean.setTransferType(strTransferType);
					}
					// 签字日期
					if (strSignatureDate != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(strSignatureDate);
						bean.setSignatureDate(date);
					}
					// 开始日期
					if (strStartDate != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(strStartDate);
						bean.setStartDate(date);
					}
					// 结束日期
					if (strEndDate != null) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								DATE_FORMAT);
						Date date = sdfFrom.parse(strEndDate);
						bean.setEndDate(date);
					}
					// 劳务内容
					if (strContractContent != null) {
						bean.setContractContent(strContractContent);
					}
					// 设置备注
					if (strNote != null) {
						bean.setNote(strNote);
					}
					// 设置单据状态
					bean.setDcmStatus("0");
					// 设置最后修改人
					bean.setLastModifiedBy(employee.getWorkerCode());
					// 设置最后修改时间
					bean.setLastModifiedDate(new Date());
					// 设置企业code
					bean.setEnterpriseCode(employee.getEnterpriseCode());
					SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
					strLastModifiedTime = sdf
							.format(bean.getLastModifiedDate());
					// 设置is-use
					bean.setIsUse("Y");
					contractId = Long.toString(borrowconstractRemote
							.save2(bean));
				}
				// 操作员工外借表
				// 保存
				employeeBorrowRemote.save2(lstInsert);
				// 更新
				employeeBorrowRemote.update2(lstUpdate);
				// 删除
				employeeBorrowRemote.delete2(lstDelete);
				tx.commit();
				LogUtil.log("Action:保存劳务派遣合同正常结束。", Level.INFO, null);
				write("{success:true," + "ContractId:'" + contractId
						+ "',updateTime:'" + strLastModifiedTime + "'}");

			} catch (DataChangeException e) {
				LogUtil.log("Action：保存劳务派遣合同异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action：保存劳务派遣合同异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.SQL_FAILURE);
			} catch (JSONException e) {
				LogUtil.log("Action：保存劳务派遣合同异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_FAILURE);
			} catch (ParseException e) {
				LogUtil.log("Action：保存劳务派遣合同异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_FAILURE);
			} catch (CodeRepeatException e) {
				LogUtil.log("Action：保存劳务派遣合同异常结束", Level.SEVERE, e);
				tx.rollback();

			}
		} catch (Exception e) {
			LogUtil.log("Action：保存劳务派遣合同结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 起始
	 * 
	 * @return
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 起始
	 * 
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 限制
	 * 
	 * @return
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 限制
	 * 
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 
	 * @return 签字日期开始
	 */
	public String getStrSignatureDateFrom() {
		return strSignatureDateFrom;
	}

	public void setStrSignatureDateFrom(String strSignatureDateFrom) {
		this.strSignatureDateFrom = strSignatureDateFrom;
	}

	/**
	 * 
	 * @return 签字日期结束
	 */
	public String getStrSignatureDateTo() {
		return strSignatureDateTo;
	}

	public void setStrSignatureDateTo(String strSignatureDateTo) {
		this.strSignatureDateTo = strSignatureDateTo;
	}

	/**
	 * 
	 * @return 修做单位ID
	 */
	public String getStrCooperateUnitId() {
		return strCooperateUnitId;
	}

	public void setStrCooperateUnitId(String strCooperateUnitId) {
		this.strCooperateUnitId = strCooperateUnitId;
	}

	/**
	 * 
	 * @return 单据状态
	 */
	public String getStrDcmStatus() {
		return strDcmStatus;
	}

	public void setStrDcmStatus(String strDcmStatus) {
		this.strDcmStatus = strDcmStatus;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * 合同ID
	 * 
	 * @return
	 */
	public String getStrWorkConstractId() {
		return strWorkConstractId;
	}

	public void setStrWorkConstractId(String strWorkConstractId) {
		this.strWorkConstractId = strWorkConstractId;
	}

	/**
	 * 外派合同Id
	 * 
	 * @return
	 */
	public String getStrBorrowContractId() {
		return strBorrowContractId;
	}

	public void setStrBorrowContractId(String strBorrowContractId) {
		this.strBorrowContractId = strBorrowContractId;
	}

	/**
	 * 方法参数
	 * 
	 * @return
	 */
	public String getStrMethod() {
		return strMethod;
	}

	public void setStrMethod(String strMethod) {
		this.strMethod = strMethod;
	}

	/**
	 * 合同编码
	 * 
	 * @return
	 */
	public String getStrWrokContractNo() {
		return strWrokContractNo;
	}

	public void setStrWrokContractNo(String strWrokContractNo) {
		this.strWrokContractNo = strWrokContractNo;
	}

	/**
	 * 签字日期
	 * 
	 * @return
	 */
	public String getStrSignatureDate() {
		return strSignatureDate;
	}

	public void setStrSignatureDate(String strSignatureDate) {
		this.strSignatureDate = strSignatureDate;
	}

	/**
	 * 开始日期
	 * 
	 * @return
	 */
	public String getStrStartDate() {
		return strStartDate;
	}

	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	/**
	 * 结束日期
	 * 
	 * @return
	 */
	public String getStrEndDate() {
		return strEndDate;
	}

	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	/**
	 * 内容
	 * 
	 * @return
	 */
	public String getStrContractContent() {
		return strContractContent;
	}

	public void setStrContractContent(String strContractContent) {
		this.strContractContent = strContractContent;
	}

	/**
	 * 备注
	 * 
	 * @return
	 */
	public String getStrNote() {
		return strNote;
	}

	public void setStrNote(String strNote) {
		this.strNote = strNote;
	}

	/**
	 * json数据包
	 * 
	 * @return
	 */
	public String getStrJsonData() {
		return strJsonData;
	}

	public void setStrJsonData(String strJsonData) {
		this.strJsonData = strJsonData;
	}

	/**
	 * 文件ID
	 * 
	 * @return
	 */
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 更新时间
	 * 
	 * @return
	 */
	public String getStrUpdateTime() {
		return strUpdateTime;
	}

	public void setStrUpdateTime(String strUpdateTime) {
		this.strUpdateTime = strUpdateTime;
	}

	/**
	 * 文件上传
	 * 
	 * @return
	 */
	public File getStrFileUpload() {
		return strFileUpload;
	}

	public void setStrFileUpload(File strFileUpload) {
		this.strFileUpload = strFileUpload;
	}

	/** 调动类型* */
	public String getStrTransferType() {
		return strTransferType;
	}

	public void setStrTransferType(String strTransferType) {
		this.strTransferType = strTransferType;
	}

}
