import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Here we will demonstrate how default response interface methods work.
 * The main problem that motivate creation of default response was to
 * not break the entire code if a new interface method was created.
 * With default response in a method interface it's not necessary that
 * all classes that implements that interface implements this new method.
 */
public class DefaultMethodTest {

    private DefaultInterface defaultInterface;

    private DefaultInterface defaultInterfaceImplementsAll;

    /**
     * We create, here, an anonymous class. This means that we don't need
     * to have a concrete class to implements an interface. We can create the object
     * in the same time we are using it. To create this anonymous class just need to
     * create a new interface object and implement the methods.
     */
    @BeforeEach
    public void initializeDefaultInterface(){
        defaultInterface = new DefaultInterface() {
            @Override
            public String tryMe(String message) {
                return "you send this message: ".concat(message);
            }
        };

        defaultInterfaceImplementsAll = new DefaultInterface() {
            @Override
            public String tryMe(String message) {
                return "You send this message to me: ".concat(message);
            }

            @Override
            public String tryMeAgain(String message) {
                return "You send this message, again, to me: ".concat(message);
            }
        };
    }

    /**
     * In this test we just call a method that don´t have a default response applied to.
     */
    @Test
    public void testTryMe(){
        Assertions.assertEquals(
                "you send this message: my name is marcus",
                defaultInterface.tryMe("my name is marcus"));
    }

    /**
     * In this example we call a method that has a default response. This method wasn't
     * implemented in our anonymous class, so the response of our call is the default
     * response registered in the method.
     */
    @Test
    public void testTryMeAgain(){
        Assertions.assertEquals(
                "You didn't send nothing to me",
                defaultInterface.tryMeAgain(""));
    }

    /**
     * In this example we call a method that has a default response.
     * But, differently of previous test method, here we are using an anonymous class
     * that implements our new default method.
     */
    @Test
    public void testTryMeAgainAll(){
        Assertions.assertEquals(
                "You send this message, again, to me: my name is marcus",
                defaultInterfaceImplementsAll.tryMeAgain("my name is marcus"));
    }
}
