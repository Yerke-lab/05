import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private final String serverAddress = "127.0.0.1";
    private final int serverPort = 9090;

    public Client() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключение к серверу: " + socket);

            new Thread(new ServerListener(socket)).start();

            while (true) {
                String message = scanner.nextLine();
                writer.println(message); // Отправка сообщения на сервер
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerListener implements Runnable {
        private BufferedReader reader;

        public ServerListener(Socket socket) {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Новое сообщение от сервера: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}