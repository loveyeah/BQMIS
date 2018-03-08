/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.jobmanagement.newemployeeregister.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJDepstationcorrespondFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJEmpStation;
import power.ejb.hr.HrJEmpStationFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.NewEmployeeRegister;
import power.ejb.hr.NewEmployeeRegisterFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 新进员工登记Action
 * 
 * @author jincong
 * @version 1.0
 */
public class NewEmployeeRegisterAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 开始行 */
	private int start;
	/** 查询行 */
	private int limit;
	/** 年度 */
	private String date;
	/** 是否存档 */
	private String isSave;
	/** 查询条件: 部门ID */
	private String deptIdQuery;
	/** 表单: 部门ID */
	private String deptIdForm;
	/** 员工ID */
	private String empId;
	/** 上次修改时间 */
	private String lastModifiyDate;
	/** 新增/修改标识 */
	private String flag;
	/** 新进人员信息 */
	private NewEmployeeRegister empRegister;

	/** 新进员工登记Remote */
	private NewEmployeeRegisterFacadeRemote registerRemote;
	/** 部门岗位对应Remote */
	private HrJDepstationcorrespondFacadeRemote correspondRemote;
	/** 人员基本信息Remote */
	private HrJEmpInfoFacadeRemote empInfoRemote;
	/** 职工岗位信息登记Remote */
	HrJEmpStationFacadeRemote empStationRemote;
	
	// 常量
	/** 查询结果为空 */
	public static final String BLANK_RESULT = "{\"list\":[],\"totalCount\":0}";
	/** 时间格式 */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 新增 */
	public static final String FLAG_ADD = "A";
	/** 修改 */
	public static final String FLAG_UPDATE = "U";
	/** 员工状态: 1 */
	public static final String EMP_STATE_1 = "1";
	/** 员工状态: 2 */
	public static final String EMP_STATE_2 = "2";
	/** 是否主岗位 */
	public static final String IS_MAIN_STATION_1 = "1";

	/**
	 * 构造函数
	 */
	public NewEmployeeRegisterAction() {
		// 接口的取得
		registerRemote = (NewEmployeeRegisterFacadeRemote) factory
				.getFacadeRemote("NewEmployeeRegisterFacade");
		correspondRemote = (HrJDepstationcorrespondFacadeRemote) factory
				.getFacadeRemote("HrJDepstationcorrespondFacade");
		empInfoRemote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		empStationRemote = (HrJEmpStationFacadeRemote) factory
				.getFacadeRemote("HrJEmpStationFacade");

	}

	/**
	 * 查询新进员工信息
	 */
	public void getNewEmployeeQuery() {
		try {
			LogUtil.log("Action:查询新进员工信息开始。", Level.INFO, null);
			// 查询
			String flag=request.getParameter("flag");//add by sychen 20100716
			PageObject object = registerRemote.getNewEmployeeQuery(flag,date,
					deptIdQuery, isSave, employee.getEnterpriseCode(), start,
					limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:查询新进员工信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:查询新进员工信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 根据部门取得岗位
	 */
	public void getStationByDeptNewEmployee() {
		try {
			LogUtil.log("Action:根据部门查找岗位信息开始。", Level.INFO, null);
			// 查询
			PageObject object = correspondRemote.findByDeptId(deptIdForm,
					employee.getEnterpriseCode());
			// 输出
			String strOutput = BLANK_RESULT;
			if (object.getList() != null && (object.getList().size() > 0)) {
				strOutput = JSONUtil.serialize(object);
			}
			write(strOutput);
			LogUtil.log("Action:根据部门查找岗位信息结束。", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:根据部门查找岗位信息失败。", Level.SEVERE, e);
		} catch (JSONException e) {
			LogUtil.log("Action:根据部门查找岗位信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 查询人员信息
	 */
	public void getEmpInfoNewEmployee() {
		try {
			LogUtil.log("Action:查询人员信息开始。", Level.INFO, null);
			// 查询
			PageObject object = registerRemote.getEmpInfoNewEmployee(employee
					.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:查询人员信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:查询人员信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 删除新进人员
	 */
	public void deleteNewEmployee() {
		try {
			LogUtil.log("Action:删除新进人员开始。", Level.INFO, null);
			HrJEmpInfo empInfo = empInfoRemote.findByEmpId(Long
					.parseLong(empId), employee.getEnterpriseCode());
			// 排他
			if (empInfo == null) {
				throw new DataChangeException("");
			}
			// 排他
			if (!formatDateToString(empInfo.getLastModifiyDate(), TIME_FORMAT)
					.equals(lastModifiyDate)) {
				throw new DataChangeException("");
			}
			// 设置员工ID
			empInfo.setEmpId(Long.parseLong(empId));
			// 设置上次修改时间
			empInfo.setLastModifiyDate(formatStringToDate(lastModifiyDate,
					TIME_FORMAT));
			// 设置上次修改人
			// modified by liuyi 091123 数据库表属性为number
//			empInfo.setLastModifiyBy(employee.getWorkerCode());
			empInfo.setLastModifiyBy(employee.getWorkerId());
			// 设置是否使用
			empInfo.setIsUse(Constants.IS_USE_N);
			// 更新
			empInfoRemote.update(empInfo);
			// 成功
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除新进人员结束。", Level.INFO, null);
		} catch (DataFormatException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:删除新进人员失败。", Level.SEVERE, e);
		} catch (DataChangeException e) {
			write(Constants.DATA_USING);
			LogUtil.log("Action:删除新进人员失败。", Level.SEVERE, e);
		}
		//modified by liuyi 091123 无该异常
//		catch (SQLException e) {
//			write(Constants.SQL_FAILURE);
//			LogUtil.log("Action:删除新进人员失败。", Level.SEVERE, e);
//		}

	}

	/**
	 * 保存新进人员信息
	 */
	@SuppressWarnings("unchecked")
	public void saveNewEmployee() throws Exception {
		UserTransaction tx = null;
		try {
			LogUtil.log("Action:保存新进人员信息开始。", Level.INFO, null);
			tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			// 事务开始
			tx.begin();
			
			HrJEmpInfo empInfo = empInfoRemote.findByEmpId(Long
					.parseLong(empRegister.getEmpId()), employee
					.getEnterpriseCode());
			// 排他
			if (empInfo == null) {
				throw new DataChangeException("");
			}
			// 排他
			if (!formatDateToString(empInfo.getLastModifiyDate(), TIME_FORMAT)
					.equals(empRegister.getLastModifiyDate())) {
				throw new DataChangeException("");
			}
			// 设置人员ID
			empInfo.setEmpId(Long.parseLong(empRegister.getEmpId()));
			// 设置员工编码
			empInfo.setEmpCode(empRegister.getEmpCode());
			// 设置出生日期
			empInfo.setBrithday(formatStringToDate(empRegister.getBirthday(),
					DATE_FORMAT));
			// 设置性别
			empInfo.setSex(empRegister.getSex());
			// 设置中文姓名
			empInfo.setChsName(empRegister.getChsName());
			// 设置婚姻状况
			empInfo.setIsWedded(empRegister.getIsWedded());
			// 设置英文姓名
			empInfo.setEnName(empRegister.getEnName());
			// 设置籍贯
			empInfo.setNativePlaceId(Long.parseLong(empRegister
					.getNativePlaceId()));
			// 设置民族
			// modified by liuyi 091123 暂无该属性 已解决
			empInfo.setNationCodeId(Long.parseLong(empRegister
					.getNationCodeId()));
			// 设置政治面貌
			if (!Constants.BLANK_STRING.equals(empRegister.getPoliticsId())
					&& !(empRegister.getPoliticsId() == null)) {
				empInfo.setPoliticsId(Long.parseLong(empRegister
						.getPoliticsId()));
			} else {
				empInfo.setPoliticsId(null);
			}
			// 设置身份证号
			empInfo.setIdentityCard(empRegister.getIdentityCard());
			// 设置所属部门
			empInfo.setDeptId(Long.parseLong(empRegister.getDeptId()));
			// 设置工作岗位
			empInfo.setStationId(Long.parseLong(empRegister.getStationId()));
			// 设置员工类别
			empInfo.setEmpTypeId(Long.parseLong(empRegister.getEmpTypeId()));
			// 设置进厂类别
			// modified by liuyi 091123 暂无该属性 已解决
			empInfo.setInTypeId(Long.parseLong(empRegister.getInTypeId()));
			// 设置进厂日期
			empInfo.setMissionDate(formatStringToDate(empRegister
					.getMissionDate(), DATE_FORMAT));
			// 设置试用期开始
			// modified by liuyi 091123 暂无该属性 已解决
			empInfo.setTryoutStartDate(formatStringToDate(empRegister
					.getTryoutStartDate(), DATE_FORMAT));
			// 设置试用期结束 已解决
			// modified by liuyi 091123 暂无该属性
			empInfo.setTryoutEndDate(formatStringToDate(empRegister
					.getTryoutEndDate(), DATE_FORMAT));
			// 设置是否存档
			if (Constants.FLAG_Y.equals(empRegister.getEmpState())) {
				empInfo.setEmpState(EMP_STATE_2);
			} else if (Constants.FLAG_N.equals(empRegister.getEmpState())) {
				empInfo.setEmpState(EMP_STATE_1);
			}
			// 设置学历
			if (!Constants.BLANK_STRING.equals(empRegister.getEducationId())
					&& !(empRegister.getEducationId() == null)) {
				empInfo.setEducationId(Long.parseLong(empRegister
						.getEducationId()));
			} else {
				empInfo.setEducationId(null);
			}
			// 设置学习专业
			// modified by liuyi 091123 暂无该属性 已解决
			if (!Constants.BLANK_STRING.equals(empRegister.getSpecialtyCodeId())
					&& !(empRegister.getSpecialtyCodeId() == null)) {
				empInfo.setSpecialtyCodeId(Long.parseLong(empRegister
						.getSpecialtyCodeId()));
			} else {
				empInfo.setSpecialtyCodeId(null);
			}
			// 设置学位
			if (!Constants.BLANK_STRING.equals(empRegister.getDegreeId())
					&& !(empRegister.getDegreeId() == null)) {
				empInfo.setDegreeId(Long.parseLong(empRegister.getDegreeId()));
			} else {
				empInfo.setDegreeId(null);
			}
			// 设置毕业日期
			empInfo.setGraduateDate(formatStringToDate(empRegister
					.getGraduateDate(), DATE_FORMAT));
			// 设置毕业学校 已解决
			// modified by liuyi 091123 该属性为String类型
			if (!Constants.BLANK_STRING.equals(empRegister.getSchoolCodeId())
					&& !(empRegister.getSchoolCodeId() == null)) {
				empInfo.setGraduateSchoolId(Long.parseLong(empRegister
						.getSchoolCodeId()));
			} else {
				empInfo.setGraduateSchoolId(null);
			}
			// 设置上次修改时间
			empInfo.setLastModifiyDate(formatStringToDate(empRegister
					.getLastModifiyDate(), TIME_FORMAT));
			// 设置是否退转军人
			empInfo.setIsVeteran(empRegister.getIsVeteran());
			// 设置备注
			empInfo.setMemo(empRegister.getMemo());
			// 设置上次修改人
			// modified by liuyi 091123 该属性为Id
//			empInfo.setLastModifiyBy(employee.getWorkerCode());
			empInfo.setLastModifiyBy(employee.getWorkerId());
			// 设置是否使用
			empInfo.setIsUse(Constants.IS_USE_Y);
			// 设置企业代码
			empInfo.setEnterpriseCode(employee.getEnterpriseCode());
			// 更新
			empInfoRemote.update(empInfo);
			
			// 新增/更新职工岗位信息登记表
			HrJEmpStation empStation = new HrJEmpStation();
			// 新增
			if(FLAG_ADD.equals(flag)) {
				// 设置人员ID
				empStation.setEmpId(Long.parseLong(empRegister.getEmpId()));
				// 设置岗位
				empStation.setStationId(Long.parseLong(empRegister.getStationId()));
				// 设置是否主岗位
				empStation.setIsMainStation(IS_MAIN_STATION_1);
				// 设置是否使用
				empStation.setIsUse(Constants.IS_USE_Y);
				// 设置企业编码
				empStation.setEnterpriseCode(employee.getEnterpriseCode());
				// 设置上次修改人
				empStation.setLastModifiedBy(employee.getWorkerCode());
				// 设置上次修改时间
				empStation.setLastModifiedDate(new Date());
				// 新增
				empStationRemote.save(empStation);
			// 更新
			} else if(FLAG_UPDATE.equals(flag)) {
				PageObject object = empStationRemote.findEmpStationInfoByEmpId(
						empRegister.getEmpId(), employee.getEnterpriseCode());
				List<HrJEmpStation> list = object.getList();
				if(list.size() == 0) {
					throw new DataChangeException("");
				}
				empStation = list.get(0);
				// 设置岗位
				empStation.setStationId(Long.parseLong(empRegister.getStationId()));
				// 设置上次修改人
				empStation.setLastModifiedBy(employee.getWorkerCode());
				// 更新
				empStationRemote.update(empStation);
			}
			// 提交事务
			tx.commit();
			// 成功
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:保存新进人员信息结束。", Level.INFO, null);
		} catch (DataFormatException e) {
			write(Constants.DATA_FAILURE);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			LogUtil.log("Action:保存新进人员信息失败。", Level.SEVERE, e);
		} catch (DataChangeException e) {
			write(Constants.DATA_USING);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			LogUtil.log("Action:保存新进人员信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			LogUtil.log("Action:保存新进人员信息失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date dtresult = null;

		sdfFrom = new SimpleDateFormat(argFormat);
		// 格式化日期
		try {
			dtresult = sdfFrom.parse(argDateStr);
		} catch (ParseException e) {
			throw new DataFormatException();
		}
		return dtresult;
	}

	/**
	 * 根据日期和形式返回日期字符串
	 * 
	 * @param argDate
	 *            日期
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期字符串
	 */
	@SuppressWarnings("unused")
	private String formatDateToString(Date argDate, String argFormat) {
		if (argDate == null) {
			return "";
		}

		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回字符串
		String strResult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			strResult = sdfFrom.format(argDate).toString();
		} catch (Exception e) {
			strResult = "";
		} finally {
			sdfFrom = null;
		}

		return strResult;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the deptIdQuery
	 */
	public String getDeptIdQuery() {
		return deptIdQuery;
	}

	/**
	 * @param deptIdQuery
	 *            the deptIdQuery to set
	 */
	public void setDeptIdQuery(String deptIdQuery) {
		this.deptIdQuery = deptIdQuery;
	}

	/**
	 * @return the deptIdForm
	 */
	public String getDeptIdForm() {
		return deptIdForm;
	}

	/**
	 * @param deptIdForm
	 *            the deptIdForm to set
	 */
	public void setDeptIdForm(String deptIdForm) {
		this.deptIdForm = deptIdForm;
	}

	/**
	 * @return the empRegister
	 */
	public NewEmployeeRegister getEmpRegister() {
		return empRegister;
	}

	/**
	 * @param empRegister
	 *            the empRegister to set
	 */
	public void setEmpRegister(NewEmployeeRegister empRegister) {
		this.empRegister = empRegister;
	}

	/**
	 * @return the isSave
	 */
	public String getIsSave() {
		return isSave;
	}

	/**
	 * @param isSave
	 *            the isSave to set
	 */
	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId
	 *            the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * @return the lastModifiyDate
	 */
	public String getLastModifiyDate() {
		return lastModifiyDate;
	}

	/**
	 * @param lastModifiyDate
	 *            the lastModifiyDate to set
	 */
	public void setLastModifiyDate(String lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
