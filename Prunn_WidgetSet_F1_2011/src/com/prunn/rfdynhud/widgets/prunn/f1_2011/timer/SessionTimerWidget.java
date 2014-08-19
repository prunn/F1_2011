package com.prunn.rfdynhud.widgets.prunn.f1_2011.timer;

import java.awt.Font;
import java.io.IOException;
import net.ctdp.rfdynhud.gamedata.GamePhase;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.SessionLimit;
import net.ctdp.rfdynhud.gamedata.SessionType;
import net.ctdp.rfdynhud.gamedata.YellowFlagState;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.FontProperty;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.IntProperty;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.properties.StringProperty;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.EnumValue;
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
public class SessionTimerWidget extends Widget
{
	
	private final EnumValue<YellowFlagState> SCState = new EnumValue<YellowFlagState>();
    private final EnumValue<GamePhase> gamePhase = new EnumValue<GamePhase>();
    private final EnumValue<GamePhase> oldGamePhase = new EnumValue<GamePhase>();
    private final IntValue LapsLeft = new IntValue();
    private final BoolValue sectorYellowFlag = new BoolValue();
    private final BoolValue GreenFlag = new BoolValue();
    private final BoolValue FinishFlag = new BoolValue();
    private final FloatValue sessionTime = new FloatValue(-1F, 0.1F);
    private DrawnString dsSession = null;
    private DrawnString dsInfo = null;
    private DrawnString dsSC = null;
    private String strlaptime = "";
    private String strInfo = "";
    private TextureImage2D texSC = null;
    private final StringValue strLaptime = new StringValue( "" );
    private ImagePropertyWithTexture imgBG = new ImagePropertyWithTexture( "imgBG", "prunn/f1_2011/timer/timer_bg.png" );
    private ImagePropertyWithTexture imgBGYellow = new ImagePropertyWithTexture( "imgBGYellow", "prunn/f1_2011/timer/timer_yellow.png" );
    private ImagePropertyWithTexture imgBGGreen = new ImagePropertyWithTexture( "imgBGGreen", "prunn/f1_2011/timer/timer_green.png" );
    private final ImagePropertyWithTexture imgBGFinish = new ImagePropertyWithTexture( "imgBGFinished", "prunn/f1_2011/timer/timer_finished.png" );
    private final ImagePropertyWithTexture imgSC = new ImagePropertyWithTexture( "imgSC", "prunn/f1_2011/timer/race_sc.png" );
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor1 = new ColorProperty( "fontColor1", PrunnWidgetSetf1_2011.FONT_COLOR1_NAME );
    private final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    protected final StringProperty strTestDay = new StringProperty("Test Day", "TD");
    protected final StringProperty strWarmup = new StringProperty("Warmup", "WU");
    protected final StringProperty strPractice1 = new StringProperty("Practice 1", "P1");
    protected final StringProperty strPractice2 = new StringProperty("Practice 2", "P2");
    protected final StringProperty strPractice3 = new StringProperty("Practice 3", "P3");
    protected final StringProperty strPractice4 = new StringProperty("Practice 4", "P4");
    protected final StringProperty strQualif = new StringProperty("Qualification", "Q3");
    private ColorProperty drawnFontColor;
    private BooleanProperty useLapLeft = new BooleanProperty("show laps left", false);
    private long visibleEnd;
    private long visibleEndFinish;
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    
   
    
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onCockpitEntered( gameData, isEditorMode );
        LapsLeft.reset();
    	sectorYellowFlag.reset();
    	SCState.reset();
        gamePhase.reset();
        String cpid = "Y29weXJpZ2h0QFBydW5uMjAxMQ";
        if(!isEditorMode)
            log(cpid);
        
    }
    public void onSessionStarted(SessionType sessionType, LiveGameData gameData, boolean isEditorMode)
    {
        super.onSessionStarted(sessionType, gameData, isEditorMode);
        gamePhase.reset();
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
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        int fh = TextureImage2D.getStringHeight( "0%C", f1_2011Font );
        
        if(scoringInfo.getSessionType() == SessionType.RACE1 || scoringInfo.getSessionType() == SessionType.RACE2 || scoringInfo.getSessionType() == SessionType.RACE3 || scoringInfo.getSessionType() == SessionType.RACE4)  
        {
            imgBG = new ImagePropertyWithTexture( "imgBG", "prunn/f1_2011/timer/race_timer_bg.png" );
            imgBGYellow = new ImagePropertyWithTexture( "imgBGYellow", "prunn/f1_2011/timer/race_timer_yellow.png" );
            imgBGGreen = new ImagePropertyWithTexture( "imgBGGreen", "prunn/f1_2011/timer/race_timer_green.png" );
            dsInfo = drawnStringFactory.newDrawnString( "dsInfo", width*32/200+1, height/2 - fh/2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
            dsSession = drawnStringFactory.newDrawnString( "dsSession",width*52/100 , height/2 - fh/2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        }
        else
        {
            dsInfo = drawnStringFactory.newDrawnString( "dsInfo", width*31/200, height/2 - fh/2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
            dsSession = drawnStringFactory.newDrawnString( "dsSession",width*49/100 , height/2 - fh/2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        }
        
        dsSC = drawnStringFactory.newDrawnString( "dsInfo", width*87/100-1, height/2 - fh/2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        imgBG.updateSize( width*80/100, height, isEditorMode );
        imgBGYellow.updateSize( width*80/100, height, isEditorMode );
        imgBGGreen.updateSize( width*80/100, height, isEditorMode );
        imgBGFinish.updateSize( width*80/100, height, isEditorMode );
        texSC = imgSC.getImage().getScaledTextureImage( width*27/100, height, texSC, isEditorMode );
        
        switch(scoringInfo.getSessionType())
        {
            case RACE1: case RACE2: case RACE3: case RACE4:
                if(isEditorMode)
                    strInfo = Loc.race_time; //"Time";
                else
                    strInfo = Loc.race_lap; //"Lap";
                break;
            case QUALIFYING1: case QUALIFYING2: case QUALIFYING3: case QUALIFYING4:
                strInfo = strQualif.getValue();
                break;
            case PRACTICE1:
                strInfo = strPractice1.getValue();
                break;
            case PRACTICE2:
                strInfo = strPractice2.getValue();
                break;
            case PRACTICE3:
                strInfo = strPractice3.getValue();
                break;
            case PRACTICE4:
                strInfo = strPractice4.getValue();
                break;
            case TEST_DAY:
                strInfo = strTestDay.getValue();
                break;
            case WARMUP:
                strInfo = strWarmup.getValue();
                break;
            default:
                strInfo = "";
                break;
                    
        }
        
        
    }
    
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        SCState.update(scoringInfo.getYellowFlagState());
        sectorYellowFlag.update(scoringInfo.getSectorYellowFlag(scoringInfo.getViewedVehicleScoringInfo().getSector()));
        gamePhase.update(scoringInfo.getGamePhase());
        
        if(gamePhase.getValue() == GamePhase.GREEN_FLAG && oldGamePhase.getValue() == GamePhase.BEFORE_SESSION_HAS_BEGUN && gamePhase.hasChanged())
            visibleEnd = scoringInfo.getSessionNanos() + 5000000000l;
        if(gamePhase.getValue() == GamePhase.SESSION_OVER && oldGamePhase.getValue() == GamePhase.GREEN_FLAG && gamePhase.hasChanged() && scoringInfo.getSessionType() != SessionType.RACE1)
            visibleEndFinish = scoringInfo.getSessionNanos() + 5000000000l;
        
        oldGamePhase.update(scoringInfo.getGamePhase());
        
        if(scoringInfo.getSessionNanos() < visibleEnd)
            GreenFlag.update( true );
        else
            GreenFlag.update( false );
        
        if(scoringInfo.getSessionNanos() < visibleEndFinish)
            FinishFlag.update( true );
        else
            FinishFlag.update( false );
        
        if((GreenFlag.hasChanged() || FinishFlag.hasChanged()) && !isEditorMode)
            forceCompleteRedraw(true);
        
        if((SCState.hasChanged() || sectorYellowFlag.hasChanged()) && !isEditorMode)
            forceCompleteRedraw(true);
        
        if( scoringInfo.getGamePhase() == GamePhase.FORMATION_LAP )
            return false;
        if( scoringInfo.getGamePhase() == GamePhase.STARTING_LIGHT_COUNTDOWN_HAS_BEGUN && scoringInfo.getEndTime() <= scoringInfo.getSessionTime() )
            return false;
        
        return true;
        
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        //ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        if(sectorYellowFlag.getValue() || (SCState.getValue() != YellowFlagState.NONE && SCState.getValue() != YellowFlagState.RESUME) || isEditorMode)
            texture.clear( imgBGYellow.getTexture(), offsetX, offsetY, false, null );
        else
            if(SCState.getValue() == YellowFlagState.RESUME || GreenFlag.getValue())
                texture.clear( imgBGGreen.getTexture(), offsetX, offsetY, false, null );
            else
                if(FinishFlag.getValue())
                    texture.clear( imgBGFinish.getTexture(), offsetX, offsetY, false, null );
                else
                    texture.clear( imgBG.getTexture(), offsetX, offsetY, false, null );
        
        if((SCState.getValue() != YellowFlagState.NONE && SCState.getValue() != YellowFlagState.RESUME) || isEditorMode)
            texture.drawImage( texSC, offsetX + width*73/100, offsetY, true, null );
         
    }
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        String strSC = "";
        if (scoringInfo.getSessionType().isRace() && scoringInfo.getViewedVehicleScoringInfo().getSessionLimit() == SessionLimit.LAPS)
    	{
            if(useLapLeft.getValue())
                LapsLeft.update(scoringInfo.getMaxLaps() - scoringInfo.getLeadersVehicleScoringInfo().getLapsCompleted());
            else
                if(scoringInfo.getLeadersVehicleScoringInfo().getCurrentLap() > scoringInfo.getMaxLaps())
                    LapsLeft.update(scoringInfo.getMaxLaps());
                else
                    LapsLeft.update(scoringInfo.getLeadersVehicleScoringInfo().getCurrentLap());
            
	    	
	    	if ( needsCompleteRedraw || LapsLeft.hasChanged() )
	    	    strlaptime = LapsLeft.getValueAsString() + " / " + scoringInfo.getMaxLaps();
	    
	    	
		}
    	else // Test day only
    		if(scoringInfo.getSessionType().isTestDay())
    		    strlaptime = scoringInfo.getViewedVehicleScoringInfo().getLapsCompleted() + "  /  ";
    		else // any other timed session (Race, Qualify, Practice)
	    	{
    		    if(scoringInfo.getSessionType() == SessionType.RACE1 || scoringInfo.getSessionType() == SessionType.RACE2 || scoringInfo.getSessionType() == SessionType.RACE3 || scoringInfo.getSessionType() == SessionType.RACE4)
    		        strInfo = Loc.race_time; //"Time";
    	               
	    		//gamePhase.update(scoringInfo.getGamePhase());
		    	sessionTime.update(scoringInfo.getSessionTime());
	    		float endTime = scoringInfo.getEndTime();
	    		
                //if( scoringInfo.getSessionStartTimestamp() == scoringInfo.getSessionNanos() )
	    	    //        log(TimingUtil.getTimeAsString(endTime - sessionTime.getValue(), true, false));
	    		  
	    		if ( needsCompleteRedraw || sessionTime.hasChanged() )
		        {
		        	if(gamePhase.getValue() == GamePhase.SESSION_OVER || (endTime <= sessionTime.getValue() && gamePhase.getValue() != GamePhase.STARTING_LIGHT_COUNTDOWN_HAS_BEGUN ) )
		        	    strlaptime = "0:00";
			        else
        			    if(gamePhase.getValue() == GamePhase.STARTING_LIGHT_COUNTDOWN_HAS_BEGUN && endTime <= sessionTime.getValue())
		        		    strlaptime = "0:00";
		        		else
		        		{
		        		    
		        		    strlaptime = TimingUtil.getTimeAsString(endTime - sessionTime.getValue(), true, false);
		        	
        		        	if (strlaptime.charAt( 0 ) == '0')
        		        	    strlaptime = strlaptime.substring( 1 );
        		        	if (strlaptime.charAt( 0 ) == '0')
                                strlaptime = strlaptime.substring( 2 );
        		        	if (strlaptime.charAt( 0 ) == '0')
                                strlaptime = strlaptime.substring( 1 );
		        		}
		        }
	    		
	    	
	    	}
        
        strLaptime.update( strlaptime );
        
        if ( needsCompleteRedraw || ( clock.c() && strLaptime.hasChanged() ) )
        {
            if(sectorYellowFlag.getValue() || (SCState.getValue() != YellowFlagState.NONE && SCState.getValue() != YellowFlagState.RESUME) || isEditorMode)
                drawnFontColor = fontColor1;
            else
                drawnFontColor = fontColor2;
            
                        
            if((SCState.getValue() != YellowFlagState.NONE && SCState.getValue() != YellowFlagState.RESUME) || isEditorMode)
                strSC = "SC";
            else
                strSC = "";
            
            if(!FinishFlag.getValue())
                dsSession.draw( offsetX, offsetY, strlaptime,drawnFontColor.getColor(), texture );
            dsInfo.draw( offsetX, offsetY, strInfo, texture );
            dsSC.draw( offsetX, offsetY, strSC, texture );
        }  
        
      
        
    }
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "timeFont" );
        writer.writeProperty( fontColor1, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( strTestDay, "" );
        writer.writeProperty( strWarmup, "" );
        writer.writeProperty( strPractice1, "" );
        writer.writeProperty( strPractice2, "" );
        writer.writeProperty( strPractice3, "" );
        writer.writeProperty( strPractice4, "" );
        writer.writeProperty( strQualif, "" );
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( useLapLeft, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( fontColor1 ) );
        else if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( strTestDay ) );
        else if ( loader.loadProperty( strWarmup ) );
        else if ( loader.loadProperty( strPractice1 ) );
        else if ( loader.loadProperty( strPractice2 ) );
        else if ( loader.loadProperty( strPractice3 ) );
        else if ( loader.loadProperty( strPractice4 ) );
        else if ( loader.loadProperty( strQualif ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( useLapLeft ) );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Font" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( fontColor1 );
        propsCont.addProperty( fontColor2 );
        propsCont.addGroup( "Session Names" );
        propsCont.addProperty( strTestDay );
        propsCont.addProperty( strWarmup );
        propsCont.addProperty( strPractice1 );
        propsCont.addProperty( strPractice2 );
        propsCont.addProperty( strPractice3 );
        propsCont.addProperty( strPractice4 );
        propsCont.addProperty( strQualif );
        propsCont.addProperty( useLapLeft );
        propsCont.addProperty( fontyoffset );
        
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
    
    public SessionTimerWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 19.0f, 5.0f );
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
        //getBorderProperty().setBorder( null );
    }
}
