package com.company;

import com.company.domain.ActionManager;
import com.company.domain.ActionManagerImp;
import com.company.ui.Menu;
import com.company.ui.MenuImp;

public class Main {

    public static void main(String[] args) {
        ActionManager actionManager = new ActionManagerImp();
        Menu menu = new MenuImp(actionManager);
        menu.mainMenu();

    }
}