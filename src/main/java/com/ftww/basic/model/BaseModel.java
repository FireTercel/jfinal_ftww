package com.ftww.basic.model;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ftww.basic.kits.SqlXmlKit;
import com.ftww.basic.kits.UtilsKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Model基础类
 * @author FireTercel
 *
 * @param <M>
 */
public abstract class BaseModel<M extends Model<M>> extends Model<M> {

	private static final long serialVersionUID = -4816622475317218590L;
	
	private static Logger log=Logger.getLogger(BaseModel.class);
	
	/**
	 * 获取SQL，固定SQL
	 * @param sqlId
	 * @return
	 */
	protected String getSql(String sqlId){
		return SqlXmlKit.getSql(sqlId);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,Object> param){
		return SqlXmlKit.getSql(sqlId, param);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param 查询参数
     * @param list 用于接收预处理的值
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,String> param,LinkedList<Object> list){
		return SqlXmlKit.getSql(sqlId, param, list);
	}
	
	
	/**
	 * 根据i18n参数查询获取哪个字段的值
	 * @param i18n
	 * @return
	 */
	protected String i18n(String i18n){
		return null;
	}
	
	/**
	 * 获取表映射对象
	 * @return
	 */
	protected Table getTable(){
		return TableMapping.me().getTable(getClass());
	}
	
	/**
	 * 获取主键值
	 * @return
	 */
	public String getPKValue(){
		return this.getStr(getTable().getPrimaryKey());
	}
	
	/**
	 * 重写save方法
	 */
	public boolean save(){
		//设置主键值
		this.set(getTable().getPrimaryKey(), UtilsKit.getUuidByJdk(true));
		// 是否需要乐观锁控制
		if(getTable().hasColumnLabel("version")){
			// 初始化乐观锁版本号
			this.set("version", Long.valueOf(0));
		}
		
		return super.save();
	}
	
	/**
	 * 重写update方法
	 */
	@SuppressWarnings("unchecked")
	public boolean update(){
		
		Table table=getTable();
		boolean hasVersion = table.hasColumnLabel("version");
		
		// 是否需要乐观锁控制，表是否有version字段
		if(hasVersion){
			String name=table.getName();
			String pk=table.getPrimaryKey();
			
			// 1.数据是否还存在
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("table", name);
			param.put("pk", pk);
			String sql= SqlXmlKit.getSql("basic.BaseModel.version", param);
			Model<M> modelOld = findFirst(sql , getPKValue());
			if(null == modelOld){ // 数据已经被删除
				throw new RuntimeException("数据库中此数据不存在，可能数据已经被删除，请刷新数据后在操作");
			}
			
			// 2.乐观锁控制
			Set<String> modifyFlag = null;
			try{
				Field field =  this.getClass().getSuperclass().getSuperclass().getDeclaredField("modifyFlag");
				//  由于属性modifyFlag 是private，所以要获得当前对象中当前Field的value就需要先进行该设置。
				field.setAccessible(true);
				Object object = field.get(this);
				if(null != object){
					modifyFlag = (Set<String>) object;
				}
				field.setAccessible(false);
			}catch (NoSuchFieldException | SecurityException e) {
				log.error("业务Model类必须继承BaseModel");
				e.printStackTrace();
				throw new RuntimeException("业务Model类必须继承BaseModel");
			}catch (IllegalArgumentException | IllegalAccessException e) {
				log.error("BaseModel访问modifyFlag异常");
				e.printStackTrace();
				throw new RuntimeException("BaseModel访问modifyFlag异常");
			}
			boolean versionModify = modifyFlag.contains("version"); // 表单是否包含version字段
			if(versionModify){
				Long versionDB = modelOld.getNumber("version").longValue(); // 数据库中的版本号
				Long versionForm = getNumber("version").longValue() + 1; // 表单中的版本号
				if(!(versionForm > versionDB)){
					throw new RuntimeException("表单数据版本号和数据库数据版本号不一致，可能数据已经被其他人修改，请重新编辑");
				}
			}
		}
		
		return super.update();
	}
	
	/**
	 * 针对Oracle做特殊处理
	 */
	public Date getDate(String attr){
		Object obj=this.get(attr);
		if(null == obj){
			return null;
		}
		
		return (Date) obj;
		
	}
	
	/**
	 * 添加或者更新缓存
	 * @param ids
	 */
	public abstract void cacheAdd(String ids);
	
	/**
	 * 删除缓存
	 * @param ids
	 */
	public abstract void cacheRemove(String ids);
	
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public abstract M cacheGet(String key);
	

}
