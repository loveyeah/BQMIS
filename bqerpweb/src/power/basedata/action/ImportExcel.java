package power.basedata.action; 
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
public class ImportExcel {
	public  Long maxTaskId = 1350L;
	public  Long maxStepId = 0L; 
	protected NativeSqlHelperRemote bll;
	List sqls  = new ArrayList();
	public ImportExcel()
	{
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("NativeSqlHelper"); 
		this.maxTaskId = bll.getMaxId("run_c_opticket_task", "operate_task_id");
		this.maxStepId = bll.getMaxId("run_c_opticketstep", "operate_step_id");
	}
	public  void insertToDB(File ef,int equId,String equCode)
	{ 
		MyFileFilter fileter = new MyFileFilter();
		File[] taskFileList = ef.listFiles(fileter); 
		 
		for (int m = 0; m < taskFileList.length; m++) {
			File tf = taskFileList[m];
			if (tf.isFile()) { 
				insertTask(equId,equCode,m,tf);
			}

		}  
		bll.exeNativeSQL(sqls); 
	}
	
	public  void insertTask(int parentId,String parentCode,int displayNo,File taskFile) {
		 String templateSql = 
		    	"insert into run_c_opticket_task\n" +
		    	"  (operate_task_id,\n" + 
		    	"   operate_task_code,\n" + 
		    	"   operate_task_name,\n" + 
		    	"   parent_operate_task_id,\n" + 
		    	"   is_task,\n" + 
		    	"   operate_task_explain,\n" + 
		    	"   display_no,\n" + 
		    	"   is_main,\n" + 
		    	"   enterprise_code,\n" + 
		    	"   is_use,\n" + 
		    	"   modify_by,\n" + 
		    	"   modify_date)\n" + 
		    	"values(%d,\n" + 
		    	"'%s','%s',%d,'N',null,%d,null,'hfdc','Y','999999',sysdate)\n"; 
		 String stepSql = 
			 "insert into run_c_opticketstep\n" +
			 "  (operate_step_id,\n" + 
			 "   operate_task_id,\n" + 
			 "   operate_step_name,\n" + 
			 "   display_no,\n" + 
			 "   is_main,\n" + 
			 "   memo,\n" + 
			 "   enterprise_code,\n" + 
			 "   is_use,\n" + 
			 "   modify_by,\n" + 
			 "   modify_date)\n" + 
			 "values(%d,%d,'%s',%d,'Y',null,'hfdc','Y','999999',sysdate)\n";

		//主键,编号,名称,父ID,排序 
//		StringBuffer sb = new StringBuffer(); 
		InputStream is;
		try {
			is = new FileInputStream(taskFile);
			jxl.Workbook wb = Workbook.getWorkbook(is); // 得到工作薄
			jxl.Sheet st = wb.getSheet(0);// 得到工作薄中的第一个工作表 
			int rsRows = st.getRows(); // 得到excel的总行数
			String taskName = st.getCell(0, 3).getContents();
			taskName = taskName.substring(5); 
			String tempSql = String.format(templateSql, new Object[]{ ++maxTaskId ,parentCode+String.format("%03d", displayNo),taskName,parentId,displayNo});
			sqls.add(tempSql);
			//sb.append(tempSql); 
			for (int i = 6; i < rsRows; i++) {
				String isMemo = st.getCell(1, i).getContents();
				Cell cell = st.getCell(3, i);
				try {
					String stepInfo = cell.getContents();
					if (isMemo.trim().equals("备注")) {
						continue;
					}
					if (stepInfo.trim().equals("以下空白")) {
						break;
					}
					if (!stepInfo.trim().equals("")) {
						//Id,taskId,name,orderby
						String temp = String.format(stepSql, new Object[]{++maxStepId,maxTaskId,stepInfo,i-5});
						sqls.add(temp);
//						sb.append(temp);
					}
				} catch (Exception e) {
					// 如果EXCEL文件中输入的数据有错，则跳过此行数据
					System.out.println("有错误，不能导入，请检查您的excel文件\n");
					continue;
				}
			}
			wb.close();// 关闭工作薄
			is.close();// 关闭输入流   
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
}
