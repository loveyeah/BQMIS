/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.bean.hr.ca;

import java.util.ArrayList;
import java.util.List;

import power.ejb.hr.ca.DeptOndutyStatisticsQueryInfo;

/**
 * 出勤统计报表Bean
 * @author zhujie
 *
 */
public class DeptOndutyStatisticsBean {
    
    /** 出勤统计报表数据 */
    private List<DeptOndutyStatisticsQueryInfo> list =new ArrayList<DeptOndutyStatisticsQueryInfo>();

    /**
     * @return the list
     */
    public List<DeptOndutyStatisticsQueryInfo> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<DeptOndutyStatisticsQueryInfo> list) {
        this.list = list;
    }
}
