import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Stream use case
 * Stream's has 3 distinct parts. Initial, middle and termination blocks
 * Every action on stream must use a termination block. If we don't call a termination operation
 * the stream won't be executed. So, the process stream will only occur when we call a termination operator
 * The most common termination operator is a forEach. We have others terminations methods like
 * sum, Collect, count, toArray, min, max, anyMatch, findFirst, findAny
 *
 * The middle part, knows as intermediate operations, provides us methods to manipulate the stream
 * For example, a Map operator transforms each member of the stream and produces, in the end,
 * another stream. A filter operator filter the stream retuning a new stream containing only
 * the filtered members. Skip operator removes, from the stream, the amount of the first results
 * that we want, different of limit operator that limit our stream to the amount needed.
 *
 * So, a pipeline is a block of intermediate operators with one single termination operator. We
 * can't read a stream twice. When a stream is processed we can´t process the same stream again.
 * In this case, if we need to process a stream twice we need to produce a new stream.
 */
public class TestCase {


    private List<String> list = null;

    @BeforeEach
    void createList() {
        list = new ArrayList<>();
        list.add("valor 1" + 0);
        list.add("valor 1" + 1);
        list.add("valor 1" + 2);
        list.add("valor 1" + 3);
        list.add("valor 1" + 4);
        list.add("valor " + 5);
        list.add("valor " + 6);
        list.add("valor " + 7);
        list.add("valor " + 8);
        list.add("valor " + 9);
    }

    /**
     * Filtering methods filter the stream
     */
    @Test
    public void filter() {
        long count = this.list.stream().filter(valor -> valor.contains("valor 1")).count();
        Assertions.assertEquals(5L, count);
    }

    /**
     * map methods execute action on each member of the stream
     * in this case we use a specialized map method to convert values
     * into an Integer object
     */
    @Test
    public void mapTo() {
        long count = list.stream().map(valor ->
                valor
                        .replace("valor", "")
                        .replace(" ", ""))
                .mapToInt(valor -> Integer.parseInt(valor))
                .count()
                ;
        Assertions.assertEquals(list.size(), count);
    }

    /**
     * Sorted method rearrange the collection, after mapping to int
     * in asc mode
     */
    @Test
    public void ordering() {
        int[] intArray = list.stream().map(valor -> valor
                        .replace("valor", "")
                        .replace(" ", ""))
                .mapToInt(valor -> Integer.parseInt(valor))
                .sorted()
                .toArray()
                ;
        Collections.reverse(Arrays.asList(intArray));
        Assertions.assertEquals(list.size(), intArray.length);
        Assertions.assertEquals(5, intArray[0]);

    }

