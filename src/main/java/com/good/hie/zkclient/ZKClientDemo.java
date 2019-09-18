package com.good.hie.zkclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * 使用ZkClient操作zookeeper
 * @author Pan
 *
 */
public class ZKClientDemo {
	/** 服务地址，ip:port，多个地址用逗号隔开 */
//    private static final String SERVER_ADDRESS = "10.6.8.98:2181";
    private static final String SERVER_ADDRESS = "10.6.8.98:2181,10.6.8.98:2182,10.6.8.98:2183";
    /** 超时时间，毫秒为单位 */
    private static final Integer TIMEOUT_MS = 30*1000;
    /** 节点名 */
    private static final String NODE_ZOO = "/pan";

    public static void main(String[] args) {
        //创建会话
        ZkClient zkClient = new ZkClient(SERVER_ADDRESS, TIMEOUT_MS,TIMEOUT_MS,new CustomSerializer());

        //创建节点
        //zkClient.createPersistent(path);
        //zkClient.createEphemeral(path);
        if(zkClient.exists(NODE_ZOO) == false){//节点不存在
        	System.out.println("节点不存在,创建:" + NODE_ZOO);
        	zkClient.create(NODE_ZOO, "i'm zk-root", CreateMode.PERSISTENT);
        }

        //查看数据
        getData(zkClient,NODE_ZOO);
        
        //监听节点  subscribeChildChanges 监听当前节点以及子节点增加或者删除
        zkClient.subscribeChildChanges(NODE_ZOO, new IZkChildListener() {
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("handleChildChange.路径："+s);
                System.out.println("变更的节点为:"+list);
            }
        });
        
        //监听节点  subscribeDataChanges 监听当前节点以及子节点内容的变更,必须用指定CustomSerializer的方式连接,否则无法监听到数据改变
        zkClient.subscribeDataChanges(NODE_ZOO, new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("handleDataChange.路径："+s);
                System.out.println("变更的内容为:"+o.toString());
            }

            public void handleDataDeleted(String s) throws Exception {
                System.out.println("handleDataDeleted.路径："+s);
            }
        });

        //修改节点数据
//        zkClient.writeData(path, "world");
//        System.out.println(zkClient.readData(path));

        //删除节点
//        zkClient.delete(path);
        //zkClient.delete(path, 1);//删除指定版本号的节点
        //zkClient.deleteRecursive(path);//级联删除所有子节点

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
					zkClient.create(NODE_ZOO + "/" + nodeName, "i'm Node-" + nodeName, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//PERSISTENT=持久化目录节点,EPHEMERAL=临时目录节点
				}
				else if(str.startsWith("setRootDate")){//测试更新root节点内容:setRootDate ddd1
					String data = str.split(" ")[1];
					zkClient.writeData(NODE_ZOO, data, -1);
				}
				else{
					System.out.println("不接受此命令: " + str);
				}
			} catch (Exception e) {
				System.out.println("执行命令失败:遇到致命错误:");
				e.printStackTrace();
			}
		}
		
		//关闭客户端连接
		if (zkClient != null) {
			zkClient.close();
		}
		System.exit(0);
    }

	private static void getData(ZkClient zkClient,String nodeZoo) {
		//读取节点的数据
//		System.out.println("我的数据:" + zkClient.readData(NODE_ZOO));
		
		//列出根下所有节点
        System.out.println("我的子节点:" + zkClient.getChildren(nodeZoo).toString());
	}
}
