package com.example.duofingo;

import java.util.HashMap;
import java.util.Map;

public class TopicToIdMap {
    Map<String, Integer> map = new HashMap<>();
    public TopicToIdMap() {
        map.put("Budgeting", 1);
        map.put("Investing", 2);
        map.put("Taxes", 3);
        map.put("Debt", 4);
        map.put("Home Ownership", 5);
        map.put("Savings", 6);
        map.put("Net Worth", 7);
        map.put("Credit", 8);
    }
}
