package power.ejb.productiontec.chemistry;

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
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlh;

/**
 * Facade for entity PtHxjdCHxzxybwh.
 * 
 * @see power.ejb.productiontec.chemistry.PtHxjdCHxzxybwh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtHxjdCHxzxybwhFacade implements PtHxjdCHxzxybwhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *增加一条化学在线仪器信息
	 */
	public PtHxjdCHxzxybwh save(PtHxjdCHxzxybwh entity) {
		LogUtil.log("saving PtHxjdCHxzxybwh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_HXJD_C_HXZXYBWH t\n"
				+ "where t.METER_NAME = '" + entity.getMeterName() + "'";
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			else
			{
				entity.setMeterId(bll.getMaxId("PT_HXJD_C_HXZXYBWH", "METER_ID"));
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			}
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条记录
	 */
	public void delete(PtHxjdCHxzxybwh entity) {
		LogUtil.log("deleting PtHxjdCHxzxybwh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtHxjdCHxzxybwh.class, entity
					.getMeterId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 *更新一条化学在线仪器信息
	 */
	public PtHxjdCHxzxybwh update(PtHxjdCHxzxybwh entity) {
		LogUtil.log("updating PtHxjdCHxzxybwh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_HXJD_C_HXZXYBWH t\n"
				+ "where t.METER_NAME = '" + entity.getMeterName() + "'"
				+ " and t.METER_ID !=" + entity.getMeterId();
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			PtHxjdCHxzxybwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过Id查一条记录
	 */
	public PtHxjdCHxzxybwh findById(Long id) {
		LogUtil.log("finding PtHxjdCHxzxybwh instance with id: " + id,
				Level.INFO, null);
		try {
			PtHxjdCHxzxybwh instance = entityManager.find(
					PtHxjdCHxzxybwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_HXJD_C_HXZXYBWH a\n"
	    + " where a.METER_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
       
       String meterSql = "delete from  "+
		"PT_HXJD_J_ZXYBYB b\n"
	    + " where b.METER_ID in (" + ids
	   + ")\n" ;
      bll.exeNativeSQL(meterSql);
		
	}

	public PageObject findAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.METER_ID,\n"
			+ "       a.METER_NAME,\n"
			+ "       a.ENTERPRISE_CODE \n"
			+ "  from PT_HXJD_C_HXZXYBWH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.METER_ID)\n"
			+ "  from PT_HXJD_C_HXZXYBWH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.METER_NAME like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.METER_NAME like '%" + name + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtHxjdCHxzxybwh phc = new PtHxjdCHxzxybwh();
				Object []data = (Object[])it.next();
				phc.setMeterId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					phc.setMeterName(data[1].toString());
				if(data[2] != null)
					phc.setEnterpriseCode(data[2].toString());
				
				arrlist.add(phc);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}

	

}