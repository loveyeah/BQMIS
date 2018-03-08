package power.ejb.productiontec.relayProtection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import power.ejb.productiontec.chemistry.PtHxjdJZxybybzb;
import power.ejb.productiontec.chemistry.form.ChemistryReportForm;

/**
 * Facade for entity PtJdbhCBhlxwh.
 * 
 * @see power.ejb.productiontec.relayProtection.PtJdbhCBhlxwh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJdbhCBhlxwhFacade implements PtJdbhCBhlxwhFacadeRemote {
	// property constants
	public static final String PROTECT_TYPE_NAME = "protectTypeName";
	public static final String MEMO = "memo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条继电保护类型维护记录
	 */
	public PtJdbhCBhlxwh save(PtJdbhCBhlxwh entity) {
		LogUtil.log("saving PtJdbhCBhlxwh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_C_BHLXWH t\n"
			+ "where t.PROTECT_TYPE_NAME = '" + entity.getProtectTypeName() + "'";
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			entity.setProtectTypeId(bll.getMaxId("PT_JDBH_C_BHLXWH", "PROTECT_TYPE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条继电保护类型维护记录
	 */
	public void delete(PtJdbhCBhlxwh entity) {
		LogUtil.log("deleting PtJdbhCBhlxwh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhCBhlxwh.class, entity
					.getProtectTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 删除一条或多条继电保护类型记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JDBH_C_BHLXWH a\n"
	    + " where a.PROTECT_TYPE_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
       
       // 删除继电定制项目维护表中的相关数据
       String sqlPro = "delete from  "+
		"PT_JDBH_C_DZXMWH b\n"
	    + " where b.PROTECT_TYPE_ID in (" + ids
	   + ")\n" ;
      bll.exeNativeSQL(sqlPro);
      
      // 删除保护装置对应保护类型表中的相关数据
      String sqlDevice = "delete from  "+
		"PT_JDBH_J_ZZDYLX c\n"
	    + " where c.PROTECT_TYPE_ID in (" + ids
	   + ")\n" ;
    bll.exeNativeSQL(sqlDevice);
	}

	/**
	 * 更新一条继电保护类型维护记录
	 */
	public PtJdbhCBhlxwh update(PtJdbhCBhlxwh entity) {
		LogUtil.log("updating PtJdbhCBhlxwh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_C_BHLXWH t\n"
				+ "where t.PROTECT_TYPE_NAME = '" + entity.getProtectTypeName() + "'"
				+ "and t.PROTECT_TYPE_ID !=" + entity.getProtectTypeId();
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
			PtJdbhCBhlxwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条继电保护类型维护记录
	 */
	public PtJdbhCBhlxwh findById(Long id) {
		LogUtil.log("finding PtJdbhCBhlxwh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhCBhlxwh instance = entityManager
					.find(PtJdbhCBhlxwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询继电保护类型维护列表
	 * @param name 名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.PROTECT_TYPE_ID,\n"
			+ "       a.PROTECT_TYPE_NAME,\n"
			+ "       a.ENTERPRISE_CODE \n"
			+ "  from PT_JDBH_C_BHLXWH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.PROTECT_TYPE_ID)\n"
			+ "  from PT_JDBH_C_BHLXWH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.PROTECT_TYPE_NAME like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.PROTECT_TYPE_NAME like '%" + name + "%' \n";
		}
		sql = sql + " order by a.PROTECT_TYPE_ID \n";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJdbhCBhlxwh pjc = new PtJdbhCBhlxwh();
				Object []data = (Object[])it.next();
				pjc.setProtectTypeId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjc.setProtectTypeName(data[1].toString());
				if(data[2] != null)
					pjc.setEnterpriseCode(data[2].toString());
				
				arrlist.add(pjc);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}

	

}