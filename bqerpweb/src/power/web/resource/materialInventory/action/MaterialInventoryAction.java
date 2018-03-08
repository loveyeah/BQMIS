/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialInventory.action;

import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvJBook;
import power.ejb.resource.InvJBookDetails;
import power.ejb.resource.InvJBookDetailsFacadeRemote;
import power.ejb.resource.InvJBookFacadeRemote;
import power.ejb.resource.PurCPlaner;
import power.ejb.resource.PurCPlanerFacadeRemote;
import power.ejb.resource.business.MaterialPrint;
import power.ejb.resource.form.LotMaterialInfo;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物料盘点表打印
 * 
 * @author chenshoujiang
 * @version 1.0
 */
public class MaterialInventoryAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 计划员维护Remote */
	private PurCPlanerFacadeRemote planerRemote;
	/** 物料Remote */
	private InvCMaterialFacadeRemote materialRemote;
	private MaterialPrint materialPrint;
	/** 物料盘点主表remote*/
	private InvJBookFacadeRemote invJBookRemote;
	/** 物料盘点明细表remote*/
	private InvJBookDetailsFacadeRemote invJBookDetailsRemote;
	/** 单位共通接口 */
	private BpCMeasureUnitFacadeRemote bpcMeasureUnitFacadeRemote;
	
	/**
	 * 构造函数
	 */
	public MaterialInventoryAction() {
		// 单位共通接口
		bpcMeasureUnitFacadeRemote = (BpCMeasureUnitFacadeRemote) factory
				.getFacadeRemote("BpCMeasureUnitFacade");
		// 计划员维护Remote
		planerRemote = (PurCPlanerFacadeRemote) factory
				.getFacadeRemote("PurCPlanerFacade");
		// 物料Remote
		materialRemote = (InvCMaterialFacadeRemote) factory
				.getFacadeRemote("InvCMaterialFacade");
		materialPrint =(MaterialPrint) factory
			.getFacadeRemote("MaterialPrintImp");
		// 物料盘点主表remote
		invJBookRemote = (InvJBookFacadeRemote) factory.getFacadeRemote("InvJBookFacade");
		// 物料盘点明细表remote
		invJBookDetailsRemote = (InvJBookDetailsFacadeRemote) factory.getFacadeRemote("InvJBookDetailsFacade");
	}
	
	/**
	 * 获取人员编码和人员名称
	 * @throws JSONException 
	 * // modify By ywliu 09/05/19
	 */
	@SuppressWarnings("unchecked")
	public void getPlaner() throws JSONException {
//		PageObject obj = planerRemote.findAllList(employee.getEnterpriseCode());
		List<PurCPlaner> list = planerRemote.findPlanerForMaterialInventory(employee.getEnterpriseCode());
		// 获取信息
//		List<PurCPlaner> list = obj.getList();
		if (list.size() > 0) {
			PurCPlaner inv = new PurCPlaner();
			inv.setPlaner("");
			inv.setPlanerName("&nbsp");
			list.add(0, inv);
//			obj.setList(list);
		}
		String str = "{total:" + 0 + ",root:"+ JSONUtil.serialize(list) + "}";
//		String str = JSONUtil.serialize(list);
		write(str);
	}
	
	/**
	 * @param sb1
	 * @param sb2
	 * @param planer
	 * @param materialSortId
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void isMaterialSort(StringBuilder sb1,StringBuilder sb2,String planer,String materialSortId) throws JSONException {
		if(!"".equals(planer)) {
			// 创建一个对象
			PageObject object = new PageObject();
			// 调用函数
			object = planerRemote.findByPlaner(planer, employee.getEnterpriseCode());
			// 获取信息
			List<PurCPlaner> list = object.getList();
			int i = 0;
			if(list != null && list.size() > 0) {
				// 把list里的数据取出
				for (PurCPlaner model : list) {
					i++;
					// 如果是否物料分类是"N",就直接把编码放到list中
					if(model.getIsMaterialClass().equals("N")) {
						sb1.append("'");
						sb1.append(model.getMaterialOrClassNo());
						sb1.append("'");
					} else if(model.getIsMaterialClass().equals("Y")){
						// 如果是否物料分类是"Y",就要先进行查询
						List<InvCMaterial> list1 = materialRemote.findAllChildrenNode(model.getMaterialOrClassNo(),
								employee.getEnterpriseCode());
						int j = 0;
						for (InvCMaterial model1 : list1) {
							j++;
							if(model1.getMaterialNo() != null) {
								sb1.append("'");
								sb1.append(model1.getMaterialNo());
								sb1.append("'");
							}
							if (j < list1.size()) {
								sb1.append(",");
							}
						}
					}
					if (i < list.size()) {
						sb1.append(",");
					}
				}
			}
		}
		// 如果待盘物料类别的编码不为空的话
		if(!"".equals(materialSortId)) {
			int k = 0;
			List<InvCMaterial> list2 = materialRemote.findAllChildrenNode(materialSortId,
					employee.getEnterpriseCode());
			
			for (InvCMaterial model2 : list2) {
				k++;
				if(model2.getMaterialNo() != null) {
					sb2.append("'");
					sb2.append(model2.getMaterialNo());
					sb2.append("'");
				}
				if (k < list2.size()) {
					sb2.append(",");
				}
			}
		}
	}
	
	/**
	 * 查询物料盘点信息
	 *
	 * @exception Exception
	 */
	public void getMaterialList() throws Exception {
		
		// 获取待盘物料类别
		String materialSortId = request.getParameter("materialSortId");
		// 待盘仓库
		String delayStore = request.getParameter("delayStore");
		// 待盘库位
		String delayLocation = request.getParameter("delayLocation");
		// 待盘物料
		String delayMaterial = request.getParameter("delayMaterial");
		// 计划员
		String planer = request.getParameter("planer");
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		/** 获取开始行数和查询行数 */
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		isMaterialSort(sb1,sb2,planer,materialSortId);
		PageObject result = new PageObject();
		// 如果非空
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 把开始，和要查询的个数作为参数传入
			result = materialPrint.getMaterialList(delayStore, delayLocation, sb1.toString(), sb2.toString(), 
						delayMaterial, planer,materialSortId, employee.getEnterpriseCode(),start, limit);
		} else {
			// 要是不为空的话，就不传入
			result = materialPrint.getMaterialList(delayStore, delayLocation, sb1.toString(), sb2.toString(), 
					delayMaterial, planer, materialSortId,employee.getEnterpriseCode());
		}
		// 输出
		String strOutput = "";
		//　要是查询结果不为空的话，就赋值
		if(result.getList() != null && result.getList().size() > 0) {
			List<LotMaterialInfo> list = result.getList();
			Long totalCount = result.getTotalCount();
			for(int i = 0; i < list.size(); i++) {
				BpCMeasureUnit object = new BpCMeasureUnit();
				if(list.get(i).getStockUmId() != null) {
					object = bpcMeasureUnitFacadeRemote.findById(Long.parseLong(list.get(i).getStockUmId()));
					if(object != null && object.getUnitName() != null) {
						list.get(i).setStockUmId(object.getUnitName());
					} else {
						list.get(i).setStockUmId("");
					}
				} else {
					list.get(i).setStockUmId("");
				}
			}
			PageObject object = new PageObject();
			object.setList(list);
			object.setTotalCount(totalCount);
			strOutput = JSONUtil.serialize(object);
		} else {
			//　　 否则设为空值
			strOutput = "{\"list\":[],\"totalCount\":0}";
		}
		write(strOutput);
	}
	
	/**
	 * 保存盘点数据
	 * @throws Exception 
	 */
	public void saveMaterialPrint() throws Exception {
		StringBuilder sb = new StringBuilder();	
		try {
			// 盘点单号
			String bookNo = invJBookRemote.getMaxBookNo("PD000000","INV_J_BOOK","BOOK_ID");
			// 获取待盘物料类别
			String materialSortId = request.getParameter("materialSortId");
			// 待盘仓库
			String delayStore = request.getParameter("delayStore");
			// 待盘库位
			String delayLocation = request.getParameter("delayLocation");
			// 计划员
			String planer = request.getParameter("planer");
			// 待盘物料
			String delayMaterial = request.getParameter("delayMaterial");
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			isMaterialSort(sb1,sb2,planer,materialSortId);
			PageObject result = new PageObject();
			// 要是不为空的话，就不传入
			result = materialPrint.getMaterialList(delayStore, delayLocation, sb1.toString(), sb2.toString(), 
					delayMaterial, planer, materialSortId,employee.getEnterpriseCode());
			List<LotMaterialInfo> list = result.getList();
			if(list != null && list.size() > 0) {
				for(int k = 0 ; k < list.size(); k++) {
					LotMaterialInfo modelInfo =  list.get(k);
					PageObject obj = invJBookDetailsRemote.getBookStatus(modelInfo.getMaterialId().toString(), 
							modelInfo.getWhsNo(), modelInfo.getLocation(), modelInfo.getLotNo(), employee.getEnterpriseCode());
					 List<InvJBookDetails> listStatus = obj.getList();
					 if(listStatus != null && listStatus.size() > 0) {
						 for(int i = 0; i < listStatus.size(); i++) {
							 if(listStatus.get(i).getBookStatus().equals("PRT")) {
								 sb.append("{").append("\"is\"").append(":\"").append("PRT")
						            .append("\",").append("\"materialNo\"").append(":\"").append(modelInfo.getMaterialNo())
						            .append("\",").append("\"column\"").append(":\"").append(k+1)
						            .append("\"}");
				            	write(sb.toString());
				            	return;
							 }
						 }
					 }
				 }
			}
			// 新建一个物料盘点主表bean
			InvJBook entity = new InvJBook();
			// 设置盘点单号
			entity.setBookNo(bookNo);
			// 设置待盘仓库
			entity.setBookWhs(delayStore);
			//　设置待盘库位
			entity.setBookLocation(delayLocation);
			// 设置待盘物料类别
			entity.setBookMaterialClass(materialSortId);
			// 设置制单人
			entity.setBookMan(employee.getWorkerCode());
			// 设置盘点状态
			entity.setBookStatus("PRT");
			// 设置上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 设置企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 保存数据 实现回滚功能
			materialPrint.saveMaterial(entity, list);
            sb.append("{").append("\"is\"").append(":\"").append("true")
            .append("\",").append("\"bookNo\"").append(":\"").append(
                    bookNo).append("\"}");
            write(sb.toString());
            } catch (Exception e) {
            	sb.append("{").append("\"is\"").append(":\"").append("false")
                 	.append("\"}");
            	write(sb.toString());
            	throw e;
        }
	}
}

