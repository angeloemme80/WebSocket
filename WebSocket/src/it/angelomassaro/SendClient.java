package it.angelomassaro;

import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;


/*
 * Client java che invia un messaggio alla websocket attraverso la classe WebsocketClientEndpoint
 */

public class SendClient {

	public static void main(String[] args) {
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://127.0.0.1:8080/WebSocket/endpoint?atutti=nrchiamata"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }

}
