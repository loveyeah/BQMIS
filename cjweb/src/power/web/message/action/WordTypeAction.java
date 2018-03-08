package power.web.message.action;

import java.io.IOException;
import java.util.List;
import javax.naming.NamingException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.message.SysCMessageDocType;
import power.ejb.message.SysCMessageDocTypeFacadeRemote;

public class WordTypeAction extends AbstractAction {
	private SysCMessageDocType syscmessage;
	private String method;

	// 取得文档一览
	public void getWordTypeList() throws IOException, NamingException,
			JSONException {
		SysCMessageDocTypeFacadeRemote bll = (SysCMessageDocTypeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("SysCMessageDocTypeFacade");
		List<SysCMessageDocType> wordtype = bll.findAll();
		String jsonstr = JSONUtil.serialize(wordtype);
		write("{root:" + jsonstr + "}");
	}

	// 文档添加
	public void addWordType() throws Exception {
		try {
			if ("insert".equals(method)) {
				SysCMessageDocTypeFacadeRemote bll = (SysCMessageDocTypeFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote(
								"SysCMessageDocTypeFacade");
				SysCMessageDocType model = new SysCMessageDocType();
				if (syscmessage != null) {
					model.setDocTypeName(syscmessage.getDocTypeName());
					model.setLastModifiedBy(employee.getWorkerName());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					bll.save(model);
				}
			}
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	// 文档修改
	public void updateWordType() throws Exception {
		try{
		if ("update".equals(method)) {
			SysCMessageDocTypeFacadeRemote bll = (SysCMessageDocTypeFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("SysCMessageDocTypeFacade");
			Long id = syscmessage.getDocTypeId();
			SysCMessageDocType model = bll.findById(id);
			if (syscmessage != null) {
				model.setDocTypeName(syscmessage.getDocTypeName());
				model.setLastModifiedBy(employee.getWorkerName());
				bll.update(model);
			}
		}
		} catch(CodeRepeatException e){
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}

	}

	// 文档删除
	public void deleteWordType() throws Exception {
		SysCMessageDocTypeFacadeRemote bll = (SysCMessageDocTypeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("SysCMessageDocTypeFacade");
		String code = request.getParameter("docTypeId");
		String[] ids = code.split(",");
		for (int i = 0; i < ids.length; i++) {
			Long docTypeId = Long.parseLong(ids[i]);
			SysCMessageDocType model = bll.findById(docTypeId);
			if (model != null) {
				bll.delete(model);
			}
		}
	}

	public SysCMessageDocType getSyscmessage() {
		return syscmessage;
	}

	public void setSyscmessage(SysCMessageDocType syscmessage) {
		this.syscmessage = syscmessage;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}