package power.ejb.manage.contract.business;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.opensymphony.workflow.service.WorkflowService;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.contract.form.MaterialDetailsForm;
@Stateless
public class ContractMaterialImp implements ContractMaterial {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;
	public ContractMaterialImp(){
		
	}
	public List<MaterialDetailsForm> findAllMaterialsByContractNo(String contractNo,String enterpriseCdoe){
		String sql =
			"select y.pur_no,\n" +
			"       y.material_id,\n" + 
			"       y.unit_price,\n" + 
			"       y.pur_qty,\n" + 
			"       y.memo,\n" + 
			"       (select r.material_name\n" + 
			"          from inv_c_material r\n" + 
			"         where r.material_id = y.material_id\n" + 
			"           and r.is_use = 'Y') materialName,\n" + 
			"       (select o.spec_no\n" + 
			"          from inv_c_material o\n" + 
			"         where o.material_id = y.material_id\n" + 
			"           and o.is_use = 'Y') specModel,\n" + 
			"        (y.unit_price*y.pur_qty) total\n" + 
			"  from PUR_J_ORDER_DETAILS y\n" + 
			" where y.pur_no in (select t.pur_no\n" + 
			"                      from pur_j_order t\n" + 
			"                     where t.contract_no = '"+contractNo+"'\n" + 
			"                       and t.is_use = 'Y') \n" + 
			"   and y.is_use = 'Y'  and y.enterprise_code = '"+enterpriseCdoe+"'";
		List list = bll.queryByNativeSQL(sql);
		List<MaterialDetailsForm> arrayList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			MaterialDetailsForm model=new MaterialDetailsForm();
			Object[] o = (Object[]) it.next();
			if (o[0] != null)
				model.setPurNo(o[0].toString());
			if (o[1] != null)
				model.setMaterialId(Long.parseLong(o[1].toString()));
			if (o[2] != null)
				model.setUnitPrice(Double.parseDouble(o[2].toString()));
			if (o[3] != null)
				model.setPurqty(Double.parseDouble(o[3].toString()));
			if (o[4] != null)
				model.setMemo(o[4].toString());
			if (o[5] != null)
				model.setMaterialName(o[5].toString());
			if (o[6] != null)
				model.setSpecModel(o[6].toString());
			if (o[7] != null)
				model.setTotal(Double.parseDouble(o[7].toString()));
			arrayList.add(model);
		}
		return arrayList;
	}
	public void updateContractMaterial(String purNo,String contractNo,String enterpriseCode,String method){
		String sql ="";
		if("select".equals(method)){
		      sql = "update pur_j_order t set t.contract_no = '"+contractNo+"' where t.pur_no = '"+
		              purNo+"' and t.enterprise_code = '"+enterpriseCode+"'";
		}else{
			  sql = "update pur_j_order t set t.contract_no = '' where t.pur_no = '"+
            purNo+"' and t.enterprise_code = '"+enterpriseCode+"'";
		}
		bll.exeNativeSQL(sql);
	}
	public List<MaterialDetailsForm> findAllMaterialsByConId(Long conId,String enterpriseCode){
		String sql = 
			"select y.pur_no,\n" +
			"        y.material_id,\n" + 
			"        y.unit_price,\n" + 
			"        y.pur_qty,\n" + 
			"        y.memo,\n" + 
			"        (select r.material_name\n" + 
			"           from inv_c_material r\n" + 
			"          where r.material_id = y.material_id\n" + 
			"            and r.is_use = 'Y') materialName,\n" + 
			"        (select o.spec_no\n" + 
			"           from inv_c_material o\n" + 
			"          where o.material_id = y.material_id\n" + 
			"            and o.is_use = 'Y') specModel,\n" + 
			"        (y.unit_price * y.pur_qty) total\n" + 
			"   from PUR_J_ORDER_DETAILS y\n" + 
			"  where y.pur_no in\n" + 
			"        (select t.pur_no\n" + 
			"           from pur_j_order t\n" + 
			"          where t.contract_no = (select h.conttrees_no\n" + 
			"                                   from con_j_contract_info h\n" + 
			"                                  where h.con_id ="+conId+"\n" + 
			"                                    and h.is_use = 'Y')\n" + 
			"            and t.is_use = 'Y')\n" + 
			"    and y.is_use = 'Y'\n" + 
			"    and y.enterprise_code = '"+enterpriseCode+"'";
;
		List list = bll.queryByNativeSQL(sql);
		List<MaterialDetailsForm> arrayList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			MaterialDetailsForm model=new MaterialDetailsForm();
			Object[] o = (Object[]) it.next();
			if (o[0] != null)
				model.setPurNo(o[0].toString());
			if (o[1] != null)
				model.setMaterialId(Long.parseLong(o[1].toString()));
			if (o[2] != null)
				model.setUnitPrice(Double.parseDouble(o[2].toString()));
			if (o[3] != null)
				model.setPurqty(Double.parseDouble(o[3].toString()));
			if (o[4] != null)
				model.setMemo(o[4].toString());
			if (o[5] != null)
				model.setMaterialName(o[5].toString());
			if (o[6] != null)
				model.setSpecModel(o[6].toString());
			if (o[7] != null)
				model.setTotal(Double.parseDouble(o[7].toString()));
			arrayList.add(model);
		}
		return arrayList;
	}
	public void updateContractMaterialByConId(String purNo,String conId,String enterpriseCode,String method){
		String sql ="";
		if("select".equals(method)){
		      sql = "update pur_j_order t\n" +
		    	  "   set t.contract_no = (select c.conttrees_no\n" + 
		    	  "                          from con_j_contract_info c\n" + 
		    	  "                         where c.con_id ='"+conId+"')\n" + 
		    	  "  where t.pur_no ='"+ purNo+"'\n" + 
		    	  "   and t.enterprise_code = '"+enterpriseCode+"'";

		}else{
			sql = "update pur_j_order t\n" +
	    	  "   set t.contract_no = ''\n" + 
	    	  "  where t.pur_no ='"+ purNo+"'\n" + 
	    	  "   and t.enterprise_code = '"+enterpriseCode+"'";
		}
		bll.exeNativeSQL(sql);
	}
}
