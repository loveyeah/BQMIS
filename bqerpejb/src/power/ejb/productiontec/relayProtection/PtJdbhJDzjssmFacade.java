package power.ejb.productiontec.relayProtection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.relayProtection.form.CalculateInstructionForm;
import power.ejb.productiontec.relayProtection.form.ProtectEquForm;

/**
 * 继电保护定值计算说明
 * @author liuyi 090721
 */
@Stateless
public class PtJdbhJDzjssmFacade implements PtJdbhJDzjssmFacadeRemote {
	// property constants
	public static final String JSSM_NAME = "jssmName";
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";
	public static final String FILL_BY = "fillBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条 继电保护定值计算说明记录
	 */
	public PtJdbhJDzjssm save(PtJdbhJDzjssm entity) {
		LogUtil.log("saving PtJdbhJDzjssm instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_DZJSSM t\n"
				+ "where t.JSSM_NAME = '" + entity.getJssmName() + "'";
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
			entity.setDzjssmId(bll.getMaxId("PT_JDBH_J_DZJSSM", "DZJSSM_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	/**
	 * 删除一条 继电保护定值计算说明记录
	 */
	public void delete(PtJdbhJDzjssm entity) {
		LogUtil.log("deleting PtJdbhJDzjssm instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJDzjssm.class, entity
					.getDzjssmId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条继电保护装置台帐记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JDBH_J_DZJSSM a\n"
	    + " where a.DZJSSM_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
	}
	/**
	 * 更新一条 继电保护定值计算说明记录
	 */
	public PtJdbhJDzjssm update(PtJdbhJDzjssm entity) {
		LogUtil.log("updating PtJdbhJDzjssm instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_DZJSSM t\n"
				+ "where t.JSSM_NAME = '" + entity.getJssmName() + "'"
				+ "and t.DZJSSM_ID !=" + entity.getDzjssmId();
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
			PtJdbhJDzjssm result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条 继电保护定值计算说明记录
	 */
	public PtJdbhJDzjssm findById(Long id) {
		LogUtil.log("finding PtJdbhJDzjssm instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhJDzjssm instance = entityManager
					.find(PtJdbhJDzjssm.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查找 继电保护定值计算说明列表
	 */
	public PageObject findAll(String name, String enterpriseCode,final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.DZJSSM_ID,\n"
			+ "       a.JSSM_NAME,\n"
			+ "       a.CONTENT, \n"
			+ "       a.MEMO, \n"
			+ "       a.FILL_BY, \n"
			+ "       a.FILL_DATE, \n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "       getworkername(a.FILL_BY), \n"		
			+ "       to_char(a.FILL_DATE,'yyyy-MM-dd') \n"
			+ "  from PT_JDBH_J_DZJSSM a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";			
				
		String sqlCount = "select count(a.DZJSSM_ID)\n"
			+ "  from PT_JDBH_J_DZJSSM a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.JSSM_NAME like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.JSSM_NAME like '%" + name + "%' \n";
		}

		sql = sql + " order by a.DZJSSM_ID \n";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJdbhJDzjssm dzjssm = new PtJdbhJDzjssm();
				CalculateInstructionForm form = new CalculateInstructionForm();
				Object []data = (Object[])it.next();
				dzjssm.setDzjssmId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					dzjssm.setJssmName(data[1].toString());
				if(data[2] != null)
					dzjssm.setContent(data[2].toString());
				if(data[3] != null)
					dzjssm.setMemo(data[3].toString());
				if(data[4] != null)
					dzjssm.setFillBy(data[4].toString());
				if(data[5] != null)
					try {
						dzjssm.setFillDate(sbf.parse(data[5].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[6] != null)
					dzjssm.setEnterpriseCode(data[6].toString());
				if(data[7] != null)
					form.setFillName(data[7].toString());
				if(data[8] != null)
					form.setFillDate(data[8].toString());
					
				form.setDzjssm(dzjssm);
				arrlist.add(form);
			 }
		  }
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}