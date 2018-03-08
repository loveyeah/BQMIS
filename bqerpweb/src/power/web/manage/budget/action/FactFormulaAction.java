package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.budget.CbmCFactFormula;
import power.ejb.manage.budget.CbmCFactFormulaFacadeRemote;
import power.ejb.manage.budget.CbmCItem;
import power.ejb.manage.budget.CbmCItemFacadeRemote;
import power.web.comm.AbstractAction;

import power.ejb.manage.budget.form.CbmCFactFormulaForm;

@SuppressWarnings("serial")
public class FactFormulaAction extends AbstractAction{
		private CbmCFactFormulaFacadeRemote remote;
		private CbmCItemFacadeRemote itemfinaceRemote;
		private int limit;
		private int start;
		/**
		 * 构造方法
		 */
		public FactFormulaAction(){
			remote = (CbmCFactFormulaFacadeRemote)factory
			.getFacadeRemote("CbmCFactFormulaFacade");
			itemfinaceRemote = (CbmCItemFacadeRemote) factory
			.getFacadeRemote("CbmCItemFacade");
		}
		/**获取预算指标公式列表**/
		public void getCbmCFactFormulaList()throws JSONException{
			String itemId = request.getParameter("itemId");
			List<CbmCFactFormulaForm> obj = remote.findAll(
					employee.getEnterpriseCode(), itemId);
			write(JSONUtil.serialize(obj));
			
		}
		/**保存对预算指标公式的修改**/
		@SuppressWarnings("unchecked")
		public void saveCbmCFactFormula()throws JSONException{
			String ItemId=request.getParameter("itemId");
			String factExplain=request.getParameter("factExplain");
			factExplain=factExplain.substring(1, factExplain.length()-1);
			try {
				String strUpdate = request.getParameter("isUpdate");
				Object obj = JSONUtil.deserialize(strUpdate);
					List<CbmCFactFormula> addList = new ArrayList<CbmCFactFormula>();
					List<Map> list = (List<Map>) obj;
					for (Map data : list) {
						Long itemId = null;
						Long formulaNo = null;
						String formulaContent = null;
						String fornulaType = null;
						if(data.get("itemId")!= null){
							itemId = Long
									.parseLong(data.get("itemId").toString());
						}
						if (data.get("formulaNo") != null) {
							formulaNo = Long
									.parseLong(data.get("formulaNo").toString());
						}
						if (data.get("formulaContent") != null) {
							formulaContent = data.get("formulaContent").toString();
						}
						if (data.get("fornulaType") != null) {
							fornulaType = data.get("fornulaType").toString();
						}
						CbmCFactFormula model = new CbmCFactFormula();
						model.setItemId(itemId);
						model.setFormulaNo(formulaNo);
						model.setEnterpriseCode(employee.getEnterpriseCode());
						model.setFormulaContent(formulaContent);
						model.setFornulaType(fornulaType);
						model.setIsUse("Y");
						addList.add(model);
					}
					String itemId = request.getParameter("itemId");
					remote.delete(itemId);
					if (addList.size() > 0)
						remote.save(addList);
					CbmCItem bubgetItem=itemfinaceRemote.findById(Long.parseLong(ItemId));
					if(factExplain==null||"[]".equals(factExplain)){
						factExplain="";
					}
					bubgetItem.setFactExplain(factExplain);
					bubgetItem.setFactFrom("2");
					itemfinaceRemote.update(bubgetItem);
					write("{success: true,msg:'保 存 成 功！'}");
				} catch (Exception exc) {
				exc.printStackTrace();
				write("{success: false,msg:'保 存 失 败！'}");
			}
		}
		
		public int getLimit() {
			return limit;
		}
		public void setLimit(int limit) {
			this.limit = limit;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
}
