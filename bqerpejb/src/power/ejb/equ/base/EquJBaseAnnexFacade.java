package power.ejb.equ.base;

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
 * Facade for entity EquJBaseAnnex.
 * 
 * @see power.ejb.equ.base.EquJBaseAnnex
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJBaseAnnexFacade implements EquJBaseAnnexFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * 保存设备基础信息附件信息
	 * @param entity
	 * @return EquJBaseAnnex
	 */
	public EquJBaseAnnex save(EquJBaseAnnex entity) {
			Long Id=bll.getMaxId("EQU_J_BASE_ANNEX", "ANNEX_ID");
			entity.setAnnexId(Id);
			entity.setIsUse("Y");
			entityManager.persist(entity);
		return entity;
	}
	/**
	 * 删除设备基础信息附件信息
	 * 
	 * @param ids
	 */
	public void delete(String ids) {
		String sql="update EQU_J_BASE_ANNEX set is_use='N' where ANNEX_ID in ('"+ids+"')";
		bll.exeNativeSQL(sql);
	}
	/**
	 * 更新设备基础信息附件信息
	 * @param entity
	 * @return EquJBaseAnnex
	 */
	public EquJBaseAnnex update(EquJBaseAnnex entity) {
			EquJBaseAnnex result = entityManager.merge(entity);
			return result;
	}
	/**
	 * 查找设备基础信息附件信息
	 * @param id
	 * @return EquJBaseAnnex
	 */
	public EquJBaseAnnex findById(Long id) {
			EquJBaseAnnex instance = entityManager
					.find(EquJBaseAnnex.class, id);
			return instance;
	}
	/**
	 * 查找设备基础信息附件信息
	 * By 设备基础信息Id
	 * @param EquBaseId 设备基础信息Id
	 * @param enterprisecode 企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByBaseId(String EquBaseId,
			String enterprisecode, int... rowStartIdxAndCount) {
		
		String sql=
			"select t.annex_id, t.file_name, t.annex\n" +
			"  from EQU_J_BASE_ANNEX t\n" + 
			" where t.enterprise_code = '"+enterprisecode+"'"  + 
			"   and t.is_use = 'Y'\n" ;
			if (EquBaseId==null||EquBaseId.equals("")) {
				sql+="   and t.equ_base_id =''\n";
			}else{
				sql+="   and t.equ_base_id = "+EquBaseId+"\n" ;
			}
			String totalCount="select count(1) from ("+sql+")";
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long count=Long.parseLong(bll.getSingal(totalCount).toString());
			PageObject pg=new PageObject();
			pg.setList(list);
			pg.setTotalCount(count);
			return pg;
	}
}