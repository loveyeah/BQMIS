package power.ejb.resource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.form.InvCmaterialInfo;
import power.ejb.resource.form.MaterialForPartInfo;
import power.ejb.resource.form.MaterialInfo;

/**
 * Facade for entity InvCMaterial.
 * 
 * @see power.ejb.logistics.InvCMaterial
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCMaterialFacade implements InvCMaterialFacadeRemote {
	

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	
	public InvCMaterial save(InvCMaterial entity) {
		LogUtil.log("saving InvCMaterial instance", Level.INFO, null);
		try {
			// 流水号的取得
			Long materialId = bll.getMaxId("INV_C_MATERIAL", "MATERIAL_ID");
			entity.setMaterialId(materialId);
			//add by fyyang 090521
			if(entity.getMaterialNo()==null||entity.getMaterialNo().equals(""))
			{
				entity.setMaterialNo(this.createMaterialNo(entity.getDefaultWhsNo(), entity.getMaertialClassId()));
			}
			entityManager.persist(entity);
			
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(InvCMaterial entity) {
		LogUtil.log("deleting InvCMaterial instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvCMaterial.class, entity
					.getMaterialId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public InvCMaterial update(InvCMaterial entity) {
		LogUtil.log("updating InvCMaterial instance", Level.INFO, null);
		try {
			InvCMaterial result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCMaterial findById(Long id) {
		LogUtil.log("finding InvCMaterial instance with id: " + id, Level.INFO,
				null);
		try {
			InvCMaterial instance = entityManager.find(InvCMaterial.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

	/**
	 * 根据编码查询物料信息
	 * 
	 * @param materialNo 编码
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByMaterialNo(String materialNo, String enterpriseCode) {
		LogUtil.log("finding InvCMaterial instance with materialNo: " + materialNo,
                Level.INFO, null);
        try {
        	PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from inv_c_material t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.material_no='" + materialNo +  "'\n" +
                "and t.is_use='Y'";
            // 执行查询
            List<InvCMaterial> list = bll.queryByNativeSQL(sql, InvCMaterial.class);
            // 查询sql
            String sqlCount =
            	"select count(*) from inv_c_material t\n" +
	            "where  t.enterprise_code='"+enterpriseCode + "'\n" +
	            "and t.material_no='" + materialNo +  "'\n" +
	            "and t.is_use='Y'";
            // 执行查询
            Long totalCount = Long
				.parseLong(bll.getSingal(sqlCount).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}

	

	/**
	 * 根据物料分类ID获得物料列表
	 * 
	 * @param maertialClassId 物料分类ID
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料列表
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByMaertialClassId(String maertialClassId, String enterpriseCode) {
		LogUtil.log("finding InvCMaterial instance with maertialClassId: " + maertialClassId,
                Level.INFO, null);
        try {
        	PageObject result = new PageObject();
        	List<InvCMaterial> list = null;
            // 查询sql
            String sql=
                "select * from inv_c_material t\n" +
                "where  t.enterprise_code='" + enterpriseCode + "'\n" +
                "and t.maertial_class_id='" + maertialClassId +  "'\n" +
                "and t.is_use='Y'";
            // 执行查询
            list = bll.queryByNativeSQL(sql, InvCMaterial.class);
            result.setList(list);
            // 返回
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}

	

	

	

	

	

	


	/**
	 * Find all InvCMaterial entities.
	 * 
	 * @return List<InvCMaterial> all InvCMaterial entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterial> findAll() {
		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvCMaterial model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
     * 物料主文件(部分)模糊查询
     * @param fuzzy 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject all InvCMaterial entities
     */
	@SuppressWarnings("unchecked")
	public PageObject findAllMaterial(String fuzzy,String enterpriseCode, final int... rowStartIdxAndCount) {
		 LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		 try {
			 PageObject result = null;
	            // 查询sql
	            String sql=
	                "select distinct w.MATERIAL_ID AS materialId, \n"  +
	                " w.MATERIAL_NAME AS materialName, \n" +
	               // " w.MAERTIAL_CLASS_ID AS maertialClassId, \n" +
	                " c.CLASS_NAME AS materialClassName, \n" +
	                " w.SPEC_NO AS specNo, \n" +
	                " w.PARAMETER AS parameter,  \n" +
	                " w.MATERIAL_NO AS materialNo \n" +
	                " from INV_C_MATERIAL w ,INV_C_ALTERNATE_MATERIAL t,INV_C_MATERIAL_CLASS c \n" +
	                " where  t.IS_USE = 'Y' \n" +
	                //for add
	                " and w.MAERTIAL_CLASS_ID = c.MAERTIAL_CLASS_ID(+)\n" +
	                " and w.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
	                " and t.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
	                " and c.IS_USE(+) = 'Y' \n"+
	                " and c.ENTERPRISE_CODE(+) = '"+ enterpriseCode +"' \n"+
	                //for add 
	                " and w.IS_USE ='Y ' \n"+
	                " and (w.MATERIAL_NO like '%" + fuzzy + "%' or w.MATERIAL_NAME like '%" +fuzzy + "%')"+ "\n" +
	                " and t.MATERIAL_ID = w.MATERIAL_ID \n" +
	                " order by w.MATERIAL_NO";
	            List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	            // 数量
	            String sqlCount=
	                "select count(distinct w.MATERIAL_ID) from INV_C_MATERIAL w ,INV_C_ALTERNATE_MATERIAL t,INV_C_MATERIAL_CLASS c \n" +
	                " where  t.IS_USE = 'Y' \n" +
	                //for add
	                " and w.MAERTIAL_CLASS_ID = c.MAERTIAL_CLASS_ID(+) \n" +
	                " and w.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
	                " and t.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
	                " and c.IS_USE(+) = 'Y' \n"+
	                " and c.ENTERPRISE_CODE(+) = '"+ enterpriseCode +"' \n"+
	                //for add 
	                " and w.IS_USE ='Y ' \n"+
	                " and (w.MATERIAL_NO like '%" + fuzzy + "%' or w.MATERIAL_NAME like '%" +fuzzy + "%')"+ "\n" +
	                " and t.MATERIAL_ID = w.MATERIAL_ID \n" +
	                " order by w.MATERIAL_ID";
             // 给InvCMaterial赋值
	            List arrlist = new ArrayList();
				if(list !=null && list.size()>0)
				{
					result = new PageObject();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						MaterialForPartInfo model = new MaterialForPartInfo();
						Object[] data = (Object[]) it.next();
						if (data[0] != null)
							model.setMaterialId(Long.parseLong(data[0].toString()));
						if (data[1] != null)
							model.setMaterialName(data[1].toString());
						if (data[2] != null)
							model.setMaterialClassName(data[2].toString());
						if (data[3] != null)
							model.setSpecNo(data[3].toString());
						if (data[4] != null)
							model.setParameter(data[4].toString());
						if (data[5] != null)
							model.setMaterialNo(data[5].toString());
						arrlist.add(model);
					}
					Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
					result.setList(arrlist);
					result.setTotalCount(totalCount);
				} 
	            return result;
	        } catch (RuntimeException re) {
	            LogUtil.log("find all failed", Level.SEVERE, re);
	            throw re;
	        }
	}
	
	/**
     * 查找替代物料信息
     * @param materialId 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject all InvCMaterial entities
     */
	@SuppressWarnings("unchecked")
	public PageObject findAlertMaterial(String materialId, String enterpriseCode,final int... rowStartIdxAndCount) {
		 LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		 try {
	            PageObject result = null;
	            // 查询sql
	            String sql=
	                "select T.ALTERNATE_MATERIAL_ID, \n"  +
	                " T.ALTER_MATERIAL_ID, \n " +
	                " T.QTY, \n" +
	                " T.PRIORITY, \n" +
	                " to_char(T.EFFECTIVE_DATE, 'yyyy-MM-dd'), \n" +
	                " to_char(T.DISCONTINUE_DATE, 'yyyy-MM-dd'),  \n" +
	                " W.MATERIAL_NO , \n" +
	                " W.MATERIAL_NAME \n" +
	                " from INV_C_MATERIAL W ,INV_C_ALTERNATE_MATERIAL T \n" +
	                " where  T.IS_USE = 'Y' \n" +
	                " and W.IS_USE ='Y' \n"+
	                " and T.MATERIAL_ID like  '" +materialId+ "' \n" +
	                " and T.ALTER_MATERIAL_ID = W.MATERIAL_ID \n" +
	                " and W.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
	                " and T.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
	                " order by W.MATERIAL_NO";
	            List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	            // 数量
	            String sqlCount=
	            	   "select count(*) from (  \n" +
	            	   "select distinct T.ALTERNATE_MATERIAL_ID, \n"  +
		                " T.ALTER_MATERIAL_ID, \n " +
		                " T.QTY, \n" +
		                " T.PRIORITY, \n" +
		                " to_char(T.EFFECTIVE_DATE, 'yyyy-MM-dd hh24:mi:ss'), \n" +
		                " to_char(T.DISCONTINUE_DATE, 'yyyy-MM-dd hh24:mi:ss'),  \n" +
		                " W.MATERIAL_NO , \n" +
		                " W.MATERIAL_NAME \n" +
		                " from INV_C_MATERIAL W ,INV_C_ALTERNATE_MATERIAL T \n" +
		                " where  T.IS_USE = 'Y' \n" +
		                " and W.IS_USE ='Y' \n"+
		                " and T.MATERIAL_ID like  '" +materialId+ "' \n" +
		                " and T.ALTER_MATERIAL_ID = W.MATERIAL_ID \n" +
		                " and W.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
		                " and T.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
		                " )";
	            // 给InvCMaterial赋值
	            List arrlist = new ArrayList();
				if(list !=null && list.size()>0)
				{
					result = new PageObject();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						InvCmaterialInfo model = new InvCmaterialInfo();
						Object[] data = (Object[]) it.next();
						if (data[0] != null)
							model.setAlternateMaterialId(Long.parseLong(data[0].toString()));
						if (data[1] != null)
							model.setAlterMaterialId(Long.parseLong(data[1].toString()));
						if (data[2] != null)
							model.setQty(Double.parseDouble(data[2].toString()));
						if (data[3] != null)
							model.setPriority(Long.parseLong(data[3].toString()));
						if (data[4] != null)
							model.setEffectiveDate(data[4].toString());
						if (data[5] != null)
							model.setDiscontinueDate(data[5].toString());
						if (data[6] != null)
							model.setMaterialNo(data[6].toString());
						if (data[7] != null)
							model.setMaterialName(data[7].toString());
						arrlist.add(model);
					}
					Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
					result.setList(arrlist);
					result.setTotalCount(totalCount);
				} 
	            return result;
	        } catch (RuntimeException re) {
	            LogUtil.log("find all failed", Level.SEVERE, re);
	            throw re;
	        }
	}
	
	/**
	 * Find all InvCMaterial entities.
	 * 
	 * @return List<WareHouseListBean> all WareHouseListBean entities
	 */
	public List<WareHouseListBean> findAllForWareHouse(String enterpriseCode){
		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		List wareHouseList=new ArrayList();
		// 数字输出格式化
		String pattern = "###,###,###,###,##0.00";
		DecimalFormat df = new DecimalFormat(pattern);
		String nullNumber = "0.00";
		try{
			String sql = "select a.MATERIAL_NO,a.MATERIAL_NAME,a.SPEC_NO,a.STOCK_UM_ID,b.NOW_STOCK,a.MIN_STOCK-b.NOW_STOCK ADVICEBUY \n"+
            			 "from INV_C_MATERIAL a, \n"+
            			 	"(select c.MATERIAL_ID NOW_MATERIAL_ID,sum(c.OPEN_BALANCE+c.RECEIPT+c.ADJUST-c.ISSUE) NOW_STOCK \n"+
            			 	 "FROM INV_J_WAREHOUSE c,INV_C_WAREHOUSE d \n"+
            			 	 "where c.IS_USE='Y' \n"+
            			 	 "and c.ENTERPRISE_CODE = '"+ enterpriseCode +"' \n"+
            			 	 "and c.WHS_NO = d.WHS_NO \n"+
            			 	 "and d.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
            			 	 "and d.IS_USE='Y' \n"+
            			 	 "and d.IS_INSPECT='N' \n"+
            			 	 "group by c.MATERIAL_ID) b \n"+
            			 "where a.MATERIAL_ID=b.NOW_MATERIAL_ID \n"+
            			 "and a.MIN_STOCK>b.NOW_STOCK \n"+
            			 "and a.IS_USE='Y' \n"+
            			 "and a.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
            			 "and a.MIN_STOCK is not null \n"+
            			 "order by a.MATERIAL_NO asc"; 
			List list=bll.queryByNativeSQL(sql);
			Iterator it=list.iterator();
			while(it.hasNext()){
				Object[] data=(Object[])it.next();
				WareHouseListBean model=new WareHouseListBean();
				if(data[0]!=null){
					model.setMaterialNo(data[0].toString());
				}
				if(data[1]!=null){
					model.setMaterialName(data[1].toString());
				}
				if(data[2]!=null){
					model.setSpecNo(data[2].toString());
				}
				if(data[3]!=null){
					model.setStockUmID(data[3].toString());
				}
				if(data[4]!=null){
					model.setNowStock(df.format(data[4]));
				}else{
					model.setNowStock(nullNumber);
				}
				if(data[5]!=null){
					model.setAdviceBuyNumber(df.format(data[5]));
				}else{
					model.setAdviceBuyNumber(nullNumber);
				}
				wareHouseList.add(model);
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}return wareHouseList;
	}
	
	/**
	 * Find all InvCMaterial entities.
	 * @param String enterpriseCode
	 * @return List<PanDianListBean> all PanDianListBean entities
	 */
	public List<CheckBalanceListBean> findAllForPanDian(String enterpriseCode,String bookNo){
		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		List panDianList=new ArrayList();
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String patternMoney = "###,###,###,###,##0.0000";
		DecimalFormat dfMoney = new DecimalFormat(patternMoney);
		String nullNumber = "0.00";
		String nullMoney = "0.0000";
		try{
			String sql = "select distinct a.MATERIAL_NO,a.MATERIAL_NAME,a.SPEC_NO,a.STOCK_UM_ID, \n"+
                                  		 "b.WHS_NAME,c.LOCATION_NAME,d.LOT_NO,d.BOOK_QTY,d.PHYSICAL_QTY, \n"+
                                  		 "d.PHYSICAL_QTY - d.BOOK_QTY earnQTY,d.BOOK_QTY*a.STD_COST bookMoney, \n"+
                                  		 "(d.PHYSICAL_QTY - d.BOOK_QTY)*a.STD_COST earnMoney,d.REASON \n"+
						 "from  \n"+
						 "INV_C_MATERIAL a, \n"+
						 "INV_C_WAREHOUSE b, \n"+
						 "INV_J_BOOK_DETAILS d left join INV_C_LOCATION c  on  \n"+
						 "d.LOCATION_NO = c.LOCATION_NO and \n"+
						 "d.WHS_NO = c.WHS_NO and \n"+
						 "c.IS_USE  = 'Y'  and \n"+
						 "c.ENTERPRISE_CODE = '"+ enterpriseCode +"'  \n"+
						 "where \n"+
						 "d.MATERIAL_ID = a.MATERIAL_ID and \n"+
						 "d.WHS_NO = b.WHS_NO and \n"+
						 "a.IS_USE = 'Y' and \n"+
						 "b.IS_USE = 'Y' and \n"+
						 "d.IS_USE = 'Y' and \n"+
						 "a.ENTERPRISE_CODE = '"+ enterpriseCode +"' and \n"+
						 "b.ENTERPRISE_CODE = '"+ enterpriseCode +"' and \n"+
						 "d.ENTERPRISE_CODE = '"+ enterpriseCode +"' and \n"+
						 "d.BOOK_NO = '"+ bookNo +"' \n"+
						 "order by a.MATERIAL_NO ASC";
			List list=bll.queryByNativeSQL(sql);
			Iterator it=list.iterator();
			while(it.hasNext()){
				Object[] data=(Object[])it.next();
				CheckBalanceListBean model=new CheckBalanceListBean();
				if(data[0]!=null){
					model.setMaterialNo(data[0].toString());
				}
				if(data[1]!=null){
					model.setMaterialName(data[1].toString());
				}
				if(data[2]!=null){
					model.setSpecNo(data[2].toString());
				}
				if(data[3]!=null){
					model.setStockUmID(data[3].toString());
				}
				if(data[4]!=null){
					model.setWhsName(data[4].toString());
				}
				if(data[5]!=null){
					model.setLocationName(data[5].toString());
				}
				if(data[6]!=null){
					model.setLotNo(data[6].toString());
				}
				if(data[7]!=null){
					model.setBookQuantity(dfNumber.format(data[7]));
				}else{
					model.setBookQuantity(nullNumber);
				}
				if(data[8]!=null){
					model.setPhysicalQuantity(dfNumber.format(data[8]));
				}else{
					model.setPhysicalQuantity(nullNumber);
				}
				if(data[9]!=null){
					model.setEarnQuantity(dfNumber.format(data[9]));
				}else{
					model.setEarnQuantity(nullNumber);
				}
				if(data[10]!=null){
					model.setBookMoney(dfMoney.format(data[10]));
				}else{
					model.setBookMoney(nullMoney);
				}
				if(data[11]!=null){
					model.setEarnMoney(dfMoney.format(data[11]));
				}else{
					model.setEarnMoney(nullMoney);
				}
				if(data[12]!=null){
					model.setReason(data[12].toString());
				}
				panDianList.add(model);
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}return panDianList;
	}
	/**
	 * 查找该当物料分类节点下所有的物料编码
	 * 
	 * @param classNo 物料分类编码
	 * @param enterpriseCode 企业编码
	 * @return 返回该当物料分类节点下所有的物料编码
     * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public List findAllChildrenNode(String classNo, String enterpriseCode){
    	LogUtil.log("finding child node with classNo: " + classNo,
                Level.INFO, null);
    	try{
    		// 查询sql
    		String sql = 
    			"SELECT * \n" + 
    			"FROM INV_C_MATERIAL \n" +
    			"WHERE is_use = 'Y' AND enterprise_code = '" + enterpriseCode +"'\n" +
    			"AND maertial_class_id IN \n" +
    			"(SELECT maertial_class_id \n" +
    			"FROM inv_c_material_class START WITH parent_class_no = '" + classNo + "' and is_use = 'Y' \n" +
    			"CONNECT BY PRIOR class_no = parent_class_no AND IS_USE = 'Y'\n" +
    			"UNION \n" + 
    			" SELECT maertial_class_id \n" +
    			" FROM inv_c_material_class \n" +
    			" WHERE class_no = '" + classNo +"' and is_use = 'Y')";
    		// 执行查询
            List<InvCMaterial> list = bll.queryByNativeSQL(sql, InvCMaterial.class);
            // 返回
            return list;
    	}catch(Exception e)
    	{
    		LogUtil.log("find failed", Level.SEVERE, e);
    		return null;
    	}    	
    }
	/**
     * 物料基础资料维护 模糊查询
     * @param fuzzy 查询字符串
     * @param rowStartIdxAndCount 分页
     * @return PageObject InvCMaterial entities
     */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialList(String fuzzy, String enterpriseCode, String materialClassCode, final int... rowStartIdxAndCount) {
		 LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		 try {
	            PageObject result = new PageObject();
	            // 查询sql
	            String sql=
	                "select distinct w.MATERIAL_ID, \n"  +
	                " w.MATERIAL_NO, \n" +
	                " w.MATERIAL_NAME, \n" +
	                " t.CLASS_NAME, \n" +
	                " w.SPEC_NO, \n" +
	                " w.PARAMETER, \n" +
	                // 存货计量单位(取得的是编码，然后调用共通方法取得名称)
	                " w.STOCK_UM_ID, \n" +
	                " h.WHS_NAME, \n" +
	                " l.LOCATION_NAME,\n" +
	                " t.maertial_class_id\n" +  // add by drdu 090624
	                " from((INV_C_MATERIAL w left join  INV_C_WAREHOUSE h " +
	                " on w.default_whs_no = h.whs_no and w.IS_USE = 'Y' and h.IS_USE = 'Y' " +
	                " and w.enterprise_code = '" + enterpriseCode + "'\n" +
	                " and h.enterprise_code = '" + enterpriseCode + "')\n" +
		            " left join INV_C_MATERIAL_CLASS t on w.maertial_class_id = t.maertial_class_id and t.IS_USE = 'Y' " +     
		            " and t.enterprise_code = '" + enterpriseCode + "')\n" +
		            " left join INV_C_LOCATION l on  w.default_whs_no=l.whs_no and w.default_location_no = l.location_no" +
		            " and l.is_use='Y'" +
		            " and l.enterprise_code = '" + enterpriseCode + "'\n" +
	                " where w.IS_USE = 'Y' and (w.MATERIAL_NO like '%" + fuzzy +
	                        "%' or w.MATERIAL_NAME like '%" +fuzzy +
	                        "%' or t.CLASS_NAME like '%" +fuzzy +
	                        "%' or w.SPEC_NO like '%" +fuzzy +
	                        "%')"+ "\n" ;
	            // add by ywliu 2009/6/29  modify 点击物料时查询所有物资
	            if(materialClassCode!=null&&!materialClassCode.equals("") && !"-1".equals(materialClassCode))
	        	{
	        		sql +=
	        			"   and  (select tt.class_no from inv_c_material_class tt\n" +
	        			"where tt.maertial_class_id=w.MAERTIAL_CLASS_ID and rownum=1) like '"+materialClassCode.trim()+"%'  \n";

	        	}      
                sql+="ORDER BY w.MATERIAL_NO \n";
	            // 信息数量查询
	            String sqlCount=
	                "select count(distinct w.MATERIAL_ID)" +
	                " from((INV_C_MATERIAL w left join  INV_C_WAREHOUSE h " +
	                " on w.default_whs_no = h.whs_no and w.IS_USE = 'Y' and h.IS_USE = 'Y' " +
	                " and w.enterprise_code = '" + enterpriseCode + "'\n" +
	                " and h.enterprise_code = '" + enterpriseCode + "')\n" +
		            " left join INV_C_MATERIAL_CLASS t on w.maertial_class_id = t.maertial_class_id and t.IS_USE = 'Y' " +     
		            " and t.enterprise_code = '" + enterpriseCode + "')\n" +
		            " left join INV_C_LOCATION l on  w.default_whs_no=l.whs_no and w.default_location_no = l.location_no" +
		            " and l.is_use='Y'" +
		            " and l.enterprise_code = '" + enterpriseCode + "'\n" +
	                " where w.IS_USE = 'Y' and (w.MATERIAL_NO like '%" + fuzzy +
	                        "%' or w.MATERIAL_NAME like '%" +fuzzy +
	                        "%' or t.CLASS_NAME like '%" +fuzzy +
	                        "%' or w.SPEC_NO like '%" +fuzzy +
	                        "%')"+ "\n";
	            // add by ywliu 2009/6/29  modify 点击物料时查询所有物资
	            if(materialClassCode!=null&&!materialClassCode.equals("")&& !"-1".equals(materialClassCode))
	        	{
	            	sqlCount +=
	        			"   and  (select tt.class_no from inv_c_material_class tt\n" +
	        			"where tt.maertial_class_id=w.MAERTIAL_CLASS_ID and rownum=1) like '"+materialClassCode.trim()+"%'  \n";

	        	}   
	            
	            List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				// 物料基础资料列表
				List<MaterialInfo> arrlist = new ArrayList<MaterialInfo>();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					MaterialInfo materialInfo = new MaterialInfo();				
					Object[] data = (Object[]) it.next();
					String materialId = data[0].toString();
					String materialNo = data[1].toString();
					String materialName = data[2].toString();
					String className = "";
					if(data[3] != null){
					    className = data[3].toString();
					}
					String specNo = "";
					if(data[4] != null) {
						specNo = data[4].toString();
					}
					String parameter = "";
					if(data[5] != null) {
						parameter = data[5].toString();
					}
					// 存货计量单位(取得的是编码，然后调用共通方法取得名称)										
					String stockUmName = "";
					if(data[6] != null) {
						String stockUmNo = data[6].toString();
						Ejb3Factory factory = Ejb3Factory.getInstance();
						BpCMeasureUnitFacadeRemote bpRemote = 
							(BpCMeasureUnitFacadeRemote) factory.getFacadeRemote("BpCMeasureUnitFacade");
						BpCMeasureUnit bpBeen = bpRemote.findById(Long.parseLong(stockUmNo));
						stockUmName = bpBeen.getUnitName();									
					}
					String whsName = "";
					if(data[7] != null) {
						whsName = data[7].toString();
					}
					String locationName = "";
					if(data[8] != null) {
						locationName = data[8].toString();
					}
					if(data[9] != null)  // add by drdu 090624
						materialInfo.setMaterialClassId(data[9].toString());
					materialInfo.setMaterialId(materialId);
					materialInfo.setMaterialNo(materialNo);
					materialInfo.setMaterialName(materialName);
					materialInfo.setClassName(className);
					materialInfo.setParameter(parameter);
					materialInfo.setSpecNo(specNo);
					materialInfo.setStockUmName(stockUmName);
					materialInfo.setWhsName(whsName);
					materialInfo.setLocationName(locationName);
					arrlist.add(materialInfo);
				}
				if(arrlist.size()>0)
				{
					result.setList(arrlist);
					result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
				}
	            return result;
	        } catch (RuntimeException re) {
	            LogUtil.log("find all failed", Level.SEVERE, re);
	            throw re;
	        }
	}
	/**
	 * 下拉框初始化[物料分类]
	 */
	public PageObject getClassNameList(String enterpriseCode){
	/*	检索项目：编码，名称						
		检索数据库：物料分类						
		检索条件：是否使用=‘Y’						
				企业编码 = session.企业编码			
				*/
		PageObject result = new PageObject();
		String sql=
			"select *\n" +
			"  from INV_C_MATERIAL_CLASS t\n" + 
			"where t.is_use = 'Y'\n" +
			"  and t.enterprise_code = '" + enterpriseCode + "'\n";
		List<InvCMaterialClass> list = bll.queryByNativeSQL(sql,InvCMaterialClass.class);
		InvCMaterialClass nullBeen = new InvCMaterialClass();
		// 在第一行增加空行
		//nullBeen.setMaertialClassId(null);
		nullBeen.setClassName("");
		list.add(0,nullBeen);
		result.setList(list);
		return result;
	}
	/**
	 * 下拉框初始化[物料类型]==
	 */
	public PageObject getMaterialTypeList(String enterpriseCode){
        PageObject result = new PageObject();
		String sql=
			"select *\n" +
			"  from INV_C_MATERIAL_TYPE t\n" + 
			"where t.is_use = 'Y'\n" +
			"  and t.enterprise_code = '" + enterpriseCode + "'\n";
		List<InvCMaterialType> list = bll.queryByNativeSQL(sql,InvCMaterialType.class);
		InvCMaterialType nullBeen = new InvCMaterialType();
		// 在第一行增加空行		
		nullBeen.setTypeName("");
		list.add(0,nullBeen);
		result.setList(list);
		return result;
	}
	/**
	 * 下拉框初始化[物料状态]
	 */
	public PageObject getMaterialStatusList(String enterpriseCode){	
		PageObject result = new PageObject();
		String sql=
			"select *\n" +
			"  from INV_C_MATERIAL_STATUS t\n" + 
			"where t.is_use = 'Y'\n" +
			"  and t.enterprise_code = '" + enterpriseCode + "'\n";
		List<InvCMaterialStatus> list = bll.queryByNativeSQL(sql,InvCMaterialStatus.class);
		InvCMaterialStatus nullBeen = new InvCMaterialStatus();
		// 在第一行增加空行
		nullBeen.setMaterialStatusId(null);
		nullBeen.setStatusName("");
		list.add(0,nullBeen);
		result.setList(list);
		return result;
	}
	/**
	 * 下拉框初始化[缺省仓库]
	 */
	public PageObject getWarehouseList(String enterpriseCode){
		PageObject result = new PageObject();
		String sql=
			"select *\n" +
			"  from INV_C_WAREHOUSE t\n" + 
			"where t.is_use = 'Y'\n" +
			"  and t.enterprise_code = '" + enterpriseCode + "'\n";
		List<InvCWarehouse> list = bll.queryByNativeSQL(sql,InvCWarehouse.class);
		InvCWarehouse nullBeen = new InvCWarehouse();
		// 在第一行增加空行
		nullBeen.setWhsNo("");
		nullBeen.setWhsName("");
		list.add(0,nullBeen);
		result.setList(list);
		return result;
	}
	/**
	 * 下拉框初始化[缺省库位]
	 */
	public PageObject getLocationList(String enterpriseCode, String whsNo){
		PageObject result = new PageObject();
		String sql=
			"select *\n" +
			"  from INV_C_LOCATION t\n" + 
			"where t.is_use = 'Y'\n" +
			"  and t.enterprise_code = '" + enterpriseCode + "'\n" ;
		if(whsNo!=null)
		{
			//modify by fyyang 090617
		 sql+=   "   and t.whs_no = '" + whsNo + "'\n";
		}
		List<InvCLocation> list = bll.queryByNativeSQL(sql,InvCLocation.class);
		InvCLocation nullBeen = new InvCLocation();
		// 在第一行增加空行
		nullBeen.setLocationNo("");
		nullBeen.setLocationName("");
		list.add(0,nullBeen);
		result.setList(list);
		return result;
	}
	/**
	 * 根据所选择记录的流水号从[物料主文件]中检索相关信息
	 */
	public PageObject getMaterialByIdAndEnterpriseCode(Long materialId, String enterpriseCode) {
		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		try {
            PageObject result = new PageObject();
	        // 查询sql
	        String sql= "select * \n"  +
	                    "from INV_C_MATERIAL t \n" +
	                    "where  t.MATERIAL_ID = '" + materialId + "' \n" +
	                    " and t.enterprise_code = '" + enterpriseCode + "'\n";
	        List<InvCMaterial> list = bll.queryByNativeSQL(sql,InvCMaterial.class);
	        result.setList(list);
	        return result;
	    } catch (RuntimeException re) {
	        LogUtil.log("find all failed", Level.SEVERE, re);
	        throw re;
	    }
	}
	
	/**
	 * 搜索所有盘点单号
	 */
	public List<String> fillAllCheckBalanceNo(){
		LogUtil.log("finding all checkBalanceNo instances", Level.INFO, null);
		List<String> model = new ArrayList<String>();
		try{
			String sql = "select distinct BOOK_NO \n"+
						 "from INV_J_BOOK_DETAILS";
			List list = bll.queryByNativeSQL(sql);
			if(list!=null&&list.size()>0){
				for(int i = 0;i<list.size();i++){
					model.add(list.get(i).toString());
				}
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}return model;
	}
	
	//add by fyyang 090519  
	//modify by drdu 090626
	public String createMaterialNo(String whsNo,Long materialClassId)
	{
		InvCMaterialClassFacadeRemote materialClassRemote=(InvCMaterialClassFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("InvCMaterialClassFacade");
		
		InvCMaterialClass metarialClassNo = materialClassRemote.findById(materialClassId);
		String metarialNo=whsNo;
		metarialNo+="-"+ metarialClassNo.getClassNo();
		
		String [] data=metarialNo.toString().split("-");
		if(data.length>0)
		{
			if(data.length==2) metarialNo=data[1]+"-00-00-00";
			if(data.length==3) metarialNo=data[1]+"-"+data[2]+"-00-00";
			// modify by liuyi 091102
			if(data.length==4) metarialNo=data[1]+"-"+data[2]+"-"+data[3]+"-00";
			if(data.length>=5) metarialNo=data[1]+"-"+data[2]+"-"+data[3]+"-"+data[4];
		}
		String sql=
			"select Trim(\n" +
			"case\n" + 
			"when max(t.material_no) is null then '00001'\n" + 
			"else\n" + 
			"  to_char(to_number( substr( max(trim(t.material_no)),length(max(trim(t.material_no)))-4,5)+1),'00000')\n" + 
			"end)\n" + 
			" from inv_c_material t\n" + 
			" where t.material_no like '"+metarialNo+"%'";
      
		metarialNo+="-"+bll.getSingal(sql).toString().trim();
		
		return metarialNo;
	}
	
	/** 判断是否能删除 true 能删除 false 不能删除*/
	public boolean checkDelete(Long materialId,String enterpriseCode)
	{
		String sql=
			"select\n" +
			"(select count(*) from MRP_J_PLAN_REQUIREMENT_DETAIL a\n" + 
			"where a.material_id="+materialId+"  and a.is_use='Y' and a.enterprise_code='"+enterpriseCode+"')\n" + 
			"+\n" + 
			"(select count(*) from PUR_J_ORDER_DETAILS b\n" + 
			"where b.material_id="+materialId+"  and b.is_use='Y' and b.enterprise_code='"+enterpriseCode+"')\n" + 
			"+\n" + 
			"(select count(*) from INV_J_ISSUE_DETAILS c\n" + 
			"where c.material_id="+materialId+"  and c.is_use='Y' and c.enterprise_code='"+enterpriseCode+"')\n" + 
			"from dual";
		int count=0;
		count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count>0)
		{
			return false;
		}
		else
		{
		return true;
		}
	}
	
	/**
	 * add by fyyang 091130
	 */
	public Double updateStdCost(Long materialId,String enterpriseCode,Double qty,Double price)
	{
		Double warehouseQty = this.findWarehouseQty(materialId, enterpriseCode);
		Double stdCostNow=0d;
		InvCMaterial materialModel=this.findById(materialId);
		if(materialModel!=null)
		{
			stdCostNow=materialModel.getStdCost();
		}
		Double stdCostTemp = 0d;
		if(warehouseQty +qty!=0)
		{
			stdCostTemp=(stdCostNow * warehouseQty + price * qty)
			/ (warehouseQty + qty);
		}
//		else if(warehouseQty + qty==0)
//		{
//			//stdCostTemp=(stdCostNow * warehouseQty + price * qty);
//			stdCostTemp=0d;	
//		}
		else
		{
			stdCostTemp=0d;	
		}
		String sql = "update inv_c_material h\n" +
		"         set h.std_cost = "+stdCostTemp+"\n"+
		"       where h.material_id = '"+materialId+"'\n" +
		"         and h.is_use = 'Y'\n" + 
		"         and h.enterprise_code = '"+enterpriseCode+"'";
         bll.exeNativeSQL(sql);
         return  stdCostTemp;
	}
	/**
	 * 库存物料的数量
	 * add by fyyang 091130
	 */
	private Double findWarehouseQty(Long materialId, String enterpriseCode) {
		String sql = "SELECT SUM(A.OPEN_BALANCE+A.RECEIPT+A.ADJUST-A.ISSUE) AS TEMP\n"
				+ "FROM INV_J_WAREHOUSE A\n"
				+ "WHERE A.MATERIAL_ID="
				+ materialId
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'";
		Object obj = bll.getSingal(sql);
		Double value =0d;
		if (obj != null) {
			value =Double.parseDouble(obj.toString());
		}
		return value;
	}
	
	
}