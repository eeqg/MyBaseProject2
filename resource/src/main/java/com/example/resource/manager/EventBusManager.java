package com.example.resource.manager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wp on 2018/6/28.
 */

public class EventBusManager {
	
	public static final int EVENT_KEY_BASE = 1000;
	public static final int EVENT_KEY_NETWORK_UNAVAILABLE = EVENT_KEY_BASE + 1;
	
	public static EventBus getDefaultBus() {
		return EventBus.getDefault();
	}
	
	public static void register(Object obj) {
		if (!getDefaultBus().isRegistered(obj)) {
			getDefaultBus().register(obj);
		}
	}
	
	public static void unregister(Object obj) {
		getDefaultBus().unregister(obj);
	}
	
	public static void post(int key) {
		post(key, null);
	}
	
	public static void post(int key, Object data) {
		getDefaultBus().post(new Event(key, data));
	}
	
	public static void postSticky(int key){
		postSticky(key, null);
	}
	
	public static void postSticky(int key, Object data) {
		getDefaultBus().postSticky(new Event(key, data));
	}
	
	public static class Event {
		public int key;
		public Object data;
		
		public Event(int key, Object data) {
			this.key = key;
			this.data = data;
		}
	}
}
