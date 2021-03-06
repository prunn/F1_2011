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
 * 
 * @author Prunn
 * copyright@Prunn2011
 * 
 */
package com.prunn.rfdynhud.widgets.prunn._util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.ctdp.rfdynhud.util.FontUtils;
import net.ctdp.rfdynhud.util.RFDHLog;
import net.ctdp.rfdynhud.widgets.WidgetsConfiguration;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;
import net.ctdp.rfdynhud.widgets.base.widget.WidgetPackage;
import net.ctdp.rfdynhud.widgets.base.widget.WidgetSet;

public class PrunnWidgetSetf1_2011 extends WidgetSet
{
    /*
     *  @author Prunn
     * copyright@Prunn2011
     */
    private PrunnWidgetSetf1_2011()
    {
        super( composeVersion( 1, 0, 0 ) );
    }
    public static final PrunnWidgetSetf1_2011 INSTANCE = new PrunnWidgetSetf1_2011();
    
    public static final WidgetPackage WIDGET_PACKAGE = new WidgetPackage( INSTANCE, "Prunn", INSTANCE.getIcon( "com/prunn/rfdynhud/widgets/prunn/prunn.png" ) );
    public static final WidgetPackage WIDGET_PACKAGE_F1_2011 = new WidgetPackage( INSTANCE, "Prunn/F1 2011", INSTANCE.getIcon( "com/prunn/rfdynhud/widgets/prunn/prunn.png" ), INSTANCE.getIcon( "com/prunn/rfdynhud/widgets/prunn/f1_2011.png" ) );
    public static final WidgetPackage WIDGET_PACKAGE_F1_2011_Race = new WidgetPackage( INSTANCE, "Prunn/F1 2011/Race", INSTANCE.getIcon( "com/prunn/rfdynhud/widgets/prunn/prunn.png" ), INSTANCE.getIcon( "com/prunn/rfdynhud/widgets/prunn/f1_2011.png" ), INSTANCE.getIcon( "com/prunn/rfdynhud/widgets/prunn/2011.png" ) );
    
    public static final String FONT_COLOR1_NAME = "FontColor1";
    public static final String FONT_COLOR2_NAME = "FontColor2";
    public static final String FONT_COLOR3_NAME = "FontColor3";
    public static final String FONT_COLOR4_NAME = "FontColor4";
    public static final String GAP_FONT_COLOR1_NAME = "GapFontColor1";
    public static final String GAP_FONT_COLOR2_NAME = "GapFontColor2";
    public static final String F1_2011_FONT_NAME = "f1_2011Font";
    public static final String POS_FONT_NAME = "PosFont";
    
    public String getDefaultNamedColorValue( String name )
    {
        if(name.equals("StandardFontColor"))
            return "#E9E9E9";
        if ( name.equals( FONT_COLOR1_NAME ) )
            return ( "#2D2D2D" );
        if ( name.equals( FONT_COLOR2_NAME ) )
            return ( "#EFEFEF" );
        if ( name.equals( FONT_COLOR3_NAME ) )
            return ( "#B21B16" );
        if ( name.equals( FONT_COLOR4_NAME ) )
            return ( "#A50000" );
        if ( name.equals( GAP_FONT_COLOR1_NAME ) )
            return ( "#FAFAFA" );
        if ( name.equals( GAP_FONT_COLOR2_NAME ) )
            return ( "#050505" );
        
        return ( null );
    }
    
    public String getDefaultNamedFontValue( String name )
    {
        if ( name.equals( F1_2011_FONT_NAME ) )
            return ( FontUtils.getFontString( "Dialog", 1, 24, true, true ) );
        if ( name.equals( POS_FONT_NAME ) )
            return ( FontUtils.getFontString( "Dialog", 1, 48, true, true ) );
        
        return ( null );
    }

    public static String ShortName( String driverName )
    {
        int sp = driverName.lastIndexOf( ' ' );
        if ( sp == -1 )
        {
            return ( driverName );
        }
        
        String sf = driverName.charAt( 0 ) + " " + driverName.substring( sp + 1 );
        
        return ( sf );
    }
    public static String generateShortTeamNames( String teamName, java.io.File getConfigFolder)
    {
        //open ini file
        File ini;
        //ini = new File(gameData.getFileSystem().getConfigFolder()GameFileSystem.INSTANCE.getConfigFolder(), "short_teams_names.ini");
        ini = new File(getConfigFolder, "short_teams_names.ini");
        
        if(ini.exists())
        {    
            try
            {
                int delimiter;
                String line;
                String fromFileTeam="";
                BufferedReader br = new BufferedReader( new FileReader( ini ) );
                
                while ((line = br.readLine()) != null)
                {   
                    delimiter = line.lastIndexOf( '=' );
                    
                    if(teamName.toUpperCase().equals(line.substring( 0, delimiter ).toUpperCase()))
                    {
                        fromFileTeam = line.substring( delimiter+1, line.length() );
                        br.close();
                        return fromFileTeam;
                    }
                }
                br.close();
            }
            catch ( Throwable t )
            {
               
            }
        }
        else
            RFDHLog.exception( "WARNING: No short_teams_names.ini found." );
        
        //check if team matches
        //else return same thing or cut the end if its too long
        return ( teamName );
    }
    public static String generateThreeLetterCode2( String driverName, java.io.File getConfigFolder )
    {
        
        //check if name is in ini file
        File ini;
        //ini = new File(gameData.getFileSystem().getConfigFolder(), "three_letter_codes.ini");
        ini = new File(getConfigFolder, "three_letter_codes.ini");
        
        
        if(ini.exists())
        {    
            try
            {
                int delimiter;
                String line;
                String fromFileTLC="";
                BufferedReader br = new BufferedReader( new FileReader( ini ) );
                
                while ((line = br.readLine()) != null)
                {   
                    delimiter = line.lastIndexOf( '=' );
                    
                    if(driverName.toUpperCase().equals(line.substring( 0, delimiter ).toUpperCase()))
                    {
                        fromFileTLC = line.substring( line.length() - 3, line.length() ).toUpperCase();
                        //RFDHLog.exception( "TLC:" + fromFileTLC ) ;
                        return fromFileTLC;
                    }
                           
                        
                }
                
            }
            catch ( Throwable t )
            {
               
            }
        }
        else
            RFDHLog.exception( "WARNING: No three_letter_codes.ini found." );
        
        
        if ( driverName.length() <= 3 )
        {
            return ( driverName.toUpperCase() );
        }
        
        int sp = driverName.lastIndexOf( ' ' );
        if ( sp == -1 )
        {
            return ( driverName.substring( 0, 3 ).toUpperCase() );
        }
        
        String tlc = driverName.substring( sp + 1, Math.min( sp + 4, driverName.length() ) ).toUpperCase();
        
        return ( tlc );
    }
    @SuppressWarnings( "unchecked" )
    public static final <W extends Widget> W getWidgetByClass( Class<W> clazz, boolean includeSubclasses, WidgetsConfiguration widgetsConfig )
    {
        int n = widgetsConfig.getNumWidgets();
        
        if ( includeSubclasses )
        {
            for ( int i = 0; i < n; i++ )
            {
                Widget w = widgetsConfig.getWidget( i );
                
                if ( clazz.isAssignableFrom( w.getClass() ) )
                    return ( (W)w );
            }
        }
        else
        {
            for ( int i = 0; i < n; i++ )
            {
                Widget w = widgetsConfig.getWidget( i );
                
                if ( clazz == w.getClass() )
                    return ( (W)w );
            }
        }
        
        return ( null );
    }
}
