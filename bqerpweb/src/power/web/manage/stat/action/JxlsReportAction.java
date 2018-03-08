package power.web.manage.stat.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.fileupload.util.Streams;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import power.web.comm.AbstractAction;

import com.opensymphony.util.jxl.ItemManger;
import com.opensymphony.util.jxl.ItemMangerImpl;
@SuppressWarnings("serial")
public class JxlsReportAction extends AbstractAction{
	/**
	 * 脱硫综合报表
	 * @throws Exception
	 */
	private File docFile;
	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}
	public void saveTuoLiuItemTmplate() throws Exception{
		String templateUrl =request.getParameter("templateUrl");
		String toFilePath = request.getSession().getServletContext().getRealPath(templateUrl);
		if(docFile != null)
		{
			try {
				InputStream in = new FileInputStream(docFile);
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(toFilePath)));
				Streams.copy(in, out, true);
				in.close();
				out.close();
				if (docFile.exists()) {
					docFile.delete();
				}
				write("succeed");
			} catch (Exception exc) {
				write("failed"); 
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void queryTuoLiuItem() throws Exception
	{
		String date = request.getParameter("date");
		ItemManger im = new ItemMangerImpl();
		im.setDate(date);
		Map beans = new HashMap();
		beans.put("im", im);
		beans.put("date", date);
		XLSTransformer transformer = new XLSTransformer(); 
		String templateUrl = request.getParameter("templateUrl"); 
		String templatePath = request.getSession().getServletContext().getRealPath(templateUrl);
		System.out.println(templatePath);
		InputStream is = new FileInputStream(new File(templatePath)); 
		 
		response.setHeader("Content-Disposition", "attachment; filename="
				+ (new Date()).getTime() + ".xls");
		HSSFWorkbook workbook = transformer.transformXLS(is, beans);
		response.setContentType("application/vnd.ms-excel;charset=UTF-8"); 
		OutputStream os = response.getOutputStream();
		workbook.write(os);
		is.close();
		os.flush();
		os.close();
	}
	/**
	 * 脱硫运行报表
	 * @throws Exception
	 */
	public void queryTuoLiuRun() throws Exception{
		
	}
	/**
	 * 脱硫运行周报
	 * @throws Exception
	 */
	public void queryTuoLiuWeek() throws Exception{
		
	}
}
