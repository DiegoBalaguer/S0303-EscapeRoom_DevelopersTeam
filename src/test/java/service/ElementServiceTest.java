package service;

import mvc.model.TestElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElementServiceTest {

    private ElementService<TestElement> elementService;

    @BeforeEach
    void setUp() {
        elementService = new ElementService<>();
    }

    // Test para agregar elementos
    @Test
    void testCreate() {
        TestElement element = new TestElement(1, "Element 1");
        elementService.create(element);

        List<TestElement> elements = elementService.readAll();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    // Test para leer todos los elementos
    @Test
    void testReadAll() {
        TestElement element1 = new TestElement(1, "Element 1");
        TestElement element2 = new TestElement(2, "Element 2");

        elementService.create(element1);
        elementService.create(element2);

        List<TestElement> elements = elementService.readAll();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }

    // Test para actualizar un elemento existente
    @Test
    void testUpdate() {
        TestElement element = new TestElement(1, "Element 1");
        TestElement updatedElement = new TestElement(1, "Updated Element");

        elementService.create(element);
        elementService.update(1, updatedElement);

        List<TestElement> elements = elementService.readAll();
        assertEquals(1, elements.size());
        assertEquals("Updated Element", elements.get(0).getName());
    }

    // Test para eliminar un elemento por su id
    @Test
    void testDelete() {
        TestElement element1 = new TestElement(1, "Element 1");
        TestElement element2 = new TestElement(2, "Element 2");

        elementService.create(element1);
        elementService.create(element2);

        elementService.delete(1);

        List<TestElement> elements = elementService.readAll();
        assertEquals(1, elements.size());
        assertEquals(2, elements.get(0).getId());
    }

    // Test para eliminar un elemento que no existe
    @Test
    void testDeleteNonExistingElement() {
        TestElement element = new TestElement(1, "Element 1");
        elementService.create(element);

        elementService.delete(99); // id que no existe

        List<TestElement> elements = elementService.readAll();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }
}