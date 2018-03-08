package power.ejb.run.runlog;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class RunCMainItemFacade implements RunCMainItemFacadeRemote {
	public static final String IS_USE = "isUse";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCMainItem save(RunCMainItem entity) throws CodeRepeatException {
		try {
			if (!this.checkCodeSame(entity.getEnterpriseCode(), entity.getMainItemCode())) {

				entity.setItemId(bll.getMaxId("run_c_main_item", "item_id"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				return entity;
			} else {
				throw new CodeRepeatException("编码不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCMainItem update(RunCMainItem entity) throws CodeRepeatException {
		try {
			if (!this.checkCodeSame(entity.getEnterpriseCode(), entity.getMainItemCode(), entity.getItemId()))
			{
			RunCMainItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else
			{
				throw new CodeRepeatException("编码不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(RunCMainItem entity) {
		LogUtil.log("deleting RunCMainItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCMainItem.class, entity
					.getItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCMainItem findById(Long id) {
		LogUtil.log("finding RunCMainItem instance with id: " + id, Level.INFO,
				null);
		try {
			RunCMainItem instance = entityManager.find(RunCMainItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCMainItem> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCMainItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCMainItem model where model."
					+ propertyName + "= :propertyValue order by diaplay_no";
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

	@SuppressWarnings("unchecked")
	public List<RunCMainItem> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCMainItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCMainItem model";
			Query query = entityManager.createQuery(queryString);
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
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据编码查找对象
	 * @param mainItemCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return RunCMainItem
	 */
	@SuppressWarnings("unchecked")
	public RunCMainItem findListByCode(String mainItemCode,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCMainItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCMainItem model where model.isUse = 'Y' and model.mainItemCode='"
					+ mainItemCode
					+ "' and model.enterpriseCode = '"
					+ enterpriseCode + "'";
			Query query = entityManager.createQuery(queryString);
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
			return (RunCMainItem)query.getResultList().get(0);
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunCMainItem> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	public PageObject findMainItemList(String fuzzy, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from run_c_main_item t\n"
					+ "where (t.main_item_name like '%" + fuzzy + "%'\n"
					+ "or t.main_item_code like '%" + fuzzy + "%')\n"
					+ "and t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'\n" + "order by diaplay_no";

			List<RunCMainItem> list = bll.queryByNativeSQL(sql,
					RunCMainItem.class, rowStartIdxAndCount);
			String sqlCount = "select count(*) from run_c_main_item t\n"
					+ "where t.main_item_name like '%" + fuzzy + "%'\n"
					+ "and t.main_item_code like '%" + fuzzy + "%'\n"
					+ "and t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'\n" + "order by diaplay_no";

			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据专业编码判断是否已经存在
	 * 
	 * @param code
	 * @param enterpriseCode
	 * @return
	 */
	public boolean existsByCode(String code, String enterpriseCode) {
		String strSql = "select count(1)\n" + "  from run_c_main_item t\n"
				+ " where t.main_item_code = '" + code + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";

		Object obj = bll.getSingal(strSql);
		int count = 0;
		count = Integer.parseInt(obj.toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	

	private boolean checkCodeSame(String enterpriseCode,String itemCode,Long... itemId) 
	{
		boolean isSame = false;
		String sql ="select count(1)\n" +
			"       from run_c_main_item s\n" + 
			"       where s.is_use = 'Y'\n" + 
			"       and s.enterprise_code = '"+enterpriseCode+"'\n" + 
			"       and s.main_item_code = '"+itemCode+"'";

	    if(itemId !=null&& itemId.length>0){
	    	sql += "  and s.item_id <> " + itemId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}