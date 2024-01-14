package com.example.ProjectService.AzureServices;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import org.springframework.stereotype.Service;

@Service
public class KeyVaultService
{

    private final SecretClient secretClient;

    public KeyVaultService(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    public String getSecretValue(String secretName) {
        KeyVaultSecret keyVaultSecret = secretClient.getSecret(secretName);
        return keyVaultSecret.getValue();
    }
}