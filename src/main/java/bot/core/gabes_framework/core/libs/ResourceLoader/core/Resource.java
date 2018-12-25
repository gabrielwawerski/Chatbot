package bot.core.gabes_framework.core.libs.ResourceLoader.core;

import java.util.prefs.Preferences;

/**
 * @since v0.3201
 */
public abstract class Resource {
    protected Preferences preferences = Preferences.userNodeForPackage(getClass());
}
