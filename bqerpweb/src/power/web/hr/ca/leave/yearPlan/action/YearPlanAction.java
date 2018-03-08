/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.hr.ca.leave.yearPlan.action;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.CodeConstants;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.HrCVacationtype;
import power.ejb.hr.ca.HrCYearPlanFields;
import power.ejb.hr.ca.HrCYearPlanTwo;
import power.ejb.hr.ca.HrCYearplan;
import power.ejb.hr.ca.HrCYearplanFacadeRemote;
import power.ejb.hr.ca.HrCYearplanId;
import power.web.comm.AbstractAction;

public class YearPlanAction extends AbstractAction {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /** 查询，保存，修改数据远程对象 */
    private HrCYearplanFacadeRemote remote;
    /** 部门id */
    private String deptId;
    /** 是否包含下级部门标志 */
    private String selectP;
    /** 年份 */
    private String year;
    /** 修改记录集 */
    private String updateRecords;
    /** 假别的种类 */
    private int colmunNum;
    /** 保存，上报标志*/
    private int reportFlag;

    /**
     * @return the reportFlag
     */
    public int getReportFlag() {
        return reportFlag;
    }

    /**
     * @param reportFlag the reportFlag to set
     */
    public void setReportFlag(int reportFlag) {
        this.reportFlag = reportFlag;
    }

