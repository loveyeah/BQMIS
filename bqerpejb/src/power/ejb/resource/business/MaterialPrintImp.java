package power.ejb.resource.business;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.InvJBook;
import power.ejb.resource.InvJBookDetails;
import power.ejb.resource.InvJBookDetailsFacadeRemote;
import power.ejb.resource.InvJBookFacadeRemote;
import power.ejb.resource.MaterialCheckPrintReportBean;
import power.ejb.resource.MaterialCheckPrintReportListBean;
import power.ejb.resource.form.LotMaterialInfo;

@Stateless
public class MaterialPrintImp implements MaterialPrint{
	// 对应 commInterface jar包删除
//	/** 远程 */
//	private CommInterfaceFacadeRemote remoteComm;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 物料盘点主表remote*/
	private InvJBookFacadeRemote invJBookRemote;
	/** 物料盘点明细表remote*/
	private InvJBookDetailsFacadeRemote invJDetailBookRemote;


	/**
	 * 构造函数
	 */
	public MaterialPrintImp() {
		invJBookRemote = (InvJBookFacadeRemote)(Ejb3Factory.getInstance())
		.getFacadeRemote("InvJBookFacade");
		invJDetailBookRemote = (InvJBookDetailsFacadeRemote)(Ejb3Factory.getInstance())
		.getFacadeRemote("InvJBookDetailsFacade");
	}
	/**
	 *
	 * @param entity
	 * 				物料盘点主表entity
	 * @param list
	 * 				物料盘点明细表list
	 *  * @throws CodeRepeatException
	 */
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveMaterial(InvJBook entity,List<LotMaterialInfo> list)throws CodeRepeatException{
    	try{
    		String bookNo = entity.getBookNo();
			 String workerCode = entity.getLastModifiedBy();
			 String enterpriseCode = entity.getEnterpriseCode();
			 // 更新数据
			 invJBookRemote.save(entity);
			 if(list != null && list.size() > 0) {
				 Long id = bll.getMaxId(
							"INV_J_BOOK_DETAILS ", "ID");
				 int i = -1;
				 for (LotMaterialInfo model2 : list) {
					 i++;
					// 新建一个物料盘点明细表bean
					InvJBookDetails entity2 = new InvJBookDetails();
					entity2.setId(id+i);
					entity2.setBookNo(bookNo);
					entity2.setMaterialId(model2.getMaterialId());
					entity2.setWhsNo(model2.getWhsNo());
					entity2.setLocationNo(model2.getLocation());
					entity2.setLotNo(model2.getLotNo());
					String qty = model2.getAccount().replace(",", "");
					entity2.setBookQty(Double.parseDouble(qty));
					// 设置盘点状态
					entity2.setBookStatus("PRT");
					// 设置上次修改人
					entity2.setLastModifiedBy(workerCode);
					// 设置企业编码
					entity2.setEnterpriseCode(enterpriseCode);
					invJDetailBookRemote.save(entity2);
				}
			 }
    	}catch(Exception e){
    		throw new RuntimeException();
    	}
    }
	/**
	 * 批号记录数据查询
	 *
	 * @param rowStartIdxAndCount
	 *            分页
	 * @param    delayStore
	 *            待盘仓库
	 * @param    delayLocation
	 *            待盘库位
	 * @param    delayMaterial
	 *            待盘物料
	 * @param   planer
	 *            采购员id
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialList(String delayStore,String delayLocation,String list1,
				String list2,String delayMaterial,String planer,String materialSortId,
				String enterpriseCode,final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			// 数字输出格式化
			String patternNumber = "###,###,###,###,##0.00";
			DecimalFormat dfNumber = new DecimalFormat(patternNumber);
			// 查询sql
			String sqlFirst = "SELECT\n"
					+ "B.MATERIAL_ID as materialId,\n"
					+ "B.MATERIAL_NO as materialNo,\n"
					+ "B.MATERIAL_NAME as materialName,\n"
					+ "B.SPEC_NO as specNo,\n"
					+ "B.STOCK_UM_ID as stockUmId,\n"
					+ "A.WHS_NO as whsNo,\n"
					+ "C.WHS_NAME as whsName,\n"
					+ "A.LOCATION_NO as location,\n"
					+ "D.LOCATION_NAME as locationName,\n"
					+ "A.LOT_NO as lotNo,\n"
					+ "A.OPEN_BALANCE + A.RECEIPT + A.ADJUST - A.ISSUE AS account\n";

				String sql = "from\n" + "INV_J_LOT A,\n"
					+ "INV_C_MATERIAL B, \n" + "INV_C_WAREHOUSE C ,\n"
					+ "INV_C_LOCATION D \n";

			String sql2= " WHERE　\n"
					+ "A.IS_USE = 'Y' AND\n" + "B.IS_USE = 'Y' AND\n"
					+ "C.IS_USE = 'Y' AND\n" + "D.IS_USE(+) = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
					+ "B.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
					+ "C.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
					+ "D.ENTERPRISE_CODE(+) ='" + enterpriseCode + "' AND\n"
					+ "B.MATERIAL_ID = A.MATERIAL_ID AND\n"
					+ "C.WHS_NO = A.WHS_NO AND\n"
					+ "C.IS_INSPECT = 'N' AND\n"
					+ "D.LOCATION_NO(+) = A.LOCATION_NO AND\n"
					+ "D.WHS_NO(+) = A.WHS_NO\n";
//					+ "B.MAERTIAL_CLASS_ID = E.MAERTIAL_CLASS_ID\n";
					// 待盘仓库项目有值的时候，连接这个条件，否则不连接
					if(!"".equals(delayStore)) {
						sql2 += " AND A.WHS_NO = '" + delayStore + "'\n";
					}
					// 待盘库位项目有值的时候，连接这个条件，否则不连接
					if(!"".equals(delayLocation)) {
						sql2 += "AND A.LOCATION_NO = '" + delayLocation + "' \n";
					}
					// 待盘物料类别项目有值的时候，连接这个条件，否则不连接
					if(!"".equals(materialSortId)) {
						if(!"".equals(list2)) {
						sql2 += " AND B.MATERIAL_NO IN ("+ list2 + ") \n";
						} else {
							sql2 += " AND B.MATERIAL_NO in (null) \n";
						}
					}
					// 待盘物料有值的时候，连接这个条件，否则不连接
					if(!"".equals(delayMaterial)) {
						sql2 += " AND B.MATERIAL_NO = '" + delayMaterial + "'\n";
					}
					// 计划员项目有值的时候，连接这个条件，否则不连接
					if(!"".equals(planer)) {
//						sql += ",PUR_C_PLANER F \n";
//						sql2 += " AND F.IS_USE = 'Y'\n";
//						sql2 += " AND F.ENTERPRISE_CODE ='" + enterpriseCode + "'\n";
//						sql2 += "AND F.PLANER = '" + planer + "'\n";
						if(!"".equals(list1)) {
							sql2 += "AND B.MATERIAL_NO IN ("+ list1 + ")\n";
						} else {
							sql2 += "AND B.MATERIAL_NO in (null)\n";
						}
					}
					sql = sql + sql2;
					sql += "order by B.MATERIAL_NO";
					sqlFirst = sqlFirst + sql;
					String sqlCount = "select count(B.MATERIAL_ID) \n";
					sqlCount = sqlCount + sql;
					List<Object> list = bll.queryByNativeSQL(sqlFirst,rowStartIdxAndCount);
					List<LotMaterialInfo> arrlist = new ArrayList<LotMaterialInfo>();
					if (list != null && list.size() > 0) {
						result = new PageObject();
						Iterator it = list.iterator();
						while (it.hasNext()) {
							LotMaterialInfo model = new LotMaterialInfo();
							Object[] data = (Object[]) it.next();
							model.setMaterialId(Long.parseLong(data[0].toString()));
							if (data[1] != null)
								model.setMaterialNo(data[1].toString());
							if (data[2] != null)
								model.setMaterialName(data[2].toString());
							if (data[3] != null)
								model.setSpecNo(data[3].toString());
							if (data[4] != null){
								model.setStockUmId(data[4].toString());
							}
							if (data[5] != null)
								model.setWhsNo(data[5].toString());
							if (data[6] != null)
								model.setWhsName(data[6].toString());
							if (data[7] != null)
								model.setLocation(data[7].toString());
							if (data[8] != null)
								model.setLocationName(data[8].toString());
							if (data[9] != null)
								model.setLotNo(data[9].toString());
							if (data[10] != null)
								model.setAccount(dfNumber.format(data[10]));
							arrlist.add(model);
						}
						if (arrlist.size() > 0) {
							Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
									.toString());
							result.setList(arrlist);
							result.setTotalCount(totalCount);
						}
					}
					return result;
				} catch (RuntimeException e) {
					throw e;
			}
		}

	/**
	 * 物料盘点打印表
	 *
	 * @param delayStore 待盘仓库
	 * @param delayLocation 待盘库位
	 * @param list1 待盘物料分类1
	 * @param list2 待盘物料分类2
	 * @param delayMaterial 待盘物料
	 * @param materialSortId 待盘ID
	 * @param planer 计划员
	 * @param enterpriseCode 企业编码
     * @return MaterialCheckPrintReportBean 物料盘点打印表明细
	 */
	public MaterialCheckPrintReportBean findAllForMaterialCheckReport(String delayStore,String delayLocation,String list1,
			String list2,String delayMaterial,String planer,String materialSortId,
			String enterpriseCode){

		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		MaterialCheckPrintReportBean materialCheckPrintReportBean = new MaterialCheckPrintReportBean();
		List materialList = new ArrayList();
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String nullNumber = "0.00";
		try{
			String sqlFirst = "SELECT\n"
				+ "B.MATERIAL_ID as materialId,\n"
				+ "B.MATERIAL_NO as materialNo,\n"
				+ "B.MATERIAL_NAME as materialName,\n"
				+ "B.SPEC_NO as specNo,\n"
				+ "B.STOCK_UM_ID as stockUmId,\n"
				+ "A.WHS_NO as whsNo,\n"
				+ "C.WHS_NAME as whsName,\n"
				+ "A.LOCATION_NO as location,\n"
				+ "D.LOCATION_NAME as locationName,\n"
				+ "A.LOT_NO as lotNo,\n"
				+ "A.OPEN_BALANCE + A.RECEIPT + A.ADJUST - A.ISSUE AS account\n";

			String sql = "from\n" + "INV_J_LOT A,\n"
				+ "INV_C_MATERIAL B, \n" + "INV_C_WAREHOUSE C ,\n"
				+ "INV_C_LOCATION D \n";
//				+ "INV_C_MATERIAL_CLASS E \n";

		String sql2= " WHERE　\n"
				+ "A.IS_USE = 'Y' AND\n" + "B.IS_USE = 'Y' AND\n"
				+ "C.IS_USE = 'Y' AND\n" + "D.IS_USE(+) = 'Y' AND\n"
//				+ "E.IS_USE = 'Y' AND\n"
				+ "A.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
				+ "B.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
				+ "C.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
				+ "D.ENTERPRISE_CODE(+) ='" + enterpriseCode + "' AND\n"
//				+ "E.ENTERPRISE_CODE ='" + enterpriseCode + "' AND\n"
				+ "B.MATERIAL_ID = A.MATERIAL_ID AND\n"
				+ "C.WHS_NO = A.WHS_NO AND\n"
				+ "C.IS_INSPECT = 'N' AND\n"
				+ "D.LOCATION_NO(+) = A.LOCATION_NO AND\n"
				+ "D.WHS_NO(+) = A.WHS_NO\n";
//				+ "B.MAERTIAL_CLASS_ID = E.MAERTIAL_CLASS_ID\n";
				// 待盘仓库项目有值的时候，连接这个条件，否则不连接
				if(!"".equals(delayStore)) {
					sql2 += " AND A.WHS_NO = '" + delayStore + "'\n";
				}
				// 待盘库位项目有值的时候，连接这个条件，否则不连接
				if(!"".equals(delayLocation)) {
					sql2 += "AND A.LOCATION_NO = '" + delayLocation + "' \n";
				}
				// 待盘物料类别项目有值的时候，连接这个条件，否则不连接
				if(!"".equals(materialSortId)) {
					if(!"".equals(list2)) {
					sql2 += " AND B.MATERIAL_NO IN ("+ list2 + ") \n";
					} else {
						sql2 += " AND B.MATERIAL_NO in (null) \n";
					}
				}
				// 待盘物料有值的时候，连接这个条件，否则不连接
				if(!"".equals(delayMaterial)) {
					sql2 += " AND B.MATERIAL_NO = '" + delayMaterial + "'\n";
				}
				// 计划员项目有值的时候，连接这个条件，否则不连接
				if(!"".equals(planer)) {
//					sql += ",PUR_C_PLANER F \n";
//					sql2 += " AND F.IS_USE = 'Y'\n";
//					sql2 += " AND F.ENTERPRISE_CODE ='" + enterpriseCode + "'\n";
//					sql2 += "AND F.PLANER = '" + planer + "'\n";
					if(!"".equals(list1)) {
						sql2 += "AND B.MATERIAL_NO IN ("+ list1 + ")\n";
					} else {
						sql2 += "AND B.MATERIAL_NO in (null)\n";
					}
				}
				sql = sql + sql2;
				sql += "order by B.MATERIAL_NO";
				sqlFirst = sqlFirst + sql;
				List list=bll.queryByNativeSQL(sqlFirst);
				Iterator it=list.iterator();
				while(it.hasNext()){
					Object[] data=(Object[])it.next();
					MaterialCheckPrintReportListBean model = new MaterialCheckPrintReportListBean();
					if(data[1]!=null){
						model.setMaterialNo(data[1].toString());
					}
					if(data[2]!=null){
						model.setMaterialName(data[2].toString());
					}
					if(data[3]!=null){
						model.setSpecNo(data[3].toString());
					}
					if(data[4]!=null){
						model.setStockUmID(data[4].toString());
					}
					if(data[6]!=null){
						model.setWhsName(data[6].toString());
					}
					if(data[8]!=null){
						model.setLocationName(data[8].toString());
					}
					if(data[9]!=null){
						model.setLotNo(data[9].toString());
					}
					if(data[10]!=null){
						model.setAccountQuantity(dfNumber.format(data[10]));
					}else{
						model.setAccountQuantity(nullNumber);
					}
					model.setRealQuantity("");
					materialList.add(model);
				}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}
		materialCheckPrintReportBean.setMaterialCheckPrintReportList(materialList);
		return materialCheckPrintReportBean;
	}
}