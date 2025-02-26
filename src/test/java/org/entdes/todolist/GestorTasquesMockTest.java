package org.entdes.todolist;

import static org.mockito.Mockito.verify;
import org.entdes.mail.IEmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GestorTasquesMockTest {

    @Mock
    private IEmailService emailService;

    @Test
    void testAfegirTascaEnviaCorreu() throws Exception {
        GestorTasques gestor = new GestorTasques(emailService, "test@example.com");
        gestor.afegirTasca("Comprar llet", null, null, null);
        verify(emailService).enviarCorreu("test@example.com", "Nova Tasca Creada", "Has creat la tasca: Comprar llet");
    }
}