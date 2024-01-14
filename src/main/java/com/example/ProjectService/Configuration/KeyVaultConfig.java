package com.example.ProjectService.Configuration;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Configuration
public class KeyVaultConfig {
   /* @Bean
    public SecretClient secretClient() {
        return new SecretClientBuilder()
                .vaultUrl("https://semeter6kluis.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }*/


    @Value("${azure.keyvault.client-id}")
    private String base64ClientId;

    @Value("${azure.keyvault.client-secret}")
    private String base64ClientSecret;

    @Value("${azure.keyvault.tenant-id}")
    private String base64TenantId;

    @Bean
    public SecretClient secretClient() {
        String clientId = decodeBase64(base64ClientId);
        String clientSecret = decodeBase64(base64ClientSecret);
        String tenantId = decodeBase64(base64TenantId);

        return new SecretClientBuilder()
                .vaultUrl("https://semeter6kluis.vault.azure.net/")
                .credential(new ClientSecretCredentialBuilder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .tenantId(tenantId)
                        .build())
                .buildClient();
    }

    private String decodeBase64(String base64Value)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Value);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
