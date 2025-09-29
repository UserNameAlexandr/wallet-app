package com.wallet.wallet_app.conrtoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.wallet_app.WalletAppApplication;
import com.wallet.wallet_app.dto.WalletDTO;
import com.wallet.wallet_app.entity.Wallet;
import com.wallet.wallet_app.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WalletAppApplication.class)
@AutoConfigureWebMvc
public class WalletControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WalletRepository walletRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    private WalletDTO createDepositRequest(UUID walletId, BigDecimal amount) {
        WalletDTO request = new WalletDTO();
        request.setWalletId(walletId);
        request.setOperationType(WalletDTO.OperationType.DEPOSIT);
        request.setAmount(amount);
        return request;
    }

    @Test
    @Transactional
    void testDeposit() throws Exception {
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new Wallet(walletId, BigDecimal.ZERO));

        WalletDTO request = createDepositRequest(walletId, BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    @DirtiesContext
    void testConcurrentOperations() throws Exception {
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new Wallet(walletId, BigDecimal.ZERO));
        int numThreads = 10;
        int numOperations = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numOperations; i++) {
            executor.submit(() -> {
                try {
                    WalletDTO request = createDepositRequest(walletId, BigDecimal.ONE);
                    mockMvc.perform(post("/api/v1/wallet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                            .andExpect(status().isOk());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);
        assertTrue(finished, "Operations did not complete in time");
        assertEquals(numOperations, successCount.get(), "Not all operations succeeded");
        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(numOperations));
    }
}
