/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.ExchangeRateInfo;

/**
 * Facade for entity SysCExchangeRate.
 * 
 * @see power.ejb.resource.SysCExchangeRate
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysCExchangeRateFacade implements SysCExchangeRateFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager; 
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved SysCExchangeRate entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCExchangeRate entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCExchangeRate entity) {
		// 修改时间
		entity.setLastModifiedDate(new java.util.Date());
		// 流水号的取得
		Long exchangeRateId = bll.getMaxId("sys_c_exchange_rate", "EXCHANGE_RATE_ID");
		entity.setExchangeRateId(exchangeRateId);
		LogUtil.log("saving SysCExchangeRate instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SysCExchangeRate entity.
	 * 
	 * @param entity
	 *            SysCExchangeRate entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCExchangeRate entity) {
		LogUtil.log("deleting SysCExchangeRate instance", Level.INFO, null);
		try {
			// 通过流水号找到实体，然后进行逻辑删除
			SysCExchangeRate newEntity = this.findById(entity.getExchangeRateId());
			if (entity != null) {
				newEntity.setIsUse("N");
				newEntity.setLastModifiedBy(entity.getLastModifiedBy());
				newEntity.setLastModifiedDate(new java.util.Date());
				SysCExchangeRate result = entityManager.merge(newEntity);
				LogUtil.log("delete successful", Level.INFO, null);			
			}
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SysCExchangeRate entity and return it or a
	 * copy of it to the sender. A copy of the SysCExchangeRate entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SysCExchangeRate entity to update
	 * @return SysCExchangeRate the persisted SysCExchangeRate entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCExchangeRate update(SysCExchangeRate entity) {
		// 修改时间
		entity.setLastModifiedDate(new java.util.Date());
		LogUtil.log("updating SysCExchaneRate instance", Level.INFO, null);
		try {
			SysCExchangeRate result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCExchangeRate findById(Long id) {
		LogUtil.log("finding SysCExchangeRate instance with id: " + id,
				Level.INFO, null);
		try {
			SysCExchangeRate instance = entityManager.find(
					SysCExchangeRate.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SysCExchangeRate entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCExchangeRate property to query
	 * @param value
	 *            the property value to match
	 * @return List<SysCExchangeRate> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SysCExchangeRate> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SysCExchangeRate instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SysCExchangeRate model where model."
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
	 * Find all SysCExchangeRate entities.
	 * 
	 * @return List<SysCExchangeRate> all SysCExchangeRate entities
	 */
	@SuppressWarnings("unchecked")
	public List<SysCExchangeRate> findAll() {
		LogUtil.log("finding all SysCExchangeRate instances", Level.INFO, null);
		try {
			final String queryString = "select model from SysCExchangeRate model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 获取全部汇率信息列表
	 */
	public PageObject getExchangeRateList(String enterpriseCode, int... rowStartIdxAndCount) {
		try{
			PageObject result = new PageObject();
			String sql = "select\n" + "    t.ORI_CURRENCY_ID,\n"
			     + "    t.DST_CURRENCY_ID,\n"
			     + "    t.RATE,\n"
			     + "    t.EFFECTIVE_DATE,\n"
			     + "    t.DISCONTINUE_DATE,\n"
			     + "    t.EXCHANGE_RATE_ID\n"
			     + "from sys_c_exchange_rate t \n"
			     + "where t.is_use = 'Y'\n"
			     + " and t.enterprise_code = '" + enterpriseCode + "'\n"
                 + "order by t.ori_currency_id,t.dst_currency_id \n";
			String sqlCount = "select count('EXCHANGE_RATE_ID')  from sys_c_exchange_rate t\n"
				 + "where t.is_use = 'Y'\n"
                 + " and t.enterprise_code = '" + enterpriseCode + "'\n"
                 + "order by t.ori_currency_id,t.dst_currency_id \n";
			List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
			// 汇率相关信息列表
			List<ExchangeRateInfo> arrlist = new ArrayList<ExchangeRateInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				ExchangeRateInfo rateInfo = new ExchangeRateInfo();				
				Object[] data = (Object[]) it.next();
				String oriIdStr = data[0].toString();
				String dstIdStr = data[1].toString();
				String oriCurrencyName = findNameById(oriIdStr);
 				String dstCurrencyName = findNameById(dstIdStr);
 				rateInfo.setOriCurrencyId(oriIdStr);
 				rateInfo.setDstCurrencyId(dstIdStr); 				
 				rateInfo.setOriCurrencyName(oriCurrencyName);
				rateInfo.setDstCurrencyName(dstCurrencyName);
				rateInfo.setRate(data[2].toString());
				rateInfo.setEffectiveDate(data[3].toString());
				rateInfo.setDiscontinueDate(data[4].toString());
				rateInfo.setExchangeRateId(data[5].toString());
				// 如果相应的货币种类已被逻辑删除，则将id设为空
				if(oriCurrencyName == "") {
					rateInfo.setOriCurrencyId("");
				}
				if(dstCurrencyName == "") {
					rateInfo.setDstCurrencyId("");
				}
				arrlist.add(rateInfo);
			}
			if(arrlist.size()>0)
			{
				result.setList(arrlist);
				result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			}	
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}		
	}
	/**
	 * 通过货币id找到相应的name
	 */
	private String findNameById(String currencyId) {
		LogUtil.log("finding SysCCurrency instance with No: " + currencyId, Level.INFO, null);
		String sql = "select\n" + "    currency_name\n"
	     + "    from sys_c_currency\n"
	     + "where currency_id='" + currencyId + "'\n"
	     + "    and is_use='Y'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		if(it.hasNext()) { 
		 	Object data = (Object) it.next();
			return data.toString();
		}
		// 当相应的货币种类已被逻辑删除
		return "";
	}
	/**
	 * 获得货币名称列表
	 */
	public PageObject getCurrencyNameList(){
		PageObject result = new PageObject();
		String sql=
			"select *\n" +
			"  from sys_c_currency t\n" + 
			"where t.is_use = 'Y'\n";
		List<SysCCurrency> list = bll.queryByNativeSQL(sql,SysCCurrency.class);
		result.setList(list);
		return result;
	}
	/**
	 * 判断相同的基准货币和兑换货币,在相同时段内有无不同汇率。
	 */
	public PageObject isDateExist(SysCExchangeRate entity){
		PageObject result = new PageObject();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");         
		// 基准币别
		String oriCurrencyIdStr = entity.getOriCurrencyId().toString();
	    // 比较币别
		String dstCurrencyIdStr = entity.getDstCurrencyId().toString();
		// 有效开始时间
		String effectiveDate = format.format(entity.getEffectiveDate());		
		// 有效截止时间
		String discontinueDate = format.format(entity.getDiscontinueDate());
		// 企业编码
		String enterpriseCode = entity.getEnterpriseCode();
		String sql = "select t.EXCHANGE_RATE_ID from sys_c_exchange_rate t\n"
			 + "where is_use = 'Y'\n"
		     + " and t.enterprise_code = '" + enterpriseCode + "'\n"
			 + " and t.ori_currency_id = " + oriCurrencyIdStr
			 + " and t.dst_currency_id = " + dstCurrencyIdStr
			 + " and ((t.effective_date <= to_date('" + effectiveDate + "','yyyy-MM-dd') \n"
			 + " and t.discontinue_date >= to_date('" + effectiveDate + "','yyyy-MM-dd')) \n"
			 + " or (t.effective_date <= to_date('" + discontinueDate + "','yyyy-MM-dd') \n"
			 + " and t.discontinue_date >= to_date('" + discontinueDate + "','yyyy-MM-dd')) \n"
		     + " or (t.effective_date >= to_date('" + effectiveDate + "','yyyy-MM-dd') \n"	
		     + " and t.discontinue_date <= to_date('" + discontinueDate + "','yyyy-MM-dd'))) \n";
		
		String sqlCount = "select count('EXCHANGE_RATE_ID')  from sys_c_exchange_rate t\n"
			 + "where is_use = 'Y'\n"
		     + " and t.enterprise_code = '" + enterpriseCode + "'\n"
			 + " and t.ori_currency_id = " + oriCurrencyIdStr
			 + " and t.dst_currency_id = " + dstCurrencyIdStr
			 + " and ((t.effective_date <= to_date('" + effectiveDate + "','yyyy-MM-dd') \n"
			 + " and t.discontinue_date >= to_date('" + effectiveDate + "','yyyy-MM-dd')) \n"
			 + " or (t.effective_date <= to_date('" + discontinueDate + "','yyyy-MM-dd') \n"
			 + " and t.discontinue_date >= to_date('" + discontinueDate + "','yyyy-MM-dd')) \n"
		     + " or (t.effective_date >= to_date('" + effectiveDate + "','yyyy-MM-dd') \n"	
		     + " and t.discontinue_date <= to_date('" + discontinueDate + "','yyyy-MM-dd'))) \n";
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setTotalCount(count);
		String oldIdStr = "";
		if(entity.getExchangeRateId() != null) {
			oldIdStr = entity.getExchangeRateId().toString();
		}		 
		// 判断修改时是否重复
		if(count == 1 && oldIdStr != "") {
			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			String idStr = "";
			if(it.hasNext()) { 
			 	Object data = (Object) it.next();
			 	idStr = data.toString();
			 	if(idStr.equals(oldIdStr)) {
			 		result.setTotalCount(0L);
			 	}
			}
		}
		return result;
	}
}