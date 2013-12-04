
package net.arcanerealm.snowattack;

import net.arcanerealm.snowattack.util.SLAPI;
import net.vectorgaming.varenas.ArenaAPI;
import net.vectorgaming.vcore.framework.VertexPlugin;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Kenny
 */
public class SnowAttack extends VertexPlugin
{
    private SnowAttackAPI api;
    @Override
    public void onEnable()
    {
        api = new SnowAttackAPI(this);
        ArenaAPI.registerArenaCreator("SNOW_ATTACK", new SnowAttackArenaCreator());
        SLAPI.load();
    }

    @Override
    public void onDisable()
    {
        
    }

    @Override
    public void setupCommands()
    {
        
    }

    @Override
    public Plugin getPlugin()
    {
        return this;
    }
}
