/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.MaterialQueryAndSelectInfo;

/**
 * 库存物料查询及选择Implements
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class MaterialQueryAndSelectImpl implements MaterialQueryAndSelect {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
    
	/**
	 * 查询库存物料
	 * modify by fyyang 090629 增加物料类别查询条件
	 * modify by fyyang 090702 不关联库位表
	 * @param fuzzy 模糊查询参数
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数（开始行数和查询行数）
	 * @return 库存物料
	 */
	@SuppressWarnings("unchecked")
	public PageObject findMaterial(String fuzzy, String enterpriseCode,String materialClassCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding MaterialQueryAndSelectInfo instance with fuzzy: " + fuzzy,
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// 查询sql
        	String sql =
        		"SELECT\n" +
        			"A.MATERIAL_ID,\n" +
        			"A.MATERIAL_NO,\n" +
        			"A.MATERIAL_NAME,\n" +
        			"A.SPEC_NO,\n" +
        			"A.STOCK_UM_ID,\n" +
        			"B.WHS_NO,\n" +
        			"B.WHS_NAME,\n" +
        			//"C.LOCATION_NO,\n" +
        			"D.location_no,\n" +
        			"D.location_no locationName,\n" +
        			"D.LOT_NO,\n" +
        			"to_char(E.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') LAST_MODIFIED_DATE1,\n" + 
        			"to_char(E.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') LAST_MODIFIED_DATE2,\n" +
        			"D.OPEN_BALANCE + D.RECEIPT + D.ADJUST - D.ISSUE as qty,\n" +
        			"to_char(D.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') LAST_MODIFIED_DATE3,\n" +
        			"A.std_cost \n"+
        		"FROM\n" +
        			"INV_C_MATERIAL A,\n" +
        			"INV_C_WAREHOUSE B,\n" +
        		//	"INV_C_LOCATION C,\n" +
        			"INV_J_LOT D,\n" +
        			"INV_J_WAREHOUSE E\n" ;
        		//	"INV_J_LOCATION F\n";
        	// 查询记录条数
        	String sqlCount =
        		"SELECT\n" +
    			"count(1)\n" +
    		"FROM\n" +
    			"INV_C_MATERIAL A,\n" +
    			"INV_C_WAREHOUSE B,\n" +
    			//"INV_C_LOCATION C,\n" +
    			"INV_J_LOT D,\n" +
    			"INV_J_WAREHOUSE E\n" ;
    		//	"INV_J_LOCATION F\n";
        	// 查询时的where条件
        	String sqlWhere =
        		"WHERE\n" +
    			"D.MATERIAL_ID = A.MATERIAL_ID AND\n" +
    			"D.WHS_NO = B.WHS_NO AND\n" +
    		//	"D.LOCATION_NO = C.LOCATION_NO(+) AND\n" +
    			"E.MATERIAL_ID = D.MATERIAL_ID AND\n" +
    			"E.WHS_NO = D.WHS_NO " +
    			
    			" AND D.IS_USE = 'Y' AND\n" +
    			"A.IS_USE = 'Y' AND\n" +
    			"B.IS_USE = 'Y' AND\n" +
    		//	"C.IS_USE(+) = 'Y' AND\n" +
    			"E.IS_USE = 'Y' AND\n" +
    		//	"F.IS_USE(+) = 'Y' AND\n" +
    			"B.IS_INSPECT = 'N' AND\n" +
    			"A.ENTERPRISE_CODE = ? AND\n" +
    			"B.ENTERPRISE_CODE = ? AND\n" +
    			//"C.ENTERPRISE_CODE(+) = ? AND\n" +
    			"D.ENTERPRISE_CODE = ? AND\n" +
    			"E.ENTERPRISE_CODE = ? \n";
    		//	"F.ENTERPRISE_CODE(+) = ?\n" ;
        	//-----------add by fyyang 090623----------
//        	if(materialClassId!=null&&!materialClassId.equals(""))
//        	{
//        	sqlWhere+=
//        	"  and (  0<>\n" +
//			"(\n" + 
//			"select\n" + 
//			"nvl((\n" + 
//			" select  instr(sys_connect_by_path(aa.maertial_class_id,','),',"+materialClassId+"')  path\n" + 
//			"              from INV_C_MATERIAL_CLASS aa\n" + 
//			"              where aa.maertial_class_id=A.maertial_class_id\n" + 
//			"             start   with  aa.parent_class_no='-1'\n" + 
//			"        connect by nocycle prior aa.class_no=aa.parent_class_no and aa.is_use='Y'\n" + 
//			"        ),0)\n" + 
//			"        from dual\n" + 
//			"        )"+
//			"   or  A.maertial_class_id="+materialClassId+"  )";
//        	}
        	//-----------------------------------------
        	//-----------add by fyyang 090629----------
//        	if(materialClassCode!=null&&!materialClassCode.equals(""))
//        	{
//        		sqlWhere+=
//        			"   and  (select tt.class_no from inv_c_material_class tt\n" +
//        			"where tt.maertial_class_id=A.MAERTIAL_CLASS_ID and rownum=1) like '"+materialClassCode.trim()+"%'  \n";
//
//        	}
        	//-----------------------------------------
        	
    			sqlWhere+=	"ORDER BY\n" +
    			"A.MATERIAL_NO";
        	sql += sqlWhere;
        	sqlCount += sqlWhere;
        	if(fuzzy == null || "".equals(fuzzy)) {
        		fuzzy = "";
        	}
        	fuzzy = "%" + fuzzy + "%";
        	// modify by liuyi 091103 
        	String querySql = "select b.material_id, \n"
        		+ "       b.material_no, \n"
        		+ "       b.material_name, \n"
        		+ "       b.spec_no, \n"
        		+ "       b.stock_um_id, \n"
        		+ "       b.default_whs_no, \n"
        		+ "       getwhsname(b.default_whs_no), \n"
        		+ "       b.default_location_no, \n"
        		+ "       b.default_location_no location_name, \n"
        		+ "       a.LOT_NO, \n"
        		+ "       a.LAST_MODIFIED_DATE1, \n"
        		+ "       a.LAST_MODIFIED_DATE2, \n"
        		+ "       a.qty, \n"
        		+ "       a.LAST_MODIFIED_DATE3, \n"
        		+ "       b.std_cost \n"
        		+ "  from ( " 
        		+ sql 
        		+ ") a, \n"
        		+ "       inv_c_material b \n"
        		+ "where a.MATERIAL_ID(+) = b.material_id \n"
        		+ "and b.enterprise_code='" + enterpriseCode + "' \n"
        		+ "and b.is_use='Y' \n"
        		+ "AND (a.LOT_NO LIKE ? OR\n" +
    			"b.default_whs_no LIKE ? OR\n" +
    			"getwhsname(b.default_whs_no)like ? OR\n" +
    			"b.MATERIAL_NO LIKE ? OR\n" +
    			"b.MATERIAL_NAME LIKE ? OR\n" +
    			"b.SPEC_NO LIKE ?) " ;
        	
        	if(materialClassCode!=null&&!materialClassCode.equals(""))
        	{
        		querySql+=
        			"   and  (select tt.class_no from inv_c_material_class tt\n" +
        			"where tt.maertial_class_id=b.MAERTIAL_CLASS_ID and rownum=1) like '"+materialClassCode.trim()+"%'  \n";

        	}
        	querySql += "order by a.material_no \n";
//        	List list = bll.queryByNativeSQL(sql,
//        	new Object[]{fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, fuzzy,
//        			enterpriseCode, enterpriseCode,
//        			enterpriseCode, enterpriseCode}, rowStartIdxAndCount);
        	List list = bll.queryByNativeSQL(querySql,
        			new Object[]{enterpriseCode, enterpriseCode,
        			enterpriseCode, enterpriseCode,
        			fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, fuzzy
        			}, rowStartIdxAndCount);
        	List<MaterialQueryAndSelectInfo> arrlist = new ArrayList<MaterialQueryAndSelectInfo>();
            Iterator it = list.iterator();
            // 格式化
            String patternNumber = "###,###,###,###,##0.0000";
    		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
    		//-----计算当前页的记录汇总-------add by fyyang -------------
    		Double totalNowCount=0.00; //当前总库存
    		Double totalStdCost=0.00;//当前总标准成本
    		Double totalAllCost=0.00;//当前总金额
    		//--------------------------------------------------------
            while (it.hasNext()) {
            	MaterialQueryAndSelectInfo info = new MaterialQueryAndSelectInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setMaterialId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setMaterialNo(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setMaterialName(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setSpecNo(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setStockUmId(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setWhsNo(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setWhsName(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setLocationNo(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setLocationName(data[8].toString());
            	}
            	if(null != data[9]) {
            		// 为0的时候不显示
            		if("0".equals(data[9].toString())) {
            			info.setLotNo("");
            		} else {
            			info.setLotNo(data[9].toString());
            		}
            	}
            	if(null != data[10]) {
            		info.setLastModifiedDateWhs(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setLastModifiedDateLocation(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setNowCount(dfNumber.format(data[12]));
            	}
            	if(null != data[13]) {
            		info.setLastModifiedDateLot(data[13].toString());
            	}
            	if(null!=data[14])
            	{
            		info.setStdCost(Double.parseDouble(data[14].toString()));
            	}
            	if(info.getStdCost()!=null&&info.getNowCount()!=null&&!info.getNowCount().equals(""))
            	{
            		info.setAllCost(info.getStdCost()*Double.parseDouble(data[12].toString()));
            	}
            	arrlist.add(info);
            	//-----------------
            	if(info.getNowCount()!=null)
            	{
            	totalNowCount+=Double.parseDouble(data[12].toString());
            	}
            	if(info.getStdCost()!=null)
            	{
            		totalStdCost+=info.getStdCost();
            	}
            	if(info.getAllCost()!=null)
            	{
            		totalAllCost+=info.getAllCost();
            	}
            	
            }
            MaterialQueryAndSelectInfo model = new MaterialQueryAndSelectInfo();
            model.setMaterialNo("当页合计");
            model.setNowCount(totalNowCount.toString());
//            model.setStdCost(totalStdCost);
            model.setAllCost(totalAllCost);
            arrlist.add(model);
            
            // modify by liuyi 091103 
            String queryTotalSql = "select count(*) from (" + querySql + ") \n";
//            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
//            new Object[]{fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, 
//        			enterpriseCode, enterpriseCode, 
//        			enterpriseCode, enterpriseCode}).toString());
            Long totalCount = Long.parseLong(bll.getSingal(queryTotalSql,
            		new Object[]{enterpriseCode, enterpriseCode, 
        			enterpriseCode, enterpriseCode,
        			fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, fuzzy
        			}).toString());
            //计算所有记录得总合计
            // modify by liuyi 091103
       //     if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1 && rowStartIdxAndCount[0]!=0) {
//            	String totalSql=
//            		"SELECT sum(D.OPEN_BALANCE + D.RECEIPT + D.ADJUST - D.ISSUE),sum(A.std_cost),\n" +
//            		"       sum((D.OPEN_BALANCE + D.RECEIPT + D.ADJUST - D.ISSUE)*A.std_cost)\n" + 
//            		"\n" + 
//            		"FROM\n" + 
//            		"INV_C_MATERIAL A,\n" + 
//            		"INV_C_WAREHOUSE B,\n" + 
//            		"INV_J_LOT D,\n" + 
//            		"INV_J_WAREHOUSE E\n" + 
//            		"WHERE\n" + 
//            		"D.MATERIAL_ID = A.MATERIAL_ID AND\n" + 
//            		"D.WHS_NO = B.WHS_NO AND\n" + 
//            		"E.MATERIAL_ID = D.MATERIAL_ID AND\n" + 
//            		"E.WHS_NO = D.WHS_NO \n";
//            	totalSql += " and " + sqlWhere.substring(5);
            	String totalSql = "select sum(tt.qty),sum(tt.std_cost),sum(tt.qty*tt.std_cost) \n"
            		+ "from(" + querySql + ") tt\n";
//            	List totalList = bll.queryByNativeSQL(totalSql,
//            			new Object[]{fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, fuzzy,
//            			enterpriseCode, enterpriseCode,
//            			enterpriseCode, enterpriseCode});
            	List totalList = bll.queryByNativeSQL(totalSql,
            			new Object[]{enterpriseCode, enterpriseCode,
            			enterpriseCode, enterpriseCode,
            			fuzzy, fuzzy, fuzzy, fuzzy, fuzzy, fuzzy
            			});
            	if(totalList!=null&&totalList.size()>0)
            	{
            		MaterialQueryAndSelectInfo totalModel = new MaterialQueryAndSelectInfo();
            	Object [] totalData= (Object [])totalList.get(0);
            	 if(totalData[0]!=null) 
            	 {
            		 totalModel.setNowCount(totalData[0].toString());
            	 }
            	 if(totalData[1]!=null) 
            	 {
//            		 totalModel.setStdCost(Double.parseDouble(totalData[1].toString()));
            	 }
            	 if(totalData[2]!=null) 
            	 {
            		 totalModel.setAllCost(Double.parseDouble(totalData[2].toString()));
            	 }
            	 totalModel.setMaterialNo("总合计");
            	 arrlist.add(totalModel);
            	}

          //  }
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("find all failed", Level.SEVERE, e);
            throw e;
        }
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findMaterialByMaterialNo(String materialNo, String enterpriseCode, final int... rowStartIdxAndCount) {
		
        	PageObject object = new PageObject();
        	// 查询sql
          String mySql=
        	  "SELECT  distinct\n" +
        	  "A.MATERIAL_ID,\n" + 
        	  "A.MATERIAL_NO,\n" + 
        	  "A.MATERIAL_NAME,\n" + 
        	  "A.SPEC_NO,\n" + 
        	  "A.STOCK_UM_ID,\n" + 
        	  "B.WHS_NO,\n" + 
        	  "B.WHS_NAME,\n" + 
        	  "C.LOCATION_NO,\n" + 
        	  "C.LOCATION_NAME,\n" + 
        	  "D.LOT_NO,\n" + 
        	  "to_char(E.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') LAST_MODIFIED_DATE1,\n" + 
        	  "to_char(F.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') LAST_MODIFIED_DATE2,\n" + 
        	  "D.OPEN_BALANCE + D.RECEIPT + D.ADJUST - D.ISSUE,\n" + 
        	  "to_char(D.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') LAST_MODIFIED_DATE3\n" + 
        	  "FROM\n" + 
        	  "INV_C_MATERIAL A,\n" + 
        	  "INV_C_WAREHOUSE B,\n" + 
        	  "INV_C_LOCATION C,\n" + 
        	  "INV_J_LOT D,\n" + 
        	  "INV_J_WAREHOUSE E,\n" + 
        	  "INV_J_LOCATION F\n" + 
        	  "WHERE\n" + 
        	  "D.MATERIAL_ID = A.MATERIAL_ID AND\n" + 
        	  "D.WHS_NO = B.WHS_NO AND\n" + 
        	  "D.LOCATION_NO = C.LOCATION_NO(+) AND\n" + 
        	  "E.MATERIAL_ID = D.MATERIAL_ID AND\n" + 
        	  "E.WHS_NO = D.WHS_NO AND\n" + 
        	  "F.MATERIAL_ID(+) = D.MATERIAL_ID AND\n" + 
        	  "F.WHS_NO(+) = D.WHS_NO AND\n" + 
        	  "F.LOCATION_NO(+) = D.LOCATION_NO AND\n" + 
        	  "\n" + 
        	  "D.IS_USE = 'Y' AND\n" + 
        	  "A.IS_USE = 'Y' AND\n" + 
        	  "B.IS_USE = 'Y' AND\n" + 
        	  "C.IS_USE(+) = 'Y' AND\n" + 
        	  "E.IS_USE = 'Y' AND\n" + 
        	  "F.IS_USE(+) = 'Y' AND\n" + 
        	  "B.IS_INSPECT = 'N' AND\n" + 
        	  "A.ENTERPRISE_CODE ='"+enterpriseCode+"' AND\n" + 
        	  "B.ENTERPRISE_CODE = '"+enterpriseCode+"' AND\n" + 
        	  "C.ENTERPRISE_CODE(+) = '"+enterpriseCode+"' AND\n" + 
        	  "D.ENTERPRISE_CODE = '"+enterpriseCode+"'  AND\n" + 
        	  "E.ENTERPRISE_CODE = '"+enterpriseCode+"' AND\n" + 
        	  "F.ENTERPRISE_CODE(+) ='"+enterpriseCode+"'\n" + 
        	  "and   A.MATERIAL_NO='"+materialNo+"'\n" ;
//        	  "\n" + 
//        	  "union\n" + 
//        	  "SELECT  0,'合计','','',0,'','','','','','','',sum(D.OPEN_BALANCE + D.RECEIPT + D.ADJUST - D.ISSUE),''\n" + 
//        	  "FROM\n" + 
//        	  "\n" + 
//        	  "INV_C_MATERIAL A,\n" + 
//        	  "INV_C_WAREHOUSE B,\n" + 
//        	  "INV_C_LOCATION C,\n" + 
//        	  "INV_J_LOT D,\n" + 
//        	  "INV_J_WAREHOUSE E,\n" + 
//        	  "INV_J_LOCATION F\n" + 
//        	  "WHERE\n" + 
//        	  "D.MATERIAL_ID = A.MATERIAL_ID AND\n" + 
//        	  "D.WHS_NO = B.WHS_NO AND\n" + 
//        	  "D.LOCATION_NO = C.LOCATION_NO(+) AND\n" + 
//        	  "E.MATERIAL_ID = D.MATERIAL_ID AND\n" + 
//        	  "E.WHS_NO = D.WHS_NO AND\n" + 
//        	  "F.MATERIAL_ID(+) = D.MATERIAL_ID AND\n" + 
//        	  "F.WHS_NO(+) = D.WHS_NO AND\n" + 
//        	  "F.LOCATION_NO(+) = D.LOCATION_NO AND\n" + 
//        	  "\n" + 
//        	  "D.IS_USE = 'Y' AND\n" + 
//        	  "A.IS_USE = 'Y' AND\n" + 
//        	  "B.IS_USE = 'Y' AND\n" + 
//        	  "C.IS_USE(+) = 'Y' AND\n" + 
//        	  "E.IS_USE = 'Y' AND\n" + 
//        	  "F.IS_USE(+) = 'Y' AND\n" + 
//        	  "B.IS_INSPECT = 'N' AND\n" + 
//        	  "A.ENTERPRISE_CODE ='"+enterpriseCode+"' AND\n" + 
//        	  "B.ENTERPRISE_CODE = '"+enterpriseCode+"' AND\n" + 
//        	  "C.ENTERPRISE_CODE(+) = '"+enterpriseCode+"' AND\n" + 
//        	  "D.ENTERPRISE_CODE = '"+enterpriseCode+"' AND\n" + 
//        	  "E.ENTERPRISE_CODE = '"+enterpriseCode+"' AND\n" + 
//        	  "F.ENTERPRISE_CODE(+) = '"+enterpriseCode+"'\n" + 
//        	  "and   A.MATERIAL_NO='"+materialNo+"'";

        String	sql= "select * from ( \n"+mySql+") order by MATERIAL_ID desc";
        String	sqlCount = "select count(*) from ( \n"+mySql+") ";
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        if(totalCount>0)
        {
        	List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
        	List<MaterialQueryAndSelectInfo> arrlist = new ArrayList<MaterialQueryAndSelectInfo>();
            Iterator it = list.iterator();
            // 格式化
            String patternNumber = "###,###,###,###,##0.0000";
    		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
            while (it.hasNext()) {
            	MaterialQueryAndSelectInfo info = new MaterialQueryAndSelectInfo();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setMaterialId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setMaterialNo(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setMaterialName(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setSpecNo(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setStockUmId(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setWhsNo(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setWhsName(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setLocationNo(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setLocationName(data[8].toString());
            	}
            	if(null != data[9]) {
            		// 为0的时候不显示
            		if("0".equals(data[9].toString())) {
            			info.setLotNo("");
            		} else {
            			info.setLotNo(data[9].toString());
            		}
            	}
            	if(null != data[10]) {
            		info.setLastModifiedDateWhs(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setLastModifiedDateLocation(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setNowCount(dfNumber.format(data[12]));
            	}
            	if(null != data[13]) {
            		info.setLastModifiedDateLot(data[13].toString());
            	}
            	arrlist.add(info);
            }
           
            object.setList(arrlist);
            object.setTotalCount(totalCount);
            return object;
        }
        return null;
     
	}
}
