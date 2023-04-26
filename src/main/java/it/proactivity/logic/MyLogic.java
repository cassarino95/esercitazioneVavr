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

    public static io.vavr.collection.List<Option<MyModel>> createVavrListByUserInput(Integer size) {
        Try<io.vavr.collection.List<Option<MyModel>>> modelList =  Try.of(() -> io.vavr.collection.List.ofAll(createListByUserInput(size)));
        if (modelList.isSuccess()) {
            return modelList.get();
        }
        return io.vavr.collection.List.of(Option.none());

    }

    public static io.vavr.collection.Queue<Option<MyModel>> createVavrQueueByUserInput(Integer size) {
        Try<io.vavr.collection.Queue<Option<MyModel>>> myModelQueue = Try.of(()-> io.vavr.collection.Queue.ofAll(createListByUserInput(size)));

        if (myModelQueue.isSuccess()) {
            return myModelQueue.get();
        }
        return io.vavr.collection.Queue.of(Option.none());
    }

    public static io.vavr.collection.Queue<Option<MyModel>> deleteRepeatedIntegerValue(io.vavr.collection.Queue<Option<MyModel>>
                                                                                              vavrListFromUserInput) {

      Set<Integer> firstParamUniqueSet = createSetOfUniqueFirstParam(vavrListFromUserInput);

        io.vavr.collection.Queue<Option<MyModel>> queueWithoutRepeatedValue = vavrListFromUserInput
                .filter(option -> option.map(MyModel::getFirstParam).exists(firstParamUniqueSet::contains));

        return queueWithoutRepeatedValue;
    }

    public static io.vavr.collection.List<Integer> listAllNumberFromOneToOneHundredAndDeleteTheLastTen() {

        return io.vavr.collection.List.rangeClosed(0, 100).dropRight(10);
    }

    public static io.vavr.collection.List<String> groupElementInListByFirstLetter() {
        io.vavr.collection.List<String> randomStringList = createRandomStringList(30);

        Character mostUsedCharacter = getMostUsedCharacter(randomStringList);

        return createListWithMostUsedLetter(mostUsedCharacter, randomStringList);
    }

    private static Set<Integer> createSetOfUniqueFirstParam(io.vavr.collection.Queue<Option<MyModel>> myModelList) {

        Set<Integer> uniqueFirstParamSet = new HashSet<>();
        myModelList.forEach(m -> {
            if (!uniqueFirstParamSet.contains(m.get().getFirstParam())) {
                uniqueFirstParamSet.add(m.get().getFirstParam());
            } else {
                uniqueFirstParamSet.remove(m.get().getFirstParam());
            }
        });

        return uniqueFirstParamSet;
    }



    private static io.vavr.collection.List<String> createListWithMostUsedLetter(Character mostUsedCharacter,
                                                                                io.vavr.collection.List<String> randomStringList) {

        return randomStringList.filter(s -> s.startsWith(mostUsedCharacter.toString()))
                .toList();
    }


    private static Character getMostUsedCharacter(io.vavr.collection.List<String> randomStringList) {
        Map<Character, Integer> mapForCountingUsedLetter = getInitialLetterAndTheirUsageMap(randomStringList);

        Integer maxValue = getMaxValueFromMap(mapForCountingUsedLetter);

        return getMostUsedCharacter(mapForCountingUsedLetter, maxValue);

    }

    private static Character getMostUsedCharacter(Map<Character, Integer> mapForCountingUsedLetter, Integer maxValue) {
        return mapForCountingUsedLetter.entrySet().stream()
                .filter(e -> e.getValue() == maxValue)
                .map(Map.Entry::getKey)
                .sorted()
                .findFirst().get();
    }

    private static Integer getMaxValueFromMap(Map<Character, Integer> mapForCountingUsedLetter) {
        List<Integer> valuesList = mapForCountingUsedLetter.values().stream().toList();
        Integer maxValue = valuesList.stream()
                .mapToInt(v -> v)
                .max().getAsInt();
        return maxValue;
    }

    private static Map<Character, Integer> getInitialLetterAndTheirUsageMap(io.vavr.collection.List<String> randomStringList) {
        Map<Character, Integer> mapForCountingUsedLetter = new HashMap<>();
        randomStringList.forEach(m -> {
            if (!mapForCountingUsedLetter.containsKey(m.charAt(0))) {
                mapForCountingUsedLetter.put(m.charAt(0), 1);
            } else {
                mapForCountingUsedLetter.replace(m.charAt(0), mapForCountingUsedLetter.get(m.charAt(0)), mapForCountingUsedLetter.get(m.charAt(0)) + 1);
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
