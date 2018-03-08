package power.basedata.action;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter{ 
	public boolean accept(File arg0) {
		 String filename = arg0.getName().toLowerCase();   
         if(filename.contains(".xls")){   
             return true;   
         }else{   
             return false;   
         }   
	}
	
}
