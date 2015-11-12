package com.trang.uima.langrid;

import jp.go.nict.langrid.commons.util.Holder;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

public class ComponentServiceFactoryHolder {
	public static ComponentServiceFactory get(){
		return instance.get();
	}

	public static void set(ComponentServiceFactory instance){
		ComponentServiceFactoryHolder.instance.set(instance);
	}

	public static void remove(){
//		instance.remove();
	}

//	private static ThreadLocal<ComponentServiceFactory> instance = new ThreadLocal<>();
	private static Holder<ComponentServiceFactory> instance = new Holder<ComponentServiceFactory>();
}
