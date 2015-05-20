package org.xobo.coke.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.ProxyObject;

import org.xobo.coke.dataType.ListMap;
import org.xobo.coke.model.BaseModel;
import org.xobo.coke.service.PersistAction;
import org.xobo.coke.service.impl.NopPersistAction;

public class PersistWrapper {
	private ListMap<Class<?>, ReferenceWrapper> referenceWrapperMap = ListMap.concurrentHashMap();
	private Map<Class<?>, PersistAction<?>> persistActionMap = new HashMap<Class<?>, PersistAction<?>>();

	public PersistWrapper addReferenceWrapper(Class<?> parentClazz, ReferenceWrapper referenceWrapper) {
		referenceWrapperMap.add(parentClazz, referenceWrapper);
		return this;
	}

	public PersistWrapper addReferenceWrapper(Class<?> parentClazz, String childrenProperty, Class<?> childClazz) {
		referenceWrapperMap.add(parentClazz, new ReferenceWrapper(childrenProperty, childClazz));
		return this;
	}

	public Collection<ReferenceWrapper> getReferenceWrappers(Class<?> clazz) {
		return referenceWrapperMap.getValue(clazz);
	}

	public Collection<ReferenceWrapper> getPropertyWrappers(BaseModel<?> baseModel) {
		return getReferenceWrappers(getOrginalClass(baseModel));
	}

	private Class<?> getOrginalClass(Object entity) {
		return entity instanceof ProxyObject ? entity.getClass().getSuperclass() : entity.getClass();
	}

	public ListMap<Class<?>, ReferenceWrapper> getReferenceWrapperMap() {
		return referenceWrapperMap;
	}

	public void setReferenceWrapperMap(ListMap<Class<?>, ReferenceWrapper> property) {
		referenceWrapperMap = property;
	}

	public Map<Class<?>, PersistAction<?>> getPersistActionMap() {
		return persistActionMap;
	}

	public void setPersistActionMap(Map<Class<?>, PersistAction<?>> persistActionMap) {
		this.persistActionMap = persistActionMap;
	}

	public void addPersistAction(Class<?> clazz, PersistAction<?> persistAction) {
		persistActionMap.put(clazz, persistAction);
	}

	public PersistAction<?> getPersistAction(BaseModel<?> baseModel) {
		return getPersistAction(getOrginalClass(baseModel));
	}

	public PersistAction<?> getPersistAction(Class<?> clazz) {
		PersistAction<?> persistAction = persistActionMap.get(clazz);
		return persistAction != null ? persistAction : new NopPersistAction();
	}

}
