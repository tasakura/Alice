package com.badlogic.androidgames.framework;

import java.util.ArrayList;
import java.util.List;


public class Pool<T> {	//ジェネリック型　＝　どの型のオブジェクトでも格納できる　
	
	public interface PoolObjectFactory<T> {		//インターフェイス
		public T createObject();
	}
	
	private final List<T> freeObjects;				//プールを管理する
	private final PoolObjectFactory<T> factory;		//プールで管理される型の新しいインスタンスを作成するために使う
	private final int maxSize;						//インスタンスに格納できるオブジェクトの最大数を追跡する
	
	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	public T newObject() {
		T object = null;
		
		if(freeObjects.size() == 0)
			object = factory.createObject();
		else
			object = freeObjects.remove(freeObjects.size()-1);	//オブジェクトの再利用
		
		return object;
	}
	
	public void free(T object) {
		if(freeObjects.size() < maxSize)
			freeObjects.add(object);
	}
	
}
