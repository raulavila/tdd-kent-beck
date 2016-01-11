package com.raulavila.money;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private Map<Pair, Integer> rates = new HashMap<Pair, Integer>();
    
    public int rate(String from, String to) {
        if(from.equals(to))
            return 1;
        
        return rates.get(new Pair(from, to));
    }

    public Money reduce(Expression source, String to) {
        return source.reduce(this, to);
    }

    public void addRate(String from, String to, int rate) {
        rates.put(new Pair(from, to), rate);
    }
}
