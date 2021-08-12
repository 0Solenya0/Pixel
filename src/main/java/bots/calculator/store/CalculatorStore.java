package bots.calculator.store;

import bots.BotStore;
import client.store.Store;

import java.time.LocalDateTime;
import java.util.HashSet;

public class CalculatorStore extends BotStore {
    private static CalculatorStore instance;

    public synchronized static CalculatorStore getInstance() {
        if (instance == null)
            instance = new CalculatorStore();
        return instance;
    }

    public synchronized static void setInstance(CalculatorStore instance1) {
        instance = instance1;
    }

    @Override
    public String getDataSource() {
        return "./db/bots/calculator/data.json";
    }
}
