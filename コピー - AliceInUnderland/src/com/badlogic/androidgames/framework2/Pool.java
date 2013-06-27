package com.badlogic.androidgames.framework2;

import java.util.ArrayList;
import java.util.List;


public class Pool<T> {	//�W�F�l���b�N�^�@���@�ǂ̌^�̃I�u�W�F�N�g�ł��i�[�ł���@
	
	public interface PoolObjectFactory<T> {		//�C���^�[�t�F�C�X
		public T createObject();
	}
	
	private final List<T> freeObjects;				//�v�[�����Ǘ�����
	private final PoolObjectFactory<T> factory;		//�v�[���ŊǗ������^�̐V�����C���X�^���X���쐬���邽�߂Ɏg��
	private final int maxSize;						//�C���X�^���X�Ɋi�[�ł���I�u�W�F�N�g�̍ő吔��ǐՂ���
	
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
			object = freeObjects.remove(freeObjects.size()-1);	//�I�u�W�F�N�g�̍ė��p
		
		return object;
	}
	
	public void free(T object) {
		if(freeObjects.size() < maxSize)
			freeObjects.add(object);
	}
	
}
