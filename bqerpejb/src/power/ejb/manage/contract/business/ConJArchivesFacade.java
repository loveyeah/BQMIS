package power.ejb.manage.contract.business;

import java.util.Date;
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

/**
 * @author slTang
 * 
 */
@Stateless
public class ConJArchivesFacade implements ConJArchivesFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(ConJArchives entity) {
		try {
			if (entity.getArchivesId() != null) {
				entity.setArchivesId(bll.getMaxId("con_j_archives",
						"archives_id"));
				entity.setIsUse("Y");
				entity.setUpbuildDate(new Date());
				entityManager.persist(entity);
			}

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(ConJArchives entity) {
		try {
			this.update(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ConJArchives update(ConJArchives entity) {
		try {
			ConJArchives result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ConJArchives findById(Long id) {
		LogUtil.log("finding ConJArchives instance with id: " + id, Level.INFO,
				null);
		try {
			ConJArchives instance = entityManager.find(ConJArchives.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public int deleteMu(String archivesIds) {
		int num = 0;
		String sql = "update con_j_archives t set t.is_use = 'N' where t.archives_id in "
				+ archivesIds;
		num = bll.exeNativeSQL(sql);
		return num;
	}

	public PageObject findArchives(Long conTypeId,String enterpriseCode,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		if (enterpriseCode == null || "".equals(enterpriseCode)) {
			enterpriseCode = "hfdc";
		}
		String sql = "select * from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'and t.draw_count='"+conTypeId+"'";
		String sqlCount = "select count(1) from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'and t.draw_count='"+conTypeId+"'";
		pg.setList(bll.queryByNativeSQL(sql, ConJArchives.class,
				rowStartIdxAndCount));
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;

	}

	public PageObject fuzzyQuery(String enterpriseCode, String undertakeNo,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String strWhere = "";
		if (enterpriseCode == null || "".equals(enterpriseCode)) {
			enterpriseCode = "hfdc";
		}
		String sql = "select * from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'";
		String sqlCount = "select count(1) from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'";
		if (undertakeNo != null || !"".equals(undertakeNo))
			strWhere += " and t.undertake_no like '%" + undertakeNo + "%'";
		sql += strWhere;
		sqlCount += strWhere;
		pg.setList(bll.queryByNativeSQL(sql, ConJArchives.class,
				rowStartIdxAndCount));
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	/**
	 * 间隔查询
	 */
	public PageObject separatedQuery(String enterpriseCode,
			String[] undertakeNo, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String strWhere = "";
		if (enterpriseCode == null || "".equals(enterpriseCode)) {
			enterpriseCode = "hfdc";
		}
		String sql = "select * from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'";
		String sqlCount = "select count(1) from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'";
		if (undertakeNo != null || undertakeNo.length > 0) {
			strWhere += " and (";
			for (String no : undertakeNo) {
				strWhere += " t.undertake_no  ='" + no + "'";
				strWhere += " or";
			}
			strWhere += ")";
		}
		if(strWhere.contains("or")){
			strWhere=strWhere.substring(0,strWhere.length()-3);
			strWhere+=")";
		}
		sql += strWhere;
		sqlCount += strWhere;
		pg.setList(bll.queryByNativeSQL(sql, ConJArchives.class,
				rowStartIdxAndCount));
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	/**
	 * 区间查询
	 */
	public PageObject intervalQuery(String enterpriseCode, String undertakeNo1,
			String undertakeNo2, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String strWhere = "";
		if (enterpriseCode == null || "".equals(enterpriseCode)) {
			enterpriseCode = "hfdc";
		}
		String sql = "select * from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'";
		String sqlCount = "select count(1) from con_j_archives t where t.is_use='Y' and t.enterprise_code='"
				+ enterpriseCode + "'";
		if(undertakeNo1.equals("") && undertakeNo2.equals("")){
			strWhere=" and (t.undertake_no>='"+undertakeNo1+"' and t.undertake_no<='"+undertakeNo2+"')";
		}
		sql += strWhere;
		sqlCount += strWhere;
		pg.setList(bll.queryByNativeSQL(sql, ConJArchives.class,
				rowStartIdxAndCount));
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	public List<ConJArchives> findfileNoList(String enterprisecode,Long conTypeId){
		String sql = "select t.* from CON_J_ARCHIVES t where t.is_use='Y'\n" + 
			"and t.enterprise_code='"+enterprisecode+"' and t.draw_count='"+conTypeId+"'";
			return bll.queryByNativeSQL(sql,ConJArchives.class);
	}
}