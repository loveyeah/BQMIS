package power.ejb.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.form.TransActionHisInfo;

/**
 * Facade for entity InvJTransactionHis.
 * 
 * @see power.ejb.resource.InvJTransactionHis
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJTransactionHisFacade implements InvJTransactionHisFacadeRemote {
	// property constants
	public static final String ORDER_NO = "orderNo";
	public static final String SEQUENCE_ID = "sequenceId";
	public static final String MATERIAL_ID = "materialId";
	public static final String TRANS_ID = "transId";
	public static final String REASON_ID = "reasonId";
	public static final String LOT_NO = "lotNo";
	public static final String TRANS_QTY = "transQty";
	public static final String TRANS_UM_ID = "transUmId";
	public static final String PRICE = "price";
	public static final String ACT_COST = "actCost";
	public static final String STD_COST = "stdCost";
	public static final String ORI_CURRENCY_ID = "oriCurrencyId";
	public static final String CURRENCY_ID = "currencyId";
	public static final String EXCHANGE_RATE = "exchangeRate";
	public static final String TAX_RATE = "taxRate";
	public static final String FROM_WHS_NO = "fromWhsNo";
	public static final String FROM_LOCATION_NO = "fromLocationNo";
	public static final String TO_WHS_NO = "toWhsNo";
	public static final String TO_LOCATION_NO = "toLocationNo";
	public static final String CUSTOMER_NO = "customerNo";
	public static final String SUPPLIER = "supplier";
	public static final String MANUFACTURER_NO = "manufacturerNo";
	public static final String RECEIVE_MAN = "receiveMan";
	public static final String RECEIVE_DEPT = "receiveDept";
	public static final String COST_MAN = "costMan";
	public static final String COST_DEPT = "costDept";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String MEMO = "memo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public  static Ejb3Factory factory;
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="BpCMeasureUnitFacade")
	protected BpCMeasureUnitFacadeRemote remoteComm;
	@EJB (beanName="BaseDataManagerImpl")
	private BaseDataManager personInfo;
	
	/**
	 * Perform an initial save of a previously unsaved InvJTransactionHis
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvJTransactionHis entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJTransactionHis entity) {
		LogUtil.log("saving InvJTransactionHis instance", Level.INFO, null);
		try {
			if(entity.getTransHisId()==null){
				// 设定主键值
		        entity.setTransHisId((bll.getMaxId("INV_J_TRANSACTION_HIS", "TRANS_HIS_ID")));
				}
	        // 设定修改时间
	        entity.setLastModifiedDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvJTransactionHis entity.
	 * 
	 * @param entity
	 *            InvJTransactionHis entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJTransactionHis entity) {
		LogUtil.log("deleting InvJTransactionHis instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvJTransactionHis.class,
					entity.getTransHisId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJTransactionHis entity and return it or a
	 * copy of it to the sender. A copy of the InvJTransactionHis entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvJTransactionHis entity to update
	 * @return InvJTransactionHis the persisted InvJTransactionHis entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJTransactionHis update(InvJTransactionHis entity) {
		LogUtil.log("updating InvJTransactionHis instance", Level.INFO, null);
		try {
			InvJTransactionHis result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJTransactionHis findById(Long id) {
		LogUtil.log("finding InvJTransactionHis instance with id: " + id,
				Level.INFO, null);
		try {
			InvJTransactionHis instance = entityManager.find(
					InvJTransactionHis.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJTransactionHis entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJTransactionHis property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJTransactionHis> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJTransactionHis> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvJTransactionHis instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJTransactionHis model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvJTransactionHis> findByOrderNo(Object orderNo) {
		return findByProperty(ORDER_NO, orderNo);
	}

	public List<InvJTransactionHis> findBySequenceId(Object sequenceId) {
		return findByProperty(SEQUENCE_ID, sequenceId);
	}

	public List<InvJTransactionHis> findByMaterialId(Object materialId) {
		return findByProperty(MATERIAL_ID, materialId);
	}

	public List<InvJTransactionHis> findByTransId(Object transId) {
		return findByProperty(TRANS_ID, transId);
	}

	public List<InvJTransactionHis> findByReasonId(Object reasonId) {
		return findByProperty(REASON_ID, reasonId);
	}

	public List<InvJTransactionHis> findByLotNo(Object lotNo) {
		return findByProperty(LOT_NO, lotNo);
	}

	public List<InvJTransactionHis> findByTransQty(Object transQty) {
		return findByProperty(TRANS_QTY, transQty);
	}

	public List<InvJTransactionHis> findByTransUmId(Object transUmId) {
		return findByProperty(TRANS_UM_ID, transUmId);
	}

	public List<InvJTransactionHis> findByPrice(Object price) {
		return findByProperty(PRICE, price);
	}

	public List<InvJTransactionHis> findByActCost(Object actCost) {
		return findByProperty(ACT_COST, actCost);
	}

	public List<InvJTransactionHis> findByStdCost(Object stdCost) {
		return findByProperty(STD_COST, stdCost);
	}

	public List<InvJTransactionHis> findByOriCurrencyId(Object oriCurrencyId) {
		return findByProperty(ORI_CURRENCY_ID, oriCurrencyId);
	}

	public List<InvJTransactionHis> findByCurrencyId(Object currencyId) {
		return findByProperty(CURRENCY_ID, currencyId);
	}

	public List<InvJTransactionHis> findByExchangeRate(Object exchangeRate) {
		return findByProperty(EXCHANGE_RATE, exchangeRate);
	}

	public List<InvJTransactionHis> findByTaxRate(Object taxRate) {
		return findByProperty(TAX_RATE, taxRate);
	}

	public List<InvJTransactionHis> findByFromWhsNo(Object fromWhsNo) {
		return findByProperty(FROM_WHS_NO, fromWhsNo);
	}

	public List<InvJTransactionHis> findByFromLocationNo(Object fromLocationNo) {
		return findByProperty(FROM_LOCATION_NO, fromLocationNo);
	}

	public List<InvJTransactionHis> findByToWhsNo(Object toWhsNo) {
		return findByProperty(TO_WHS_NO, toWhsNo);
	}

	public List<InvJTransactionHis> findByToLocationNo(Object toLocationNo) {
		return findByProperty(TO_LOCATION_NO, toLocationNo);
	}

	public List<InvJTransactionHis> findByCustomerNo(Object customerNo) {
		return findByProperty(CUSTOMER_NO, customerNo);
	}

	public List<InvJTransactionHis> findBySupplier(Object supplier) {
		return findByProperty(SUPPLIER, supplier);
	}

	public List<InvJTransactionHis> findByManufacturerNo(Object manufacturerNo) {
		return findByProperty(MANUFACTURER_NO, manufacturerNo);
	}

	public List<InvJTransactionHis> findByReceiveMan(Object receiveMan) {
		return findByProperty(RECEIVE_MAN, receiveMan);
	}

	public List<InvJTransactionHis> findByReceiveDept(Object receiveDept) {
		return findByProperty(RECEIVE_DEPT, receiveDept);
	}

	public List<InvJTransactionHis> findByCostMan(Object costMan) {
		return findByProperty(COST_MAN, costMan);
	}

	public List<InvJTransactionHis> findByCostDept(Object costDept) {
		return findByProperty(COST_DEPT, costDept);
	}

	public List<InvJTransactionHis> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvJTransactionHis> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<InvJTransactionHis> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvJTransactionHis> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvJTransactionHis entities.
	 * 
	 * @return List<InvJTransactionHis> all InvJTransactionHis entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJTransactionHis> findAll() {
		LogUtil.log("finding all InvJTransactionHis instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from InvJTransactionHis model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查找事务历史表
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param materialId
	 * @param transType
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject queryForMaterialMove(String enterpriseCode, String sdate,
			String edate, String materialId,String transType,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql ="select distinct h.TRANS_HIS_ID,h.ORDER_NO,h.SEQUENCE_ID, \n" +
				        "t.TRANS_NAME, \n" +
				        " m.MATERIAL_NO,m.MATERIAL_NAME,m.SPEC_NO,m.PARAMETER, \n" +
				        "h.TRANS_QTY,h.TRANS_UM_ID,h.LAST_MODIFIED_BY,h.LAST_MODIFIED_DATE, \n" +
				        "wf.WHS_NAME  wfWhsName,  \n" +
				        "lf.LOCATION_NAME  lfLocationName, \n" +
				        "wt.WHS_NAME  wtWhsName , \n" +
				        "lt.LOCATION_NAME  ltLocationName, \n" +
				        "h.LOT_NO, \n" +
				        "h.MEMO \n" +
				        "from  INV_J_TRANSACTION_HIS h,  \n" +
				        "INV_C_TRANSACTION t,  \n" +
				        "INV_C_MATERIAL m,  \n" +
				        "INV_C_WAREHOUSE  wf,  \n" +
				        "INV_C_LOCATION  lf, \n" +
				        "INV_C_WAREHOUSE  wt, \n" +
				        "INV_C_LOCATION  lt \n" +
				        " where  t.TRANS_ID = h.TRANS_ID  \n" +
				        "    and m.MATERIAL_ID = h.MATERIAL_ID \n" +
				        "    and wf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.LOCATION_NO(+) =h.FROM_LOCATION_NO  \n" +
				        
				        "    and wt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.LOCATION_NO(+) =h.TO_LOCATION_NO \n" +
				        
				        "    and h.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and t.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and m.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and wf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and wt.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lt.enterprise_code(+)='" + enterpriseCode + "'" ;
			
			String sqlCount = "select count(*) from ( \n" +
					    "select distinct h.TRANS_HIS_ID,h.ORDER_NO,h.SEQUENCE_ID, \n" +
				        "t.TRANS_NAME, \n" +
				        " m.MATERIAL_NO,m.MATERIAL_NAME,m.SPEC_NO,m.PARAMETER, \n" +
				        "h.TRANS_QTY,h.TRANS_UM_ID,h.LAST_MODIFIED_BY,h.LAST_MODIFIED_DATE, \n" +
				        "wf.WHS_NAME  wfWhsName,  \n" +
				        "lf.LOCATION_NAME  lfLocationName, \n" +
				        "wt.WHS_NAME  wtWhsName , \n" +
				        "lt.LOCATION_NAME  ltLocationName, \n" +
				        "h.LOT_NO, \n" +
				        "h.MEMO \n" +
				        "from  INV_J_TRANSACTION_HIS h,  \n" +
				        "INV_C_TRANSACTION t,  \n" +
				        "INV_C_MATERIAL m,  \n" +
				        "INV_C_WAREHOUSE  wf,  \n" +
				        "INV_C_LOCATION  lf, \n" +
				        "INV_C_WAREHOUSE  wt, \n" +
				        "INV_C_LOCATION  lt \n" +
				        " where  t.TRANS_ID = h.TRANS_ID  \n" +
				        "    and m.MATERIAL_ID = h.MATERIAL_ID \n" +
				        "    and wf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.LOCATION_NO(+) =h.FROM_LOCATION_NO  \n" +
				        
				        "    and wt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.LOCATION_NO(+) =h.TO_LOCATION_NO \n" +
				        
				        "    and h.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and t.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and m.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and wf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and wt.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lt.enterprise_code(+)='" + enterpriseCode + "'" ;
			if (sdate != null && !sdate.equals("")) {
				String strWhere =  "  and h.LAST_MODIFIED_DATE >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (edate != null && !edate.equals("")) {
				String strWhere = "  and h.LAST_MODIFIED_DATE <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			
			
			if (transType != null && !transType.equals("")) {
				String strWhere = "   and t.TRANS_CODE = '" + transType
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (materialId != null && !materialId.equals("")) {
				// modify by drdu 20090601
			//	String strWhere = "  and h.MATERIAL_ID = '" + materialId
				String strWhere = "  and m.MATERIAL_NAME like '%" + materialId + "%'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			sql += " order by m.MATERIAL_NO, h.TRANS_HIS_ID"; 
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if(list !=null && list.size()>0)
			{
				Double totalTransQty=0.00;  //异动数量合计
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					TransActionHisInfo model = new TransActionHisInfo();
					Object[] data = (Object[]) it.next();
					// 流水号
					if (data[0] != null)
						model.setTransHisId(Long.parseLong(data[0].toString()));
					// 单据号
					if (data[1] != null)
						model.setOrderNo(data[1].toString());
					// 项号
					if (data[2] != null)
						model.setSequenceNo(Long.parseLong(data[2].toString()));
					// 事务名称
					if (data[3] != null)
						model.setTransName(data[3].toString());
					// 物料编码
					if (data[4] != null)
						model.setMaterialNo(data[4].toString());
					// 物料名称
					if (data[5] != null)
						model.setMaterialName(data[5].toString());
					// 规格型号
					if (data[6] != null)
						model.setSpecNo(data[6].toString());
					// 材质参数
					if (data[7] != null)
						model.setParameter(data[7].toString());
					// 异动数量
					if (data[8] != null)
						model.setTransQty(Double.parseDouble(data[8].toString()));
					// 单位
					if (data[9] != null) {
						BpCMeasureUnit bcmu = remoteComm.findById(Long.parseLong(data[9].toString()));
		    		    if(bcmu!=null){
		    		    	String stockUmID = bcmu.getUnitName();
		    		    	model.setTransUmId(stockUmID);
		    		    }
		    		}
					// 操作人
					if (data[10] != null) {
					    Employee emp = personInfo.getEmployeeInfo(data[10].toString());
					    if(emp != null) {
							// 人员姓名
							String strChsName = emp.getWorkerName();
							model.setOperatedBy(strChsName);
					    }
					}
					// 操作时间
					if (data[11] != null)
						model.setOperatedDate(data[11].toString());
					// 操作仓库
					if (data[12] != null)
						model.setWhsName(data[12].toString());
					// 操作库位
					if (data[13] != null)
						model.setLocationName(data[13].toString());
					// 调入仓库
					if (data[14] != null)
						model.setWhsNameTwo(data[14].toString());
					// 调入库位
					if (data[15] != null)
						model.setLocationNameTwo(data[15].toString());
					// 批号
					if (data[16] != null)
						model.setLotNo(data[16].toString());
					// 备注
					if (data[17] != null)
						model.setMemo(data[17].toString());
					
					arrlist.add(model);
					totalTransQty+=model.getTransQty();
				}
				//-------------------------------
				TransActionHisInfo info = new TransActionHisInfo();
				info.setTransQty(totalTransQty);
				arrlist.add(info);
				//---------------------------------
				sqlCount += " )"; 
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				
				/// 计算总共的异动合计数量------------
				if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1 && rowStartIdxAndCount[0]!=0) {
				String totalSql=
					"select sum(h.TRANS_QTY)\n" +
					"from  INV_J_TRANSACTION_HIS h,\n" + 
					"INV_C_TRANSACTION t,\n" + 
					"INV_C_MATERIAL m,\n" + 
					"INV_C_WAREHOUSE  wf,\n" + 
					"INV_C_LOCATION  lf,\n" + 
					"INV_C_WAREHOUSE  wt,\n" + 
					"INV_C_LOCATION  lt\n" + 
					" where  t.TRANS_ID = h.TRANS_ID  \n" +
			        "    and m.MATERIAL_ID = h.MATERIAL_ID \n" +
			        "    and wf.WHS_NO(+) = h.FROM_WHS_NO \n" +
			        "    and lf.WHS_NO(+) = h.FROM_WHS_NO \n" +
			        "    and lf.LOCATION_NO(+) =h.FROM_LOCATION_NO  \n" +
			        
			        "    and wt.WHS_NO (+)=h.TO_WHS_NO \n" +
			        "    and lt.WHS_NO (+)=h.TO_WHS_NO \n" +
			        "    and lt.LOCATION_NO(+) =h.TO_LOCATION_NO \n" +
			        
			        "    and h.enterprise_code='" + enterpriseCode + "'\n" +
			        "    and t.enterprise_code='" + enterpriseCode + "'\n" +
			        "    and m.enterprise_code='" + enterpriseCode + "'\n" +
			        "    and wf.enterprise_code(+)='" + enterpriseCode + "'\n" +
			        "    and lf.enterprise_code(+)='" + enterpriseCode + "'\n" +
			        "    and wt.enterprise_code(+)='" + enterpriseCode + "'\n" +
			        "    and lt.enterprise_code(+)='" + enterpriseCode + "'" ;

				if (sdate != null && !sdate.equals("")) {
					String strWhere =  "  and h.LAST_MODIFIED_DATE >=to_date('" + sdate
							+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
					totalSql += strWhere;
				}
				if (edate != null && !edate.equals("")) {
					String strWhere = "  and h.LAST_MODIFIED_DATE <=to_date('" + edate
							+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
					totalSql += strWhere;
				}
				
				
				if (transType != null && !transType.equals("")) {
					String strWhere = "   and t.TRANS_CODE = '" + transType
							+ "'\n";
					totalSql += strWhere;
				}
				if (materialId != null && !materialId.equals("")) {
					
					String strWhere = "  and m.MATERIAL_NAME like '%" + materialId + "%'\n";
					totalSql += strWhere;
				}
				
				Object objtotal=bll.getSingal(totalSql);
				if(objtotal!=null&&!objtotal.equals(""))
				{
					TransActionHisInfo myInfo = new TransActionHisInfo();
					myInfo.setTransQty(Double.parseDouble(objtotal.toString()));
					arrlist.add(myInfo);
				}
				
				}
				//--------------------------------
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			} 
			return result;
		} catch (RuntimeException e) { 
 			throw e;
		}
	}
	
	//======================add by drdu  090514============================
	@SuppressWarnings("unchecked")
	public PageObject queryForissuList(String enterpriseCode, String materialCode,String issueNo,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql ="select distinct h.TRANS_HIS_ID, i.issue_no,h.SEQUENCE_ID, \n" +
				        "t.TRANS_NAME, \n" +
				        " m.MATERIAL_NO,m.MATERIAL_NAME,m.SPEC_NO,m.PARAMETER, \n" +
				        "h.TRANS_QTY,h.TRANS_UM_ID,h.LAST_MODIFIED_BY,h.LAST_MODIFIED_DATE, \n" +
				        "wf.WHS_NAME  wfWhsName,  \n" +
				        "lf.LOCATION_NAME  lfLocationName, \n" +
				        "wt.WHS_NAME  wtWhsName , \n" +
				        "lt.LOCATION_NAME  ltLocationName, \n" +
				        "h.LOT_NO, \n" +
				        "h.MEMO,\n" +
				        "h.std_cost,\n" +
				        " h.price\n" +
				        "from  INV_J_TRANSACTION_HIS h,  \n" +
				        "INV_C_TRANSACTION t,  \n" +
				        "INV_J_ISSUE_HEAD  i,  \n" +
				        
				        "INV_C_MATERIAL m,  \n" +
				        "INV_C_WAREHOUSE  wf,  \n" +
				        "INV_C_LOCATION  lf, \n" +
				        "INV_C_WAREHOUSE  wt, \n" +
				        "INV_C_LOCATION  lt \n" +
				        " where  t.TRANS_ID = h.TRANS_ID  \n" +
				        "    and m.MATERIAL_ID = h.MATERIAL_ID \n" +
				        "    and wf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.LOCATION_NO(+) =h.FROM_LOCATION_NO  \n" +
				        "    and i.issue_no = h.order_no \n" +
				        
				        "    and wt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.LOCATION_NO(+) =h.TO_LOCATION_NO \n" +
				        
				        "    and h.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and i.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and t.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and m.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and wf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and wt.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lt.enterprise_code(+)='" + enterpriseCode + "'" ;
			
			String sqlCount = "select count(*) from ( \n" +
					    "select distinct h.TRANS_HIS_ID,i.issue_no,h.SEQUENCE_ID, \n" +
				        "t.TRANS_NAME, \n" +
				        " m.MATERIAL_NO,m.MATERIAL_NAME,m.SPEC_NO,m.PARAMETER, \n" +
				        "h.TRANS_QTY,h.TRANS_UM_ID,h.LAST_MODIFIED_BY,h.LAST_MODIFIED_DATE, \n" +
				        "wf.WHS_NAME  wfWhsName,  \n" +
				        "lf.LOCATION_NAME  lfLocationName, \n" +
				        "wt.WHS_NAME  wtWhsName , \n" +
				        "lt.LOCATION_NAME  ltLocationName, \n" +
				        "h.LOT_NO, \n" +
				        "h.MEMO \n" +
				        "from  INV_J_TRANSACTION_HIS h,  \n" +
				        "INV_C_TRANSACTION t,  \n" +
				        "INV_C_MATERIAL m,  \n" +
				        "INV_J_ISSUE_HEAD  i,  \n" +
				        "INV_C_WAREHOUSE  wf,  \n" +
				        "INV_C_LOCATION  lf, \n" +
				        "INV_C_WAREHOUSE  wt, \n" +
				        "INV_C_LOCATION  lt \n" +
				        " where  t.TRANS_ID = h.TRANS_ID  \n" +
				        "    and m.MATERIAL_ID = h.MATERIAL_ID \n" +
				        "    and wf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.LOCATION_NO(+) =h.FROM_LOCATION_NO  \n" +
				        "    and i.issue_no = h.order_no \n" +
				        "    and wt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.LOCATION_NO(+) =h.TO_LOCATION_NO \n" +
				        
				        "    and h.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and t.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and i.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and m.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and wf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and wt.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lt.enterprise_code(+)='" + enterpriseCode + "'" ;
			
			if (materialCode != null && !materialCode.equals("")) {
				String strWhere = "  and m.MATERIAL_NO = '" + materialCode
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (issueNo != null && !issueNo.equals("")) {
				String strWhere = "  and h.ORDER_NO = '" + issueNo
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			sql += " order by m.MATERIAL_NO, h.TRANS_HIS_ID"; 
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if(list !=null && list.size()>0)
			{
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					TransActionHisInfo model = new TransActionHisInfo();
					Object[] data = (Object[]) it.next();
					// 流水号
					if (data[0] != null)
						model.setTransHisId(Long.parseLong(data[0].toString()));
					// 单据号
					if (data[1] != null)
						model.setOrderNo(data[1].toString());
					// 项号
					if (data[2] != null)
						model.setSequenceNo(Long.parseLong(data[2].toString()));
					// 事务名称
					if (data[3] != null)
						model.setTransName(data[3].toString());
					// 物料编码
					if (data[4] != null)
						model.setMaterialNo(data[4].toString());
					// 物料名称
					if (data[5] != null)
						model.setMaterialName(data[5].toString());
					// 规格型号
					if (data[6] != null)
						model.setSpecNo(data[6].toString());
					// 材质参数
					if (data[7] != null)
						model.setParameter(data[7].toString());
					// 异动数量
					if (data[8] != null)
						model.setTransQty(Double.parseDouble(data[8].toString()));
					// 单位
					if (data[9] != null) {
						BpCMeasureUnit bcmu = remoteComm.findById(Long.parseLong(data[9].toString()));
		    		    if(bcmu!=null){
		    		    	String stockUmID = bcmu.getUnitName();
		    		    	model.setTransUmId(stockUmID);
		    		    }
		    		}
					// 操作人
					if (data[10] != null) {
					    Employee emp = personInfo.getEmployeeInfo(data[10].toString());
					    if(emp != null) {
							// 人员姓名
							String strChsName = emp.getWorkerName();
							model.setOperatedBy(strChsName);
					    }
					}
					// 操作时间
					if (data[11] != null)
						model.setOperatedDate(data[11].toString());
					// 操作仓库
					if (data[12] != null)
						model.setWhsName(data[12].toString());
					// 操作库位
					if (data[13] != null)
						model.setLocationName(data[13].toString());
					// 调入仓库
					if (data[14] != null)
						model.setWhsNameTwo(data[14].toString());
					// 调入库位
					if (data[15] != null)
						model.setLocationNameTwo(data[15].toString());
					// 批号
					if (data[16] != null)
						model.setLotNo(data[16].toString());
					// 备注
					if (data[17] != null)
						model.setMemo(data[17].toString());
					//标准成本
					if(data[18] != null)
						model.setStdCost(Double.parseDouble(data[18].toString()));
					//物料单价
					if(data[19] != null)
						model.setPrice(Double.parseDouble(data[19].toString()));
					arrlist.add(model);
				}
				sqlCount += " )"; 
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			} 
			return result;
		} catch (RuntimeException e) { 
 			throw e;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject queryForArrivalList(String enterpriseCode, String materialCode,String arrivalNo,
			String purNo,final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql =
				"select distinct h.TRANS_HIS_ID,\n" +
				"                h.order_no,\n" + 
				"                h.SEQUENCE_ID,\n" + 
				"                t.TRANS_NAME,\n" + 
				"                m.MATERIAL_NO,\n" + 
				"                m.MATERIAL_NAME,\n" + 
				"                m.SPEC_NO,\n" + 
				"                m.PARAMETER,\n" + 
				"                h.TRANS_QTY,\n" + 
				"                h.TRANS_UM_ID,\n" + 
				"                h.LAST_MODIFIED_BY,\n" + 
				"                h.LAST_MODIFIED_DATE,\n" + 
				"                wf.WHS_NAME wfWhsName,\n" + 
				"                lf.LOCATION_NAME lfLocationName,\n" + 
				"                wt.WHS_NAME wtWhsName,\n" + 
				"                lt.LOCATION_NAME ltLocationName,\n" + 
				"                h.LOT_NO,\n" + 
				"                h.MEMO,\n" + 
				"                s.pur_no,\n" + 
				"                h.std_cost,\n" + 
				"                h.price\n" + 
				"  from INV_J_TRANSACTION_HIS h,\n" + 
				"       INV_C_TRANSACTION     t,\n" + 
				"       PUR_J_ARRIVAL_DETAILS s,\n" + 
				"       INV_C_MATERIAL        m,\n" + 
				"       INV_C_WAREHOUSE       wf,\n" + 
				"       INV_C_LOCATION        lf,\n" + 
				"       INV_C_WAREHOUSE       wt,\n" + 
				"       INV_C_LOCATION        lt\n" + 
				" where t.TRANS_ID = h.TRANS_ID\n" + 
				"   and m.MATERIAL_ID = h.MATERIAL_ID\n" + 
				"   and wf.WHS_NO(+) = h.FROM_WHS_NO\n" + 
				"   and lf.WHS_NO(+) = h.FROM_WHS_NO\n" + 
				"   and lf.LOCATION_NO(+) = h.FROM_LOCATION_NO\n" + 
				"   and h.order_no = s.pur_no\n" + 
				"   and h.material_id = s.material_id\n" + 
				"   and wt.WHS_NO(+) = h.TO_WHS_NO\n" + 
				"   and lt.WHS_NO(+) = h.TO_WHS_NO\n" + 
				"   and lt.LOCATION_NO(+) = h.TO_LOCATION_NO\n" + 
				"   and h.is_use = 'Y'\n" + 
				"   and t.is_use = 'Y'\n" + 
				"   and m.is_use = 'Y'\n" + 
				"   and s.is_use = 'Y'\n" + 
				"   and wf.is_use(+) = 'Y'\n" + 
				"   and lf.is_use(+) = 'Y'\n" + 
				"   and wt.is_use(+) = 'Y'\n" + 
				"   and lt.is_use(+) = 'Y'\n" + 
				"   and h.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   and m.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   and s.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   and wf.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
				"   and lf.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
				"   and wt.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
				"   and lt.enterprise_code(+) = '"+enterpriseCode+"'";
			
			String sqlCount = "select count(*) from ( \n" +
					    "select distinct h.TRANS_HIS_ID,h.order_no,h.SEQUENCE_ID, \n" +
				        "t.TRANS_NAME, \n" +
				        " m.MATERIAL_NO,m.MATERIAL_NAME,m.SPEC_NO,m.PARAMETER, \n" +
				        "h.TRANS_QTY,h.TRANS_UM_ID,h.LAST_MODIFIED_BY,h.LAST_MODIFIED_DATE, \n" +
				        "wf.WHS_NAME  wfWhsName,  \n" +
				        "lf.LOCATION_NAME  lfLocationName, \n" +
				        "wt.WHS_NAME  wtWhsName , \n" +
				        "lt.LOCATION_NAME  ltLocationName, \n" +
				        "h.LOT_NO, \n" +
				        "h.MEMO \n" +
				        "from  INV_J_TRANSACTION_HIS h,  \n" +
				        "INV_C_TRANSACTION t,  \n" +
				        "PUR_J_ARRIVAL_DETAILS s,\n" + 
				        "INV_C_MATERIAL m,  \n" +
				        "INV_C_WAREHOUSE  wf,  \n" +
				        "INV_C_LOCATION  lf, \n" +
				        "INV_C_WAREHOUSE  wt, \n" +
				        "INV_C_LOCATION  lt \n" +
				        " where  t.TRANS_ID = h.TRANS_ID  \n" +
				        "    and m.MATERIAL_ID = h.MATERIAL_ID \n" +
				        "    and wf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.WHS_NO(+) = h.FROM_WHS_NO \n" +
				        "    and lf.LOCATION_NO(+) =h.FROM_LOCATION_NO  \n" +
				        "    and h.order_no = s.pur_no\n" + 
				        "    and wt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.WHS_NO (+)=h.TO_WHS_NO \n" +
				        "    and lt.LOCATION_NO(+) =h.TO_LOCATION_NO \n" +
				        
				        "    and h.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and t.enterprise_code='" + enterpriseCode + "'\n" +
				        "    and m.enterprise_code='" + enterpriseCode + "'\n" +
				        "   and s.enterprise_code = '"+enterpriseCode+"'\n" + 
				        "    and wf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lf.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and wt.enterprise_code(+)='" + enterpriseCode + "'\n" +
				        "    and lt.enterprise_code(+)='" + enterpriseCode + "'" ;
			
			if (materialCode != null && !materialCode.equals("")) {
				String strWhere = "  and m.MATERIAL_NO = '" + materialCode
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (arrivalNo != null && !arrivalNo.equals("")) {
				String strWhere = "  and h.order_no = '" + arrivalNo
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (purNo != null && !purNo.equals("")) {
				String strWhere = "  and s.pur_no = '" + purNo
						+ "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			sql += " order by m.MATERIAL_NO, h.TRANS_HIS_ID"; 
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if(list !=null && list.size()>0)
			{
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					TransActionHisInfo model = new TransActionHisInfo();
					Object[] data = (Object[]) it.next();
					// 流水号
					if (data[0] != null)
						model.setTransHisId(Long.parseLong(data[0].toString()));
					// 单据号
					if (data[1] != null)
						model.setOrderNo(data[1].toString());
					// 项号
					if (data[2] != null)
						model.setSequenceNo(Long.parseLong(data[2].toString()));
					// 事务名称
					if (data[3] != null)
						model.setTransName(data[3].toString());
					// 物料编码
					if (data[4] != null)
						model.setMaterialNo(data[4].toString());
					// 物料名称
					if (data[5] != null)
						model.setMaterialName(data[5].toString());
					// 规格型号
					if (data[6] != null)
						model.setSpecNo(data[6].toString());
					// 材质参数
					if (data[7] != null)
						model.setParameter(data[7].toString());
					// 异动数量
					if (data[8] != null)
						model.setTransQty(Double.parseDouble(data[8].toString()));
					// 单位
					if (data[9] != null) {
						BpCMeasureUnit bcmu = remoteComm.findById(Long.parseLong(data[9].toString()));
		    		    if(bcmu!=null){
		    		    	String stockUmID = bcmu.getUnitName();
		    		    	model.setTransUmId(stockUmID);
		    		    }
		    		}
					// 操作人
					if (data[10] != null) {
					    Employee emp = personInfo.getEmployeeInfo(data[10].toString());
					    if(emp != null) {
							// 人员姓名
							String strChsName = emp.getWorkerName();
							model.setOperatedBy(strChsName);
					    }
					}
					// 操作时间
					if (data[11] != null)
						model.setOperatedDate(data[11].toString());
					// 操作仓库
					if (data[12] != null)
						model.setWhsName(data[12].toString());
					// 操作库位
					if (data[13] != null)
						model.setLocationName(data[13].toString());
					// 调入仓库
					if (data[14] != null)
						model.setWhsNameTwo(data[14].toString());
					// 调入库位
					if (data[15] != null)
						model.setLocationNameTwo(data[15].toString());
					// 批号
					if (data[16] != null)
						model.setLotNo(data[16].toString());
					// 备注
					if (data[17] != null)
						model.setMemo(data[17].toString());
					if(data[18] != null)
						model.setPurNo(data[18].toString());
					if(data[19] != null)
						model.setStdCost(Double.parseDouble(data[19].toString()));
					if(data[20] != null)
						model.setPrice(Double.parseDouble(data[20].toString()));
					arrlist.add(model);
				}
				sqlCount += " )"; 
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			} 
			return result;
		} catch (RuntimeException e) { 
 			throw e;
		}
	}

	/**
	 * 查找事务历史表的最大流水号，以插入新的事务记录
	 * @return Long 最大流水号
	 * @throws Exception
	 */
	public Long getMaxId(){
		return (Long) bll.getMaxId("INV_J_TRANSACTION_HIS", "TRANS_HIS_ID");
	}
	/**
	 * 条件查找事务历史表中最近的一条记录，获得其流水号（is_use='Y' and 企业编码 = enterpriseCode）
	 * @param enterpriseCode 企业编码
	 * @return 流水号
	 */
	public Long getLatestId(String enterpriseCode) {
		String sql = 
		"SELECT\n" + 
		    "MAX(A.TRANS_HIS_ID)\n" + 
		"FROM\n" + 
		    "INV_J_TRANSACTION_HIS A\n" + 
		"WHERE\n" + 
		    "A.IS_USE = 'Y' AND\n" + 
		    "A.ENTERPRISE_CODE = '" + enterpriseCode + "' AND\n" +
		    "A.TRANS_ID != 0";
		Object obj = bll.getSingal(sql);
		if (null != obj) {
			return Long.parseLong(obj.toString());
		} else {
			return null;
		}
	}
	
}