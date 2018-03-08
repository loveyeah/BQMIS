package power.ejb.equ.workbill;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.workbill.form.Materiel;
import power.ejb.hr.LogUtil;
import power.ejb.resource.InvJIssueHead;
/**
 * Facade for entity EquJOtma.
 * 
 * @see power.ejb.equ.workbill.EquJOtma
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJOtmaFacade implements EquJOtmaFacadeRemote {
	// property constants
	public static final String WO_CODE = "woCode";
	public static final String MAT_CODE = "matCode";
	public static final String ENTERPRISECODE = "enterprisecode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	/**
	 * Perform an initial save of a previously unsaved EquJOtma entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJOtma entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJOtma entity) {
		try {
			entity.setId(bll.getMaxId("EQU_J_OTMA", "ID"));
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Delete a persistent EquJOtma entity.
	 */
	public void delete(EquJOtma entity) {
		LogUtil.log("deleting EquJOtma instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJOtma.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void delMateriel(String matCodes,String enterprisecode) {
		try{ 
			String sql = "delete EQU_J_OTMA t where t.mat_code in ('"
					+ matCodes + "') " + "and t.enterprisecode='" + enterprisecode + "'";
			bll.exeNativeSQL(sql);
		}catch(RuntimeException re){
		}
		
		
	}
	/**
	 * Persist a previously saved EquJOtma entity and return it or a copy of it
	 * to the sender. A copy of the EquJOtma entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJOtma entity to update
	 * @return EquJOtma the persisted EquJOtma entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJOtma update(EquJOtma entity) {
		LogUtil.log("updating EquJOtma instance", Level.INFO, null);
		try {
			EquJOtma result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJOtma findById(Long id) {
		LogUtil.log("finding EquJOtma instance with id: " + id, Level.INFO,
				null);
		try {
			EquJOtma instance = entityManager.find(EquJOtma.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public List<EquJOtma> findBywoCode(String woCode) {
		try {
			String sql = "select t.* from EQU_J_OTMA t where t.wo_code='"
					+ woCode + "'";
			List<EquJOtma> matCode = bll.queryByNativeSQL(sql,EquJOtma.class);
			return matCode;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void deleteMulti(String woCode, String matCodes,
			String enterprisecode) {
		try {
			String sql = "delete EQU_J_OTMA t where t.mat_code in ("
					+ matCodes + ") " + " and t.wo_code='" + woCode + "'"
					+ "and t.enterprisecode='" + enterprisecode + "'";
			bll.exeNativeSQL(sql);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	public void deleteWorkbillMaterialRelation(String woCode,String enterprisecode){
		try{
			String sql = "delete EQU_J_OTMA t where t.wo_code = '"+woCode+"'"
			             +" and t.enterprisecode='"+enterprisecode+"'";
			bll.exeNativeSQL(sql);
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	//  根据工单号，查询物料列表
	public PageObject relateMaterielList(String woCode) {
		List<EquJOtma> listCode = this.findBywoCode(woCode);
		StringBuffer codes = new StringBuffer(); 
		for(int i=0;i<listCode.size();i++){
			EquJOtma model = new EquJOtma();
			 model = listCode.get(i);
			if(i == listCode.size()-1)
				codes.append("'"+model.getMatCode()+"'");
			else
				codes.append("'"+model.getMatCode()+"'"+",");
			
		}
		if (codes.length() == 0 || codes.equals("") || codes == null){
			return null;	
		} else{
		PageObject result = new PageObject();
		String sql = "";
		sql += 	
			"select distinct(b.issue_no),\n" +
			"getworkername(b.receipt_by),\n" + 
			"c.class_name,\n" + 
			"b.issue_status,\n" + 
			"d.material_no,\n" + 
			"d.material_name,\n" + 
			"d.spec_no,\n" + 
			"e.applied_count,\n" + 
			"e.act_issued_count,\n" + 
			"d.stock_um_id,\n" + 
			"decode(b.issue_status,\n" + 
			"0,'待审批状态',\n" + 
			"1,'审批中状态',\n" + 
			"2,'审批结束状态',\n" + 
			"9,'退回状态'\n" + 
			"),(select g.unit_name from bp_c_measure_unit g where g.unit_id=d.stock_um_id) unit_name,\n" + 
			"b.work_flow_no,\n" + 
			"b.issue_head_id,\n" +
			"e.material_id\n" + 
			"from INV_J_ISSUE_HEAD b,\n" + 
			"INV_C_MATERIAL_CLASS c,\n" + 
			" INV_C_MATERIAL d, INV_J_ISSUE_DETAILS e\n" + 
			"where b.ISSUE_NO in ("+codes+")\n" + 
			"and e.issue_head_id=b.issue_head_id\n" + 
			"and e.material_id = d.material_id\n" + 
			"and c.maertial_class_id=d.maertial_class_id\n" + 
			"and b.is_use='Y'\n" + 
			"and d.is_use='Y'\n" + 
			"and c.is_use='Y'\n" + 
			"and e.is_use='Y'\n" + 
			"order by b.issue_no";

		List list = bll.queryByNativeSQL(sql);
		if (list != null) {
			List<Materiel> arr = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] ob = (Object[]) list.get(i);
				Materiel model = new Materiel();
				if (ob[0] != null)
					model.setMatCode(ob[0].toString());
				if (ob[1] != null)
					model.setReceiptBy(ob[1].toString());
				
				if (ob[2] != null)
					model.setClassName(ob[2].toString());
				
				if (ob[3] != null)
					model.setIssueStatus(ob[3].toString());
				
				if (ob[4] != null)
					model.setMaterialNo(ob[4].toString());
				
				if (ob[5] != null)
					model.setMaterialName(ob[5].toString());
				
				if (ob[6] != null)
					model.setSpecNo(ob[6].toString());

				if (ob[7] != null)
					model.setAppliedCount(Double.parseDouble(ob[7].toString()));
				
				if (ob[8] != null)
					model.setActIssuedCount(Double
							.parseDouble(ob[8].toString()));
				if (ob[9] != null)
					model.setStockUmId(Long.parseLong(ob[9].toString()));
//				
//				if (ob[10] != null)
//					model.setUnitPrice(Double.parseDouble(ob[10].toString()));
//				
//				if (ob[11] != null)
//					model.setTotalPrice(Double.parseDouble(ob[11].toString()));
				
				if (ob[10] != null)
					model.setStatusFlag(ob[10].toString());
				if(ob[11] !=null)
					model.setUnitName(ob[11].toString());
				if(ob[12] !=null)
					model.setWorkflowNo(ob[12].toString());
				if(ob[13] !=null)
					model.setIssueHeadId(ob[13].toString());
				if(ob[14] !=null)
					model.setMaterialId(ob[14].toString());
				arr.add(model);
			}
			result.setList(arr);
			return result;
		} else {
			return null;
		}
	}}
}