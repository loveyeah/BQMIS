/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialnamequery.action;

import java.io.IOException;
import java.io.OutputStream;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvJMaterialAttachment;
import power.ejb.resource.InvJMaterialAttachmentFacadeRemote;
import power.ejb.resource.business.MaterialNameQuery;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


/**
 * 物料查询
 * 
 * @author daichunlin
 * @version 1.0
 */
public class MaterialNameQueryAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 物料主文件remote */
	private MaterialNameQuery remote;
	private InvJMaterialAttachmentFacadeRemote mapRemote;

	/**
	 * 构造函数
	 */
	public MaterialNameQueryAction() {
		remote = (MaterialNameQuery) factory
				.getFacadeRemote("MaterialNameQueryImp");
		mapRemote = (InvJMaterialAttachmentFacadeRemote) factory
				.getFacadeRemote("InvJMaterialAttachmentFacade");
	}

	/**
	 * 查询物料信息
	 * modify by fyyang 090624 增加按物料类别查询
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialNameList() throws JSONException {
		//add by fyyang 091119 flag 区分是否应该除去计算机类物资
		String flag=request.getParameter("flag");
		String materialClassCode=request.getParameter("materialClassCode");
		// 查询字符串
		String fuzzy = request.getParameter("fuzzy");
		
		// add by liuyi 20100504 区分是否为领料单时的查询
		String issue = request.getParameter("issue");
		
		if (fuzzy == null || fuzzy.length() == 0) {
			// 检索所有
			fuzzy = Constants.ALL_DATA;
		}
		// 开始行
		String strStart = request.getParameter("start");
		// 行数
		String strLimit = request.getParameter("limit");
		// 查询
		// modified by liuyi 20100504
//		PageObject obj = remote.getMaterialList(employee.getEnterpriseCode(),
//				fuzzy,materialClassCode,flag, Integer.parseInt(strStart), Integer.parseInt(strLimit));
		PageObject obj = remote.getMaterialList(issue,employee.getEnterpriseCode(),
				fuzzy,materialClassCode,flag, Integer.parseInt(strStart), Integer.parseInt(strLimit));
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 图片信息
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialMap() throws JSONException, IOException {

		// 文档号
		String strdocNo = request.getParameter("docNo");
		InvJMaterialAttachment entity = mapRemote.getMaterialMap(employee.getEnterpriseCode(),strdocNo);
		response.setContentType("image/jpeg");
		if (entity != null) {
			// 获得照片
			byte[] materialMap = entity.getDocContent();
			if (materialMap != null) {
				OutputStream outs = null;
				try {
					outs = response.getOutputStream();
					for (int i = 0; i < materialMap.length; i++) {
						outs.write(materialMap[i]);// 输出到页面
					}
					outs.flush();
				} catch (IOException e) {
					write("{success:true}");
				} finally {
					if (outs != null) {
						outs.close();
					}
				}
			}
		} else {
			write("{success:true'}");
		}

	}
	/**
	 * 查询该用户对应的查询权限
	 */
	public void getQueryRightByWorkId()
	{
		String fileAddr=request.getParameter("fileAddr");
		String strRight= remote.getQueryRightByWorkId(employee.getEmpId(),fileAddr,employee.getEnterpriseCode());
		write(strRight);
	}
}
