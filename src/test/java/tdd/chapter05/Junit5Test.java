package tdd.chapter05;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tdd.chapter02.Calculator;

import static org.junit.jupiter.api.Assertions.*;

public class Junit5Test {

    @Test
    public void assertEqualsTest() {
        int expect = 3;
        assertEquals(expect, Calculator.plus(1, 2));
    }

    @Test
    public void assertNotEqualsTest() {
        int expect = 3;
        assertNotEquals(expect, Calculator.plus(2, 2));
    }

    @Test
    public void assertSameTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        Student student2 = student1;
        assertSame(student1, student2);
    }

    @Test
    public void assertNotSameTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        Student student2 = new Student("리다양", "우리집", 28);
        assertNotSame(student1, student2);
    }

    @Test
    public void assertTrueTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        Student student2 = new Student("리다양", "우리집", 28);
        assertTrue(student1.equals(student2));
    }

    @Test
    public void assertNotTrueTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        Student student2 = new Student("리다양", "우리집", 28);
        assertFalse(student1 == student2);
    }

    @Test
    public void assertNullTest() {
        Student student1 = null;
        assertNull(student1);
    }

    @Test
    public void assertNotNullTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        assertNotNull(student1);
    }

    @Test
    public void failTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        if(!student1.getName().equals("리다양")) {
            fail();
        }
        assertEquals(new Student("리다양", "우리집", 28), student1);
    }

    @Test
    public void assertThrowsTest() {
        Student student1 = new Student("리다양", "우리집", -1);
        assertThrows(InvalidAgeException.class, () -> student1.getAge());
    }

    @Test
    public void assertDoesNotThrowTest() {
        Student student1 = new Student("리다양", "우리집", 28);
        assertDoesNotThrow(() -> student1.getAge());
    }

    @Disabled
    @Test
    public void disabledTest() {
        // test case is not complete, so this test case is disabled temporarily
    }
}
