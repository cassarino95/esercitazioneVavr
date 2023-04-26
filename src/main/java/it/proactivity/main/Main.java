package it.proactivity.main;

import io.vavr.Tuple2;
import io.vavr.Tuple4;
import io.vavr.control.Option;
import it.proactivity.logic.MyLogic;
import it.proactivity.model.MyModel;


import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<Option<MyModel>> myModelList = MyLogic.createListByUserInput(3);
        Tuple2<List<Integer>, List<String>> tuple2 = MyLogic.readAndSplitMyModelList(myModelList);
        Tuple4<List<Integer>, List<String>, Double, Double> tuple4 = MyLogic.easyStatsOnMyModelList(myModelList);

        io.vavr.collection.Queue<Option<MyModel>> vavrListFromUserInput = MyLogic.createVavrQueueByUserInput(3);
    }
}
