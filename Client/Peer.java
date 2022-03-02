public class Peer {
    String ip;
    String port;

	public Peer (String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String display() {
        return ip + ":" + port;
    }
}