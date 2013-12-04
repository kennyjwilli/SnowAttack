
package net.arcanerealm.snowattack;

import net.vectorgaming.vcore.framework.VertexPlugin;


/**
 *
 * @author Kenny
 */
public class SnowAttackAPI
{
    private static VertexPlugin plugin;
    public SnowAttackAPI(SnowAttack instance)
    {
        plugin = instance;
    }
    
    public static VertexPlugin getPlugin()
    {
        return plugin;
    }
}
