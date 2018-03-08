/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.empinfo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.StationBean;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ReadXls;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工工号维护Action
 * 
 * @author liuxin
 * 
 */
public class EmpInfoAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private HrJEmpInfoFacadeRemote remote;
	private HrJEmpInfo empInfo;
	@PersistenceContext
	private EntityManager entityManager;
	// 企业编码（反射时check用）
	private static String enterpriseCode;
	// 是否使用
	private static final String YES = "Y";
	private static final String NO = "N";
	/** 人员ID */
	private String empId;
	/** 员工编码 */
	private String empCode;
	/** 员工姓名 */
	private String empName;
	/** 建设码 */
	private String searchCode;
	/** 员工编码是否修改 */
	private String change;
	/** 文件路径 */
	private File xlsFile;
	/** 页面ID */
	private static final String BUSINESS_ID = "PD001";
	/** 员工状态 */
	private static final String empState = "3";

	/** xls文件里的数据 */
	private String totalArray;

	public String getTotalArray() {
		return totalArray;
	}

	public void setTotalArray(String totalArray) {
		this.totalArray = totalArray;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	// 构造函数
	public EmpInfoAction() {
		remote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
	}

	/**
	 * 查询所有在职员工
	 */
	public void getEmpInfo() throws JSONException {
		LogUtil.log("Action:查询在职员工开始。", Level.INFO, null);
		
		// 保存企业编码
		enterpriseCode = employee.getEnterpriseCode();
		//add by fyyang 20100630- 增加分页及按名称查询----
		String chsName=request.getParameter("chsName");
		Object objStart=request.getParameter("start");
		Object objLimit=request.getParameter("limit");
		//List<HrJEmpInfo> dblist;
		PageObject obj=new PageObject();
		if(objStart!=null&&objLimit!=null)
		{
		 int start = Integer.parseInt(request.getParameter("start"));
		 int limit = Integer.parseInt(request.getParameter("limit"));
		 obj = remote.queryAllEmployee(enterpriseCode,
					empState, YES,chsName,start,limit);
		}
		else
		{
			// DB中的数据检索
			obj = remote.queryAllEmployee(enterpriseCode,
					empState, YES,chsName);
		}
		
		//---add end ----------------
		;
		// 从session中取出文件内容
		List<HrJEmpInfo> xlsData = (List<HrJEmpInfo>) session
				.getAttribute("xlsData");
		// 如果文件内容不为空
		// 要写出的数据
		String strRecord = "";
		if (xlsData == null) {
			if (obj != null && obj.getTotalCount() > 0) {
				
				strRecord = JSONUtil.serialize(obj);
				write(strRecord);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
				write(strRecord);
			}
		}
		// session中内容不为空
		else {
			xlsDataBind(obj.getList(), xlsData);
		}
		LogUtil.log("Action:查询在职员工结束。", Level.INFO, null);
	}

	/**
	 * 清除页面session
	 */
	public void clearEmpInfoSession() {
		request.getSession().setAttribute("xlsData", null);
		request.getSession().setAttribute("data", null);
		write(Constants.ADD_SUCCESS);
	}

	/**
	 * 回显excel文件中的内容
	 * 
	 * @param dbData
	 *            数据库中的数据
	 * @param xlsData
	 *            文件中的数据
	 */
	private void xlsDataBind(List<HrJEmpInfo> dbData, List<HrJEmpInfo> xlsData) {
		PageObject resultData = new PageObject();
		// 把session中的数据添加到检索数据的后面
		if (dbData == null) {
			resultData.setList(xlsData);
			resultData.setTotalCount(new Long(xlsData.size()));
		} else {
			dbData.addAll(xlsData);
			resultData.setList(dbData);
			resultData.setTotalCount(new Long(dbData.size()));
		}

		try {
			// 要写出的数据
			String strRecord = "";
			if (resultData.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(resultData);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询在职员工失败。", Level.INFO, null);
		}
	}

	/**
	 * 增加员工
	 */
	public void addEmpInfo() throws CodeRepeatException {
		LogUtil.log("Action:增加员工开始。", Level.INFO, null);
		empInfo = new HrJEmpInfo();
		// 判断人员编码是否重复
		if (remote.findByEmpCode(empCode, employee.getEnterpriseCode()) == 0) {
			try {
				empInfo.setEmpCode(empCode);
				// add by liuyi 20100406 增加员工新工号
				empInfo.setNewEmpCode(empCode);
				empInfo.setEnterpriseCode(Constants.ENTERPRISE_CODE);
				empInfo.setChsName(empName);
				empInfo.setRetrieveCode(searchCode);
				empInfo.setIsUse(YES);
				// 设置创建时间
				empInfo.setCreateDate(new java.util.Date());
				// 设置创建者
				// modified by liuyi 091123 表中属性为number
//				empInfo.setCreateBy(employee.getWorkerCode());
				empInfo.setCreateBy(employee.getWorkerId());
				// 修改人和修改时间
				// modified by liuyi 091123 表中属性为number
//				empInfo.setLastModifiyBy(employee.getWorkerCode());
				empInfo.setLastModifiyBy(employee.getWorkerId());
				empInfo.setLastModifiyDate(new java.util.Date());
				remote.save(empInfo);
				write(Constants.ADD_SUCCESS);
				LogUtil.log("Action:增加员工成功。", Level.INFO, null);
			} catch (Exception e) {
				// modified by liuyi 20100406 出现异常时默认为编码重复异常
//				write(Constants.ADD_FAILURE);
				write("codeRepeat");
				LogUtil.log("Action:增加员工失败。", Level.WARNING, null);
			}
		} else {
			write("codeRepeat");
		}
	}

	/**
	 * 修改员工
	 */
	public void updEmpInfo() throws Exception {
		LogUtil.log("Action:修改员工开始。", Level.INFO, null);
		empInfo = new HrJEmpInfo();
		// 页面数据是否修改
		if ("0".equals(change)) {
			try {
				// 没有找到
				if (remote.findByEmpId(new Long(empId), employee
						.getEnterpriseCode()) == null) {
					throw new Exception();
				} else {
					empInfo = remote.findByEmpId(new Long(empId), employee
							.getEnterpriseCode());
//					empInfo.setEmpCode(empCode);
					// add by liuyi 20100406 增加新工号 且只修改新工号值
					empInfo.setNewEmpCode(empCode);
					empInfo.setChsName(empName);
					empInfo.setRetrieveCode(searchCode);
					// 修改人
					// modified by liuyi 091123 表中属性为number
//					empInfo.setLastModifiyBy(employee.getWorkerCode());
					empInfo.setLastModifiyBy(employee.getWorkerId());
					remote.update(empInfo);
					write(Constants.MODIFY_SUCCESS);
					LogUtil.log("Action:修改员工成功。", Level.INFO, null);
				}
			} catch (Exception e) {
				write("false");
				LogUtil.log("Action:修改员工失败。", Level.INFO, null);
			}
		} else if (remote.findByEmpCode(empCode, employee.getEnterpriseCode()) == 0) {
			try {
				// 没有找到
				if (remote.findByEmpId(new Long(empId), employee
						.getEnterpriseCode()) == null) {
					throw new Exception();
				} else {
					empInfo = remote.findByEmpId(new Long(empId), employee
							.getEnterpriseCode());
//					empInfo.setEmpCode(empCode);
					// add by liuyi 20100406 增加新工号 且只修改新工号值
					empInfo.setNewEmpCode(empCode);
					empInfo.setChsName(empName);
					empInfo.setRetrieveCode(searchCode);
					// 修改人
					// modified by liuyi 091123 表中属性为number
//					empInfo.setLastModifiyBy(employee.getWorkerCode());
					empInfo.setLastModifiyBy(employee.getWorkerId());
					remote.update(empInfo);
					write(Constants.MODIFY_SUCCESS);
					LogUtil.log("Action:修改员工成功。", Level.INFO, null);
				}
			} catch (Exception e) {
//				write("false");
				// modified by liuyi 20100406 出现异常时默认为编码重复异常
				write("codeRepeat");
				LogUtil.log("Action:修改员工失败。", Level.WARNING, null);
			}
		} else {
			write("codeRepeat");
			LogUtil.log("Action:修改员工失败。", Level.INFO, null);
		}
	}

	/**
	 * 删除员工
	 */
	public void deleteEmpInfo() throws Exception {
		LogUtil.log("Action:删除员工开始。", Level.INFO, null);
		empInfo = new HrJEmpInfo();
		empInfo = remote.findByEmpId(new Long(empId), employee
				.getEnterpriseCode());
		try {
			// 没有找到
			if (remote.findByEmpId(new Long(empId), employee
					.getEnterpriseCode()) == null) {
				throw new Exception();
			} else {
				empInfo = remote.findByEmpId(new Long(empId), employee
						.getEnterpriseCode());
				empInfo.setIsUse(NO);
				// 修改人和修改时间
				// modified by liuyi 091123 表中属性为number
//				empInfo.setLastModifiyBy(employee.getWorkerCode());
				empInfo.setLastModifiyBy(employee.getWorkerId());
				remote.update(empInfo);
				write(Constants.MODIFY_SUCCESS);
				LogUtil.log("Action:删除员工成功。", Level.INFO, null);
			}
		} catch (Exception e) {
			write("false");
			LogUtil.log("Action:删除员工失败。", Level.WARNING, null);
		}
	}

	/**
	 * 对导入的文件做check
	 * 
	 * @return
	 * @throws Exception
	 */
	public void importFileCheck() {
		try {
			// 文件名
			String xlsFileName = "";
			// 文件是否存在
			if (xlsFile != null) {
				xlsFileName = xlsFile.getAbsolutePath();
			}
			// 读取文件
			ReadXls readXls = new ReadXls(xlsFileName, BUSINESS_ID);
			// 验证结果
			Map<String, String> checkData = readXls.ValidateXlsData();
			// 错误信息
			StringBuffer sbError = null;
			// 所有数据
			List<HrJEmpInfo> saveList = null;
			// 获取正确的文件内容
			List<HashMap<String, String>> list = null;
			// 文件中的数据
			List<HrJEmpInfo> xlsList = null;

			// checkData为空时，则文件格式正确
			if (checkData == null) {
				list = readXls.getXlsData();
				saveList = new ArrayList<HrJEmpInfo>();
				// 文件中的数据error
				xlsList = new ArrayList<HrJEmpInfo>();
				// 追加数据
				//modify by fyyang 20100630
				PageObject obj= remote.queryAllEmployee(
						Constants.ENTERPRISE_CODE, empState, YES,null);
				List<HrJEmpInfo> dbList=new ArrayList<HrJEmpInfo>();
				if(obj!=null&&obj.getTotalCount()>0)
				{
					dbList=obj.getList();
				}
				// DB中数据追加到所有数据集合中
				if (dbList != null && dbList.size() > 0) {
					saveList.addAll(dbList);
				}
				// fromIndex以后的数据为xls文件里的数据
				int fromIndex = saveList.size();
				// 把list中数据格式化成List<HrJEmpInfo>
				for (int i = 0; i < list.size(); i++) {
					// 定义bean存储
					empInfo = new HrJEmpInfo();
					Map rowMap = list.get(i);
					Iterator it1 = rowMap.keySet().iterator();
					while (it1.hasNext()) {
						String key = (String) it1.next();
						String value = (String) rowMap.get(key);
						// bean中加入员工工号
						if (key != null && "empCode".equals(key.toString()))
							empInfo.setEmpCode(value.toString());

						// bean中加入中文名
						if (key != null && "empName".equals(key.toString()))
							empInfo.setChsName(value.toString());

						// bean中加入检索码
						if (key != null && "searchCode".equals(key.toString()))
							if (value != null)
								empInfo.setRetrieveCode(value.toString());
					}
					saveList.add(saveList.size(), empInfo);
					xlsList.add(xlsList.size(), empInfo);
				}
				// 文件中的数据
				session.setAttribute("xlsData", xlsList);
				// 全部的数据
				session.setAttribute("data", saveList);
				write(Constants.ADD_SUCCESS);
			} else {
				String errorType = checkData.get("errorType");
				if (errorType != Constants.SELF_REPEAT) {
					// 获取错误字段
					String errorField = checkData.get("colomnName");
					// 获取错误行号
					String errorRowNo = checkData.get("rowNo");
					// 编码转换
					String JSONStr = "{success:true,type:'" + errorType
							+ "',param1:'" + errorRowNo + "',param2:'"
							+ errorField + "'}";
					write(JSONStr);
				} else {
					// 获取错误字段
					String errorKey = checkData.get("keys");
					// 获取错误行号
					String errorRow1No = checkData.get("row1No");
					String errorRow2No = checkData.get("rwo2No");
					// 编码转换
					String JSONStr = "{success:true,type:'" + errorType
							+ "',param1:'" + errorRow1No + "',param2:'"
							+ errorRow2No + "',param3:'" + errorKey + "'}";
					write(JSONStr);
				}
			}
		} catch (Exception e) {
			write(Constants.IO_FAILURE);
		}
	}

	/**
	 * 点击确认按钮的操作
	 * 
	 * @return
	 */
	public String codeRepeatCheck(String[] empCode) {
		LogUtil.log("Action:excel数据与数据库数据员工工号重复性check开始。", Level.INFO, null);
		int isExsit = remote.findByEmpCode(empCode[0], enterpriseCode);
		LogUtil.log("Action:excel数据与数据库数据员工工号重复性check结束。", Level.INFO, null);
		if (isExsit > 0) {
			return Constants.DB_EXIST;
		} else {
			return null;
		}
	}

	/**
	 * 导入数据库
	 * 
	 * @return
	 */
	public void importToDatabase() {

		List<HrJEmpInfo> list = new ArrayList<HrJEmpInfo>();
		list.addAll((ArrayList<HrJEmpInfo>) session.getAttribute("xlsData"));
		LogUtil.log("Action:导入员工开始。", Level.INFO, null);
		// 全部导入成功
		if (remote.importToDatabase(list, employee.getWorkerCode(), employee
				.getEnterpriseCode())) {
			clearEmpInfoSession();// modify by ywliu 2009/09/07
			write("importSuccess");
		} else {
			write("importFailture");
		}

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

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public void setEmpInfo(HrJEmpInfo empInfo) {
		this.empInfo = empInfo;
	}

	public File getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}

}