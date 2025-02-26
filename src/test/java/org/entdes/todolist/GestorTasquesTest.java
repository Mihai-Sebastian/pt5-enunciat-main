package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testAfegirTascaAmbDescripcioBuida() {
        Exception ex = assertThrows(Exception.class, () -> gestor.afegirTasca("", null, null, null));
        assertEquals("La descripció no pot estar buida.", ex.getMessage());
    }

    @Test
    void testAfegirTascaAmbDescripcioNull() {
        Exception ex = assertThrows(Exception.class, () -> gestor.afegirTasca(null, null, null, null));
        assertEquals("La descripció no pot estar buida.", ex.getMessage());
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
    void noPermetreTasquesAmbMateixaDescripcioIgnorantMajusculesMinuscules() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Fer esport", null, null, null);
            gestor.afegirTasca("fer esport", null, null, null);
        });
        assertEquals("Ja existeix una tasca amb la mateixa descripció", exception.getMessage());
    }

    @Test
    void llistatAmbNombreTotalTasques() throws Exception {
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
        int id1 = gestor.afegirTasca("Comprar pa", null, null, null);
        int id2 = gestor.afegirTasca("Fer exercici", null, null, null);
        int id3 = gestor.afegirTasca("Comprar llet", null, null, null);

        gestor.marcarCompletada(id1);

        List<Tasca> tasquesFiltradesPerDescripcio = gestor.llistarTasquesPerDescripcio("Comprar");
        assertEquals(2, tasquesFiltradesPerDescripcio.size());

        List<Tasca> tasquesCompletades = gestor.llistarTasquesPerComplecio(true);
        assertEquals(1, tasquesCompletades.size());

        List<Tasca> tasquesNoCompletades = gestor.llistarTasquesPerComplecio(false);
        assertEquals(2, tasquesNoCompletades.size());

        List<Tasca> totesLesTasques = gestor.llistarTasques();
        assertEquals(3, totesLesTasques.size());
    }

    @Test
    void testBotonsPerCadaTasca() throws Exception {
        int id = gestor.afegirTasca("Fer la compra", LocalDate.now(), LocalDate.now().plusDays(2), 3);
        
        Tasca tasca = gestor.obtenirTasca(id);
        assertNotNull(tasca);

        gestor.marcarCompletada(id);
        assertTrue(gestor.obtenirTasca(id).isCompletada());

        gestor.modificarTasca(id, "Comprar menjar", false, LocalDate.now(), LocalDate.now().plusDays(2), 4);
        assertEquals("Comprar menjar", gestor.obtenirTasca(id).getDescripcio());

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
        assertEquals(LocalDate.now(), tasca.getDataFiReal());
    }

    @Test
    void testDataFiRealEsGuardaEnMarcarCompletada() throws Exception {
        int id = gestor.afegirTasca("Llegir un llibre", LocalDate.now(), LocalDate.now().plusDays(4), 4);
        
        gestor.marcarCompletada(id);
        assertEquals(LocalDate.now(), gestor.obtenirTasca(id).getDataFiReal());

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

    @Test
    void testMarcarCompletadaTascaNoExistent() {
        Exception ex = assertThrows(Exception.class, () -> gestor.marcarCompletada(999));
        assertEquals("La tasca no existeix", ex.getMessage());
    }

    @Test
    void testEliminarTascaNoExistent() {
        Exception ex = assertThrows(Exception.class, () -> gestor.eliminarTasca(999));
        assertEquals("La tasca no existeix", ex.getMessage());
    }

    @Test
    void testObtenirTascaNoExistent() {
        Exception ex = assertThrows(Exception.class, () -> gestor.obtenirTasca(999));
        assertEquals("La tasca no existeix", ex.getMessage());
    }

    @Test
    void testModificarTascaAmbDescripcioDuplicada() throws Exception {
        gestor.afegirTasca("Tasca original", null, null, null);
        int id = gestor.afegirTasca("Altre tasca", null, null, null);
        
        Exception ex = assertThrows(Exception.class, () -> 
            gestor.modificarTasca(id, "TASCA ORIGINAL", null, null, null, null));
        assertEquals("Ja existeix una tasca amb la mateixa descripció", ex.getMessage());
    }

    @Test
    void testModificarPrioritatInvalida() throws Exception {
        int id = gestor.afegirTasca("Tasca", null, null, 2);
        Exception ex = assertThrows(Exception.class, () ->
            gestor.modificarTasca(id, "Tasca", null, null, null, 0));
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", ex.getMessage());
    }

    @Test
    void testModificarTascaSenseCanviarCompletada() throws Exception {
        int id = gestor.afegirTasca("Tasca", null, null, null);
        gestor.marcarCompletada(id);
        
        gestor.modificarTasca(id, "Tasca modificada", null, null, null, null);
        
        Tasca tasca = gestor.obtenirTasca(id);
        assertTrue(tasca.isCompletada());
    }

    @Test
    void testModificarTascaAmbDataIniciAnteriorActual() throws Exception {
        int id = gestor.afegirTasca("Tasca original", LocalDate.now(), null, null);
        Exception ex = assertThrows(Exception.class, () ->
            gestor.modificarTasca(id, "Tasca modificada", null, LocalDate.now().minusDays(1), null, null));
        assertEquals("La data d'inici no pot ser anterior a la data actual.", ex.getMessage());
    }

    @Test
    void testFiltrarTasquesPerDescripcioBuit() throws Exception {
        gestor.afegirTasca("Tasca 1", null, null, null);
        gestor.afegirTasca("Tasca 2", null, null, null);
        List<Tasca> resultats = gestor.llistarTasquesPerDescripcio("");
        assertEquals(2, resultats.size());
    }
}