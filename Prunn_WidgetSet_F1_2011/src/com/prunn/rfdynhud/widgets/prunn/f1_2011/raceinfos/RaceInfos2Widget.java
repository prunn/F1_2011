package com.prunn.rfdynhud.widgets.prunn.f1_2011.raceinfos;

import java.awt.Font;
import java.io.IOException;

import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.DelayProperty;
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
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

import com.prunn.rfdynhud.plugins.tlcgenerator.StandardTLCGenerator;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;



/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class RaceInfos2Widget extends Widget
{
    private DrawnString dsPos = null;
    private DrawnString dsName = null;
    private DrawnString dsBest = null;
    private DrawnString dsLast = null;
    private DrawnString dsGainedPlaces = null;
    private DrawnString dsBestTime = null;
    private DrawnString dsLastTime = null;
    private DrawnString dsPitStops = null;
    
    private final ImagePropertyWithTexture imgPos = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/labeled_data_neutral.png" );
    private final ImagePropertyWithTexture imgPos1 = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/gap_first.png" );
    private final ImagePropertyWithTexture imgTime = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/data_temp2.png" );
    private final ImagePropertyWithTexture imgPitStops = new ImagePropertyWithTexture( "imgPitStops", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgPositive = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/tower/pospositive.png" );
    private final ImagePropertyWithTexture imgNegative = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/tower/posnegative.png" );
    private final ImagePropertyWithTexture imgNeutral = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/tower/posneutral.png" );
    private TextureImage2D texGainedPlaces = null;

    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor1 = new ColorProperty("fontColor1", PrunnWidgetSetf1_2011.FONT_COLOR1_NAME);
    private final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    private BooleanProperty uppercasename = new BooleanProperty("uppercase name",true); 
    
    private final FloatValue sessionTime = new FloatValue(-1F, 0.1F);
    private final DelayProperty visibleTime;
    private long visibleEnd;
    private boolean startedPositionsInitialized = false;
    private int[] startedPositions = null;
    short gainedPlaces;
    private ColorProperty GainedFontColor;
    private final IntValue CurrentLap = new IntValue();
    private BoolValue racefinished = new BoolValue();
    StandardTLCGenerator gen = new StandardTLCGenerator();
    
    
    
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onCockpitEntered( gameData, isEditorMode );
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
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
        int fh = TextureImage2D.getStringHeight( "0%C", getFontProperty() );
        int rowHeight = height / 4;
        int fw2 = Math.round(width * 0.47f);
        int fw3c = (width - fw2 - rowHeight)/2;
        imgPos.updateSize( width, rowHeight, isEditorMode );
        imgPos1.updateSize( width, rowHeight, isEditorMode );
        imgTime.updateSize( width, rowHeight, isEditorMode );
        imgPositive.updateSize( fw3c, rowHeight, isEditorMode );
        imgNegative.updateSize( fw3c, rowHeight, isEditorMode );
        imgNeutral.updateSize( fw3c, rowHeight, isEditorMode );
        imgPitStops.updateSize( width, rowHeight, isEditorMode );
        texGainedPlaces = imgNeutral.getImage().getScaledTextureImage( width/2, rowHeight, texGainedPlaces, isEditorMode );
        
        int top1 = ( rowHeight - fh ) / 2 + fontyoffset.getValue();
        int top2 = ( rowHeight - fh ) / 2 + rowHeight + fontyoffset.getValue();
        int top3 = ( rowHeight - fh ) / 2 + rowHeight*2 + fontyoffset.getValue();
        int top4 = ( rowHeight - fh ) / 2 + rowHeight*3 + fontyoffset.getValue();
        
        dsPos = drawnStringFactory.newDrawnString( "dsPos", width*12/100, top1, Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsName = drawnStringFactory.newDrawnString( "dsName", width*27/100, top1, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsBest = drawnStringFactory.newDrawnString( "dsBest", width*15/100, top3, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsLast = drawnStringFactory.newDrawnString( "dsLast", width*15/100, top4, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsBestTime = drawnStringFactory.newDrawnString( "dsBestTime", width*92/100, top3, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsLastTime = drawnStringFactory.newDrawnString( "dsLastTime", width*92/100, top4, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsPitStops = drawnStringFactory.newDrawnString( "dsPitStops", width*70/100, top2, Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsGainedPlaces = drawnStringFactory.newDrawnString( "dsGainedPlaces", width*35/100, top2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        
    }
    private void initStartedFromPositions( ScoringInfo scoringInfo )
    {
        startedPositions = new int[scoringInfo.getNumVehicles()];
        
        for(int j=0;j < scoringInfo.getNumVehicles(); j++)
            startedPositions[j] = scoringInfo.getVehicleScoringInfo( j ).getDriverId();
        
        startedPositionsInitialized = true;
    }
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        if ( !startedPositionsInitialized )
            initStartedFromPositions( scoringInfo );
        
        CurrentLap.update(scoringInfo.getLeadersVehicleScoringInfo().getSector());
        racefinished.update(scoringInfo.getViewedVehicleScoringInfo().getFinishStatus().isFinished());
        
        //if(RaceInfosWidget.visible() || RaceGapWidget.visible())
        //  return false;&& !RaceInfosWidget.visible() && !RaceGapWidget.visible()&& scoringInfo.getLeadersVehicleScoringInfo().getLapsCompleted() / scoringInfo.getMaxLaps() > 0.4f
        
        if((CurrentLap.hasChanged() && CurrentLap.getValue() == 2 && scoringInfo.getViewedVehicleScoringInfo().getLapsCompleted() > 4 && (short)( Math.random() * 2) == 0) || (racefinished.hasChanged() && racefinished.getValue()))
        {
            VehicleScoringInfo vsi = scoringInfo.getViewedVehicleScoringInfo();
            
            int startedfrom=0;
            for(int p=0; p < scoringInfo.getNumVehicles(); p++)
            {
                if( vsi.getDriverId() == startedPositions[p] )
                {
                    startedfrom = p+1;
                    break;
                } 
            }
            gainedPlaces = (short)( startedfrom - vsi.getPlace( false ) );
            
            forceCompleteRedraw(true);
            visibleEnd = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
            return true;
        }
        
        if(scoringInfo.getSessionNanos() < visibleEnd )
            return true;
        
        return false;	
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        
        int rowHeight = height / 4;
        
        texture.clear( imgPitStops.getTexture(), offsetX, offsetY + rowHeight, true, null ); 
        
        if(gameData.getScoringInfo().getViewedVehicleScoringInfo().getNextInFront( false ) == null)
            texture.clear( imgPos1.getTexture(), offsetX, offsetY, false, null );
        else
            texture.clear( imgPos.getTexture(), offsetX, offsetY, false, null );
        
        texture.clear( imgTime.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
        texture.clear( imgTime.getTexture(), offsetX, offsetY + rowHeight*3, false, null );
        
        
        if(gainedPlaces > 0 || isEditorMode)
            texGainedPlaces = imgPositive.getImage().getScaledTextureImage( width/2, rowHeight, texGainedPlaces, isEditorMode );
        else 
            if(gainedPlaces < 0)
                texGainedPlaces = imgNegative.getImage().getScaledTextureImage( width/2, rowHeight, texGainedPlaces, isEditorMode );
            else
                texGainedPlaces = imgNeutral.getImage().getScaledTextureImage( width/2, rowHeight, texGainedPlaces, isEditorMode );
        
        texture.drawImage( texGainedPlaces, offsetX, offsetY+rowHeight, true, null );
                   
        
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
    	sessionTime.update(scoringInfo.getSessionTime());
    	
    	if ( needsCompleteRedraw )
        {
    	    VehicleScoringInfo vsi = scoringInfo.getViewedVehicleScoringInfo();
            int numpit = vsi.getNumPitstopsMade();
            //" Stops"" Stop""Best""Last"
            String plural = ( numpit > 1 ) ? " " + Loc2.pit_stops  : " " + Loc2.pit_stop;
    	    String pitstops = Integer.toString( numpit ) + plural;
             
            dsPos.draw( offsetX, offsetY, Integer.toString( vsi.getPlace( false ) ), texture );
            if( uppercasename.getValue() )
                dsName.draw( offsetX, offsetY, gen.ShortName( vsi.getDriverNameShort().toUpperCase()), texture );
            else
                dsName.draw( offsetX, offsetY, gen.ShortName( vsi.getDriverNameShort()), texture );
            
            dsBest.draw( offsetX, offsetY, Loc2.best_lap, texture );
            dsBestTime.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( vsi.getBestLapTime() ), texture );
            dsLast.draw( offsetX, offsetY, Loc2.last_lap, texture );
            dsLastTime.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( vsi.getLastLapTime() ), texture );
            dsPitStops.draw( offsetX, offsetY, pitstops, texture );
            if(gainedPlaces >= 0)
                GainedFontColor = fontColor2;
            else
                GainedFontColor = fontColor1;
            dsGainedPlaces.draw( offsetX, offsetY, String.valueOf( Math.abs( gainedPlaces )), GainedFontColor.getColor(), texture );
            
        }
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( fontColor1, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty(visibleTime, "");
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( uppercasename, "" );
        
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( fontColor1 ) );
        else if ( loader.loadProperty( fontColor2 ) );
        else if(!loader.loadProperty(visibleTime));
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( uppercasename ) );
        
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Colors" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( fontColor1 );
        propsCont.addProperty( fontColor2 );
        propsCont.addProperty(visibleTime);

        propsCont.addProperty( fontyoffset );
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
    
    public RaceInfos2Widget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011_Race, 18.1f, 15.8f );
        visibleTime = new DelayProperty("visibleTime", net.ctdp.rfdynhud.properties.DelayProperty.DisplayUnits.SECONDS, 6);
        visibleEnd = 0;
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
