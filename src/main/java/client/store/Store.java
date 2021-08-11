package client.store;

import shared.util.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Store {
    protected Config config = Config.getConfig("mainConfig");
}
