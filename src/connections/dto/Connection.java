package connections.dto;

public class Connection {
private int id;
private String ipAdress;
private int port;
public Connection(int id, String ipAdress, int port) {
	this.id = id;
	this.ipAdress = ipAdress;
	this.port = port;
}
public Connection()
{
}
public int getId() {
	return id;
}
public String getIpAdress() {
	return ipAdress;
}
public int getPort() {
	return port;
}

}
