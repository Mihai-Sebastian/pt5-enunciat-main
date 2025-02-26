package org.entdes.todolist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


class TascaTest {

    @BeforeEach
    void resetIdCounter() {
        // Reinicia l'idCounter per evitar efectes entre tests
        Tasca.resetIdCounter();
    }

    @Test
    void testConstructor() {
        Tasca tasca = new Tasca("Descripció");
        assertEquals("Descripció", tasca.getDescripcio());
        assertEquals(1, tasca.getId());
    }

    @Test
    void testSettersAndGetters() {
        Tasca tasca = new Tasca("Test");
        LocalDate now = LocalDate.now();

        tasca.setCompletada(true);
        assertTrue(tasca.isCompletada());

        tasca.setDescripcio("Nova descripció");
        assertEquals("Nova descripció", tasca.getDescripcio());

        tasca.setDataInici(now);
        assertEquals(now, tasca.getDataInici());

        tasca.setDataFiPrevista(now.plusDays(1));
        assertEquals(now.plusDays(1), tasca.getDataFiPrevista());

        tasca.setDataFiReal(now);
        assertEquals(now, tasca.getDataFiReal());

        tasca.setPrioritat(3);
        assertEquals(Integer.valueOf(3), tasca.getPrioritat());

        tasca.setPrioritat(null);
        assertNull(tasca.getPrioritat());
    }

    @Test
    void testIdIncrement() {
        Tasca tasca1 = new Tasca("Tasca 1");
        Tasca tasca2 = new Tasca("Tasca 2");
        assertEquals(1, tasca1.getId());
        assertEquals(2, tasca2.getId());
    }

    @Test
    void testToString() {
        Tasca tasca = new Tasca("Test");
        assertEquals("Test: Pendent", tasca.toString());
        tasca.setCompletada(true);
        assertEquals("Test: Completada", tasca.toString());
    }
}