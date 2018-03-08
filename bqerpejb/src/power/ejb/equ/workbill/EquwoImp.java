package power.ejb.equ.workbill;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.EquCStandardMainmat;
import power.ejb.equ.standardpackage.EquCStandardMainmatFacadeRemote;
import power.ejb.equ.standardpackage.EquCStandardManplanFacadeRemote;
import power.ejb.equ.standardpackage.EquCStandardToolsFacadeRemote;
 /**
 * @author sltang
 *
 */
@Stateless
public class EquwoImp implements EquwoInterface {
	
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "EquJOtmaFacade")
	protected EquJOtmaFacadeRemote oRemote;
	@EJB(beanName = "EquJWorkticketsFacade")
	protected EquJWorkticketsFacadeRemote wRemote;
	@EJB(beanName = "EquJWoFacade")
	protected EquJWoFacadeRemote remote;
	@EJB(beanName = "EquJMainmatFacade")
	protected EquJMainmatFacadeRemote mremote;
	@EJB(beanName = "EquJToolsFacade")
	protected EquJToolsFacadeRemote tremote;
	@EJB(beanName = "EquJManplanFacade")
	protected EquJManplanFacadeRemote maremote;
	
	public void endEquwo(EquJWo entity,String worktickectCodes,String matCodes){
		try{
			if(entity!=null){
				remote.update(entity, null);
				if(worktickectCodes!=null){
					wRemote.deleteMutil(entity.getWoCode(), worktickectCodes);
				}
				if(matCodes!=null){
					oRemote.deleteMulti(entity.getWoCode(), matCodes, entity.getEnterprisecode());
				}
				
			}
			
		}catch(RuntimeException re){
			throw re;
		}
		
		
	}
}
