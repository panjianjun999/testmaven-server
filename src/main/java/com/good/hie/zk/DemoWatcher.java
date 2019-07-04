package com.good.hie.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * 通用监听类，接收来自zookeeper的回调
 * @author Pan
 *
 */
public class DemoWatcher implements Watcher {
	private ZooKeeper zk = null;
	private String watcherName = null;
	
	/**
	 * 
	 * @param zk ZooKeeper
	 * @param watcherName 观察者名称
	 */
	public DemoWatcher(ZooKeeper zk,String watcherName) {
		this.zk = zk;
		this.watcherName = watcherName;
	}
	
    public void process(WatchedEvent event) {
        System.out.println("watcher." + watcherName + ".process=" + "path:" + event.getPath()
        	+ ",type:" + event.getType() + ",stat:" + event.getState());
        
        try {
        	if(zk != null) {
        		System.out.println("\t event.getPath()=" + event.getPath());
        		System.out.println("\t event.getPath()=" + zk.getChildren(event.getPath(), true).toString());
        		
        		zk.exists(event.getPath(),true);//持续观察
        	}
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
