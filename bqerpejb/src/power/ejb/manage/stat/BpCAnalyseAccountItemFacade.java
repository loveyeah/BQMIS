package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity BpCAnalyseAccountItem.
 * 
 * @see power.ejb.manage.stat.BpCAnalyseAccountItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCAnalyseAccountItemFacade implements
		BpCAnalyseAccountItemFacadeRemote {
	// property constants
	public static final String DISPLAY_NO = "displayNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved BpCAnalyseAccountItem
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCAnalyseAccountItem entity) {
		LogUtil.log("saving BpCAnalyseAccountItem instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCAnalyseAccountItem> addList) {
		if (addList != null && addList.size() > 0) {

			for (BpCAnalyseAccountItem entity : addList) {

				this.save(entity);
			}
		}
	}

	public Long isNew(String accountCode, String itemCode) {
		Long count;
		String sql = "select count(*) from bp_c_analyse_account_item t "
				+ " where t.account_Code='" + accountCode + "'"
				+ " and t.item_Code='" + itemCode + "'";
		count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

	/**
	 * Delete a persistent BpCAnalyseAccountItem entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCAnalyseAccountItem entity) {
		LogUtil
				.log("deleting BpCAnalyseAccountItem instance", Level.INFO,
						null);
		try {
			entity = entityManager.getReference(BpCAnalyseAccountItem.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean deleteAccountItem(Long accountCode, String enterpriseCode) {
		try {
			String sql = "delete from bp_c_analyse_account_item t"
					+ " where t.account_code=" + accountCode + " "
					+ " and t.enterprise_code='" + enterpriseCode + "'";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException e) {
			throw e;
			// return false;
		}

	}

	public boolean delete(String ids) {
		try {

			String[] temp1 = ids.split(";");

			for (String i : temp1) {
				String[] temp2 = i.split(",");
				BpCAnalyseAccountItemId id = new BpCAnalyseAccountItemId();
				id.setAccountCode(Long.parseLong(temp2[0]));
				id.setItemCode(temp2[1]);
				this.delete(this.findById(id));
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved BpCAnalyseAccountItem entity and return it or
	 * a copy of it to the sender. A copy of the BpCAnalyseAccountItem entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountItem entity to update
	 * @return BpCAnalyseAccountItem the persisted BpCAnalyseAccountItem entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCAnalyseAccountItem update(BpCAnalyseAccountItem entity) {
		LogUtil
				.log("updating BpCAnalyseAccountItem instance", Level.INFO,
						null);
		try {
			BpCAnalyseAccountItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCAnalyseAccountItem> updateList) {
		if (updateList != null && updateList.size() > 0) {

			for (BpCAnalyseAccountItem entity : updateList) {

				this.update(entity);
			}
		}
	}

	public BpCAnalyseAccountItem findById(BpCAnalyseAccountItemId id) {
		LogUtil.log("finding BpCAnalyseAccountItem instance with id: " + id,
				Level.INFO, null);
		try {
			BpCAnalyseAccountItem instance = entityManager.find(
					BpCAnalyseAccountItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCAnalyseAccountItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCAnalyseAccountItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCAnalyseAccountItem> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCAnalyseAccountItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCAnalyseAccountItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCAnalyseAccountItem model where model."
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
	 * Find all BpCAnalyseAccountItem entities.
	 * 
	 * @return List<BpCAnalyseAccountItem> all BpCAnalyseAccountItem entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String accountCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,s.item_name itemName\n"
					+ " FROM bp_c_analyse_account_item t"
					+ ",bp_c_stat_item s \n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode + "'"
					+ "    and  t.account_code = '" + accountCode + "'"
					+ "    and  t.item_code = s.item_code" + "\n"
					+ " ORDER BY t.display_no \n"; 
			String sqlCount = "SELECT count(*)\n"
					+ " FROM bp_c_analyse_account_item t"
					+ ",bp_c_stat_item s \n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode + "'"
					+ "    and  t.account_code = '" + accountCode + "'"
					+ "    and  t.item_code = s.item_code" + "\n";
					 
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);

			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {

				Object[] data = (Object[]) it.next();

				BpCAnalyseAccountItemAdd model = new BpCAnalyseAccountItemAdd();
				BpCAnalyseAccountItem baseInfo = new BpCAnalyseAccountItem();
				BpCAnalyseAccountItemId id = new BpCAnalyseAccountItemId();
				if (data[0] != null) {
					id.setAccountCode(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					id.setItemCode(data[1].toString());
				}

				baseInfo.setId(id);
				if (data[2] != null) {
					baseInfo.setDisplayNo(Long.parseLong(data[2].toString()));
				}
				if (data[4] != null) {
					baseInfo.setItemAlias(data[4].toString());
				}
				if (data[5] != null) {
					baseInfo.setItemBaseName(data[5].toString());
				}
				if (data[6] != null) {
					baseInfo.setDataType(data[6].toString());
				}
				if (data[7] != null) {
					model.setItemName(data[7].toString());
				}
				model.setBaseInfo(baseInfo);

				arrlist.add(model);
			}
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(count);

			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	// public Long checkAccountCode(String acconutCode) {
	// String sql = "select count(1) from BP_C_ANALYSE_ACCOUNT t"
	// + " where t.account_code=" + acconutCode;
	// Long count = Long.valueOf(dll.getSingal(sql).toString());
	// return count;
	// }

}