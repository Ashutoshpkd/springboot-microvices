package com.aip.products.controller;

import com.aip.products.exception.ProductServerException;
import com.aip.products.repository.ProductReactiveRepository;
import com.aip.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private ProductReactiveRepository repository;

    private static final String PRODUCT_URL = "/api/products";

    @Test
    void getProductTest() {
        when(repository.findAll()).thenThrow();

        webTestClient.get()
                .uri(PRODUCT_URL)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .consumeWith(message -> {
                    assert message.getResponseBody().equals("Something went wrong: Failure handle test");
                });
    }
}
