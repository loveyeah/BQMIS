/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action.hr.ca;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.AttendanceStatisticsQueryFacadeRemote;
import power.ejb.hr.ca.DeptOndutyStatisticsQueryInfo;
import power.web.birt.bean.hr.ca.DeptOndutyStatisticsBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * 部门请假单报表Action
 * @author zhujie
 *
 */
public class DeptOndutyStatisticsAction extends AbstractAction{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 远程 */
    private AttendanceStatisticsQueryFacadeRemote remote;
    /** 员工姓名截断字节数 */
    private static final int TEN = 10;
    /** 部门名称截断字节数 */
    private static final int SIXTYNINE = 69;
    
    /**
     * 构造函数
     */
    public DeptOndutyStatisticsAction() {
        remote = (AttendanceStatisticsQueryFacadeRemote) factory.getFacadeRemote("AttendanceStatisticsQueryFacade");
    }
    
    /**
     * 部门出勤统计查询
     * @param deptId 部门id
     * @param year 考勤年份
     * @param month 考勤月份
     * @param enterpriseCode 企业编码
     * @return DeptOndutyStatisticsBean
     */
    @SuppressWarnings("unchecked")
    public DeptOndutyStatisticsBean getDeptOndutyStatistics(String deptId,String year,
            String month,String enterpriseCode){
        DeptOndutyStatisticsBean entity = new DeptOndutyStatisticsBean();
        PageObject pg = new PageObject();
        List<DeptOndutyStatisticsQueryInfo> result = new ArrayList<DeptOndutyStatisticsQueryInfo>();
        pg = remote.findDeptOndutyStatisticsQueryInfo(deptId, year, month, enterpriseCode);
        if(pg!=null){
            result = (List<DeptOndutyStatisticsQueryInfo>)pg.getList();
        }
        // 对信息进行处理
        if(result!=null&&result.size()>0){
            DeptOndutyStatisticsQueryInfo bean = null;
            for(int i=0;i<result.size();i++){
                List<String> strList = new ArrayList();
                bean = result.get(i);
                // 对员工姓名进行截断前10个字节数处理
                if(bean.getChsName()!=null&&bean.getChsName().getBytes().length>TEN){
                    bean.setChsName(commUtils.cutByByte(bean.getChsName(), TEN));
                }
                // 对部门名称进行换行处理
                String deptName = commUtils.addBrByByteLengthForHR(bean.getDeptName(),
                        SIXTYNINE);
                bean.setDeptName(deptName.replace(Constant.BLANK, Constant.MARK_BLACK));
                strList.add(deptName);
                // 叠加后的计数
                bean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
            }
            entity.setList(result);
        }
        return entity;
    }
}
