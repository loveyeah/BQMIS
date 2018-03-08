/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 车辆进出查询Remote 接口
 *
 * @author zhaomingjian
 * @version 1.0
 */
@Remote
public interface CarPassSearchFacadeRemote {
	/**
     * 
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @param passcode 通行证号
     * @param firm 所在单位
     * @param rowStartIdxAndCount 开始行及限制行
     * @return PageObject 
     */
    public PageObject getCarPassInfoDetails(String startDate,String endDate,String passcode,String firm,String strEnterpriseCode,final int... rowStartIdxAndCount);

}
