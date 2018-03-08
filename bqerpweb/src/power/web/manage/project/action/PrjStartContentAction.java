package power.web.manage.project.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.web.comm.AbstractAction;
import power.ejb.hr.reward.HrJMonthRewardDetail;
import power.ejb.manage.project.*;
import power.ear.comm.ejb.PageObject;

@SuppressWarnings("serial")
public class PrjStartContentAction extends AbstractAction {
	private PrjJStartReport content;
	private PrjJStartContentFacadeRemote contentRemote;
	private PrjJStartReportFacadeRemote remote ;
	public PrjStartContentAction() {
		contentRemote = (PrjJStartContentFacadeRemote) factory.getFacadeRemote("PrjJStartContentFacade");
		remote = (PrjJStartReportFacadeRemote)factory.getFacadeRemote("PrjJStartReportFacade");
	}
	
	public void saveOpenRegister() throws Exception{
		String reportId=request.getParameter("reportId");
		long id=Long.parseLong(reportId);
		PrjJStartReport model=remote.findReportById(id);
		if (content.getApproveChargeBy()!=null&&!content.getApproveChargeBy().equals("")) {
			model.setApproveChargeBy(content.getApproveChargeBy());
		};
		model.setApproveDate(content.getApproveDate());
		model.setApproveText(content.getApproveText());
		model.setWorkApproveDate(content.getWorkApproveDate());
		model.setWorkChargeBy(content.getWorkChargeBy());
		model.setWorkOperateBy(content.getWorkOperateBy());
		model.setIsBackEntry("Y");
		model.setBackEntryBy(employee.getWorkerCode());
		model.setBackEnrtyDate(new Date());
		
		remote.update(model);
	}
    
	public void saveOrUpdateContent() throws Exception{
		String reportId=request.getParameter("reportId");
		String sortId=request.getParameter("sort");
		long id=Long.parseLong(reportId);
		PrjJStartReport model=remote.findReportById(id);
		String isAdd = request.getParameter("isAdd");
		List<Map> addList = (List<Map>) JSONUtil.deserialize(isAdd);
		String isUpdate = request.getParameter("isUpdate");
		List<Map> updateList = (List<Map>) JSONUtil.deserialize(isUpdate);
		List<PrjJStartContent> addDetailList = null;
		List<PrjJStartContent> updateDetailList = null;
		if(addList != null && addList.size() > 0) {
			addDetailList = new ArrayList<PrjJStartContent>();
			for (Map data : addList) {
				PrjJStartContent detailModel = new PrjJStartContent();
				detailModel.setContentType(sortId);
				if (data.get("content") != null && !"".equals(data.get("content"))) {
					detailModel.setReoprtContent(data.get("content").toString());
				}
				detailModel.setReportId(id);
				detailModel.setIsUse("Y");
				detailModel.setEnterpriseCode(employee.getEnterpriseCode());
				detailModel.setEntryBy(employee.getWorkerCode());
				detailModel.setEntryDate(new Date());
				addDetailList.add(detailModel);
			}
		}
		
		if(updateList != null && updateList.size() > 0) {
			updateDetailList = new ArrayList<PrjJStartContent>();
			for (Map data : updateList) {
				PrjJStartContent detailModel = new PrjJStartContent();
				//detailModel.setRewardId(Long.parseLong(rewardId));
				if (data.get("reportContentId") != null && !"".equals(data.get("reportContentId"))) {
					detailModel.setReoprtContentId(Long.parseLong(data.get("reportContentId").toString()));
				}
				if (data.get("reportsId") != null && !"".equals(data.get("reportsId"))) {
					detailModel.setReportId(Long.parseLong(data.get("reportsId").toString()));
				}
				if (data.get("isUse") != null && !"".equals(data.get("isUse"))) {
					detailModel.setIsUse(data.get("isUse").toString());
				}
				if (data.get("content") != null && !"".equals(data.get("content"))) {
					detailModel.setReoprtContent(data.get("content").toString());
				}
				detailModel.setIsUse("Y");
				detailModel.setReportId(id);
				detailModel.setContentType(sortId);
				detailModel.setEntryBy(employee.getWorkerCode());
				detailModel.setEntryDate(new Date());
				detailModel.setEnterpriseCode(employee.getEnterpriseCode());
				updateDetailList.add(detailModel);
			}
		}
		String detailIds = request.getParameter("ids");
		if(detailIds != "") {
			contentRemote.delete(detailIds);
		} else {
			contentRemote.saveOrUpdateDetailList(addDetailList, updateDetailList);
		}
		write("{success:true,msg:'操作成功！'}");
	}
	
