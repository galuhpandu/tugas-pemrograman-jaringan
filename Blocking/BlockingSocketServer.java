import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BlockingSocketServer {
    private static final int PORT = 8888;
    public static void main(String[] args) {

        try {
            // Membuat ServerSocket untuk menerima koneksi dari klien
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server terbuka dengan port " + PORT);

            // Loop tak terbatas untuk menerima koneksi dari klien
            while (true) {
                // Menerima koneksi dari klien
                Socket clientSocket = serverSocket.accept();
                System.out.println("Koneksi yang terhubung " + clientSocket.getInetAddress());

                // Menggunakan thread untuk menangani setiap koneksi klien secara terpisah
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menangani setiap koneksi klien
    private static void handleClient(Socket clientSocket) {
        try {
            // Input stream untuk menerima data dari klien
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Output stream untuk mengirim data ke klien
            OutputStream outputStream = clientSocket.getOutputStream();

            // Menerima nama dari klien
            String clientName = reader.readLine();
            System.out.println("Nama Client : " + clientName);

            // Scanner untuk membaca input dari server
            Scanner scanner = new Scanner(System.in);

            // Loop tak terbatas untuk menerima dan mengirim pesan dengan klien tertentu
            while (true) {
                // Menerima pesan dari klien
                String receivedMessage = reader.readLine();
                System.out.println("Pesan dari " + clientName + ": " + receivedMessage);

                // Mengirim balasan ke klien
                System.out.print("Kirim pesan ke " + clientName + " : ");
                String response = scanner.nextLine();
                outputStream.write(response.getBytes());
                outputStream.write('\n'); // Menambahkan newline untuk memisahkan pesan

                // Break dari loop jika klien mengirim 'exit'
                if (receivedMessage.equalsIgnoreCase("exit") || response.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            // Menutup koneksi untuk klien tertentu
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
