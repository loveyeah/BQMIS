/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.form.MaterialNameQueryInfo;

@Stateless
public class MaterialNameQueryImp implements MaterialNameQuery {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 单位*/
	@EJB(beanName = "BpCMeasureUnitFacade")
	BpCMeasureUnitFacadeRemote unitRemote;

	/**
	 * 物料查询
	 * modify by fyyang 090629 增加按物料类别查询
	 * modify by fyyang  091119 flag: "" or null ----全部；  1----------去掉计算机5-E-05%
	 * @param fuzzy
	 *            查询字符串
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return PageObject
	 */
//	public PageObject getMaterialList(String enterpriseCode, String fuzzy,String materialClassCode,String flag,
//			final int... rowStartIdxAndCount) {
	@SuppressWarnings("unchecked")
	public PageObject getMaterialList(String issue,String enterpriseCode, String fuzzy,String materialClassCode,String flag,
			final int... rowStartIdxAndCount) {
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String sql = "SELECT\n" 
				    + "A.MATERIAL_ID, \n"
					+ "A.MATERIAL_NO, A.MATERIAL_NAME, A.SPEC_NO,\n"
					+ "A.PARAMETER, A.STOCK_UM_ID, A.FACTORY,\n"
					+ "A.DOC_NO, B.CLASS_NAME,A.MAX_STOCK, A.QA_CONTROL_FLAG, A.PUR_UM_ID, \n"
					+ "(select sum(C.OPEN_BALANCE + C.RECEIPT + C.ADJUST - C.ISSUE) from inv_j_warehouse C where C.material_id = A.MATERIAL_ID group by C.material_id) as stock \n"//add by ywliu 20091019
					+",A.Std_Cost \n" //add by fyyang 20100114
					// add by liuyi 20100504
					+ " ,nvl(c.qty,0) \n"
					+ " ,B.Class_No  \n" //add by ltong 20100505
					+ "FROM \n" 
					+ "INV_C_MATERIAL A,\n"
					+ "INV_C_MATERIAL_CLASS B \n"
					// add by liuyi 20100504
					+ " ,(select aa.material_id,\n" +
					"     sum(nvl(dd.rcv_qty,0)-nvl(aa.iss_qty,0)) qty\n" + 
					" from mrp_j_plan_requirement_detail aa,\n" + 
					"      pur_j_plan_order              bb,\n" + 
					"      mrp_j_plan_requirement_head   cc,\n" + 
					"      pur_j_order_details           dd\n" + 
					"where aa.requirement_detail_id = bb.requirement_detail_id\n" + 
					"  and aa.approved_qty > nvl(aa.iss_qty, 0)\n" + 
					"  and cc.requirement_head_id = aa.requirement_head_id\n" + 
					"  and cc.mr_status = '2'\n" + 
					"  and bb.pur_no = dd.pur_no\n" + 
					"  and dd.material_id = aa.material_id\n" + 
					"  and dd.rcv_qty>0\n" + 
					"  and nvl(aa.iss_qty, 0) <  nvl(dd.rcv_qty, 0)\n" + 
					"  group by aa.material_id) c \n"

					+ "WHERE \n"
					+ "A.IS_USE = 'Y' AND\n" + "B.IS_USE = 'Y' AND\n"
					+ "(A.MATERIAL_NO LIKE '%" + fuzzy + "%' OR\n"
					+ " A.MATERIAL_NAME LIKE '%" + fuzzy + "%' OR\n"
					+ " A.SPEC_NO LIKE '%" + fuzzy + "%' OR\n"
					+ " B.CLASS_NAME LIKE '%" + fuzzy + "%') AND\n"
					+ "B.MAERTIAL_CLASS_ID = A.MAERTIAL_CLASS_ID AND\n"
					+ "A.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
					+ "B.ENTERPRISE_CODE ='" + enterpriseCode + "' \n"
					// add by liuyi 20100504 
					+ "  and c.material_id(+)=a.material_id \n";
//			if(materialClassId!=null&&!materialClassId.equals(""))
//			{
//				sql+=
//					"  and (  0<>\n" +
//					"(\n" + 
//					"select\n" + 
//					"nvl((\n" + 
//					" select  instr(sys_connect_by_path(aa.maertial_class_id,','),',"+materialClassId+"')  path\n" + 
//					"              from INV_C_MATERIAL_CLASS aa\n" + 
//					"              where aa.maertial_class_id=A.maertial_class_id\n" + 
//					"             start   with  aa.parent_class_no='-1'\n" + 
//					"        connect by nocycle prior aa.class_no=aa.parent_class_no and aa.is_use='Y'\n" + 
//					"        ),0)\n" + 
//					"        from dual\n" + 
//					"        )"+
//					"   or  A.maertial_class_id="+materialClassId+"  )";
//
//			}
		 if(materialClassCode!=null&&!materialClassCode.equals("")&&!materialClassCode.equals("-1"))
			{
				sql+="  and B.Class_No like '"+materialClassCode.trim()+"%'  \n";
			}
		 //add by fyyang 091119
		 if(flag!=null&&flag.equals("1"))
		 {
			 sql+="  and B.Class_No not like  '5-E-05%'   \n";
		 }
		
		 if(issue != null && issue.equals("1")){
			 sql += 
				 "  and (select sum(C.OPEN_BALANCE + C.RECEIPT + C.ADJUST - C.ISSUE) from inv_j_warehouse C where C.material_id = A.MATERIAL_ID group by C.material_id)\n" +
				 "> nvl(c.qty,0) \n";

		 }
		 // modified by liuyi 20100504 
		 String sqlCount= "select count(*) from (" + sql + ") \n";
			sql+="ORDER BY A.MATERIAL_NO \n";
			List<MaterialNameQueryInfo> list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
//			String sqlCount= 
//                "SELECT count(A.MATERIAL_ID) \n"
//                + "FROM \n" 
//				+ "INV_C_MATERIAL A,\n"
//				+ "INV_C_MATERIAL_CLASS B \n" 
//                + "WHERE \n"
//				+ "A.IS_USE = 'Y' AND\n" + "B.IS_USE = 'Y' AND\n"
//				+ "(A.MATERIAL_NO LIKE '%" + fuzzy + "%' OR\n"
//				+ " A.MATERIAL_NAME LIKE '%" + fuzzy + "%' OR\n"
//				+ " A.SPEC_NO LIKE '%" + fuzzy + "%' OR\n"
//				+ " B.CLASS_NAME LIKE '%" + fuzzy + "%') AND\n"
//				+ "B.MAERTIAL_CLASS_ID = A.MAERTIAL_CLASS_ID AND\n"
//				+ "A.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
//				+ "B.ENTERPRISE_CODE ='" + enterpriseCode + "' \n";
//		
//			 if(materialClassCode!=null&&!materialClassCode.equals("")&&!materialClassCode.equals("-1"))
//			{
//				 sqlCount+="  and B.Class_No like '"+materialClassCode.trim()+"%'  \n";
//			}
//			 //add by fyyang 091119
//			 if(flag!=null&&flag.equals("1"))
//			 {
//				 sqlCount+="  and a.material_no not like  '5-E-05%'   \n";
//			 }
//			if(materialClassId!=null&&!materialClassId.equals(""))
//			{
//				sqlCount+=
//					"  and (  0<>\n" +
//					"(\n" + 
//					"select\n" + 
//					"nvl((\n" + 
//					" select  instr(sys_connect_by_path(aa.maertial_class_id,','),',"+materialClassId+"')  path\n" + 
//					"              from INV_C_MATERIAL_CLASS aa\n" + 
//					"              where aa.maertial_class_id=A.maertial_class_id\n" + 
//					"             start   with  aa.parent_class_no='-1'\n" + 
//					"        connect by nocycle prior aa.class_no=aa.parent_class_no and aa.is_use='Y'\n" + 
//					"        ),0)\n" + 
//					"        from dual\n" + 
//					"        )"+
//					"   or  A.maertial_class_id="+materialClassId+"  )";
//
//			}
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
            
			List<MaterialNameQueryInfo> arrlist = new ArrayList<MaterialNameQueryInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				MaterialNameQueryInfo materialInfo = new MaterialNameQueryInfo();
				Object[] data = (Object[]) it.next();
				// ID
				if (null != data[0])
					materialInfo.setMaterialId(data[0].toString());
				// 编码
				if (null != data[1])
					materialInfo.setMaterialNo(data[1].toString());
				// 名称
				if (null != data[2])
					materialInfo.setMaterialName(data[2].toString());
				// 规格型号
				if (null != data[3]) {
					materialInfo.setSpecNo(data[3].toString());
				}
				// 材质/参数
				if (null != data[4])
					materialInfo.setParameter(data[4].toString());
				// 存货计量单位
				if (null != data[5]) {
					materialInfo.setStockUmId(Long
							.parseLong(data[5].toString()));
					// 单位名称
					BpCMeasureUnit unit = unitRemote.findById(materialInfo
							.getStockUmId());
					if(unit != null)
					materialInfo.setStockUmName(unit.getUnitName());
				}
				// 生产厂家
				if (null != data[6])
					materialInfo.setFactory(data[6].toString());
				// 文档号
				if (null != data[7])
					materialInfo.setDocNo(data[7].toString());
				// 名称
				if (null != data[8]) {
					materialInfo.setClassName(data[8].toString());
				}
				// 名称
				if (null != data[9]) {
					materialInfo.setMaxStock(data[9].toString());
				}
				// 是否免检
				if (null != data[10]) {
					materialInfo.setQaControlFlag(data[10].toString());
				}
				// 采购计量单位ID
				if (null != data[11])
					materialInfo.setPurUmId(Long
							.parseLong(data[11].toString()));
				if (null != data[12]) {//add by ywliu 20091019
					materialInfo.setStock(data[12].toString());
				} else {
					materialInfo.setStock("0");
				}
				if(data[13]!=null)
				{
					//add by fyyang 20100114
					materialInfo.setStdCost(Double.parseDouble(data[13].toString()));
				}
				// add by liuyi 20100504 
				if(data[14] != null)
				{
					materialInfo.setInNotUsed(data[14].toString());
					if(materialInfo.getStock() != null){
						materialInfo.setCanUseStock(String.valueOf((Double.parseDouble(materialInfo.getStock()) - Double.parseDouble(materialInfo.getInNotUsed()))));
					}else{
						materialInfo.setCanUseStock("0");
					}
				}
				if(data[15]!=null)
				{
					//物料分类 add by ltong 20100505
					materialInfo.setClassNo(data[15].toString());
				}
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			pobj.setTotalCount(totalCount);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	
	public String getQueryRightByWorkId(Long workId, String fileAddr,String enterpriseCode)
	{
		
		
		String sql=
			"select tt.rightsign\n" +
			"  from SYS_J_QUERYRIGHT tt\n" + 
			" where tt.is_use = 'Y'\n" + 
			"   and tt.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and tt.file_id in\n" + 
			"       (select a.file_id\n" + 
			"          from sys_c_fls a\n" + 
			"         where trim(replace(a.file_addr,'\\','/')) = '"+fileAddr+"'\n" + 
			"           and a.is_use = 'Y'\n" + 
			"           and a.enterprise_code = 'hfdc')\n" + 
			"   and tt.role_id in (select t.role_id\n" + 
			"                        from sys_j_ur t\n" + 
			"                       where t.worker_id = "+workId+"\n" + 
			"                         and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                         and t.is_use = 'Y')";
      Object obj=bll.getSingal(sql);
      if(obj==null||obj.equals(""))
      {
    	  return "";
      }
      else
      {
    	  return obj.toString();
      }
	}

}
