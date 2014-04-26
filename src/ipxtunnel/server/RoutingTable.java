package ipxtunnel.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class RoutingTable
{
	private static HashMap<RealNodeAddress, ArrayList<FakeNodeAddress>> nodes = new HashMap<RealNodeAddress, ArrayList<FakeNodeAddress>>();
	
	public synchronized static void addNode(RealNodeAddress realNode)
	{
		ArrayList<FakeNodeAddress> newFakeNodes = new ArrayList<FakeNodeAddress>();
		nodes.put(realNode, newFakeNodes);
	}
	
	public synchronized static void addNode(RealNodeAddress realNode, FakeNodeAddress fakeNode)
	{
		ArrayList<FakeNodeAddress> newFakeNodes = new ArrayList<FakeNodeAddress>();
		if (nodes.containsKey(realNode))
		{
			newFakeNodes.add(fakeNode);
			newFakeNodes.addAll(nodes.get(realNode));
		}
		nodes.put(realNode, newFakeNodes);
	}
	
	public synchronized static boolean nodeExists(RealNodeAddress realNode)
	{
		return nodes.containsKey(realNode);
	}
	
	public synchronized static int fakeNodeCountFor(RealNodeAddress realNode)
	{
		int nodeCount = 0;
		if (nodeExists(realNode))
		{
			nodeCount = nodes.get(realNode).size();
		}
		return nodeCount;
	}
	
	public synchronized static RealNodeAddress[] getRealNodes()
	{
		return nodes.keySet().toArray(new RealNodeAddress[0]);
	}
	
	public synchronized static ArrayList<FakeNodeAddress> getFakeNodes(RealNodeAddress realNode)
	{
		return nodes.get(realNode);
	}
	
	public synchronized static RealNodeAddress getRealNodeForFakeNode(FakeNodeAddress fakeNode)
	{
		RealNodeAddress realNode = null;
		for (Entry<RealNodeAddress, ArrayList<FakeNodeAddress>> entry : nodes.entrySet())
		{
			if (entry.getValue().contains(fakeNode))
			{
				realNode = entry.getKey();
				break;
			}
		}
		
		return realNode;
	}

	public synchronized static FakeNodeAddress getFakeNodeForClientAndRealNode(RealNodeAddress realNode, InetAddress clientAddress)
	{
		FakeNodeAddress fakeNode = null;
		for(FakeNodeAddress fakeNodeCandidate : nodes.get(realNode))
		{
			if (fakeNodeCandidate.getClientAddress().equals(clientAddress))
			{
				fakeNode = fakeNodeCandidate;
				break;
			}
		}
		
		return fakeNode;
	}
	
	public synchronized static ArrayList<RealNodeAddress> getRealNodesForClient(InetAddress client)
	{
		ArrayList<RealNodeAddress> realNodes = new ArrayList<RealNodeAddress>();
		for (RealNodeAddress realNode : nodes.keySet())
		{
			if (realNode.getClientAddress().equals(client))
			{
				realNodes.add(realNode);
			}
		}
		
		return realNodes;
	}

	public synchronized static void removeClient(Client clientToRemove)
	{
		//TODO Fix this stuff
//		Set<Entry<RealNodeAddress, ArrayList<FakeNodeAddress>>> entries = 
//		for (RealNodeAddress node : nodes.keySet())
//		{
//			nodes.get(node)
//		}
	}


}
