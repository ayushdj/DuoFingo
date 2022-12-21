package com.example.duofingo;

import java.util.HashMap;
import java.util.Map;

public class TopicNameToChapterList {
    Map<String, String[]> map = new HashMap<>();

    String[] budgetingArray = new String[]{"50/30/20 budget", "Zero-base budget", "Pay yourself first", "Envelope system"};
    String[] investingArray = new String[]{"Investing1", "Investing2", "Investing3", "Investing4", "Investing5"};
    String[] taxArray = new String[]{"Tax1", "Tax2", "Tax3", "Tax4"};
    String[] debtArray = new String[]{"Revolving vs non-revolving debt", "Secured vs unsecured debt", "Understanding your debt"};
    String[] homeOwnershipArray = new String[]{"About Home ownership", "Buy only what you can afford", "Down payment", "Home emergency fund"};
    String[] savingsArray = new String[]{"Savings1", "Savings2", "Savings3", "Savings4", "Savings5"};
    String[] netWorthArray = new String[]{"Net Worth1", "Net Worth2", "Net Worth3"};
    String[] creditArray = new String[]{"What is Credit", "Credit report", "Credit score", "Importance of Credit"};

    public TopicNameToChapterList() {
        map.put("Budgeting", budgetingArray);
        map.put("Investing", investingArray);
        map.put("Taxes", taxArray);
        map.put("Debt", debtArray);
        map.put("Home Ownership", homeOwnershipArray);
        map.put("Savings", savingsArray);
        map.put("Net Worth", netWorthArray);
        map.put("Credit", creditArray);
    }
}
