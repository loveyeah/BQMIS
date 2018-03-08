package power.ejb.message;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity SysJMessageEmp.
 * 
 * @see power.ejb.message.SysJMessageEmp
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysJMessageEmpFacade implements SysJMessageEmpFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public void save(SysJMessageEmp entity) {
		try {
			if( entity.getId() == null){
				entity.setId(bll.getMaxId("SYS_J_MESSAGE_EMP", "id"));
			}
			entity.setSendDate(new java.util.Date());
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	public void delete(SysJMessageEmp entity) {
		try {
			entity = entityManager.getReference(SysJMessageEmp.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	public SysJMessageEmp update(SysJMessageEmp entity) {
		try {
			SysJMessageEmp result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public SysJMessageEmp findById(Long id) {
		try {
			SysJMessageEmp instance = entityManager.find(SysJMessageEmp.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<SysJMessageEmp> findByMessageId(Long messageId){
		String sql=
			"select t.* from sys_j_message_emp t where t.id=? and t.message_status=0";
		List<SysJMessageEmp> list=bll.queryByNativeSQL(sql, new Object[]{messageId},SysJMessageEmp.class);
		if(list!=null){
			return list;
		}
		else{
			return null;
		}
	}
	
	public SysJMessageEmp findMessage(Long messageId,String reveiveId){
		String sql=
			"select t.* from sys_j_message_emp t where t.id=? and t.message_status=0 and t.receive_by_id=?";
		List<SysJMessageEmp> list=bll.queryByNativeSQL(sql, new Object[]{messageId,reveiveId},SysJMessageEmp.class);
		if(list!=null){
			SysJMessageEmp model=list.get(0);
			if(model!=null){
				return model;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	public SysJMessageEmp findMessage(Long messageId){
		String sql=
			"select t.* from sys_j_message_emp t where t.id=? and t.message_status=1";
		List<SysJMessageEmp> list=bll.queryByNativeSQL(sql, new Object[]{messageId},SysJMessageEmp.class);
		if(list!=null){
			SysJMessageEmp model=list.get(0);
			if(model!=null){
				return model;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	public Long getNoVeiwMessageCount(String receiveId){
		String sql = "select count(1) from sys_j_message_emp t where receive_by_id=? and message_status=1";
        Object o = bll.getSingal(sql, new Object[]{receiveId});
        if(o!=null)
        	return Long.parseLong(o.toString());
        else
        	return 0l;
	    }
}