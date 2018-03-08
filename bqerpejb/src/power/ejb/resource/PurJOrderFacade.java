package power.ejb.resource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.PurchaseWarehouseDetailInfo;

/**
 * Facade for entity PurJOrder.
 * 
 * @see power.ejb.logistics.PurJOrder
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurJOrderFacade implements PurJOrderFacadeRemote {
    // property constants
    public static final String PUR_NO = "purNo";
    public static final String PROJECT = "project";
    public static final String BUYER = "buyer";
    public static final String CONTRACT_NO = "contractNo";
    public static final String SUPPLIER = "supplier";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String CURRENCY_TYPE = "currencyType";
    public static final String TAX_RATE = "taxRate";
    public static final String MEMO = "memo";
    public static final String WF_NO = "wfNo";
    public static final String WF_STATE = "wfState";
    public static final String ENTERPRISE_CODE = "enterpriseCode";
    public static final String IS_USE = "isUse";

    /** 字符串Key: 采购订单与需求计划关联 planRelated */
    private static final String KEY_ORDER_DETAILS_RELATE = "planRelated";
    /** 字符串Key: 采购单明细 detail */
    private static final String KEY_DETAIL = "detail";
    
    /** 字符串Key: 增加 add */
    private static final String KEY_ADD = "add";
    /** 字符串Key: 修改 update */
    private static final String KEY_UPDATE = "update";
    /** 字符串Key: 删除 delete */
    private static final String KEY_DELETE = "delete";
    
    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /** 采购单明细处理远程接口 */
    @EJB(beanName = "PurJOrderDetailsFacade")
    protected PurJOrderDetailsFacadeRemote orderDetailRemote;
    /** 采购订单与需求计划关联处理远程接口 */
    @EJB(beanName = "PurJPlanOrderFacade")
    protected PurJPlanOrderFacadeRemote planRelateRemote;
    /** 物料需求计划明细处理远程接口 */
    @EJB(beanName = "MrpJPlanRequirementDetailFacade")
    protected MrpJPlanRequirementDetailFacadeRemote planRemote;

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
    public PurJOrder save(PurJOrder entity) {
        LogUtil.log("saving PurJOrder instance", Level.INFO, null);
        try {
            if (entity.getId() != null) {
                throw new RuntimeException("订单ID不为空");
            }
            
            // 流水号自动采番
            entity.setId(bll.getMaxId("PUR_J_ORDER", "ID"));
            // 订单编号自动采番
            String sql = "select case when  max(PUR_NO) is  null then 'CG' else max(PUR_NO) end from PUR_J_ORDER";
            entity.setPurNo(getNextString(bll.getSingal(sql).toString(), 6));
            // 订单状态 :待审批状态
            entity.setPurStatus("0");
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
            
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
            return entity;
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 字符串采番
     * @param argStr 上次的字符串
     * @param argNumLen 字符串中的数字个数
     * @return 采番的字符串
     */
    public String getNextString(String argStr, int argNumLen) {
        String strResult = argStr;
        String strTempNum = "0";
        if (argStr.length() > argNumLen) {
            int index = argStr.length() - argNumLen;
            strResult = argStr.substring(0, index);
            
            strTempNum = argStr.substring(index + 1);
        }
        StringBuilder sb = new StringBuilder("0000000000");
        long lngNo = Long.parseLong(strTempNum);
        sb.append(++lngNo);
        
        strResult += sb.toString().substring(sb.length() - argNumLen);
        return strResult;
    }
    
    /**
     * 删除一条记录
     *
     * @param entityId 采购单流水号
     * @throws RuntimeException when the operation fails
     */
    public void delete(Long entityId) {
        LogUtil.log("deleting PurJOrder instance", Level.INFO, null);
        try {
            PurJOrder entity = findById(entityId);
            // 删除
            delete(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }
    
    /**
     * 删除一条记录
     *
     * @param entity 采购单
     * @throws RuntimeException when the operation fails
     */
    public void delete(PurJOrder entity) {
        LogUtil.log("deleting PurJOrder instance", Level.INFO, null);
        try {
            // 是否使用设为N
            entity.setIsUse("N");
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            
            // 更新
            update(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
    public PurJOrder update(PurJOrder entity) {
        LogUtil.log("updating PurJOrder instance", Level.INFO, null);
        try {
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 更新记录
            PurJOrder result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 通过采购单流水号得到采购单
     * 
     * @param id 采购单流水号
     * @return PurJOrder 采购单
     */
    public PurJOrder findById(Long id) {
        LogUtil.log("finding PurJOrder instance with id: " + id, Level.INFO,
                null);
        try {
            PurJOrder instance = entityManager.find(PurJOrder.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all PurJOrder entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the PurJOrder property to query
     * @param value
     *            the property value to match
     * @return List<PurJOrder> found by query
     */
    @SuppressWarnings("unchecked")
    public List<PurJOrder> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding PurJOrder instance with property: " + propertyName
                + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from PurJOrder model where model."
                    + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 通过采购单编号得到采购单
     * 
     * @param purNo 采购单编号
     * @return List<PurJOrder> 采购单
     */
    public List<PurJOrder> findByPurNo(Object purNo) {
        return findByProperty(PUR_NO, purNo);
    }

    /**
     * 查找所有的采购单
     * 
     * @return List<PurJOrder> 采购单
     */
    @SuppressWarnings("unchecked")
    public List<PurJOrder> findAll() {
        LogUtil.log("finding all PurJOrder instances", Level.INFO, null);
        try {
            final String queryString = "select model from PurJOrder model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    
    /**
     * 通过采购单流水号得到采购单
     * 
     * @param argOrderId 采购单流水号
     * @param enterpriseCode 企业编码
     * @return PurJOrderRegister 符合条件的采购单
     */
    @SuppressWarnings("unchecked")
    public PurJOrderRegister findByOrderId(String argOrderId, String enterpriseCode) {
        StringBuilder sbd = new StringBuilder();
        // SQL语句连接
        sbd.append("SELECT DISTINCT ");
        sbd.append("A.ID AS ORDER_ID, A.PUR_NO, A.PUR_STATUS, A.CONTRACT_NO, A.SUPPLIER, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS ORDER_MODIFY_DATE, ");
        sbd.append("A.BUYER, A.MEMO AS ORDER_MEMO, A.DUE_DATE AS ORDER_DUE_DATE, A.TAX_RATE AS ORDER_TAX_RATE, ");
        sbd.append("C.BUYER_NAME, D.CLIENT_CODE AS SUPPLIER_NO, D.CLIENT_NAME AS SUPPLY_NAME, A.CURRENCY_ID, B.EXCHAGE_RATE AS RATE ");
        sbd.append("FROM PUR_J_ORDER A,PUR_J_ORDER_DETAILS B, ");
        sbd.append("PUR_C_BUYER C, CON_J_CLIENT_INFO D ");
        sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' ");
        sbd.append("AND C.IS_USE = 'Y' AND D.IS_USE = 'Y' ");
        sbd.append("AND A.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND B.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND C.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND D.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND A.BUYER = C.BUYER AND A.SUPPLIER = D.CLIEND_ID ");
        sbd.append("AND A.PUR_NO = B.PUR_NO ");
        sbd.append("AND B.RCV_QTY < B.PUR_QTY ");
        sbd.append("AND A.ID = ");
        sbd.append(argOrderId);
        
        List<PurJOrderRegister> lstOrders = orderDetailRemote.queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class);
        PurJOrderRegister result = null;
        if (lstOrders != null && lstOrders.size() > 0) {
            result = lstOrders.get(0);
        }
        
        return result;
    }
    
    /**
     * 模糊查询采购单
     * 
     * @param argFuzzy 模糊查询字段
     * @param enterpriseCode 企业编码
     * @param argShowAll 是否显示所有订单
     * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 符合条件的采购单
     */
    @SuppressWarnings("unchecked")
    public PageObject findByFuzzy(String argFuzzy, String enterpriseCode, boolean argShowAll, final int... rowStartIdxAndCount) {
        StringBuilder sbd = new StringBuilder();
        // SQL语句连接
        sbd.append("SELECT DISTINCT ");
        sbd.append("A.ID AS ORDER_ID, A.PUR_NO, A.PUR_STATUS, A.CONTRACT_NO, A.SUPPLIER, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS ORDER_MODIFY_DATE, ");
        sbd.append("A.BUYER, A.MEMO AS ORDER_MEMO, A.DUE_DATE AS ORDER_DUE_DATE, A.TAX_RATE AS ORDER_TAX_RATE, ");
        sbd.append("C.BUYER_NAME, D.client_code AS SUPPLIER_NO, D.client_name AS SUPPLY_NAME, A.CURRENCY_ID, B.EXCHAGE_RATE AS RATE,A.INVOICE_NO AS INVOICE_NO ");
        sbd.append("FROM PUR_J_ORDER A,PUR_J_ORDER_DETAILS B, ");
        sbd.append("PUR_C_BUYER C, CON_J_CLIENTS_INFO D ");
        sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' ");
        sbd.append("AND C.IS_USE = 'Y'  ");
        sbd.append("AND A.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND B.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND C.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND D.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND A.BUYER = C.BUYER AND A.SUPPLIER = D.CLIEND_ID ");
        sbd.append("AND A.PUR_NO = B.PUR_NO ");
        if (!argShowAll) {
            sbd.append("AND B.RCV_QTY < B.PUR_QTY ");
            sbd.append("AND A.pur_status='0' "); //add by fyyang 091113
        }
        if (argFuzzy != null && argFuzzy.length() > 0) {
            sbd.append("AND (A.PUR_NO LIKE '%");
            sbd.append(argFuzzy);
            sbd.append("%' OR A.CONTRACT_NO LIKE '%");
            sbd.append(argFuzzy);
            sbd.append("%' OR D.CLIENT_CODE LIKE '%");
            sbd.append(argFuzzy);
            sbd.append("%' OR D.CLIENT_NAME LIKE '%");
            sbd.append(argFuzzy);
            sbd.append("%' OR C.BUYER LIKE '%");
            sbd.append(argFuzzy);
            sbd.append("%' OR C.BUYER_NAME LIKE '%");
            sbd.append(argFuzzy);
            sbd.append("%')");
        }
        
        // 模糊查询采购单
        List<PurJOrderRegister> lstOrders = orderDetailRemote.queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class, rowStartIdxAndCount);
        String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.ID) FROM ");
        // 查询符合条件的采购单的总数
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        
        PageObject result = new PageObject();
        // 符合条件的采购单
        result.setList(lstOrders);
        // 符合条件的采购单的总数
        result.setTotalCount(totalCount);
        
        return result;
    }

    /**
     * 增加采购单
     * @param argOrder 需要增加的采购单信息
     * @param argPlanedDetail 需要更新的采购单明细对象(需求单)信息
     * @param argUnplanedDetail 需要更新的采购单明细对象(行政单)信息
     * @param argPlans 需要更新的需求物资信息
     * @return PurJOrder 增加的采购单
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PurJOrder addOrderData(PurJOrder argOrder,
            Map<String, List<Map<String, Object>>> argPlanedDetail,
            Map<String, List<PurJOrderDetails>> argUnplanedDetail,
            Map<String, List<MrpJPlanRequirementDetail>> argPlans) {
        // 增加采购单
        PurJOrder orderResult = save(argOrder);
        // 初始化流水号
        Long lngDetail = new Long(0);
        Long lngPlanRelate = new Long(0);
        
        // 增加采购单明细(需求单)
        List<Map<String, Object>> lstPlanedAddOrderDetails = argPlanedDetail.get(KEY_ADD);
        Map<String, Object> mapPlanedOrderDetail = null;
        PurJOrderDetails detail = null;
        for (Iterator<Map<String, Object>> it = lstPlanedAddOrderDetails.iterator(); it.hasNext();) {
            mapPlanedOrderDetail = it.next();
            // 增加采购单明细(需求单)
            detail = (PurJOrderDetails) mapPlanedOrderDetail.get(KEY_DETAIL);
            // 设置采购单编号
            detail.setPurNo(orderResult.getPurNo());
            lngDetail = orderDetailRemote.save(detail, lngDetail);
            
            // 更新采购订单与需求计划关联表
            lngPlanRelate = savePlanRelates(orderResult.getPurNo(), lngDetail, lngPlanRelate,
                    (Map<String, List<PurJPlanOrder>>) mapPlanedOrderDetail.get(KEY_ORDER_DETAILS_RELATE));
        }
        
        
        // 增加采购单明细(行政单)
        List<PurJOrderDetails> lstUnPlanedAddOrderDetails = argUnplanedDetail.get(KEY_ADD);
        for (Iterator<PurJOrderDetails> it = lstUnPlanedAddOrderDetails.iterator(); it.hasNext();) {
            detail = it.next();
            // 设置采购单编号
            detail.setPurNo(orderResult.getPurNo());
            // 增加采购单明细(行政单)
            lngDetail = orderDetailRemote.save(detail, lngDetail);
        }
        
        
        // 增加需求物资
        List<MrpJPlanRequirementDetail> lstAddPlans = argPlans.get(KEY_ADD);
        for (Iterator<MrpJPlanRequirementDetail> it = lstAddPlans.iterator(); it.hasNext();) {
            // 增加的需求物资(实际是更新)
            planRemote.update(it.next());
        }
        // 修改需求物资
        List<MrpJPlanRequirementDetail> lstUpdatePlans = argPlans.get(KEY_UPDATE);
        for (Iterator<MrpJPlanRequirementDetail> it = lstUpdatePlans.iterator(); it.hasNext();) {
            // 修改的需求物资
            planRemote.update(it.next());
        }
        // 删除需求物资
        List<MrpJPlanRequirementDetail> lstDeletePlans = argPlans.get(KEY_DELETE);
        for (Iterator<MrpJPlanRequirementDetail> it = lstDeletePlans.iterator(); it.hasNext();) {
            // 删除的需求物资(实际是更新)
            planRemote.update(it.next());
        }
        
        return orderResult;
    }
    
    /**
     * 修改采购单
     * @param argOrder 需要修改的采购单信息
     * @param argPlanedDetail 需要更新的采购单明细对象(需求单)信息
     * @param argUnplanedDetail 需要更新的采购单明细对象(行政单)信息
     * @param argPlans 需要更新的需求物资信息
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateOrderData(PurJOrder argOrder,
            Map<String, List<Map<String, Object>>> argPlanedDetail,
            Map<String, List<PurJOrderDetails>> argUnplanedDetail,
            Map<String, List<MrpJPlanRequirementDetail>> argPlans) {
        if (argOrder.getId() != null) {
            // 修改采购单
            update(argOrder);
        }
        // 初始化流水号
        Long lngDetail = new Long(0);
        Long lngPlanRelate = new Long(0);
        
        // 增加采购单明细(需求单)
        Map<String, Object> mapPlanedOrderDetail = null;
        PurJOrderDetails detail = null;
        PurJOrderDetails planedOrderDetailResult = null;
        List<Map<String, Object>> lstPlanedAddOrderDetails = argPlanedDetail.get(KEY_ADD);
        for (Iterator<Map<String, Object>> it = lstPlanedAddOrderDetails.iterator(); it.hasNext();) {
            mapPlanedOrderDetail = it.next();
            // 增加采购单明细(需求单)
            detail = (PurJOrderDetails) mapPlanedOrderDetail.get(KEY_DETAIL);
            // 设置采购单编号
            detail.setPurNo(argOrder.getPurNo());
            lngDetail = orderDetailRemote.save(detail, lngDetail);
            
            // 更新采购订单与需求计划关联表
            lngPlanRelate = savePlanRelates(argOrder.getPurNo(), lngDetail, lngPlanRelate,
                    (Map<String, List<PurJPlanOrder>>) mapPlanedOrderDetail.get(KEY_ORDER_DETAILS_RELATE));
        }
        // 修改采购单明细(需求单)
        List<Map<String, Object>> lstPlanedUpdateOrderDetails = argPlanedDetail.get(KEY_UPDATE);
        for (Iterator<Map<String, Object>> it = lstPlanedUpdateOrderDetails.iterator(); it.hasNext();) {
            mapPlanedOrderDetail = it.next();
            // 修改采购单明细(行政单)
            planedOrderDetailResult = orderDetailRemote.update((PurJOrderDetails) mapPlanedOrderDetail.get(KEY_DETAIL));
            
            // 更新采购订单与需求计划关联表
            lngPlanRelate = savePlanRelates(argOrder.getPurNo(), planedOrderDetailResult.getPurOrderDetailsId(), lngPlanRelate,
                    (Map<String, List<PurJPlanOrder>>) mapPlanedOrderDetail.get(KEY_ORDER_DETAILS_RELATE));
        }
        // 删除采购单明细(需求单)
        List<Map<String, Object>> lstPlanedDeleteOrderDetails = argPlanedDetail.get(KEY_DELETE);
        for (Iterator<Map<String, Object>> it = lstPlanedDeleteOrderDetails.iterator(); it.hasNext();) {
            mapPlanedOrderDetail = it.next();
            detail = (PurJOrderDetails) mapPlanedOrderDetail.get(KEY_DETAIL);
            // 删除采购单明细(行政单)
            orderDetailRemote.delete(detail);
            
            // 更新采购订单与需求计划关联表
            lngPlanRelate = savePlanRelates(argOrder.getPurNo(), detail.getPurOrderDetailsId(), lngPlanRelate,
                    (Map<String, List<PurJPlanOrder>>) mapPlanedOrderDetail.get(KEY_ORDER_DETAILS_RELATE));
        }
        
        
        // 增加采购单明细(行政单)
        List<PurJOrderDetails> lstUnPlanedAddOrderDetails = argUnplanedDetail.get(KEY_ADD);
        for (Iterator<PurJOrderDetails> it = lstUnPlanedAddOrderDetails.iterator(); it.hasNext();) {
            detail = it.next();
            // 设置采购单编号
            detail.setPurNo(argOrder.getPurNo());
            // 增加采购单明细(行政单)
            lngDetail = orderDetailRemote.save(detail, lngDetail);
        }
        // 修改采购单明细(行政单)
        List<PurJOrderDetails> lstUnPlanedUpdateOrderDetails = argUnplanedDetail.get(KEY_UPDATE);
        for (Iterator<PurJOrderDetails> it = lstUnPlanedUpdateOrderDetails.iterator(); it.hasNext();) {
            // 修改采购单明细(行政单)
            orderDetailRemote.update(it.next());
        }
        // 删除采购单明细(行政单)
        List<PurJOrderDetails> lstUnPlanedDeleteOrderDetails = argUnplanedDetail.get(KEY_DELETE);
        for (Iterator<PurJOrderDetails> it = lstUnPlanedDeleteOrderDetails.iterator(); it.hasNext();) {
            // 删除采购单明细(行政单)
            orderDetailRemote.delete(it.next());
        }
        
        
        // 增加需求物资
        List<MrpJPlanRequirementDetail> lstAddPlans = argPlans.get(KEY_ADD);
        for (Iterator<MrpJPlanRequirementDetail> it = lstAddPlans.iterator(); it.hasNext();) {
            // 增加的需求物资(实际是更新)
            planRemote.update(it.next());
        }
        // 修改需求物资
        List<MrpJPlanRequirementDetail> lstUpdatePlans = argPlans.get(KEY_UPDATE);
        for (Iterator<MrpJPlanRequirementDetail> it = lstUpdatePlans.iterator(); it.hasNext();) {
            // 修改的需求物资
            planRemote.update(it.next());
        }
        // 删除需求物资
        List<MrpJPlanRequirementDetail> lstDeletePlans = argPlans.get(KEY_DELETE);
        for (Iterator<MrpJPlanRequirementDetail> it = lstDeletePlans.iterator(); it.hasNext();) {
            // 删除的需求物资(实际是更新)
            planRemote.update(it.next());
        }
    }

    /**
     * 删除采购单
     * @param argOrder 需要删除的采购单信息
     * @param argPlanedDetail 需要删除的采购单明细对象(需求单)信息
     * @param argUnplanedDetail 需要删除的采购单明细对象(行政单)信息
     * @param argPlans 需要更新的需求物资信息
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteOrderData(PurJOrder argOrder,
            Map<String, List<Map<String, Object>>> argPlanedDetail,
            Map<String, List<PurJOrderDetails>> argUnplanedDetail,
            Map<String, List<MrpJPlanRequirementDetail>> argPlans) {
        // 删除采购单
        delete(argOrder);
        // 初始化流水号
        Long lngPlanRelate = new Long(0);
        
        // 删除采购单明细(需求单)
        Map<String, Object> mapPlanedOrderDetail = null;
        PurJOrderDetails detail = null;
        List<Map<String, Object>> lstPlanedDeleteOrderDetails = argPlanedDetail.get(KEY_DELETE);
        for (Iterator<Map<String, Object>> it = lstPlanedDeleteOrderDetails.iterator(); it.hasNext();) {
            mapPlanedOrderDetail = it.next();
            detail = (PurJOrderDetails) mapPlanedOrderDetail.get(KEY_DETAIL);
            // 删除采购单明细(行政单)
            orderDetailRemote.delete(detail);
            
            // 更新采购订单与需求计划关联表
            lngPlanRelate = savePlanRelates(argOrder.getPurNo(), detail.getPurOrderDetailsId(), lngPlanRelate,
                    (Map<String, List<PurJPlanOrder>>) mapPlanedOrderDetail.get(KEY_ORDER_DETAILS_RELATE));
        }
        
        
        // 删除采购单明细(行政单)
        List<PurJOrderDetails> lstUnPlanedDeleteOrderDetails = argUnplanedDetail.get(KEY_DELETE);
        for (Iterator<PurJOrderDetails> it = lstUnPlanedDeleteOrderDetails.iterator(); it.hasNext();) {
            // 删除采购单明细(行政单)
            orderDetailRemote.delete(it.next());
        }
        
        
        // 增加需求物资
        List<MrpJPlanRequirementDetail> lstAddPlans = argPlans.get(KEY_ADD);
        for (Iterator<MrpJPlanRequirementDetail> it = lstAddPlans.iterator(); it.hasNext();) {
            // 增加的需求物资(实际是更新)
            planRemote.update(it.next());
        }
        // 修改需求物资
        List<MrpJPlanRequirementDetail> lstUpdatePlans = argPlans.get(KEY_UPDATE);
        for (Iterator<MrpJPlanRequirementDetail> it = lstUpdatePlans.iterator(); it.hasNext();) {
            // 修改的需求物资
            planRemote.update(it.next());
        }
    }
    
    /**
     * 更新采购订单与需求计划关联表
     * @param argPurNo 采购订单表.采购订单号
     * @param argOrderDetailsId 采购订单明细表.流水号
     * @param argPlanRelateId 采购订单与需求计划关联.流水号
     * @param argPlanRelates 需要更新的采购订单与需求计划关联信息
     */
    private Long savePlanRelates(String argPurNo, Long argOrderDetailsId, Long argPlanRelateId,
            Map<String, List<PurJPlanOrder>> argPlanRelates) {
        if (argPlanRelates == null || argPlanRelates.size() < 1) {
            return argPlanRelateId;
        }
        
        PurJPlanOrder planRelate = null;
        // 增加采购订单与需求计划关联
        List<PurJPlanOrder> lstAddPlanRelates = argPlanRelates.get(KEY_ADD);
        for (Iterator<PurJPlanOrder> it = lstAddPlanRelates.iterator(); it.hasNext();) {
            planRelate = it.next();
            // 采购订单号
            planRelate.setPurNo(argPurNo);
            // 采购订单项次号
            planRelate.setPurOrderDetailsId(argOrderDetailsId);
            // 增加的采购订单与需求计划关联
            argPlanRelateId = planRelateRemote.save(planRelate, argPlanRelateId);
        }
        // 修改采购订单与需求计划关联
        List<PurJPlanOrder> lstUpdatePlanRelates = argPlanRelates.get(KEY_UPDATE);
        for (Iterator<PurJPlanOrder> it = lstUpdatePlanRelates.iterator(); it.hasNext();) {
            planRelate = it.next();
            // 采购订单号
            planRelate.setPurNo(argPurNo);
            // 采购订单项次号
            planRelate.setPurOrderDetailsId(argOrderDetailsId);
            // 修改的采购订单与需求计划关联
            planRelateRemote.update(planRelate);
        }
        // 删除采购订单与需求计划关联
        List<PurJPlanOrder> lstDeletePlanRelates = argPlanRelates.get(KEY_DELETE);
        for (Iterator<PurJPlanOrder> it = lstDeletePlanRelates.iterator(); it.hasNext();) {
            planRelate = it.next();
            // 采购订单号
            planRelate.setPurNo(argPurNo);
            // 采购订单项次号
            planRelate.setPurOrderDetailsId(argOrderDetailsId);
            // 删除的采购订单与需求计划关联
            planRelateRemote.delete(planRelate);
        }
        
        return argPlanRelateId;
    }

	public PageObject findByQuery(String strQueryTimeFrom,
			String strQueryTimeTo, String buyer, String purNo,
			String enterpriseCode, int... rowStartIdxAndCount) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date queryTimeFrom = new Date();
		Date queryTimeTo = new Date();
		try {
			if(strQueryTimeFrom == null)
			{
				strQueryTimeFrom = "2009-02-11";
			}
			queryTimeFrom = dateFormat.parse(strQueryTimeFrom);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(strQueryTimeTo == null)
			{
				strQueryTimeTo = "2009-06-05";
			}
			queryTimeTo = dateFormat.parse(strQueryTimeTo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sbd = new StringBuilder();
        // SQL语句连接
        sbd.append("SELECT DISTINCT ");
        sbd.append("A.ID AS ORDER_ID, A.PUR_NO, A.PUR_STATUS, A.CONTRACT_NO, A.SUPPLIER, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') AS ORDER_MODIFY_DATE, ");
        sbd.append("A.BUYER, A.MEMO AS ORDER_MEMO, A.DUE_DATE AS ORDER_DUE_DATE, A.TAX_RATE AS ORDER_TAX_RATE, ");
        sbd.append("C.BUYER_NAME, getclientname(A.SUPPLIER)  AS SUPPLY_NAME, A.CURRENCY_ID, B.EXCHAGE_RATE AS RATE, ");
        sbd.append("A.LAST_MODIFIED_BY, A.PROJECT, E.BUYER_NAME AS LAST_MODIFIED_BY_NAME ");
        sbd.append("FROM PUR_J_ORDER A,PUR_J_ORDER_DETAILS B, ");
        sbd.append("PUR_C_BUYER C,  PUR_C_BUYER E ");
        sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' ");
        sbd.append("AND C.IS_USE = 'Y'  ");
        sbd.append("AND A.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND B.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND C.ENTERPRISE_CODE = '");
        sbd.append(enterpriseCode);
        sbd.append("' AND A.BUYER = C.BUYER   ");
        sbd.append("AND A.LAST_MODIFIED_BY = E.BUYER AND A.PUR_NO = B.PUR_NO ");
        sbd.append("  and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" + strQueryTimeFrom +"'\n");
        sbd.append("  and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" + strQueryTimeTo + "'\n");
//        sbd.append(" and " + strQueryTimeFrom +"<=to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') and " + strQueryTimeTo + ">= to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') ");
        if(buyer != null &&!buyer.equals(""))
        {
        	sbd.append("and A.BUYER = '");
        	sbd.append(buyer);
        	sbd.append("' ");
        }
        if(purNo !=null && !purNo.equals(""))
        {
        	sbd.append("and A.PUR_NO like '%");
        	sbd.append(purNo);
        	sbd.append("%' ");
        }
     
       
        // 条件查询采购单
        List<PurJOrderRegister> lstOrders = orderDetailRemote.queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class, rowStartIdxAndCount);
        String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.ID) FROM ");
        // 查询符合条件的采购单的总数
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        
        PageObject result = new PageObject();
        // 符合条件的采购单
        result.setList(lstOrders);
        // 符合条件的采购单的总数
        result.setTotalCount(totalCount);
        
        return result;
	}
	
	public PageObject getOrdersMaterial(String strQueryTimeFrom, String strQueryTimeTo, String buyer, String materialNo, String materialName,
			String specNo,String enterpriseCode, String supplyName,final int... rowStartIdxAndCount) {
		if(materialNo != null && !"".equals(materialNo)) {
			materialNo = "%"+materialNo+"%";
		} else {
			materialNo = "%";
		}
		
		if(materialName != null && !"".equals(materialName)) {
			materialName = "%"+materialName+"%";
		} else {
			materialName = "%";
		}
		
		if(specNo != null && !"".equals(specNo)) {
			specNo = "%"+specNo+"%";
		} else {
			specNo = "%";
		}
		if(buyer != null && !"".equals(buyer)) {
			buyer = "%"+buyer+"%";
		} else {
			buyer = "%";
		}
		
		String sql = "select a.pur_no,\n" +
				"       c.material_no,\n" + 
				"       c.material_name,\n" + 
				"       c.spec_no,\n" + 
				"       b.pur_qty,\n" + 
				"       b.unit_price,\n" + 
				"       a.last_modified_date,\n" + 
				"       getclientname(a.supplier),\n" + 
				"       getworkername(a.buyer)\n" + 
				",d.CURRENCY_NAME \n"+ //add by fyyang 091119
				"  from pur_j_order a, pur_j_order_details b, inv_c_material c,SYS_C_CURRENCY d\n" + 
				" where a.pur_no = b.pur_no\n" + 
				"   and b.material_id = c.material_id\n" + 
				" AND a.CURRENCY_ID = d.CURRENCY_ID \n"+ //add by fyyang 091119
				"  and d.is_use='Y'   and d.enterprise_code = '" + enterpriseCode + "' \n" +//add by fyyang 091119
				"   and a.is_use = 'Y'\n" + 
				"   and b.is_use = 'Y'\n" + 
				"   and c.is_use = 'Y'\n" + 
				"   and a.enterprise_code = '" + enterpriseCode + "'\n" +  
				"   and b.enterprise_code = '" + enterpriseCode + "'\n" +
				"   and c.enterprise_code = '" + enterpriseCode + "'\n" +
				"   and c.material_no like '" + materialNo + "'\n" +
				"   and c.material_name like '" + materialName + "'\n" +
				"   and getworkername(a.buyer) like '" + buyer + "'\n" +
				"   and (c.spec_no like '" + specNo + "' or c.spec_no is null)\n" +
				"   and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" + strQueryTimeFrom +"'\n" +
				"   and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" + strQueryTimeTo + "'\n";
		if(supplyName!=null&&!supplyName.equals(""))
		{
			//add by fyyang 20100114
			sql+="\n  and getclientname(a.supplier) like '%"+supplyName.trim()+"%'  ";
		}	
		List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
		Iterator it = list.iterator();
		PageObject obj = new PageObject();
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList();
		while(it.hasNext())
		{
			PurchaseWarehouseDetailInfo info = new PurchaseWarehouseDetailInfo();
			Object [] data=(Object [])it.next();
			if(data[0]!=null) {
				info.setPurNo(data[0].toString());
				if(info.getPurNo() != null && !"".equals(info.getPurNo())) {
					sql = "select t.requirement_detail_id\n" +
						"  from pur_j_plan_order t\n" + 
						" where t.pur_no = '" + info.getPurNo() + "'\n" +
						"   and t.enterprise_code = 'hfdc'\n" + 
						"   and t.is_use = 'Y'";
					List mrpIdlist = bll.queryByNativeSQL(sql);
					String requirementDetailId = "";
					Iterator mrpIdit = mrpIdlist.iterator();
					while(mrpIdit.hasNext()) {
						requirementDetailId = mrpIdit.next().toString();
//						Object [] mrpIddata=(Object [])mrpIdit.next();
//						if(mrpIddata[0] != null) {
//							requirementDetailId += mrpIddata[0].toString();
//						}	
					}
					info.setRequirementDetailId(requirementDetailId);
				}
			}	
			if(data[1]!=null) 
				info.setMaterialNo(data[1].toString());
			if(data[2]!=null)
				info.setMaterialName(data[2].toString());
			if(data[3]!=null) 
				info.setSpecNo(data[3].toString());
			if(data[4]!=null)
				info.setPurQty(Double.parseDouble((data[4].toString())));
			if(data[5]!=null) 
				info.setUnitPrice(Double.parseDouble((data[5].toString())));
			if(data[6]!=null)
				info.setEntryDate(data[6].toString());
			if(data[7]!=null)
				info.setSupplyName(data[7].toString());
			if(data[8]!=null)
				info.setBuyerName(data[8].toString());
			//add by fyyang 091119
			if(data[9]!=null)
			info.setCurrencyType(data[9].toString());
			arraylist.add(info);
		}
		String sqlCount = "select count(a.pur_no)\n" +
						"  from pur_j_order a, pur_j_order_details b, inv_c_material c\n" + 
						" where a.pur_no = b.pur_no\n" + 
						"   and b.material_id = c.material_id\n" + 
						"   and a.is_use = 'Y'\n" + 
						"   and b.is_use = 'Y'\n" + 
						"   and c.is_use = 'Y'\n" + 
						"   and a.enterprise_code = '" + enterpriseCode + "'\n" +  
						"   and b.enterprise_code = '" + enterpriseCode + "'\n" +
						"   and c.enterprise_code = '" + enterpriseCode + "'\n" +
						"   and c.material_no like '" + materialNo + "'\n" +
						"   and c.material_name like '" + materialName + "'\n" +
						"   and getworkername(a.buyer) like '" + buyer + "'\n" +
						"   and (c.spec_no like '" + specNo + "' or c.spec_no is null)\n" +
						"   and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" + strQueryTimeFrom +"'\n" +
						"   and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" + strQueryTimeTo + "'\n";
		if(supplyName!=null&&!supplyName.equals(""))
		{
			//add by fyyang 20100114
			sqlCount+="\n  and getclientname(a.supplier) like '%"+supplyName.trim()+"%'  ";
		}	
		obj.setList(arraylist);
		obj.setTotalCount(Long.valueOf(bll.getSingal(sqlCount).toString()));
		return obj;
	}
    
}