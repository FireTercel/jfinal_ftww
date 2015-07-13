package com.ftww.basic.kits.tree;

import java.util.List;

/**
 * Created by wangrenhui on 14-4-12.
 */
public interface TreeNode<T> {
	
	public String getIds();
	
	public String getParentIds();
	
	public List<T> getChildren();
	
	public void setChildren(List<T> children);

}
