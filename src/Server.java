import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private static final Executor executor = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {


        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket cliente;

            while (true){
                cliente = serverSocket.accept();
                System.out.println("Cliente aceptado");
                executor.execute(new ClientHandler(cliente));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}