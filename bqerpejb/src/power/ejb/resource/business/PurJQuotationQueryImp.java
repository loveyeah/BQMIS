/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource.business;

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
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.PurJQuotation;
import power.ejb.resource.SysCCurrency;
import power.ejb.resource.form.PurJQuotationInfo;

/**
 * Facade for entity PurJQuotation.
 * 
 * @see power.ejb.resource.PurJQuotation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurJQuotationQueryImp implements PurJQuotationQuery {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 单位*/
	@EJB(beanName = "BpCMeasureUnitFacade")
	BpCMeasureUnitFacadeRemote unitRemote;

	/**
	 * Perform an initial save of a previously unsaved PurJQuotation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PurJQuotation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Boolean save(PurJQuotation entity) throws CodeRepeatException {
		LogUtil.log("saving PurJQuotation instance", Level.INFO, null);
		try {
			if (entity.getQuotationId() == null) {
				entity.setQuotationId(bll.getMaxId("pur_j_quotation",
						"quotation_id"));
			}
			if (isValid(entity)) {
				entity.setLastModifiedDate(new Date());
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return true;
			} else {
				throw new CodeRepeatException("供应商的同一种物料在相同时段内不能有不同报价。");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PurJQuotation entity.
	 * 
	 * @param entity
	 *            PurJQuotation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long quotationId, String strEmployee) {
		String sql = "update pur_j_quotation t \n" 
			    + "set t.last_modified_by = '"+ strEmployee + "' \n" 
				+ ",   t.last_modified_date = sysdate \n"
				+ ",   t.is_use = 'N' \n" 
				+ "where t.quotation_id ="+ quotationId;
		
		bll.exeNativeSQL(sql);
	}

	/**
	 * Persist a previously saved PurJQuotation entity and return it or a copy
	 * of it to the sender. A copy of the PurJQuotation entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PurJQuotation entity to update
	 * @return PurJQuotation the persisted PurJQuotation entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PurJQuotation update(PurJQuotation entity) {
		LogUtil.log("updating PurJQuotation instance", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new Date());
			PurJQuotation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * find data by id 
	 * @param id
	 * @return
	 */
	public PurJQuotation findById(Long id) {
		LogUtil.log("finding PurJQuotation instance with id: " + id,
				Level.INFO, null);
		try {
			PurJQuotation instance = entityManager
					.find(PurJQuotation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find PurJQuotation entities.
	 * 
	 * @param fuzzy
	 *            the name of the PurJQuotation property to query
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDetail(String enterpriseCode, String fuzzy,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql = "select a.quotation_id, \n"
				    + "       b.material_id, \n" 
				    + "       a.unit_id, \n" 
					+ "       b.material_no, \n" 
					+ "       b.material_name, \n"
					+ "       b.spec_no, \n" 
					+ "       b.parameter, \n"
					+ "       a.supplier, \n" 
					+ "       c.client_name, \n"
					+ "       a.quoted_qty, \n" 
					+ "       a.quoted_price, \n"
					+ "       d.currency_name, \n"
					+ "       a.effective_date, \n"
					+ "       a.discontinue_date, \n" 
					+ "       a.memo, \n"
					+ "       a.quotation_currency, \n"
					+ "       a.last_modified_date, \n"
					+ "       c.client_code \n"
					+ " from                    \n"
					+ "       pur_j_quotation a, \n"
					+ "       inv_c_material b, \n"
					+ "       con_j_client_info c, \n"
					+ "       sys_c_currency d  \n"
					+ " where a.material_id = b.material_id \n"
					+ " and   a.supplier = c.cliend_id(+) \n"
					+ " and   a.quotation_currency = d.currency_id(+) \n"
					+ " and   a.is_use = 'Y' \n" 
					+ " and   b.is_use = 'Y' \n"
					+ " and   c.is_use(+) = 'Y' \n" 
					+ " and   d.is_use(+) = 'Y' \n"
					+ " and   a.enterprise_code = '"+enterpriseCode+"' \n"
					+ " and   b.enterprise_code = '"+enterpriseCode+"' \n"
					+ " and   c.enterprise_code(+) = '"+enterpriseCode+"' \n"
					+ " and   d.enterprise_code(+) = '"+enterpriseCode+"' \n";

			String sqlCount = "select count(a.quotation_id) \n"
				+ " from                    \n"
				+ "       pur_j_quotation a, \n"
				+ "       inv_c_material b, \n"
				+ "       con_j_client_info c, \n"
				+ "       sys_c_currency d  \n"
				+ " where a.material_id = b.material_id \n"
				+ " and   a.supplier = c.cliend_id(+) \n"
				+ " and   a.quotation_currency = d.currency_id(+) \n"
				+ " and   a.is_use = 'Y' \n" 
				+ " and   b.is_use = 'Y' \n"
				+ " and   c.is_use(+) = 'Y' \n" 
				+ " and   d.is_use(+) = 'Y' \n"
				+ " and   a.enterprise_code = '"+enterpriseCode+"' \n"
				+ " and   b.enterprise_code = '"+enterpriseCode+"' \n"
				+ " and   c.enterprise_code(+) = '"+enterpriseCode+"' \n"
				+ " and   d.enterprise_code(+) = '"+enterpriseCode+"' \n";

			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and   (c.client_code like \'%" + fuzzy
						+ "%\' \n" + " or   b.material_no like \'%" + fuzzy
						+ "%\' \n" + " or   b.material_name like \'%" + fuzzy
						+ "%\' \n" + " or   c.client_name like \'%" + fuzzy
						+ "%\') \n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			sql += "order by b.material_no";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<PurJQuotationInfo> arrlist = new ArrayList();

			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					PurJQuotationInfo model = new PurJQuotationInfo();
					Object[] data = (Object[]) it.next();
					model.setQuotationId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						model.setMaterialId(Long.parseLong(data[1].toString()));
					if (data[2] != null) {
						model.setUnitId(Long.parseLong(data[2].toString()));
						// 单位名称
						BpCMeasureUnit unit = unitRemote.findById(model
								.getUnitId());
						if (unit == null || unit.getUnitName() == null
								|| unit.getUnitName().trim() == "") {
							model.setUnitName("");
						} else {
							model.setUnitName(unit.getUnitName());
						}
					}
					if (data[3] != null)
						model.setMaterialNo((data[3].toString()));
					if (data[4] != null)
						model.setMaterialName(data[4].toString());
					if (data[5] != null)
						model.setSpecNo(data[5].toString());
					if (data[6] != null)
						model.setParameter(data[6].toString());
					if (data[7] != null)
						model.setSupplier(Long.parseLong(data[7].toString()));
					if (data[8] != null)
						model.setSupplyName(data[8].toString());
					if (data[9] != null)
						model.setQuotedQty(Double.parseDouble(data[9].toString()));
					if (data[10] != null)
						model.setQuotedPrice(Double.parseDouble(data[10].toString()));
					if (data[11] != null)
						model.setCurrencyName(data[11].toString());
					if (data[12] != null)
						model.setEffectiveDate(data[12].toString());
					if (data[13] != null)
						model.setDiscontinueDate(data[13].toString());
					if (data[14] != null)
						model.setMemo(data[14].toString());
					if (data[15] != null)
						model.setQuotationCurrency(Long.parseLong(data[15].toString()));
					if (data[16] instanceof Date) {
						PurJQuotation test = this.findById(model.getQuotationId());
						model.setlastModifiedDate(test.getLastModifiedDate().getTime());
					}
					if (data[17] != null)
						model.setclientCode(data[17].toString());
					arrlist.add(model);
				}
				if (arrlist.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
							.toString());
					result.setList(arrlist);
					result.setTotalCount(totalCount);
				}
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * Find All Currency ID and Name.
	 * 
	 * @param enterpriseCode
	 * 
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllCurrency(String enterpriseCode) {
		PageObject result = new PageObject();
		String sql = "select a.currency_id, \n" 
			    + "       a.currency_name \n"
				+ " from                    \n" 
				+ "       sys_c_currency a \n"
				+ " where a.is_use = 'Y' \n" 
				+ " and   a.enterprise_code = '"+enterpriseCode+"' \n"
				+ "order by a.currency_id";

		String sqlCount = "select count(a.currency_id) \n"
				+ " from                    \n" 
				+ "       sys_c_currency a \n"
				+ " where a.is_use = 'Y' \n"
				+ " and   a.enterprise_code = '"+enterpriseCode+"' \n";

		List list = bll.queryByNativeSQL(sql);
		List<SysCCurrency> arrlist = new ArrayList();

		if (list != null && list.size() > 0) {
			result = new PageObject();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SysCCurrency model = new SysCCurrency();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					model.setCurrencyId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setCurrencyName(data[1].toString());
				arrlist.add(model);
			}
			if (arrlist.size() > 0) {
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arrlist);
				result.setTotalCount(totalCount);
			}
		}
		return result;
	}
	
	/**
	 * check whether the date is repeat
	 * @param entity
	 * 
	 * @return flag
	 *  false sign repeat
	 */
	@SuppressWarnings("unchecked")
	public Boolean isValid(PurJQuotation entity) {
		Boolean flag = false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(entity.getEffectiveDate());
		String endDate = sdf.format(entity.getDiscontinueDate());
		String sqlCount = "select a.quotation_id \n"
				+ " from                    \n" 
				+ "       pur_j_quotation a \n"
				+ " where a.material_id = " + entity.getMaterialId() + "\n"
				+ " and   a.supplier = " + entity.getSupplier() + "\n"
				+ " and   a.unit_id = " + entity.getUnitId() + "\n"
				+ " and   a.quotation_currency = " + entity.getQuotationCurrency() + "\n"
				+ " and   a.quoted_qty = " + entity.getQuotedQty() + "\n"
				+ " and   a.quoted_price = " + entity.getQuotedPrice() + "\n"
				+ " and   a.is_use = 'Y' \n" 
				+ " and   a.enterprise_code = '"+ entity.getEnterpriseCode() + "' \n"
				+ " and  ( (a.effective_date <= to_date('"
				+ startDate
				+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
				+ " and   a.discontinue_date >= to_date('"
				+ startDate
				+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss') ) \n"
				+ " or   (a.effective_date <= to_date('"
				+ endDate
				+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
				+ " and   a.discontinue_date >= to_date('"
				+ endDate
				+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss') ) )\n";
		
		List list = bll.queryByNativeSQL(sqlCount);
		if(list.size() == 0) {
			flag = true;
		} else if(list.size() == 1) {
			if(list.toString().equals("["+entity.getQuotationId().toString()+"]")) {
				flag = true;
			}
		} 

		return flag;
	}
}