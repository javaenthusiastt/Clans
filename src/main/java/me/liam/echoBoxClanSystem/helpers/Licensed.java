package me.liam.echoBoxClanSystem.helpers;

import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.data.Keys;

public class Licensed {

    public static boolean isNotLicensed() {
        return Keys.isValidKey(Main.getInstance().getConfig().getString("key"));
    }

}
