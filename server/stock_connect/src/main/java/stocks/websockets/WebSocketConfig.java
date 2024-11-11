package stocks.websockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final stocks.websockets.MessageSockets messageSockets;

    public WebSocketConfig(stocks.websockets.MessageSockets messageSockets) {
        this.messageSockets = messageSockets;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageSockets, "/stocks").setAllowedOrigins("*");
    }
}