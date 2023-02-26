import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

public class ClientHandler implements Runnable{
    Socket client;
    InputStream inputStream;
    BufferedReader bReader;
    ObjectOutputStream objectOutputStream;
    String message;
    FilesHandler filesHandler;
    private static final int PACKET_SIZE = 1024;

    public ClientHandler(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try {
            inputStream = client.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println("Leyendo mensaje...");
            while(true){
                message = bReader.readLine();
                System.out.println(message);

                if(message.equals("GET")){
                    sendFileNames();
                }else{
                    sendFile(message);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFileNames() throws Exception{
        filesHandler = new FilesHandler();
        String[] files = filesHandler.getFilesNames();
        objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        objectOutputStream.writeObject(files);
        System.out.println("Lista de archivos enviados.");
    }

    private void sendFile(String fileName) throws Exception{
        int port = 5000;
        filesHandler = new FilesHandler();
        File file = filesHandler.getFile(fileName);

        // Si el archivo es nulo no se encontró el archivo y se envía un array con un mensaje de error al cliente
        if (file == null) {
            String[] errorMessage = {"No se encontró el archivo"};
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(errorMessage);
            return;
        }

        int sequenceNumber = 0;
        byte[] fileData = Files.readAllBytes(file.toPath());
        int dataSize = fileData.length;
        int numPackets = (int) Math.ceil((double) dataSize / PACKET_SIZE);

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = client.getInetAddress();


        for (int i = 0; i < numPackets; i++) {
            int offset = i * PACKET_SIZE;
            int length = Math.min(PACKET_SIZE, dataSize - offset);

            byte[] packetData = new byte[PACKET_SIZE];
            System.arraycopy(fileData, offset, packetData, 0, length);

            // Agregar encabezado personalizado con el número de secuencia
            CustomHeader header = new CustomHeader(sequenceNumber);
            byte[] headerBytes = header.toBytes();
            byte[] datagram = new byte[headerBytes.length + length];

            System.arraycopy(headerBytes, 0, datagram, 0, headerBytes.length);
            System.arraycopy(packetData, 0, datagram, headerBytes.length, length);

            DatagramPacket packet = new DatagramPacket(datagram, datagram.length, address, port);
            socket.send(packet);

            sequenceNumber++;
        }

        System.out.println("Se envió el archivo");
    }



}
