/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.employee.empborrowinregister.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.EmpMoveRegisterFacadeRemote;
import power.ejb.hr.HrJEmployeeborrowIn;
import power.ejb.hr.HrJEmployeeborrowInFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工借调登记
 * @author chen shoujiang
 *  
 */
public class EmpBorrowInRegisterAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	/** 日期格式 */
	private static final String DATE_FORMAT_YYYYMMDD="yyyy-MM-dd";
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    private static final String FINAL_ZERO = "0";
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** check后返回前台的String*/
    private static String STR_CHECK_Y = "{success:true,msg:'Y'}";
    /** check后返回前台的String*/
    private static String STR_CHECK_N = "{success:true,msg:'N'}";
    /** 批处理增加成功*/
    private static String STR_BATCH_ADD_SUCCESS =  "success";
    /**保存数据成功返回前台的String*/
    private static String STR_SAVE_SUCCESS = "{success:true,msg:'success'}";
    /** 返回前台的前面的值*/
    private static String STR_SAVE_FRONT = "{success:true,msg:'";
    /** 返回前台后面的结尾*/
    private static String STR_SAVE_BACK = "'}";
	/** 员工借调登记*/
	private EmpMoveRegisterFacadeRemote empMoveRegisterFacadeRemote;
	/** 员工借调登记 */ 
	private HrJEmployeeborrowInFacadeRemote hrJEmployeeborrowInFacadeRemote;
	/** 员工借调ID */
	private String hrJEmployeeborrowInId;
	/** 员工ID */
	private String empId;
	/** 员工Code*/
	private String empCode;
	/**中文名称 */
	private String chsName;
	/** 所属部门ID */
	private String ssDeptId;
	/** 所属部门名称 */
	private String ssDeptName;
	/** 岗位ID */
	private String stationId;
	/** 岗位名称 */
	private String stationName;
	/** 借调部门ID */
	private String jdDeptId;
	/** 借调部门名称*/
	private String jdDeptName;;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 单据状态 */
	private String dcmStatus;
	/** 备注 */
	private String memo;
	/** 上次修改时间*/
	private String lastModifiedDate;
	/** 是否已回*/
	private String ifBack;
	/** 人员Id数组*/
	private String empidArray;
	/** 开始 */
	private String start;
	private String limit;
	
	/**
	 * 构造函数
	 */
	public EmpBorrowInRegisterAction() {
		/** 员工借调登记*/
		empMoveRegisterFacadeRemote = (EmpMoveRegisterFacadeRemote) factory
			.getFacadeRemote("EmpMoveRegisterFacade");
		/** 员工借调登记 */
		hrJEmployeeborrowInFacadeRemote = (HrJEmployeeborrowInFacadeRemote) factory
			.getFacadeRemote("HrJEmployeeborrowInFacade");
	}
	
	/**
	 * 查询员工借调登记信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getEmpBorrowInRegisterInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询员工借调登记信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// 如果非空
			if (start != null && limit != null && !(Constants.BLANK_STRING).equals(start) && !Constants.BLANK_STRING.equals(limit)) {
				// 有分页信息时执行
				obj = empMoveRegisterFacadeRemote.getEmpMoveRegisterInfo(startDate, endDate, ssDeptId,jdDeptId, 
						dcmStatus, employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
			}else {
				// 无分页信息时执行
				obj = empMoveRegisterFacadeRemote.getEmpMoveRegisterInfo(startDate, endDate, ssDeptId,
						jdDeptId, dcmStatus, employee.getEnterpriseCode());
			}
			// 输出
			String strOutput = Constants.BLANK_STRING;
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询员工借调登记信息结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询员工借调登记信息失败", Level.INFO, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询员工借调登记信息失败", Level.INFO, null);
            throw sqle;
		}
	}
	
	public void getEmpBorrowInRegisterByDeptId() {
		try{
				LogUtil.log("Action:查询员工借调登记信息开始", Level.INFO, null);
				// 分页信息
				PageObject obj = new PageObject();
				// 如果非空
				if (start != null && limit != null && !(Constants.BLANK_STRING).equals(start) && !Constants.BLANK_STRING.equals(limit)) {
					obj = empMoveRegisterFacadeRemote.getEmpBorrowInByDeptId(jdDeptId,
							employee.getEnterpriseCode(), Integer.parseInt(start),Integer.parseInt(limit));
				}else {
					obj = empMoveRegisterFacadeRemote.getEmpBorrowInByDeptId(jdDeptId,
							employee.getEnterpriseCode());
				}
				// 输出
				String strOutput = Constants.BLANK_STRING;
				//　要是查询结果不为空的话，就赋值
				if(obj.getList() != null && obj.getList().size() > 0) {
					strOutput = JSONUtil.serialize(obj);
				} else {
					// 否则设为空值
					strOutput = STR_JSON_NULL;
				}
				write(strOutput);
				LogUtil.log("Action:查询员工借调登记信息结束", Level.INFO, null);
			} catch (JSONException jsone) {
	            write(Constants.DATA_FAILURE);
				LogUtil.log("Action:查询员工借调登记信息失败", Level.INFO, null);
			} catch (SQLException sqle) {
	            write(Constants.SQL_FAILURE);
	            LogUtil.log("Action:查询员工借调登记信息失败", Level.INFO, null);
			}
	}
	
	/**
	 * 删除员工借调登记记录
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void deleteEmpBorrowInRegisterInfo() throws JSONException, Exception {
		LogUtil.log("Action:删除员工借调登记记录开始", Level.INFO, null);
		try{
			// 创建一个
			HrJEmployeeborrowIn entity = new HrJEmployeeborrowIn();
			// 如果id非空的话
			if(null != hrJEmployeeborrowInId && !Constants.BLANK_STRING.equals(hrJEmployeeborrowInId))
			{
				// 找到对应序号信息		
				entity = hrJEmployeeborrowInFacadeRemote.findById(Long.parseLong(hrJEmployeeborrowInId));
				entity.setLastModifiedDate(formatStringToDate(lastModifiedDate,
						DATE_FORMAT_YYYYMMDD_TIME_SEC));
				// 上次修改人
				entity.setLastModifiedBy(employee.getWorkerCode());
				hrJEmployeeborrowInFacadeRemote.delete(entity);
			}
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除员工借调登记结束", Level.INFO, null);
		}catch (DataChangeException e){
			LogUtil.log("Action:删除员工借调登记失败", Level.SEVERE, null);
            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:删除员工借调登记失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 *上报员工借调登记
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void reportEmpBorrowInRegisterInfo() throws JSONException, Exception {
		LogUtil.log("Action:上报员工借调登记开始", Level.INFO, null);
		try{
			// 创建一个
			HrJEmployeeborrowIn entity = new HrJEmployeeborrowIn();
			// 如果id非空的话
			if(null != hrJEmployeeborrowInId && !Constants.BLANK_STRING.equals(hrJEmployeeborrowInId))
			{
				// 找到对应序号信息		
				entity = hrJEmployeeborrowInFacadeRemote.findById(Long.parseLong(hrJEmployeeborrowInId));
				entity.setLastModifiedDate(formatStringToDate(lastModifiedDate,
						DATE_FORMAT_YYYYMMDD_TIME_SEC));
				// 上次修改人
				entity.setLastModifiedBy(employee.getWorkerCode());
				hrJEmployeeborrowInFacadeRemote.report(entity);
			}
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:上报员工借调登记结束", Level.INFO, null);
		}catch (DataChangeException e){
			LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 * 通过check所选员工是否已借调出
	 */
	public void checkEmpIsBorrowIn() {
		LogUtil.log("Action:check所选员工是否已借调出开始", Level.INFO, null);
		try{
			// 如果empId非空的话
			if(null != empId && !Constants.BLANK_STRING.equals(empId))
			{
				Boolean flag = hrJEmployeeborrowInFacadeRemote.checkIfBackByEmpId(empId, employee.getEnterpriseCode());
				if(flag == true)
					write(STR_CHECK_Y);
				else if(flag == false)
					write(STR_CHECK_N);
			}
			LogUtil.log("Action:check所选员工是否已借调出结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action:check所选员工是否已借调出失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 * 保存员工借调登记
	 * @throws DataFormatException
	 */
	public void addEmpBorrowInRegister() throws DataFormatException {
		LogUtil.log("Action:保存员工借调登记开始", Level.INFO, null);
		try{
			// 创建一个
			HrJEmployeeborrowIn entity = new HrJEmployeeborrowIn();
			entity.setEmpId(Long.parseLong(empId));
			if(checkNull(jdDeptId))
				entity.setBorrowDeptId(Long.parseLong(jdDeptId));
			entity.setIfBack(FINAL_ZERO);
			entity.setDcmStatus(FINAL_ZERO);
			if(checkNull(startDate))
				entity.setStartDate(formatStringToDate(startDate,DATE_FORMAT_YYYYMMDD));
			if(checkNull(endDate))
				entity.setEndDate(formatStringToDate(endDate,DATE_FORMAT_YYYYMMDD));
			if(checkNull(memo))
				entity.setMemo(memo);
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			entity.setLastModifiedBy(employee.getWorkerCode());
			hrJEmployeeborrowInFacadeRemote.save(entity);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:保存员工借调登记结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action:保存员工借调登记失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 * 更新员工借调登记
	 * @throws DataFormatException
	 */
	public void updateEmpBorrowInRegister() throws DataFormatException {
		LogUtil.log("Action:保存员工借调登记开始", Level.INFO, null);
		try{
			// 创建一个
			HrJEmployeeborrowIn entity = new HrJEmployeeborrowIn();
			// 如果id非空的话
			if(null != hrJEmployeeborrowInId && !Constants.BLANK_STRING.equals(hrJEmployeeborrowInId))
			{
				// 找到对应序号信息		
				entity = hrJEmployeeborrowInFacadeRemote.findById(Long.parseLong(hrJEmployeeborrowInId));
				entity.setLastModifiedDate(formatStringToDate(lastModifiedDate,
						DATE_FORMAT_YYYYMMDD_TIME_SEC));
				entity.setIfBack(ifBack);
				if(checkNull(jdDeptId)) 
					entity.setBorrowDeptId(Long.parseLong(jdDeptId));
				if(checkNull(startDate))
					entity.setStartDate(formatStringToDate(startDate,DATE_FORMAT_YYYYMMDD));
				if(checkNull(endDate))
					entity.setEndDate(formatStringToDate(endDate,DATE_FORMAT_YYYYMMDD));
				if(checkNull(memo))
					entity.setMemo(memo);
				// 上次修改人
				entity.setLastModifiedBy(employee.getWorkerCode());
				hrJEmployeeborrowInFacadeRemote.update(entity);
				write(Constants.MODIFY_SUCCESS);
			}
		}catch (DataChangeException e){
			LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 * 批处理增加数据
	 * @throws DataFormatException
	 * @throws SQLException 
	 */
	public void batchAddEmpBorrowInRegister() throws DataFormatException, SQLException {
		try{
			// 创建一个
			HrJEmployeeborrowIn entity = new HrJEmployeeborrowIn();
			entity.setBorrowDeptId(Long.parseLong(jdDeptId));
			entity.setIfBack(FINAL_ZERO);
			entity.setDcmStatus(FINAL_ZERO);
			entity.setStartDate(formatStringToDate(startDate,DATE_FORMAT_YYYYMMDD));
			entity.setEndDate(formatStringToDate(endDate,DATE_FORMAT_YYYYMMDD));
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			entity.setLastModifiedBy(employee.getWorkerCode());
			String flag = hrJEmployeeborrowInFacadeRemote.batchAddData(empidArray, entity);
			if(flag == STR_BATCH_ADD_SUCCESS)
				write(STR_SAVE_SUCCESS);
			else {
				String ss = STR_SAVE_FRONT+ flag + STR_SAVE_BACK;
				write(ss);
			}
		}catch (SQLException sqle) {
            LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
            throw sqle;
		} 
	}
	
	/**
	 * 批处理时修改数据
	 * @throws DataChangeException 
	 * @throws DataFormatException 
	 */
	public void batchUpdateEmpBorrowInRegister() throws DataFormatException, DataChangeException {
		try{
			// 创建一个
			hrJEmployeeborrowInFacadeRemote.batchUpdateData(borrowInArray, endDate, employee.getWorkerCode());
			write(Constants.MODIFY_SUCCESS);
		}catch(DataFormatException e){
			write(Constants.DATA_FAILURE);
		}catch (DataChangeException e){
			LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:上报员工借调登记失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
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

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			dtresult = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			dtresult = null;
		} finally {
			sdfFrom = null;
		}

		return dtresult;
	}
	
	/**
	 * check字符串是否为空或者null
	 */
	private Boolean checkNull(String chaStr) {
		if(chaStr != null && !chaStr.equals(Constants.BLANK_STRING))
			return true;
		else 
			return false;
	}
	public String getHrJEmployeeborrowInId() {
		return hrJEmployeeborrowInId;
	}

	public void setHrJEmployeeborrowInId(String hrJEmployeeborrowInId) {
		this.hrJEmployeeborrowInId = hrJEmployeeborrowInId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getChsName() {
		return chsName;
	}

	public void setChsName(String chsName) {
		this.chsName = chsName;
	}

	public String getSsDeptId() {
		return ssDeptId;
	}

	public void setSsDeptId(String ssDeptId) {
		this.ssDeptId = ssDeptId;
	}

	public String getSsDeptName() {
		return ssDeptName;
	}

	public void setSsDeptName(String ssDeptName) {
		this.ssDeptName = ssDeptName;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getJdDeptId() {
		return jdDeptId;
	}

	public void setJdDeptId(String jdDeptId) {
		this.jdDeptId = jdDeptId;
	}

	public String getJdDeptName() {
		return jdDeptName;
	}

	public void setJdDeptName(String jdDeptName) {
		this.jdDeptName = jdDeptName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDcmStatus() {
		return dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	private String borrowInArray;
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getBorrowInArray() {
		return borrowInArray;
	}

	public void setBorrowInArray(String borrowInArray) {
		this.borrowInArray = borrowInArray;
	}

	public String getEmpidArray() {
		return empidArray;
	}

	public void setEmpidArray(String empidArray) {
		this.empidArray = empidArray;
	}

	public String getIfBack() {
		return ifBack;
	}

	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}
}
