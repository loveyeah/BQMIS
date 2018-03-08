package power.web.run.runlog.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.runlog.shift.RunCShift;
import power.ejb.run.runlog.shift.RunCShiftFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftInitial;
import power.ejb.run.runlog.shift.RunCShiftInitialFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTime;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.web.comm.AbstractAction;

public class RunLogDutyQueryAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	RunCShiftInitialFacadeRemote remote;
	RunCShiftFacadeRemote sremote;
	RunCShiftTimeFacadeRemote tremote;
	private String specialityCode;
	private String specialityName;
	private String startDate;
	private String endDate;
	private RunCShiftInitial model;
	RunCShift shiftModel;
	RunCShiftTime timeModel;
	/**
	 * 构造函数
	 */
	public RunLogDutyQueryAction() {
		remote =(RunCShiftInitialFacadeRemote) factory.getFacadeRemote("RunCShiftInitialFacade");
		sremote=(RunCShiftFacadeRemote) factory.getFacadeRemote("RunCShiftFacade");
		tremote=(RunCShiftTimeFacadeRemote) factory.getFacadeRemote("RunCShiftTimeFacade");
	}

/**
 * 排班算法
 * (intervalDays%10)%3 == 0  返回初始设置
 * (intervalDays%10)%3 == 1  1,2不动,4,5,6往前各移一位,3=>6
 * (intervalDays%10)%3 == 2  4,5不动,2,3往前各移一位,1=>6
 * @param intervalDays 与初始化天数间隔的天数
 * @param shiftAmount 
 * @param initSeq
 * @return
 */
    private String[] GetSequenceByIntervalDays(int intervalDays, int amount,String[] initSeq,String seqType)
    { 
    	int shiftAmount=initSeq.length;
        String[] array = new String[amount];
        String [] shfit=new String[shiftAmount];
        if(seqType.equals("2"))//灞桥排班算法
        {
        	double interday=intervalDays/2;
         	int moveStep = (int)Math.floor(interday) % shiftAmount;
        	 for (int i = 0; i < shiftAmount; i++)
            {
                //array[shiftAmount-i-1] = initSeq[(moveStep + i) % shiftAmount];
        		 shfit[i]=initSeq[(moveStep + i) % shiftAmount];
            }
        	if(intervalDays % 2 == 0)
        	{
	           array[0]=shfit[0];
	           array[1]=shfit[1];
	           array[2]="";
	           for(int j=2;j<shiftAmount;j++)
	           {
	        	   array[j+1]=shfit[j];
	           }
        	}
        	else
        	{
        		for(int j=0;j<shiftAmount;j++)
        		{
        			array[j]=shfit[j];
        			array[amount-1]="";
        		}
        	}
        }
        else if(seqType.equals("1"))//灞桥排班算法
        {
        	double interday=intervalDays/2.0;
        	int moveStep = (int)Math.round(interday) % shiftAmount;
        	for (int i = 0; i < shiftAmount; i++)
            {
                //array[shiftAmount-i-1] = initSeq[(moveStep + i) % shiftAmount];
        		 shfit[i]=initSeq[(moveStep + i) % shiftAmount];
            }
        	if(intervalDays % 2 == 0)
        	{
        		for(int j=0;j<shiftAmount;j++)
        		{
        			array[j]=shfit[j];
        		}
        		array[amount-1]="";
        	}
        	else
        	{
        	   array[0]=shfit[0];
 	           array[1]=shfit[1];
 	           array[2]="";
 	           for(int j=2;j<shiftAmount;j++)
 	           {
 	        	   array[j+1]=shfit[j];
 	           }
        	}
        }
        else//一般排班算法
        {
        	int moveStep = intervalDays % shiftAmount;
            for (int i = 0; i < shiftAmount; i++)
            {
                //array[shiftAmount-i-1] = initSeq[(moveStep + i) % shiftAmount];
                array[i]=initSeq[(moveStep + i) % shiftAmount];
            }
        }
        return array;
        //return null;
    }
    /**
     * 排班查询
     * @throws Exception
     */
	public void dutyQuery() throws Exception{
		try
		{
			List list=remote.findInitialBySpecial(specialityCode, employee.getEnterpriseCode());
			List<RunCShiftInitial> initialList=remote.findListByDate(specialityCode, employee.getEnterpriseCode(), startDate, endDate);
			if(list.isEmpty())
			{
				write("{failure:true,errorMsg:'该专业值班信息未初始化！'}");
			}
			else if(initialList.isEmpty())
			{
				write("{failure:true,errorMsg:'查询日期不在排班初始化设置生效日期内！'}");
			}
			else
			{
				model=initialList.get(0);
				if((model.getShiftAmount() == 0) && (model.getTimeAmount() == 0))
				{
					write("{failure:true,errorMsg:'该专业值班班组或班次没有设置，请设置后查询！'}");
				}
				else
				{
					List<RunCShiftTime> timeList=tremote.findTimeListByNo(model.getInitialNo(), model.getEnterpriseCode());
					List<RunCShift> shfitList=sremote.findListByNO(model.getInitialNo(), model.getEnterpriseCode());
					List<RunCShift> testlis=sremote.findListByNoC(model.getInitialNo(), model.getEnterpriseCode());
					//获取值班班组（灞桥为方便排班增加一个空的值班班组）
					int shiftamount=Integer.parseInt(model.getShiftAmount().toString());//值班班组的个数
					int amount=testlis.size();//值班班组和空班组的个数
					String[] initseq=new String[shiftamount];
					for(int i=0;i<shfitList.size();i++)
					{
						shiftModel=shfitList.get(i);
						initseq[i]=shiftModel.getShiftName();
					}
					String seqType="";
					if(testlis.get(amount-1).getIsShift().equals("2"))//灞桥特定情况
					{
						seqType="1";
					}
					else if(testlis.get(2).getIsShift().equals("2"))//灞桥特定情况
					{
						seqType="2";
					}
					else
					{
						seqType="3";
					}
					String str="{'columModle':[";
					str=str+"{'header':'专业', 'dataIndex' : 'special','align':'center'},{'header':'日期', 'dataIndex' : 'date','align':'center'} ";
					for(int j=0;j<timeList.size();j++){
						timeModel=timeList.get(j);
						if(timeModel.getIsRest().equals("Y"))
						{
							str=str+",{'header':'"+timeModel.getShiftTimeName()+"', 'dataIndex' : 'time"+j+"','align':'center','renderer': changeColor}";
						}
						else
						{
							str=str+",{'header':'"+timeModel.getShiftTimeName()+"', 'dataIndex' : 'time"+j+"','align':'center'}";
						}
					}
					str=str+"],";
					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date sdate=sf.parse(startDate);
					Date edate=sf.parse(endDate);
					long millisecond=edate.getTime()-sdate.getTime(); 
					int days=Math.abs((int)(millisecond/(1000 * 60 * 60 * 24)));
					str=str+"'data' :[";
					for(int x=0;x<=days;x++)
					{
						Calendar displayDate=Calendar.getInstance();   
						displayDate.setTime(sdate); 
						//System.out.println(displayDate.get(Calendar.DAY_OF_MONTH));//今天的日期 
						displayDate.set(Calendar.DAY_OF_MONTH,displayDate.get(Calendar.DAY_OF_MONTH)+x);//让日期加1 
						Date disDate=sf.parse(sf.format(displayDate.getTime()));
						String xxDate=sf.format(displayDate.getTime());
						str=str+"{";
						str=str+"'special':'"+specialityName+"' ,'date':'"+xxDate+"' ";
						long msecond=disDate.getTime()- model.getActiveDate().getTime();
						int toActivateDate=Math.abs((int)(msecond/(1000 * 60 * 60 * 24)));
						String[] shiftTable = GetSequenceByIntervalDays(toActivateDate, amount, initseq,seqType);
						for(int j=0;j<timeList.size();j++){
							str=str+",'time"+j+"':'"+shiftTable[j].toString()+"' ";
						}
						str=str+"},";
					}
					str = str.substring(0, str.length() - 1);
					str=str+"]";
					str=str+",'fieldsNames':[{name:'special'},{name:'date'}";
					for(int j=0;j<timeList.size();j++){
						str=str+",{name:'time"+j+"'}";
					}
					str=str+"]}";
					write("{success:true,json:"+str+"}");
				}
				
			}
		}
		catch(Exception exc)
		{
			throw exc;
		}
		
	}
	/*
	 * 根据排班获取下一值班班组
	 */
	public Object[] getNextShift(String nextdate,int shiftTimeSeq, String specialcode,String enterpriseCode) throws Exception{
		try
		{
			//List list=remote.findInitialBySpecial(specialcode, employee.getEnterpriseCode());
			List list=remote.findInitialBySpecial(specialcode, enterpriseCode);
			List<RunCShiftInitial> initialList=remote.findListByDate(specialcode, enterpriseCode, nextdate, nextdate);
			if(list.isEmpty())
			{
				write("{failure:true,errorMsg:'该专业值班信息未初始化！'}");
				return null;
			}
			else if(initialList.isEmpty())
			{
				write("{failure:true,errorMsg:'该日期没排班！'}");
				return null;
			}
			else
			{
				model=initialList.get(0);
				if((model.getShiftAmount() == 0) && (model.getTimeAmount() == 0))
				{
					write("{failure:true,errorMsg:'该专业值班班组或班次没有设置，请先设置！'}");
					return null;
				}
				else
				{
					int shiftTimeId;
					List<RunCShiftTime> timeList=tremote.findTimeListByNo(model.getInitialNo(), model.getEnterpriseCode());
					List<RunCShift> shfitList=sremote.findListByNO(model.getInitialNo(), model.getEnterpriseCode());
					//获取值班班组
					List<RunCShift> testlis=sremote.findListByNoC(model.getInitialNo(), model.getEnterpriseCode());
					//获取值班班组（灞桥为方便排班增加一个空的值班班组）
					int shiftamount=Integer.parseInt(model.getShiftAmount().toString());//值班班组的个数
					int amount=testlis.size();//值班班组和空班组的个数
					String[] initseq=new String[shiftamount];
					for(int i=0;i<shfitList.size();i++)
					{
						shiftModel=shfitList.get(i);
						//initseq[i]=shiftModel.getShiftName();
						initseq[i]=shiftModel.getShiftId().toString();
					}
					String seqType="";
					if(testlis.get(amount-1).getIsShift().equals("2"))//灞桥特定情况
					{
						seqType="1";
					}
					else if(testlis.get(2).getIsShift().equals("2"))//灞桥特定情况
					{
						seqType="2";
					}
					else
					{
						seqType="3";
					}
					//int amount=Integer.parseInt(model.getShiftAmount().toString());
					Date activeDate=model.getActiveDate();
					//String[] initseq=new String[amount];
//					for(int i=0;i<shfitList.size();i++){
//						shiftModel=shfitList.get(i);
//						//initseq[amount-i-1]=shiftModel.getShiftName();
//						initseq[i]=shiftModel.getShiftId().toString();
//					}
					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date nexdate=sf.parse(nextdate);
					long millisecond=nexdate.getTime()-activeDate.getTime(); 
					int days=Math.abs((int)(millisecond/(1000 * 60 * 60 * 24)));
					String[] shiftTable = GetSequenceByIntervalDays(days, amount, initseq,seqType);
					List<RunCShiftTime> tlist=tremote.findTimeListByNo(model.getInitialNo(), enterpriseCode);
					int length=tlist.size();
					String[] dutyTable = new String[length];
                    int[] timeTable = new int[length]; 
                     if (length > 0)
                     {
                         int j = 0;
                         for (int i = 0; i < length; i++)
                         {
                        	 RunCShiftTime dr = tlist.get(i);
                             if (!dr.getIsRest().equals("Y"))
                             {
                                 dutyTable[j] = shiftTable[i];//除去休息班的排班
                                 timeTable[j] = Integer.parseInt(dr.getShiftTimeId().toString());
                                 j++;
                             } 
                         }
                     }
                     if ((dutyTable[shiftTimeSeq-1] != null) && (timeTable[shiftTimeSeq-1] !=0))
                     {
                         shiftTimeId = timeTable[shiftTimeSeq-1];
                         Object[] o=new Object[2];
                         o[0]=shiftTimeId;
                         o[1]=dutyTable[shiftTimeSeq-1];
                         return o;
                     }
                     else
                     {
                    	 write("{failure:true,errorMsg:'没有查到班组'}");
                         return null;
                     }
					
					}
				}
		}
		
		catch(Exception ext)
		{
			throw ext;
		}
	}
	/*
	 * 根据专业查询有效期内的班次列表
	 */
	public void findShfitTimeBySpecial() throws JSONException
	{
		
		Date date=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String sd=sf.format(date);
		List<RunCShiftInitial> initialList=remote.findListByDate(specialityCode, employee.getEnterpriseCode(), sd, sd);
		if(initialList.isEmpty())
		{
			write("{failure:true,errorMsg:'查询日期不在初始化设置生效日期内！'}");
		}
		else
		{
			model=initialList.get(0);
			List<RunCShiftTime> list=tremote.findTimeListByNoEr(model.getInitialNo(), model.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		}
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public RunCShiftInitial getModel() {
		return model;
	}

	public void setModel(RunCShiftInitial model) {
		this.model = model;
	}

	public RunCShift getShiftModel() {
		return shiftModel;
	}

	public void setShiftModel(RunCShift shiftModel) {
		this.shiftModel = shiftModel;
	}

	public RunCShiftTime getTimeModel() {
		return timeModel;
	}

	public void setTimeModel(RunCShiftTime timeModel) {
		this.timeModel = timeModel;
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
}