    /**
     * 构造函数
     */
    public YearPlanAction() {
        remote = (HrCYearplanFacadeRemote) factory
                .getFacadeRemote("HrCYearplanFacade");
    }
    /**
	 * 取得登陆人员所在部门name 和 id 
	 */
	public void getEmpOfDeptRecords() {
		try {
			LogUtil.log("Action:取得登陆人员所在部门name 和 id 开始。", Level.INFO, null);
			StringBuffer str = new StringBuffer();
			if(employee.getDeptId()!=null) {
				str.append("{\"list\":[{\"deptId\":\"")
					// 部门id
					.append(employee.getDeptId())
					.append("\",\"deptName\":\"")
					// 部门名称
					.append(employee.getDeptName())
					// 数据总条数
					.append(
							"\"}],\"totalCount\":1}");
			} else {
				str.append("{\"list\":[],\"totalCount\":null}");
			}
			// 输出
			write(str.toString());
			LogUtil.log("Action:取得登陆人员所在部门name 和 id 结束。", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:取得登陆人员所在部门name 和 id 失败。", Level.SEVERE, e);
		}
	}
    /**
     * 年初计划登记 查询所有信息
     * 
     * @throws SQLException，JSONException
     * @return void
     */
    public void searchYearPlanList() throws SQLException, JSONException {
        List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
        String root = "list";
        String totalProperty = "totalCount";
        StoreObject storeObj = new StoreObject();
        MetaData metas = new MetaData();
        // 用于获得假别列表
        List<HrCVacationtype> vacationType = new ArrayList<HrCVacationtype>();
        // 用于获得人员列表
        List<Map<String, Object>> yearPlanList = new ArrayList<Map<String, Object>>();
        try {
            LogUtil.log("Action:年初计划登记查询开始。", Level.INFO, null);
            HrCYearPlanFields obj = new HrCYearPlanFields();
            boolean selectPDept = Boolean.parseBoolean(selectP);
            // 查询所有假别信息
            obj = remote.findAllVacation(year, deptId,selectPDept,
                    employee.getEnterpriseCode());
            // 拼凑list
            yearPlanList = this.setBody(obj.getYearPlanList(), obj
                    .getVacationType().size());
            // 获得假别列表
            vacationType = obj.getVacationType();

            // -------- 根据假别列表，设置第二行表头信息-------------

            // 行号
            Map<String, Object> num = new HashMap<String, Object>();
            num.put("name", "num");
            num.put("header", "行号");
            num.put("sortable", false);
            num.put("fixed", true);
            num.put("menuDisabled", true);
            num.put("rowspan", "undefined");
            num.put("renderer", "numbererW");
            num.put("width", 35);
            num.put("align", "right");
            fields.add(num);

            // 员工姓名表头
            Map<String, Object> chsName = new HashMap<String, Object>();
            chsName.put("name", "chsName");
            chsName.put("dataIndex", "chsName");
            chsName.put("header", "员工姓名");
            chsName.put("width", 150);
            chsName.put("sortable", true);
            fields.add(chsName);

            // 员工所在部门表头
            Map<String, Object> deptName = new HashMap<String, Object>();
            deptName.put("name", "deptName");
            deptName.put("dataIndex", "deptName");
            deptName.put("header", "所属部门");
            deptName.put("width", 100);
            deptName.put("sortable", true);
            fields.add(deptName);

            // 时长和天数的表头
            for (int i = 0; i < vacationType.size(); i++) {
                // 假别id隐藏列
                Map<String, Object> vacationTypeId = new HashMap<String, Object>();
                vacationTypeId.put("name", "vacationTypeId" + i);
                vacationTypeId.put("dataIndex", "vacationTypeId" + i);
                vacationTypeId.put("hidden", "true");
                vacationTypeId.put("sortable", true);
                fields.add(vacationTypeId);

                // 天数的表头
                Map<String, Object> days = new HashMap<String, Object>();
                days.put("name", "days" + i);
                days.put("dataIndex", "days" + i);
                days.put("header", "天数");
                days.put("renderer", "numberFormat2");
                days.put("editor", "txtExchageHours");
                days.put("width", 80);
                days.put("align", "right");
                days.put("sortable", true);
                days.put("css", "CSS_GRID_INPUT_COL");
                days.put("sortType", "asFloat");
                fields.add(days);
                // 时长的表头
                Map<String, Object> hours = new HashMap<String, Object>();
                hours.put("name", "hours" + i);
                hours.put("dataIndex", "hours" + i);
                hours.put("header", "时长");
                hours.put("renderer", "numberFormat1");
                hours.put("width", 80);
                hours.put("align", "right");
                hours.put("sortable", true);
                hours.put("sortType", "asFloat");

                fields.add(hours);
            }

            // 审批状态的表头
            Map<String, Object> signState = new HashMap<String, Object>();
            signState.put("name", "signState");
            signState.put("dataIndex", "signState");
            signState.put("header", "审批状态");
            signState.put("width", 100);
            signState.put("renderer", "statesFormat");
            signState.put("sortable", true);
            fields.add(signState);

            // 标准出勤时间
            Map<String, Object> standardTime = new HashMap<String, Object>();
            standardTime.put("name", "standardTime");
            standardTime.put("dataIndex", "standardTime");
            standardTime.put("header", "标准出勤时间");
            standardTime.put("sortable", true);
            standardTime.put("hidden", "true");
            fields.add(standardTime);

            // 判断是否是新增数据
            Map<String, Object> isNew = new HashMap<String, Object>();
            isNew.put("name", "isNew");
            isNew.put("dataIndex", "isNew");
            isNew.put("header", "是否新增");
            isNew.put("sortable", true);
            isNew.put("hidden", "true");
            fields.add(isNew);

            // 判断是否被修改
            Map<String, Object> isChange = new HashMap<String, Object>();
            isChange.put("name", "isChange");
            isChange.put("dataIndex", "isChange");
            isChange.put("header", "是否被修改");
            isChange.put("hidden", "true");
            fields.add(isChange);

            // 员工id
            Map<String, Object> empId = new HashMap<String, Object>();
            empId.put("name", "empId");
            empId.put("dataIndex", "empId");
            empId.put("header", "员工id");
            empId.put("hidden", "true");
            fields.add(empId);

            // 把第二行的所有表头添加到metas里
            metas.setFields(fields);

            // -------- 根据假别列表，设置第二行表头信息-------------

            // -------- 根据假别列表，设置第一行表头信息-------------

            // 添加第一行的表头
            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            // 行号
            rows.add(new HashMap<String, Object>());
            // 人员姓名
            rows.add(new HashMap<String, Object>());
            // 部门
            rows.add(new HashMap<String, Object>());
            // 依次读出循环中的假别id和假别name,并添加到rows
            for (int i = 0; i < vacationType.size(); i++) {
                // 假别id对应的表头
                rows.add(new HashMap<String, Object>());
                Map<String, Object> vt = new HashMap<String, Object>();
                vt.put("colspan", 2);
                vt.put("header", vacationType.get(i).getVacationType());
                vt.put("align", "center");
                rows.add(vt);
            }
            // 审批状态
            rows.add(new HashMap<String, Object>());
            // 标准出勤时间
            rows.add(new HashMap<String, Object>());
            // 判断是否是新增数据
            rows.add(new HashMap<String, Object>());
            // 员工id
            rows.add(new HashMap<String, Object>());
            // 判断是否被修改
            rows.add(new HashMap<String, Object>());
            // -------- 根据假别列表，设置第一行表头信息-------------

            // -------- 设置grid各个属性 -------------------------
            metas.setId("emp");
            metas.setRoot(root);
            metas.setTotalProperty(totalProperty);
            // 设置第一行grid表头
            metas.setRows(rows);
            // 设置第二行grid表头
            storeObj.setMetaData(metas);
            // 设置grid各列显示内容
            storeObj.setList(yearPlanList);
            // 设置总条数
            storeObj.setTotalCount((long) yearPlanList.size());
            // -------- 设置grid各个属性 -------------------------

            write(JSONUtil.serialize(storeObj));

            LogUtil.log("Action:年初计划登记查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:年初计划登记查询失败。", Level.SEVERE, jsone);
            throw jsone;
        } catch (SQLException sqle) {
            LogUtil.log("Action:年初计划登记查询失败。", Level.SEVERE, sqle);
            throw sqle;
        }
    }

    /***************************************************************************
     * 拼凑grid各显示行的内容
     * 
     * @return List
     */
    public List<Map<String, Object>> setBody(List<HrCYearPlanTwo> yearList,
            int num) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int j = 1;
        // 遍历整个list
        for (int i = 0; i < yearList.size(); i = i + num) {
            Map<String, Object> year = new HashMap<String, Object>();
            // 行号
            year.put("num", j++);
            // 设置员工id
            year.put("empId", yearList.get(i).getEmpId());
            // 中文名
            year.put("chsName", yearList.get(i).getChsName());
            // 部门名称
            year.put("deptName", yearList.get(i).getDeptName());
            // 获得标准出勤时间
            double standardTime = remote.findStandardTime(this.year, employee
                    .getEnterpriseCode(), yearList.get(i).getEmpId());
            // 设置标准出勤时间
            year.put("standardTime", standardTime);
            // 设置是否修改字段
            year.put("isChange", "false");
            // 各假别的时长和天数
            for (int m = 0; m < num; m++) {
                // 设置假别id的列值
                year.put("vacationTypeId" + m, yearList.get(m)
                        .getVacationTypeId());
                // 数字格式化函数
                // 天数保留2位小数
                DecimalFormat df2 = new DecimalFormat("0.00");
                // 时长保留一位小数
                DecimalFormat df4 = new DecimalFormat("0.0");

                // 假别m对应的时长和天数
                String strHours = yearList.get(m + i).getHours();
                String strDays = yearList.get(m + i).getDays();

                // 数据库中不存在此人员的假别信息时
                if (strHours == null || "".equals(strHours)) {
                    // 如果数据库中时长为空，则为新增数据
                    year.put("isNew", "true");
                    if (yearList.get(m + i).getVacationTypeId().equals("1")) {
                        // 当假别id为0时，换休情况下
                        // 获得时长时间
                        int intYear = Integer.parseInt(this.year);
                        // 得到去年的值
                        intYear = intYear-1;
                        String strYear = Integer.toString(intYear); 
                        double time = remote.findExchangeTime(strYear, employee.getEnterpriseCode(), yearList.get(m + i).getEmpId());
                        // 设置时长
                        year.put("hours" + m, df4.format(time));
                        // 设置天数
                        String strDouDays = "0.00";
                        if (standardTime != 0) {
                            strDouDays = df2.format(time / standardTime);
                        }
                        year.put("days" + m, strDouDays);
                    } else {
                        // 非换休情况下
                        year.put("hours" + m, df4.format(0));
                        year.put("days" + m, df2.format(0));
                    }
                } else {
                    // 存在假别信息，则直接赋值
                    year.put("hours"+m, df4.format(Double.parseDouble(strHours)));
                    year.put("days"+m, df2.format(Double.parseDouble(getStringZ(strDays))));
                    // 此数据已在数据库中存在
                    year.put("isNew", "false");
                }
            }
            // 审批状态
            if (yearList.get(i).getSignState() == null
                    || "".equals(yearList.get(i).getSignState())) {
                year.put("signState", "0");
            } else {
                year.put("signState", yearList.get(i).getSignState());
            }
            list.add(year);
        }
        return list;
    }

