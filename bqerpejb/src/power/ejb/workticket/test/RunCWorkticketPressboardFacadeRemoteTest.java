package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketPressboard;
import power.ejb.workticket.RunCWorkticketPressboardFacadeRemote;

public class RunCWorkticketPressboardFacadeRemoteTest {
	protected static RunCWorkticketPressboardFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorkticketPressboardFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketPressboardFacade");
	}

	@Test
	public void testSave() throws Exception{
		try{
			RunCWorkticketPressboard entity=new RunCWorkticketPressboard();
			entity.setEnterpriseCode("hfdc");
			entity.setModifyBy("999999");
			entity.setOrderBy(Long.valueOf(1));
			entity.setParentPressboardId(Long.valueOf(0));
			entity.setPressboardCode("123");
			entity.setPressboardName("压板1");
			System.out.print(remote.save(entity).getParentPressboardId());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Test
	public void testDelete() throws Exception{
		try{
			RunCWorkticketPressboard entity=remote.findById(Long.valueOf(1));
			remote.delete(entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Test
	public void testUpdate() throws Exception{
		try{
			RunCWorkticketPressboard entity=remote.findById(Long.valueOf(1));
			entity.setPressboardName("345");
			System.out.print(remote.update(entity).getPressboardName());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	@Test
	public void testFindParentPressboardId() {
		List list=remote.findByParentPressboardId(Long.valueOf(Long.valueOf(0)));
		System.out.print(list.size());
	}

}
