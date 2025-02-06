import java.io.*;
import java.net.*;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        int udpPort = 8080; // Known UDP port number of the server

        try {
            // Step 1: UDP Discovery
            DatagramSocket udpSocket = new DatagramSocket();
            udpSocket.setBroadcast(true);

            String discoveryMessage = "CCS DISCOVER";
            byte[] sendData = discoveryMessage.getBytes();
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, udpPort);

            System.out.println("Sending UDP broadcast to discover server...");
            udpSocket.send(sendPacket);

            // Receive the server's response
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            if (!response.equals("CSS FOUND")) {
                System.out.println("Unexpected response from server: " + response);
                return;
            }

            InetAddress serverAddress = receivePacket.getAddress();
            System.out.println("Server discovered at IP: " + serverAddress.getHostAddress());
            udpSocket.close();

            // Step 2: Establish TCP connection
            Socket tcpSocket = new Socket(serverAddress, udpPort);
            System.out.println("Connected to server via TCP at " + serverAddress.getHostAddress() + ":" + udpPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);

            // Step 3: Send random requests cyclically
            Random random = new Random();
            String[] operations = {"ADD", "SUB", "MUL", "DIV"};
            while (true) {
                int arg1 = random.nextInt(100);
                int arg2 = random.nextInt(100);
                String operation = operations[random.nextInt(operations.length)];

                // Create a request
                String request = operation + " " + arg1 + " " + arg2;
                System.out.println("Sending request: " + request);
                out.println(request);

                // Receive and print the response
                String responseFromServer = in.readLine();
                System.out.println("Response from server: " + responseFromServer);

                // Random interval before sending the next request
                Thread.sleep(random.nextInt(3000) + 1000); // 1 to 4 seconds
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout waiting for response.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
