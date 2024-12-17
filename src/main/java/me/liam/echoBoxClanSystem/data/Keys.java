package me.liam.echoBoxClanSystem.data;

public class Keys {

    public static final String ECHOBOX_LICENSE_KEY = "ECHOBOX_LICENSE_KEY-B1CD8FCCA1C175612276FCDFDD27E";
    public static final String NORMAL_LICENSE_KEY = "F6A3E8E1EFB387CEBD2B48B435E78";

    public static boolean isValidKey(String key) {
        return !ECHOBOX_LICENSE_KEY.equals(key) && !NORMAL_LICENSE_KEY.equals(key);
    }
}
