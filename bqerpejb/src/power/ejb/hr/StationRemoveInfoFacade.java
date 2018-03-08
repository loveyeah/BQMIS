/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 班组人员调动Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Stateless
public class StationRemoveInfoFacade implements StationRemoveInfoFacadeRemote {
    @PersistenceContext
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    // @EJB(beanName = "HrJStationremove")
    /** 岗位调动Remote */
    private HrJStationremoveFacadeRemote hrJStationremoveRemote;
    // @EJB(beanName = "HrJEmpInfo")
    /** 人员基本信息Remote */
    private HrJEmpInfoFacadeRemote hrJEmpInfoRemote;
    // 是班组("1")
    private static String STR_IS_BANZHU_1 = "1";
    // 是使用("Y")
    private static String STR_IS_USE_Y = "Y";
    // 岗位调动类别(班组间:"0")
    private static String STR_STATION_MOVETYPE_0 = "0";

    /**
     * 获得班组人员调动信息
     * 
     * @param startDate
     *                调动开始日期
     * @param endDate
     *                调动结束日期
     * @param oldDeptId
     *                调动前部门ID
     * @param newDeptId
     *                调动后部门ID
     * @param enterpriseCode
     *                企业编码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
    @SuppressWarnings("unchecked")
    public PageObject getStationRemoveInfoList(String startDate,
	    String endDate, String oldDeptId, String newDeptId,
	    String enterpriseCode, final int... rowStartIdxAndCount) {
	try {
	    PageObject result = new PageObject();
	    // 查询sql
	    String strSql = "select B.CHS_NAME, "
		    + "  A.Remove_Date, "
		    + "  C.Dept_Name cname, "
		    + "  D.Dept_Name dname,"
		    + "  A.Do_Date, "
		    + "  A.Memo, "
		    + "  A.Stationremoveid, "
		    + "  E.Station_Move_Type, "
		    + "  A.Emp_Id, "
		    + "  B.EMP_CODE, "
		    + "  to_char( A.Last_Modified_Date,'yyyy-mm-dd hh24:mi:ss') ";
	    String sqlCount = "select count(distinct A.Stationremoveid) ";
	    String fromStr = "from " + "  HR_J_STATIONREMOVE   A  "
//		    + "Left join   HR_J_EMP_INFO B "
//	            + "ON  A.Emp_Id = B.EMP_ID AND B.Enterprise_Code = ? "
	            + "INNER join   HR_J_EMP_INFO B "
	            // modify by liuyi 090914 B表无is_use属性
//		    + "ON  A.Emp_Id = B.EMP_ID AND B.Enterprise_Code = ? AND B.IS_USE = ? "
	            + "ON  A.Emp_Id = B.EMP_ID AND B.Enterprise_Code = ? "
		    + "Left join   HR_C_DEPT     C "
		    + "ON  A.Old_Dep_Id = C.DEPT_ID "//AND C.IS_BANZU = ? "
		    + "   AND C.Enterprise_Code = ? "
		    + "Left join   HR_C_DEPT     D "
		    + "ON  A.New_Dep_Id = D.DEPT_ID "//AND D.IS_BANZU = ? "
		    + "   AND D.Enterprise_Code = ? "
		    + "Left join   HR_C_STATIONMOVETYPE E "
		    + "ON  A.Station_Move_Type_Id = E.Station_Move_Type_Id "
		    + "   AND E.ENTERPRISE_CODE = ? AND E.IS_USE = ? ";

	    String strSqlWhere = "where A.IS_USE = ? "
		    + "  AND A.ENTERPRISE_CODE = ? "
		    + "  AND A.Station_Move_Type_Id = ? ";
	    // 查询参数数量
	    int paramsCnt = 8;
	    if (checkIsNotNull(startDate)) {
		paramsCnt++;
	    }
	    if (checkIsNotNull(endDate)) {
		paramsCnt++;
	    }
	    if (checkIsNotNull(oldDeptId)) {
		paramsCnt++;
	    }
	    if (checkIsNotNull(newDeptId)) {
		paramsCnt++;
	    }
	    // 查询参数数组
	    Object[] params = new Object[paramsCnt];
	    int i = 0;
	    params[i++] = enterpriseCode;
	    // modify by liuyi 090914 去掉一个is_use条件
//	    params[i++] = STR_IS_USE_Y;
//	    params[i++] = STR_IS_BANZHU_1;
	    params[i++] = enterpriseCode;
//	    params[i++] = STR_IS_BANZHU_1;
	    params[i++] = enterpriseCode;
	    params[i++] = enterpriseCode;
	    params[i++] = STR_IS_USE_Y;
	    params[i++] = STR_IS_USE_Y;
	    params[i++] = enterpriseCode;
	    params[i++] = STR_STATION_MOVETYPE_0;
	    if (checkIsNotNull(startDate)) {
		strSqlWhere += "  and to_char(A.Remove_Date,'yyyy-mm-dd') >= ? ";
		params[i++] = startDate;
	    }
	    // 结束日期
	    if (checkIsNotNull(endDate)) {
		strSqlWhere += "  and to_char(A.Remove_Date,'yyyy-mm-dd') <= ? ";
		params[i++] = endDate;
	    }
	    // 部门编码
	    if (checkIsNotNull(oldDeptId)) {
		strSqlWhere += " and A.Old_Dep_Id = ? ";
		// strSqlWhere += " and d.dept_code like ? ";
		params[i++] = oldDeptId;
	    }
	    // 人员编码
	    if (checkIsNotNull(newDeptId)) {
		strSqlWhere += " and A.New_Dep_Id = ? ";
		params[i++] = newDeptId;
	    }
	    // 拼接查询sql
	    strSql = strSql + fromStr + strSqlWhere + " ORDER BY A.Stationremoveid ";
	    sqlCount = sqlCount + fromStr + strSqlWhere;
	    LogUtil.log("EJB:班组人员调动查询开始。", Level.INFO, null);
	    LogUtil.log("SQL=" + strSql, Level.INFO, null);

	    // list
	    List list = bll.queryByNativeSQL(strSql, params,
		    rowStartIdxAndCount);
	    List<StationRemoveInfo> arrList = new ArrayList<StationRemoveInfo>();
	    Iterator it = list.iterator();
	    while (it.hasNext()) {
		Object[] data = (Object[]) it.next();
		StationRemoveInfo info = new StationRemoveInfo();

		// 人员姓名
		if (data[0] != null) {
		    info.setEmpName(data[0].toString());
		}
		// 调动日期
		if (data[1] != null) {
		    info.setRemoveDate(data[1].toString());
		}
		// 调动前部门名称
		if (data[2] != null) {
		    info.setOldDepName(data[2].toString());
		}
		// 调动后部门名称
		if (data[3] != null) {
		    info.setNewDepName(data[3].toString());
		}
		// 执行日期
		if (data[4] != null) {
		    info.setDoDate(data[4].toString());
		}
		// 备注
		if (data[5] != null) {
		    info.setMemo(data[5].toString());
		}

		// 岗位调动单ID
		if (data[6] != null) {
		    info.setStationremoveid(data[6].toString());
		}
		// 岗位调动类别名称
		if (data[7] != null) {
		    info.setStationMoveTypeName(data[7].toString());
		}
		// 人员ID
		if (data[8] != null) {
		    info.setEmpId(data[8].toString());
		}
		// 人员编码(工号)
		if (data[9] != null) {
		    info.setEmpCode(data[9].toString());
		}
		// 上次修改日期
		if (data[10] != null) {
		    info.setLastModifiedDate(data[10].toString());
		}
		arrList.add(info);
	    }
	    // 按查询结果集设置返回结果
	    if (arrList.size() == 0) {
		// 设置查询结果集
		result.setList(null);
		// 设置行数
		result.setTotalCount(0L);
	    } else {
		result.setList(arrList);
		result.setTotalCount(Long.parseLong(
			bll.getSingal(sqlCount, params).toString()));
	    }
	    LogUtil.log("EJB:班组人员调动查询结束。", Level.INFO, null);
	    // 返回PageObject
	    return result;
	} catch (RuntimeException e) {
	    LogUtil.log("EJB:班组人员调动查询失败。", Level.SEVERE, e);
	    throw e;
	}
    }

    /**
     * 下拉框列表[员工姓名]
     */
    @SuppressWarnings("unchecked")
    public PageObject getEmpNameList(String enterpriseCode, String deptId) {
	PageObject result = new PageObject();
	String sql = "select * " 
	        + "  from HR_J_EMP_INFO t "
	        // modify by liuyi 090914 t表中无is_use属性
//		+ "where t.is_use = ? " 
//		+ "  and t.enterprise_code = ? " 
			+ "  where t.enterprise_code = ? "
			// modify by liuyi 090924 班组与部门不是先前的一一对应关系
		+ "  and t.DEPT_ID = ? ";
//		+ "  and t.banzu_id = ? ";
	// 查询参数数组
//	Object[] params = new Object[3];
	Object[] params = new Object[2];
	params[0] = enterpriseCode;
	params[1] = deptId;
	LogUtil.log("下拉框列表[员工姓名]EJB:sql=" + sql, Level.INFO, null);
	List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, params, HrJEmpInfo.class);
	HrJEmpInfo nullBeen = new HrJEmpInfo();
	// 在第一行增加空行
	nullBeen.setEmpId(null);
	nullBeen.setChsName("");
	nullBeen.setEmpCode("");
	list.add(0, nullBeen);
	result.setList(list);
	return result;
    }

    /**
     * 为空判断
     * 
     * @param value
     * @return boolean
     */
    private boolean checkIsNotNull(String value) {
	if (value != null && !"".equals(value)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * 新增保存班组人员调动信息
     * 
     * @param entity
     * @throws Exception
     */
    public void save(HrJStationremove entity) throws Exception {
	try {
	    LogUtil.log("EJB:班组人员调动新增保存开始", Level.INFO, null);
	    hrJEmpInfoRemote = (HrJEmpInfoFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJEmpInfoFacade");
	    hrJStationremoveRemote = (HrJStationremoveFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJStationremoveFacade");

	    // 保存到岗位调动单
	    hrJStationremoveRemote.save(entity);

	    // modify by liuyi 090925 班组调动时，暂不将人员信息表中的数据更改
//	    // 获得人员ID
//	    Long empId = entity.getEmpId();
//	    // 通过人员ID获得实体信息
//	    HrJEmpInfo empInfo = hrJEmpInfoRemote.findById(empId);
//	    // 设置调动后的部门
//	    empInfo.setDeptId(entity.getNewDepId());
//	    // 设置修改者
//	    // modify by liuyi 090914 存id,而非编码
////	    empInfo.setLastModifiyBy(entity.getLastModifiedBy());
//	    empInfo.setLastModifiyBy(Long.parseLong(entity.getLastModifiedBy()));
//	    // 保存(含修改时间)到人员基本信息
//	    hrJEmpInfoRemote.update(empInfo);
	    LogUtil.log("Action:班组人员调动新增保存结束", Level.INFO, null);
	} catch (Exception e) {
	    // 保存失败
	    LogUtil.log("EJB:班组人员调动新增保存失败", Level.SEVERE, e);
	    throw e;
	}
    }

    public void update(HrJStationremove entity) throws SQLException,
	    DataChangeException {
	try {
	    LogUtil.log("EJB:班组人员调动修改保存开始。", Level.INFO, null);
	    hrJStationremoveRemote = (HrJStationremoveFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJStationremoveFacade");
	    // 排他：
	    // 通过id取得上次更新时间
	    HrJStationremove oldBeen = hrJStationremoveRemote.findById(entity
		    .getStationremoveid());
	    String strDbOldTime = oldBeen.getLastModifiedDate().toString()
		    .substring(0, 19);
	    // 画面端的上次更新时间
	    String strOldTime = DateToString(entity.getLastModifiedDate());
	    // 比较更新时间是否一致
	    if (!strDbOldTime.equals(strOldTime)) {
		LogUtil.log("EJB: " + strDbOldTime + " | " + strOldTime,
			Level.INFO, null);
		throw new DataChangeException(null);
	    }
	    // 设置修改时间
	    entity.setLastModifiedDate(new Date());
	    // 更新
	    hrJStationremoveRemote.update(entity);
	    LogUtil.log("EJB:班组人员调动修改保存结束。", Level.INFO, null);
	} catch (DataChangeException e) {
	    LogUtil.log("EJB:班组人员调动修改保存失败。", Level.INFO, e);
	    throw e;
	} catch (RuntimeException sqlE) {
	    LogUtil.log("EJB:班组人员调动修改保存失败。", Level.SEVERE, sqlE);
	    throw new SQLException();
	}
    }

    /**
     * 根据日期和形式返回日期字符串
     * 
     * @param argDate
     *                日期
     * @return 日期字符串
     */
    private String DateToString(Date date) {
	SimpleDateFormat defaultFormat = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");
	String sysDate = defaultFormat.format(date);
	return sysDate;
    }
}
