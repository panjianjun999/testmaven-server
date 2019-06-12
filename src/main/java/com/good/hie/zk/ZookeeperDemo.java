package com.good.hie.zk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZookeeperDemo {
	/** 服务地址，ip:port，多个地址用逗号隔开 */
    private static final String SERVER_ADDRESS = "10.6.8.98:2181,10.6.8.98:2182,10.6.8.98:2183";
    /** 超时时间，毫秒为单位 */
    private static final Integer TIMEOUT_MS = 3000;
    /** 节点名 */
    private static final String NODE_ZOO = "/pan";

    public static void main(String[] args) throws Exception {
    	//ZooKeeper实例
        ZooKeeper zk = new ZooKeeper(SERVER_ADDRESS, TIMEOUT_MS, new DemoWatcher(null,"root"));
        waitUntilConnected(zk);
        
        System.out.println("zookeeper ok!,root=" + NODE_ZOO);
        
        //持续观察的观察者
        DemoWatcher rootWatcher = new DemoWatcher(zk,"rootWatcher");
        
        //检查根目录
        Stat rootStat = zk.exists(NODE_ZOO, rootWatcher);
        if (null == rootStat) {//节点不存在
        	createNode(zk, NODE_ZOO, "i'm zk-root", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//PERSISTENT=持久化目录节点,EPHEMERAL=临时目录节点
        }
        
        //查看根目录
        getData(zk, NODE_ZOO, rootStat);
        
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		while(true){
			System.out.println("输入命令:<stop/test/other...>"); 
			try {
				String str = br.readLine().trim();
				System.out.println("后台指令:" + str);
				if(str.equals("stop")){
					break;
				}
				else if(str.startsWith("createNode-EPHEMERAL")){//增加临时节点:createNode-EPHEMERAL testep1
					String nodeName = str.split(" ")[1];
					createNode(zk, NODE_ZOO + "/" + nodeName, "i'm Node-" + nodeName, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//PERSISTENT=持久化目录节点,EPHEMERAL=临时目录节点
				}
				else if(str.startsWith("setRootDate")){//测试更新root节点内容:setRootDate ddd1
					String data = str.split(" ")[1];
					updateNode(zk, NODE_ZOO, data, -1);
				}
				else{
					System.out.println("不接受此命令: " + str);
				}
			} catch (Exception e) {
				System.out.println("执行命令失败:遇到致命错误:");
				e.printStackTrace();
			}
		}
		
		System.exit(0);
    }

    /**
     * 根据指定节点名字，创建节点
     * @param zk               zk客户端
     * @param node_zoo         节点名
     * @param data             节点信息
     * @param ACL_LIST         访问控制列表，这里使用了完全开放的ACL，允许任何客户端对znode进行读写
     * @param createMode       创建znode的类型：有两种类型的znode：短暂的和持久的。
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void createNode(ZooKeeper zk, String node_zoo, String data,List<ACL> ACL_LIST,CreateMode createMode)
            throws KeeperException, InterruptedException {
        String createResult = zk.create(node_zoo, data.getBytes(), ACL_LIST ,createMode);
        System.out.println("createResult=" + createResult);
    }

    /**
     * 删除节点
     * @param zk               zk客户端
     * @param node_zoo         节点名
     * @param version          指定的版本，-1/无视版本
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void deleteNode(ZooKeeper zk, String node_zoo, Integer version)
            throws KeeperException, InterruptedException {
        zk.delete(node_zoo, version);
    }

    /**
     * 更新节点
     * @param zk               zk客户端
     * @param node_zoo         节点名
     * @param data             节点数据
     * @param version          指定的版本，-1/无视版本
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void updateNode(ZooKeeper zk, String node_zoo, String data, Integer version)
            throws KeeperException, InterruptedException {
        zk.setData(node_zoo, data.getBytes(), version);
    }

    /**
     * 根据节点名获取节点信息
     * @param zk               zk客户端
     * @param node_zoo         节点名
     * @param stat             节点状态
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void getData(ZooKeeper zk, String node_zoo, Stat stat) throws KeeperException, InterruptedException {
        byte[] bs = zk.getData(node_zoo, true, stat);
        System.out.println("getData=" + new String(bs) + ",stat.getVersion()=" + stat.getVersion());
    }

    /**
     * 防止java程序未连上zookeeper的服务器而往下执行，需等待zk的状态码
     * @param zooKeeper
     */
    public static void waitUntilConnected(ZooKeeper zooKeeper) {
        CountDownLatch connectedLatch = new CountDownLatch(1);
//        Watcher watcher = new ConnectedWatcher(connectedLatch);
//        zooKeeper.register(watcher);
        if (States.CONNECTING == zooKeeper.getState()) {
            try {
                connectedLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
