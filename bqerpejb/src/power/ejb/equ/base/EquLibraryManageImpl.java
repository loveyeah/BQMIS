package power.ejb.equ.base;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.business.MaterialQueryAndSelect;
import power.ejb.resource.form.MaterialQueryAndSelectInfo;

@Stateless
public class EquLibraryManageImpl implements EquLibraryManage {
	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public EquLibraryManageImpl() {
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("NativeSqlHelper");
		service = new WorkflowServiceImpl();
	}

		@SuppressWarnings("unchecked")
	public PageObject find(String fuzzy,String fuzzy1, String enterpriseCode, final int... rowStartIdxAndCount) {
		 
	        	// 查询sql
	        	String sql =
	        		"SELECT\n" +
	        			"A.MATERIAL_ID,\n" +
	        			"A.MATERIAL_NO,\n" +
	        			"A.MATERIAL_NAME,\n" +
	        			"nvl(A.MAX_STOCK,0),\n" +
	        			"nvl(A.MIN_STOCK,0),\n" +      			
	        			"A.SPEC_NO,\n" +
	        			"getunitname(A.stock_um_id),\n" +
	        			"B.OPEN_BALANCE + B.RECEIPT + B.ADJUST - B.ISSUE ,\n"+ 
	        			"nvl(A.MAX_STOCK,0)-(B.OPEN_BALANCE + B.RECEIPT + B.ADJUST - B.ISSUE) \n"+ 
	        		
	        		"FROM\n" +
	        			"INV_C_MATERIAL A,\n"+ 
	        		     "INV_J_LOT  B\n"  ;
	        	
	        	// 查询记录条数
	        	String sqlCount =
	        		"SELECT\n" +
	    			"count(1)\n" +
	    		"FROM\n" +
	    			"INV_C_MATERIAL A,\n" +
	    			"INV_J_LOT  B\n"  ;
	    		
	    		
	        	// 查询时的where条件
	        	String sqlWhere =
	        		"WHERE\n" +
	    			"A.IS_USE = 'Y' AND\n" +
	    			"B.MATERIAL_ID = A.MATERIAL_ID AND\n" +
	    		
	    			"  A.MATERIAL_NAME  like '%"+fuzzy+"%' AND\n" + 
	    			"  A.SPEC_NO  like '%"+fuzzy1+"%' AND\n" +
	    			" (B.OPEN_BALANCE + B.RECEIPT + B.ADJUST - B.ISSUE)< nvl(A.MIN_STOCK,0)  AND\n" + 
	    			" B.enterprise_code = '"+enterpriseCode+"' AND\n"+
	    			
	    			" A.enterprise_code = '"+enterpriseCode+"'\n";
	        	
	    			sqlWhere+=	"ORDER BY\n" +
	    			"A.MATERIAL_NO";
	        	sql += sqlWhere;
	        	sqlCount += sqlWhere;
	       
	        	List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	    		PageObject object = new PageObject();
	    		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
	    		object.setList(list);
	    		object.setTotalCount(totalCount);
	    		return object;

	        

		}
		
		//add by ypan 20100919
		public PageObject findQueryList(String enterpriseCode,String materialName, String modelName, final int... rowStartIdxAndCount) {
			 
        	// 查询sql
        	String sql =

        		"select t.material_no,t.material_name,t.spec_no,getunitname(t.stock_um_id),B.OPEN_BALANCE + B.RECEIPT + B.ADJUST - B.ISSUE,nvl(t.min_stock,0),nvl(t.max_stock,0) from INV_C_MATERIAL t,INV_J_LOT b\n" +
        		"where t.material_id=b.material_id\n" + 
        		" and t.is_use='Y'\n" + 
        		" and t.is_use='Y'\n"+
        		"and t.enterprise_code= '"+enterpriseCode+"'\n"+
        		"and b.enterprise_code= '"+enterpriseCode+"'\n";
        	
        	String sqlCount=
        		"select count(1)  from INV_C_MATERIAL t,INV_J_LOT b\n" +
        		"  where t.material_id=b.material_id\n" + 
        		"   and t.is_use='Y'\n" + 
        		"   and t.is_use='Y'\n"+
        		"and t.enterprise_code= '"+enterpriseCode+"'\n"+
        		"and b.enterprise_code= '"+enterpriseCode+"'\n";


        	 if(materialName!=null&&!materialName.equals("")){
        		 sql+="and t.material_name like '%"+materialName+"%'";
        		 sqlCount+="and t.material_name like '%"+materialName+"%'";
        	 }
        	 if(modelName!=null&&!modelName.equals("")){
        		 sql+="and t.spec_no like '%"+modelName+"%'";
        		 sqlCount+="and t.spec_no like '%"+modelName+"%'";
        	 }
        	List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
    		PageObject object = new PageObject();
    		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
    		object.setList(list);
    		object.setTotalCount(totalCount);
    		return object;

        

	}

	
}