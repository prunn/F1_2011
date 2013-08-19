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
package com.prunn.rfdynhud.widgets.prunn.f1_2011.resultmonitor;

import net.ctdp.rfdynhud.util.LocalizationsManager;

public class Loc
{
    private static final String t( String key )
    {
        return ( LocalizationsManager.INSTANCE.getLocalization( ResultMonitorWidget.class, key ) );
    }
    
    public static final String session_race = t( "session.race" );
    public static final String session_qualification = t( "session.qualification" );
    public static final String session_practice_1 = t( "session.practice_1" );
    public static final String session_practice_2 = t( "session.practice_2" );
    public static final String session_practice_3 = t( "session.practice_3" );
    public static final String session_practice_4 = t( "session.practice_4" );
    public static final String session_test = t( "session.test" );
    public static final String session_warmup = t( "session.warmup" );
    public static final String classification = t( "classification" );
    public static final String no_time = t( "no.time" );
    public static final String stop = t( "stop" );
    public static final String stops = t( "stops" );
    
}

