/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 员工离职登记Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Stateless
public class DismissionInfoFacade implements DismissionInfoFacadeRemote {
    @PersistenceContext
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    // @EJB(beanName = "HrJDimission")
    /** 员工离职Remote */
    private HrJDimissionFacadeRemote hrJDimissionFacadeRemote;
    // @EJB(beanName = "HrJEmpInfo")
    /** 人员基本信息Remote */
    private HrJEmpInfoFacadeRemote hrJEmpInfoRemote;
    /** 存档("1") */
    private static String STR_SAVE_FLAG_1 = "1";
    /** 是使用("Y") */
    private static String STR_IS_USE_Y = "Y";
    /** 不使用("N") */
    private static String STR_IS_USE_N = "N";
    /** 员工状态(离职：“3”) */
    private static String STR_EMP_STATE_3 = "3";

    /**
     * 获得员工离职登记信息
     * 
     * @param startDate
     *                年度
     * @param endDate
     *                部门ID
     * @param oldDeptId
     *                离职类别ID
     * @param enterpriseCode
     *                企业编码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
    @SuppressWarnings("unchecked")
    public PageObject getDismissionInfoList(String flag,String year, String deptId,
	    String typeId, String advicenoteNo, String enterpriseCode, int... rowStartIdxAndCount) {
	try {
	    PageObject result = new PageObject();
	    // 查询sql
	    String strSql = "select B.Emp_Code, "
		    + "  B.Chs_Name, "
		    + "  D.Emp_Type_Name, "
		    + "  C.Dept_Name, "
		    + "  E.STATION_NAME, "
		    + "  A.Out_Type_Id, "
		    + "  F.Out_Type_Type, "
		    + "  to_char( A.Dimission_Date,'yyyy-mm-dd'), "
		    + "  A.DIMISSION_REASON, "
		    + "  A.WHITHER, "
		    + "  A.IF_SAVE, "
		    + "  A.Memo, "
		    + "  A.Dimissionid, "
		    + "  to_char( A.Last_Modified_Date,'yyyy-mm-dd hh24:mi:ss'), "
		    + "  to_char( B.Last_Modifiy_Date,'yyyy-mm-dd hh24:mi:ss'),"
		    + "  A.EMP_ID ,"
	        + "  B.NEW_EMP_CODE, "
	    	+ "  A.ADVICENOTE_NO, " // add by ywliu 20100618
	    	+ "	 to_char(A.STOPSALARY_DATE,'yyyy-mm-dd') "// add by ywliu 20100618
    	    + "	, to_char(A.REGISTER_DATE,'yyyy-mm-dd')," + // add by sychen 20100717
    	    	"to_char(g.print_date, 'yyyy-MM-dd')\n ";//add by wpzhu 20100816
	    //String sqlCount = "select count(distinct A.Dimissionid) ";

	    String fromStr = "from "
		    + "  HR_J_DIMISSION A "
		    + " inner join  HR_J_EMP_INFO B  "
		    + " ON A.EMP_ID = B.Emp_Id  AND B.Enterprise_Code = ? "
		    + " Left join   HR_C_DEPT C "
		    + " ON  B.Dept_Id = C.DEPT_ID AND C.Enterprise_Code = ? "
		    
		    + " Left join   hr_j_newemployee g " //add by wpzhu  20100816
		    + " ON  g.emp_id = B.emp_id and g.is_use='"+STR_IS_USE_Y+"' AND g.Enterprise_Code = '"+enterpriseCode+"'"
		    
		    + " Left join  HR_C_EMP_TYPE D ON"
		    // modified by liuyi 091123 表中无企业编码属性
//		    + " B.Emp_Type_Id = D.EMP_TYPE_ID AND D.Enterprise_Code = ? AND D.Is_Use = ? "
		    + " B.Emp_Type_Id = D.EMP_TYPE_ID  AND D.Is_Use = ? "
		    + " Left join  HR_C_STATION E "
		    + " ON B.Station_Id = E.Station_Id AND E.Enterprise_Code = ? "
		    + " Left join   HR_C_OUTTYPE F "
		    + " ON A.OUT_TYPE_ID = F.OUT_TYPE_ID AND F.Enterprise_Code = ? AND F.Is_Use = ? ";

	    String strSqlWhere = "where A.Is_Use = ? "
		    + "  AND A.Enterprise_Code = ? ";
	    // 查询参数数量
	 // modified by liuyi 091123 表中无企业编码属性 去掉一个参数
//	    int paramsCnt = 9;
	    int paramsCnt = 8;
	    if (checkIsNotNull(year)) {
		paramsCnt++;
	    }
	    if (checkIsNotNull(deptId)) {
		paramsCnt++;
	    }
	    if (checkIsNotNull(typeId)) {
		paramsCnt++;
	    }
	    //update by sychen 20100716
//	    if (checkIsNotNull(advicenoteNo)) {
//		paramsCnt++;
//	    }
	    // 查询参数数组
	    Object[] params = new Object[paramsCnt];
	    int i = 0;
	    params[i++] = enterpriseCode;
	    params[i++] = enterpriseCode;
//	    params[i++] = enterpriseCode;
	    params[i++] = STR_IS_USE_Y;
	    params[i++] = enterpriseCode;
	    params[i++] = enterpriseCode;
	    params[i++] = STR_IS_USE_Y;
	    params[i++] = STR_IS_USE_Y;
	    params[i++] = enterpriseCode;
	    // 年度
	    if (checkIsNotNull(year)) {
		strSqlWhere += " and extract(year from A.DIMISSION_DATE) = ? ";
		params[i++] = year;
	    }
	    // 部门期
	    // add by sychen 20100716
	    if(flag!=null&&flag.equals("deptFlag")){
	        if (checkIsNotNull(deptId)) {
	    		strSqlWhere += "  and B.DEPT_ID  in (select t.dept_id\n" +
            			"                       from hr_c_dept t\n" + 
            			"                      where t.is_use = 'Y'\n" + //update by sychen 20100901
//            			"                      where t.is_use = 'U'\n" + 
            			"                      start with t.dept_id = ?\n" + 
            			"                     connect by prior t.dept_id = t.pdept_id)\n";
	    		params[i++] = deptId;
	    	    }
	    }
	    // add by sychen 20100716 end 
	    else {
	        if (checkIsNotNull(deptId)) {
	    		strSqlWhere += "  and B.DEPT_ID = ? ";
	    		params[i++] = deptId;
	    	    }
		}
	    // 离职类别
	    if (checkIsNotNull(typeId)) {
		strSqlWhere += " and A.Out_Type_Id = ? ";
		params[i++] = typeId;
	    }
	    // 通知单号
	    if (checkIsNotNull(advicenoteNo)) {
	    	//update by sychen 20100716
	    	strSqlWhere += " AND  decode(a.ADVICENOTE_NO,'','','人离字（'||to_char(sysdate,'yyyy')||'）第'||a.ADVICENOTE_NO||'号') LIKE  '%"+advicenoteNo+"%' \n";

//		strSqlWhere += " and A.ADVICENOTE_NO = ? ";
//		params[i++] = advicenoteNo;
			//update by sychen 20100716 end
	    }
	    // 拼接查询sql
	    strSql = strSql + fromStr + strSqlWhere
		    + " ORDER BY A.DIMISSION_DATE ";
	    //sqlCount = sqlCount + fromStr + strSqlWhere;
	    LogUtil.log("EJB:员工离职登记查询开始。", Level.INFO, null);
	    LogUtil.log("SQL=" + strSql, Level.INFO, null);
	    // list
	    List list = bll.queryByNativeSQL(strSql, params,
		    rowStartIdxAndCount);
	    List<DismissionInfo> arrList = new ArrayList<DismissionInfo>();
	    Iterator it = list.iterator();
	    while (it.hasNext()) {
		Object[] data = (Object[]) it.next();
		DismissionInfo info = new DismissionInfo();
		// 员工工号
		if (data[0] != null) {
		    info.setEmpCode(data[0].toString());
		}
		// 员工姓名
		if (data[1] != null) {
		    info.setEmpName(data[1].toString());
		}
		// 员工类别
		if (data[2] != null) {
		    info.setEmpTypeName(data[2].toString());
		}
		// 原工作部门
		if (data[3] != null) {
		    info.setOldDepName(data[3].toString());
		}
		// 原工作岗位
		if (data[4] != null) {
		    info.setOldStationName(data[4].toString());
		}
		// 离职类别id
		if (data[5] != null) {
		    info.setOutTypeId(data[5].toString());
		}
		// 离职类别
		if (data[6] != null) {
		    info.setOutTypeName(data[6].toString());
		}
		// 离职日期
		if (data[7] != null) {
		    info.setDisMissionDate(data[7].toString());
		}
		// 离职原因
		if (data[8] != null) {
		    info.setDisMissionReason(data[8].toString());
		}
		// 离职后去向
		if (data[9] != null) {
		    info.setWhither(data[9].toString());
		}
		// 是否存档
		if (data[10] != null) {
		    info.setIfSave(data[10].toString());
		}
		// 备注
		if (data[11] != null) {
		    info.setMemo(data[11].toString());
		}
		// 流水号
		if (data[12] != null) {
		    info.setDimissionid(data[12].toString());
		}
		// 上次修改日期
		if (data[13] != null) {
		    info.setLastModifiedDate(data[13].toString());
		}
		// emp上次修改日期
		if (data[14] != null) {
		    info.setEmpLastModifiedDate(data[14].toString());
		}
		// 离职人员ID
		if (data[15] != null) {
		    info.setEmpId(data[15].toString());
		}
		//员工工号 add by drdu 20100506
		if(data[16] != null)
		{
			info.setNewEmpCode(data[16].toString());
		}
		//员工工号 add by drdu 20100506
		if(data[17] != null)
		{
			info.setAdvicenoteNo(data[17].toString());
		}
		//员工工号 add by drdu 20100506
		if(data[18] != null)
		{
			info.setStopsalaryDate(data[18].toString());
		}
		if(data[19] != null)
		{
			info.setRegisterDate(data[19].toString());
		}
		if(data[20] != null)
		{
			info.setPrintDate(data[20].toString());
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
		String sqlCount="select count(*) from ("+strSql+")";
		result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount,
			params).toString()));
	    }
	    LogUtil.log("EJB:员工离职登记查询结束。", Level.INFO, null);
	    // 返回PageObject
	    return result;
	} catch (RuntimeException e) {
	    LogUtil.log("EJB:员工离职登记查询失败。", Level.SEVERE, e);
	    throw e;
	}
    }

    /**
     * 删除员工离职登记信息(未存档的)
     * 
     * @param dimissionid
     *                离职人员id
     * @param enterpriseCode
     *                企业编码
     * @param lastModifiedDate
     * @throws DataChangeException
     */
    public void delete(String dimissionid, String workerCode,
	    String enterpriseCode, String lastModifiedDate)
	    throws DataChangeException {
	try {
	    hrJDimissionFacadeRemote = (HrJDimissionFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJDimissionFacade");
	    hrJEmpInfoRemote = (HrJEmpInfoFacadeRemote) Ejb3Factory
	    .getInstance().getFacadeRemote("HrJEmpInfoFacade");
	    // 流水号
	    Long id = Long.parseLong(dimissionid);
	    // 通过离职人员id获得实体
	    HrJDimission hrJDimissionBeen = hrJDimissionFacadeRemote
		    .findById(id);
	    // 取得上次更新时间
	    String dbLastModifiedDate = hrJDimissionBeen.getLastModifiedDate()
		    .toString().substring(0, 19);
	    if (!lastModifiedDate.equals(dbLastModifiedDate)) {
		throw new DataChangeException(null);
	    } else {
		// 设置修改者
		hrJDimissionBeen.setLastModifiedBy(workerCode);
		// 设置修改时间
		hrJDimissionBeen.setLastModifiedDate(new Date());
		// 设置是否使用：“N”
		hrJDimissionBeen.setIsUse(STR_IS_USE_N);
		
		//add by sychen 20100715
	    HrJEmpInfo hrJEmpInfoBeen = hrJEmpInfoRemote.findById(hrJDimissionBeen.getEmpId());
	    // 设置 员工状态(在职："U")
	    hrJEmpInfoBeen.setEmpState("U");
	    // 设置 是否使用(是：“Y”)
	    hrJEmpInfoBeen.setIsUse(STR_IS_USE_Y);
	    hrJEmpInfoRemote.update(hrJEmpInfoBeen);
		//add by sychen 20100715 end 
	    
		LogUtil.log("EJB:员工离职登记删除开始。", Level.INFO, null);
		hrJDimissionFacadeRemote.update(hrJDimissionBeen);
		LogUtil.log("EJB:员工离职登记删除结束。", Level.INFO, null);
	    }
	} catch (RuntimeException e) {
	    LogUtil.log("EJB:员工离职登记删除失败。", Level.SEVERE, e);
	    throw e;
	}
    }

    /**
     * 修改保存or存档离职员工登记信息(含排他)
     * 
     * @param entity
     *                实体信息
     * @param lastModifiedDate
     *                画面端的上次修改时间
     * @return
     * @throws DataChangeException
     * @throws SQLException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HrJDimission update(HrJDimission entity, String lastModifiedDate,
	    String empLastModifiedDate) throws DataChangeException,
	    SQLException {
	try {
	    LogUtil.log("EJB:员工离职登记修改保存开始。", Level.INFO, null);
	    // 判断是否存档
	    String ifSave = entity.getIfSave();
	    hrJDimissionFacadeRemote = (HrJDimissionFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJDimissionFacade");
	    hrJEmpInfoRemote = (HrJEmpInfoFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJEmpInfoFacade");

	    // 存档(更新人员信息表)
	    if (STR_SAVE_FLAG_1.equals(ifSave)) {
		updateEmpInfo(entity, empLastModifiedDate);
	    }
	    // 保存
	    hrJDimissionFacadeRemote.update(entity, lastModifiedDate);
	    LogUtil.log("EJB:员工离职登记修改保存结束。", Level.INFO, null);
	    return null;
	} catch (DataChangeException de) {
	    LogUtil.log("EJB:员工离职登记修改保存失败。", Level.SEVERE, de);
	    throw de;
	} catch (SQLException se) {
	    LogUtil.log("EJB:员工离职登记修改保存失败。", Level.SEVERE, se);
	    throw se;
	}
    }

    /**
     * 新增(保存or存档)离职员工登记信息(含排他)
     * 
     * @param entity
     *                实体信息
     * @param lastModifiedDate
     *                画面端的上次修改时间
     * @param empLastModifiedDate
     *                画面端的上次修改时间（人员基本信息）
     * @return
     * @throws DataChangeException
     * @throws SQLException
     * @throws CodeRepeatException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HrJDimission save(HrJDimission entity, String lastModifiedDate,
	    String empLastModifiedDate) throws DataChangeException,
	    SQLException, CodeRepeatException {
	try {
	    LogUtil.log("EJB:员工离职登记新增保存开始。", Level.INFO, null);
	    // 判断是否存档
	    String ifSave = entity.getIfSave();
	    hrJDimissionFacadeRemote = (HrJDimissionFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJDimissionFacade");
	    hrJEmpInfoRemote = (HrJEmpInfoFacadeRemote) Ejb3Factory
		    .getInstance().getFacadeRemote("HrJEmpInfoFacade");
	    // 重复性check (人员ID在离职人员登记里是否已存在)
	    if (checkEmpIdSameForAdd(entity.getEmpId().toString(),
		    STR_IS_USE_Y)) {
		throw new CodeRepeatException(null);
	    }
	    // 存档(更新人员信息表)
	    if (STR_SAVE_FLAG_1.equals(ifSave)) {
		updateEmpInfo(entity, empLastModifiedDate);
	    }
	    // 保存 (含设置插入时间,修改时间)
	    hrJDimissionFacadeRemote.save(entity);
	    LogUtil.log("EJB:员工离职登记新增保存结束。", Level.INFO, null);
	    return null;
	} catch (DataChangeException de) {
	    LogUtil.log("EJB:员工离职登记新增保存失败。", Level.SEVERE, de);
	    throw de;
	} catch (CodeRepeatException ce) {
	    LogUtil.log("EJB:员工离职登记新增保存失败。", Level.SEVERE, ce);
	    throw ce;
	} catch (SQLException se) {
	    LogUtil.log("EJB:员工离职登记新增保存失败。", Level.SEVERE, se);
	    throw se;
	}
    }

    private boolean checkEmpIdSameForAdd(String empId, String isUse) {
	String sql = "select count(1) from HR_J_DIMISSION t where t.EMP_ID=? and t.IS_USE=?";
	int size = Integer.parseInt(bll.getSingal(sql,
		new Object[] { empId, isUse }).toString());
	if (size > 0) {
	    return true;
	}
	return false;
    }

    private void updateEmpInfo(HrJDimission entity, String empLastModifiedDate)
	    throws DataChangeException, SQLException {
	try {
	    Long empId = entity.getEmpId();
	    // 通过id获得人员信息实体
	    HrJEmpInfo hrJEmpInfoBeen = hrJEmpInfoRemote.findById(empId);
	 // modified by liuyi 091125 员工离职表中和基本信息表中最后修改时间取出的数据
	    // 格式不一致 一个带有时分秒，一个无 通过时间比较不太可行 现程序可跑通 但效果不正确
	    //  已将人员基本信息表的bean取最后修改时间的格式修改
	    // 通过取得DB端的上次修改时间
	    // add by ywliu 加入判断若最后修改时间为空,则不进行排他判断
	    if(!"".equals(empLastModifiedDate) && empLastModifiedDate != null ) {
		    String dbLastDate = hrJEmpInfoBeen.getLastModifiyDate().toString()
			    .substring(0, 19);
		    if (!empLastModifiedDate.equals(dbLastDate)) {
			throw new DataChangeException(null);
		    }
	    }
//	    if (!empLastModifiedDate.startsWith(dbLastDate)) {
//			throw new DataChangeException(null);
//		    }
	    // 设置 员工状态(离职：“3”)
	    hrJEmpInfoBeen.setEmpState(STR_EMP_STATE_3);
	    // 设置 是否使用(否：“N”)
	    hrJEmpInfoBeen.setIsUse(STR_IS_USE_N);
	    // 设置离职日期(画面.离职日期)
	    hrJEmpInfoBeen.setDimissionDate(entity.getDimissionDate());
	    //设置是否退休 add by wpzhu 20100730
	   if("3".equals(entity.getOutTypeId())||entity.getOutTypeId()==3)
	   {
		   hrJEmpInfoBeen.setIsRetired("1");//已退休
	   }
	    
	    // 设置修改者
	    // modified by liuyi 091123 两个数据库表中类型不一致，待解决
	    // 解决方法 通过entity中的修改人编码到人员信息表中查找其员工Id
	    if(entity.getLastModifiedBy() != null)
	    {
	    	List<HrJEmpInfo> empList = hrJEmpInfoRemote.findByEmpCode(entity.getLastModifiedBy());
	    	if(empList != null && empList.size() > 0)
	    	{
	    		hrJEmpInfoBeen.setLastModifiyBy(empList.get(0).getEmpId());
	    	}
	    }
	    // 更新 (设置修改时间)
	    hrJEmpInfoRemote.update(hrJEmpInfoBeen);
	} catch (DataChangeException de) {
	    LogUtil.log("EJB:员工离职登记修改保存失败。", Level.SEVERE, de);
	    throw de;
	}
	// modified by liuyi 091123 无该异常出现
//	catch (SQLException se) {
//	    LogUtil.log("EJB:员工离职登记修改保存失败。", Level.SEVERE, se);
//	    throw se;
//	}
    }

    /**
     * 通过人员id获得基本信息
     * 
     * @param enterpriseCode
     *                企业编码
     * @param dimissionid
     *                人员id
     * @return PageObject
     */
    @SuppressWarnings("unchecked")
    public PageObject getEmpInfo(String enterpriseCode, String dimissionid) {
	try {
	    PageObject result = new PageObject();
	    String strSql = " select "
		    + " B.Emp_Code, "
		    + " B.Chs_Name, "
		    + " D.Emp_Type_Name, "
		    + " C.Dept_Name, "
		    + " E.STATION_NAME, "
		    + " to_char( B.Last_Modifiy_Date,'yyyy-mm-dd hh24:mi:ss'), "
		    + " B.EMP_ID "
		    + " from "
		    + " HR_J_EMP_INFO B "
		    + " Left join   HR_C_DEPT C "
		    + " ON  B.Dept_Id = C.DEPT_ID AND C.Enterprise_Code = ? "
		    + " Left join  HR_C_EMP_TYPE D "
		    + " ON B.Emp_Type_Id = D.EMP_TYPE_ID AND D.Enterprise_Code = ? AND D.Is_Use = ? "
		    + " Left join  HR_C_STATION E "
		    + " ON B.Station_Id = E.Station_Id AND E.Enterprise_Code = ? "
		    + " where B.Emp_Id = ?  AND B.Enterprise_Code = ? and B.IS_USE = ? ";

	    // 查询参数数组
	    Object[] params = new Object[7];
	    params[0] = enterpriseCode;
	    params[1] = enterpriseCode;
	    params[2] = STR_IS_USE_Y;
	    params[3] = enterpriseCode;
	    params[4] = dimissionid;
	    params[5] = enterpriseCode;
	    params[6] = STR_IS_USE_Y;
	    LogUtil.log("EJB:离职员工登记：通过员工id取信息開始。sql=" + strSql, Level.INFO,
		    null);
	    List list = bll.queryByNativeSQL(strSql, params);
	    List<DismissionInfo> arrList = new ArrayList<DismissionInfo>();
	    Iterator it = list.iterator();
	    while (it.hasNext()) {
		Object[] data = (Object[]) it.next();
		DismissionInfo info = new DismissionInfo();
		// 员工工号
		if (data[0] != null) {
		    info.setEmpCode(data[0].toString());
		}
		// 员工姓名
		if (data[1] != null) {
		    info.setEmpName(data[1].toString());
		}
		// 员工类别
		if (data[2] != null) {
		    info.setEmpTypeName(data[2].toString());
		}
		// 原工作部门
		if (data[3] != null) {
		    info.setOldDepName(data[3].toString());
		}
		// 原工作岗位
		if (data[4] != null) {
		    info.setOldStationName(data[4].toString());
		}
		// 上次修改时间
		if (data[5] != null) {
		    info.setEmpLastModifiedDate(data[5].toString());
		}
		// 员工id
		if (data[6] != null) {
		    info.setDimissionid(data[6].toString());
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
		result.setTotalCount(Long.parseLong(String.valueOf(arrList.size())));
	    }
	    LogUtil.log("EJB:离职员工登记：通过员工id取信息结束。", Level.INFO, null);
	    return result;
	} catch (RuntimeException e) {
	    LogUtil.log("EJB:离职员工登记：通过员工id取信息失败。", Level.SEVERE, e);
	    throw e;
	}
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
}
