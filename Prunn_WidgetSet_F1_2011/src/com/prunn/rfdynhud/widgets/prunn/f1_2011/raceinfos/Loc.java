/**
 * Copyright (C) 2009-2010 Cars and Tracks Development Project (CTDP).
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.prunn.rfdynhud.widgets.prunn.f1_2011.raceinfos;

import net.ctdp.rfdynhud.util.LocalizationsManager;

public class Loc
{
    private static final String t( String key )
    {
        return ( LocalizationsManager.INSTANCE.getLocalization( RaceInfosWidget.class, key ) );
    }
    
    public static final String pit_lane = t( "pit.lane" );
    public static final String pit_stop = t( "pit.stop" );
    public static final String fastest_lap = t( "fastest.lap" );
    public static final String winner = t( "winner" );
    
}
