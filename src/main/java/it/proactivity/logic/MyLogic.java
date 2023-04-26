package it.proactivity.logic;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple4;
import io.vavr.control.Option;
import io.vavr.control.Try;
import it.proactivity.model.MyModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;


import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyLogic {

    private static final BiPredicate<Integer, Integer> validateUserInputFirstParam = (userInput, index) ->
            userInput % 2 == 0 && index % 2 == 0 || userInput % 2 == 1 && index % 2 == 1;
    private static final Predicate<String> validateSecondUserInput = StringUtils::isAlphaSpace;

    private static final Predicate<Integer> deleteLast10Numbers = value -> value <= 90;



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

    public static io.vavr.collection.List<Option<MyModel>> createVavrListByUserInput(Integer size) {
        return io.vavr.collection.List.ofAll(createListByUserInput(size));
    }

    public static io.vavr.collection.Queue<Option<MyModel>> createVavrQueueByUserInput(Integer size) {
        return io.vavr.collection.Queue.ofAll(createListByUserInput(size));
    }

    public static io.vavr.collection.Queue<Option<MyModel>> deleteRepeatedIntegerValue(io.vavr.collection.Queue<Option<MyModel>>
                                                                                              vavrListFromUserInput) {

        Set<Integer> seenValues = new HashSet<>();
        io.vavr.collection.Queue<Option<MyModel>> outputQueue = io.vavr.collection.Queue.empty();
        for (Option<MyModel> option : vavrListFromUserInput) {
            if (option.isDefined()) {
                MyModel myModel = option.get();
                if (!seenValues.contains(myModel.getFirstParam())) {
                    seenValues.add(myModel.getFirstParam());
                    outputQueue = outputQueue.append(Option.of(myModel));
                }
            }
        }
        return outputQueue;
    }

    public static io.vavr.collection.List<Integer> listAllNumberFromOneToOneHundredAndDeleteTheLastTen() {

        List<Integer> integerList = new ArrayList<>();
        addIntegerElementToList(integerList);

        io.vavr.collection.List<Integer> vavrIntegerList = io.vavr.collection.List.ofAll(integerList);
        return vavrIntegerList.filter(deleteLast10Numbers);
    }

    public static io.vavr.collection.List<String> groupElementInListByFirstLetter() {
        io.vavr.collection.List<String> randomStringList = createRandomStringList(50);

        Map<Character, Integer> mapForCountMostUsedLetter = createMostUsedLetterInStringList(randomStringList);

        return createListWithMostUsedLetter(mapForCountMostUsedLetter, randomStringList);
    }

    private static io.vavr.collection.List<String> createListWithMostUsedLetter(Map<Character, Integer> mapForCountMostUsedLetter,
                                                                                io.vavr.collection.List<String> randomStringList) {
        Integer maxValue =  mapForCountMostUsedLetter.values().stream()
                .mapToInt(v -> v)
                .max().getAsInt();

        List<Character> mostUsedLetter = mapForCountMostUsedLetter.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(maxValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return randomStringList.filter(s -> mostUsedLetter.stream()
                        .anyMatch(c -> s.startsWith(c.toString())))
                .toList();

    }


    private static Map<Character, Integer> createMostUsedLetterInStringList(io.vavr.collection.List<String> randomStringList) {
        Map<Character, Integer> mapForCountingUsedLetter = new HashMap<>();
        randomStringList.forEach(m -> {
            if (!mapForCountingUsedLetter.containsKey(m.charAt(0))) {
                mapForCountingUsedLetter.put(m.charAt(0), 1);
            } else {
                mapForCountingUsedLetter.replace(m.charAt(0), mapForCountingUsedLetter.get(m.charAt(0) + 1));
            }
        });
        return mapForCountingUsedLetter;
    }

    private static io.vavr.collection.List<String> createRandomStringList(Integer size) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(generateRandomNumber());
            stringList.add(randomString);
        }
        return io.vavr.collection.List.ofAll(stringList);
    }

    private static Integer generateRandomNumber() {
        int min = 5;
        int max = 10;
        int randomNumber = (int)(Math.random() * ((max - min) + 1)) + min;
        return randomNumber;
    }

    private static void addIntegerElementToList(List<Integer> integerList) {
        for (int i = 1; i <= 100; i++) {
            integerList.add(i);
        }
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
