
import io.vavr.Tuple2;
import io.vavr.Tuple4;
import io.vavr.control.Option;
import it.proactivity.logic.MyLogic;
import it.proactivity.model.MyModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class MyLogicTest {


    private static final List<Option<MyModel>> myModelListForTest = createMyModelList(5, 5);


    @Test
    void readAndSplitMyModelListPositiveTest() {

        Tuple2<List<Integer>, List<String>> tuple2 = MyLogic.readAndSplitMyModelList(myModelListForTest);

        List<Integer> firstParamListFromTuple = tuple2._1;
        List<Integer> firstParamListFromMyModelList = getFirstParamListFromMyModelList(myModelListForTest);

        Integer actual = firstParamListFromTuple.get(0);
        Integer expected = firstParamListFromMyModelList.get(0);
        assertEquals(expected, actual);

        List<String> secondParamListFromTuple = tuple2._2;
        List<String> secondParamListFromMyModelList = getSecondParamListFromMyModelList(myModelListForTest);

        String actualString = secondParamListFromTuple.get(0);
        String expectedString = secondParamListFromMyModelList.get(0);
        assertTrue(expectedString.equals(actualString));

    }

    @Test
    void readAndSplitMyModelListNegativeTest() {
        List<Option<MyModel>> emptyList = new ArrayList<>();
        Tuple2<List<Integer>, List<String>> nullTuple = MyLogic.readAndSplitMyModelList(emptyList);

        assertNull(nullTuple);
    }

    @Test
    void easyStatsOnMyModelListPositiveTest() {

        Tuple4<List<Integer>, List<String>, Double, Double> tuple = MyLogic.easyStatsOnMyModelList(myModelListForTest);
        Double actual = tuple._4;
        Double expected = 5.0;
        assertEquals(actual, expected);
    }

    @Test
    void easyStatsOnMyModelListNegativeTest() {
        List<Option<MyModel>> emptyList = new ArrayList<>();
        Tuple4<List<Integer>, List<String>, Double, Double> nullTuple = MyLogic.easyStatsOnMyModelList(emptyList);
        assertNull(nullTuple);
    }

    @Test
    void deleteRepeatedIntegerValueTest() {
        io.vavr.collection.Queue<Option<MyModel>> vavrQueue = createVavrQueueForTest();
        io.vavr.collection.Queue<Option<MyModel>> queueWithoutRepeatedValue = MyLogic.deleteRepeatedIntegerValue(vavrQueue);

        Integer expectedSize = 3;
        Integer actualSize = queueWithoutRepeatedValue.size();
        assertEquals(expectedSize, actualSize);

        Integer firstParamFromFirstElementListExpected = queueWithoutRepeatedValue.get(0).get().getFirstParam();
        Integer firstParamFromFirstElementListActual = 1;

        assertEquals(firstParamFromFirstElementListExpected, firstParamFromFirstElementListActual);
    }

    @Test
    void listAllNumberFromOneToOneHundredAndDeleteTheLastTenTest() {
        io.vavr.collection.List<Integer> list = MyLogic.listAllNumberFromOneToOneHundredAndDeleteTheLastTen();

        Integer actualSize = list.size();
        Integer expectedSize = 90;

        assertEquals(expectedSize, actualSize);

    }

    @Test
    void groupElementInListByFirstLetterTest() {
        io.vavr.collection.List<String> stringList = MyLogic.groupElementInListByFirstLetter();

    }

    private static List<Option<MyModel>> createMyModelList(Integer size, Integer stringLength) {
        List<Option<MyModel>> myModelListForTesting = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Option<MyModel> myModel = Option.of(new MyModel(i, RandomStringUtils.randomAlphabetic(stringLength)));
            myModelListForTesting.add(myModel);
        }
        return myModelListForTesting;
    }

    private static List<Integer> getFirstParamListFromMyModelList(List<Option<MyModel>> myModelList) {
        return myModelList.stream()
                .map(m -> m.get().getFirstParam())
                .toList();
    }

    private static List<String> getSecondParamListFromMyModelList(List<Option<MyModel>> myModelList) {
        return myModelList.stream()
                .map(m -> m.get().getSecondParam())
                .toList();
    }

    private static io.vavr.collection.Queue<Option<MyModel>> createVavrQueueForTest() {
        io.vavr.collection.Queue<Option<MyModel>> queueForTesting = io.vavr.collection.Queue.of(Option.of(new MyModel(1, "Ciao")),
                Option.of(new MyModel(2, "Come")), Option.of(new MyModel(3, "Stai")), Option.of(new MyModel(3, "Io sto bene")),
                Option.of(new MyModel(4, "Ci vediamo")), Option.of(new MyModel(4, "Dopo")), Option.of(new MyModel(5, "Allora")));

        return queueForTesting;
    }
}
