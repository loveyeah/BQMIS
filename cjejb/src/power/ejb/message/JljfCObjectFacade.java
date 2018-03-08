package power.ejb.message;

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
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.system.SysCFls;

/**
 * Facade for entity JljfCObject.
 * 
 * @see power.ejb.message.JljfCObject
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class JljfCObjectFacade implements JljfCObjectFacadeRemote {
	// property constants
	public static final String ZBBMTX_NAME = "zbbmtxName";
	public static final String ZBBM_HAVE_DATA = "zbbmHaveData";
	public static final String ITEMTYPE = "itemtype";
	public static final String ITEM_CODE = "itemCode";
	public static final String ZBBMTX_ALIAS = "zbbmtxAlias";
	public static final String ZBBMTX_ALIAS1 = "zbbmtxAlias1";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	@SuppressWarnings("unchecked")
	public JljfCObject save(JljfCObject entity) throws CodeRepeatException {
		LogUtil.log("saving JljfCObject instance", Level.INFO, null);
		try {
			if (checkNameSameForAdd(entity.getZbbmtxName(), entity
					.getZbbmtxAlias())) {
				throw new CodeRepeatException("该公司已经存在!");
			} else {
				entity.setZbbmtxCode(this.createZbbmtxCode());
				entity.setIsUse("Y");
				entityManager.persist(entity);
			}
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	private String createZbbmtxCode() {
		String sql = "SELECT replace(nvl(to_char(max(replace(t.zbbmtx_code,' ',''))+1,'000'),'001'),' ','')\n"
				+ " FROM jljf_c_object T where length(replace(t.zbbmtx_code,' ',''))=3";
		Object o = bll.getSingal(sql);
		return o.toString();
	}

	public void delete(JljfCObject entity) {
		entity.setIsUse("N");
		try {
			this.update(entity);
		} catch (CodeRepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public JljfCObject update(JljfCObject entity) throws CodeRepeatException {
		LogUtil.log("updating JljfCObject instance", Level.INFO, null);
		try {
			if (checkNameSameForUpdate(entity.getZbbmtxName(), entity.getZbbmtxAlias()
					, entity.getZbbmtxCode())) {
				throw new CodeRepeatException("该公司已经存在!");
			}
			JljfCObject result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkNameSameForAdd(String zbbmtxName, String zbbmtxAlias) {
		String sql = "select count(1) from jljf_c_object t where (t.zbbmtx_name=? or t.zbbmtx_alias=?) and t.is_use='Y'";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { zbbmtxName, zbbmtxAlias }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkNameSameForUpdate(String zbbmtxAlias,
			String zbbmtxName, String zbbmtxCode) {
		String sql = "select count(1) from jljf_c_object t where (t.zbbmtx_name=? or t.zbbmtx_alias=?) and t.zbbmtx_code<>? and is_use='Y'";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { zbbmtxName, zbbmtxAlias, zbbmtxCode })
				.toString());
		if (size > 0) {
			return true;
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	public JljfCObject findByZbbmtxCode(String zbbmtxCode) {
		LogUtil.log("finding all JljfCObject instances", Level.INFO, null);
		try {
			final String queryString = "select jl.* from jljf_c_object jl where jl.zbbmtx_code="+zbbmtxCode+" and jl.is_use='Y'";
			List<JljfCObject> list=bll.queryByNativeSQL(queryString,JljfCObject.class);
			if(list!=null){
				return list.get(0);
			}else{
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("find JljfCObject failed", Level.SEVERE, re);
			throw re;
		}
	}
	


	@SuppressWarnings("unchecked")
	public List<JljfCObject> findAll() {
		LogUtil.log("finding all JljfCObject instances", Level.INFO, null);
		try {
			final String queryString = "select model from JljfCObject model where model.isUse='Y'";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
//	add by sltang
	public List<JljfCObject> findByFuzzy(String fuzzy){
		String sql=
			"select t.* from jljf_c_object t where t.zbbmtx_name like ? and t.is_use='Y'";
		List<JljfCObject> list = bll.queryByNativeSQL(sql, new Object[]{"%"+fuzzy+"%"},JljfCObject.class);
		if(list!=null){
			return list;
		}else{
			return null;
		}
		
	}

}