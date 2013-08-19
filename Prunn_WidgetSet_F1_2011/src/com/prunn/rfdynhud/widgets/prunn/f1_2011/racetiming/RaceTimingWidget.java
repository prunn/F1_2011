package com.prunn.rfdynhud.widgets.prunn.f1_2011.racetiming;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.prunn.rfdynhud.plugins.tlcgenerator.StandardTLCGenerator;

import net.ctdp.rfdynhud.gamedata.FinishStatus;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.FontProperty;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.IntProperty;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.StandingsTools;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.values.StandingsView;
import net.ctdp.rfdynhud.values.StringValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class RaceTimingWidget extends Widget
{
    private TextureImage2D texData = null;
    private TextureImage2D texDriver = null;
    private TextureImage2D texDataFirst = null;
    private TextureImage2D texDriverFirst = null;
    private final ImagePropertyWithTexture imgData = new ImagePropertyWithTexture( "imgData", "prunn/f1_2011/race_gap.png" );
    private final ImagePropertyWithTexture imgDriver = new ImagePropertyWithTexture( "imgDriver", "prunn/f1_2011/tower/race_bg.png" );
    private final ImagePropertyWithTexture imgDataFirst = new ImagePropertyWithTexture( "imgDataFirst", "prunn/f1_2011/data_caption_small.png" );
    private final ImagePropertyWithTexture imgDriverFirst = new ImagePropertyWithTexture( "imgDriverFirst", "prunn/f1_2011/tower/race_bg_first.png" );
    
    private final ImagePropertyWithTexture imgBackground = new ImagePropertyWithTexture( "imgBackground", "prunn/f1_2011/timing_background_1.png" );
    private final ImagePropertyWithTexture imgBackground2 = new ImagePropertyWithTexture( "imgBackground2", "prunn/f1_2011/timing_background_2.png" );
    private final ImagePropertyWithTexture imgBackground3 = new ImagePropertyWithTexture( "imgBackground3", "prunn/f1_2011/timing_background_3.png" );
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor2 = new ColorProperty( "fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME );
    private final ColorProperty fontColor3 = new ColorProperty( "fontColor3", PrunnWidgetSetf1_2011.FONT_COLOR3_NAME );
    private DrawnString[] dsPos = null;
    private DrawnString[] dsName = null;
    private DrawnString[] dsTime = null;
    private IntProperty fontyoffset = new IntProperty("Y Font Offset Row 1", 0);
    private IntProperty fontyoffset2 = new IntProperty("Y Font Offset Row 2", 0);
    private IntProperty fontxposoffset = new IntProperty("X Position Font Offset", 0);
    private IntProperty fontxnameoffset = new IntProperty("X Name Font Offset", 0);
    private IntProperty fontxtimeoffset = new IntProperty("X Time Font Offset", 0);
    private final IntProperty numVeh = new IntProperty( "numberOfVehicles", 6 );
    private final IntValue currentSector = new IntValue();
    private final IntValue drawnCars = new IntValue();
    private VehicleScoringInfo[] vsis;
    private IntValue[] positions = null;
    private StringValue[] names = null;
    private StringValue[] gaps = null;
    private Boolean init = false;
    private IntValue cveh = new IntValue();
    private IntValue cpos = new IntValue();
    private IntValue rtime = new IntValue();
    private IntValue sessiontime = new IntValue();
    StandardTLCGenerator gen = new StandardTLCGenerator();
    private BooleanProperty relativeToCurrent = new BooleanProperty("Relative to viewed car", false);
    
    private BooleanProperty timeRefreshMode = new BooleanProperty("Time Mode",true);
    private IntProperty refreshTime = new IntProperty("Refresh Time", 10);
    
    
    
    
    /*@Override
    public WidgetPackage getWidgetPackage()
    {
        return ( PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011_Race );
    }
    
    @Override
    public String getDefaultNamedColorValue( String name )
    {
        return ( PrunnWidgetSetf1_2011.getDefaultNamedColorValue( name ) );
    }
    
    @Override
    public String getDefaultNamedFontValue(String name)
    {
        return ( PrunnWidgetSetf1_2011.getDefaultNamedFontValue( name ) );
    }*/
    
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onRealtimeEntered( gameData, isEditorMode );
        String cpid = "Y29weXJpZ2h0QFBydW5uMjAxMQ";
        if(!isEditorMode)
            log(cpid);
        drawnCars.reset();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initSubTextures( LiveGameData gameData, boolean isEditorMode, int widgetInnerWidth, int widgetInnerHeight, SubTextureCollector collector )
    {
    }
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
        int maxNumItems = numVeh.getValue();
        dsPos = new DrawnString[maxNumItems];
        dsName = new DrawnString[maxNumItems];
        dsTime = new DrawnString[maxNumItems];
        int fh = TextureImage2D.getStringHeight( "0%C", f1_2011Font );        
        int rowHeight = Math.round(height / 2 * 0.83f);
        
        imgBackground.updateSize( width*9/100, height, isEditorMode );
        imgBackground2.updateSize( width*83/100, height, isEditorMode );
        imgBackground3.updateSize( width*9/100, height, isEditorMode );
        
        texData = imgData.getImage().getScaledTextureImage( width/maxNumItems, rowHeight, texData, isEditorMode );
        texDriver = imgDriver.getImage().getScaledTextureImage( width/maxNumItems, rowHeight, texDriver, isEditorMode );
        texDataFirst = imgDataFirst.getImage().getScaledTextureImage( width/maxNumItems, rowHeight, texDataFirst, isEditorMode );
        texDriverFirst = imgDriverFirst.getImage().getScaledTextureImage( width/maxNumItems, rowHeight, texDriverFirst, isEditorMode );
        
        Color whiteFontColor = fontColor2.getColor();
        
        int top1 = height/2 - rowHeight/2 - fh/2 + fontyoffset.getValue();
        int top2 = height/2 + rowHeight/2 - fh/2 + fontyoffset2.getValue();
        int offx = width/10*6/maxNumItems;
        int offx2 = width*39/(100*maxNumItems);
        
        for(int i=0;i < maxNumItems;i++)
        { 
           dsPos[i] = drawnStringFactory.newDrawnString( "dsPos", offx2 + i*width/maxNumItems - i*width/100 + fontxposoffset.getValue(), top1, Alignment.CENTER, false, getFont(), isFontAntiAliased(), whiteFontColor );
           dsName[i] = drawnStringFactory.newDrawnString( "dsName", offx + i*width/maxNumItems - i*width/100 + fontxnameoffset.getValue(), top1, Alignment.LEFT, false, getFont(), isFontAntiAliased(), whiteFontColor );
           dsTime[i] = drawnStringFactory.newDrawnString( "dsTime", offx + i*width/maxNumItems - i*width/100 + fontxtimeoffset.getValue(), top2, Alignment.CENTER, false, getFont(), isFontAntiAliased(), whiteFontColor );
            
        }
        
    }
    private static final String getTimeAsGapString2( float gap )
    {
        if ( gap == 0f )
            return ( TimingUtil.getTimeAsLaptimeString( 0f ) );
        
        if ( gap < 0f )
            return ( "- " + TimingUtil.getTimeAsLaptimeString( -gap ) );
        
        return ( "+ " + TimingUtil.getTimeAsLaptimeString( gap ) );
    }
    private void initArrays()
    {
        int maxNumItems = numVeh.getValue();
        positions = new IntValue[maxNumItems];
        gaps = new StringValue[maxNumItems];
        names = new StringValue[maxNumItems];
        vsis = new VehicleScoringInfo[maxNumItems];
        
        for(int i=0;i < maxNumItems;i++)
        {
            positions[i] = new IntValue();
            positions[i].update( -1 );
            gaps[i] = new StringValue();
            gaps[i].update( "" );
            names[i] = new StringValue();
            names[i].update( "" );
        }
        init = true; 
    }
    @Override
    protected Boolean updateVisibility( LiveGameData gameData, boolean isEditorMode )
    {
        super.updateVisibility( gameData, isEditorMode );
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        if(!init)
            initArrays();
        
        currentSector.update( scoringInfo.getLeadersVehicleScoringInfo().getSector() );
        cveh.update(scoringInfo.getViewedVehicleScoringInfo().getDriverId());
        rtime.update( (int)((scoringInfo.getSessionNanos() / 1000000000f) % refreshTime.getValue()) );
        sessiontime.update( (int)(scoringInfo.getSessionNanos() / 1000000000f) );
        
        if( currentSector.hasChanged() || cveh.hasChanged() || refreshTime.getValue() == 0 || ( timeRefreshMode.getValue() && rtime.hasChanged() && rtime.getValue() == 0 ) || (refreshTime.getValue() == 1 && sessiontime.hasChanged()))
        {
            StandingsTools.getDisplayedVSIsForScoring( scoringInfo, scoringInfo.getViewedVehicleScoringInfo(), false, StandingsView.RELATIVE_TO_LEADER, false, vsis );
            int maxNumItems = Math.min( numVeh.getValue(), scoringInfo.getNumVehicles());
            for(int i=0;i < maxNumItems;i++)
            {
                VehicleScoringInfo vsi = vsis[i];
                positions[i].update( vsi.getPlace( false ) );
                names[i].update( gen.generateThreeLetterCode2( vsi.getDriverName(), gameData.getFileSystem().getConfigFolder() ) );
                if(vsi.getFinishStatus() == FinishStatus.DNF || vsi.getFinishStatus() == FinishStatus.DQ)
                    gaps[i].update( "OUT" );
                else
                    if(vsi.isInPits())
                        gaps[i].update( "PIT" );
                    else
                        if(relativeToCurrent.getValue() && scoringInfo.getViewedVehicleScoringInfo().getPlace( false ) > 1 && scoringInfo.getViewedVehicleScoringInfo().getLapsBehindLeader( false ) == 0)
                            gaps[i].update(getTimeAsGapString2( -(scoringInfo.getViewedVehicleScoringInfo().getTimeBehindLeader( false ) - vsi.getTimeBehindLeader( false ))) );
                        else
                            if( vsi.getPlace( false ) == 1 )
                                gaps[i].update( "Leader" );
                            else
                                if(vsi.getLapsBehindLeader( false ) > 0)
                                {
                                    String laps = ( vsi.getLapsBehindLeader( false ) > 1 ) ? " Laps" : " Lap";
                                    gaps[i].update( "+ " + String.valueOf( vsi.getLapsBehindLeader( false ) ) + laps);
                                }   
                                else
                                    gaps[i].update( "+ " + TimingUtil.getTimeAsLaptimeString( Math.abs(vsi.getTimeBehindLeader( false )) ));
                                
                
            }
            
            cpos.update(scoringInfo.getViewedVehicleScoringInfo().getPlace( false ));
            if(cpos.hasChanged() && !isEditorMode)
                forceCompleteRedraw( true );
        }
        
        return true;
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        int rowHeight = height / 2;
        int maxNumItems = numVeh.getValue();
        
        texture.clear( imgBackground.getTexture(), offsetX, offsetY, false, null );
        texture.clear( imgBackground2.getTexture(), offsetX + width*9/100, offsetY, false, null );
        texture.clear( imgBackground3.getTexture(), offsetX + width*91/100, offsetY, false, null );
        
        int offx = width*3/200;
        int offx2 = width*7/200;
        
        for(int i=0;i < maxNumItems;i++)
        {    
            if(positions[i].getValue() == 1)
            {
                texture.drawImage( texDriverFirst, offsetX + offx2 + i*width/maxNumItems - i*width/100, offsetY + rowHeight*18/100, true, null );
                texture.drawImage( texDataFirst, offsetX + offx + i*width/maxNumItems - i*width/100, offsetY + rowHeight*106/100, true, null );
            }
            else
                if(positions[i].getValue() != -1)
                {
                    if( gaps[i].getValue() != "OUT" )
                        texture.drawImage( texDriver, offsetX + offx2 + i*width/maxNumItems - i*width/100, offsetY + rowHeight*18/100, true, null );
                    else
                        texture.drawImage( texData, offsetX + offx2 + i*width/maxNumItems - i*width/100, offsetY + rowHeight*18/100, true, null );
                    
                    texture.drawImage( texData, offsetX + offx + i*width/maxNumItems - i*width/100, offsetY + rowHeight*106/100, true, null );
                }
        }
        
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        int maxNumItems = Math.min( numVeh.getValue(), scoringInfo.getNumVehicles());
        
        for(int i=0;i < maxNumItems;i++)
        { 
            if( needsCompleteRedraw || ( clock.c() && positions[i].hasChanged() ) )
            {
                if(positions[i].getValue() != -1 && gaps[i].getValue() != "OUT" )
                    dsPos[i].draw( offsetX, offsetY, String.valueOf(positions[i].getValue()), texture );
                else
                    dsPos[i].draw( offsetX, offsetY, "", texture );
            } 
            if( needsCompleteRedraw || ( clock.c() && names[i].hasChanged() ) )
                dsName[i].draw( offsetX, offsetY, names[i].getValue(), texture );
            if( needsCompleteRedraw || ( clock.c() && gaps[i].hasChanged() ) )
            {
                if(gaps[i].getValue() == "PIT")
                    dsTime[i].draw( offsetX, offsetY, gaps[i].getValue(), fontColor3.getColor(), texture );
                else
                    dsTime[i].draw( offsetX, offsetY, gaps[i].getValue(), fontColor2.getColor(),texture );
            }
        }
       
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );

        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( fontColor3, "" );
        writer.writeProperty( numVeh, "" );
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( fontyoffset2, "" );
        writer.writeProperty( fontxposoffset, "" );
        writer.writeProperty( fontxnameoffset, "" );
        writer.writeProperty( fontxtimeoffset, "" );
        writer.writeProperty( timeRefreshMode, "timeRefreshMode" );
        writer.writeProperty( refreshTime, "refreshTime" );
        writer.writeProperty( relativeToCurrent, "relativeToCurrent" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        
        if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( fontColor3 ) );
        else if ( loader.loadProperty( numVeh ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( fontyoffset2 ) );
        else if ( loader.loadProperty( fontxposoffset ) );
        else if ( loader.loadProperty( fontxnameoffset ) );
        else if ( loader.loadProperty( fontxtimeoffset ) );
        else if ( loader.loadProperty( timeRefreshMode ) );
        else if ( loader.loadProperty( refreshTime ) );
        else if ( loader.loadProperty( relativeToCurrent ) );
    }
    
    @Override
    protected void addFontPropertiesToContainer( PropertiesContainer propsCont, boolean forceAll )
    {
        propsCont.addGroup( "Colors and Fonts" );
        
        super.addFontPropertiesToContainer( propsCont, forceAll );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( fontColor2 );
        propsCont.addProperty( fontColor3 );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Specific" );
        propsCont.addProperty( timeRefreshMode );
        propsCont.addProperty( refreshTime );
        propsCont.addProperty( numVeh );
        propsCont.addGroup( "Font Displacement" );
        propsCont.addProperty( fontyoffset );
        propsCont.addProperty( fontyoffset2 );
        propsCont.addProperty( fontxposoffset );
        propsCont.addProperty( fontxnameoffset );
        propsCont.addProperty( fontxtimeoffset );
        propsCont.addProperty( relativeToCurrent );
    }
    
    @Override
    protected boolean canHaveBorder()
    {
        return ( false );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareForMenuItem()
    {
        super.prepareForMenuItem();
        
        getFontProperty().setFont( "Dialog", Font.PLAIN, 6, false, true );
    }
    
    public RaceTimingWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011_Race, 75.0f, 8.9f );
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
        getFontColorProperty().setColor( PrunnWidgetSetf1_2011.FONT_COLOR1_NAME );
    }
}
