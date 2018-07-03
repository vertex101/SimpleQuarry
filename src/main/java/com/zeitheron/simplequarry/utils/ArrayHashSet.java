package com.zeitheron.simplequarry.utils;

import java.util.HashSet;

public class ArrayHashSet<E> extends HashSet<E>
{
	private E[] arr = null;
	
	public void updateArray()
	{
		arr = (E[]) toArray(new Object[0]);
	}
	
	public E[] asArray()
	{
		if(arr == null || arr.length != size())
			updateArray();
		return arr;
	}
}