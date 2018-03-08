package power.ejb.resource;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.InquirePriceBean;

/**
 * 采购单明细
 * 
 * @see power.ejb.logistics.PurJOrderDetails
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurJOrderDetailsFacade implements PurJOrderDetailsFacadeRemote {
	// property constants
	public static final String PUR_NO = "purNo";
	public static final String PUR_LINE = "purLine";
	public static final String MATERIAL_ID = "materialId";
	public static final String FACTORY = "factory";
	public static final String RECEIPT_WHS = "receiptWhs";
	public static final String RECEIPT_LOCATION = "receiptLocation";
	public static final String PUR_QTY = "purQty";
	public static final String RCV_QTY = "rcvQty";
	public static final String INS_QTY = "insQty";
	public static final String UNIT_PRICE = "unitPrice";
	public static final String CURRENCY_TYPE = "currencyType";
	public static final String PUR_UM = "purUm";
	public static final String TAX_RATE = "taxRate";
	public static final String EXCHAGE_RATE = "exchageRate";
	public static final String QA_CONTROL_FLAG = "qaControlFlag";
	public static final String MEMO = "memo";
	public static final String OPERATE_BY = "operateBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	@EJB(beanName = "InvCMaterialFacade")
	protected InvCMaterialFacadeRemote materialRemote;

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public PurJOrderDetails save(PurJOrderDetails entity) {
		LogUtil.log("saving PurJOrderDetails instance", Level.INFO, null);
		try {
			// 流水号自动采番
			entity.setPurOrderDetailsId(bll.getMaxId("PUR_J_ORDER_DETAILS", "PUR_ORDER_DETAILS_ID"));
			// 订单项次号: 由于采用了订单明细ID号，故该字段已经不再使用。全部默认为0
			entity.setPurLine(new Long(0));
			// 已收数量: 默认为0，在采购入库时，修改此值
			entity.setRcvQty(new Double(0));
			// 暂收数量
			entity.setInsQty(new Double(0));
			
			if (entity.getTaxRate() == null) {
				// 税率: 默认为0
				entity.setTaxRate(new Double(0));
			}
			if (entity.getExchageRate() == null
					|| entity.getExchageRate().equals(new Double(0))) {
				// 汇率: 默认为1
				entity.setExchageRate(new Double(1));
			}
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
	 * 增加一条记录(多次增加时用)
	 *
	 * @param entity 要增加的记录
	 * @param argId 上次增加记录的流水号
	 * @return Long 增加后记录的流水号
	 */
	public Long save(PurJOrderDetails entity, Long argId) {
		LogUtil.log("saving PurJOrderDetails instance", Level.INFO, null);
		try {
			Long lngId = argId;
			if (lngId == null || lngId.longValue() < 1L) {
				// 流水号自动采番
				lngId = bll.getMaxId("PUR_J_ORDER_DETAILS", "PUR_ORDER_DETAILS_ID");
			} else {
				lngId = Long.valueOf(argId.longValue() + 1L);
			}
			
			// 流水号
			entity.setPurOrderDetailsId(lngId);
			// 订单项次号: 由于采用了订单明细ID号，故该字段已经不再使用。全部默认为0
			entity.setPurLine(new Long(0));
			// 已收数量: 默认为0，在采购入库时，修改此值
			entity.setRcvQty(new Double(0));
			// 暂收数量
			entity.setInsQty(new Double(0));
			
			if (entity.getTaxRate() == null) {
				// 税率: 默认为0
				entity.setTaxRate(new Double(0));
			}
			if (entity.getExchageRate() == null
					|| entity.getExchageRate().equals(new Double(0))) {				
				// 汇率: 默认为1
				entity.setExchageRate(new Double(1));
			}
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
            
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
			return lngId;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
    /**
     * 删除一条记录
     *
     * @param argPurOrderDetailsId 采购单明细流水号
     * @throws RuntimeException when the operation fails
     */
	public void delete(Long argPurOrderDetailsId) {
		LogUtil.log("deleting PurJOrderDetails instance", Level.INFO, null);
		try {
			PurJOrderDetails entity = findById(argPurOrderDetailsId);

			// 更新
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
	 * @param entity 采购单明细
	 * @throws RuntimeException when the operation fails
	 */
	public void delete(PurJOrderDetails entity) {
		LogUtil.log("deleting PurJOrderDetails instance", Level.INFO, null);
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
	public PurJOrderDetails update(PurJOrderDetails entity) {
		LogUtil.log("updating PurJOrderDetails instance", Level.INFO, null);
		try {
			// 设定修改时间
			entity.setLastModifiedDate(new java.util.Date());
			PurJOrderDetails result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过采购单明细流水号得到采购单明细
	 * 
	 * @param id 采购单明细流水号
     * @return PurJOrderDetails 采购单明细
	 */
	public PurJOrderDetails findById(Long id) {
		LogUtil.log("finding PurJOrderDetails instance with id: " + id,
				Level.INFO, null);
		try {
			PurJOrderDetails instance = entityManager.find(
					PurJOrderDetails.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurJOrderDetails entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurJOrderDetails property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurJOrderDetails> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PurJOrderDetails> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PurJOrderDetails instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PurJOrderDetails model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				} 
				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过采购单编号得到采购单明细
	 *
	 * @param argEnterpriseCd 企业编码
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByPurNo(String argEnterpriseCd, String purNo, String argSupplier, final int... rowStartIdxAndCount) {
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
		sbd.append("SELECT DISTINCT ");
		sbd.append("A.PUR_ORDER_DETAILS_ID AS ORDER_DETAILS_ID, C.PUR_ORDER_DETAILS_ID, A.PUR_QTY, A.RCV_QTY,A.CURRENCY_ID, A.EXCHAGE_RATE AS RATE, ");
		sbd.append("A.DUE_DATE AS ORDER_DETAILS_DUE_DATE, A.TAX_RATE AS ORDER_DETAILS_TAX_RATE, A.MEMO AS ORDER_DETAILS_MEMO, A.UNIT_PRICE AS QUOTED_PRICE, ");
		sbd.append("C.PUR_ORDER_DETAILS_ID, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS ORDER_DETAILS_MODIFY_DATE, ");
		sbd.append("B.MATERIAL_ID, B.MATERIAL_NO, B.MATERIAL_NAME, B.SPEC_NO, B.PARAMETER, A.QA_CONTROL_FLAG, ");
		sbd.append("B.PUR_UM_ID, B.MAX_STOCK ");
		// add by liuyi 091127 增加物料单位  
		sbd.append("  ,getunitname(b.pur_um_id) as stock_Um_Name  ");
		sbd.append("FROM PUR_J_ORDER_DETAILS A, INV_C_MATERIAL B, PUR_J_PLAN_ORDER C ");
		sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' AND C.IS_USE(+) = 'Y' ");
		sbd.append("AND A.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND B.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND C.ENTERPRISE_CODE(+) = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND A.PUR_ORDER_DETAILS_ID = C.PUR_ORDER_DETAILS_ID(+) AND A.PUR_NO = '");
		sbd.append(purNo);
		sbd.append("' AND A.MATERIAL_ID = B.MATERIAL_ID ");
		
		List<PurJOrderRegister> lst = queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class, rowStartIdxAndCount);
		String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.PUR_ORDER_DETAILS_ID) FROM ");
		// 查询符合条件的采购单明细的总数
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        
        // add by liuyi 20100406 
        for(PurJOrderRegister register : lst){
        	String reSql = 
        		"select distinct b.memo, getdeptname(c.mr_dept)\n" +
        		"  from pur_j_plan_order              a,\n" + 
        		"       mrp_j_plan_requirement_detail b,\n" + 
        		"       mrp_j_plan_requirement_head   c\n" + 
        		" where a.is_use = 'Y'\n" + 
        		"   and a.enterprise_code = 'hfdc'\n" + 
        		"   and a.pur_order_details_id = '"+register.getPurOrderDetailsId()+"'\n" + 
        		"   and a.pur_no = '"+purNo+"'\n" + 
        		"   and a.requirement_detail_id = b.requirement_detail_id\n" + 
        		"   and b.requirement_head_id = c.requirement_head_id";
        	List reList = bll.queryByNativeSQL(reSql);
        	if(reList != null && reList.size() > 0)
        	{
        		Object[] redata = (Object[])reList.get(0);
        		if(redata[0] != null)
        			register.setSbMemo(redata[0].toString());
        		if(redata[1] != null)
        			register.setSbDeptName(redata[1].toString());
        	}

        }
		PageObject result = new PageObject();
		// 符合条件的采购单明细
		result.setList(lst);
		// 符合条件的采购单明细的总数
		result.setTotalCount(totalCount);
		
		return result;
	}
	/*
	 * 通过采购单编号得到采购单明细 不要求供应商
	 * @param argEnterpriseCd 企业编码
	 * @param purNo 采购单编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	public PageObject findDetailByPurNo(String argEnterpriseCd, String purNo,
			int... rowStartIdxAndCount) {
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
		sbd.append("SELECT DISTINCT ");
		sbd.append("A.PUR_ORDER_DETAILS_ID AS ORDER_DETAILS_ID, C.PUR_NO ,C.PUR_STATUS," +
				" B.MATERIAL_ID ,B.MATERIAL_NO ,B.MATERIAL_NAME,B.SPEC_NO,B.PARAMETER, " +
				" A.PUR_QTY, A.RCV_QTY,A.INS_QTY,A.UNIT_PRICE,D.CURRENCY_NAME, A.DUE_DATE AS ORDER_DETAILS_DUE_DATE," +
				" A.MEMO AS ORDER_DETAILS_MEMO,E.REQUIREMENT_DETAIL_ID,E.PUR_ORDER_DETAILS_ID " );
		
		sbd.append("FROM PUR_J_ORDER_DETAILS A, INV_C_MATERIAL B, PUR_J_ORDER C,SYS_C_CURRENCY D,PUR_J_PLAN_ORDER E ");
		sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' AND C.IS_USE(+) = 'Y' ");
		sbd.append("AND A.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND B.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND C.ENTERPRISE_CODE(+) = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND A.PUR_NO = C.PUR_NO AND A.PUR_NO = '");
		sbd.append(purNo);
		sbd.append("' AND A.MATERIAL_ID = B.MATERIAL_ID ");
		sbd.append(" AND A.CURRENCY_ID = D.CURRENCY_ID ");
		sbd.append(" AND A.PUR_ORDER_DETAILS_ID = E.PUR_ORDER_DETAILS_ID(+) ");
		
		List<PurJOrderRegister> lst = queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class, rowStartIdxAndCount);
		String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.PUR_ORDER_DETAILS_ID) FROM ");
		// 查询符合条件的采购单明细的总数
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        
		PageObject result = new PageObject();
		// 符合条件的采购单明细
		result.setList(lst);
		// 符合条件的采购单明细的总数
		result.setTotalCount(totalCount);
		
		return result;
	}
	/**
	 * 后期维护比较用的数据检索
	 *
	 * @param argEnterpriseCd 企业编码
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @return List<PurJOrderRegister> 采购单明细
	 */
	@SuppressWarnings("unchecked")
	public List<PurJOrderRegister> getMeasureByPurNo(String argEnterpriseCd, String purNo, String argSupplier) {
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
		sbd.append("SELECT DISTINCT ");
		sbd.append("A.PUR_ORDER_DETAILS_ID as ORDER_DETAILS_ID, A.PUR_QTY, A.RCV_QTY, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS ORDER_DETAILS_MODIFY_DATE, ");
		sbd.append("A.DUE_DATE as ORDER_DETAILS_DUE_DATE, A.TAX_RATE as ORDER_DETAILS_TAX_RATE, A.MEMO as ORDER_DETAILS_MEMO, ");
		sbd.append("B.MATERIAL_ID, B.MATERIAL_NO, B.MATERIAL_NAME, B.SPEC_NO, B.PARAMETER, B.QA_CONTROL_FLAG, ");
		sbd.append("B.PUR_UM_ID, B.MAX_STOCK, C.PLAN_ORDER_ID, C.PUR_ORDER_DETAILS_ID, C.REQUIREMENT_DETAIL_ID, C.MR_QTY as NEED_QTY, ");
		sbd.append("to_char(C.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS PLAN_RELATE_MODIFY_DATE, ");
		sbd.append("E.APPROVED_QTY, E.PUR_QTY AS PLAN_PUR_QTY, to_char(E.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS PLAN_MODIFY_DATE ");
		sbd.append("FROM PUR_J_ORDER_DETAILS A, INV_C_MATERIAL B, PUR_J_PLAN_ORDER C, MRP_J_PLAN_REQUIREMENT_DETAIL E ");
		sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' AND C.IS_USE = 'Y' AND E.IS_USE = 'Y' ");
		sbd.append("AND A.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND B.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND C.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND E.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND E.REQUIREMENT_DETAIL_ID = C.REQUIREMENT_DETAIL_ID AND A.PUR_ORDER_DETAILS_ID = C.PUR_ORDER_DETAILS_ID ");
		sbd.append("AND A.MATERIAL_ID = B.MATERIAL_ID AND A.PUR_NO = '");
		sbd.append(purNo);
		sbd.append("'");
		
		List<PurJOrderRegister> result = queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class);
		
		return result;
	}

	/**
	 * 通过币别和供应商编号取得汇率<br/>
	 * 2009/02/20修改： 供应商编号废止
	 * @param argEnterpriseCd 企业编码
	 * @param argCurrencyId 币别
	 * @param argSupplier 供应商编号
	 * @return
	 */
	public String getExchangeRate(String argEnterpriseCd, String argCurrencyId, String argSupplier) {
		if (isNullOrEmpty(argCurrencyId)) {
			return "";
		}
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
		sbd.append("SELECT COUNT(B.PARM_NO) ");
		sbd.append("FROM SYS_C_PARAMETERS B ");
		sbd.append("WHERE B.PARM_VALUE = '");
		sbd.append(argCurrencyId);
		sbd.append("' AND B.PARM_NO = 'ORICUR'");
		
		Object result = bll.getSingal(sbd.toString());
		if (result != null && !"0".equals(result.toString())) {
			// 画面选择币别与系统参数表的本币相同时，设为1
			return "1";
		}
		
		sbd = new StringBuilder();
		// 取得系统参数表的本币作为兑换币别，采购订单的币别作为基准币别，取得最新汇率
		sbd.append("SELECT A.RATE ");
		sbd.append("FROM SYS_C_EXCHANGE_RATE A, ");
		sbd.append("SYS_C_PARAMETERS B ");
		sbd.append("WHERE A.IS_USE = 'Y' ");
		sbd.append("AND A.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND B.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND A.DST_CURRENCY_ID = B.PARM_VALUE ");
		sbd.append("AND A.EFFECTIVE_DATE <= SYSDATE ");
		sbd.append("AND A.DISCONTINUE_DATE >= SYSDATE ");
		sbd.append("AND A.ORI_CURRENCY_ID = '");
		sbd.append(argCurrencyId);
		sbd.append("' AND B.PARM_NO = 'ORICUR'");
		
	    result = bll.getSingal(sbd.toString());
		if (result == null) {
			// 没有的话，返回空
			return "";
		}
		return result.toString();
	}

	/**
	 * 通过物料ID和供应商编号取得报价信息
	 * @param argEnterpriseCd 企业编码
	 * @param argMaterialId 物料ID
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 报价信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getUnitPrice(String argEnterpriseCd, String argMaterialId, String argSupplier
			, final int... rowStartIdxAndCount) {
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
//		sbd.append("SELECT * FROM PUR_J_QUOTATION A ");
//		sbd.append("WHERE A.IS_USE = 'Y' AND A.ENTERPRISE_CODE = '");
//		sbd.append(argEnterpriseCd);
//		if (isNullOrEmpty(argMaterialId)) {
//			sbd.append("' AND A.MATERIAL_ID IS NULL");
//		} else {
//			sbd.append("' AND A.MATERIAL_ID = ");
//			sbd.append(argMaterialId);
//		}
//		sbd.append(" AND A.EFFECTIVE_DATE <= SYSDATE AND A.DISCONTINUE_DATE >= SYSDATE ");
//		if (isNullOrEmpty(argMaterialId)) {
//			sbd.append("AND A.SUPPLIER IS NULL");
//		} else {
//			sbd.append("AND A.SUPPLIER = '");
//			sbd.append(argSupplier);
//			sbd.append("'");
//		}
		//modify by ywliu 2009/5/26
		sbd.append("select a.inquire_qty, a.unit_price, d.unit_name ,d.unit_id  from mrp_j_plan_inquire_detail a, mrp_j_plan_gather b, inv_c_material c, BP_C_MEASURE_UNIT d \n");
		sbd.append("where a.is_select_supplier = 'Y' and a.gather_id = b.gather_id and c.stock_um_id = d.unit_id and b.material_id = c.material_id ");
		if (isNullOrEmpty(argMaterialId)) {
			sbd.append(" AND b.MATERIAL_ID IS NULL");
		} else {
			sbd.append(" AND b.MATERIAL_ID = ");
			sbd.append(argMaterialId);
		}
		sbd.append(" AND A.effect_start_date <= SYSDATE AND A.effect_end_date >= SYSDATE ");
		if (isNullOrEmpty(argMaterialId)) {
			sbd.append("AND A.inquire_supplier IS NULL");
		} else {
			sbd.append("AND A.inquire_supplier = '");
			sbd.append(argSupplier);
			sbd.append("'");
		}

		List lstResult = bll.queryByNativeSQL(sbd.toString(), rowStartIdxAndCount);
		Iterator it=lstResult.iterator();
		List<InquirePriceBean> inquirePriceList = new ArrayList<InquirePriceBean>();
		while(it.hasNext()){
			Object[] data=(Object[])it.next();
			InquirePriceBean bean = new InquirePriceBean();
			if(data[0]!=null){
				bean.setInquireQty(data[0].toString());
			}
			if(data[1]!=null){
				bean.setUnitPrice(data[1].toString());
			}
			if(data[2]!=null){
				bean.setUnitName(data[2].toString());
			}
			if(data[3]!=null){
				bean.setUnitId(data[3].toString());
			}
			inquirePriceList.add(bean);
		}	
//		String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.QUOTATION_ID) FROM ");
		String sqlCount = "select count(*) from (" +sbd.toString()+")";
		// 查询符合条件的采购单的总数
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		PageObject result = new PageObject();
		// 符合条件的采购单
		result.setList(inquirePriceList);
		// 符合条件的采购单的总数
		result.setTotalCount(totalCount);
		
		return result;
	}

	/**
	 * 通过采购员ID和供应商编号取得需求计划明细数据
	 * modify by fyyang 090814  查找出设置的供应商及单价
	 * @param argEnterpriseCd 企业编码
	 * @param argBuyer 采购员ID
	 * @param argSupplier 供应商编号
	 * @param argIsSelfPlan 是否检索当前采购员的需求计划明细数据
	 * @return 符合条件的需求计划明细数据
	 */
	@SuppressWarnings("unchecked")
	public List<PurJOrderRegister> getPlans(String argEnterpriseCd, String argBuyer,
			String argSupplier, boolean argIsSelfPlan) {
		StringBuilder sbd = new StringBuilder();
		List<PurJOrderRegister> result = new ArrayList<PurJOrderRegister>();
//		// SQL语句连接  根据采购员查询物料编码方法注释掉 ，String argBuyer 这个参数已经不用 modify by ywliu 2009/6/30
//		sbd.append("SELECT A.IS_MATERIAL_CLASS, A.MATERIAL_OR_CLASS_NO ");
//		sbd.append("FROM PUR_C_BUYER A WHERE A.IS_USE = 'Y'");
//		if (argIsSelfPlan) {
//			sbd.append(" AND A.BUYER = '");
//			sbd.append(argBuyer);
//			sbd.append("'");
//		} else {
//			sbd.append(" AND A.BUYER <> '");
//			sbd.append(argBuyer);
//			sbd.append("'");
//		}
//		List lstResult = bll.queryByNativeSQL(sbd.toString());
//		
//		// 物料编码List
//		Set<String> setMaterials = new HashSet<String>();
//		
//		boolean isMaterial = false;
//		String strMaterials = "";
//		if (lstResult != null && lstResult.size() > 0) {
//			for (int intCnt = 0; intCnt < lstResult.size(); intCnt++) {
//				Object[] datas = (Object[]) lstResult.get(intCnt);
//				if (datas[0] != null) {
//					isMaterial = datas[0].toString().equals("Y");
//				}
//				if (!isMaterial) {
//					if (datas[1] != null) {
//						// 增加物料编码
//						setMaterials.add(datas[1].toString());
//					}
//				} else {
//					if (datas[1] != null) {
//						// 查找该当物料分类节点下所有的物料编码
//						List lstMaterial = materialRemote.findAllChildrenNode(datas[1].toString(), argEnterpriseCd);
//						
//						if (lstMaterial != null) {
//							// 增加物料分类
//							for (int intCnt1 = 0; intCnt1 < lstMaterial.size(); intCnt1++) {
//								setMaterials.add(((InvCMaterial) lstMaterial.get(intCnt1)).getMaterialNo());
//							}
//						}
//					}
//				}
//			}
//			
//			StringBuilder sbdTempMaterials = new StringBuilder();
//			for (Iterator<String> it = setMaterials.iterator(); it.hasNext();) {
//				sbdTempMaterials.append("'");
//				sbdTempMaterials.append(it.next());
//				sbdTempMaterials.append("'");
//				sbdTempMaterials.append(",");
//			}
//			if (sbdTempMaterials.length() > 0) {
//				sbdTempMaterials.deleteCharAt(sbdTempMaterials.lastIndexOf(","));
//			}
//			strMaterials = sbdTempMaterials.toString();
//		}
//		
//		sbd = new StringBuilder();
		//-------modify by fyyang 091106---start------ 
		String ids = "";
		if(!argBuyer.equals("999999"))
		{
		// 按照采购员过滤需求计划明细 add by ywliu 20091104
		String sql = "select t.requirement_detail_ids from mrp_j_plan_gather t where t.buyer = '"+argBuyer
				+"' and t.ENTERPRISE_CODE = '" + argEnterpriseCd +"'"
				+"  and t.IS_USE = 'Y'";
		List idsList = bll.queryByNativeSQL(sql);
		Iterator idsIt=idsList.iterator();
		
		while(idsIt.hasNext()) {
			Object data=(Object)idsIt.next();
			if(data!=null) {
				ids += data.toString()+",";
			}
		}
		//add by fyyang 091105
		if(ids.equals(""))
		{
			return result;
		}
		ids = ids.substring(0, ids.length()-1);
		// add by ywliu 20091104 End
		}
		
		//-------modify by fyyang 091106----end----- 
		// SQL语句连接
		sbd.append("SELECT DISTINCT A.REQUIREMENT_DETAIL_ID, A.REQUIREMENT_HEAD_ID, A.APPROVED_QTY, A.PUR_QTY AS PLAN_PUR_QTY, ");
		sbd.append("B.MATERIAL_ID, B.MATERIAL_NO, B.MATERIAL_NAME, B.SPEC_NO, B.PARAMETER, B.QA_CONTROL_FLAG, ");
		sbd.append("B.MAX_STOCK, B.PUR_UM_ID, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS PLAN_MODIFY_DATE ");
		//add by fyyang 090814 -----------------------------------------------------------------------------
		sbd.append(",( select bb.unit_price \n");
		sbd.append(" from mrp_j_plan_gather aa,mrp_j_plan_inquire_detail bb \n");
		sbd.append(" where aa.gather_id=bb.gather_id(+) \n");
		sbd.append(	"and bb.is_select_supplier(+)='Y'  \n");
		sbd.append(	"and bb.effect_start_date<=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  and bb.effect_end_date>=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  \n");
		sbd.append(	"		and aa.material_id=A.Material_Id and rownum=1  and  instr(','||aa.requirement_detail_ids||',',','||a.requirement_detail_id||',' )<>0  \n");//modify by drdu 091127
		sbd.append(	"	  ) quotedPrice, \n");
		sbd.append(	"		  (  select bb.inquire_supplier \n");
		sbd.append(	"		from mrp_j_plan_gather aa,mrp_j_plan_inquire_detail bb \n");
		sbd.append(	"		where aa.gather_id=bb.gather_id(+) \n");
		sbd.append(	"		and bb.is_select_supplier(+)='Y' \n");
		sbd.append(	"	and bb.effect_start_date<=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  and bb.effect_end_date>=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')   \n");
		sbd.append(	"		and aa.material_id=A.Material_Id and rownum=1  and  instr(','||aa.requirement_detail_ids||',',','||a.requirement_detail_id||',' )<>0 \n");//modify by drdu 091127
		sbd.append(	"		  ) supplier, \n");
		sbd.append(	"	  GETCLIENTNAME( (select bb.inquire_supplier \n");
		sbd.append(	"	from mrp_j_plan_gather aa,mrp_j_plan_inquire_detail bb \n");
		sbd.append(	"		where aa.gather_id=bb.gather_id(+) \n");
		sbd.append(	"		and bb.is_select_supplier(+)='Y' \n");
		sbd.append(	"		and bb.effect_start_date<=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  and bb.effect_end_date>=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  \n");
		sbd.append(	"		and aa.material_id=A.Material_Id and rownum=1  and  instr(','||aa.requirement_detail_ids||',',','||a.requirement_detail_id||',' )<>0 ))  supplyName, \n");//modify by drdu 091127
		sbd.append(	" (select cc.client_code \n");
		sbd.append(	"	     from mrp_j_plan_gather aa,mrp_j_plan_inquire_detail bb,CON_J_CLIENTS_INFO cc \n");
		sbd.append(	"	    where aa.gather_id=bb.gather_id(+)  and bb.inquire_supplier=cc.cliend_id(+) \n");
		sbd.append(	"   and bb.is_select_supplier(+)='Y'  \n");
		sbd.append(	"    and bb.effect_start_date<=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  and bb.effect_end_date>=to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')  \n");
		sbd.append(	"    and aa.material_id=A.Material_Id and rownum=1  and  instr(','||aa.requirement_detail_ids||',',','||a.requirement_detail_id||',' )<>0 ) supplierNo ,\n");//modify by drdu 091127
		//----------------------------------------------------------------------------------------------------
		sbd.append(" getunitname(b.pur_um_id) \n"); //add by drdu 091127
		sbd.append("  ,a.memo,getdeptname(e.mr_dept) \n"); // add by liuyi 20100406
		
		sbd.append("FROM MRP_J_PLAN_REQUIREMENT_DETAIL A, INV_C_MATERIAL B, MRP_J_PLAN_REQUIREMENT_HEAD E ");
		sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' AND E.IS_USE = 'Y' ");
		sbd.append("AND A.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND B.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND E.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND A.REQUIREMENT_HEAD_ID = E.REQUIREMENT_HEAD_ID AND E.MR_STATUS = '2' ");
		sbd.append("AND A.MATERIAL_ID = B.MATERIAL_ID ");
		sbd.append("AND (A.IS_GENERATED = 'N' OR A.IS_GENERATED = 'G')");
		sbd.append("AND (A.IS_GENERATED = 'N' OR A.IS_GENERATED = 'G')");
		//----modify by fyyang 091106---start-----
		if(!argBuyer.equals("999999"))
		{
		// add by ywliu 20091104
		sbd.append("AND A.requirement_detail_id in (");
		sbd.append(ids);
		sbd.append(")");
		// add by ywliu 20091104 End
		}
		//----modify by fyyang 091106---end-----
//		if (strMaterials.length() > 0) {
//			sbd.append("AND B.MATERIAL_NO IN (");
//			sbd.append(strMaterials);
//			sbd.append(")");
//		} else {  根据采购员查询物料编码方法注释掉  modify by ywliu 2009/6/30
//			sbd.append("AND B.MATERIAL_NO IS NULL");
//		}
		//List<PurJOrderRegister> result = queryDescribeByNativeSQL(sbd.toString(), PurJOrderRegister.class);
		//modify by fyyang 090814-------------------------------
		
		List list=bll.queryByNativeSQL(sbd.toString());
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object [] data=(Object [])it.next();
			PurJOrderRegister model=new PurJOrderRegister();
			if(data[0]!=null)
			{
				model.setRequirementDetailId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setRequirementHeadId(Long.parseLong(data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setApprovedQty(Double.parseDouble(data[2].toString()));
			}
			if(data[3]!=null)
			{
				model.setPlanPurQty(Double.parseDouble(data[3].toString()));
			}
			if(data[4]!=null)
			{
				model.setMaterialId(Long.parseLong(data[4].toString()));
			}
			if(data[5]!=null)
			{
				model.setMaterialNo(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setMaterialName(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setSpecNo(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setParameter(data[8].toString());
			}
			if(data[9]!=null)
			{
				model.setQaControlFlag(data[9].toString());
			}
			if(data[10]!=null)
			{
				model.setMaxStock(Double.parseDouble(data[10].toString()));
			}
		
			if(data[11]!=null)
			{
				model.setPurUmId(Long.parseLong(data[11].toString()));
			}
			if(data[12]!=null)
			{
				model.setPlanModifyDate(data[12].toString());
			}
			//quotedPrice  supplier  supplyName
			if(data[13]!=null)
			{
				model.setQuotedPrice(Double.parseDouble(data[13].toString()));
			}
			if(data[14]!=null)
			{
				model.setSupplier(Long.parseLong(data[14].toString()));
			}
			if(data[15]!=null)
			{
				model.setSupplyName(data[15].toString());
			}
			if(data[16]!=null)
			{
				model.setSupplierNo(data[16].toString());
			}
			if(data[17] != null)
				model.setStockUmName(data[17].toString());
			if(data[18] != null)
				model.setSbMemo(data[18].toString());
			if(data[19] != null)
				model.setSbDeptName(data[19].toString());
			result.add(model);
		}
		return result;
	}
	
	/**
	 * 得到物料的当前库存
	 * @param argEnterpriseCd 企业编码
	 * @param argMaterialIds 物料ID(复数)
	 * @return 物料的当前库存
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Double> getCurrentMatCounts(String argEnterpriseCd, String argMaterialIds) {
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
		sbd.append("SELECT A.MATERIAL_ID, SUM(A.OPEN_BALANCE + A.RECEIPT + A.ADJUST - A.ISSUE) AS NOWCOUNT ");
		sbd.append("FROM INV_J_WAREHOUSE A, INV_C_WAREHOUSE B ");
		sbd.append("WHERE A.IS_USE = 'Y' AND A.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND B.IS_USE = 'Y' AND B.ENTERPRISE_CODE = '");
		sbd.append(argEnterpriseCd);
		sbd.append("' AND A.WHS_NO = B.WHS_NO AND B.IS_INSPECT = 'N' ");
		sbd.append("AND A.MATERIAL_ID IN (");
		sbd.append(argMaterialIds);
		sbd.append(")");
		sbd.append(" GROUP BY A.MATERIAL_ID");
		
		List lstResult = bll.queryByNativeSQL(sbd.toString());
		Map<String, Double> result = new HashMap<String, Double>();
		for (int intCnt = 0; intCnt < lstResult.size(); intCnt++) {
			Object[] datas = (Object[]) lstResult.get(intCnt);
			
			Double value = null;
			if (datas[1] != null) {
				value = new Double(datas[1].toString());
			}
			// 物料ID 当前库存
			result.put(datas[0].toString(), value);
		}
		
		if (result.size() < 1) {
			return null;
		}
		return result;
	}
	
	/**
	 * 通过物料ID得到采购单明细
	 *
	 * @param materialId 物料ID
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return List<PurJOrderDetails> 采购单明细
	 */
	public List<PurJOrderDetails> findByMaterialId(Object materialId, final int... rowStartIdxAndCount) {
		return findByProperty(MATERIAL_ID, materialId, rowStartIdxAndCount);
	}

	/**
	 * 通过生产厂家得到采购单明细
	 *
	 * @param factory 生产厂家
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return List<PurJOrderDetails> 采购单明细
	 */
	public List<PurJOrderDetails> findByFactory(Object factory, final int... rowStartIdxAndCount) {
		return findByProperty(FACTORY, factory, rowStartIdxAndCount);
	}

	/**
	 * 查找所有的采购单明细
	 * 
	 * @return List<PurJOrderDetails> 采购单明细
	 */
	@SuppressWarnings("unchecked")
	public List<PurJOrderDetails> findAll() {
		LogUtil.log("finding all PurJOrderDetails instances", Level.INFO, null);
		try {
			final String queryString = "select model from PurJOrderDetails model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 采购单信息查询
	 *
	 * @param purNo 采购单号
	 * @param enterpriseCode 企业编码
     * @return PurchaseBean 采购单明细
	 */
public PurchaseBean findAllForPurchase(String purNo,String enterpriseCode){
		
		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		PurchaseBean purchaseBean = new PurchaseBean();
		List purchaseList = new ArrayList();
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String patternMoney = "###,###,###,###,##0.0000";
		DecimalFormat dfMoney = new DecimalFormat(patternMoney);
		String nullNumber = "0.00";
		String nullMoney = "0.0000";
		try{
			String sql = "select distinct a.BUYER,a.CONTRACT_NO,a.SUPPLIER,c.CLIENT_NAME,f.CURRENCY_NAME,a.DUE_DATE,a.TAX_RATE,g.RATE,a.MEMO \n"+
 			 "from PUR_J_ORDER a,CON_J_CLIENT_INFO c,SYS_C_CURRENCY f,SYS_C_EXCHANGE_RATE g,PUR_J_QUOTATION e \n"+
 			 "where a.IS_USE='Y' \n"+
 			 "and c.IS_USE='Y' \n"+ 
 			 "and a.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+ 
 			 "and c.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+ 
 			 "and a.PUR_NO='"+ purNo +"' \n"+ 
 			 "and a.SUPPLIER = c.CLIEND_ID \n"+
 			 "and a.CURRENCY_ID = f.CURRENCY_ID \n"+
 			 "and a.CURRENCY_ID = g.ORI_CURRENCY_ID \n"+
 			 "and g.DST_CURRENCY_ID = e.QUOTATION_CURRENCY \n"+
 			 "and g.EFFECTIVE_DATE <= SYSDATE and g.DISCONTINUE_DATE >= SYSDATE \n"+
 			 "and e.EFFECTIVE_DATE <= SYSDATE and e.DISCONTINUE_DATE >= SYSDATE \n"+
 			 "and e.SUPPLIER = c.CLIEND_ID \n"+
 			 "and e.IS_USE = 'Y' \n"+
 			 "and e.ENTERPRISE_CODE = '"+ enterpriseCode +"' \n"+
 			 "and f.IS_USE='Y' \n"+
 			 "and f.ENTERPRISE_CODE = '"+ enterpriseCode +"' \n"+
 			 "and g.IS_USE = 'Y' \n"+
 			 "and g.ENTERPRISE_CODE = '"+ enterpriseCode +"'";
			List list=bll.queryByNativeSQL(sql);
			Iterator it=list.iterator();
			while(it.hasNext()){
				Object[] data=(Object[])it.next();
				if(data[0]!=null){
					purchaseBean.setBuyer(data[0].toString());
				}
				if(data[1]!=null){
					purchaseBean.setContractNo(data[1].toString());
				}
				if(data[2]!=null){
					purchaseBean.setSupplier(data[2].toString());
				}
				if(data[3]!=null){
					purchaseBean.setClientName(data[3].toString());
				}
				if(data[4]!=null){
					purchaseBean.setCurrencyType(data[4].toString());
				}
				if(data[5]!=null){
					purchaseBean.setDueDate(data[5].toString());
				}
				if(data[6]!=null){
					purchaseBean.setTaxRate(dfMoney.format(data[6]));
				}else{
					purchaseBean.setTaxRate(nullMoney);
				}
				if(data[7]!=null){
					purchaseBean.setExchangeRate(dfMoney.format(data[7]));
				}else{
					purchaseBean.setExchangeRate(nullMoney);
				}
				if(data[8]!=null){
					purchaseBean.setMeno(data[8].toString());
				}
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}
		try{
			String sql = "SELECT DISTINCT \n"+
								"B.PUR_ORDER_DETAILS_ID AS orderDetailsId, \n"+
								"A.MATERIAL_NO, \n"+
								"A.MATERIAL_NAME, \n"+
								"A.SPEC_NO, \n"+
								"A.PARAMETER, \n"+
								"B.PUR_QTY, \n"+
								"B.RCV_QTY, \n"+
								"B.UNIT_PRICE, \n"+
								"B.DUE_DATE AS orderDetailsDueDate, \n"+
								"B.TAX_RATE AS orderDetailsTaxRate, \n"+
								"B.PUR_QTY * B.UNIT_PRICE, \n"+	
								"B.PUR_QTY * B.UNIT_PRICE * B.TAX_RATE, \n"+
								//"DECODE(A.QA_CONTROL_FLAG,'Y','是','N','否'), \n"+
								"  GETUNITNAME(A.PUR_UM_ID),\n"+//modify by fyyang 20100315 是否免检去掉，增加单价列
								"B.MEMO AS orderDetailsMemo \n"+
								"FROM \n"+
								"INV_C_MATERIAL A, \n"+
								"PUR_J_ORDER_DETAILS B  left join PUR_J_PLAN_ORDER C  on B.PUR_ORDER_DETAILS_ID = C.PUR_ORDER_DETAILS_ID  and C.IS_USE = 'Y'  and C.ENTERPRISE_CODE = '"+ enterpriseCode +"' \n"+
								"WHERE \n"+
								"A.IS_USE = 'Y' AND \n"+
								"B.IS_USE = 'Y'   AND \n"+
								"A.ENTERPRISE_CODE = '"+ enterpriseCode +"'   AND \n"+
								"B.ENTERPRISE_CODE = '"+ enterpriseCode +"'    AND \n"+
								"B.PUR_NO = '"+ purNo +"'   AND \n"+
								"B.MATERIAL_ID = A.MATERIAL_ID \n"+
								"ORDER BY \n"+
								"A.MATERIAL_NO ASC";
			List list=bll.queryByNativeSQL(sql);
			Iterator it=list.iterator();
			while(it.hasNext()){
				Object[] data=(Object[])it.next();
				PurchaseListBean model = new PurchaseListBean();
				if(data[0]!=null){
					model.setOrderDetailsId(data[0].toString());
				}
				if(data[1]!=null){
					model.setMaterialNo(data[1].toString());
				}
				if(data[2]!=null){
					model.setMaterialName(data[2].toString());
				}
				if(data[3]!=null){
					model.setSpecNo(data[3].toString());
				}
				if(data[4]!=null){
					model.setParamater(data[4].toString());
				}
				if(data[5]!=null){
					model.setPurchaseQuatity(dfNumber.format(data[5]));
				}else{
					model.setPurchaseQuatity(nullNumber);
				}
				if(data[6]!=null){
					model.setAchieveQuantity(dfNumber.format(data[6]));
				}else{
					model.setAchieveQuantity(nullNumber);
				}
				if(data[7]!=null){
					model.setQuotedPrice(dfMoney.format(data[7]));
				}else{
					model.setQuotedPrice(nullMoney);
				}
				if(data[8]!=null){
					model.setDueDate(data[8].toString());
				}
				if(data[9]!=null){
					model.setTaxRate(dfMoney.format(data[9]));
				}else{
					model.setTaxRate(nullMoney);
				}
				if(data[10]!=null){
					model.setLittleCulMoney(dfMoney.format(data[10]));
				}else{
					model.setLittleCulMoney(nullMoney);
				}
				if(data[11]!=null){
					model.setTaxMoney(dfMoney.format(data[11]));
				}else{
					model.setTaxMoney(nullMoney);
				}
				if(data[12]!=null){
					model.setWhetherNoCheck(data[12].toString());
				}
				if(data[13]!=null){
					// modified by liuyi 20100406 备注中显示的是需求计划明细备注
//					model.setMeno(data[13].toString());
					model.setMeno("");
				}
				
				// add by liuyi 20100406 通过采购明细id和采购单号获得需求计划明细id，进而获取需求计划明细备注和申报部门
				String ssSql = 
					"select distinct b.memo, c.mr_dept, getdeptname(c.mr_dept)\n" +
					"  from pur_j_plan_order              a,\n" + 
					"       mrp_j_plan_requirement_detail b,\n" + 
					"       mrp_j_plan_requirement_head   c\n" + 
					" where a.is_use = 'Y'\n" + 
					"   and a.enterprise_code = 'hfdc'\n" + 
					"   and a.pur_order_details_id = '"+model.getOrderDetailsId()+"'\n" + 
					"   and a.pur_no = '"+ purNo +"'\n" + 
					"   and a.requirement_detail_id = b.requirement_detail_id\n" + 
					"   and b.requirement_head_id = c.requirement_head_id";
				List ssList = bll.queryByNativeSQL(ssSql);
				if(ssList != null && ssList.size() > 0)
				{
					Object[] ssdata = (Object[])ssList.get(0);
					if(ssdata[0] != null)
						model.setMeno(ssdata[0].toString());
					if(ssdata[1] != null)
						model.setDeptCode(ssdata[1].toString());
					if(ssdata[2] != null)
						model.setDeptName(ssdata[2].toString());
				}

				purchaseList.add(model);
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}
		purchaseBean.setPurchaseList(purchaseList);
		return purchaseBean;
	}
	
	/**
	 * 查询一条无参数sql语句,返回查询结果 
	 * @param argSql SQL语句
	 * @param argClass JavaBean class对象
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 符合条件的List对象
	 */
	@SuppressWarnings("unchecked")
	public List queryDescribeByNativeSQL(String argSql, Class<?> argClass, final int ...rowStartIdxAndCount) {
		List lstResult = bll.queryByNativeSQL(argSql, rowStartIdxAndCount);
		
		try {
			lstResult = getDescriptionList(lstResult, argSql, argClass);
		} catch (Exception e) {
			throw new RuntimeException("转换为泛型安全的List时出错!");
		}
		
		return lstResult;
	}

	/**
	 * 转换为泛型安全的List
	 * @param argList 装有对象数组的List
	 * @param argSql SQL语句
	 * @param argClass JavaBean class对象
	 * @return 泛型安全的List
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	public List getDescriptionList(List argList, String argSql, Class<?> argClass) throws Exception {
    	List lstResult = new ArrayList();
    	if (argList != null ){
    		for (int intCnt = 0; intCnt < argList.size(); intCnt++) {
    			lstResult.add(getDescriptionObject((Object[]) argList.get(intCnt), argSql, argClass));
    		}
    	}
    	
    	return lstResult;
    }
    
    private Object getDescriptionObject(Object[] argObjects, String argSql, Class<?> argClass) throws Exception {
    	Matcher matcherSelect = Pattern.compile("(?<=SELECT )(?:DISTINCT )?(.+?(?=\\s+FROM\\s+))",
    			Pattern.CASE_INSENSITIVE).matcher(argSql);
    	Matcher matcherProp = Pattern.compile("(?:^|,)\\s*+([^,]+(?<=\\S))").matcher("");
    	Matcher matcherSpace = Pattern.compile(".*\\s(\\S+)").matcher("");
    	Matcher matcherSplit = Pattern.compile("\\.(\\S+)").matcher("");
    	
    	String strSelectProp = "";
    	if (matcherSelect.find()) {
    		strSelectProp = matcherSelect.group(1);
    	}
    	
    	Object result = null;
    	
    	int intPropCnt = 0;
    	matcherProp.reset(strSelectProp);
    	while(matcherProp.find()) {
    		String strPropName = matcherProp.group(1);
    		if (strPropName != null) {
    			while (strPropName.indexOf("(") > -1
    					&& strPropName.indexOf(")") < 0) {
    				// 如果有函数
    				if (matcherProp.find()) {
    					strPropName += matcherProp.group(1);
    				} else {
    					break;
    				}
    			}
    			matcherSpace.reset(strPropName);
    			if (matcherSpace.find()) {
    				strPropName = matcherSpace.group(1);
    			}
    			
    			matcherSplit.reset(strPropName);
    			if (matcherSplit.find()) {
    				strPropName = matcherSplit.group(1);
    			}
    			
    			strPropName = convertBeanPropName(strPropName);
    			if (result == null) {
    				result = argClass.newInstance();
    			}
    			
    			Field field = argClass.getDeclaredField(strPropName);
    			field.setAccessible(true);
    			field.set(result, getValueByType(argObjects[intPropCnt], field.getType()));
    		}
    		
    		intPropCnt++;
    	}
    	
    	return result;
    }
    
    /**
     * @param requirementDetailId
     * @return PurchaseListBean
     * modify by fyyang 090513
     * 根据需求计划明细ID查询采购单信息
     */
    public PurchaseListBean findOrdersByDetailID(Long requirementDetailId){
    	String sql = "select distinct c.pur_order_details_id,c.pur_no,c.pur_qty,c.rcv_qty,c.ins_qty,e.client_name,c.unit_price,\n" +
    	" d.buyer,getworkername(d.buyer), \n" + 
    	"  to_char(d.last_modified_date,'yyyy-MM-dd'), \n" + 
    	" to_char(d.due_date,'yyyy-MM-dd') \n" + 
    		"from mrp_j_plan_requirement_detail a , PUR_J_PLAN_ORDER b ,pur_j_order_details c,PUR_J_ORDER d,con_j_clients_info e\n" + 
    		"where a.requirement_detail_id =b.requirement_detail_id and c.pur_no = b.pur_no and a.requirement_detail_id = '" +
    		requirementDetailId +"'   and  c.pur_no=d.pur_no  and d.supplier=e.cliend_id(+) "
    		+ " and a.material_id=c.material_id \n";
    	PurchaseListBean model = new PurchaseListBean();
    	List result = bll.queryByNativeSQL(sql);
    	Iterator it = result.iterator();
    	if(it.hasNext()){
    		Object[] data= (Object[]) it.next();
    		if(data[0] != null) {
    			model.setOrderDetailsId(data[0].toString());
    		}
    		if(data[1] != null) {
    			model.setMaterialName(data[1].toString());
    		}
    		if(data[2] != null) {
    			model.setPurchaseQuatity(data[2].toString());
    		}
    		if(data[3] != null) {
    			model.setAchieveQuantity(data[3].toString());
    		}
    		if(data[4] != null) {
    			model.setSpecNo(data[4].toString());
    		}
    		if(data[5]!=null)
    		{
    			model.setMeno(data[5].toString());
    		}
    		if(data[6]!=null)
    		{
    			//quotedPrice
    			model.setQuotedPrice(data[6].toString());
    		}
    		if(data[7] != null)
    			model.setBuyerBy(data[7].toString());
    		if(data[8] != null)
    			model.setBuyerName(data[8].toString());
    		if(data[9] != null)
    			model.setBuyTime(data[9].toString());
    		if(data[10] != null)
    			model.setDueDate(data[10].toString());
    	}
    	return model;
    }
    
    /**
     * 根据类型得到该类型的值
     * @param argObject 值
     * @param argType 类型
     * @return 该类型的值
     */
    private Object getValueByType(Object argObject, Class<?> argType) {
    	if (argObject == null) {
    		return null;
    	}
        String strValue = argObject.toString();

        if (void.class == argType) {
            return null;
        } else if (Integer.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Integer(strValue);
        } else if (Short.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
        	return new Short(strValue);
        } else if (Long.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Long(strValue);
        } else if (Double.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Double(strValue);
        } else if (Float.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Float(strValue);
        } else if (Boolean.class == argType) {
            return new Boolean(strValue);
        } else if (Byte.class == argType) {
            return new Byte(strValue);
        } else if (Character.class == argType) {
            char ch = '\0';
            if (strValue.trim().length() > 0) {
                ch = strValue.charAt(0);
            } else {
            	return null;
            }
            return new Character(ch);
        } else if (Date.class == argType) {
        	return argObject;
        }

        return strValue;
    }

    private String convertBeanPropName(String argSqlPropName) {
    	String[] split = argSqlPropName.split("_");
    	
    	if (split == null || split.length < 1) {
    		return argSqlPropName.toLowerCase();
    	}
    	StringBuilder sdbResult = new StringBuilder();
    	sdbResult.append(split[0].toLowerCase());
    	for (int intCnt = 1; intCnt < split.length; intCnt++) {
    		sdbResult.append(getFirstUpperCase(split[intCnt].toLowerCase()));
    	}
    	
    	return sdbResult.toString();
    }

    public String getFirstUpperCase(String argString) {

        if (isNullOrEmpty(argString)) {
            return "";
        }
        if (argString.length() < 2) {
            return argString.toUpperCase();
        }

        char ch = argString.charAt(0);
        ch = Character.toUpperCase(ch);

        return ch + argString.substring(1);
    }

    public boolean isNullOrEmpty(CharSequence argCharSeq) {

        if ((argCharSeq == null) ||
                (argCharSeq.toString().trim().length() < 1)) {
            return true;
        }

        return false;
    }
    
  /**
   * add by sychen 20100427
   * update by ltong 20100428
   * @param list
   */
	@SuppressWarnings("unchecked")
	public void updatePurQty(List<Map> list)
	{
		try {
			for (Map data : list) {
				PurJOrderDetails model =this.findById( Long.parseLong(data.get("id").toString())); 
				if (data.get("purQty") != null) {
					model.setPurQty(Double.parseDouble(data.get("purQty").toString()));
				} 
				this.update(model);
				
			}
		} catch (RuntimeException re) {
			throw re;
		}
	
	}
}