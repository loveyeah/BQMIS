/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.hr;

import java.util.List;

import power.ejb.hr.DismissionStaffListBean;
import power.ejb.hr.HrJTotalBean;
import power.ejb.hr.NewStaffListBean;

/**
 * 新进/离职员工打印信息Bean
 * @author wangpeng
 *
 */
public class GotAndLostWorkerBean {

    /** 新进/离职员工统计表打印信息 */
    private List<HrJTotalBean> lstGotAndLostWorker;
    /** 新进员工花名册打印信息 */
    private List<NewStaffListBean> lstGotWorker;
    /** 离职员工花名册打印信息 */
    private List<DismissionStaffListBean> lstLostWorker;

    /**
     * 构造方法
     */
    public GotAndLostWorkerBean() {

    }

    /**
     * @return 新进/离职员工统计表打印信息
     */
    public List<HrJTotalBean> getLstGotAndLostWorker() {
        return lstGotAndLostWorker;
    }

    /**
     * 设定新进/离职员工统计表打印信息
     * @param lstGotAndLostWorker 新进/离职员工统计表打印信息
     */
    public void setLstGotAndLostWorker(List<HrJTotalBean> lstGotAndLostWorker) {
        this.lstGotAndLostWorker = lstGotAndLostWorker;
    }

    /**
     * @return 新进员工花名册打印信息
     */
    public List<NewStaffListBean> getLstGotWorker() {
        return lstGotWorker;
    }

    /**
     * 设定新进员工花名册打印信息
     * @param lstGotWorker 新进员工花名册打印信息
     */
    public void setLstGotWorker(List<NewStaffListBean> lstGotWorker) {
        this.lstGotWorker = lstGotWorker;
    }

    /**
     * @return 离职员工花名册打印信息
     */
    public List<DismissionStaffListBean> getLstLostWorker() {
        return lstLostWorker;
    }

    /**
     * 设定离职员工花名册打印信息
     * @param lstLostWorker 离职员工花名册打印信息
     */
    public void setLstLostWorker(List<DismissionStaffListBean> lstLostWorker) {
        this.lstLostWorker = lstLostWorker;
    }

}
