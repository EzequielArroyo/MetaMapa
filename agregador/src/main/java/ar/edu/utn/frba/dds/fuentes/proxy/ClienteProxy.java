package ar.edu.utn.frba.dds.fuentes.proxy;

import ar.edu.utn.frba.dds.entities.dto.input.ProxyDto.ProxyResponse;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClienteProxy {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ClienteProxy.class);
    public ClienteProxy(@Value("${fuente.proxy.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public ProxyResponse buscarHechosPaginable(Integer page, Integer size, LocalDateTime fecha) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/hechos")
                    .queryParam("fechaModificacionDesde", fecha)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ProxyResponse>() {
                })
                .block();
        }catch (Exception ex) {
            logger.warn("Proxy no disponible, se omite importación", ex);
            return new ProxyResponse();
        }

    }
}
