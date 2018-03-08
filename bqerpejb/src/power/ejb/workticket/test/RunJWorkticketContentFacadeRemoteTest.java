package power.ejb.workticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.business.RunJWorkticketContent;
import power.ejb.workticket.business.RunJWorkticketContentFacadeRemote;
public class RunJWorkticketContentFacadeRemoteTest {
	protected static RunJWorkticketContentFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunJWorkticketContentFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJWorkticketContentFacade");
	}

	@Test
	public void testSave() {
		RunJWorkticketContent model = new RunJWorkticketContent();
		model.setEnterpriseCode("hfdc");
		model.setAttributeCode("1");
		model.setBackKeyDesc("关键词描述");
		model.setBackKeyId(Long.parseLong("1"));
		model.setCreateBy("xxx");
		model.setCreateDate(new java.util.Date());
		model.setEquName("jiqi");
		model.setFlagId(Long.parseLong("1"));
		model.setFrontKeyDesc("前");
		model.setFlagId(Long.parseLong("2"));
		model.setIsreturn("Y");
		model.setLine(Long.parseLong("5"));
		model.setLocationId(Long.parseLong("6"));
		model.setLocationName("ooooo");
		model.setWorkticketNo("S4200811D0001");
		model.setWorktypeName("iiii");
		remote.save(model);	
	}

	@Test
	public void testDelete() {
		Long id = 5l;
		remote.delete(id);
	}

	@Test
	public void testDeleteMulti() {
		String ids = "1,2";
		remote.deleteMulti(ids);
	}

	@Test
	public void testUpdate() {
		RunJWorkticketContent model = remote.findById(1l);
		model.setIsUse("Y");
	}

	@Test
	public void testFindById() {
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAll("hfdc", "S1200811D0001");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}
