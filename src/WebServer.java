import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer {

    public static void main(String[] args) throws IOException {

        final int Port = 8886;
        try(ServerSocket serverSocket =new ServerSocket(Port)) {
            System.out.println("Server is Listening!" + serverSocket);
            while (true) {
                Socket clientS = serverSocket.accept();
                //System.out.println(clientS.getInputStream());
                Thread newThread = new Thread(new ClientHandler(clientS));
                newThread.start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


class ClientHandler implements Runnable {

    public Socket clientS;
    ClientHandler(Socket clientS) {
        this.clientS=clientS;
    }

    @Override
    public void run() {
        System.out.println("New thread started: " + Thread.currentThread().getName());
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
             PrintWriter out = new PrintWriter(clientS.getOutputStream(), true)){

//             Scanner scanner = new Scanner(System.in)) {
//            System.out.println("Enter the message : ");
//
//            String responseMessage = scanner.nextLine();

            //Read the HTTP request line
            String read = input.readLine();
            System.out.println("Request received: " + read);

            if (read != null) {
                String parts[] = read.split(" ");
                String method = parts[0];
                String path = parts[1];

                if (method.equals("GET")) {
                    String responseMessage = "HELLO WORLD!!";
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/plain");
                    out.println("Content-Length: " + responseMessage.length());
                    out.println();
                    out.println(responseMessage);
                } else {
                    out.println("HTTP/1.1 501 Not Implemented");
                    out.println("Content-Type: text/plain");
                    out.println();
                    out.println("Unsupported HTTP method: " + method);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientS.close();
                System.out.println("Client disconnected " +  Thread.currentThread().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
