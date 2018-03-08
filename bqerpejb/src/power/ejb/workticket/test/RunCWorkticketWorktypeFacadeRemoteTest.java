package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketContentKey;
import power.ejb.workticket.RunCWorkticketContentKeyFacadeRemote;
import power.ejb.workticket.RunCWorkticketWorktype;
import power.ejb.workticket.RunCWorkticketWorktypeFacadeRemote;
public class RunCWorkticketWorktypeFacadeRemoteTest {
	protected static RunCWorkticketWorktypeFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketWorktypeFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketWorktypeFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException{
		RunCWorkticketWorktype model= new RunCWorkticketWorktype();
		model.setEnterpriseCode("hfdc");
		model.setworktypeName("648");
		model.setModifyBy("999999");
		RunCWorkticketWorktype entity= remote.save(model);
		System.out.print(entity.getworktypeId()); 
	}

	@Test
	public void testDelete() throws CodeRepeatException{
		remote.delete(Long.parseLong("2"));
	}

	@Test
	public void testDeleteMulti() {
		remote.deleteMulti("1,2");
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCWorkticketWorktype model= remote.findById(Long.valueOf(1));
		model.setEnterpriseCode("hfdc");
		model.setworktypeName("hr");
		model.setModifyBy("999999");
		RunCWorkticketWorktype entity=remote.update(model);
		System.out.print(entity.getworktypeName());
	}

	@Test
	public void testFindById() {
		RunCWorkticketWorktype model = remote.findById(Long.parseLong("1"));
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAll("hfdc","");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	@Test
	public void testFindByWorkticketTypeCode(){
		List list=remote.findByWorkticketTypeCode("hfdc", "A");
		System.out.print(list.size());
	}

}
