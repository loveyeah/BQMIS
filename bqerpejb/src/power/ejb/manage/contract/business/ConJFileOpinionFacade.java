package power.ejb.manage.contract.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.ConFileOpinionForm;

/**
 * Facade for entity ConJFileOpinion.
 * 
 * @see power.ejb.manage.contract.business.ConJFileOpinion
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJFileOpinionFacade implements ConJFileOpinionFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "ConJContractInfoFacade")
	protected ConJContractInfoFacadeRemote conremote;
	@EJB(beanName = "ConJModifyFacade")
	protected ConJModifyFacadeRemote modremote;
	/**
	 * Perform an initial save of a previously unsaved ConJFileOpinion entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConJFileOpinion entity to persist
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ConJFileOpinion entity) throws CodeRepeatException {
		LogUtil.log("saving ConJFileOpinion instance", Level.INFO, null);
		try {
				if(entity.getOpinionId() == null)
				{
					entity.setOpinionId(bll.getMaxId("con_j_file_opinion", "opinion_id"));
				}
				if("CON".equals(entity.getFileType()))
				{
					ConJContractInfo model=conremote.findById(entity.getKeyId());
					model.setFileStatus("BAK");
					conremote.update(model, null,null);//更改合同表归档状态为"被退回"
				}
				else
				{
					ConJModify model=modremote.findById(entity.getKeyId());
					model.setFileStatus("BAK");
					modremote.update(model);//更改变更表归档状态为"被退回"
				}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ConJFileOpinion entity.
	 * 
	 * @param entity
	 *            ConJFileOpinion entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ConJFileOpinion entity) {
		LogUtil.log("deleting ConJFileOpinion instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(ConJFileOpinion.class, entity
					.getOpinionId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ConJFileOpinion entity and return it or a copy
	 * of it to the sender. A copy of the ConJFileOpinion entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            ConJFileOpinion entity to update
	 * @returns ConJFileOpinion the persisted ConJFileOpinion entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConJFileOpinion update(ConJFileOpinion entity) {
		LogUtil.log("updating ConJFileOpinion instance", Level.INFO, null);
		try {
			ConJFileOpinion result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJFileOpinion findById(Long id) {
		LogUtil.log("finding ConJFileOpinion instance with id: " + id,
				Level.INFO, null);
		try {
			ConJFileOpinion instance = entityManager.find(
					ConJFileOpinion.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all ConJFileOpinion entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConJFileOpinion property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ConJFileOpinion> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ConJFileOpinion> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ConJFileOpinion instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConJFileOpinion model where model."
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

	/**
	 * Find all ConJFileOpinion entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJFileOpinion> all ConJFileOpinion entities
	 */
	@SuppressWarnings("unchecked")
	public List<ConJFileOpinion> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all ConJFileOpinion instances", Level.INFO, null);
		try {
			final String queryString = "select model from ConJFileOpinion model";
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
	public List<ConFileOpinionForm> findFileOList(Long id,String type,String enterpriseCode){
		String sql="select r.opinion_id,\n" +
					"       r.key_id,\n" + 
					"       r.file_type,\n" + 
					"       r.opinion,\n" + 
					"       r.gd_worker,\n" + 
					"       getworkername(r.gd_worker) worker_name,\n" + 
					"       to_char(r.withdrawal_time, 'yyyy-MM-dd hh24:mi:ss') withdrawal_time\n" + 
					"  from con_j_file_opinion r\n" + 
					" where r.key_id="+id+"\n" + 
					"   and r.file_type='"+type+"'\n" + 
					"   and r.is_use = 'Y'\n" + 
					"   and r.enterprise_code = '"+enterpriseCode+"'";
		List list=bll.queryByNativeSQL(sql);
		List<ConFileOpinionForm> arrayList=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			ConFileOpinionForm model=new ConFileOpinionForm();
			Object[] o = (Object[]) it.next();
			if (o[0] != null)
			model.setOpinionId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				model.setKeyId(Long.parseLong(o[1].toString()));
			if (o[2] != null)
				model.setFileType(o[2].toString());
			if (o[3] != null)
				model.setOpinion(o[3].toString());
			if (o[4] != null)
				model.setGdWorker(o[4].toString());
			if (o[5] != null)
				model.setGdWorkerName(o[5].toString());
			if (o[6] != null)
				model.setWithdrawalTime(o[6].toString());
			arrayList.add(model);
		}
		return arrayList;
	}

}