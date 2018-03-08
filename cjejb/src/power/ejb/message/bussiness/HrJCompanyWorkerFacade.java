package power.ejb.message.bussiness;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.message.form.WorkerInfo;
import power.ejb.system.SysJUr;

/**
 * Facade for entity HrJCompanyWorker.
 * 
 * @see power.ejb.message.bussiness.HrJCompanyWorker
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJCompanyWorkerFacade implements HrJCompanyWorkerFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	public void save(HrJCompanyWorker entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("HR_J_COMPANY_WORKER", "id"));
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void addWorkersToCompany(String enterpriseCode, String companyCode,String workerCodes) { 
		String[] temp = workerCodes.split(",");
		String ws = "''";
		for(String s : temp)
		{
			ws += ",'"+s+"'";
		}  
		String sql = "select t.worker_code from hr_j_company_worker t where t.zbbmtx_code='"+companyCode
					+"' and t.worker_code  in("+ws+") and t.is_use='Y'";
		List list = bll.queryByNativeSQL(sql);  
		Long id = bll.getMaxId("HR_J_COMPANY_WORKER", "id");  
		 
			for(int i=0;i<temp.length;i++)
			{
				if (!this.isExist(temp[i], list)) {
					HrJCompanyWorker m = new HrJCompanyWorker();
					m.setId(id + i);
					m.setZbbmtxCode(companyCode);
					m.setEnterpriseCode(enterpriseCode);
					m.setIsUse("Y");
					m.setWorkerCode(temp[i]);
					this.save(m);
					if ((i + 1) % 20 == 0) {
						entityManager.flush();
					}
				}
		}
	}
	private boolean isExist(String workerCode,List list)
	{
		if(list == null && list.size()>0)
			return false;
		for(int i=0;i<list.size();i++)
		{ 
			if(list.get(i).toString().equals(workerCode))
			{
				return true;
			}
		}
		return false;
	}

	 

	public void delete(HrJCompanyWorker entity) {
		entity.setIsUse("N");
		this.update(entity);
	}

	public HrJCompanyWorker update(HrJCompanyWorker entity) {
		LogUtil.log("updating HrJCompanyWorker instance", Level.INFO, null);
		try {
			HrJCompanyWorker result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJCompanyWorker findById(Long id) {
		LogUtil.log("finding HrJCompanyWorker instance with id: " + id,
				Level.INFO, null);
		try {
			HrJCompanyWorker instance = entityManager.find(
					HrJCompanyWorker.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<WorkerInfo> findByZbbmtxCode(String zbbmtxCode) {
		try {
			String sql = "select getworkername(t.worker_code),t.worker_code from hr_j_company_worker t"
					+ " where t.is_use='Y' and t.zbbmtx_code=?";
			List list = bll.queryByNativeSQL(sql, new Object[] { zbbmtxCode });
			List arr = new ArrayList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					WorkerInfo model = new WorkerInfo();
					Object[] ob = (Object[]) list.get(i);
					if (ob[0] != null) {
						model.setWorkerName(ob[0].toString());
					}
					if (ob[1] != null) {
						model.setWorkerCode(ob[1].toString());
					}
					if (model != null) {
						arr.add(model);
					}
				}
			}
			return arr;
		} catch (RuntimeException re) {
			LogUtil.log("find WorkerInfo failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJCompanyWorker findAllByCode(String zbbmtxCode, String workerCode) {
		try {
			String sql = "";
			sql = "select * from hr_j_company_worker where zbbmtx_code=? and woker_code=?";
			List list = bll.queryByNativeSQL(sql, new Object[] { zbbmtxCode,
					workerCode });
			if (list.size() > 0) {
				HrJCompanyWorker model = (HrJCompanyWorker) bll
						.queryByNativeSQL(sql,
								new Object[] { zbbmtxCode, workerCode }).get(0);
				return model;
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("find WorkerInfo failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrJCompanyWorker findAllByWorkerCode(String workerCode){
		try{
			String sql = "";
			sql = "select * from HR_J_COMPANY_WORKER t where t.worker_code="+workerCode +"and is_use='Y'";
			List<HrJCompanyWorker> list = bll.queryByNativeSQL(sql,HrJCompanyWorker.class);
			if(list != null)
				return  list.get(0);
			else
				return null;

		}catch(RuntimeException re){
			LogUtil.log("find JljfCObject failed", Level.SEVERE, re);
			throw re;
		}
	}

}
