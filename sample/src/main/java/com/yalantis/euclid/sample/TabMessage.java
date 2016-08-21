package com.yalantis.euclid.sample;

/**
 * Created by iiro on 7.6.2016.
 */
public class TabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.bb_menu_recents:
                message += "recents";
                break;
            case R.id.bb_menu_favorites:
                message += "favorites";
                break;
            case R.id.bb_menu_nearby:
                message += "nearby";
                break;
            case R.id.bb_menu_friends:
                message += "friends";
                break;
            case R.id.bb_menu_food:
                message += "food";
                break;
            case R.id.bb_menu_mine:
                message += "mine";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }

    public static int res2index(int menuItemId){
        int index = 0;
        switch (menuItemId) {
            case R.id.bb_menu_recents:
                index = 0;
                break;
            case R.id.bb_menu_favorites:
                index = 0;
                break;
            case R.id.bb_menu_nearby:
                index = 1;
                break;
            case R.id.bb_menu_friends:
                index = 2;
                break;
            case R.id.bb_menu_food:
                index = 0;
                break;
            case R.id.bb_menu_mine:
                index = 3;
                break;
        }
        return index;
    }
}
