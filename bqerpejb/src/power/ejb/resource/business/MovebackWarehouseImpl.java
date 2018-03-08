/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.*;
import power.ejb.resource.form.MovebackWarehouseInfo;

/**
 * 退库管理方法体.
 * 
 * @author liugonglei
 */
@Stateless
public class MovebackWarehouseImpl implements MovebackWarehouse {
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    @PersistenceContext
    private EntityManager entityManager;

	@EJB(beanName = "BpCMeasureUnitFacade")
    protected BpCMeasureUnitFacadeRemote remote;
	@EJB(beanName = "UnitInfoImpl")
    /**
     * 通过领料单号查找领料单信息
     * @param strReceive_No
     * @param strEnterPrise_Code
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject findByReceiveNo(String strReceive_No,
			String strEnterPrise_Code, int start, int limit) {
		LogUtil.log("Find receivemessage width RECEIVE_NO：" + strReceive_No, Level.INFO, null);
        try {
            PageObject result = new PageObject();
            String sql = "select \n"
                + "A.ISSUE_DETAILS_ID \n"
                + ",B.ISSUE_NO \n"
                + ",A.REQUIREMENT_DETAIL_ID \n"
                + ",A.MATERIAL_ID \n"
                + ",A.ACT_ISSUED_COUNT \n"
                + ",B.RECEIPT_BY \n"
                + ",B.RECEIPT_DEP \n"
                + ",A.ISSUE_HEAD_ID \n"
                + ",B.FEE_BY_DEP \n"
                + ",C.MATERIAL_NO \n"
                + ",C.MATERIAL_NAME \n"
                + ",C.STOCK_UM_ID \n"
                + ",A.LAST_MODIFIED_DATE \n"
                + "from \n" 
                +"INV_J_ISSUE_DETAILS A \n" 
                +",INV_J_ISSUE_HEAD B \n"
                +",INV_C_MATERIAL C \n"
                + "where  \n"
                + "A.IS_USE = 'Y' \n"
                + "AND C.IS_USE = 'Y' \n"
                + "AND B.IS_USE = 'Y' \n"
                + "AND B.ISSUE_HEAD_ID = A.ISSUE_HEAD_ID \n"
                + "AND A.MATERIAL_ID = C.MATERIAL_ID \n"
                + "AND B.ISSUE_NO = '"+strReceive_No+"'\n"
                + "AND B. ISSUE_STATUS = '2' \n"
                + "AND A.ACT_ISSUED_COUNT >0 \n"
                + "AND A.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                + "AND B.ENTERPRISE_CODE = '"+strEnterPrise_Code+"'\n"
                + "AND C.ENTERPRISE_CODE = '"+strEnterPrise_Code+"'\n"
                + "order by A.ISSUE_DETAILS_ID";
            String countSql = "select \n"
                + "COUNT(A.ISSUE_DETAILS_ID) \n"
                + "from \n" 
                +"INV_J_ISSUE_DETAILS A \n" 
                +",INV_J_ISSUE_HEAD B \n"
                +",INV_C_MATERIAL C \n"
                + "where  \n"
                + "A.IS_USE = 'Y' \n"
                + "AND C.IS_USE = 'Y' \n"
                + "AND B.IS_USE = 'Y' \n"
                + "AND B.ISSUE_HEAD_ID = A.ISSUE_HEAD_ID \n"
                + "AND A.MATERIAL_ID = C.MATERIAL_ID \n"
                + "AND B.ISSUE_NO = '"+strReceive_No+"'\n"
                + "AND B. ISSUE_STATUS = '2' \n"
                + "AND A.ACT_ISSUED_COUNT >0 \n"
                + "AND A.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                + "AND B.ENTERPRISE_CODE = '"+strEnterPrise_Code+"'\n"
                + "AND C.ENTERPRISE_CODE = '"+strEnterPrise_Code+"'";
            List list = bll.queryByNativeSQL(sql,start,limit);
            List<MovebackWarehouseInfo> arrayList =new ArrayList();
            if(list != null&& list.size()>0){
                Iterator it = list.iterator();
                while(it.hasNext()){
                    MovebackWarehouseInfo mbackWh = new MovebackWarehouseInfo();
                    Object[] listData = (Object[])it.next();
                    if(null!=listData[0]){
                        mbackWh.setIssueDetailsId(listData[0].toString());
                    }
                    if(null!=listData[1]){
                        mbackWh.setIssueNo(listData[1].toString());
                    }
                    if(null!= listData[2]){
                        mbackWh.setRequirementDetailId(listData[2].toString());
                    }
                    if(null!= listData[3]){
                        mbackWh.setMaterialId(listData[3].toString());
                    }
                    if(null!=listData[4]){
                        mbackWh.setActIssuedCount(listData[4].toString());
                    }
                    if(null!=listData[5]){
                        mbackWh.setReceiptBy(listData[5].toString());
                    }
                    if(null!=listData[6]){
                        mbackWh.setReceiptDept(listData[6].toString());
                    }
                    if(null != listData[7]){
                        mbackWh.setIssueHeadId(listData[7].toString());
                    }
                    if(null!= listData[8]){
                        mbackWh.setFeeByDep(listData[8].toString());
                    }
                    if(null!= listData[9]){
                        mbackWh.setMaterialNo(listData[9].toString());
                    }
                    if(null!=listData[10]){
                        mbackWh.setMaterialName(listData[10].toString());
                    }
                    if(null!=listData[11]){
                    	Long id = Long.parseLong(listData[11].toString());
                    	BpCMeasureUnit pbj = remote.findById(id);
                        if(pbj == null){
                        	mbackWh.setStockUmName("");
                        }else {
                        	mbackWh.setStockUmName(pbj.getUnitName());
                        }
                    }
                    if(listData[12] instanceof Date){
                    	InvJIssueDetails entity = getIssueDetails(mbackWh.getIssueDetailsId());
                    	mbackWh.setLastModifiedDate(entity.getLastModifiedDate().getTime());
                    }
                    arrayList.add(mbackWh);
                }
            }
            Object countlist = bll.getSingal(countSql);
            result.setList(arrayList);
            result.setTotalCount(new Long(countlist.toString()));
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("Find receivemessage width RECEIVE_NO：" + strReceive_No, Level.INFO, re);
            throw re;
        }
    }
    /**
     * 查找仓库信息
     * @param strOrder_No
     * @param lngSequence_No
     * @param lngMaterial_Id
     * @param strEnterPrise_Code
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject getWareHouse(String strOrder_No, Long lngSequence_No,
            Long lngMaterial_Id,String strEnterPrise_Code) {
        LogUtil.log("Find WareHouse width strOrder_No：" + strOrder_No
                + ",lngSequence_No:" + String.valueOf(lngSequence_No) + "lngMaterial_Id:"
                + String.valueOf(lngMaterial_Id), Level.INFO, null);
        try {
            PageObject result = new PageObject();
            String sql = "select \n"
                + "min(A.LOT_NO), \n"
                + "A.FROM_WHS_NO, \n"
                + "B.WHS_NAME \n"
                + "from  \n"
                + "INV_J_TRANSACTION_HIS A, \n"
                + "INV_C_WAREHOUSE B, \n"
                + "INV_C_LOCATION C, \n"
                + "INV_C_TRANSACTION D \n"
                + "where  \n"
                + "A.ORDER_NO ='"+strOrder_No +"'\n"
                + "AND A.SEQUENCE_ID = ? \n" 
                + "AND A.FROM_WHS_NO = B.WHS_NO \n"
                + "AND A.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                + "AND D.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                + "AND D.TRANS_CODE = 'I' \n"
                + "AND A.TRANS_ID = D.     TRANS_ID \n"
                + "AND A.MATERIAL_ID = ? \n"
                + " group by A.FROM_WHS_NO,B.WHS_NAME";
            Object[] params = {lngSequence_No,lngMaterial_Id};
            List list = bll.queryByNativeSQL(sql, params);
            List<MovebackWarehouseInfo> arrayList =new ArrayList();
            if(null!=list&& 0<list.size()){
                Iterator it = list.iterator();
                while(it.hasNext()){
                    MovebackWarehouseInfo mbackWh = new MovebackWarehouseInfo();
                    Object[] listData = (Object[])it.next();
                    if(null != listData[0]){
                        mbackWh.setLotNo(listData[0].toString());
                    }
                    if (null !=listData[1]){
                        mbackWh.setFromWhsId(listData[1].toString());
                    }
                    if(null != listData[2]){
                        mbackWh.setWhsName(listData[2].toString());
                    }
                    arrayList.add(mbackWh);
                }
            }
            result.setList(arrayList);
            return result;
        } catch (RuntimeException er) {
            LogUtil.log("Find WareHouse width strOrder_No：" + strOrder_No
                    + ",lngSequence_No:" + String.valueOf(lngSequence_No) + "lngMaterial_Id:"
                    + String.valueOf(lngMaterial_Id), Level.INFO, er);
            throw er;
        }
    }
    /**
     * 查找库位信息
     * @param strOrder_No
     * @param lngSequence_No
     * @param lngMaterial_Id
     * @param strFrom_Whs_Id
     * @param strEnterPrise_Code
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject getLocationName(String strOrder_No, Long lngSequence_No,
			Long lngMaterial_Id, String strFrom_Whs_Id,
			String strEnterPrise_Code) {
        LogUtil.log("Find WareHouse width strOrder_No：" + strOrder_No
                + ",lngSequence_No:" + String.valueOf(lngSequence_No)
                + "lngMaterial_Id:" + String.valueOf(lngMaterial_Id) + "strFrom_Whs_Id:"
                + String.valueOf(strFrom_Whs_Id), Level.INFO, null);
        try{
            PageObject result = new PageObject();
            String sql = "select \n" 
                + "min(A.LOT_NO), \n"
                + "A.FROM_LOCATION_NO, \n"
                + "C.LOCATION_NAME  \n"
                + "from  \n"
                + "INV_J_TRANSACTION_HIS A, \n"
                + "INV_C_WAREHOUSE B, \n"
                + "INV_C_LOCATION C, \n"
                + "INV_C_TRANSACTION D \n"
                + "where  \n"
                + "A.ORDER_NO ='"+strOrder_No +"'\n"
                + "AND A.SEQUENCE_ID = ? \n" 
                + "AND A.FROM_WHS_NO = B.WHS_NO \n"
                + "AND A.FROM_WHS_NO = C. WHS_NO \n"
                + "AND A.FROM_WHS_NO = '"+strFrom_Whs_Id+"'\n"
                + "AND A.FROM_LOCATION_NO = C. LOCATION_NO \n"
                + "AND D.TRANS_CODE = 'I' \n"
                + "AND A.TRANS_ID = D.     TRANS_ID \n"
                + "AND A.MATERIAL_ID = ? \n"
                + "AND A.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                + "AND D.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                + "group by A.FROM_LOCATION_NO,C.LOCATION_NAME";
            Object[] params = {lngSequence_No,lngMaterial_Id};
            List list = bll.queryByNativeSQL(sql, params);
            List<MovebackWarehouseInfo> arrayList =new ArrayList();
            if(null!=list&& 0<list.size()){
                Iterator it = list.iterator();
                while(it.hasNext()){
                    MovebackWarehouseInfo mbackWh = new MovebackWarehouseInfo();
                    Object[] listData = (Object[])it.next();
                    if(null != listData[0]){
                        mbackWh.setLotNo(listData[0].toString());
                    }
                    if (null !=listData[1]){
                        mbackWh.setFromLocationId(listData[1].toString());
                    }
                    if(null != listData[2]){
                        mbackWh.setLocationName(listData[2].toString());
                    }
                    arrayList.add(mbackWh);
                }
            }
            result.setList(arrayList);
            return result;
        }
        catch (RuntimeException er){
            LogUtil.log("Find WareHouse width strOrder_No：" + strOrder_No
                    + ",lngSequence_No:" + String.valueOf(lngSequence_No)
                    + "lngMaterial_Id:" + String.valueOf(lngMaterial_Id) + "strFrom_Whs_Id:"
                    + String.valueOf(strFrom_Whs_Id), Level.INFO, er);
            throw er;
        }
    }
    /**
     * 取得事物原因
     * @param strEnterpriseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject getMoveBackReason(String strEnterpriseCode) {
        LogUtil.log("finding INV_C_REASON instance", Level.INFO,null);
        try {
            PageObject result = new PageObject();
            String sql = " select * from INV_C_REASON A, INV_C_TRANSACTION B where A.ENTERPRISE_CODE = '"
                    + strEnterpriseCode + "'\n"
                    +"AND A.IS_USE = 'Y' AND A.TRANS_ID = B.TRANS_ID \n"
                    + "AND B.TRANS_CODE = 'R'";
            List<InvCReason> list = bll.queryByNativeSQL(sql, InvCReason.class);
            result.setList(list);
            return result;
        }catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 更新领料详细表
     * 
     * @param actIssuedCount
     * @param lastModifiedBy
     * @param lastModifiedDate
     * @param issueDetailsId
     * @return
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InvJIssueDetails updateIssueDetails(InvJIssueDetails entity)
            throws CodeRepeatException {
        LogUtil.log("Update INV_J_ISSUE_DETAILS instance", Level.INFO, null);
        try {
            entity.setLastModifiedDate(new Date());
            InvJIssueDetails result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
            LogUtil.log("update failed", Level.SEVERE, e);
            throw new CodeRepeatException("修改失败");
        }
    }
    /**
     *  更新物料需求计划明细
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MrpJPlanRequirementDetail updatePlanRequirementDetail(
            MrpJPlanRequirementDetail entity) throws CodeRepeatException {
        LogUtil.log("Update INV_J_ISSUE_DETAILS instance", Level.INFO, null);
        try {
            entity.setLastModifiedDate(new Date());
            MrpJPlanRequirementDetail result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
            LogUtil.log("update failed", Level.SEVERE, e);
            throw new CodeRepeatException("修改失败");
        }
    }
    /**
     * 登陆事务历史表
     * @entity
     * @return
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InvJTransactionHis saveTransactionHis(InvJTransactionHis entity)
            throws CodeRepeatException {
        LogUtil.log("saving InvJTransactionHis instance", Level.INFO, null);
        try {
            if (entity.getTransHisId() == null) {
                // 设定主键值
                entity.setTransHisId(bll.getMaxId("INV_J_TRANSACTION_HIS",
                        "TRANS_HIS_ID"));
            }
            // 保存
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
            return entity;
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }

    }
    /**
     * 更新库存物料记录
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InvJWarehouse updateJWarehouse(InvJWarehouse entity)
        throws CodeRepeatException {
        LogUtil.log("Update InvJWarehouse instance", Level.INFO, null);
        try {
            InvJWarehouse result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
            LogUtil.log("update failed", Level.SEVERE, e);
            throw new CodeRepeatException("修改失败");
        }
    }
    /**
     * 更新库位物料记录
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InvJLocation updateJLocation(InvJLocation entity)
            throws CodeRepeatException {
        LogUtil.log("Update InvJLocation instance", Level.INFO, null);
        try {
            InvJLocation result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
            LogUtil.log("update failed", Level.SEVERE, e);
            throw new CodeRepeatException("修改失败");
        }
    }
    /**
     * 更新批号记录表
     * @param entity
     * @return
     * @throws CodeRepeatException
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InvJLot updateJLot(InvJLot entity) throws CodeRepeatException {
        LogUtil.log("Update InvJLot instance", Level.INFO, null);
        try {
            InvJLot result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException e) {
                LogUtil.log("update failed", Level.SEVERE, e);
            throw new CodeRepeatException("修改失败");
        }
    }
    /**
     * 取得领料单明细信息
     * 
     * @param issueDetailsId
     * @return
     */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InvJIssueDetails getIssueDetails(String strIssueDetailsId) {
        LogUtil.log("finding InvJIssueDetails instance with strIssueDetailsId: " + strIssueDetailsId,
                Level.INFO, null);
        try {
            InvJIssueDetails instance = entityManager.find(
                    InvJIssueDetails.class, Long.valueOf(strIssueDetailsId));
            return instance;
        }catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 取得物料需求计划明细
     * 
     * @param requirementDetailId
     * @return
     * @throws CodeRepeatException
     */
    public MrpJPlanRequirementDetail getPlanRequirementDetail(
            String strRequirementDetailId) throws CodeRepeatException {
		LogUtil.log(
				"finding MrpJPlanRequirementDetail instance with strRequirementDetailId: "
						+ strRequirementDetailId, Level.INFO, null);
		try {
			MrpJPlanRequirementDetail instance = entityManager.find(
					MrpJPlanRequirementDetail.class, Long
							.valueOf(strRequirementDetailId));
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
    /**
	 * 取得事物作用流水号
	 * 
	 * @param transCode
	 * @param entrypriseCode
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Object[] getTransId(String strTransCode, String strEntrypriseCode) {
        LogUtil.log("finding TransId with strTransCode: " + strTransCode, Level.INFO,
                null);
        try {
            String sql = "SELECT TRANS_ID, IS_OPEN_BALANCE, IS_RECEIVE, IS_ADJUST, IS_ISSUES"
                    + "\n"
                    + "FROM INV_C_TRANSACTION "
                    + "\n"
                    + "where TRANS_CODE ='"
                    + strTransCode
                    + "'\n"
                    + "AND ENTERPRISE_CODE = '"+strEntrypriseCode+"'\n"
                    + "AND IS_USE = 'Y'";
            List list = bll.queryByNativeSQL(sql);
            Object[] data = null;
            if(list.size()>0){
                data = (Object[]) list.get(0);
            }
            return data;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 取得库存物料记录
     * 
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public InvJWarehouse getJWarehouse(String strMaterialId,
            String strWarehouseInvId, String strEnterpriseCode) {
        LogUtil.log("finding InvJWarehouse instance with strMaterialId: "
                + strMaterialId + "strWarehouseInvId:" + strWarehouseInvId, Level.INFO,
                null);
        try {
            String sql = "select * from INV_J_WAREHOUSE \n"
                +"where \n"
                +"WHS_NO = '" + strWarehouseInvId +"'\n"
                +"AND MATERIAL_ID = " + Long.valueOf(strMaterialId) +"\n"
                +"AND ENTERPRISE_CODE ='" + strEnterpriseCode+"'\n"
                +"and IS_USE = 'Y'";
            List<InvJWarehouse> list = bll.queryByNativeSQL(sql, InvJWarehouse.class);
            InvJWarehouse entity = null;
            if(list.size()>0){
                entity = list.get(0);
            }
            return entity;
        }catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 取得库位物料记录表记录
     * 
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @param locatinInvId
     * @return
     */
    @SuppressWarnings("unchecked")
    public InvJLocation getJLocation(String strMaterialId, String strWarehouseInvId,
            String strEnterpriseCode, String strLocatinInvId) {
        LogUtil.log("finding InvJLocation instance with strMaterialId: "
                + strMaterialId + "strWarehouseInvId:" + strWarehouseInvId+
                "strLocatinInvId:"+strLocatinInvId, Level.INFO,
                null);
        try {
            String sql = "select * from INV_J_LOCATION \n"
                +"where\n"
                +"LOCATION_NO = '" + strLocatinInvId +"'\n"
                +"AND WHS_NO = '" + strWarehouseInvId +"'\n"
                +"AND MATERIAL_ID = " + Long.valueOf(strMaterialId) +"\n"
                +"AND ENTERPRISE_CODE = '" + strEnterpriseCode+"'\n"
                +"AND IS_USE = 'Y'";
            List<InvJLocation> list = bll.queryByNativeSQL(sql, InvJLocation.class);
            InvJLocation entity = null;
            if(list.size()>0){
                entity = list.get(0);
            }
            return entity;
        }catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 取得批号记录表
     * 
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @param locatinInvId
     * @param lotNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public InvJLot getJLot(String strMaterialId, String strWarehouseInvId,
            String strEnterpriseCode, String strLocatinInvId, String lotNo) {
        LogUtil.log("finding InvJLot instance with strMaterialId: " + strMaterialId
                + "strWarehouseInvId:" + strWarehouseInvId + "strLocatinInvId:"
                + strLocatinInvId + "lotNo:" + lotNo, Level.INFO, null);
        try {
            String sql = "select * from INV_J_LOT \n" + "where\n" + "LOT_NO= '"
                    + lotNo + "'\n";
            if (!"".equals(strLocatinInvId)) {
                sql = sql + "AND LOCATION_NO = '" + strLocatinInvId + "'\n";
            }
            sql = sql + "AND WHS_NO = '" + strWarehouseInvId + "'\n"
                    + "AND MATERIAL_ID = " + Long.valueOf(strMaterialId) + "\n"
                    + "AND ENTERPRISE_CODE = '" + strEnterpriseCode + "'\n"
                    + "AND IS_USE = 'Y'";
            List<InvJLot> list = bll.queryByNativeSQL(sql, InvJLot.class);
            InvJLot entity = null;
            if (list.size() > 0) {
                entity = list.get(0);
            }
            return entity;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 取得批号
     * @param materialId
     * @param warehouseInvId
     * @param enterpriseCode
     * @param locatinInvId
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getLotNo(String strOrder_No, Long lngSequence_No,
			Long lngMaterialId, String strFrom_Whs_Id,
			String strFrom_location_Id, String strEnterPrise_Code) {
		LogUtil.log("Find LotNo width strOrder_No：" + strOrder_No + ",lngSequence_No:"
                + String.valueOf(lngSequence_No) + ",lngMaterialId:"
                + String.valueOf(lngMaterialId) + ",strFrom_Whs_Id:" + strFrom_Whs_Id
                + ",strFrom_location_Id:" + strFrom_location_Id, Level.INFO, null);
        try {
            String sql = "select \n" + "min(A.LOT_NO) \n" + "from  \n"
                    + "INV_J_TRANSACTION_HIS A, \n" + "INV_C_WAREHOUSE B, \n"
                    + "INV_C_LOCATION C, \n" + "INV_C_TRANSACTION D \n"
                    + "where  \n" + "A.ORDER_NO ='" + strOrder_No + "'\n"
                    + "AND A.SEQUENCE_ID = ? \n"
                    + "AND A.FROM_WHS_NO = B.WHS_NO \n";
            if (!"".equals(strFrom_location_Id)) {
                sql = sql + "AND A. FROM_LOCATION_NO = '" + strFrom_location_Id
                        + "'\n";
            }
            sql = sql + "AND A.FROM_WHS_NO = C. WHS_NO \n"
                    + "AND A.FROM_WHS_NO = '" + strFrom_Whs_Id + "'\n"
                    + "AND A.FROM_LOCATION_NO = C. LOCATION_NO \n"
                    + "AND D.TRANS_CODE = 'I' \n"
                    + "AND A.TRANS_ID = D.TRANS_ID \n"
                    + "AND A.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                    + "AND D.ENTERPRISE_CODE = '"+strEnterPrise_Code +"'\n"
                    + "AND A.MATERIAL_ID = ? ";
            Object[] params = { lngSequence_No, lngMaterialId };
            List list = bll.queryByNativeSQL(sql, params);
            return list.get(0).toString();
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
}