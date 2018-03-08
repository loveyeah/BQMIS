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
import javax.swing.text.html.FormSubmitEvent;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.report.form.PtJJdbhjd2Form;

/**
 * 大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)
 * @author liuyi 091013
 */
@Stateless
public class PtJJdbhjd2Facade implements PtJJdbhjd2FacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)数据
	 */
	public void save(PtJJdbhjd2 entity) {
		LogUtil.log("saving PtJJdbhjd2 instance", Level.INFO, null);
		try {
			entity.setJdbhjd2Id(bll.getMaxId("PT_J_JDBHJD2", "JDBHJD2_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)数据
	 */
	public void delete(PtJJdbhjd2 entity) {
		LogUtil.log("deleting PtJJdbhjd2 instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJJdbhjd2.class, entity
					.getJdbhjd2Id());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)数据
	 */
	public PtJJdbhjd2 update(PtJJdbhjd2 entity) {
		LogUtil.log("updating PtJJdbhjd2 instance", Level.INFO, null);
		try {
			PtJJdbhjd2 result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdbhjd2 findById(Long id) {
		LogUtil.log("finding PtJJdbhjd2 instance with id: " + id, Level.INFO,
				null);
		try {
			PtJJdbhjd2 instance = entityManager.find(PtJJdbhjd2.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJJdbhjd2> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJJdbhjd2 instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdbhjd2 model where model."
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
	public List<PtJJdbhjd2> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJJdbhjd2 instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdbhjd2 model";
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

	public PageObject findAllByMonthAndFlag(String month, String tabelFlag,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.jdbhjd2_id, \n"
			+ "a.str_month, \n"
			+ "a.count_type, \n"
			+ "a.kv35_num, \n"
			+ "a.kv110_num, \n"
			+ "a.kv220_num, \n"
			+ "a.kv330_num, \n"
			+ "a.mc_protect_num, \n"
			+ "a.safe_device_num, \n"
			+ "a.other_num, \n"
			+ "a.engineer, \n"
			+ "a.equ_dept, \n"
			+ "a.entry_by, \n"
			+ "a.table_flag, \n"
			+ "getworkername(a.engineer), \n"
			+ "getworkername(a.equ_dept), \n"
			+ "getworkername(a.entry_by), \n"
			+ "to_char(a.entry_date,'yyyy-MM-dd') \n"
			+ "from PT_J_JDBHJD2 a \n"
			+ "where a.enterprise_code='" + enterpriseCode + "' \n";
		if(month != null && !month.equals(""))
			sql += "and a.str_month='" + month + "' \n";
		if(tabelFlag != null && !tabelFlag.equals(""))
			sql += "and a.table_flag='" + tabelFlag + "' \n";
		sql += "order by a.count_type \n";
//		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PtJJdbhjd2Form> arrlist = new ArrayList<PtJJdbhjd2Form>(3);
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				Object[] da = (Object[])it.next();
				PtJJdbhjd2Form form = new PtJJdbhjd2Form();
				if(da[0] != null)
					form.setJdbhjd2Id(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setStrMonth(da[1].toString());
				if(da[2] != null)
					form.setCountType(da[2].toString());
				if(da[3] != null)
					form.setKv35Num(Double.parseDouble(da[3].toString()));
				if(da[4] != null)
					form.setKv110Num(Double.parseDouble(da[4].toString()));
				if(da[5] != null)
					form.setKv220Num(Double.parseDouble(da[5].toString()));
				if(da[6] != null)
					form.setKv330Num(Double.parseDouble(da[6].toString()));
				if(da[7] != null)
					form.setMcProtectNum(Double.parseDouble(da[7].toString()));
				if(da[8] != null)
					form.setSafeDeviceNum(Double.parseDouble(da[8].toString()));
				if(da[9] != null)
					form.setOtherNum(Double.parseDouble(da[9].toString()));
				if(da[10] != null)
					form.setEngineer(da[10].toString());
				if(da[11] != null)
					form.setEquDept(da[11].toString());
				if(da[12] != null)
					form.setEntryBy(da[12].toString());
				if(da[13] != null)
					form.setTableFlag(da[13].toString());
				if(da[14] != null)
					form.setEngineerName(da[14].toString());
				if(da[15] != null)
					form.setEquDeptName(da[15].toString());
				if(da[16] != null)
					form.setEntryByName(da[16].toString());
				if(da[17] != null)
					form.setEntryDateString(da[17].toString());
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
			arrlist.add(new PtJJdbhjd2Form());
			arrlist.add(new PtJJdbhjd2Form());
			arrlist.add(new PtJJdbhjd2Form());
			if(tabelFlag.equals("2"))
			{
				arrlist.get(0).setCountType("1");
				arrlist.get(1).setCountType("2");
				arrlist.get(2).setCountType("3");
				arrlist.get(0).setTypeName("安装数量");
				arrlist.get(1).setTypeName("计划校验数量");
				arrlist.get(2).setTypeName("实际校验数量");
			}
			else if(tabelFlag.equals("4"))
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

	public void saveModiRec(List<PtJJdbhjd2> addList,
			List<PtJJdbhjd2> updateList) {
		if(addList != null && addList.size() > 0)
		{
			for(PtJJdbhjd2 entity : addList)
			{
				this.save(entity);
				entityManager.flush();
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(PtJJdbhjd2 entity : updateList)
			{
				this.update(entity);
			}
		}
	}

}