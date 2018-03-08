package power.ejb.productiontec.report;

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
import power.ejb.productiontec.report.form.PtJJdbhjd1Form;

/**
 * Facade for entity PtJJdbhjd1.
 * 
 * @see power.ejb.productiontec.report.PtJJdbhjd1
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJJdbhjd1Facade implements PtJJdbhjd1FacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJJdbhjd1 entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdbhjd1 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdbhjd1 entity) {
		LogUtil.log("saving PtJJdbhjd1 instance", Level.INFO, null);
		try {
			entity.setJdbhjd1Id(bll.getMaxId("PT_J_JDBHJD1", "JDBHJD1_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJJdbhjd1 entity.
	 * 
	 * @param entity
	 *            PtJJdbhjd1 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJJdbhjd1 entity) {
		LogUtil.log("deleting PtJJdbhjd1 instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJJdbhjd1.class, entity
					.getJdbhjd1Id());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJJdbhjd1 entity and return it or a copy of
	 * it to the sender. A copy of the PtJJdbhjd1 entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdbhjd1 entity to update
	 * @return PtJJdbhjd1 the persisted PtJJdbhjd1 entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdbhjd1 update(PtJJdbhjd1 entity) {
		LogUtil.log("updating PtJJdbhjd1 instance", Level.INFO, null);
		try {
			PtJJdbhjd1 result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdbhjd1 findById(Long id) {
		LogUtil.log("finding PtJJdbhjd1 instance with id: " + id, Level.INFO,
				null);
		try {
			PtJJdbhjd1 instance = entityManager.find(PtJJdbhjd1.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJJdbhjd1 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdbhjd1 property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdbhjd1> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJJdbhjd1> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJJdbhjd1 instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdbhjd1 model where model."
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
	 * Find all PtJJdbhjd1 entities.
	 * 
	 * @return List<PtJJdbhjd1> all PtJJdbhjd1 entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJJdbhjd1> findAll() {
		LogUtil.log("finding all PtJJdbhjd1 instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdbhjd1 model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findAllByMonthAndFlag(String month, String tabelFlag,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.jdbhjd1_id, \n"
			+ "a.str_month, \n"
			+ "a.count_type, \n"
			+ "a.dynamo_num, \n"
			+ "a.transformer_num, \n"
			+ "a.fbz_protect_num, \n"
			+ "a.factory_proctect_num, \n"
			+ "a.block_num, \n"
			+ "a.engineer, \n"
			+ "a.equ_dept, \n"
			+ "a.entry_by, \n"
			+ "a.table_flag, \n"
			+ "getworkername(a.engineer), \n"
			+ "getworkername(a.equ_dept), \n"
			+ "getworkername(a.entry_by), \n"
			+ "to_char(a.entry_date,'yyyy-MM-dd') \n"
			+ "from PT_J_JDBHJD1 a \n"
			+ "where a.enterprise_code='" + enterpriseCode + "' \n";
		if(month != null && !month.equals(""))
			sql += "and a.str_month='" + month + "' \n";
		if(tabelFlag != null && !tabelFlag.equals(""))
			sql += "and a.table_flag='" + tabelFlag + "' \n";
		sql += "order by a.count_type \n";
//		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PtJJdbhjd1Form> arrlist = new ArrayList<PtJJdbhjd1Form>(3);
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				Object[] da = (Object[])it.next();
				PtJJdbhjd1Form form = new PtJJdbhjd1Form();
				if(da[0] != null)
					form.setJdbhjd1Id(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setStrMonth(da[1].toString());
				if(da[2] != null)
					form.setCountType(da[2].toString());
				if(da[3] != null)
					form.setDynamoNum(Double.parseDouble(da[3].toString()));
				if(da[4] != null)
					form.setTransformerNum(Double.parseDouble(da[4].toString()));
				if(da[5] != null)
					form.setFbzProtectNum(Double.parseDouble(da[5].toString()));
				if(da[6] != null)
					form.setFactoryProctectNum(Double.parseDouble(da[6].toString()));
				if(da[7] != null)
					form.setBlockNum(Double.parseDouble(da[7].toString()));			
				if(da[8] != null)
					form.setEngineer(da[8].toString());
				if(da[9] != null)
					form.setEquDept(da[9].toString());
				if(da[10] != null)
					form.setEntryBy(da[10].toString());
				if(da[11] != null)
					form.setTableFlag(da[11].toString());
				if(da[12] != null)
					form.setEngineerName(da[12].toString());
				if(da[13] != null)
					form.setEquDeptName(da[13].toString());
				if(da[14] != null)
					form.setEntryByName(da[14].toString());
				if(da[15] != null)
					form.setEntryDateString(da[15].toString());
				if(form.getCountType().equals("1"))
					form.setTypeName("安装数量");
				if(form.getCountType().equals("2"))
					form.setTypeName("计划校验数量");
				if(form.getCountType().equals("3"))
					form.setTypeName("实际校验数量");
				if(form.getCountType().equals("4"))
					form.setTypeName("总次数");
				if(form.getCountType().equals("5"))
					form.setTypeName("正确动作次数");
				if(form.getCountType().equals("6"))
					form.setTypeName("正确动作率");
				
				arrlist.add(form);
			}
		}
		else
		{
			arrlist.add(new PtJJdbhjd1Form());
			arrlist.add(new PtJJdbhjd1Form());
			arrlist.add(new PtJJdbhjd1Form());
			if(tabelFlag.equals("1"))
			{
				arrlist.get(0).setCountType("1");
				arrlist.get(1).setCountType("2");
				arrlist.get(2).setCountType("3");
				arrlist.get(0).setTypeName("安装数量");
				arrlist.get(1).setTypeName("计划校验数量");
				arrlist.get(2).setTypeName("实际校验数量");
			}
			else if(tabelFlag.equals("3"))
			{
				arrlist.get(0).setCountType("4");
				arrlist.get(1).setCountType("5");
				arrlist.get(2).setCountType("6");
				arrlist.get(0).setTypeName("总次数");
				arrlist.get(1).setTypeName("正确动作次数");
				arrlist.get(2).setTypeName("正确动作率");
			}
		}
		pg.setList(arrlist);
//		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(3L);
		return pg;
	}

	public void saveModiRec(List<PtJJdbhjd1> addList,
			List<PtJJdbhjd1> updateList) {
		if(addList != null && addList.size() > 0)
		{
			for(PtJJdbhjd1 entity : addList)
			{
				this.save(entity);
				entityManager.flush();
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(PtJJdbhjd1 entity : updateList)
			{
				this.update(entity);
			}
		}
	}
}