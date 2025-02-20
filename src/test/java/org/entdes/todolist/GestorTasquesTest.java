package org.entdes.todolist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

class GestorTasquesTest {
    private GestorTasques gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorTasques();
    }

    @Test
    void afegirTasca() throws Exception {
        int id = gestor.afegirTasca("Comprar pa", null, null, null);
        assertEquals(1, gestor.getNombreTasques());
        assertEquals("Comprar pa", gestor.obtenirTasca(id).getDescripcio());
    }

    @Test
    void eliminarTasca() {
    }

    @Test
    void marcarCompletada() {
    }

    @Test
    void modificarTasca() {
    }

    @Test
    void obtenirTasca() {
    }

    @Test
    void getNombreTasques() {
    }

    @Test
    void llistarTasques() {
    }

    @Test
    void llistarTasquesPerDescripcio() {
    }

    @Test
    void llistarTasquesPerComplecio() {
    }

    @Test
    void crearTascaAmbDescripcioDataIniciDataFiIPrioritat() throws Exception {
        LocalDate avui = LocalDate.now();
        LocalDate futura = avui.plusDays(5);
        int id = gestor.afegirTasca("Fer exercici", avui, futura, 3);
        assertEquals(avui, gestor.obtenirTasca(id).getDataInici());
        assertEquals(futura, gestor.obtenirTasca(id).getDataFiPrevista());
        assertEquals(3, gestor.obtenirTasca(id).getPrioritat());
    }

    @Test
    void noPermetreTasquesAmbMismaDescripcioIgnorantMajusculesMinuscules() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Fer esport", null, null, null);
            gestor.afegirTasca("fer esport", null, null, null);
        });
        assertEquals("Ja existeix una tasca amb la mateixa descripció", exception.getMessage());
    }

    @Test
    void haHaverLlistatAmbNombreTotalTasques() throws Exception {
        assertEquals(0, gestor.getNombreTasques());
        gestor.afegirTasca("Tasca 1", null, null, null);
        gestor.afegirTasca("Tasca 2", null, null, null);
        assertEquals(2, gestor.getNombreTasques());
    }

    @Test
    void filtrarTasquesPerDescripcioIgnorantMajusculesMinuscules() throws Exception {
        gestor.afegirTasca("Comprar Pa", null, null, null);
        gestor.afegirTasca("Fer exercici", null, null, null);
        gestor.afegirTasca("Comprar llet", null, null, null);
        
        List<Tasca> resultats = gestor.llistarTasquesPerDescripcio("comprar");
        assertEquals(2, resultats.size());
    }

    @Test
    void filtrarTasquesPerComplecio() throws Exception {
        int id1 = gestor.afegirTasca("Tasca 1", null, null, null);
        int id2 = gestor.afegirTasca("Tasca 2", null, null, null);
        int id3 = gestor.afegirTasca("Tasca 3", null, null, null);

        gestor.marcarCompletada(id1);

        List<Tasca> completades = gestor.llistarTasquesPerComplecio(true);
        List<Tasca> noCompletades = gestor.llistarTasquesPerComplecio(false);
        
        assertEquals(1, completades.size());
        assertEquals(2, noCompletades.size());
    }

    @Test
void testFiltresIndependents() throws Exception {
    GestorTasques gestor = new GestorTasques();

    int id1 = gestor.afegirTasca("Comprar pa", null, null, null);
    int id2 = gestor.afegirTasca("Fer exercici", null, null, null);
    int id3 = gestor.afegirTasca("Comprar llet", null, null, null);

    gestor.marcarCompletada(id1);

    // Filtrar per descripció "Comprar"
    List<Tasca> tasquesFiltradesPerDescripcio = gestor.llistarTasquesPerDescripcio("Comprar");
    assertEquals(2, tasquesFiltradesPerDescripcio.size());  // "Comprar pa" i "Comprar llet"

    // Filtrar per compleció (només completades)
    List<Tasca> tasquesCompletades = gestor.llistarTasquesPerComplecio(true);
    assertEquals(1, tasquesCompletades.size());  // Només "Comprar pa"

    // Filtrar per compleció (només no completades)
    List<Tasca> tasquesNoCompletades = gestor.llistarTasquesPerComplecio(false);
    assertEquals(2, tasquesNoCompletades.size());  // "Fer exercici" i "Comprar llet"

    // Sense filtres, s'han de mostrar totes les tasques
    List<Tasca> totesLesTasques = gestor.llistarTasques();
    assertEquals(3, totesLesTasques.size());
}
void testBotonsPerCadaTasca() throws Exception {
        int id = gestor.afegirTasca("Fer la compra", LocalDate.now(), LocalDate.now().plusDays(2), 3);
        
        Tasca tasca = gestor.obtenirTasca(id);
        assertNotNull(tasca);

        // Marcar com completada
        assertFalse(tasca.isCompletada());
        gestor.marcarCompletada(id);
        assertTrue(gestor.obtenirTasca(id).isCompletada());

        // Editar la tasca
        gestor.modificarTasca(id, "Comprar menjar", false, LocalDate.now(), LocalDate.now().plusDays(2), 4);
        assertEquals("Comprar menjar", gestor.obtenirTasca(id).getDescripcio());

        // Eliminar la tasca
        gestor.eliminarTasca(id);
        assertThrows(Exception.class, () -> gestor.obtenirTasca(id));
    }

    @Test
    void testEdicioTasca() throws Exception {
        int id = gestor.afegirTasca("Fer esport", LocalDate.now(), LocalDate.now().plusDays(3), 2);
        
        gestor.modificarTasca(id, "Fer running", true, LocalDate.now(), LocalDate.now().plusDays(5), 5);
        
        Tasca tasca = gestor.obtenirTasca(id);
        assertEquals("Fer running", tasca.getDescripcio());
        assertTrue(tasca.isCompletada());
        assertEquals(5, tasca.getPrioritat());
    }

    @Test
    void testDataFiRealEsGuardaEnMarcarCompletada() throws Exception {
        int id = gestor.afegirTasca("Llegir un llibre", LocalDate.now(), LocalDate.now().plusDays(4), 4);
        
        gestor.marcarCompletada(id);
        assertEquals(LocalDate.now(), gestor.obtenirTasca(id).getDataFiReal());

        // Desmarcar la tasca com completada ha de buidar la data fi real
        gestor.modificarTasca(id, "Llegir un llibre", false, LocalDate.now(), LocalDate.now().plusDays(4), 4);
        assertNull(gestor.obtenirTasca(id).getDataFiReal());
    }

    @Test
    void testValidarDates() {
        Exception ex1 = assertThrows(Exception.class, () -> 
            gestor.afegirTasca("Tasques passades", LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), 3));
        assertEquals("La data d'inici no pot ser anterior a la data actual.", ex1.getMessage());

        Exception ex2 = assertThrows(Exception.class, () -> 
            gestor.afegirTasca("Tasques incorrectes", LocalDate.now().plusDays(3), LocalDate.now().plusDays(2), 2));
        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", ex2.getMessage());
    }

    @Test
    void testValidarPrioritat() {
        Exception ex1 = assertThrows(Exception.class, () -> 
            gestor.afegirTasca("Prioritat baixa", LocalDate.now(), LocalDate.now().plusDays(2), 0));
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", ex1.getMessage());

        Exception ex2 = assertThrows(Exception.class, () -> 
            gestor.afegirTasca("Prioritat alta", LocalDate.now(), LocalDate.now().plusDays(2), 6));
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", ex2.getMessage());
    }
}