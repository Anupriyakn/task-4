import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Chat Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection: " + clientSocket);

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clients.add(writer);

                Thread clientHandler = new Thread(new ClientHandler(clientSocket, writer));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket, PrintWriter writer) {
            this.clientSocket = clientSocket;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(clientSocket.getInputStream());
                while (true) {
                    String message = scanner.nextLine();
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcast(String message) {
        for (PrintWriter client : clients) {
            try {
                client.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
