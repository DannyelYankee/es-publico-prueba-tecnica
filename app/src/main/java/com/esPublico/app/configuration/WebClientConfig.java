package com.esPublico.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl("https://kata-espublicotech.g3stiona.com/v1")
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
				.filter(errorHandlingFilter()).build();
	}

	private ExchangeFilterFunction errorHandlingFilter() {
		return ExchangeFilterFunction.ofResponseProcessor(response -> {
			if (response.statusCode().isError()) {
				return handleErrorResponse(response);
			}
			return Mono.just(response);
		});
	}

	private Mono<ClientResponse> handleErrorResponse(ClientResponse response) {
		return response.bodyToMono(String.class).flatMap(body -> {
			return Mono.error(new RuntimeException("Error during API call: " + response.statusCode() + " - " + body));
		});
	}

}
