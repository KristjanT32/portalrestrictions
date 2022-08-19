package krisapps.portalrestrictions.portalrestrictions.language;

import org.apache.commons.lang.ArrayUtils;

public enum LocalizationManager {

    //Locales
    RESTRICTION_ALREADY_SET("&e[&bRestrictionManager&e] &cSorry, but another restriction of this type is already active.\nYou can view it with &e/viewrestriction &b%target%&c."),
    RESTRICTION_SET_SUCCESS("&e[&bRestrictionManager&e] &aSuccessfully set restriction of type &b%type%&a."),
    RESTRICTION_NOT_SET("&e[&bRestrictionManager&e] &cIt appears that no restrictions have been set for &b%type%&c."),
    RESTRICTION_REMOVE_SUCCESS("&e[&bRestrictionManager&e] &aRestriction for %target% removed!"),
    UNSUPPORTED_RESTRICTION_TARGET("&e[&bRestrictionManager&e] &cUnknown restriction type: &b%target%"),

    RESTRICTED_ENTERPORTAL_PLAYER("&cSorry, but entering portals has been restricted for you."),
    RESTRICTED_ENTERPORTAL_GLOBAL("&cSorry, but entering portals has been restricted."),

    RESTRICTED_CREATEPORTAL_GLOBAL("&cSorry, but creating portals has been restricted."),

    SHOW_RESTRICTION_HEADER("&e[&bRestrictionManager&e] &aShowing restriction of type &b%type%&a:"),
    SHOW_GLOBALRESTRICTION_BODY("&e=======================================\n&eRestriction for &l&b%target%&e\nRestriction Type: &b%type%&e\nRestriction Duration: &b%duration% day(s)&e\nEffective Context: &b%context%\n&eRestriction Issued On: &b%date%\n&e======================================="),
    SHOW_SPECIALRESTRICTION_BODY("&e=======================================\n&eRestriction for &l&d%target%&e\nRestriction Type: &b%type%&e\nRestriction Duration: &b%duration% day(s)&e\nEffective Context: &b%context%&e\nRestricted for: &b%players%\n&eRestriction Issued On: &b%date%\n&e======================================="),

    RESTRICTION_REMOVED_SCHEDULE("&e[&bRestrictionManager&e] Restriction of type &b%target%&e &d(&b%type%&d)&e is no longer active."),

    //Errors
    SAVE_ERROR("&e[&bRestrictionManager&e] &cAn error has occurred while saving the data."),
    INVALID_SYNTAX("&cInvalid syntax."),



    ;

    private String message;
    private LocalizationManager(String message){
        this.message = message;
    }

    public String getString(){
        return this.message;
    }




}
