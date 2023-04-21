package it.proactivity.logic;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple4;
import io.vavr.control.Option;
import io.vavr.control.Try;
import it.proactivity.model.MyModel;
import org.apache.commons.lang3.StringUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MyLogic {

    private static final BiPredicate<Integer, Integer> validateUserInputFirstParam = (userInput, index) ->
            userInput % 2 == 0 && index % 2 == 0 || userInput % 2 == 1 && index % 2 == 1;
    private static final Predicate<String> validateSecondUserInput = StringUtils::isAlphaSpace;

    public static List<Option<MyModel>> createListByUserInput(Integer size) {
        if (size == null) {
            return Collections.emptyList();
        }
        Scanner scanner = new Scanner(System.in);
        return createMyModelList(scanner, size);
    }

    public static Tuple2<List<Integer>, List<String>> readAndSplitMyModelList(List<Option<MyModel>> myModelList) {
        if (myModelList.isEmpty()) {
            return null;
        }
        return createTupleFromMyModelList(myModelList);
    }

    public static Tuple4<List<Integer>, List<String>, Double, Double> easyStatsOnMyModelList(List<Option<MyModel>> myModelList) {

        if (myModelList.isEmpty()) {
            return null;
        }

        Tuple2<List<Integer>, List<String>> tuple2 = readAndSplitMyModelList(myModelList);

        if (tuple2 != null) {
            List<Integer> firstParamList = tuple2._1;
            List<String> secondParamList = tuple2._2;
            Double averageFirstParameter = getAverageFromFirstParameterList(firstParamList);
            Double averageSecondParam = getAverageSecondParam(secondParamList);
            return Tuple.of(firstParamList, secondParamList, averageFirstParameter, averageSecondParam);
        }
        return null;
    }

    private static Double getAverageSecondParam(List<String> secondParamList) {
        return secondParamList.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);
    }

    private static Double getAverageFromFirstParameterList(List<Integer> firstParamList) {
        return firstParamList.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    private static Tuple2<List<Integer>, List<String>> createTupleFromMyModelList(List<Option<MyModel>> myModelList) {
        List<Integer> firstParamList = myModelList.stream()
                .map(m -> m.get().getFirstParam())
                .toList();

        List<String> secondParamList = myModelList.stream()
                .map(m -> m.get().getSecondParam())
                .toList();

        return Tuple.of(firstParamList, secondParamList);
    }

    private static List<Option<MyModel>> createMyModelList(Scanner scanner, Integer size) {

        List<Option<MyModel>> myModelList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Try<Integer> firstUserInput = Try.of(scanner::nextInt);
            Try<String> secondUserInput = Try.of(scanner::next);

            if (firstUserInput.isFailure() || secondUserInput.isFailure()) {
                myModelList.add(Option.none());
            } else {
                Option<MyModel> modelToInsert = createMyModel(firstUserInput.get(), secondUserInput.get(), i);
                myModelList.add(modelToInsert.isDefined() ? modelToInsert : Option.none());
            }
        }
        return myModelList;
    }

    private static Option<MyModel> createMyModel(Integer firstUserInput, String secondUserInput, Integer index) {

        if (validateUserInputFirstParam.test(firstUserInput, index) && validateSecondUserInput.test(secondUserInput)) {
            MyModel model = new MyModel();
            model.setFirstParam(firstUserInput);
            model.setSecondParam(secondUserInput);
            return Option.of(model);
        }
        return Option.none();
    }
}
