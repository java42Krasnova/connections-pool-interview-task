package connections.service;

import java.util.HashMap;

import connections.dto.Connection;

public class ConnectionPoolImpl implements ConnectionsPool {
	private static class Node {
		Connection connection;
		Node prev;
		Node next;

		public Node(Connection connection) {
			this.connection = connection;
		}
	}

	private static class ConnectionsList {
		Node head = null;
		Node tail = null;

		Node add(Connection connection) {
			Node newNode = new Node(connection);
			if (head == null) {
				head = tail = newNode;
			} else {
				addNewConnection(newNode);
			}
			return newNode;
		}

		private void addNewConnection(Node newNode) {
			newNode.prev = null;
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
		}

		public void changeLastActiveConnection(Node currentActiveConnectionNode) {
			if (currentActiveConnectionNode != tail) {
				removeConnectionNodeFromeCenter(currentActiveConnectionNode);
			} else {
				removeLongerUnusedConnectionFromList();
			}
			addNewConnection(currentActiveConnectionNode);

		}

		private void removeLongerUnusedConnectionFromList() {
			tail = tail.prev;
			tail.next = null;
		}

		private void removeConnectionNodeFromeCenter(Node currentActiveConnectionNode) {
			Node beforReorder = currentActiveConnectionNode.prev;
			Node afterReorder = currentActiveConnectionNode.next;
			beforReorder.next = afterReorder;
			afterReorder.prev = beforReorder;
		}
	}

	ConnectionsList list = new ConnectionsList();
	HashMap<Integer, Node> mapConnections = new HashMap<>();
	int connectionsPoolLimit;

	public ConnectionPoolImpl(int limit) {
		connectionsPoolLimit = limit;
	}

	@Override
	public boolean addConnection(Connection connection) {
		int id = connection.getId();
		if (mapConnections.containsKey(id)) {
			return false;
		}
		if (mapConnections.size() == connectionsPoolLimit) {
			int idlongerUnusedConnectionNode = list.tail.connection.getId();
			list.removeLongerUnusedConnectionFromList();
			mapConnections.remove(idlongerUnusedConnectionNode);
		}
		mapConnections.put(id, list.add(connection));
		return true;
	}

	@Override
	public Connection getConnection(int id) {
		Node currentConnection = mapConnections.get(id);
		if (currentConnection == null) {
			return null;
		}
		if (currentConnection.prev != null) {
			list.changeLastActiveConnection(currentConnection);
		}
		return currentConnection.connection;
	}

}
