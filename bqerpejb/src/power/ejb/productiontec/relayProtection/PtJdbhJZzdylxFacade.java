package power.ejb.productiontec.relayProtection;

import java.text.ParseException;
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
import power.ejb.productiontec.relayProtection.form.TypeForm;
import power.ejb.run.securityproduction.SpJSecurityPlan;
import power.ejb.run.securityproduction.form.SpJSecurityPlanInfo;

/**
 * Facade for entity PtJdbhJZzdylx.
 * 
 * @see power.ejb.productiontec.relayProtection.PtJdbhJZzdylx
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJdbhJZzdylxFacade implements PtJdbhJZzdylxFacadeRemote {
	// property constants
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *新增一条保护装置对应保护类型数据
	 */
	public void save(PtJdbhJZzdylx entity) {
		LogUtil.log("saving PtJdbhJZzdylx instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条保护装置对应保护类型数据
	 */
	public void delete(PtJdbhJZzdylx entity) {
		LogUtil.log("deleting PtJdbhJZzdylx instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJZzdylx.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条保护装置对应保护类型数据
	 */
	public PtJdbhJZzdylx update(PtJdbhJZzdylx entity) {
		LogUtil.log("updating PtJdbhJZzdylx instance", Level.INFO, null);
		try {
			PtJdbhJZzdylx result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条保护装置对应保护类型数据
	 */
	public PtJdbhJZzdylx findById(PtJdbhJZzdylxId id) {
		LogUtil.log("finding PtJdbhJZzdylx instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhJZzdylx instance = entityManager
					.find(PtJdbhJZzdylx.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}



	/**
	 * Find all PtJdbhJZzdylx entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhJZzdylx> all PtJdbhJZzdylx entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJdbhJZzdylx> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJdbhJZzdylx instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJdbhJZzdylx model";
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
	 * 保护装置对应保护类型表中的数据进行增加或删除
	 */
	public void saveDevicesAndTypes(String devId,String typeIds,String enterpriseCode)
	{
		String dvds[] = devId.split(",");
		String ds[] = typeIds.split(",");
		String sql = "delete from  "+
			"PT_JDBH_J_ZZDYLX a\n"
			+ " where a.DEVICE_ID in (" +devId 
			+ ")\n" ;
        bll.exeNativeSQL(sql);
		for(int j = 0;j<= dvds.length - 1;j++)			
			for(int i = 0; i<= ds.length - 1; i++)
			{
				PtJdbhJZzdylxId pjjId = new PtJdbhJZzdylxId();
				pjjId.setDeviceId(Long.parseLong(dvds[j]));
				pjjId.setProtectTypeId(Long.parseLong(ds[i]));
				PtJdbhJZzdylx pjj = new PtJdbhJZzdylx();
				pjj.setId(pjjId);
				pjj.setEnterpriseCode(enterpriseCode);
				save(pjj);
			}
		}

	/**
	 * 保护装置对应保护类型表中的数据查找
	 */
	public PageObject findDeviceProList(String devId,int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "";
		String sqlCount = "";
		if(!devId.equals(""))
		{
			sql = "select t.protect_type_id ," 
				  +"t.protect_type_name, " 
				  +" decode ((select count(*) from PT_JDBH_J_ZZDYLX a " 
				  +"where a.device_id= " +devId
				  +" and a.protect_type_id=t.protect_type_id),0,'N','Y') " 
				  + " from PT_JDBH_C_BHLXWH t "
				  + "order by t.protect_type_id ";
			sqlCount = "select count(*) from PT_JDBH_C_BHLXWH t";
			
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list != null && list.size() > 0) {
				while(it.hasNext()){
					TypeForm tf = new TypeForm();
					Object []data = (Object[])it.next();
					tf.setProtectTypeId(data[0].toString());
					if(data[1] != null)
						tf.setProtectTypeName(data[1].toString());
					if(data[2] != null)
						tf.setChooseFlag(data[2].toString());
					arrlist.add(tf);
				 }
			  }
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				pg.setList(arrlist);
				pg.setTotalCount(totalCount);
				
		}
		else if(devId.equals(""))
		{
			sql = "select t.protect_type_id,t.protect_type_name " 
					+ " from PT_JDBH_C_BHLXWH t "
					+ "order by t.protect_type_id";
			sqlCount = "select count(*) from PT_JDBH_C_BHLXWH t ";
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list != null && list.size() > 0) {
				while(it.hasNext()){
					TypeForm tf = new TypeForm();
					Object []data = (Object[])it.next();
					tf.setProtectTypeId(data[0].toString());
					if(data[1] != null)
						tf.setProtectTypeName(data[1].toString());
					tf.setChooseFlag("N");
					arrlist.add(tf);
				 }
			  }
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
				pg.setList(arrlist);
				pg.setTotalCount(totalCount);
		}
		return pg;
	}
}