    /**
     * Sorted method rearrange members. In this example
     * we rearrange member in a reverse mode. mapToInt method
     * doesn't have a sorted method with comparable because int
     * primitive doesn't implement comparable interface
     */
    @Test
    public void orderingDesc() {
        int[] intArray = list.stream().map(valor -> valor
                        .replace("valor", "")
                        .replace(" ", ""))
                .map(Integer::parseInt)
                .sorted(Collections.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray()
                ;
        Arrays.stream(intArray).forEach(System.out::println);
        Assertions.assertEquals(list.size(), intArray.length);
        Assertions.assertEquals(14, intArray[0]);

    }

    /**
     * In this example we use summingInt to sum Integers
     * We use method reference to convert String to int and then
     * another method reference to get intValue
     */
    @Test
    public void summarizingMap() {
        int sum = list.stream().map(valor ->
                        valor
                                .replace("valor", "")
                                .replace(" ", ""))
                .map(Integer::parseInt)
                .collect(Collectors.summingInt(Integer::intValue))
        ;
        Assertions.assertEquals(95, sum);
    }

    /**
     * Another example of how to sum itens. Here we use a sum method
     * that exists in intStream object
     */
    @Test
    public void summarizingintStream() {
        int sum = list.stream().map(valor ->
                        valor
                                .replace("valor", "")
                                .replace(" ", ""))
                .mapToInt(valor -> Integer.parseInt(valor))
                .sum()
        ;
        Assertions.assertEquals(95, sum);
    }

    /**
     * Reduce method returns a single element from stream
     * Here we are concatenating members of Stream and
     * producing a single String to return
     */
    @Test
    public void reducingString() {
        String expected = "";
        for (String s: list) {
            expected = expected.concat(s).concat(",");
        }
        expected = expected.substring(0, expected.length()-1);

        String resultado = list.stream()
                .reduce((a, b) -> a.concat(",").concat(b)).get();
        Assertions.assertEquals(expected, resultado);
    }

    /**
     * Another way to sum values in a stream. Here we
     * reduce the stream into a single return. Reduce method
     * was used to sum object a and b.
     */
    @Test
    public void reducingSum() {
        int sum = list.stream()
                .map(valor -> valor.replace(" ", "").replace("valor", ""))
                .mapToInt(valor -> Integer.parseInt(valor))
                .reduce((a, b) -> a + b).getAsInt();
        Assertions.assertEquals(95, sum);
    }

    /**
     * FlatMap methods flatten objects. In this example we have a list of lists
     * and we transform this list of lists into a flatten list, containing all
     * elements of all lists.
     */
    @Test
    public void flatMap() {
        List<List<String>> newList = new ArrayList<>();
        newList.add(list);
        newList.add(list);

        List<String> flatList = newList
                .stream()
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
        Assertions.assertEquals(list.size()*2, flatList.size());
    }

    /**
     * In this example we map the stream to remove strings values
     * then we map this new stream into a intStream
     * then we filter this intStream. The filter use a mod operator %
     * so, if the result of <item> mod 2 equals zero means that object is
     * divisible by 2 and there isn't any rest of division.
     */
    @Test
    public void isOdd() {
        int[] oddNumber = {10, 12, 14, 6, 8};
        int[] odds = list.stream()
                .map(valor -> valor.replace(" ", "").replace("valor", ""))
                .mapToInt(valor -> Integer.parseInt(valor))
                .filter(v -> v%2 == 0)
                .toArray()
        ;

        Assertions.assertTrue(Arrays.equals(oddNumber,odds));
        Assertions.assertEquals(5, odds.length);
        Assertions.assertEquals(10, odds[0]);
    }


    /**
     * In this example we tried to show that, once a stream is consumed, we can't
     * consume the same stream twice. If we try will be throw a IllegalStateException
     */
    @Test
    public void isOddStream() {
        IntStream odds = list.stream()
                .map(valor -> valor.replace(" ", "").replace("valor", ""))
                .filter(v -> Integer.parseInt(v) % 2 == 0)
                .flatMapToInt(v -> IntStream.builder().add(Integer.parseInt(v)).build());

        Assertions.assertEquals(5, odds.count());
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,() -> odds.findFirst());
        Assertions.assertEquals("stream has already been operated upon or closed", exception.getMessage());
    }

    /**
     * Different of get the first position of an Array, here we use a findFirst method.
     * This method returns an optional object and we can get this optional.
     */
    @Test
    public void findFirst() {
        IntStream first = list.stream()
                .map(valor -> valor.replace(" ", "").replace("valor", ""))
                .flatMapToInt(v -> IntStream.of(Integer.parseInt(v)))
                .sorted();

        Assertions.assertEquals(5, first.findFirst().getAsInt());
    }

    /**
     * here is the same example of oddNumbers but, instead of oddNumbers, here
     * we need to collect the numbers where the response of mod produces a rest division.
     */
    @Test
    public void isEven() {
        int[] even = list.stream()
                .map(valor -> valor.replace(" ", "").replace("valor", ""))
                .mapToInt(valor -> Integer.parseInt(valor))
                .filter(v -> v%2 != 0)
                .toArray()
                ;

        Assertions.assertEquals(5, even.length);
        Assertions.assertEquals(11, even[0]);
    }

}
