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

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 新进/离职员工统计查询方法体
 * 
 * @author zhouxu
 * @version 1.0
 */
@Stateless
public class HrJNewAndDimissionFacade implements HrJNewAndDimissionFacadeRemote {
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /** 是否使用: 是 */
    private static final String IS_USE_Y = "Y";
    /** 是否使用: 否 */
    private static final String IS_USE_N = "N";
    /** 员工状态: 离职 */
    private static final String EMP_STATE_3 = "3";
    /** 是否存档: 是 */
    private static final String IF_SAVE_Y = "1";

    @SuppressWarnings("unchecked")
    public PageObject findDimission(String argYear, String argDeptId, String argEnterpriseCode,
    		String typeId,String advicenoteNo,
            final int... rowStartIdxAndCount) {
        LogUtil.log("EJB:离职员工统计查询开始", Level.INFO, null);
        try {
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT    ");
            sbd.append("    B.EMP_CODE,    ");
            sbd.append("    B.CHS_NAME,    ");
            sbd.append("    F.EMP_TYPE_NAME,    ");
            sbd.append("    TO_CHAR(A.DIMISSION_DATE , 'yyyy-mm-dd'),    ");
            sbd.append("    E.OUT_TYPE_TYPE,    ");
            sbd.append("    C.DEPT_NAME,    ");
            sbd.append("    D.STATION_NAME,    ");
            sbd.append("    A.DIMISSION_REASON,    ");
            sbd.append("    A.WHITHER,    ");
            sbd.append("    A.MEMO    ");
            sbd.append("    ,B.new_emp_code    ");
            sbd.append("FROM    ");
            sbd.append("    HR_J_DIMISSION A    ");
            sbd.append("    INNER JOIN HR_J_EMP_INFO B ON A.EMP_ID=B.EMP_ID    ");
            sbd.append("    AND B.ENTERPRISE_CODE = ?    ");
            if (!"".equals(argDeptId)) {
                sbd.append("    AND B.DEPT_ID=?    ");
            }
            sbd.append("    LEFT JOIN HR_C_DEPT C ON B.DEPT_ID = C.DEPT_ID    ");
            sbd.append("    AND C.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_C_STATION D ON B.STATION_ID = D.STATION_ID    ");
            sbd.append("    AND D.ENTERPRISE_CODE = ?   ");
            sbd.append("    LEFT JOIN HR_C_OUTTYPE E ON A.OUT_TYPE_ID = E.OUT_TYPE_ID    ");
            sbd.append("    AND E.IS_USE = ?    ");
            sbd.append("    AND E.ENTERPRISE_CODE = ?   ");
            sbd.append("    LEFT JOIN HR_C_EMP_TYPE F ON B.EMP_TYPE_ID = F.EMP_TYPE_ID    ");
            sbd.append("    AND F.IS_USE = ?    ");
            sbd.append("    AND F.ENTERPRISE_CODE = ?   ");
            sbd.append("WHERE     ");
            sbd.append("    A.IS_USE=?    ");
            sbd.append("    AND A.IF_SAVE = ?    ");
            sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
            if (!"".equals(argYear)) {
                sbd.append("    AND TO_CHAR(A.DIMISSION_DATE , 'yyyy')=?    ");
            }
            
            // 打印SQL语句
            LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);

            List listParams = new ArrayList();
            listParams.add(argEnterpriseCode);
            if (!"".equals(argDeptId)) {
                listParams.add(argDeptId);
            }
            listParams.add(argEnterpriseCode);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(IF_SAVE_Y);
            listParams.add(argEnterpriseCode);
            if (!"".equals(argYear)) {
                listParams.add(argYear);
            }
            //-----add by fyyang 20100623--------------
             if(typeId!=null&&!typeId.equals(""))
             {
            	 sbd.append("     and A.Out_Type_Id = ?  ");
            	 listParams.add(typeId);
             }
             if(advicenoteNo!=null&&!advicenoteNo.equals(""))
             {
            	 sbd.append("     and A.ADVICENOTE_NO = ?  ");
            	 listParams.add(advicenoteNo);
             }
            
             sbd.append("    ORDER BY B.EMP_CODE    ");
            //-----------------------------------------
            Object[] params = listParams.toArray();
            // 查询一条有参数sql语句
            List lstResult = bll.queryByNativeSQL(sbd.toString(), params, rowStartIdxAndCount);

            // 查询符合条件的社会关系总数
            List lstResultCount = bll.queryByNativeSQL(sbd.toString(), params);
            Long totalCount = 0L;
            if (lstResultCount != null) {
                totalCount = (long) lstResultCount.size();
            }
            List<DismissionStaffListBean> arrlist = new ArrayList<DismissionStaffListBean>();
            Iterator it = lstResult.iterator();
            while (it.hasNext()) {
                DismissionStaffListBean dimissionEmpolyBean = new DismissionStaffListBean();
                Object[] data = (Object[]) it.next();
                // 人员工号
                if (null != data[0]) {
                    dimissionEmpolyBean.setWorkerCode(data[0].toString());
                }
                // 人员名称
                if (null != data[1]) {
                    dimissionEmpolyBean.setWorkerName(data[1].toString());
                }
                // 员工类别
                if (null != data[2]) {
                    dimissionEmpolyBean.setWorkerType(data[2].toString());
                }
                // 离职日期
                if (null != data[3]) {
                    dimissionEmpolyBean.setDismissionDate(data[3].toString());
                }
                // 离职类别
                if (null != data[4]) {
                    dimissionEmpolyBean.setDismissionType(data[4].toString());
                }
                // 所属部门
                if (null != data[5]) {
                    dimissionEmpolyBean.setDepartment(data[5].toString());
                }
                // 岗位名称
                if (null != data[6]) {
                    dimissionEmpolyBean.setJobName(data[6].toString());
                }
                // 离职原因
                if (null != data[7]) {
                    dimissionEmpolyBean.setDismissionReason(data[7].toString());
                }
                // 离职后去向
                if (null != data[8]) {
                    dimissionEmpolyBean.setWhither(data[8].toString());
                }
                // 备注
                if (null != data[9]) {
                    dimissionEmpolyBean.setMemo(data[9].toString());
                }
                if(null != data[10]){
                	dimissionEmpolyBean.setNewEmpCode(data[10].toString());
                }
                arrlist.add(dimissionEmpolyBean);
            }
            PageObject result = new PageObject();
            // 设置list
            result.setList(arrlist);
            // 设置总数
            result.setTotalCount(totalCount);
            LogUtil.log("EJB:离职员工统计查询结束。", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:离职员工统计查询错误。", Level.SEVERE, e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public PageObject findNew(String argYear, String argDeptId, String argEnterpriseCode,
            final int... rowStartIdxAndCount) {
        LogUtil.log("EJB:新进员工统计查询开始", Level.INFO, null);

        try {
            StringBuilder sbd = new StringBuilder();
            sbd.append("SELECT    ");
            sbd.append("    A.EMP_CODE,    ");
            sbd.append("    A.CHS_NAME,    ");
            sbd.append("    DECODE(NVL(A.SEX,''),'M','男','W','女','',''),    ");
            sbd.append("    D.EMP_TYPE_NAME,    ");
            sbd.append("    E.IN_TYPE,    ");
            sbd.append("    A.MISSION_DATE,    ");
            sbd.append("    A.IDENTITY_CARD,    ");
            sbd.append("    TO_CHAR(A.TRYOUT_START_DATE, 'yyyy-mm-dd'),    ");
            sbd.append("    TO_CHAR(A.TRYOUT_END_DATE, 'yyyy-mm-dd') ,    ");
            sbd.append("    B.DEPT_NAME,    ");
            sbd.append("    C.STATION_NAME,    ");
            sbd.append("    A.MEMO    ");
            sbd.append("    ,A.new_emp_code    ");
            sbd.append("FROM    ");
            sbd.append("    HR_J_EMP_INFO A    ");
            sbd.append("    LEFT JOIN HR_C_DEPT B ON B.DEPT_ID=A.DEPT_ID AND B.ENTERPRISE_CODE=?    ");
            sbd.append("    LEFT JOIN HR_C_STATION C ON A.STATION_ID=C.STATION_ID    ");
            sbd.append("    AND C.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_C_EMP_TYPE D ON A.EMP_TYPE_ID = D.EMP_TYPE_ID    ");
            sbd.append("    AND D.IS_USE = ?    ");
            sbd.append("    AND D.ENTERPRISE_CODE = ?    ");
            sbd.append("    LEFT JOIN HR_C_INTYPE E ON A.IN_TYPE_ID = E.IN_TYPE_ID    ");
            sbd.append("    AND E.IS_USE = ?    ");
            sbd.append("    AND E.ENTERPRISE_CODE = ?   ");
            sbd.append("WHERE     ");
            sbd.append("    (A.IS_USE = ? OR   ");
            sbd.append("    (A.IS_USE = ? AND (A.EMP_STATE = ? or A.EMP_STATE = 'N')))   ");
            sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
            if (!"".equals(argDeptId)) {
                sbd.append("    AND A.DEPT_ID=?    ");
            }
            if (!"".equals(argYear)) {
                sbd.append("    AND TO_CHAR(A.MISSION_DATE,'yyyy')=?    ");
            }
            sbd.append("    ORDER BY A.EMP_CODE    ");
            // 打印SQL语句
            LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);

            List listParams = new ArrayList();
            listParams.add(argEnterpriseCode);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(argEnterpriseCode);
            listParams.add(IS_USE_Y);
            listParams.add(IS_USE_N);
            listParams.add(EMP_STATE_3);
            listParams.add(argEnterpriseCode);
            if (!"".equals(argDeptId)) {
                listParams.add(argDeptId);
            }
            if (!"".equals(argYear)) {
                listParams.add(argYear);
            }
            Object[] params = listParams.toArray();
            // 查询一条有参数sql语句
            List lstResult = bll.queryByNativeSQL(sbd.toString(), params, rowStartIdxAndCount);

            // 查询符合条件的社会关系总数
            List lstResultCount = bll.queryByNativeSQL(sbd.toString(), params);
            Long totalCount = 0L;
            if (lstResultCount != null) {
                totalCount = (long) lstResultCount.size();
            }
            List<NewStaffListBean> arrlist = new ArrayList<NewStaffListBean>();
            Iterator it = lstResult.iterator();
            while (it.hasNext()) {
                NewStaffListBean newEmpolyBean = new NewStaffListBean();
                Object[] data = (Object[]) it.next();
                // 人员工号
                if (null != data[0]) {
                    newEmpolyBean.setWorkerCode(data[0].toString());
                }
                // 人员名称
                if (null != data[1]) {
                    newEmpolyBean.setWorkerName(data[1].toString());
                }
                // 性别
                if (null != data[2]) {
                    newEmpolyBean.setSex(data[2].toString());
                }
                // 员工类别
                if (null != data[3]) {
                    newEmpolyBean.setWorkerType(data[3].toString());
                }
                // 进厂类别
                if (null != data[4]) {
                    newEmpolyBean.setMissionType(data[4].toString());
                }
                // 进厂日期
                if (null != data[5]) {
                    newEmpolyBean.setMissionDate(data[5].toString());
                }
                // 身份证号
                if (null != data[6]) {
                    newEmpolyBean.setIdCard(data[6].toString());
                }
                // 试用期开始
                if (null != data[7]) {
                    newEmpolyBean.setTryStartDate(data[7].toString());
                }
                // 试用期结束
                if (null != data[8]) {
                    newEmpolyBean.setTryEndDate(data[8].toString());
                }
                // 所属部门
                if (null != data[9]) {
                    newEmpolyBean.setDepartment(data[9].toString());
                }
                // 岗位名称
                if (null != data[10]) {
                    newEmpolyBean.setJobName(data[10].toString());
                }
                // 备注
                if (null != data[11]) {
                    newEmpolyBean.setMemo(data[11].toString());
                }
             // 新工号
                if (null != data[12]) {
                    newEmpolyBean.setNewEmpCode(data[12].toString());
                }
                arrlist.add(newEmpolyBean);
            }
            PageObject result = new PageObject();
            // 设置list
            result.setList(arrlist);
            // 设置总数
            result.setTotalCount(totalCount);
            LogUtil.log("EJB:新进员工统计查询结束", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:新进员工统计查询错误", Level.SEVERE, e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public PageObject findTotal(String argYear, String argDeptId, String argEnterpriseCode,
            final int... rowStartIdxAndCount) {
        LogUtil.log("EJB:新进/离职员工统计查询开始", Level.INFO, null);

        //  modified by liuyi 091211 对应新需求 初始只显示一级部门
//        try {
//            StringBuilder sbd = new StringBuilder();
//            sbd.append("SELECT    ");
//            sbd.append("    A.DEPT_ID,    ");
//            sbd.append("    A.DEPT_NAME,    ");
//            sbd.append("    C.STATION_ID,    ");
//            sbd.append("    C.STATION_NAME,    ");
//            sbd.append("    B.STANDARD_PERSON_NUM,    ");
//            sbd.append("    COUNT(DISTINCT E.EMP_ID) AS NOWNUM,    ");
//            sbd.append("    COUNT(DISTINCT H.EMP_ID) AS NEWNUM,    ");
//            sbd.append("    COUNT(DISTINCT K.EMP_ID) AS BIAOZHUNNUM,    ");
//            sbd.append("    A.DEPT_CODE    ");
//            sbd.append("FROM    ");
//            sbd.append("    HR_C_DEPT A    ");
//            sbd.append("    LEFT JOIN HR_J_DEPSTATIONCORRESPOND B ON A.DEPT_ID=B.DEPT_ID    ");
//            sbd.append("    AND B.IS_USE=?    ");
//            sbd.append("    AND B.ENTERPRISE_CODE=?    ");
//            sbd.append("    LEFT JOIN HR_C_STATION C ON B.STATION_ID=C.STATION_ID    ");
//            sbd.append("    AND C.IS_USE=?    ");
//            sbd.append("    AND C.ENTERPRISE_CODE=?    ");
//            sbd.append("    LEFT JOIN HR_J_EMP_INFO E ON TO_CHAR(E.MISSION_DATE,'YYYY')<=?    ");
//            sbd.append("    AND A.DEPT_ID=E.DEPT_ID    ");
//            sbd.append("    AND B.STATION_ID=E.STATION_ID    ");
//            sbd.append("    AND (E.IS_USE= ? OR    ");
//            sbd.append("        (E.IS_USE= ? AND (E.EMP_STATE = ? or E.EMP_STATE = 'N') AND TO_CHAR(E.DIMISSION_DATE,'YYYY')>?)) ");
//            sbd.append("    AND E.ENTERPRISE_CODE=?    ");
//            sbd.append("    LEFT JOIN HR_J_EMP_INFO H ON TO_CHAR(H.MISSION_DATE,'YYYY')=?    ");
//            sbd.append("    AND A.DEPT_ID=H.DEPT_ID    ");
//            sbd.append("    AND B.STATION_ID=H.STATION_ID    ");
//            sbd.append("    AND (H.IS_USE= ? OR    ");
//            sbd.append("        (H.IS_USE= ? AND (H.EMP_STATE = ? or H.EMP_STATE = 'N'))) ");
//            sbd.append("    AND H.ENTERPRISE_CODE=?    ");
//            sbd.append("    LEFT JOIN HR_J_EMP_INFO J ON A.DEPT_ID=J.DEPT_ID    ");
//            sbd.append("    AND B.STATION_ID=J.STATION_ID    ");
//            sbd.append("    AND J.ENTERPRISE_CODE=?    ");
//            sbd.append("    LEFT JOIN HR_J_DIMISSION K ON J.EMP_ID=K.EMP_ID    ");
//            sbd.append("    AND TO_CHAR(K.DIMISSION_DATE,'YYYY')=?    ");
//            sbd.append("    AND K.IF_SAVE=?    ");
//            sbd.append("    AND K.IS_USE=?    ");
//            sbd.append("    AND K.ENTERPRISE_CODE=?    ");
//            if (!"".equals(argDeptId)) {
//                sbd.append("WHERE    ");
//                sbd.append("    A.DEPT_ID=?    ");
//                sbd.append("    AND A.IS_USE=?    ");
//                sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
//            } else {
//            	sbd.append("WHERE    ");
//                sbd.append("    A.IS_USE=?    ");
//                // add by liuyi 091210 默认查灞桥电厂下的一级部门
//                sbd.append(" and  a.pdept_id=0      ");
//                sbd.append("    AND A.ENTERPRISE_CODE = ?    ");
//            }
//            sbd.append("GROUP BY(A.DEPT_ID,A.DEPT_CODE,A.DEPT_NAME,C.STATION_ID," +
//            		"C.STATION_NAME,B.STANDARD_PERSON_NUM)   " +
//            		"ORDER BY A.DEPT_CODE  ");
//            // 打印SQL语句
//            LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);
//
//            Object[] params;
//            if (!"".equals(argDeptId)) {
//                params = new Object[] { IS_USE_Y, argEnterpriseCode, /*S_USE_Y*/"U",
//                		argEnterpriseCode, argYear, IS_USE_Y, IS_USE_N, 
//                		EMP_STATE_3, argYear, argEnterpriseCode, argYear,
//                		IS_USE_Y, IS_USE_N, EMP_STATE_3, argEnterpriseCode,
//                		argEnterpriseCode, argYear, IF_SAVE_Y, IS_USE_Y, argEnterpriseCode, argDeptId,
//                		/*IS_USE_Y*/"U", argEnterpriseCode };
//            } else {
//            	// modified by liuyi 091130 is_use属性值问题
//                params = new Object[] { IS_USE_Y, argEnterpriseCode, /*IS_USE_Y*/"U",
//                		argEnterpriseCode, argYear, IS_USE_Y, IS_USE_N, 
//                		EMP_STATE_3, argYear, argEnterpriseCode, argYear,
//                		IS_USE_Y, IS_USE_N, EMP_STATE_3, argEnterpriseCode,
//                		argEnterpriseCode, argYear, IF_SAVE_Y, IS_USE_Y, argEnterpriseCode,
//                		/*IS_USE_Y*/"U", argEnterpriseCode };
//            }
//
//            // 查询一条有参数sql语句
//            List lstResult = bll.queryByNativeSQL(sbd.toString(), params, rowStartIdxAndCount);
//
//            // 查询符合条件的社会关系总数
//            List lstResultCount = bll.queryByNativeSQL(sbd.toString(), params);
//            Long totalCount = 0L;
//            if (lstResultCount != null) {
//                totalCount = (long) lstResultCount.size();
//            }
//        String sql = 
//
//        	"select * from\n" +
//        	"(\n" + 
//        	"\n" + 
//        	"SELECT A.DEPT_ID,\n" + 
//        	"       A.DEPT_NAME,\n" + 
//        	"\n" + 
//        	"       C.STATION_ID,\n" + 
//        	"       C.STATION_NAME,\n" + 
//        	"       sum(B.STANDARD_PERSON_NUM),\n" + 
//        	"       (select count(DISTINCT E.EMP_ID)\n" + 
//        	"          from HR_J_EMP_INFO E\n" + 
//        	"         WHERE TO_CHAR(E.MISSION_DATE, 'YYYY') <= '" +argYear+"'\n" + 
//        	"           AND (A.DEPT_ID = E.DEPT_ID or\n" + 
//        	"                e.dept_id in\n" + 
//        	"                (select t.dept_id\n" + 
//        	"                   from hr_c_dept t\n" + 
//        	"                  start with t.pdept_id = a.dept_id\n" + 
//        	"                 connect by prior t.dept_id = t.pdept_id))\n" + 
//        	"           AND C.STATION_ID = E.STATION_ID\n" + 
//        	"           AND (E.IS_USE = 'Y' OR\n" + 
//        	"                (E.IS_USE = 'N' AND (E.EMP_STATE = '3' or E.EMP_STATE = 'N') AND\n" + 
//        	"                TO_CHAR(E.DIMISSION_DATE, 'YYYY') > '" +argYear+"'))\n" + 
//        	"           AND E.ENTERPRISE_CODE = '" +argEnterpriseCode+ "') as NOWNUM,\n" + 
//        	"       (select count(DISTINCT H.EMP_ID)\n" + 
//        	"          from HR_J_EMP_INFO H\n" + 
//        	"         where TO_CHAR(H.MISSION_DATE, 'YYYY') = '" +argYear+"'\n" + 
//        	"           AND (A.DEPT_ID = H.DEPT_ID or\n" + 
//        	"                h.dept_id in\n" + 
//        	"                (select t.dept_id\n" + 
//        	"                   from hr_c_dept t\n" + 
//        	"                  start with t.pdept_id = a.dept_id\n" + 
//        	"                 connect by prior t.dept_id = t.pdept_id))\n" + 
//        	"           AND C.STATION_ID = H.STATION_ID\n" + 
//        	"           AND (H.IS_USE = 'Y' OR\n" + 
//        	"                (H.IS_USE = 'N' AND (H.EMP_STATE = '3' or H.EMP_STATE = 'N')))\n" + 
//        	"           AND H.ENTERPRISE_CODE = '" +argEnterpriseCode+ "') AS NEWNUM,\n" + 
//        	"\n" + 
//        	"       (select count(DISTINCT K.EMP_ID)\n" + 
//        	"          from HR_J_DIMISSION K, HR_J_EMP_INFO J\n" + 
//        	"         where (A.DEPT_ID = J.DEPT_ID or\n" + 
//        	"               j.dept_id in\n" + 
//        	"               (select t.dept_id\n" + 
//        	"                   from hr_c_dept t\n" + 
//        	"                  start with t.pdept_id = a.dept_id\n" + 
//        	"                 connect by prior t.dept_id = t.pdept_id))\n" + 
//        	"           AND C.STATION_ID = J.STATION_ID\n" + 
//        	"           AND J.ENTERPRISE_CODE = '" +argEnterpriseCode+ "'\n" + 
//        	"           and J.EMP_ID = K.EMP_ID\n" + 
//        	"           AND TO_CHAR(K.DIMISSION_DATE, 'YYYY') = '" +argYear+"'\n" + 
//        	"           AND K.IF_SAVE = '1'\n" + 
//        	"           AND K.IS_USE = 'Y'\n" + 
//        	"           AND K.ENTERPRISE_CODE = '" +argEnterpriseCode+ "') AS BIAOZHUNNUM,\n" + 
//        	"       A.DEPT_CODE\n" + 
//        	"  FROM HR_C_DEPT A,HR_J_DEPSTATIONCORRESPOND B,HR_C_STATION C\n" + 
//        	"  where b.dept_id in\n" + 
//        	"                                           (select t.dept_id\n" + 
//        	"                                              from hr_c_dept t\n" + 
//        	"                                             where (t.dept_id = a.dept_id or\n" + 
//        	"                                                   t.dept_id in\n" + 
//        	"                                                   (select ff.dept_id\n" + 
//        	"                                                       from hr_c_dept ff\n" + 
//        	"                                                      start with ff.dept_id =\n" + 
//        	"                                                                 a.dept_id\n" + 
//        	"                                                     connect by prior\n" + 
//        	"                                                                 ff.dept_id =\n" + 
//        	"                                                                 ff.pdept_id)))\n" + 
//        	"                                                                 AND B.IS_USE = 'Y'\n" + 
//        	"                                       AND B.ENTERPRISE_CODE = '" +argEnterpriseCode+ "'\n" + 
//        	"                 and B.STATION_ID = C.STATION_ID(+)\n" + 
//        	"                 AND C.IS_USE(+) = 'U'\n" + 
//        	"                          AND C.ENTERPRISE_CODE(+) = '" +argEnterpriseCode+ "'\n" + 
//        	"\n" + 
//        	"\n" + 
//        	" and a.pdept_id = 0\n" + 
//        	"      --  and\n" + 
//        	"      -- a.dept_id in (select t.dept_id\n" + 
//        	"      --                     from hr_c_dept t\n" + 
//        	"      --                    start with t.pdept_id = 0\n" + 
//        	"      --                   connect by prior t.dept_id = t.pdept_id)\n" + 
//        	"   and A.IS_USE = 'U'\n" + 
//        	"   AND A.ENTERPRISE_CODE = '" +argEnterpriseCode+ "'\n" + 
//        	" GROUP BY (A.DEPT_ID, A.DEPT_CODE, A.DEPT_NAME, C.STATION_ID, C.STATION_NAME\n" + 
//        	"          )\n" + 
//        	"\n" + 
//        	"\n" + 
//        	" union\n" + 
//        	"\n" + 
//        	" (select distinct y.dept_id,y.dept_name ,null ,null,null,null,null,null,y.dept_code from hr_c_dept y\n" + 
//        	" where y.is_use='U'\n" + 
//        	" and y.pdept_id=0\n" + 
//        	" and\n" + 
//        	" (select count(*) from HR_J_DEPSTATIONCORRESPOND u\n" + 
//        	"         where u.is_use='Y'\n" + 
//        	"         and u.dept_id in (select t.dept_id\n" + 
//        	"                                              from hr_c_dept t\n" + 
//        	"                                             where (t.dept_id = y.dept_id or\n" + 
//        	"                                                   t.dept_id in\n" + 
//        	"                                                   (select ff.dept_id\n" + 
//        	"                                                       from hr_c_dept ff\n" + 
//        	"                                                      start with ff.dept_id =\n" + 
//        	"                                                                 y.dept_id\n" + 
//        	"                                                     connect by prior\n" + 
//        	"                                                                 ff.dept_id =\n" + 
//        	"                                                                 ff.pdept_id))))=0)\n" + 
//        	"\n" + 
//        	"\n" + 
//        	" )tt\n" + 
//        	"ORDER BY tt.DEPT_CODE,tt.STATION_ID";
        
        //modify by fyyang 20100623
        String sql=
        	"select tt.oneLevel,\n" +
        	"       tt.station_id,\n" + 
        	"       tt.oneLevelName,\n" + 
        	"       tt.station_name,\n" + 
        	"       sum(tt.standard_person_num),\n" + 
        	"       sum(tt.nowNum),\n" + 
        	"       sum(tt.inNum),\n" + 
        	"       sum(tt.outNum)\n" + 
        	"\n" + 
        	"  from (\n" + 
        	"\n" + 
        	"        select a.dept_code,\n" + 
        	"                a.dept_name,\n" + 
        	"                GETFirstLevelBYID(a.dept_id) oneLevel,\n" + 
        	"                GETDEPTNAME(GETFirstLevelBYID(a.dept_id)) oneLevelName,\n" + 
        	"                c.station_id,\n" + 
        	"                c.station_name,\n" + 
        	"                b.standard_person_num,\n" + 
        	"                (select count(1)\n" + 
        	"                   from HR_J_EMP_INFO E\n" + 
        	"                  WHERE TO_CHAR(E.MISSION_DATE, 'YYYY') <= '"+argYear+"'\n" + 
        	"                    AND e.dept_id = b.dept_id\n" + 
        	"                    AND C.STATION_ID = b.station_id\n" + 
        	"                    AND (E.IS_USE = 'Y' OR\n" + 
        	"                         (E.IS_USE = 'N' AND\n" + 
        	"                         (E.EMP_STATE = '3' or E.EMP_STATE = 'N') AND\n" + 
        	"                         TO_CHAR(E.DIMISSION_DATE, 'YYYY') > '"+argYear+"'))\n" + 
        	"                    AND E.ENTERPRISE_CODE = 'hfdc') nowNum,\n" + 
        	"                (select count(1)\n" + 
        	"                   from hr_j_newemployee f\n" + 
        	"                  where to_char(f.mission_date, 'YYYY') = '"+argYear+"'\n" + 
        	"                    and f.is_use = 'Y'\n" + 
        	"                    and f.enterprise_code = '"+argEnterpriseCode+"'\n" + 
        	"                    and f.new_deptid = b.dept_id\n" + 
        	"                    and f.station_id = b.station_id\n" + 
        	"\n" + 
        	"                 ) inNum,\n" + 
        	"                (select count(1)\n" + 
        	"                   from hr_j_dimission g, HR_J_EMP_INFO h\n" + 
        	"                  where g.emp_id = h.emp_id\n" + 
        	"                    and to_char(g.dimission_date, 'YYYY') = '"+argYear+"'\n" + 
        	"                    and g.is_use = 'Y'\n" + 
        	"                    and g.enterprise_code = '"+argEnterpriseCode+"'\n" + 
        	"                    and h.dept_id = b.dept_id\n" + 
        	"                    and h.station_id = b.station_id\n" + 
        	"\n" + 
        	"                 ) outNum\n" + 
        	"          from hr_c_dept a, HR_J_DEPSTATIONCORRESPOND b, hr_c_station c\n" + 
        	"         where a.dept_id = b.dept_id(+)\n" + 
        	"           and b.station_id = c.station_id(+)\n" + 
        	"           and a.is_use = 'Y'\n" + //update by sychen 20100901
//        	"           and a.is_use = 'U'\n" + 
        	"           and b.is_use(+) = 'Y'\n" + 
        	"           and b.enterprise_code(+) = '"+argEnterpriseCode+"'\n" + 
        	"           and a.enterprise_code = '"+argEnterpriseCode+"') tt\n" + 
        	" group by tt.oneLevel, tt.station_id, tt.oneLevelName, tt.station_name\n" + 
        	" order by tt.oneLevel, tt.station_id";



        String sqlCount = "select count(*) from (" + sql + ") \n";
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        List lstResult = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
            List<HrJTotalBean> arrlist = new ArrayList<HrJTotalBean>();
            Iterator it = lstResult.iterator();
            while (it.hasNext()) {
                HrJTotalBean totalBean = new HrJTotalBean();
                Object[] data = (Object[]) it.next();
                // 部门名称
                if (null != data[2]) {
                    totalBean.setDeptName(data[2].toString());
                }
                // 岗位名称
                if (null != data[3]) {
                    totalBean.setStationName(data[3].toString());
                }
                // 岗位标准人数
                if (null != data[4]) {
                    totalBean.setStationNum(data[4].toString());
                }
                // 现在人数
                if (null != data[5]) {
                    totalBean.setNowNum(data[5].toString());
                }
                // 新进人数
                if (null != data[6]) {
                    totalBean.setNewNum(data[6].toString());
                }
                // 离职人数
                if (null != data[7]) {
                    totalBean.setDimissionNum(data[7].toString());
                }
                arrlist.add(totalBean);
            }
            PageObject result = new PageObject();
            // 符合条件的社会关系信息
            result.setList(arrlist);
            // 符合条件的社会关系信息的总数
            result.setTotalCount(totalCount);
            LogUtil.log("EJB:新进/离职员工统计查询结束", Level.INFO, null);
            return result;
//        }
//    catch (RuntimeException e) {
//            LogUtil.log("EJB:新进/离职员工统计查询错误", Level.SEVERE, e);
//            throw e;
//        }
    }

}
