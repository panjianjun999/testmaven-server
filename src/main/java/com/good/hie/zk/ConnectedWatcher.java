package com.good.hie.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
/**
 * 等待连接zookeeper的监听类
 * @author Pan
 *
 */
public class ConnectedWatcher implements Watcher {
    private CountDownLatch connectedLatch;

    ConnectedWatcher(CountDownLatch connectedLatch) {
        this.connectedLatch = connectedLatch;
    }

    public void process(WatchedEvent event) {
    	System.out.println("watcher.ConnectedWatcher" + ".process=" + "path:" + event.getPath()
    		+ ",type:" + event.getType() +",stat:" + event.getState());
    	
        if (event.getState() == KeeperState.SyncConnected) {
            connectedLatch.countDown();
        }
    }
}
