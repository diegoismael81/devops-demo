package com.tcs.devops.challenge.gateway_service.config;

import com.tcs.devops.challenge.gateway_service.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private WebClient.Builder webClient;
    private final String apiKeyConfig;

    public AuthFilter(WebClient.Builder webClient, @Value("${tcs.api-key}") String apiKeyConfig) {
        super(Config.class);
        this.webClient = webClient;
        this.apiKeyConfig = apiKeyConfig;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            if (!exchange.getRequest().getHeaders().containsKey("X-Parse-REST-API-Key")) {
                return onError(exchange, HttpStatus.BAD_REQUEST, "Missing X-Parse-REST-API-Key header");
            }

            String apiKeyHeader = exchange.getRequest().getHeaders().get("X-Parse-REST-API-Key").get(0);
            if (!apiKeyHeader.equals(apiKeyConfig)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Invalid X-Parse-REST-API-Key");
            }

            if (!exchange.getRequest().getHeaders().containsKey("X-JWT-KWY")) {
                return onError(exchange, HttpStatus.BAD_REQUEST, "Missing X-JWT-KWY header");
            }

            String jwtToken = exchange.getRequest().getHeaders().get("X-JWT-KWY").get(0);

            return webClient.build()
                    .post()
                    .uri("http://auth-service/auth/validate?token=" + jwtToken)
                    .retrieve()
                    .bodyToMono(ResponseDTO.class)
                    .flatMap(response -> {
                        if (response.isStatus()) {
                            return chain.filter(exchange);
                        } else {
                            return onError(exchange, HttpStatus.UNAUTHORIZED, response.getMessage());
                        }
                    })
                    .onErrorResume(e -> onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Error on validating token " + e.getMessage()));
        }));
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String message){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {}
}