import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements Runnable{
    private int port;
    private Statistics statistics;
    private final ExecutorService threadpool = Executors.newCachedThreadPool();

    public TCPServer(int port, Statistics statistics) {
        this.port = port;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);
            System.out.println("TCP Server started on Port: " + port);

            new Thread(() -> {
                while (true){
                    try {
                        Thread.sleep(10000);//10 seconds
                        statistics.printStatistics();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();



            while(true){
                Socket clientSocket = socket.accept();

                System.out.println("Client connected: " + clientSocket.getInetAddress());
                statistics.incrementClients();

                //threadpool.execute(() -> handleClient(clientSocket));
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket clientSocket){
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;

            while((request = reader.readLine()) != null){
                statistics.incrementRequests();
                System.out.println("Received request: " + request);

                String[] parts = request.split(" ");
                if (parts.length != 3) {
                    writer.println("ERROR");
                    statistics.incrementInvalidRequests();
                    continue;
                }

                String operation = parts[0];
                int arg1, arg2;
                try {
                    arg1 = Integer.parseInt(parts[1]);
                    arg2 = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    writer.println("ERROR");
                    statistics.incrementInvalidRequests();
                    continue;
                }

                int result;
                switch (operation) {
                    case "ADD" -> result = arg1 + arg2;
                    case "SUB" -> result = arg1 - arg2;
                    case "MUL" -> result = arg1 * arg2;
                    case "DIV" -> {
                        if (arg2 == 0) {
                            writer.println("ERROR");
                            statistics.incrementInvalidRequests();
                            continue;
                        }
                        result = arg1 / arg2;
                    }
                    default -> {
                        writer.println("ERROR");
                        statistics.incrementInvalidRequests();
                        continue;
                    }
                }

                writer.println(result);
                statistics.incrementOperationCount(operation);
                statistics.addToResultSum(result);
                System.out.println("Sent response: " + result);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }

    private String processRequest(String request){
        String[] parts = request.split(" ");
        if(parts.length != 3){
            return "WRONG FORMAT!";
        }

        String oper = parts[0];
        int arg1, arg2;
        try{
            arg1 = Integer.parseInt(parts[1]);
            arg2 = Integer.parseInt(parts[2]);
        }catch (NumberFormatException e){
            return "Wrong number format!";
        }


        switch (oper){
            case "ADD":
                return String.valueOf(arg1+arg2);
            case "SUB":
                return String.valueOf(arg1-arg2);
            case "MUL":
                return String.valueOf(arg1*arg2);
            case "DIV":
                if (arg2 != 0){
                    return String.valueOf(arg1/arg2);
                }else{
                    return "Error: Can not divide by 0.";
                }
            default:
                return "WRONG FORMAT!";
        }
    }
}
