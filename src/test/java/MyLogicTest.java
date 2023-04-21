
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
}
