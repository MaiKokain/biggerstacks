/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.config;

/**
 * Client side version of ServerConfig for use in single player.
 */
public class LocalConfig extends ServerConfig
{
    public final static LocalConfig INSTANCE = new LocalConfig();
    
    LocalConfig()
    {
        super(false);
    }
}
