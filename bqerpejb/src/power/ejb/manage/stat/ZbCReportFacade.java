package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.InvCMaterialClass;

/**
 * Facade for entity ZbCReport.
 * 
 * @author drdu 20100603
 */
@Stateless
public class ZbCReportFacade implements ZbCReportFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(ZbCReport entity) {
		LogUtil.log("saving ZbCReport instance", Level.INFO, null);
		try {
			if(entity.getReportId() == null)
			{
				entity.setReportId(bll.getMaxId("zb_c_report", "report_id"));
			}
			entity.setIsUse("Y");
			entity.setReportCode(this.createReportNoMethod(entity.getFaReprotCode()));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ZbCReport entity) {
		LogUtil.log("deleting ZbCReport instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(ZbCReport.class, entity
					.getReportId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ZbCReport update(ZbCReport entity) {
		LogUtil.log("updating ZbCReport instance", Level.INFO, null);
		try {
			ZbCReport result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ZbCReport findById(Long id) {
		LogUtil.log("finding ZbCReport instance with id: " + id, Level.INFO,
				null);
		try {
			ZbCReport instance = entityManager.find(ZbCReport.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from  zb_c_report \n";
			if (strWhere != "") {
				sql = sql + " where  " + strWhere;
			}
			List list = bll.queryByNativeSQL(sql, ZbCReport.class,
					rowStartIdxAndCount);
			String sqlCount = "select count(*)　from zb_c_report \n";
			if (strWhere != "") {
				sqlCount = sqlCount + " where  " + strWhere;
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ZbCReport> findReportTreeList(String fReportCode,String enterpriseCode) {
		String strWhere = "";
		if (fReportCode.equals("root")) {
			strWhere = "   fa_reprot_code = '0'  \n";
		} else {
			strWhere = " fa_reprot_code='" + fReportCode + "' \n";
		}
		strWhere = strWhere + "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by report_code asc";
		PageObject result = findList(strWhere);
		return result.getList();
	}

	public boolean IfHasChild(String code, String enterpriseCode) {
		boolean isSame = false;
		String strWhere = "";
		if (code.equals("root")) {
			strWhere = "   fa_reprot_code = '0'  \n";
		} else {
			strWhere = " fa_reprot_code='" + code + "' \n";
		}
		strWhere = strWhere + "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by report_id";
		String sql = "select count(1)\n" + "  from zb_c_report t\n"
				+ " where " + strWhere;
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
	
	public ZbCReport findByCode(String reportId, String enterpriseCode) {
		String strWhere = "  report_code='" + reportId + "' \n"
				+ " and  enterprise_code='" + enterpriseCode + "' \n"
				+ " and is_use='Y'";
		PageObject result = findList(strWhere);
		if (result.getList() != null) {
			if (result.getList().size() > 0) {
				return (ZbCReport) result.getList().get(0);
			}
		}
		return null;
	}

	public Object findReportInfo(String reportCode) {
		String sql = 
			"select a.report_id,\n" +
			"       a.report_code,\n" + 
			"       a.report_name,\n" + 
			"       a.fa_reprot_code,\n" + 
			"       a.report_type,\n" + 
			"       a.last_modifier_by,\n" + 
			"       getworkername(a.last_modifier_by),\n" + 
			"       to_char(a.last_modifier_date, 'yyyy-MM-dd')\n" + 
			"  from zb_c_report a\n" + 
			" where a.report_code = '"+reportCode+"'";

		return bll.getSingal(sql);
	}
	
	 @SuppressWarnings("unused")
	private String createReportNoMethod(String parentReportNo)
	    {
	    	String sql="";
	    	if(parentReportNo.equals("0")||parentReportNo.length()==1)
	    	{
	    		sql=
	    			"select chr(ascii(substr(max(t.report_code),\n" +
	    			"                        instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                        1)) +\n" + 
	    			"           decode(ascii(substr(max(t.report_code),\n" + 
	    			"                               instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                               1)),\n" + 
	    			"                  57,\n" + 
	    			"                  8,\n" + 
	    			"                  90,\n" + 
	    			"                  7,\n" + 
	    			"                  1))\n" + 
	    			"  from zb_c_report t\n" + 
	    			" where t.fa_reprot_code = '"+parentReportNo+"'\n" + 
	    			"   and t.is_use = 'Y'";
	    		Object obj=bll.getSingal(sql);
	    		if(obj==null||obj.equals(""))
	    		{
	    			if(parentReportNo.equals("0")) return "1";
	    			else return parentReportNo+"-1";
	    				
	    		}
	    		else
	    		{
	    			if(parentReportNo.equals("0")) return obj.toString().trim();
	    			else return parentReportNo+"-"+obj.toString().trim();
	    		}

	    	}
	    	else
	    	{
	    		sql=
	    			"select case\n" +
	    			"         when substr(max(t.report_code), instr(max(t.report_code), '-', -1) + 1, 2) = '99' then\n" + 
	    			"          'A1'\n" + 
	    			"         else\n" + 
	    			"\n" + 
	    			"          decode(instr('0123456789',\n" + 
	    			"                       substr(max(t.report_code),\n" + 
	    			"                              instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                              1)),\n" + 
	    			"                 '0',\n" + 
	    			"                 decode(substr(max(t.report_code),\n" + 
	    			"                               instr(max(t.report_code), '-', -1) + 2,\n" + 
	    			"                               1),\n" + 
	    			"                        'Z',\n" + 
	    			"                        chr(ascii(substr(max(t.report_code),\n" + 
	    			"                                         instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                                         1)) +\n" + 
	    			"                            decode(ascii(substr(max(t.report_code),\n" + 
	    			"                                                instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                                                1)),\n" + 
	    			"                                   57,\n" + 
	    			"                                   8,\n" + 
	    			"                                   90,\n" + 
	    			"                                   7,\n" + 
	    			"                                   1)) || '1',\n" + 
	    			"                        substr(max(t.report_code),\n" + 
	    			"                               instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                               1) ||\n" + 
	    			"                        chr(ascii(substr(max(t.report_code),\n" + 
	    			"                                         instr(max(t.report_code), '-', -1) + 2,\n" + 
	    			"                                         1)) +\n" + 
	    			"                            decode(ascii(substr(max(t.report_code),\n" + 
	    			"                                                instr(max(t.report_code), '-', -1) + 2,\n" + 
	    			"                                                1)),\n" + 
	    			"                                   57,\n" + 
	    			"                                   8,\n" + 
	    			"                                   90,\n" + 
	    			"                                   7,\n" + 
	    			"                                   1))),\n" + 
	    			"                 to_char(to_number(substr(max(t.report_code),\n" + 
	    			"                                          instr(max(t.report_code), '-', -1) + 1,\n" + 
	    			"                                          2)) + 1,\n" + 
	    			"                         '00'))\n" + 
	    			"\n" + 
	    			"       end\n" + 
	    			"\n" + 
	    			"  from zb_c_report t\n" + 
	    			" where t.fa_reprot_code = '"+parentReportNo+"'\n" + 
	    			"   and t.is_use = 'Y'";
	    		
	                 Object obj=bll.getSingal(sql);
	                 if(obj==null||obj.equals(""))
	                 {
	                	 return parentReportNo+"-00";
	                 }
	                 else
	                 {
	                	 return parentReportNo+"-"+obj.toString().trim();
	                 }
	    	}
	    }
}