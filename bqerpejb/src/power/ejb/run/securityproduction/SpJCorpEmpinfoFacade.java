package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.run.securityproduction.form.SpJEmpForm;

/**
 * 发电企业人员表
 */
@Stateless
public class SpJCorpEmpinfoFacade implements SpJCorpEmpinfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条发电企业人员
	 */
	public void save(SpJCorpEmpinfo entity) {
		LogUtil.log("saving SpJCorpEmpinfo instance", Level.INFO, null);
		try {
			entity.setEmpId(bll.getMaxId("sp_j_corp_empinfo", "emp_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条发电企业人员
	 */
	public void delete(SpJCorpEmpinfo entity) {
		LogUtil.log("deleting SpJCorpEmpinfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJCorpEmpinfo.class, entity
					.getEmpId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 删除一条或多条发电企业人员
	 */
	public void delete(String ids)
	{
		String sql = "update sp_j_corp_empinfo a set a.is_use = 'N'"
			+ "where a.emp_id in (" + ids + ") ";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条人员
	 */
	public SpJCorpEmpinfo update(SpJCorpEmpinfo entity) {
		LogUtil.log("updating SpJCorpEmpinfo instance", Level.INFO, null);
		try {
			SpJCorpEmpinfo result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJCorpEmpinfo findById(Long id) {
		LogUtil.log("finding SpJCorpEmpinfo instance with id: " + id,
				Level.INFO, null);
		try {
			SpJCorpEmpinfo instance = entityManager.find(SpJCorpEmpinfo.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findEmpInfoList(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.emp_id, \n"
			 	+ "a.emp_name, \n" 
			 	+ "a.emp_duty, \n"
			 	+ "a.modifyby, \n"
			 	+ "getworkername(a.modifyby), \n"
			 	+ "to_char(a.modify_date,'yyyy-MM-dd') \n"
			 	+ "from sp_j_corp_empinfo a  \n"
			 	+ "where a.is_use='Y' \n"
			 	+ "and a.enterprise_code='" + enterpriseCode + "' \n";
		if(name != null && !name.equals(""))
			sql += " and a.emp_name like '%" + name + "%' \n";
		String sqlCount = "select count(*) from (" + sql + ") ";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<SpJEmpForm> arrlist = new ArrayList<SpJEmpForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				SpJEmpForm form = new SpJEmpForm();
				Object[] data = (Object[])it.next();
				if(data[0] != null)
					form.setEmpId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					form.setEmpName(data[1].toString());
				if(data[2] != null)
					form.setEmpDuty(data[2].toString());
				if(data[3] != null)
					form.setModifyby(data[3].toString());
				if(data[4] != null)
					form.setModifyName(data[4].toString());
				if(data[5] != null)
					form.setModifyDateString(data[5].toString());
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	

}