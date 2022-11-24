package com.bingshan.strategy;

import com.bingshan.judge.ConditionData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchStrategyFactoryTest {

    @Test
    void match() {
        Integer matchMode = 1;
        List<ConditionData> list = new ArrayList<>();
        ConditionData cd1 = new ConditionData();
        cd1.setParamType("uri");
        cd1.setOperator("match");
        cd1.setParamName("/");
        cd1.setParamValue("http/**");
        list.add(cd1);
        //MatchStrategyFactory.match(matchMode, list, exchange);
    }
}