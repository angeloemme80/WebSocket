package it.angelomassaro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/*
 * Endpoint di websocket
 * Salva tutte le sessione utenti iche si collegano in un HashMap e appena arriva un messaggio con un parametro specifico, 
 * invia un messaggio a tutte le sessioni connesse in quel momento
 */

@ServerEndpoint("/endpoint")
public class MyWebSocket {
    
	//mettendola static, assumerà lo stesso valore in tutte le Socket e quindi conserverà le sessioni
	private static HashMap<String,Session> socketSessions = new HashMap<String,Session>();
    
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());
        this.socketSessions.put(session.getId(), session);
        System.out.println("Aggiunta sessione: " + session.toString());
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
        socketSessions.remove(session.getId());
        System.out.println("Rimossa sessione: " + session.toString());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message + " - socketSessions: " + socketSessions.size());
        
        try {
        	//Invia un messaggio di risposta Hello Client al client che ha appena inviato un messaggio
            //session.getBasicRemote().sendText("Hello Client " + session.getId() + "!");
            
        	//Leggo la url del messaggio, se è presente una chiave "atutti" che contiene il valore "nrchiamata" allora manda il messaggio a tutte le sessioni connesse al momento
        	Map<String, List<String>> mappaParametro = session.getRequestParameterMap();
        	if(mappaParametro.containsKey("atutti")) {
        		List<String> listaValori = mappaParametro.get("atutti");
        		if (listaValori.contains("nrchiamata")) {
		        	//Manda un messaggio a tutti i client che hanno la sessione aperta
		            for (Map.Entry<String, Session> entry : socketSessions.entrySet()) {
		                if (entry.getValue().isOpen())
		                	entry.getValue().getBasicRemote().sendText("Questo è per tutti");
		            }
        		}
        	}
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
    
    
}