import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ExecuteAllMessages implements  Runnable{
    private static DatagramChannel channel;
    private static DatagramSocket socket;
    private static SocketAddress client;
    private static ByteBuffer byteBuffer;
    private static CommandReader reader;

    public ExecuteAllMessages(DatagramChannel ch,DatagramSocket sk,SocketAddress cl,ByteBuffer b,CommandReader read){
        channel = ch;
        socket = sk;
        client = cl;
        byteBuffer= b;
        reader = read;
    }
    @Override
    public void run() {
        try {
                Messages messages = Serializator.deserialization(byteBuffer.array());
                System.out.println(messages.getCommand());
                if (messages.getCommand().equals("exit")) {
                    throw new IOException();
                }
                reader.read(messages);
                Messages feedBack = new Messages(messages.getCommand());
                feedBack.setAnswer(messages.getAnswer());
                byteBuffer.clear();
                byte[] outputAnswer = Serializator.serialization(feedBack).toByteArray();
                ByteBuffer bufferOut = ByteBuffer.allocate(65507);
                bufferOut.put(outputAnswer);
                bufferOut.flip();
                channel.send(bufferOut, client);
                System.out.println("Ответ был послан клиенту под именем ");
                bufferOut.clear();
        }catch (IOException e){
            System.out.println("Пользователь отключился от сервера");
        }
    }
}
