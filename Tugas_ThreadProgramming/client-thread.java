import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner scanner = new Scanner(System.in);

        Thread readThread = new Thread(() -> {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Server: " + inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        readThread.start();

        String userInput;
        while ((userInput = scanner.nextLine()) != null) {
            out.println(userInput);
        }

        socket.close();
        in.close();
        out.close();
        scanner.close();
    }
}