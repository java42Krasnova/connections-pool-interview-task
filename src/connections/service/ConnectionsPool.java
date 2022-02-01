package connections.service;

import connections.dto.Connection;

public interface ConnectionsPool {
boolean addConnection(Connection connection);
Connection getConnection(int id);
public int[] getConnectionsIdList();
}
