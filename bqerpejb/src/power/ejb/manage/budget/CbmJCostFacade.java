package power.ejb.manage.budget;

import java.util.ArrayList;
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
import power.ejb.manage.budget.form.CostForm;

/**
 * 成本分析
 * @author liuyi 091019
 */
@Stateless
public class CbmJCostFacade implements CbmJCostFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条成本分析数据
	 */
	public void save(CbmJCost entity) {
		LogUtil.log("saving CbmJCost instance", Level.INFO, null);
		try {
			entity.setCostId(bll.getMaxId("CBM_J_COST", "COST_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条成本分析数据
	 */
	public void delete(CbmJCost entity) {
		LogUtil.log("deleting CbmJCost instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJCost.class, entity
					.getCostId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条成本分析数据
	 */
	public CbmJCost update(CbmJCost entity) {
		LogUtil.log("updating CbmJCost instance", Level.INFO, null);
		try {
			CbmJCost result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJCost findById(Long id) {
		LogUtil.log("finding CbmJCost instance with id: " + id, Level.INFO,
				null);
		try {
			CbmJCost instance = entityManager.find(CbmJCost.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<CbmJCost> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding CbmJCost instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmJCost model where model."
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

	
	@SuppressWarnings("unchecked")
	public List<CbmJCost> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmJCost instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmJCost model";
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

	public PageObject findAllCostRec(String time, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.cost_id, \n"
			+ "a.analyse_date, \n"
			+ "a.item_id, \n"
			+ "a.fact_value, \n"
			+ "a.memo, \n"
			+ "a.is_use, \n"
			+ "b.item_name, \n"
			+ "b.come_from, \n"
			+ "b.item_type \n"
			+ "from CBM_J_COST a,CBM_C_COST_ITEM b \n"
			+ "where a.item_id=b.item_id(+) \n"
			+ "and a.enterprise_code='" + enterpriseCode + "' \n"
			+ "and a.is_use='Y' \n"
			+ "and a.analyse_date='" + time + "' \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<CostForm> arrlist = new ArrayList<CostForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				CostForm form = new CostForm();
				Object[] da = (Object[])it.next();
				if(da[0] != null)
					form.setCostId(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setAnalyseDate(da[1].toString());
				if(da[2] != null)
					form.setItemId(Long.parseLong(da[2].toString()));
				if(da[3] != null)
					form.setFactValue(Double.parseDouble(da[3].toString()));
				else
					form.setFactValue(0.0);
				if(da[4] != null)
					form.setMemo(da[4].toString());
				if(da[5] != null)
					form.setIsUse(da[5].toString());
				if(da[6] != null)
					form.setItemName(da[6].toString());
				if(da[7] != null)
					form.setComeFrom(da[7].toString());
				if(da[8] != null)
					form.setItemType(da[8].toString());
				arrlist.add(form);
			}
		}
		else
		{
			String itemType = "1";
			if(time.length() == 4)
				itemType = "1";
			else if(time.length() == 7)
				itemType = "2";
			else 
				itemType = "3";
			sql = "select a.item_id, \n"
				+ "a.item_name, \n"
				+ "a.come_from, \n"
				+ "a.item_type \n"
				+ "from CBM_C_COST_ITEM a \n"
				+ "where a.item_type='" + itemType + "' \n"
				+ "and a.is_use='Y' \n"
				+ "and a.enterprise_code='" + enterpriseCode + "' \n";
			List alist = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			if(alist != null && alist.size() > 0)
			{
				Iterator it = alist.iterator();
				while(it.hasNext())
				{
					CostForm form = new CostForm();
					Object[] da = (Object[])it.next();
					if(da[0] != null)
						form.setItemId(Long.parseLong(da[0].toString()));
					if(da[1] != null)
						form.setItemName(da[1].toString());
					if(da[2] != null)
						form.setComeFrom(da[2].toString());
					if(da[3] != null)
						form.setItemType(da[3].toString());
					form.setFactValue(0.0);
					form.setAnalyseDate(time);
					arrlist.add(form);
				}
			}
		}
		String sqlCount = "select count(*) from (" + sql + ")\n";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	public void saveAllModRec(List<CbmJCost> addList, List<CbmJCost> updateList) {
		String strDate="";
		if(addList != null && addList.size() > 0)
		{
			strDate=addList.get(0).getAnalyseDate();
			for(CbmJCost entity : addList)
			{
				this.save(entity);
				entityManager.flush();
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			strDate=updateList.get(0).getAnalyseDate();
			for(CbmJCost entity : updateList)
			{
				this.update(entity);
			}
		}
		if(!strDate.equals(""))
		{
			entityManager.flush();
			calculateCostItemValue(strDate);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void calculateCostItemValue(String strDate)
	{
		List<CbmJCost> list = new ArrayList<CbmJCost>();
		String sql=
			"select t.item_id,a.account_order from  CBM_J_COST t,CBM_C_COST_ITEM a\n" +
			"         where t.item_id=a.item_id\n" + 
			"         and t.analyse_date='"+strDate+"'\n" + 
			"         and a.come_from='3'\n" + 
			"         and a.account_order<>1\n" + 
			"         and t.is_use='Y'\n" + 
			"         and a.is_use='Y'\n" + 
			"         order by a.account_order asc";
		List objList = bll.queryByNativeSQL(sql);
		Iterator it = objList.iterator();
		while (it.hasNext()) {
			CbmJCost entity = new CbmJCost();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				entity.setItemId(Long.parseLong(data[0].toString()));
				list.add(entity);
			}

		}
		
		for(CbmJCost itemModel:list)
		{
			String sqlValue = "select GetCostItemValue('"
				+ strDate + "'," + itemModel.getItemId()
				+ ") from dual";
		String itemValue = bll.getSingal(sqlValue).toString();
		String updateSql=
			"update CBM_J_COST t\n" +
			"         set t.fact_value="+itemValue+"\n" + 
			"         where t.analyse_date='"+strDate+"'\n" + 
			"         and t.item_id="+itemModel.getItemId()+"\n" + 
			"         and t.is_use='Y'";
		bll.exeNativeSQL(updateSql);
		entityManager.flush();

		}
	}

}