    /**
     * 保存数据
     * 
     * @throws JSONException
     * @return void
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public void saveYearPlanByVacation() throws JSONException, SQLException {
        // 修改的记录
        List<Map> updateAllRecords = (List<Map>) JSONUtil
                .deserialize(updateRecords);
        LogUtil.log("Action:保存年初计划登记开始。", Level.INFO, null);
        try {
            // 修改信息
            List<List> saveList = update(updateAllRecords);
            // 保存数据
            remote.updateAndInsertYearPlan(saveList.get(0), saveList.get(1));
            LogUtil.log("Action:保存年初计划登记结束。", Level.INFO, null);
        } catch (RuntimeException e) {
            LogUtil.log("Action:保存年初计划登记失败。", Level.SEVERE, e);
            throw e;
        }
    }

    /**
     * 更新或插入数据
     * 
     * @throws RuntimeException
     * @return List<List>
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public List<List> update(List<Map> updateAllRecords) throws RuntimeException, SQLException {
        // List 用于保存数据
        List<List> listAll = new ArrayList<List>();
        List <HrCYearplan> listForUpdate = new ArrayList<HrCYearplan>();
        List <HrCYearplan> listForInsert = new ArrayList<HrCYearplan>();
        // 保存修改信息
        Map map;
        // 循环
        for (int i = 0; i < updateAllRecords.size(); i++) {
			// 修改记录的map
			map = updateAllRecords.get(i);
			// 年初计划表登记
			// 获得人员id
			String empId = getString(map.get("empId"));
			// 获得签字状态
			String signState = getString(map.get("signState"));
			// 0：未上报 3：已退回
			if (CodeConstants.FROM_STATUS_0.equals(signState)
					|| CodeConstants.FROM_STATUS_3.equals(signState)) {
				// 修改的记录
				// 对数据进行处理
				for (int j = 0; j < colmunNum; j++) {
					// 获得假别id
					String vacationTypeId = getString(map.get("vacationTypeId"
							+ j));
					List<HrCYearplan> list = null;
					// 根据人员id,年份，假别id 搜索数据
					try {
						list = remote.searchMsgByLogicId(year, empId,
								vacationTypeId, employee.getEnterpriseCode());
					} catch (SQLException e) {
						throw new RuntimeException();
					}
					if (list != null) {
						HrCYearplan entity = new HrCYearplan();
						// db中存在此数据
						entity = list.get(0);
						// 设置天数
						entity.setDays(Double.parseDouble(getString((map
								.get("days" + j)))));
						// 设置时长
						entity.setHours(Double.parseDouble(getString((map
								.get("hours" + j)))));
						// 上次修改人
						entity.setLastModifiyBy(employee.getWorkerCode());
						// 把得到的数据放入updateList中
						listForUpdate.add(entity);
					} else {
						HrCYearplan entity = new HrCYearplan();
						// db 中不存在此数据,插入此条数据
						// 创建逻辑主键bean
						HrCYearplanId logicId = new HrCYearplanId();
						// 设置人员id
						logicId.setEmpId(Long.parseLong(getString((map
								.get("empId")))));
						// 设置年份
						logicId.setPlanYear(year);
						// 设置假别id
						logicId.setVacationTypeId(Long
								.parseLong((vacationTypeId)));
						// 设置主键
						entity.setId(logicId);
						// 天数
						entity.setDays(Double.parseDouble(getString(map
								.get("days" + j))));
						// 时长
						entity.setHours(Double.parseDouble(getString(map
								.get("hours" + j))));
						// 签字状态
						entity.setSignState(CodeConstants.FROM_STATUS_0);
						// 上次修改人
						entity.setLastModifiyBy(employee.getWorkerCode());
						// 企业编码
						entity.setEnterpriseCode(employee.getEnterpriseCode());
						// 插入数据
						listForInsert.add(entity);
					}
				}
			}

		}
		listAll.add(listForUpdate);
		listAll.add(listForInsert);
		return listAll;
	}

    /**
	 * 上报数据
	 * 
	 * @throws RuntimeException
	 * @return void
	 * @throws SQLException
	 * @throws JSONException
	 */
    @SuppressWarnings("unchecked")
    public void reportRecords()
            throws RuntimeException, SQLException, JSONException {
        LogUtil.log("ACTION:上报年初计划登记表信息开始", Level.INFO, null);
        List<Map> updateAllRecords = (List<Map>) JSONUtil.deserialize(updateRecords);
        List<List> saveList = null;
        // 用于存储上报数据信息
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            // 仅上报
            if(reportFlag == 0) {
                list = reportMsg(updateAllRecords);
            }else {
                saveList = update(updateAllRecords);
                list = reportMsg(updateAllRecords);
            }
            remote.saveAndReportYearPlan(list, saveList);
            LogUtil.log("ACTION:上报年初计划登记表信息结束", Level.INFO, null);
        } catch (SQLException e) {
            LogUtil.log("ACTION:上报年初计划登记表信息失败", Level.SEVERE, e);
            throw e;
        }
    }

    /**
     * 上报操作
     * 
     * @throws RuntimeException
     * @return List<Map<String, Object>>
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> reportMsg(List<Map> updateAllRecords) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map map;
        // 循环读出每条记录内容
        for (int i = 0; i < updateAllRecords.size(); i++) {
            // 修改记录的map
            map = updateAllRecords.get(i);
            // 获得人员id
            String empId = getString(map.get("empId"));
            // 获得签字状态
            String signState = getString(map.get("signState"));
            // 0：未上报 3：已退回
            if (CodeConstants.FROM_STATUS_0.equals(signState)
                    || CodeConstants.FROM_STATUS_3.equals(signState)) {
                // 如果单据状态为未上报0，或已退回3的状态，则上报此人的数据
                Map<String, Object> reportRecord = new HashMap<String, Object>();
                // 行号
                reportRecord.put("empId", empId);
                reportRecord.put("year", year);
                reportRecord.put("enterpriseCode", employee.getEnterpriseCode());
                reportRecord.put("lastModifiyBy", employee.getWorkerCode());
                list.add(reportRecord);
            }
        }
        return list;
    }
    /**
     * 获得字符串值
     */
    private String getString(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }

    }
    private String getStringZ(String obj) {
    	if (obj == null || "".equals(obj)) {
    		return "0";
        } else {
        	return obj;
        }
    }
    /**
     * @return the deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     *            the deptId to set
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the updateRecords
     */
    public String getUpdateRecords() {
        return updateRecords;
    }

    /**
     * @param updateRecords
     *            the updateRecords to set
     */
    public void setUpdateRecords(String updateRecords) {
        this.updateRecords = updateRecords;
    }


    /**
     * @return the colmunNum
     */
    public int getColmunNum() {
        return colmunNum;
    }

    /**
     * @param colmunNum
     *            the colmunNum to set
     */
    public void setColmunNum(int colmunNum) {
        this.colmunNum = colmunNum;
    }

	/**
	 * @return the selectP
	 */
	public String getSelectP() {
		return selectP;
	}

	/**
	 * @param selectP the selectP to set
	 */
	public void setSelectP(String selectP) {
		this.selectP = selectP;
	}


	
}
