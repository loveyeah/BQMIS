package power.ejb.equ.change;

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
 * Facade for entity EquJChangphoto.
 * 
 * @see power.ejb.equ.change.EquJChangphoto
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJChangphotoFacade implements EquJChangphotoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(EquJChangphoto entity) {
		LogUtil.log("saving EquJChangphoto instance", Level.INFO, null);
		try {
			 if(entity.getChangePhotoId()==null)
				{
					entity.setChangePhotoId(bll.getMaxId("equ_j_changphoto", "change_photo_id"));
					entity.setIsUse("Y");
				
				}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}


	public void delete(EquJChangphoto entity) {
		LogUtil.log("deleting EquJChangphoto instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJChangphoto.class, entity
					.getChangePhotoId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteByChangeNo(String changeNo,String enterpriseCode)
	{
		EquJChangphoto entity=this.findByChangeNo(changeNo, enterpriseCode);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}


	public EquJChangphoto update(EquJChangphoto entity) {
		LogUtil.log("updating EquJChangphoto instance", Level.INFO, null);
		try {
			EquJChangphoto result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJChangphoto findById(Long id) {
		LogUtil.log("finding EquJChangphoto instance with id: " + id,
				Level.INFO, null);
		try {
			EquJChangphoto instance = entityManager.find(EquJChangphoto.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public EquJChangphoto findByChangeNo(String changeNo,String enterprisecode)
	{
		String sql=
			"select * from equ_j_changphoto a\n" +
			"where a.equ_change_no ='"+changeNo+"'\n" + 
			"and a.enterprise_code='"+enterprisecode+"'\n" + 
			"and a.is_use='Y'";
		
		List<EquJChangphoto> list=bll.queryByNativeSQL(sql,EquJChangphoto.class);
		if(list!=null)
		{
			if(list.size()>0)
			{
				return  list.get(0);
			}
		}
		return null;
		
	}





}