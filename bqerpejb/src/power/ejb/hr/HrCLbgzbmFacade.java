package power.ejb.hr;

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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCLbgzbm.
 *
 * @see power.ejb.hr.HrCLbgzbm
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCLbgzbmFacade implements HrCLbgzbmFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public HrCLbgzbm save(HrCLbgzbm entity) throws CodeRepeatException {
		LogUtil.log("saving HrCLbgzbm instance", Level.INFO, null);
		try {
			if(this.checkNameSame(entity.getLbWorkName()))
			{
				throw new CodeRepeatException("劳保名称不能重复!");
			}
			if(entity.getLbWorkId()==null)
			{
				entity.setLbWorkId(bll.getMaxId("hr_c_lbgzbm", "lb_work_id"));
			}
			entity.setIsUse("Y");
			entity.setLastModifiedDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deletes(String ids) {
		String sql = "update hr_c_lbgzbm a set a.is_use = 'N' where a.lb_work_id in (" + ids
				+ ")";

		bll.exeNativeSQL(sql);
	}

	public HrCLbgzbm update(HrCLbgzbm entity) throws CodeRepeatException {
		LogUtil.log("updating HrCLbgzbm instance", Level.INFO, null);
		try {
			if(this.checkNameSame(entity.getLbWorkName(), entity.getLbWorkId()))
			{
				throw new CodeRepeatException("劳保名称不能重复!");
			}
			HrCLbgzbm result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCLbgzbm findById(Long id) {
		LogUtil.log("finding HrCLbgzbm instance with id: " + id, Level.INFO,
				null);
		try {
			HrCLbgzbm instance = entityManager.find(HrCLbgzbm.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrCLbgzbm> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCLbgzbm instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCLbgzbm model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String typeName,String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCLbgzbm instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String whereString = "";
			if (typeName != null && typeName.length()>0) {
				whereString = " and t.lb_work_name like '%"+typeName+"%'";
			}
			String sqlCount = "select count(*) from hr_c_lbgzbm t where t.is_use='Y' and t.enterprise_code='"+enterpriseCode+"'";
			
			sqlCount +=whereString;
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if (totalCount > 0) {
				String sql = "select * from hr_c_lbgzbm t where t.is_use='Y' and t.enterprise_code='"+enterpriseCode+"'";
				sql += whereString;
				List<HrCLbgzbm> list = bll.queryByNativeSQL(sql,HrCLbgzbm.class, rowStartIdxAndCount);
				result.setList(list);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 查找所有劳保工种
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllLbgzbms(String enterpriseCode){
		try{
			String sql = "SELECT L.LB_WORK_ID,L.LB_WORK_NAME FROM HR_C_LBGZBM L \n" +
					" WHERE L.IS_USE = 'Y'  AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有劳保工种d和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有劳保工种id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有劳保工种id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * CHECK劳保名称唯一性
	 * @param lbWorkName
	 * @param lbWorkId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkNameSame(String lbWorkName,Long ... lbWorkId)
	{
		String sql=
			"select count(1) from HR_C_LBGZBM t\n" +
			"where t.lb_work_name='"+lbWorkName+"'";
		  if(lbWorkId !=null&& lbWorkId.length>0){
		    	sql += "  and t.lb_work_id <> " + lbWorkId[0];
		    } 
		    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
			{
		    	return true;
			}
		    else
		    {
		    	return false;
		    }
	}
}