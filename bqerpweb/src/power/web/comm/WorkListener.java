package power.web.comm;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WorkListener implements ServletContextListener {

	private Timer timer = null;
	private SampleTask sampleTask = null;

	public void contextDestroyed(ServletContextEvent arg0) {

		timer.cancel();
		System.out.println("定时器已销毁");
	}
	

	public void contextInitialized(ServletContextEvent event) {
		timer = new java.util.Timer(true);
		sampleTask = new SampleTask(event.getServletContext());
		System.out.println("定时器已启动");
		Calendar cal=Calendar.getInstance();  
		cal.set(Calendar.HOUR_OF_DAY,1);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);  
		timer.scheduleAtFixedRate(sampleTask, cal.getTime(), 12*60*60*1000L);//当天1点第一次运行,以后每隔12小时运行一次
		System.out.println("已经添加任务调度表");
	}

	public void autoWorkOff() {
		System.out.println("sltang");
	}
}
