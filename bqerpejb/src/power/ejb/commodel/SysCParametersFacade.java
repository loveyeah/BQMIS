package power.ejb.commodel;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * ϵͳ
 * @author slTang
 */
@Stateless
public class SysCParametersFacade implements SysCParametersFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll; 
	
//	public void save(SysCParameters entity) {
//		LogUtil.log("saving SysCParameters instance", Level.INFO, null);
//		try {
//			entityManager.persist(entity);
//			LogUtil.log("save successful", Level.INFO, null);
//		} catch (RuntimeException re) {
//			LogUtil.log("save failed", Level.SEVERE, re);
//			throw re;
//		}
//	}
//
//
//	public void delete(SysCParameters entity) {
//		LogUtil.log("deleting SysCParameters instance", Level.INFO, null);
//		try {
//			entity = entityManager.getReference(SysCParameters.class, entity
//					.getParmNo());
//			entityManager.remove(entity);
//			LogUtil.log("delete successful", Level.INFO, null);
//		} catch (RuntimeException re) {
//			LogUtil.log("delete failed", Level.SEVERE, re);
//			throw re;
//		}
//	}

	
	public SysCParameters update(SysCParameters entity) {
		LogUtil.log("updating SysCParameters instance", Level.INFO, null);
		try {
			SysCParameters result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 根据参数编号查找参数值
	 * @param pamNo
	 * @param enterpriseCode
	 * @return
	 */
	public String findBypamNo(String pamNo,String enterpriseCode){
		String returnValue="";
		String sql=
			"select t.parm_value from sys_c_parameters t where t.parm_no=? and t.enterprise_code=? and rownum=1";
		Object ob=bll.getSingal(sql, new Object[]{pamNo,enterpriseCode});
		if(ob!=null){
			returnValue=ob.toString();
		}else{
			returnValue=null;
		}
		return returnValue;
	}

//	public SysCParameters findById(String id) {
//		LogUtil.log("finding SysCParameters instance with id: " + id,
//				Level.INFO, null);
//		try {
//			SysCParameters instance = entityManager.find(SysCParameters.class,
//					id);
//			return instance;
//		} catch (RuntimeException re) {
//			LogUtil.log("find failed", Level.SEVERE, re);
//			throw re;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<SysCParameters> findAll(final int... rowStartIdxAndCount) {
//		LogUtil.log("finding all SysCParameters instances", Level.INFO, null);
//		try {
//			final String queryString = "select model from SysCParameters model";
//			Query query = entityManager.createQuery(queryString);
//			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
//				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
//				if (rowStartIdx > 0) {
//					query.setFirstResult(rowStartIdx);
//				}
//
//				if (rowStartIdxAndCount.length > 1) {
//					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
//					if (rowCount > 0) {
//						query.setMaxResults(rowCount);
//					}
//				}
//			}
//			return query.getResultList();
//		} catch (RuntimeException re) {
//			LogUtil.log("find all failed", Level.SEVERE, re);
//			throw re;
//		}
//	}
}