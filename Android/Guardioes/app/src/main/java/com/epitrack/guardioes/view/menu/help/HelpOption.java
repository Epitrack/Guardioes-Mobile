package com.epitrack.guardioes.view.menu.help;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;

public enum HelpOption implements IMenu {

    TUTORIAL    (1, R.string.tutorial, R.drawable.icon_tutorial),
    TERM        (2, R.string.term, R.drawable.icon_term),
    REPORT      (3, R.string.report, R.drawable.icon_report);

    private final int id;
    private final int name;
    private final int icon;

    HelpOption(final int id, final int name, final int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public final int getId() {
        return id;
    }

    public final int getName() {
        return name;
    }

    public final int getIcon() {
        return icon;
    }

    public static HelpOption getBy(final int id) {

        for (final HelpOption help : HelpOption.values()) {

            if (help.getId() == id) {
                return help;
            }
        }

        throw new IllegalArgumentException("The HelpOption has not found.");
    }
}
