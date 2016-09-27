import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by srujant on 26/9/16.
 */
public class SessionManagerImpl implements SessionManager {

    private static final ConcurrentHashMap<String, RemoteEndpoint.Basic> userSessions = new ConcurrentHashMap<>();

    static {

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        if (userSessions.size() != 0) {
                            String message;
                            Map.Entry<String, RemoteEndpoint.Basic> entry = userSessions.entrySet().iterator().next();
                            RemoteEndpoint.Basic user = entry.getValue();
                            FileReader fileReader = new FileReader(new File("/home/srujant/Desktop/java/websocket"));
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            while ((message = bufferedReader.readLine()) != null) {
                                System.out.println("Size :"+message.length());
                                user.sendText(message, false);
                                Thread.sleep(1000);
                            }
                            user.sendText("EndOfFile", true);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Exception while reading data");
                } catch (InterruptedException e) {
                    throw new RuntimeException("Exception in thread sleep");
                }
            }
        });
        t.start();
    }

    @Override
    public void addSession(Session session, String type) {
        RemoteEndpoint.Basic user = session.getBasicRemote();
        userSessions.put(session.getId(), user);
        System.out.println(session.getId() + " Connected to network");
        try {
            if ("text".equalsIgnoreCase(type)) {
                user.sendText("Connection Established");
            } else {
                user.sendBinary(ByteBuffer.wrap("Connection Established".getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadCastMessage(String message, String type) {
        int opcode;
        if ("text".equalsIgnoreCase(type)) {
            opcode = 1;
        } else {
            opcode = 2;
        }

        for (Map.Entry<String, RemoteEndpoint.Basic> user : userSessions.entrySet()) {
            try {
                if (opcode == 1) {
                    user.getValue().sendText("from " + user.getKey() + " Message " + message);
                } else {
                    message = "from " + user.getKey() + " Message " + message;
                    user.getValue().sendBinary(ByteBuffer.wrap(message.getBytes()));
                }
            } catch (IOException e) {
                throw new RuntimeException("failed to send data to " + user.toString());
            }
        }
    }

    @Override
    public void removeSession(String id) {
        userSessions.remove(id);
        System.out.println(id + " ended the session");
    }
}
