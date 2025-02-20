package org.entdes.todolist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.entdes.mail.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GestorTasques {
    
    private List<Tasca> llista = new ArrayList<>();
    private final IEmailService emailService;
    
    private String destinatari;

    public GestorTasques() {
        this.emailService = null;
    }

    
    @Autowired
    public GestorTasques(IEmailService emailService, @Value("${spring.mail.recipient}") String destinatari) {
        this.emailService = emailService;
        this.destinatari = destinatari;
    }

    public int afegirTasca(String descripcio, LocalDate dataInici, LocalDate dataFiPrevista, Integer prioritat)
            throws Exception {
        if (descripcio == null || descripcio.trim().isEmpty()) {
            throw new Exception("La descripció no pot estar buida.");
        }

        validarSiExisteixTasca(descripcio);

        if (dataInici != null && dataFiPrevista != null && dataInici.isAfter(dataFiPrevista)) {
            throw new Exception("La data d'inici no pot ser posterior a la data fi prevista.");
        }
        if (dataInici != null && dataInici.isBefore(LocalDate.now())) {
            throw new Exception("La data d'inici no pot ser anterior a la data actual.");
        }

        Tasca novaTasca = new Tasca(descripcio);
        novaTasca.setDataInici(dataInici);
        novaTasca.setDataFiPrevista(dataFiPrevista);
        novaTasca.setPrioritat(prioritat);
        llista.add(novaTasca);

        // Enviar notificació per correu
        if(emailService != null){
            emailService.enviarCorreu(this.destinatari, "Nova Tasca Creada", "Has creat la tasca: " + descripcio);
        }

        return novaTasca.getId();
    }

    private void validarSiExisteixTasca(int id, String descripcio) throws Exception {

        for (Tasca tasca : llista) {
            if (tasca.getDescripcio().equalsIgnoreCase(descripcio)) {
                if (tasca.getId() != id)
                    throw new Exception("Ja existeix una tasca amb la mateixa descripció");
            }
        }
    }

    private void validarSiExisteixTasca(String descripcio) throws Exception {
        validarSiExisteixTasca(0, descripcio);
    }

    public void eliminarTasca(int id) throws Exception {
        boolean esborrat = llista.removeIf(tasca -> tasca.getId() == id);
        if (!esborrat)
            throw new Exception("La tasca no existeix");
    }

    public void marcarCompletada(int id) throws Exception {
        Tasca tascaModificada = null;
        for (Tasca tasca : llista) {
            if (tasca.getId() == id) {
                tasca.setCompletada(true);
                tascaModificada = tasca;
                break;
            }
        }
        if (tascaModificada == null)
            throw new Exception("La tasca no existeix");
    }

    public void modificarTasca(int id, String novaDescripcio, Boolean completada, LocalDate dataInici,
            LocalDate dataFiPrevista, Integer prioritat) throws Exception {
        if (novaDescripcio == null || novaDescripcio.trim().isEmpty()) {
            throw new Exception("La descripció no pot estar buida.");
        }

        validarSiExisteixTasca(id, novaDescripcio);

        if (dataInici != null && dataFiPrevista != null && dataInici.isAfter(dataFiPrevista)) {
            throw new Exception("La data d'inici no pot ser posterior a la data fi prevista.");
        }

        if (prioritat != null && (prioritat < 1 || prioritat > 5)) {
            throw new Exception("La prioritat ha de ser un valor entre 1 i 5");
        }

        Tasca tascaModificada = null;
        for (Tasca tasca : llista) {
            if (tasca.getId() == id) {
                if (tasca.isCompletada() && (completada == null || !completada)) {
                    tasca.setDataFiReal(null);
                }
                tasca.setCompletada(completada == null ? false : completada);
                tasca.setDescripcio(novaDescripcio);
                tasca.setPrioritat(prioritat);
                tascaModificada = tasca;
                break;
            }
        }
        if (tascaModificada == null)
            throw new Exception("La tasca no existeix");
    }

    public Tasca obtenirTasca(int id) throws Exception {
        for (Tasca tasca : llista) {
            if (tasca.getId() == id) {
                return tasca;
            }
        }

        throw new Exception("La tasca no existeix");
    }

    public int getNombreTasques() {
        return llista.size();
    }

    public List<Tasca> llistarTasques() {
        return llista;
    }

    public List<Tasca> llistarTasquesPerDescripcio(String filtreDescripcio) {
        List<Tasca> tasquesFiltrades = new ArrayList<>();
        for (Tasca tasca : llistarTasques()) {
            if (tasca.getDescripcio().toLowerCase().contains(filtreDescripcio.toLowerCase())) {
                tasquesFiltrades.add(tasca);
            }
        }
        return tasquesFiltrades;
    }

    public List<Tasca> llistarTasquesPerComplecio(boolean filtreCompletada) {
        List<Tasca> tasquesFiltrades = new ArrayList<>();
        for (Tasca tasca : llistarTasques()) {
            if (tasca.isCompletada() && filtreCompletada) {
                tasquesFiltrades.add(tasca);
            }
        }
        return tasquesFiltrades;
    }
}