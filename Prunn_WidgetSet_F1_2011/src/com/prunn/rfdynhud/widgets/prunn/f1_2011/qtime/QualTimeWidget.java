package com.prunn.rfdynhud.widgets.prunn.f1_2011.qtime;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.prunn.rfdynhud.plugins.tlcgenerator.StandardTLCGenerator;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

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
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.EnumValue;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class QualTimeWidget extends Widget
{
    private static enum Situation
    {
        LAST_SECONDS_OF_SECTOR_1,
        SECTOR_1_FINISHED_BEGIN_SECTOR_2,
        LAST_SECONDS_OF_SECTOR_2,
        SECTOR_2_FINISHED_BEGIN_SECTOR_3,
        LAST_SECONDS_OF_SECTOR_LAP,
        LAP_FINISHED_BEGIN_NEW_LAP,
        OTHER,
        ;
    }
    
    private static final float SECTOR_DELAY = 5f;
    
    private DrawnString dsPos = null;
    private DrawnString dsPosFrom = null;
    private DrawnString dsName = null;
    private DrawnString dsTime = null;
    private DrawnString dsGap = null;
    
    private TextureImage2D texPos = null;
    private final ImagePropertyWithTexture imgPos = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/big_position_neutral.png" );
    private final ImagePropertyWithTexture imgPosFromFirst = new ImagePropertyWithTexture( "imgPosFromFirst", "prunn/f109/pos1.png" );
    private final ImagePropertyWithTexture imgPosFrom = new ImagePropertyWithTexture( "imgPosFrom", "prunn/f109/pos.png" );
    private final ImagePropertyWithTexture imgPosFirst = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/big_position_first.png" );
    private final ImagePropertyWithTexture imgPosOut = new ImagePropertyWithTexture( "imgPosOut", "prunn/f1_2011/big_position_knockout.png" );
    private final ImagePropertyWithTexture imgName = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgTime = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgTimeBlack = new ImagePropertyWithTexture( "imgTimeBlack", "prunn/f1_2011/gap_first.png" );
    private final ImagePropertyWithTexture imgTimeBlackKnockOut = new ImagePropertyWithTexture( "imgTimeBlackKnockOut", "prunn/f1_2011/labeled_data_neutral.png" );
    private final ImagePropertyWithTexture imgTimeGreen = new ImagePropertyWithTexture( "imgTimeGreen", "prunn/f1_2011/gap_faster.png" );
    private final ImagePropertyWithTexture imgTimeGreenFinish = new ImagePropertyWithTexture( "imgTimeGreenFinish", "prunn/f1_2011/data_fastest.png" );
    private final ImagePropertyWithTexture imgTimeGreenKnockOut = new ImagePropertyWithTexture( "imgTimeGreenKnockOut", "prunn/f1_2011/labeled_data_green.png" );
    private final ImagePropertyWithTexture imgTimeYellow = new ImagePropertyWithTexture( "imgTimeYellow", "prunn/f1_2011/gap_slower.png" );
    private final ImagePropertyWithTexture imgTimeYellowFinish = new ImagePropertyWithTexture( "imgTimeYellowFinish", "prunn/f1_2011/data_slower.png" );
    private final ImagePropertyWithTexture imgTimeYellowKnockOut = new ImagePropertyWithTexture( "imgTimeYellowKnockOut", "prunn/f1_2011/labeled_data_yellow.png" );
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    StandardTLCGenerator gen = new StandardTLCGenerator();
    
    
    private final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    private final FontProperty posFont = new FontProperty("positionFont", PrunnWidgetSetf1_2011.POS_FONT_NAME);
    private final ColorProperty fontColor1 = new ColorProperty("fontColor1", PrunnWidgetSetf1_2011.FONT_COLOR1_NAME);
    
    private final IntProperty posKnockout = new IntProperty("positionForKnockOut", "posKnockout", 10);
    private final ColorProperty KnockoutFontColor = new ColorProperty("Knockout Font Color", PrunnWidgetSetf1_2011.FONT_COLOR4_NAME);
    protected final BooleanProperty forcePlayer = new BooleanProperty("Force to player", "forcePlayer", false);
    private BooleanProperty uppercasename = new BooleanProperty("uppercase name",true); 
    
    private final EnumValue<Situation> situation = new EnumValue<Situation>();
    private final IntValue leaderID = new IntValue();
    private final IntValue leaderPos = new IntValue();
    private final IntValue ownPos = new IntValue();
    private float leadsec1 = -1f;
    private float leadsec2 = -1f;
    private float leadlap = -1f;
    private final FloatValue cursec1 = new FloatValue(-1f, 0.001f);
    private final FloatValue cursec2 = new FloatValue(-1f, 0.001f);
    private final FloatValue curlap = new FloatValue(-1f, 0.001f);
    private final FloatValue oldbesttime = new FloatValue(-1f, 0.001f);
    private final FloatValue gapOrTime = new FloatValue(-1f, 0.001f);
    private final FloatValue lastLaptime = new FloatValue(-1f, 0.001f);
    private final FloatValue fastestlap = new FloatValue(-1f, 0.001f);
    private final FloatValue knockoutlap = new FloatValue(-1f, 0.001f);
    private final BoolValue gapAndTimeInvalid = new BoolValue();
    private float oldbest = 0;
    private int gapRightOffset = 0;
    private static Boolean isvisible = false;
    public static Boolean visible()
    {
        return isvisible;
    }
    /*@Override
    public String getDefaultNamedColorValue( String name )
    {
        if(name.equals("KnockoutFontColor"))
            return "#DA1C19";
        
        return ( PrunnWidgetSetf1_2011.getDefaultNamedColorValue( name ) );
    }
    
    @Override
    public String getDefaultNamedFontValue(String name)
    {
        return ( PrunnWidgetSetf1_2011.getDefaultNamedFontValue( name ) );
    }
    
    @Override
    public WidgetPackage getWidgetPackage()
    {
        return ( PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011 );
    }*/
    
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onCockpitEntered( gameData, isEditorMode );
        String cpid = "Y29weXJpZ2h0QFBydW5uMjAxMQ";
        if(!isEditorMode)
            log(cpid);
        situation.reset();
        leaderID.reset();
        leaderPos.reset();
        ownPos.reset();
        cursec1.reset();
        cursec2.reset();
        curlap.reset();
        oldbesttime.reset();
        gapOrTime.reset();
        lastLaptime.reset();
        gapAndTimeInvalid.reset();
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
        gapRightOffset = TextureImage2D.getStringWidth( "  ", getFontProperty() );
        
        int rowHeight = height / 3;
        int fh = TextureImage2D.getStringHeight( "09gy", getFontProperty() );
        int posfh = TextureImage2D.getStringHeight( "0", posFont );
        
        texPos = imgPos.getImage().getScaledTextureImage( rowHeight *290/100, rowHeight *2, texPos, isEditorMode );
        imgPos.updateSize( rowHeight *290/100, rowHeight *2, isEditorMode );
        imgPosOut.updateSize( rowHeight *290/100, rowHeight *2, isEditorMode );
        imgPosFirst.updateSize( rowHeight *290/100, rowHeight *2, isEditorMode );
        imgPosFrom.updateSize( rowHeight, rowHeight, isEditorMode );
        imgPosFromFirst.updateSize( rowHeight, rowHeight, isEditorMode );
        imgName.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTime.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeBlack.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeBlackKnockOut.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeGreen.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeGreenFinish.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeGreenKnockOut.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeYellow.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeYellowFinish.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        imgTimeYellowKnockOut.updateSize( width - rowHeight *3, rowHeight, isEditorMode );
        
        Color blackFontColor = getFontColor();
        Color whiteFontColor = fontColor2.getColor();
        
        int textOff = ( rowHeight - fh ) / 2;
        
        dsName = drawnStringFactory.newDrawnString( "dsName", imgName.getTexture().getWidth(), textOff + fontyoffset.getValue(), Alignment.RIGHT, false, getFont(), isFontAntiAliased(), whiteFontColor);
        dsTime = drawnStringFactory.newDrawnString( "dsTime", imgName.getTexture().getWidth()-10, rowHeight + textOff + fontyoffset.getValue(), Alignment.RIGHT, false, getFont(), isFontAntiAliased(), whiteFontColor);
        dsPos = drawnStringFactory.newDrawnString( "dsPos", imgName.getTexture().getWidth() + imgPos.getTexture().getWidth()/2 + width*1/100, imgPos.getTexture().getWidth()/3 - posfh/2 + 2 + fontyoffset.getValue(), Alignment.CENTER, false, posFont.getFont(), isFontAntiAliased(), whiteFontColor);
        dsPosFrom = drawnStringFactory.newDrawnString( "dsPosFrom", rowHeight *3 / 4, rowHeight * 2 + textOff + fontyoffset.getValue(), Alignment.CENTER, false, getFont(), isFontAntiAliased(), whiteFontColor );
        dsGap = drawnStringFactory.newDrawnString( "dsTime", imgName.getTexture().getWidth()-10 - gapRightOffset, rowHeight * 2 + textOff + fontyoffset.getValue(), Alignment.RIGHT, false, getFont(), isFontAntiAliased(), blackFontColor);
        
    }
    
    private VehicleScoringInfo getLeaderCarInfos( ScoringInfo scoringInfo )
    {
        VehicleScoringInfo currentcarinfos = scoringInfo.getViewedVehicleScoringInfo();
        if(forcePlayer.getValue())
            currentcarinfos = scoringInfo.getPlayersVehicleScoringInfo();
        if(posKnockout.getValue() <= 2 || posKnockout.getValue() > scoringInfo.getNumVehicles() || currentcarinfos.getPlace( false )+2 < posKnockout.getValue() || scoringInfo.getVehicleScoringInfo( posKnockout.getValue()-1 ).getBestLapTime() < 0)
        {
            return ( scoringInfo.getLeadersVehicleScoringInfo() );
            
        }
        
        return ( scoringInfo.getVehicleScoringInfo( posKnockout.getValue()-1 ) );
    }
    
    private void updateSectorValues( ScoringInfo scoringInfo )
    {
        VehicleScoringInfo currentcarinfos = scoringInfo.getViewedVehicleScoringInfo();
        VehicleScoringInfo leadercarinfos = getLeaderCarInfos( scoringInfo );
        if(forcePlayer.getValue())
            currentcarinfos = scoringInfo.getPlayersVehicleScoringInfo();
        
        if(leadercarinfos.getFastestLaptime() != null && leadercarinfos.getFastestLaptime().getLapTime() >= 0)
        {
            leadsec1 = leadercarinfos.getFastestLaptime().getSector1();
            leadsec2 = leadercarinfos.getFastestLaptime().getSector1And2();
            leadlap = leadercarinfos.getFastestLaptime().getLapTime();
        }
        else
        {
            leadsec1 = 0f;
            leadsec2 = 0f;
            leadlap = 0f;
        }
        
        cursec1.update( currentcarinfos.getCurrentSector1() );
        cursec2.update( currentcarinfos.getCurrentSector2( true ) );

        if ( scoringInfo.getSessionTime() > 0f )
            curlap.update( currentcarinfos.getCurrentLaptime() );
        else
            curlap.update( scoringInfo.getSessionNanos() / 1000000000f - currentcarinfos.getLapStartTime() ); 
            

    }
    
    private boolean updateSituation( VehicleScoringInfo currentcarinfos )
    {
        final byte sector = currentcarinfos.getSector();
        
        if(sector == 1 && curlap.getValue() > leadsec1 - SECTOR_DELAY && leadlap > 0)
        {
            situation.update( Situation.LAST_SECONDS_OF_SECTOR_1 );
        }
        else if(sector == 2 && curlap.getValue() - cursec1.getValue() <= SECTOR_DELAY && leadlap > 0)
        {
            situation.update( Situation.SECTOR_1_FINISHED_BEGIN_SECTOR_2 );
        }
        else if(sector == 2  && curlap.getValue() > leadsec2 - SECTOR_DELAY && leadlap > 0)
        {
            situation.update( Situation.LAST_SECONDS_OF_SECTOR_2 );
        }
        else if(sector == 3 && curlap.getValue() - cursec2.getValue() <= SECTOR_DELAY && leadlap > 0)
        {
            situation.update( Situation.SECTOR_2_FINISHED_BEGIN_SECTOR_3 );
        }
        else if(sector == 3 && curlap.getValue() > leadlap - SECTOR_DELAY && leadlap > 0)
        {
            situation.update( Situation.LAST_SECONDS_OF_SECTOR_LAP );
        }
        else if(sector == 1 && curlap.getValue() <= SECTOR_DELAY && currentcarinfos.getLastLapTime() > 0)
        {
            situation.update( Situation.LAP_FINISHED_BEGIN_NEW_LAP );
        }
        else
        {
            situation.update( Situation.OTHER );
        }
        
        return ( situation.hasChanged() );
    }
    
    @Override
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        super.updateVisibility(gameData, isEditorMode);
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        updateSectorValues( scoringInfo );
        VehicleScoringInfo currentcarinfos = scoringInfo.getViewedVehicleScoringInfo();
        if(forcePlayer.getValue())
            currentcarinfos = scoringInfo.getPlayersVehicleScoringInfo();
        
        
        
        fastestlap.update(scoringInfo.getLeadersVehicleScoringInfo().getBestLapTime());
        if( gameData.getScoringInfo().getNumVehicles() >= posKnockout.getValue() )
            knockoutlap.update(scoringInfo.getVehicleScoringInfo( posKnockout.getValue()-1 ).getBestLapTime());
        
        if ( (updateSituation( currentcarinfos )  || fastestlap.hasChanged() || knockoutlap.hasChanged()) && !isEditorMode)
            forceCompleteRedraw( true );
        
        if ( currentcarinfos.isInPits() )
        {
            isvisible = false;
            return false;
        }
        
        if(currentcarinfos.getFinishStatus() == FinishStatus.FINISHED && situation.getValue() != Situation.LAP_FINISHED_BEGIN_NEW_LAP )
            return false;
        
        float curLaptime;
        if ( scoringInfo.getSessionTime() > 0f )
            curLaptime = currentcarinfos.getCurrentLaptime();
        else
            curLaptime = scoringInfo.getSessionNanos() / 1000000000f - currentcarinfos.getLapStartTime();
        
        if ( curLaptime > 0f )
        {
            isvisible = true;
            //forceCompleteRedraw( true );
            return true;
        }
            
        return false;
         
    }
    
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        VehicleScoringInfo currentcarinfos = gameData.getScoringInfo().getViewedVehicleScoringInfo();
        VehicleScoringInfo leadercarinfos = getLeaderCarInfos( scoringInfo );
        if(forcePlayer.getValue())
            currentcarinfos = scoringInfo.getPlayersVehicleScoringInfo();
        
        int rowHeight = height / 3;
        
        texture.clear( imgName.getTexture(), offsetX + width *8/100, offsetY, false, null );
        texture.clear( imgTime.getTexture(), offsetX + width *4/100, offsetY + rowHeight, false, null );
        
        switch ( situation.getValue() )
        {
            case LAST_SECONDS_OF_SECTOR_1:
                if(leadercarinfos.getPlace( false ) == 1)
                    texture.clear( imgTimeBlack.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                else
                    texture.clear( imgTimeBlackKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                
                break;
                
            case SECTOR_1_FINISHED_BEGIN_SECTOR_2:
                if( cursec1.getValue() <= leadsec1 )
                {
                    if(leadercarinfos.getPlace( false ) == 1)
                       texture.clear( imgTimeGreen.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    else
                       texture.clear( imgTimeGreenKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                }
                else
                {
                    if(leadercarinfos.getPlace( false ) == 1)
                        texture.clear( imgTimeYellow.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    else
                        texture.clear( imgTimeYellowKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                }
                break;
                
            case LAST_SECONDS_OF_SECTOR_2:
                if(leadercarinfos.getPlace( false ) == 1)
                    texture.clear( imgTimeBlack.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                else
                    texture.clear( imgTimeBlackKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                
                break;
                
            case SECTOR_2_FINISHED_BEGIN_SECTOR_3:
                if( cursec2.getValue() <= leadsec2 )
                {
                    if(leadercarinfos.getPlace( false ) == 1)
                        texture.clear( imgTimeGreen.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    else
                        texture.clear( imgTimeGreenKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                }
                else
                {
                    if(leadercarinfos.getPlace( false ) == 1)
                        texture.clear( imgTimeYellow.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    else
                        texture.clear( imgTimeYellowKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                }  
                break;
                
            case LAST_SECONDS_OF_SECTOR_LAP:
                if(leadercarinfos.getPlace( false ) == 1)
                    texture.clear( imgTimeBlack.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                else
                    texture.clear( imgTimeBlackKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                
                break;
                
            case LAP_FINISHED_BEGIN_NEW_LAP:
                if( currentcarinfos.getLastLapTime() <= leadlap )
                {
                    if(leadercarinfos.getPlace( false ) == 1)
                        texture.clear( imgTimeGreenFinish.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    else
                        texture.clear( imgTimeGreenKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                }
                else
                    if( currentcarinfos.getPlace( false ) == 1)
                        texture.clear( imgTimeYellowFinish.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    else
                    {
                        if(leadercarinfos.getPlace( false ) == 1)
                            texture.clear( imgTimeYellow.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                        else
                            texture.clear( imgTimeYellowKnockOut.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    }
                
                     
                if(currentcarinfos.getPlace( false ) == 1)
                    texPos = imgPosFirst.getImage().getScaledTextureImage( rowHeight *290/100, rowHeight *2, texPos, isEditorMode );
                else 
                    if(currentcarinfos.getPlace( false ) <= posKnockout.getValue())
                        texPos = imgPos.getImage().getScaledTextureImage( rowHeight *290/100, rowHeight *2, texPos, isEditorMode );
                    else
                        texPos = imgPosOut.getImage().getScaledTextureImage( rowHeight *290/100, rowHeight *2, texPos, isEditorMode );
                    
                texture.drawImage( texPos, offsetX + width - imgPos.getTexture().getWidth(), offsetY, true, null );
                        
                break;
                
            
        }
    }
    
    private static final String getTimeAsGapString2( float gap )
    {
        if ( gap == 0f )
            return ( "- " + TimingUtil.getTimeAsLaptimeString( 0f ) );
        
        if ( gap < 0f )
            return ( "- " + TimingUtil.getTimeAsLaptimeString( -gap ) );
        
        return ( "+ " + TimingUtil.getTimeAsLaptimeString( gap ) );
    }
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        updateSectorValues( scoringInfo );
        
        VehicleScoringInfo currentcarinfos = scoringInfo.getViewedVehicleScoringInfo();
        VehicleScoringInfo leadercarinfos = getLeaderCarInfos( scoringInfo );
        
        if(forcePlayer.getValue())
            currentcarinfos = scoringInfo.getPlayersVehicleScoringInfo();
            
        leaderID.update( leadercarinfos.getDriverId() );
        leaderPos.update( leadercarinfos.getPlace( false ) );
        
        if ( needsCompleteRedraw || ( clock.c() && leaderID.hasChanged() ) )
        {
            if( uppercasename.getValue() )
                dsName.draw( offsetX, offsetY, gen.ShortName( currentcarinfos.getDriverNameShort().toUpperCase()), texture );
            else
                dsName.draw( offsetX, offsetY, gen.ShortName( currentcarinfos.getDriverNameShort()), texture );
            
        }
        
        
        switch ( situation.getValue() )
        {
            case LAST_SECONDS_OF_SECTOR_1:
                gapAndTimeInvalid.update( false );
                gapOrTime.update( leadsec1 );
                
                if ( needsCompleteRedraw || ( clock.c() && gapOrTime.hasChanged() ) )
                    dsGap.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( gapOrTime.getValue() ), fontColor2.getColor() , texture);
                if ( needsCompleteRedraw || ( clock.c() && leaderPos.hasChanged() ) )
                    dsPosFrom.draw( offsetX, offsetY, leaderPos.getValueAsString(), texture );  
                if ( needsCompleteRedraw || ( clock.c() && curlap.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( curlap.getValue(), false, false, true, false ) + "    ", texture);
                break;
                
            case SECTOR_1_FINISHED_BEGIN_SECTOR_2:
                gapAndTimeInvalid.update( false );
                gapOrTime.update( cursec1.getValue() - leadsec1 );
                
                if ( needsCompleteRedraw || ( clock.c() && gapOrTime.hasChanged() ) )
                    dsGap.draw( offsetX, offsetY, getTimeAsGapString2( gapOrTime.getValue() ), ( gapOrTime.getValue() <= 0 ) ? fontColor2.getColor() : fontColor1.getColor() , texture);
                if ( needsCompleteRedraw || ( clock.c() && leaderPos.hasChanged() ) )
                    dsPosFrom.draw( offsetX, offsetY, leaderPos.getValueAsString(), texture );
                if ( needsCompleteRedraw || ( clock.c() && cursec1.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( cursec1.getValue(), false, false, true, true ) , texture);
                break;
                
            case LAST_SECONDS_OF_SECTOR_2:
                gapAndTimeInvalid.update( false );
                gapOrTime.update( leadsec2 );
                
                if ( needsCompleteRedraw || ( clock.c() && gapOrTime.hasChanged() ) )
                    dsGap.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( leadsec2 ), fontColor2.getColor() , texture);
                if ( needsCompleteRedraw || ( clock.c() && leaderPos.hasChanged() ) )
                    dsPosFrom.draw( offsetX, offsetY, leaderPos.getValueAsString(), texture );
                if ( needsCompleteRedraw || ( clock.c() && curlap.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( curlap.getValue(), false, false, true, false ) + "    ", texture);
                break;
                
            case SECTOR_2_FINISHED_BEGIN_SECTOR_3:
                gapAndTimeInvalid.update( false );
                gapOrTime.update( cursec2.getValue() - leadsec2 );
                
                if ( needsCompleteRedraw || ( clock.c() && gapOrTime.hasChanged() ) )
                    dsGap.draw( offsetX, offsetY, getTimeAsGapString2( gapOrTime.getValue() ), ( gapOrTime.getValue() <= 0 ) ? fontColor2.getColor() : fontColor1.getColor() , texture);
                if ( needsCompleteRedraw || ( clock.c() && leaderPos.hasChanged() ) )
                    dsPosFrom.draw( offsetX, offsetY, leaderPos.getValueAsString(), texture );
                if ( needsCompleteRedraw || ( clock.c() && cursec2.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( cursec2.getValue(), false, false, true, true ) , texture);
                break;
                
            case LAST_SECONDS_OF_SECTOR_LAP:
                gapAndTimeInvalid.update( false );
                gapOrTime.update( leadlap );
                
                if ( needsCompleteRedraw || ( clock.c() && gapOrTime.hasChanged() ) )
                    dsGap.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( gapOrTime.getValue() ), fontColor2.getColor() , texture);
                if ( needsCompleteRedraw || ( clock.c() && leaderPos.hasChanged() ) )
                    dsPosFrom.draw( offsetX, offsetY, leaderPos.getValueAsString(), texture );
                if ( needsCompleteRedraw || ( clock.c() && curlap.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( curlap.getValue(), false, false, true, false ) + "    ", texture);
                break;
                
            case LAP_FINISHED_BEGIN_NEW_LAP:
                //plan: if allready first show gap to previous own best time. else if newly first show gap to second                             
                
                float secondbest=0;
                oldbesttime.update( currentcarinfos.getBestLapTime() );
                //oldposition.update( currentcarinfos.getPlace( false ) );
                ownPos.update( currentcarinfos.getPlace( false ) );
                
                if(oldbesttime.hasChanged())
                    oldbest = oldbesttime.getOldValue();
                
                if(ownPos.getValue() == 1)
                {
                    if(gameData.getScoringInfo().getSecondFastestLapVSI() != null && ownPos.getValue() != 1)
                        secondbest = gameData.getScoringInfo().getSecondFastestLapVSI().getBestLapTime(); 
                    else
                        secondbest = oldbest;
                }
                else
                    if(ownPos.getValue() == posKnockout.getValue())
                    {
                        //second best p10-11
                        if(gameData.getScoringInfo().getVehicleScoringInfo( posKnockout.getValue()-1 ) != null && ownPos.getValue() != posKnockout.getValue())
                            secondbest = gameData.getScoringInfo().getVehicleScoringInfo( posKnockout.getValue()-1 ).getBestLapTime(); 
                        else
                            secondbest = oldbest;
                    }
                
            
               
                
                if (currentcarinfos.getLastLapTime() <= leadercarinfos.getBestLapTime() && secondbest < 0)
                    gapOrTime.update(  currentcarinfos.getLastLapTime() - oldbest );
                else
                    if( currentcarinfos.getLastLapTime() <= leadercarinfos.getBestLapTime() )
                        gapOrTime.update( currentcarinfos.getLastLapTime() - secondbest );
                    else
                        gapOrTime.update( currentcarinfos.getLastLapTime() - leadercarinfos.getBestLapTime() );
                    
                if ( needsCompleteRedraw || ( clock.c() && gapOrTime.hasChanged() ) )
                    dsGap.draw( offsetX, offsetY, getTimeAsGapString2( gapOrTime.getValue() ), ( gapOrTime.getValue() <= 0 ) ? fontColor2.getColor() : fontColor1.getColor(), texture);
                
                
                
                lastLaptime.update( currentcarinfos.getLastLapTime() );
                gapAndTimeInvalid.update( false );
                
                if ( needsCompleteRedraw || ( clock.c() && leaderPos.hasChanged() ) )
                {
                    if( (currentcarinfos.getLastLapTime() <= leadlap && leadercarinfos.getPlace( false ) == 1) || ownPos.getValue() == 1)
                        dsPosFrom.draw( offsetX, offsetY, "", texture );
                    else
                        dsPosFrom.draw( offsetX, offsetY, leaderPos.getValueAsString(), texture );
                    
                }
                if ( needsCompleteRedraw || ( clock.c() && ownPos.hasChanged() ) )
                    dsPos.draw( offsetX, offsetY, ownPos.getValueAsString(),( ownPos.getValue() <= posKnockout.getValue() ) ? fontColor2.getColor() : KnockoutFontColor.getColor(), texture );
                if ( needsCompleteRedraw || ( clock.c() && lastLaptime.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( lastLaptime.getValue(), false, false, true, true ) , texture);
                break;
              
            case OTHER:
                // other cases not info not drawn
                gapAndTimeInvalid.update( true );
                
                if ( needsCompleteRedraw || ( clock.c() && gapAndTimeInvalid.hasChanged() ) )
                {
                    dsGap.draw( offsetX, offsetY, "", texture);
                    dsPosFrom.draw( offsetX, offsetY, "", texture );
                }
                if ( needsCompleteRedraw || ( clock.c() && curlap.hasChanged() ) )
                    dsTime.draw( offsetX, offsetY, TimingUtil.getTimeAsString( curlap.getValue(), false, false, true, false ) + "    ", texture);
                break;
        }
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( posFont, "" );
        writer.writeProperty( fontColor1, "" );
        writer.writeProperty( KnockoutFontColor, "" );
        writer.writeProperty( posKnockout, "" );
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( forcePlayer, "" );
        writer.writeProperty( uppercasename, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        
        if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( posFont ) );
        else if ( loader.loadProperty( fontColor1 ) );
        else if ( loader.loadProperty( KnockoutFontColor ) );
        else if ( loader.loadProperty( posKnockout ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( forcePlayer ) );
        else if ( loader.loadProperty( uppercasename ) );
    }
    
    @Override
    protected void addFontPropertiesToContainer( PropertiesContainer propsCont, boolean forceAll )
    {
        propsCont.addGroup( "Colors and Fonts" );
        
        super.addFontPropertiesToContainer( propsCont, forceAll );
        
        propsCont.addProperty( fontColor1 );
        propsCont.addProperty( fontColor2 );
        propsCont.addProperty( posFont );
        propsCont.addProperty( KnockoutFontColor );
        
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Specific" );
        propsCont.addProperty( forcePlayer );
        propsCont.addProperty( fontyoffset );
        propsCont.addProperty( posKnockout );
        propsCont.addProperty( uppercasename );
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
    
    public QualTimeWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 26.2f, 10.75f );
        
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
