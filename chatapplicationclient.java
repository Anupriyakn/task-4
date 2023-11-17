import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Connected to the server");

            Scanner serverInput = new Scanner(socket.getInputStream());
            PrintWriter clientOutput = new PrintWriter(socket.getOutputStream(), true);

            Scanner userInput = new Scanner(System.in);

            Thread serverListener = new Thread(() -> {
                while (serverInput.hasNextLine()) {
                    System.out.println(serverInput.nextLine());
                }
            });
            serverListener.start();

            while (true) {
                String message = userInput.nextLine();
                clientOutput.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
