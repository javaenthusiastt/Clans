package me.liam.echoBoxClanSystem.helpers;

import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.configs.Config;

public class Prefixes {

    public static String getPrefix(){
        return Adventure.parseToLegacy(Config.getConfigPrefix());
    }
}
