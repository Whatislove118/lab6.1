import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    static ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static void main(String...args) {
            try(DatagramChannel channel = DatagramChannel.open();DatagramSocket socket = channel.socket()){
                CommandReader reader = new CommandReader();
                SocketAddress address = new InetSocketAddress(8888);
                socket.bind(address);
                ByteBuffer buffer = ByteBuffer.allocate(65507);
                while(!socket.isClosed()){
                    SocketAddress client = channel.receive(buffer);
                    ExecuteAllMessages executeAllMessages = new ExecuteAllMessages(channel,socket,client,buffer,reader);
                    executorService.execute(executeAllMessages);
                    buffer.clear();
                }
            executorService.shutdown();
            }catch(IOException e){
                System.out.println("Ошибка создания пакета данных!");
            }
    }

}



