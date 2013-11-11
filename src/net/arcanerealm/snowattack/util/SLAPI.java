
package net.arcanerealm.snowattack.util;

import net.vectorgaming.varenas.ArenaManager;

/**
 *
 * @author Kenny
 */
public class SLAPI 
{
    public static void load()
    {
        for(String s : ArenaManager.getMaps())
        {
            if(ArenaManager.getArenaSettings(s).getType().equalsIgnoreCase("SNOW_ATTACK"))
            {
                 ArenaManager.getAreanFramework(s).addLocation("waiting", ArenaManager.getArenaConfig(s).getLocation("waiting"));
            }
        }
    }
}
