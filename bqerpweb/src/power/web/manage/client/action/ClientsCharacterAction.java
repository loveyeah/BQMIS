package power.web.manage.client.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConCClientsCharacter;
import power.ejb.manage.client.ConCClientsCharacterFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class ClientsCharacterAction extends AbstractAction {
	
	private ConCClientsCharacterFacadeRemote characterRemote;
	private ConCClientsCharacter character;
	
	public ClientsCharacterAction() {
		characterRemote = (ConCClientsCharacterFacadeRemote)factory.getFacadeRemote("ConCClientsCharacterFacade");
	}
	
	/**
	 * 增加
	 */
	public void addCharacter()
	{
		character.setEnterpriseCode(employee.getEnterpriseCode());
		try{
			character = characterRemote.save(character);
			write("{success:true,id:'"+character.getCharacterId()+"',msg:'增加成功！'}");
		}catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:公司性质名称不能重复！'}");
		}
	}
	
	/**
	 * 修改
	 */
	public void updateCharacter()
	{
		ConCClientsCharacter model = characterRemote.findById(character.getCharacterId());
		model.setCharacterName(character.getCharacterName());
		model.setMemo(character.getMemo());
		try{
			characterRemote.update(model);
			write("{success:true,id:'"+character.getCharacterId()+"',msg:'修改成功！'}");
		}catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:公司性质名称不能重复！'}");
		}
	}
	
	/**
	 * 删除
	 */
	public void deleteCharacter()
	{
		String ids = request.getParameter("ids");
		characterRemote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 根据名称查询列表
	 */
	public void getClientsCharacterList() throws JSONException {
		//取得模糊查询条件
		String characterName = request.getParameter("fuzzytext");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = characterRemote.findAll(characterName, employee.getEnterpriseCode(), start,limit);
		} else {
			// 查询
			object = characterRemote.findAll(characterName, employee.getEnterpriseCode());
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public ConCClientsCharacter getCharacter() {
		return character;
	}

	public void setCharacter(ConCClientsCharacter character) {
		this.character = character;
	}

}
