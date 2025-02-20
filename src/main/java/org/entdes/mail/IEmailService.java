package org.entdes.mail;

public interface IEmailService {
    void enviarCorreu(String destinatari, String assumpte, String cos) throws Exception;
}
