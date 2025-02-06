import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer implements Runnable{
    private int port;

    public UDPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            System.out.println("Listening in port " + port);

            byte[] receivedData = new byte[1024];
            while(true){
                DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                if(message.startsWith("CCS DISCOVER")){
                    System.out.println("Received message: " + message);
                    String response = "CSS FOUND";
                    byte[] data = response.getBytes();

                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();

                    DatagramPacket sendPacket = new DatagramPacket(data, data.length, clientAddress, clientPort);
                    socket.send(sendPacket);
                    System.out.println("Sent: " + response);
                }

            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
