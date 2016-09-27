import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by srujant on 24/9/16.
 */
@ServerEndpoint("/echoServer_Binary")
public class DemoSocketServerBinary {

    private static final SessionManager sessionManager = new SessionManagerImpl();

    @OnOpen
    public void onOpen(Session session) {
        sessionManager.addSession(session,"binary");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Message from " + session.getId() + " : " + message);
        sessionManager.broadCastMessage(message,"text");
    }

    @OnMessage
    public  void binaryMessage(ByteBuffer byteBuffer, Session session){
        String message = new String(byteBuffer.array());
        System.out.println("Binary Message from " + session.getId() + " : " +message );
        sessionManager.broadCastMessage(message,"binary");
    }

    @OnClose
    public void onClose(Session session) {
        sessionManager.removeSession(session.getId());
    }
}
