/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action.hr.ca;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.AttendanceStatisticsQueryFacadeRemote;
import power.ejb.hr.ca.DeptleaveStatisticsQueryInfo;
import power.web.birt.bean.hr.ca.DeptleaveStatisticsBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * 部门请假单报表Action
 * @author zhujie
 *
 */
public class DeptleaveStatisticsAction extends AbstractAction{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 远程 */
    private AttendanceStatisticsQueryFacadeRemote remote;
    /** 员工姓名截断字节数 */
    private static final int TEN = 10;
    /** 假别截断字节数 */
    private static final int THIRTEEN = 13;
    /** 部门名称截断字节数 */
    private static final int FITTEEN = 15;
    /** 备注截断字节数 */
    private static final int SEVENTEEN = 17;
    /** 原因及去向截断字节数 */
    private static final int NINETEEN = 19;
    
    /**
     * 构造函数
     */
    public DeptleaveStatisticsAction() {
        remote = (AttendanceStatisticsQueryFacadeRemote) factory.getFacadeRemote("AttendanceStatisticsQueryFacade");
    }
    
    /**
     * 部门请假单查询
     * @param deptId 部门id
     * @param yearMonth 选择年月
     * @param signState 签字状态
     * @param enterpriseCode 企业编码
     * @return DeptleaveStatisticsBean
     */
    @SuppressWarnings("unchecked")
    public DeptleaveStatisticsBean getDeptleaveStatistics(String deptId,String yearMonth,
            String signState,String enterpriseCode){
        DeptleaveStatisticsBean entity = new DeptleaveStatisticsBean();
        PageObject pg = new PageObject();
        List<DeptleaveStatisticsQueryInfo> result = new ArrayList<DeptleaveStatisticsQueryInfo>();
        pg = remote.findDeptleaveStatisticsQueryInfo(deptId, yearMonth, signState, enterpriseCode);
        if(pg!=null){
            result = (List<DeptleaveStatisticsQueryInfo>)pg.getList();
        }
        // 对信息进行处理
        if(result!=null&&result.size()>0){
            DeptleaveStatisticsQueryInfo bean = null;
            for(int i=0;i<result.size();i++){
                List<String> strList = new ArrayList();
                bean = result.get(i);
                // 对员工姓名进行截断前10个字节数处理
                if(bean.getChsName()!=null&&bean.getChsName().getBytes().length>TEN){
                    bean.setChsName(commUtils.cutByByte(bean.getChsName(), TEN));
                }
                // 对部门名称进行换行处理
                String deptName = commUtils.addBrByByteLengthForHR(bean.getDeptName(),
                        FITTEEN);
                bean.setDeptName(deptName.replace(Constant.BLANK, Constant.MARK_BLACK));
                strList.add(deptName);
                // 对假别进行换行处理
                String vacationType = commUtils.addBrByByteLengthForHR(bean.getVacationType(),
                        THIRTEEN);
                bean.setVacationType(vacationType.replace(Constant.BLANK, Constant.MARK_BLACK));
                strList.add(vacationType);
                // 对原因进行换行处理
                String reason = commUtils.addBrByByteLengthForHR(bean.getReason(),
                        NINETEEN);
                bean.setReason(reason.replace(Constant.BLANK, Constant.MARK_BLACK));
                strList.add(reason);
                // 对去向进行换行处理
                String whither = commUtils.addBrByByteLengthForHR(bean.getWhither(),
                        NINETEEN);
                bean.setWhither(whither.replace(Constant.BLANK, Constant.MARK_BLACK));
                strList.add(whither);
                // 对备注进行换行处理
                String memo = commUtils.addBrByByteLengthForHR(bean.getMemo(),
                        SEVENTEEN);
                bean.setMemo(memo.replace(Constant.BLANK, Constant.MARK_BLACK));
                strList.add(memo);
                // 叠加后的计数
                bean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
            }
            entity.setList(result);
        }
        return entity;
    }
}
