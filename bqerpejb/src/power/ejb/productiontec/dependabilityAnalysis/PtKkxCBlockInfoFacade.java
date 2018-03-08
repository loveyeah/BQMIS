package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PtKkxCBlockInfo.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.PtKkxCBlockInfo
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtKkxCBlockInfoFacade implements PtKkxCBlockInfoFacadeRemote {
	

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存
	 */
	public void save(PtKkxCBlockInfo entity) {
		try {
			
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除
	 */
	public void delete(PtKkxCBlockInfo entity) {
		try {
			entity = entityManager.getReference(PtKkxCBlockInfo.class, entity
					.getBlockInfoId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 *修改 
	 */
	public PtKkxCBlockInfo update(PtKkxCBlockInfo entity) {
		try {
			PtKkxCBlockInfo result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtKkxCBlockInfo findById(Long id) {
		try {
			PtKkxCBlockInfo instance = entityManager.find(
					PtKkxCBlockInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	public void delete(String ids) {
		String sqlString = "delete from  PT_KKX_C_BLOCK_INFO  s "
				+ " where s.BLOCK_INFO_ID in (" + ids + ")";
		bll.exeNativeSQL(sqlString);
	}
	
	public void save(List<PtKkxCBlockInfo> addList, List<PtKkxCBlockInfo> updateList,
			String deleteId){
		if(addList.size() > 0){
			Long id = bll.getMaxId("PT_KKX_C_BLOCK_INFO", "BLOCK_INFO_ID");
			for(PtKkxCBlockInfo entity : addList){
				entity.setBlockInfoId(id++);
				this.save(entity);
			}}
		if(updateList.size() > 0){
			for (PtKkxCBlockInfo entity : updateList) {
				this.update(entity);
			}
		}
		if (deleteId.length() > 0) {
			this.delete(deleteId);
		}
		}
	
	/**
	 * Find all PtKkxCBlockInfo entities.
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			final String queryString = "select * from PT_KKX_C_BLOCK_INFO t where t.enterprise_code='"+enterpriseCode+"'";
			String sql="select count(1) from PT_KKX_C_BLOCK_INFO t where t.enterprise_code='"+enterpriseCode+"'";
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			List<PtKkxCBlockInfo> list = bll.queryByNativeSQL(queryString, PtKkxCBlockInfo.class,rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}