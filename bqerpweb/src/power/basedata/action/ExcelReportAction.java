package power.basedata.action; 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.report.JxlReports;
import power.ejb.report.JxlReportsFacadeRemote;
import power.ejb.report.JxlReportsRight;
import power.ejb.report.JxlReportsRightFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.util.jxl.JXLExcelUtils;
@SuppressWarnings("serial")
public class ExcelReportAction extends AbstractAction {
	JxlReportsFacadeRemote bll = (JxlReportsFacadeRemote) Ejb3Factory.getInstance()
	.getFacadeRemote("JxlReportsFacade");  
	
	File xlsFile;
	private JxlReportsRightFacadeRemote remote;
	private int start;
	private int limit;
	private JxlReportsRight model;
	public ExcelReportAction(){
		remote = (JxlReportsRightFacadeRemote) factory
		.getFacadeRemote("JxlReportsRightFacade");
	}
	/**
	 * 增加模板文件
	 * @throws IOException 
	 */
	public void add() throws IOException {
		try {
			String code = request.getParameter("code");
			String memo = request.getParameter("memo");
			String isUse = request.getParameter("isUse");
			String dateType = request.getParameter("dateType");
			String operateMethod = request.getParameter("operateMethod");
			JxlReports tp = null;
			if (operateMethod.equals("add")) 
			{
				tp = new JxlReports(); 
				tp.setCode(code);
			}
			else
			{
				tp = bll.findById(code);
			}
			tp.setIsUse(isUse);
			tp.setDateType(dateType); 
			System.out.println("hi:Iam"+xlsFile);
			if(xlsFile != null){
				FileInputStream stream = new FileInputStream(xlsFile);
				ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
				byte[] b = new byte[1000];
				int n;
				while ((n = stream.read(b)) != -1)
					out.write(b, 0, n);
				stream.close();
				out.close();
				tp.setContent(out.toByteArray());
			}
			tp.setMemo(memo);
			if (operateMethod.equals("add"))
				bll.save(tp);
			else
				bll.update(tp);
		} catch (CodeRepeatException exc) {
			write("{failure:true,msg:'"+exc.getMessage()+"'}"); 
		}
	}
	/**
	 * 删除模板文件
	 */
	public void delete ()
	{
		String code = request.getParameter("code");
		JxlReports entity = bll.findById(code);
		bll.delete(entity); 
	}
	public void getList() throws JSONException
	{
		List<JxlReports> list = bll.findAll();
		if (list != null)
			for (JxlReports o : list) {
				o.setContent(null);
			}
		write(JSONUtil.serialize(list));
	}
	public void viewTemplate() throws IOException
	{ 
			String code = request.getParameter("code");
			JxlReports entity = bll.findById(code);
			response.setContentType("application/vnd.ms-excel"); 
			response.setHeader("Content-Disposition","attachment;filename="+entity.getCode()+".xls");
			response.getOutputStream().write(entity.getContent());
		 
	}
	
	public void saveRoleUsers() throws Exception {
		
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);
			List<JxlReportsRight> addList = new ArrayList<JxlReportsRight>();
			List<JxlReportsRight> updateList = new ArrayList<JxlReportsRight>();
			List<Map> list = (List<Map>) obj;
			try {
				if(list != null && list.size()>0)
				{
				for (Map data : list) {

					String id = null;
					String code = null;

					String workerCode = null;

					if (data.get("model.id") != null) {
						id = data.get("model.id").toString();
					}

					if (data.get("model.code") != null) {
						code = data.get("model.code").toString();
					}

					if (data.get("model.workerCode") != null) {
						workerCode = data.get("model.workerCode").toString();
					}

					JxlReportsRight model = new JxlReportsRight();

					// 增加
					if (id == null || "".equals(id)) {

						if (code != null && !code.equals(""))
							model.setWorkerCode(workerCode);
						model.setCode(code);

						addList.add(model);
					} else {
						model = remote.findById(Long.parseLong(id));

						if (code != null && !code.equals(""))

							model.setWorkerCode(workerCode);

						updateList.add(model);
					}

				}
			}
			

			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");

			}catch (CodeRepeatException e) {
				String out = "{success:true,msg :'" + e.getMessage() + "'}";
				write(out);
			}
			
		
		
	}
	
	public void getUsersList() throws JSONException {
		String code = request.getParameter("code");
		PageObject obj = remote.getAllUsers(code, start, limit);
		write(JSONUtil.serialize(obj));
	}
	public void getReportModelList() throws JSONException {
		String workerCode  = employee.getWorkerCode();
		PageObject obj = bll.findReportModelList(workerCode, start,limit);
		write(JSONUtil.serialize(obj));
	}
	/**
	 * 测试模板文件
	 */
	public void test() {
		String template = request.getParameter("template");
		String title = request.getParameter("title");
		String dateStr = request.getParameter("date");
		String workerCode = employee.getWorkerName();
		String hiddenCols = request.getParameter("hiddenCols");
		String strWhere = request.getParameter("strWhere");
		String strWhere2 = request.getParameter("strWhere2");
		String strWhere3 = request.getParameter("strWhere3");  
		// 封装参数
		Properties arguments = new Properties();
		if (template != null)
			arguments.setProperty("template", template);
		else 
			return;
		if (title != null)
			arguments.setProperty("title", title);
		if (dateStr != null)
			arguments.setProperty("dateStr", dateStr);
		if (workerCode != null)
			arguments.setProperty("workerCode", workerCode);
		if (hiddenCols != null)
			arguments.setProperty("hiddenCols", hiddenCols);
		if (strWhere != null)
			arguments.setProperty("strWhere", strWhere);
		if (strWhere2 != null)
			arguments.setProperty("strWhere2", strWhere2);
		if (strWhere3 != null)
			arguments.setProperty("strWhere3", strWhere3); 
		JXLExcelUtils jxl = new JXLExcelUtils(); 
		response.setContentType("application/vnd.ms-excel"); 
		String fileName ; 
		try {
			if(title != null && !title.trim().equals(""))
				fileName = new String(title.getBytes("GB2312"), "ISO_8859_1"); 
			else
				fileName = template;
			//inline(在线打开)，attachment（下载）   
		    response.setHeader("Content-Disposition","inline;filename="+fileName+".xls"); 
		    OutputStream out = response.getOutputStream(); 
		    //InputStream is = this.getClass().getClassLoader().getResourceAsStream(url);
			//InputStream is = session.getServletContext().getResourceAsStream(url);
		    JxlReports xlsContent = bll.findById(template);
		    InputStream is = new ByteArrayInputStream(xlsContent.getContent());
		    if(is == null )
			{ 
				System.out.println("没有维护模块文件！");
				return;
			}
		    WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			Workbook fromWb = Workbook.getWorkbook(is, setting);
			is.close(); 
			WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
			fromWb.close();
			jxl.createExcelFileByTemplate(toWb, arguments);
			
			toWb.write();
			toWb.close(); 
//			out.close(); 
		} catch (Exception exc) {
			exc.printStackTrace();
		} 
	}
	public File getXlsFile() {
		return xlsFile;
	}
	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public JxlReportsRight getModel() {
		return model;
	}
	public void setModel(JxlReportsRight model) {
		this.model = model;
	}
	 
}
