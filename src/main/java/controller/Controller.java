package controller;

import db.Context;

public abstract class Controller {
    protected Context context;
    public Controller() {
        context = new Context();
    }
}