	public void saveOrUpdateOperation() throws Exception{
		String reportId=request.getParameter("reportId");
		String sortId=request.getParameter("sort");
		long id=Long.parseLong(reportId);
		PrjJStartReport model=remote.findReportById(id);
		String isAdd = request.getParameter("isAdd");
		List<Map> addList = (List<Map>) JSONUtil.deserialize(isAdd);
		String isUpdate = request.getParameter("isUpdate");
		List<Map> updateList = (List<Map>) JSONUtil.deserialize(isUpdate);
		List<PrjJStartContent> addDetailList = null;
		List<PrjJStartContent> updateDetailList = null;
		if(addList != null && addList.size() > 0) {
			addDetailList = new ArrayList<PrjJStartContent>();
			for (Map data : addList) {
				PrjJStartContent detailModel = new PrjJStartContent();
				detailModel.setContentType(sortId);
				if (data.get("operation") != null && !"".equals(data.get("operation"))) {
					detailModel.setReoprtContent(data.get("operation").toString());
				}
				detailModel.setReportId(id);
				detailModel.setIsUse("Y");
				detailModel.setEnterpriseCode(employee.getEnterpriseCode());
				detailModel.setEntryBy(employee.getWorkerCode());
				detailModel.setEntryDate(new Date());
				addDetailList.add(detailModel);
			}
		}
		
		if(updateList != null && updateList.size() > 0) {
			updateDetailList = new ArrayList<PrjJStartContent>();
			for (Map data : updateList) {
				PrjJStartContent detailModel = new PrjJStartContent();
				//detailModel.setRewardId(Long.parseLong(rewardId));
				if (data.get("reportContentId") != null && !"".equals(data.get("reportContentId"))) {
					detailModel.setReoprtContentId(Long.parseLong(data.get("reportContentId").toString()));
				}
				if (data.get("reportsId") != null && !"".equals(data.get("reportsId"))) {
					detailModel.setReportId(Long.parseLong(data.get("reportsId").toString()));
				}
				if (data.get("isUse") != null && !"".equals(data.get("isUse"))) {
					detailModel.setIsUse(data.get("isUse").toString());
				}
				if (data.get("operation") != null && !"".equals(data.get("operation"))) {
					detailModel.setReoprtContent(data.get("operation").toString());
				}
				detailModel.setIsUse("Y");
				detailModel.setReportId(id);
				detailModel.setContentType(sortId);
				detailModel.setEntryBy(employee.getWorkerCode());
				detailModel.setEntryDate(new Date());
				detailModel.setEnterpriseCode(employee.getEnterpriseCode());
				updateDetailList.add(detailModel);
			}
		}
		String detailIds = request.getParameter("ids");
		if(detailIds != "") {
			contentRemote.delete(detailIds);
		} else {
			contentRemote.saveOrUpdateDetailList(addDetailList, updateDetailList);
		}
		write("{success:true,msg:'操作成功！'}");
	}
	
	public void queryOperation() throws Exception{
			 Object objstart=request.getParameter("start");
			    Object objlimit=request.getParameter("limit");
			    String sortId=request.getParameter("sort");
				String reportId=request.getParameter("reportId");
				int id=Integer.parseInt(reportId);
                PageObject obj=new  PageObject();
			    if(objstart!=null&&objlimit!=null)
			    {
			        int start = Integer.parseInt(request.getParameter("start"));
					int limit = Integer.parseInt(request.getParameter("limit"));
					obj=contentRemote.findOperationList(employee.getEnterpriseCode(), sortId, id, start,limit);
			    }
			    else
			    {
			    	obj=contentRemote.findOperationList(employee.getEnterpriseCode(),null,0);
			    }
			    String str=JSONUtil.serialize(obj);
				write(str);
		
	}
	
	public void queryContent() throws Exception{
		 Object objstart=request.getParameter("start");
		    Object objlimit=request.getParameter("limit");
		    String sortId=request.getParameter("sort");
			String reportId=request.getParameter("reportId");
			int id=Integer.parseInt(reportId);
           PageObject obj=new  PageObject();
		    if(objstart!=null&&objlimit!=null)
		    {
		        int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				obj=contentRemote.findOperationList(employee.getEnterpriseCode(), sortId, id, start,limit);
		    }
		    else
		    {
		    	obj=contentRemote.findOperationList(employee.getEnterpriseCode(),null,0);
		    }
		    String str=JSONUtil.serialize(obj);
			write(str);
	
}
	public PrjJStartReport getContent() {
		return content;
	}

	public void setContent(PrjJStartReport content) {
		this.content = content;
	}
}
