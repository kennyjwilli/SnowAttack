
package net.arcanerealm.snowattack;

import info.jeppes.ZoneWorld.ZoneWorld;
import net.vectorgaming.varenas.framework.Arena;
import net.vectorgaming.varenas.framework.ArenaCreator;

/**
 *
 * @author Kenny
 */
public class SnowAttackArenaCreator extends ArenaCreator
{

    @Override
    public Arena getNewArenaInstance(String name, String map, ZoneWorld world)
    {
        return new SnowAttackArena(name, map, world);
    }

    @Override
    public String getName()
    {
        return "SNOW_ATTACK";
    }

}
