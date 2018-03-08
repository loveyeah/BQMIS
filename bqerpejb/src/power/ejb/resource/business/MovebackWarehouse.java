/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;


import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.*;

/**
 * 退库管理接口.
 * 
 * @author liugonglei
 */
@Remote
public interface MovebackWarehouse {
    /**
     * 通过领料单号查找领料单信息
     * @param strReceive_No
     * @param strEnterPrise_Code
     * @return
     */
    public PageObject findByReceiveNo(String strReceive_No,String strEnterPrise_Code,int start,int limit);
    /**
     * 查找仓库信息
     * @param strOrder_No
     * @param lngSequence_No
     * @param lngMaterial_Id
     * @param strEnterPrise_Code
     * @return
     */
    public PageObject getWareHouse(String strOrder_No,Long lngSequence_No,Long  lngMaterial_Id,String strEnterPrise_Code);
    /**
     * 取得事物原因
     * @param strEnterpriseCode
     * @return
     */
    public PageObject getMoveBackReason(String strEnterpriseCode);
    /**
     * 查找库位信息
     * @param strOrder_No
     * @param lngSequence_No
     * @param lngMaterial_Id
     * @param strFrom_Whs_Id
     * @param strEnterPrise_Code
     * @return
     */
    public PageObject getLocationName(String strOrder_No, Long lngSequence_No,
            Long lngMaterial_Id, String strFrom_Whs_Id,String strEnterPrise_Code);
    /**
     * 取得领料单明细信息
     * @param issueDetailsId
     * @return
     */
    public InvJIssueDetails getIssueDetails(String strIssueDetailsId);
    /**
     *  更新领料单明细
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
    public InvJIssueDetails updateIssueDetails(InvJIssueDetails entity)
            throws CodeRepeatException;
    /**
     * 取得物料需求计划明细
     * @param requirementDetailId
     * @return
     * @throws CodeRepeatException
     */
    public MrpJPlanRequirementDetail getPlanRequirementDetail(
            String strRequirementDetailId) throws CodeRepeatException;
    /**
     *  更新物料需求计划明细
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
    public MrpJPlanRequirementDetail updatePlanRequirementDetail(MrpJPlanRequirementDetail entity)
            throws CodeRepeatException;
    /**
     * 取得事物作用流水号
     * @param transCode
     * @param entrypriseCode
     * @return
     */
    public Object[] getTransId(String strTransCode,String strEntrypriseCode);
    /**
     * 事务历史表插入
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
    public InvJTransactionHis saveTransactionHis(InvJTransactionHis entity)
            throws CodeRepeatException;
    /**
     * 取得库存物料记录
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @return
     */
    public InvJWarehouse getJWarehouse(String strMaterialId,
            String strWarehouseInvId, String strEnterpriseCode);
    /**
     * 更新库存物料记录
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
    public InvJWarehouse updateJWarehouse(InvJWarehouse entity)
            throws CodeRepeatException;
    /**
     * 取得库位物料记录表记录
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @param locatinInvId
     * @return
     */
    public InvJLocation getJLocation(String strMaterialId,
            String strWarehouseInvId, String strEnterpriseCode,String strLocatinInvId);
    /**
     * 更新库位物料记录
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
    public InvJLocation updateJLocation(InvJLocation entity)
            throws CodeRepeatException;
    /**
     * 取得批号记录表
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @param locatinInvId
     * @param lotNo
     * @return
     */
    public InvJLot getJLot(String strMaterialId, String strWarehouseInvId,
            String strEnterpriseCode, String strLocatinInvId, String strLotNo);
    /**
     * 更新批号记录表
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
    public InvJLot updateJLot(InvJLot entity)
            throws CodeRepeatException;
    /**
     * 取得批号
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @param locatinInvId
     * @return
     */
    public String getLotNo(String strOrder_No, Long lngSequence_No, Long lngMaterial_Id,
            String strFrom_Whs_Id, String strFrom_location_Id,String strEnterPrise_Code);
}
