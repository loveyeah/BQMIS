package power.ejb.productiontec.chemistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.chemistry.form.CondenserLeakForm;

/**
 * Facade for entity PtHxjdJNqqxlDetail.
 * 
 * @see power.ejb.productiontec.chemistry.PtHxjdJNqqxlDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtHxjdJNqqxlDetailFacade implements PtHxjdJNqqxlDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtHxjdJNqqxlDetail save(PtHxjdJNqqxlDetail entity) {
		LogUtil.log("saving PtHxjdJNqqxlDetail instance", Level.INFO, null);
		try {
			entity.setNqjxlDetailId(bll.getMaxId("PT_HXJD_J_NQQXL_DETAIL", "nqjxl_detail_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String ids) {
		String sql = "delete PT_HXJD_J_NQQXL_DETAIL b where b.nqjxl_detail_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
	}
	
	public PtHxjdJNqqxlDetail update(PtHxjdJNqqxlDetail entity) {
		LogUtil.log("updating PtHxjdJNqqxlDetail instance", Level.INFO, null);
		try {
			PtHxjdJNqqxlDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtHxjdJNqqxlDetail findById(Long id) {
		LogUtil.log("finding PtHxjdJNqqxlDetail instance with id: " + id,
				Level.INFO, null);
		try {
			PtHxjdJNqqxlDetail instance = entityManager.find(
					PtHxjdJNqqxlDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String nqjxlId,String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql =  "select a.nqjxl_detail_id,\n" +
				"       a.nqjxl_id,\n" + 
				"       a.project_names,\n" + 
				"       a.item_name1,\n" + 
				"       a.item_name2,\n" + 
				"       a.item_name3,\n" + 
				"       a.item_name4,\n" + 
				"       (b.nqjxl_id)nqjxl_id_detail\n" + 
				"  from pt_hxjd_j_nqqxl_detail a, pt_hxjd_j_nqqxl b\n" + 
				" where a.nqjxl_id = b.nqjxl_id(+)\n" ;
				//+ "   and a.enterprise_code = '"+enterpriseCode+"'";

			String sqlCount =  "select count(1)\n" +
				"  from pt_hxjd_j_nqqxl_detail a, pt_hxjd_j_nqqxl b\n" + 
				" where a.nqjxl_id = b.nqjxl_id(+)\n" ;
				//+ "   and a.enterprise_code = '"+enterpriseCode+"'";

			if(nqjxlId !=null && !(nqjxlId.equals("")))
			{
				sql = sql + " and b.nqjxl_id =" + nqjxlId + " \n";
				sqlCount = sqlCount + " and b.nqjxl_id =" + nqjxlId + " \n";
			}
			
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list != null && list.size() > 0) {
				while (it.hasNext()) {
					CondenserLeakForm form = new CondenserLeakForm();
					Object[] data = (Object[]) it.next();
					form.setNqjxlDetailId(Long.parseLong(data[0].toString()));
					if(data[1] != null)
						form.setNqjxlId(Long.parseLong(data[1].toString()));
					if(data[2] != null)
						form.setProjectNames(data[2].toString());
					if(data[3] != null)
						form.setItemName1(Double.parseDouble(data[3].toString()));
					if(data[4] != null)
						form.setItemName2(Double.parseDouble(data[4].toString()));
					if(data[5] != null)
						form.setItemName3(Double.parseDouble(data[5].toString()));
					if(data[6] != null)
						form.setItemName4(Double.parseDouble(data[6].toString()));
					if(data[7] != null)
						form.setNqjxlIdDet(Long.parseLong(data[7].toString()));
					
					arrlist.add(form);
				}
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
}