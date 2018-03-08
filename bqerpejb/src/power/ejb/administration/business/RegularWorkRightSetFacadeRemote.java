/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 定期工作权限查询Remote
 * 
 * @author chaihao
 * 
 */
@Remote
public interface RegularWorkRightSetFacadeRemote {

    /**
     * 按工作类别编码查找定期工作权限
     * 
     * @param strWorkTypeCode 工作类别编码
     * @param strEnterpriseCode 企业代码
     * @param rowStartIdxAndCount 检索数据附加参数
     * @return 结果
     */
    public PageObject findRegularWorkRight(String strWorkTypeCode, String strEnterpriseCode,
            int... rowStartIdxAndCount) throws SQLException;
}
