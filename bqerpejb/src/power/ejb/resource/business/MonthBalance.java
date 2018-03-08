/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.resource.form.MaterialDiscrepancyForReport;
import power.ejb.resource.form.MonthBalanceInfo;
import power.ejb.resource.form.WarehouseThisMonthInfo;
/**
 * 库存月结 interface
 * @author huangweijie
 */
@Remote 
public interface MonthBalance {
    /**
     * 批号本期接收
     * @param startId 结账起始流水号
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getReceipt(Long startId, String enterpriseCode);
    /**
     * 批号本期调整
     * @param startId 结账起始流水号
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getAdjust(Long startId, String enterpriseCode);
    /**
     * 批号本期出库
     * @param startId 结账起始流水号
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getIssue(Long startId, String enterpriseCode);
    /**
     * 仓库本期接收
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getReceipt(Long startId, String whoNo, String enterpriseCode);
    /**
     * 仓库本期调整
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getAdjust(Long startId, String whoNo, String enterpriseCode);
    /**
     * 仓库本期出库
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getIssue(Long startId, String whoNo, String enterpriseCode);
    /**
     * 库位本期接收
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getReceipt(Long startId, String whoNo, String locationNo, String enterpriseCode);
    /**
     * 库位本期调整
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getAdjust(Long startId, String whoNo, String locationNo, String enterpriseCode);
    /**
     * 库位本期出库
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getIssue(Long startId, String whoNo, String locationNo, String enterpriseCode);
    /**
     * 批号记录表
     * @param enterpriseCode 企业编码
     * @return 批号月结bean
     */
    public List<MonthBalanceInfo> getLotTable(String enterpriseCode);
    /**
     * 库存记录表
     * @param whsNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结bean
     */
    public List<MonthBalanceInfo> getWarehouseTable(String whsNo, String enterpriseCode);
    /**
     * 库位记录表
     * @param whsNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库位月结bean
     */
    public List<MonthBalanceInfo> getLocationTable(String whsNo, String locationNo, String enterpriseCode);
    /**
     * 由物料ID从库存物料记录表中得到异动数量
     * @param materialIdList 【物料ID】list
     * @param enterpriseCode 企业编码
     * @return 【物料ID，异动数量】list 
     */
    public List<MonthBalanceInfo> getMandQ(String materialIdList, String enterpriseCode);
    
    /**
     * 材料收发统计报表总信息
     * @return List<WarehouseThisMonthInfo>
     * add by ywliu 2009/7/4
     * modify by fyyang 090811
     */
    public List<WarehouseThisMonthInfo> getWarehouseThisMonthInfo(String dateMonth);
    
    /**
     * 材料收发统计报表明细信息
     * @return List<WarehouseThisMonthInfo>
     * add by ywliu 2009/7/4
     * modify by fyyang 090811
     */
    public List<WarehouseThisMonthInfo> getThisMonthInfoByWarehouse(String whsNo,String dateMonth);
    
    /**
     * 材料差异报表信息
     * add by fyyang 090817
     * @param dateMonth
     * @return
     */
    public MaterialDiscrepancyForReport getMaterialDiscrepancyInfo(String dateMonth);
    
    public List<WarehouseThisMonthInfo> getWarehouseThisMonthDetailInfo(String dateMonth,String whsNo);
    
    /**
     * add by liuyi 20100319 材料收发报表仓库月份查询  仓库收发存统计报表
     */
    public List<WarehouseThisMonthInfo> getMonthInfoOfWareByCodeAndMonth(String whsNo,String dateMonth);
    
    
    
    /**
     * 仓库收发存统计报表 总信息  
     * add by liuyi 20100319
     */
    public List<WarehouseThisMonthInfo> findWareOfMonthInfo(String dateMonth);
    
    
    public List<WarehouseThisMonthInfo> getWarehousOfMonthDetailInfo(String dateMonth,String whsNo);
}
