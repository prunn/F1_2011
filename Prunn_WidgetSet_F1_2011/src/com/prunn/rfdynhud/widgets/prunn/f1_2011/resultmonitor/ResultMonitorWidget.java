package com.prunn.rfdynhud.widgets.prunn.f1_2011.resultmonitor;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.prunn.rfdynhud.plugins.tlcgenerator.StandardTLCGenerator;

import net.ctdp.rfdynhud.gamedata.FinishStatus;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.SessionType;
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
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.values.StringValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class ResultMonitorWidget extends Widget
{
    private DrawnString[] dsPos = null;
    private DrawnString[] dsName = null;
    private DrawnString[] dsTeam = null;
    private DrawnString[] dsTime = null;
    private DrawnString dsTrack = null;
    private DrawnString dsSession = null;
    
    private final ImagePropertyWithTexture imgTrack = new ImagePropertyWithTexture( "imgTrack", "prunn/f1_2011/data_title_long.png" );
    private final ImagePropertyWithTexture imgSession = new ImagePropertyWithTexture( "imgSession", "prunn/f1_2011/race_control.png" );
    private final ImagePropertyWithTexture imgFirst = new ImagePropertyWithTexture( "imgFirst", "prunn/f1_2011/gap_first.png" );
    private final ImagePropertyWithTexture imgPos = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/labeled_data_neutral.png" );
    private final ImagePropertyWithTexture imgTeam = new ImagePropertyWithTexture( "imgPosKnockOut", "prunn/f1_2011/data_caption.png" );
    private final ImagePropertyWithTexture imgTime = new ImagePropertyWithTexture( "imgPosKnockOut", "prunn/f1_2011/data_neutral.png" );
    
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor1 = new ColorProperty( "fontColor1", PrunnWidgetSetf1_2011.FONT_COLOR1_NAME );
    private final ColorProperty fontColor2 = new ColorProperty( "fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME );
    
    private final IntProperty numVeh = new IntProperty( "numberOfVehicles", 10 );
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    private IntProperty fontxposoffset = new IntProperty("X Position Font Offset", 0);
    private IntProperty fontxnameoffset = new IntProperty("X Name Font Offset", 0);
    private IntProperty fontxtimeoffset = new IntProperty("X Time Font Offset", 0);
    
    private IntValue[] positions = null;
    private StringValue[] driverNames = null;
    private StringValue[] driverTeam = null;
    private FloatValue[] gaps = null;
    StandardTLCGenerator gen = new StandardTLCGenerator();
    private BooleanProperty AbsTimes = new BooleanProperty("Use absolute times", false) ;
    
    
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onRealtimeEntered( gameData, isEditorMode );
        String cpid = "Y29weXJpZ2h0QFBydW5uMjAxMQ";
        if(!isEditorMode)
            log(cpid);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initSubTextures( LiveGameData gameData, boolean isEditorMode, int widgetInnerWidth, int widgetInnerHeight, SubTextureCollector collector )
    {
    }
    
    private void initValues()
    {
        int maxNumItems = numVeh.getValue();
        
        if ( ( positions != null ) && ( positions.length == maxNumItems ) )
            return;
        
        gaps = new FloatValue[maxNumItems];
        positions = new IntValue[maxNumItems];
        driverNames = new StringValue[maxNumItems];
        driverTeam = new StringValue[maxNumItems];
        
        for(int i=0;i < maxNumItems;i++)
        { 
            positions[i] = new IntValue();
            driverNames[i] = new StringValue();
            driverTeam[i] = new StringValue();
            gaps[i] = new FloatValue();
        }
        
        
    }
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
        int maxNumItems = numVeh.getValue();
        int fh = TextureImage2D.getStringHeight( "0%C", getFontProperty() );
        int rowHeight = height / (maxNumItems + 3);
        
        imgTrack.updateSize( width*80/100, rowHeight, isEditorMode );
        imgSession.updateSize( width*80/100, rowHeight, isEditorMode );
        imgFirst.updateSize( width*31/100, rowHeight, isEditorMode );
        imgPos.updateSize( width*31/100, rowHeight, isEditorMode );
        imgTeam.updateSize( width*33/100, rowHeight, isEditorMode );
        imgTime.updateSize( width*23/100, rowHeight, isEditorMode );
        Color whiteFontColor = fontColor2.getColor();
        
        dsPos = new DrawnString[maxNumItems];
        dsName = new DrawnString[maxNumItems];
        dsTeam = new DrawnString[maxNumItems];
        dsTime = new DrawnString[maxNumItems];
        
        
        int top = ( rowHeight - fh ) / 2;
        
        dsTrack = drawnStringFactory.newDrawnString( "dsTrack", width*5/100, top, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor1.getColor() );
        top += rowHeight;
        dsSession = drawnStringFactory.newDrawnString( "dsSession", width*3/100, top, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        top += rowHeight;
        top += rowHeight;
        
        for(int i=0;i < maxNumItems;i++)
        {
            dsPos[i] = drawnStringFactory.newDrawnString( "dsPos", width*7/200 + fontxposoffset.getValue(), top + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            dsName[i] = drawnStringFactory.newDrawnString( "dsName", width*8/100 + fontxnameoffset.getValue(), top + fontyoffset.getValue(), Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            dsTeam[i] = drawnStringFactory.newDrawnString( "dsTeam", width*40/100 + fontxnameoffset.getValue(), top + fontyoffset.getValue(), Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            dsTime[i] = drawnStringFactory.newDrawnString( "dsTime",  width*97/100 + fontxtimeoffset.getValue(), top + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            
            top += rowHeight;
        }
        
    }
    
    @Override
    protected Boolean updateVisibility( LiveGameData gameData, boolean isEditorMode )
    {
        super.updateVisibility( gameData, isEditorMode );
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        int drawncars = Math.min( scoringInfo.getNumVehicles(), numVeh.getValue() );
        initValues();
        
        for(int i=0;i < drawncars;i++)
        { 
            VehicleScoringInfo vsi = scoringInfo.getVehicleScoringInfo( i );
            
            if(vsi != null)
            {
                positions[i].update( vsi.getPlace( false ) );
                driverNames[i].update( gen.ShortName( vsi.getDriverNameShort()) );
                
                if(vsi.getVehicleInfo() != null)
                    driverTeam[i].update( gen.generateShortTeamNames( vsi.getVehicleInfo().getTeamName(), gameData.getFileSystem().getConfigFolder() ));
                else
                    driverTeam[i].update( vsi.getVehicleClass());
                
                //if(scoringInfo.getSessionType() != SessionType.RACE)
                    gaps[i].update(vsi.getBestLapTime());
                //else
                    //gaps[i].update(vsi.getNumPitstopsMade());
                    
                    
                
            }
        }
        return true;
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        int maxNumItems = numVeh.getValue();
        int drawncars = Math.min( scoringInfo.getNumVehicles(), maxNumItems );
        int rowHeight = height / (maxNumItems + 3);
        
        texture.clear( imgTrack.getTexture(), offsetX + width*2/100, offsetY, false, null );
        texture.clear( imgSession.getTexture(), offsetX, offsetY+rowHeight, false, null );
        
        texture.clear( imgFirst.getTexture(), offsetX, offsetY+rowHeight*3, false, null );
        texture.clear( imgTeam.getTexture(), offsetX + width*37/100, offsetY+rowHeight*3, false, null );
        texture.clear( imgTime.getTexture(), offsetX + width*77/100, offsetY+rowHeight*3, false, null );
          
        for(int i=1;i < drawncars;i++)
        {
            texture.clear( imgPos.getTexture(), offsetX, offsetY+rowHeight*(i+3), false, null );
            texture.clear( imgTeam.getTexture(), offsetX + width*37/100, offsetY+rowHeight*(i+3), false, null );
            texture.clear( imgTime.getTexture(), offsetX + width*77/100, offsetY+rowHeight*(i+3), false, null );
        }
        
    }
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        int drawncars = Math.min( scoringInfo.getNumVehicles(), numVeh.getValue() );
        String SessionName;
        //one time for leader
        
        if ( needsCompleteRedraw || clock.c())
        {
            switch(scoringInfo.getSessionType())
            {
                case RACE1: case RACE2: case RACE3: case RACE4:
                    SessionName = Loc.session_race;//"Race";
                    break;
                case QUALIFYING1: case QUALIFYING2: case QUALIFYING3:  case QUALIFYING4:
                    SessionName = Loc.session_qualification;//"Qualifying";
                    break;
                case PRACTICE1:
                    SessionName = Loc.session_practice_1;//"Practice 1";
                    break;
                case PRACTICE2:
                    SessionName = Loc.session_practice_2;//"Practice 2";
                    break;
                case PRACTICE3:
                    SessionName = Loc.session_practice_3;//"Practice 3";
                    break;
                case PRACTICE4:
                    SessionName = Loc.session_practice_4;//"Practice 4";
                    break;
                case TEST_DAY:
                    SessionName = Loc.session_test;//"Test";
                    break;
                case WARMUP:
                    SessionName = Loc.session_warmup;//"Warmup";
                    break;
                default:
                    SessionName = "";
                    break;
                        
            }
            //" Session Classification"
            dsTrack.draw( offsetX, offsetY, gameData.getTrackInfo().getTrackName(), texture);
            dsSession.draw( offsetX, offsetY, SessionName + " " + Loc.classification, texture);
            
            dsPos[0].draw( offsetX, offsetY, positions[0].getValueAsString(), texture );
            dsName[0].draw( offsetX, offsetY, driverNames[0].getValue(), texture );
            dsTeam[0].draw( offsetX, offsetY, driverTeam[0].getValue(), texture );
            
            if(scoringInfo.getSessionType() == SessionType.RACE1 )
            {
                String stops = ( scoringInfo.getLeadersVehicleScoringInfo().getNumPitstopsMade() > 1 ) ? " " + Loc.stops : " " + Loc.stop;
                dsTime[0].draw( offsetX, offsetY, scoringInfo.getLeadersVehicleScoringInfo().getNumPitstopsMade() + stops, texture);
            }
            else
                if(gaps[0].isValid())
                    dsTime[0].draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString(gaps[0].getValue() ) , texture);
                else
                    dsTime[0].draw( offsetX, offsetY, Loc.no_time, texture);
        
        
            // the other guys"No Time Set"
            for(int i=1;i < drawncars;i++)
            { 
                if ( needsCompleteRedraw || clock.c() )
                {
                    dsPos[i].draw( offsetX, offsetY, positions[i].getValueAsString(), fontColor2.getColor(), texture );
                    dsName[i].draw( offsetX, offsetY,driverNames[i].getValue() , texture );  
                    dsTeam[i].draw( offsetX, offsetY, driverTeam[i].getValue(), texture );
                    if(scoringInfo.getVehicleScoringInfo( i ).getFinishStatus() == FinishStatus.DQ)
                        dsTime[i].draw( offsetX, offsetY, "DQ", texture);
                    else
                        if(scoringInfo.getSessionType() == SessionType.RACE1 )
                        {
                            if(scoringInfo.getVehicleScoringInfo( i ).getFinishStatus() == FinishStatus.DNF)
                                dsTime[i].draw( offsetX, offsetY, "DNF", texture); 
                            else
                            {
                                String stops = ( scoringInfo.getVehicleScoringInfo( i ).getNumPitstopsMade() > 1 ) ? " " + Loc.stops : " " + Loc.stop;
                                dsTime[i].draw( offsetX, offsetY, scoringInfo.getVehicleScoringInfo( i ).getNumPitstopsMade() + stops, texture);
                            }
                        }
                        else
                            if(!gaps[i].isValid())
                                dsTime[i].draw( offsetX, offsetY, Loc.no_time, texture);
                            else
                                if(AbsTimes.getValue() || !gaps[0].isValid())
                                   dsTime[i].draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString(gaps[i].getValue() ), texture);
                                else
                                    dsTime[i].draw( offsetX, offsetY,"+ " + TimingUtil.getTimeAsLaptimeString(Math.abs( gaps[i].getValue() - gaps[0].getValue() )) , texture);
                 }
                
            }
        }
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( fontColor1, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( numVeh, "" );
        writer.writeProperty( AbsTimes, "" );
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( fontxposoffset, "" );
        writer.writeProperty( fontxnameoffset, "" );
        writer.writeProperty( fontxtimeoffset, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( fontColor1 ) );
        else if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( numVeh ) );
        else if ( loader.loadProperty( AbsTimes ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( fontxposoffset ) );
        else if ( loader.loadProperty( fontxnameoffset ) );
        else if ( loader.loadProperty( fontxtimeoffset ) );
    }
    
    @Override
    protected void addFontPropertiesToContainer( PropertiesContainer propsCont, boolean forceAll )
    {
        propsCont.addGroup( "Colors and Fonts" );
        
        super.addFontPropertiesToContainer( propsCont, forceAll );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( fontColor1 );
        propsCont.addProperty( fontColor2 );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Specific" );
        
        propsCont.addProperty( numVeh );
        propsCont.addProperty( AbsTimes );
        propsCont.addGroup( "Font Displacement" );
        propsCont.addProperty( fontyoffset );
        propsCont.addProperty( fontxposoffset );
        propsCont.addProperty( fontxnameoffset );
        propsCont.addProperty( fontxtimeoffset );
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
    
    public ResultMonitorWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 66.4f, 46.5f );
        
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
        getFontColorProperty().setColor( PrunnWidgetSetf1_2011.FONT_COLOR1_NAME );
    }
}
