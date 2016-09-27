

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;


/**
 * Created by srujant on 22/9/16.
 */


@ServerEndpoint("/echoServer_Text")
public class DemoSocketServerText{

    private static final SessionManager sessionManager = new SessionManagerImpl();

    @OnOpen
    public void onOpen(Session session) {
        sessionManager.addSession(session,"text");
    }

    @OnMessage
    public void textMessage(String message, Session session){
        System.out.println("Message from " + session.getId() + " : " + message);
        sessionManager.broadCastMessage(message,"text");
    }

    @OnMessage
    public  void binaryMessage(ByteBuffer byteBuffer,Session session){
        String message = new String(byteBuffer.array());
        System.out.println("Binary Message from " + session.getId() + " : " +message );
        sessionManager.broadCastMessage(message,"byte");
    }

    @OnClose
    public void onClose(Session session) {
        sessionManager.removeSession(session.getId());
    }

}
