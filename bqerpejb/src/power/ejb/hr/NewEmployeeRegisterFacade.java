/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 新进员工登记Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class NewEmployeeRegisterFacade implements
		NewEmployeeRegisterFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	/** 字符串: 空字符串 */
	public final String STRING_BLANK = "";
	/** 字符串: 是否使用 */
	public final String IS_USE_Y = "Y";
	/** 字符串: 是否存档 是 */
	public final String IS_SAVE_Y = "1";
	/** 字符串: 是否存档 否 */
	public final String IS_SAVE_N = "0";
	/** 字符串: 员工状态 2 */
	public final String EMP_STATE_2 = "2";
	
	/**
	 * 查询新进员工信息
	 * 
	 * @param date
	 *            年度
	 * @param deptId
	 *            部门ID
	 * @param isSave
	 *            是否存档
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getNewEmployeeQuery(String flag,String date, String deptId,
			String isSave, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		// Log开始
		LogUtil.log("EJB:查询新进员工信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 参数List
        	List listParams = new ArrayList();
        	// 查询sql
        	String sql =
        		"SELECT " +
        			"A.EMP_CODE, " +
        			"TO_CHAR(A.BRITHDAY, 'YYYY-MM-DD'), " +
        			"A.SEX, " +
        			"A.CHS_NAME, " +
        			"A.IS_WEDDED, " +
        			"A.EN_NAME, " +
        			"A.IDENTITY_CARD, " +
        			"TO_CHAR(A.MISSION_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(A.TRYOUT_START_DATE, 'YYYY-MM-DD'), " +
        			"TO_CHAR(A.TRYOUT_END_DATE, 'YYYY-MM-DD'), " +
        			"A.EMP_STATE, " +
        			"TO_CHAR(A.GRADUATE_DATE, 'YYYY-MM-DD'), " +
        			"A.IS_VETERAN, " +
        			"A.MEMO, " +
        			"TO_CHAR(A.LAST_MODIFIY_DATE, 'YYYY-MM-DD hh24:mi:ss'), " +
        			"B.DEPT_NAME, " +
        			"B.DEPT_ID, " +
        			"C.STATION_NAME, " +
        			"C.STATION_ID, " +
        			"D.EMP_TYPE_NAME, " +
        			"D.EMP_TYPE_ID, " +
        			"E.IN_TYPE, " +
        			"E.IN_TYPE_ID, " +
        			"F.NATION_CODE_ID, " +
        			"G.POLITICS_ID, " +
        			"H.EDUCATION_ID, " +
        			"I.SPECIALTY_CODE_ID, " +
        			"J.DEGREE_ID, " +
        			"K.SCHOOL_CODE_ID, " +
        			"L.NATIVE_PLACE_ID, " +
        			"A.EMP_ID "
        			+" ,A.NEW_EMP_CODE ";
        	String sqlWhere =
        		"FROM " +
        			"HR_J_EMP_INFO A LEFT JOIN HR_C_DEPT B " +
        			"ON A.DEPT_ID = B.DEPT_ID AND " +
        			"B.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_STATION C " +
        			"ON A.STATION_ID = C.STATION_ID AND " +
        			"C.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_EMP_TYPE D " +
        			"ON A.EMP_TYPE_ID = D.EMP_TYPE_ID AND " +
        			"D.IS_USE = ? AND D.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_INTYPE E " +
        			"ON A.IN_TYPE_ID = E.IN_TYPE_ID AND " +
        			"E.IS_USE = ? AND E.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_NATION F " +
        			"ON A.NATION_CODE_ID = F.NATION_CODE_ID AND " +
        			"F.IS_USE = ? AND F.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_POLITICS G " +
        			"ON A.POLITICS_ID = G.POLITICS_ID AND " +
        			"G.IS_USE = ? AND G.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_EDUCATION H " +
        			"ON A.EDUCATION_ID = H.EDUCATION_ID AND " +
        			"H.IS_USE = ? AND H.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_SPECIALTY I " +
        			"ON A.SPECIALTY_CODE_ID = I.SPECIALTY_CODE_ID AND " +
        			"I.IS_USE = ? AND I.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_DEGREE J " +
        			"ON A.DEGREE_ID = J.DEGREE_ID AND " +
        			"J.IS_USE = ? AND J.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_SCHOOL K " +
        			// modified by liuyi 091124 新增一个学校id
//        			"ON A.GRADUATE_SCHOOL = K.SCHOOL_CODE_ID AND " +
        			"ON A.GRADUATE_SCHOOL_ID = K.SCHOOL_CODE_ID AND " +
        			"K.IS_USE = ? AND K.ENTERPRISE_CODE = ? " +
        			"LEFT JOIN HR_C_NATIVE_PLACE L " +
        			"ON A.NATIVE_PLACE_ID = L.NATIVE_PLACE_ID AND " +
        			"L.IS_USE = ? AND L.ENTERPRISE_CODE = ? " +
        		"WHERE " +
        			"A.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? AND " +
        			"A.EMP_STATE IS NOT NULL ";
        	if(date != null && !(STRING_BLANK.equals(date))) {
        		sqlWhere += "AND TO_CHAR(A.MISSION_DATE, 'YYYY') = ? ";
        		listParams.add(date);
        	}
        	//add  by sychen 20100716 
        	if(flag!=null &&flag.equals("deptFlag")){
        		if(deptId != null && !(STRING_BLANK.equals(deptId))) {
            		sqlWhere += "and a.dept_id in (select t.dept_id\n" +
            			"                       from hr_c_dept t\n" + 
            			"                      where t.is_use = 'Y'\n" + //update by sychen 20100901
//            			"                      where t.is_use = 'U'\n" + 
            			"                      start with t.dept_id = ?\n" + 
            			"                     connect by prior t.dept_id = t.pdept_id)\n";

            		listParams.add(deptId);
            	}
        	}
        	//add  by sychen 20100716 end
        	else {
        		if(deptId != null && !(STRING_BLANK.equals(deptId))) {
            		sqlWhere += "AND A.DEPT_ID = ? ";
            		listParams.add(deptId);
            	}
        	}

        	if(isSave != null && !(STRING_BLANK.equals(isSave))) {
        		if(IS_SAVE_Y.equals(isSave)) {
        			sqlWhere += "AND A.EMP_STATE = ? ";
        		} else if(IS_SAVE_N.equals(isSave)){
        			sqlWhere += "AND (A.EMP_STATE <> ?  OR A.EMP_STATE IS NULL) ";
        		}
        		listParams.add(EMP_STATE_2);
        	}
        	sqlWhere += "ORDER BY A.EMP_CODE";
        	sql += sqlWhere;
        	String sqlCount = "SELECT COUNT(1) " + sqlWhere;
        	// 生成查询参数
        	Object[] params = listParams.toArray();
        	Object[] paramsNew = new Object[params.length + 22];
        	// 企业代码
        	paramsNew[0] = enterpriseCode;
        	paramsNew[1] = enterpriseCode;
        	// 是否使用
        	for(int i = 2; i < 22; i += 2) {
        		paramsNew[i] = IS_USE_Y;
        	}
        	// 企业代码
        	for(int i = 3; i < 22; i += 2) {
        		paramsNew[i] = enterpriseCode;
        	}
        	// 其他参数
        	for(int i = 0; i < params.length; i++) {
        		paramsNew[22 + i] = params[i];
        	}
        	List list = bll.queryByNativeSQL(sql,
        			paramsNew, rowStartIdxAndCount);
        	Iterator it = list.iterator();
        	List<NewEmployeeRegister> arrlist = new ArrayList<NewEmployeeRegister>();
            while (it.hasNext()) {
            	NewEmployeeRegister info = new NewEmployeeRegister();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setEmpCode(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setBirthday(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setSex(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setChsName(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setIsWedded(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setEnName(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setIdentityCard(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setMissionDate(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setTryoutStartDate(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setTryoutEndDate(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setEmpState(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setGraduateDate(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setIsVeteran(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setMemo(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setLastModifiyDate(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setDeptName(data[15].toString());
            	}
            	if(null != data[16]) {
            		info.setDeptId(data[16].toString());
            	}
            	if(null != data[17]) {
            		info.setStationName(data[17].toString());
            	}
            	if(null != data[18]) {
            		info.setStationId(data[18].toString());
            	}
            	if(null != data[19]) {
            		info.setEmpTypeName(data[19].toString());
            	}
            	if(null != data[20]) {
            		info.setEmpTypeId(data[20].toString());
            	}
            	if(null != data[21]) {
            		info.setInType(data[21].toString());
            	}
            	if(null != data[22]) {
            		info.setInTypeId(data[22].toString());
            	}
            	if(null != data[23]) {
            		info.setNationCodeId(data[23].toString());
            	}
            	if(null != data[24]) {
            		info.setPoliticsId(data[24].toString());
            	}
            	if(null != data[25]) {
            		info.setEducationId(data[25].toString());
            	}
            	if(null != data[26]) {
            		info.setSpecialtyCodeId(data[26].toString());
            	}
            	if(null != data[27]) {
            		info.setDegreeId(data[27].toString());
            	}
            	if(null != data[28]) {
            		info.setSchoolCodeId(data[28].toString());
            	}
            	if(null != data[29]) {
            		info.setNativePlaceId(data[29].toString());
            	}
            	if(null != data[30]) {
            		info.setEmpId(data[30].toString());
            	}
            	if(null != data[31]){
            		info.setNewEmpCode(data[31].toString());
            	}
            	arrlist.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		paramsNew).toString());
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            // Log结束
            LogUtil.log("EJB:查询新进员工信息查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询新进员工信息查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询人员信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpInfoNewEmployee(String enterpriseCode,
			final int... rowStartIdxAndCount) {
		// Log开始
		LogUtil.log("EJB:查询人员信息开始。", Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 查询sql
        	String sql =
        		"SELECT * " +
        		"FROM " +
        			"HR_J_EMP_INFO A " +
        		"WHERE " +
        			"A.EMP_STATE IS NULL AND " +
        			"A.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? " +
        		"ORDER BY A.EMP_ID";
        	String sqlCount = 
        		"SELECT COUNT(A.EMP_ID) " +
        		"FROM " +
        			"HR_J_EMP_INFO A " +
        		"WHERE " +
        			"A.EMP_STATE IS NULL AND " +
        			"A.IS_USE = ? AND " +
        			"A.ENTERPRISE_CODE = ? " +
        		"ORDER BY A.EMP_ID";
        	List<HrJEmpInfo> list = bll.queryByNativeSQL(sql,
        			new Object[] {IS_USE_Y, enterpriseCode},
        			HrJEmpInfo.class, rowStartIdxAndCount);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
            		new Object[] {IS_USE_Y, enterpriseCode}).toString());
            if (list == null) {
                list = new ArrayList<HrJEmpInfo>();
            }
            object.setList(list);
            object.setTotalCount(totalCount);
            // Log结束
            LogUtil.log("EJB:查询人员信息结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
        	LogUtil.log("EJB:查询人员信息失败。", Level.SEVERE, e);
            throw e;
        }
	}
}
