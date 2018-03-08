package power.ejb.productiontec.relayProtection;

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
import power.ejb.productiontec.relayProtection.form.ProtectProjectForm;

/**
 * 继电定值项目维护表
 * @author liuyi 090713
 */
@Stateless
public class PtJdbhCDzxmwhFacade implements PtJdbhCDzxmwhFacadeRemote {
	// property constants
	public static final String PROTECT_TYPE_ID = "protectTypeId";
	public static final String FIXVALUE_ITEM_NAME = "fixvalueItemName";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *新增一条继电定值项目维护表记录
	 */
	public PtJdbhCDzxmwh save(PtJdbhCDzxmwh entity) {
		LogUtil.log("saving PtJdbhCDzxmwh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_C_DZXMWH t\n"
				+ "where t.FIXVALUE_ITEM_NAME = '" + entity.getFixvalueItemName() + "'"
				+ " and t.PROTECT_TYPE_ID ='" + entity.getProtectTypeId() + "'";
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
				entity.setFixvalueItemId(bll.getMaxId("PT_JDBH_C_DZXMWH", "FIXVALUE_ITEM_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条继电定值项目维护表记录
	 */
	public void delete(PtJdbhCDzxmwh entity) {
		LogUtil.log("deleting PtJdbhCDzxmwh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhCDzxmwh.class, entity
					.getFixvalueItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	/**
	 * 删除一条或多条继电定值项目维护表数据
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JDBH_C_DZXMWH a\n"
	    + " where a.FIXVALUE_ITEM_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
       
	}
	/**
	 *更新一条继电定值项目维护表数据
	 */
	public PtJdbhCDzxmwh update(PtJdbhCDzxmwh entity) {
		LogUtil.log("updating PtJdbhCDzxmwh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_C_DZXMWH t\n"
				+ "where t.FIXVALUE_ITEM_NAME = '" + entity.getFixvalueItemName() + "'"
				+ " and t.PROTECT_TYPE_ID ='" + entity.getProtectTypeId() + "'"
				+ " and t.FIXVALUE_ITEM_ID !=" + entity.getFixvalueItemId();
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
			PtJdbhCDzxmwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条继电定值项目维护表数据
	 */
	public PtJdbhCDzxmwh findById(Long id) {
		LogUtil.log("finding PtJdbhCDzxmwh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhCDzxmwh instance = entityManager
					.find(PtJdbhCDzxmwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	/**
	 * 查询继电定值项目维护表列表
	 * @param name 名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.FIXVALUE_ITEM_ID,\n"
			+ "       b.PROTECT_TYPE_NAME,\n"
			+ "       a.FIXVALUE_ITEM_NAME,\n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "		  a.PROTECT_TYPE_ID	\n"		
			+ "  from PT_JDBH_C_DZXMWH a, PT_JDBH_C_BHLXWH b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and a.PROTECT_TYPE_ID = b.PROTECT_TYPE_ID";
		
		
		String sqlCount = "select count(a.FIXVALUE_ITEM_ID)\n"
			+ "  from PT_JDBH_C_DZXMWH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.FIXVALUE_ITEM_NAME like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.FIXVALUE_ITEM_NAME like '%" + name + "%' \n";
		}
		sql = sql + " order by a.FIXVALUE_ITEM_ID \n";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJdbhCDzxmwh pjcd = new PtJdbhCDzxmwh();
				ProtectProjectForm info = new ProtectProjectForm();
				Object []data = (Object[])it.next();
				pjcd.setFixvalueItemId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					info.setProtectTypeName(data[1].toString());
				if(data[2] != null)
					pjcd.setFixvalueItemName(data[2].toString());
				if(data[3] != null)
					pjcd.setEnterpriseCode(data[3].toString());
				if(data[4] != null)
					pjcd.setProtectTypeId(Long.parseLong(data[4].toString()));
				info.setPjcd(pjcd);
				arrlist.add(info);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}

}