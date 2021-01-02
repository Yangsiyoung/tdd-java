package tdd.chapter05;

import org.junit.jupiter.api.*;

public class JunitLifecycleTest {

    @BeforeAll
    public static void beforeAllTest() {
        System.out.println("### beforeAllTest is executed once###");
    }
    @BeforeEach
    public void beforeEachTest() {
        System.out.println("## beforeEachTest is executed each test##");
    }

    @Test
    public void aTest() {
        System.out.println("# aTest #");
    }

    @Test
    public void bTest() {
        System.out.println("# bTest #");
    }

    @AfterEach
    public void afterEachTest() {
        System.out.println("## afterEachTest is executed each test##");
    }

    @AfterAll
    public static void afterAllTest() {
        System.out.println("### afterAllTest is executed once###");
    }
}
