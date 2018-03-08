/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.bean.hr.ca;

import java.util.ArrayList;
import java.util.List;

import power.ejb.hr.ca.DeptleaveStatisticsQueryInfo;

/**
 * 部门请假单报表Bean
 * @author zhujie
 *
 */
public class DeptleaveStatisticsBean {

    /** 部门请假单报表数据 */
    private List<DeptleaveStatisticsQueryInfo> list =new ArrayList<DeptleaveStatisticsQueryInfo>();

    /**
     * @return the list
     */
    public List<DeptleaveStatisticsQueryInfo> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<DeptleaveStatisticsQueryInfo> list) {
        this.list = list;
    }
}
