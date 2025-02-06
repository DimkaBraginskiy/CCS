public class CCS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar CCS.jar <port>");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
            return;
        }

        Statistics statistics = new Statistics();

        try {
            // Start the UDP server for service discovery
            UDPServer udpServer = new UDPServer(port);
            Thread udpThread = new Thread(udpServer);
            udpThread.start();

            // Start the UDP server for client communication
            TCPServer tcpServer = new TCPServer(port, statistics);
            Thread tcpThread = new Thread(tcpServer);
            tcpThread.start();

            System.out.println("Server is ready to receive discovery and TCP client requests...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
