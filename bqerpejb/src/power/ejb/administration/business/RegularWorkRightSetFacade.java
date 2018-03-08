/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.RegularWorkRightSetInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 定期工作权限查询Facade
 * 
 * @author chaihao
 * 
 */
@Stateless
public class RegularWorkRightSetFacade implements
        RegularWorkRightSetFacadeRemote {

    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 按工作类别编码查找定期工作权限
     * 
     * @param strWorkTypeCode 工作类别编码
     * @param strEnterpriseCode 企业代码
     * @param rowStartIdxAndCount 检索数据附加参数
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findRegularWorkRight(String strWorkTypeCode, String strEnterpriseCode,
            int... rowStartIdxAndCount) throws SQLException {
        LogUtil.log("EJB:工作权限查询开始", Level.INFO, null);
        try {
            // 查询SQL语句
            String strSql = "";
            // 查询行数SQL语句
            String strSqlCount = "";
            // 需要返回的结果
            PageObject result = new PageObject();
            // 构造查询SQL语句
            strSql = "SELECT "
                    + "A.ID,"
                    + "A.USER_CODE,"
                    + "B.CHS_NAME,"
                    + "C.DEPT_NAME,"
                    + "TO_CHAR(A.UPDATE_TIME, 'yyyy-mm-dd hh24:mi:ss') TIME "
                    + "FROM AD_C_RIGHT A LEFT JOIN HR_J_EMP_INFO B "
                    + "ON A.USER_CODE=B.EMP_CODE AND B.ENTERPRISE_CODE=? "
                    + "LEFT JOIN HR_C_DEPT C "
                    + "ON B.DEPT_ID=C.DEPT_ID AND C.ENTERPRISE_CODE=? "
                    + "WHERE A.IS_USE=? "
                    + "AND A.ENTERPRISE_CODE=? ";
            // 构造查询行数SQL语句
            strSqlCount = "SELECT "
                    + "COUNT(A.ID) "
                    + "FROM AD_C_RIGHT A LEFT JOIN HR_J_EMP_INFO B "
                    + "ON A.USER_CODE=B.EMP_CODE AND B.ENTERPRISE_CODE=? "
                    + "LEFT JOIN HR_C_DEPT C "
                    + "ON B.DEPT_ID=C.DEPT_ID AND C.ENTERPRISE_CODE=? "
                    + "WHERE A.IS_USE=? "
                    + "AND A.ENTERPRISE_CODE=? ";
            // SQL语句参数个数
            int intParamsCnt = 4;
            if ((null != strWorkTypeCode) && (!"".equals(strWorkTypeCode))) {
                intParamsCnt++;
            }
            // 查询参数数组
            Object[] params = new Object[intParamsCnt];
            // 设置参数
            params[0] = strEnterpriseCode;
            params[1] = strEnterpriseCode;
            params[2] = "Y";
            params[3] = strEnterpriseCode;
            if ((null != strWorkTypeCode) && (!"".equals(strWorkTypeCode))) {
                strSql += "AND A.WORKTYPE_CODE=? ";
                strSqlCount += "AND A.WORKTYPE_CODE=? ";
                params[4] = strWorkTypeCode;
            }
            LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
            // 行数
            Long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount, params).toString());
            // 查询结果List
            List lst = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
            List<RegularWorkRightSetInfo> lstRegularWorkRightSetInfo = new ArrayList<RegularWorkRightSetInfo>();
            if (lst != null) {
                Iterator it = lst.iterator();
                while (it.hasNext()) {
                    Object[] obj = (Object[]) it.next();
                    RegularWorkRightSetInfo regularWorkRightSetInfo = new RegularWorkRightSetInfo();
                    // 设置序号
                    if (null != obj[0]) {
                        regularWorkRightSetInfo.setId(obj[0].toString());
                    }
                    // 设置人员编码
                    if (null != obj[1]) {
                        regularWorkRightSetInfo.setUserCode(obj[1].toString());
                    }
                    // 设置姓名
                    if (null != obj[2]) {
                        regularWorkRightSetInfo.setName(obj[2].toString());
                    }
                    // 设置部门名称
                    if (null != obj[3]) {
                        regularWorkRightSetInfo.setDepName(obj[3].toString());
                    }
                    // 设置修改时间
                    if (null != obj[4]) {
                        regularWorkRightSetInfo.setUpdateTime(obj[4].toString());
                    }
                    lstRegularWorkRightSetInfo.add(regularWorkRightSetInfo);
                }
            }
            // 设置查询结果集
            result.setList(lstRegularWorkRightSetInfo);
            // 设置行数
            result.setTotalCount(lngTotalCount);
            // 返回查询结果
            return result;
        } catch (Exception e) {
            LogUtil.log("EJB:查询失败", Level.SEVERE, e);
            throw new SQLException();
        }
    }
}
