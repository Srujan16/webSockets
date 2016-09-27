import javax.websocket.Session;

/**
 * Created by srujant on 26/9/16.
 */
public interface SessionManager {
     void addSession(Session session, String type);
     void broadCastMessage(String message,String type);
     void removeSession(String id);
}
