package com.prunn.rfdynhud.widgets.prunn.f1_2011.raceinfos;

import java.awt.Font;
import java.io.IOException;

import com.prunn.rfdynhud.plugins.tlcgenerator.StandardTLCGenerator;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

import net.ctdp.rfdynhud.gamedata.Laptime;
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
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.NumberUtil;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class RaceInfosWidget extends Widget
{
    public static Boolean isvisible = false;
    public static Boolean visible()
    {
        return isvisible;
    }
    
    private DrawnString dsPos = null;
    private DrawnString dsName = null;
    private DrawnString dsTeam = null;
    private DrawnString dsTime = null;
    private DrawnString dsTitle = null;
    private DrawnString dsTimeB = null;
    private DrawnString dsTitleB = null;
    private DrawnString dsTimeC = null;
    private DrawnString dsTitleC = null;
    private DrawnString dsTitle2 = null;
    private DrawnString dsWinner = null;
    private TextureImage2D texFastestTitle = null;
    private TextureImage2D texFastestData = null;
    private TextureImage2D texWinnerTime = null;
    private TextureImage2D texPos = null;
    private final ImagePropertyWithTexture imgPosBig = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/big_position_neutral.png" );
    private final ImagePropertyWithTexture imgPosBig1 = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/big_position_first.png" );
    private final ImagePropertyWithTexture imgName = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgNameW = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgTeam = new ImagePropertyWithTexture( "imgTeam", "prunn/f1_2011/data_caption.png" );
    private final ImagePropertyWithTexture imgTeamF = new ImagePropertyWithTexture( "imgTeam", "prunn/f1_2011/data_caption_small.png" );
    private final ImagePropertyWithTexture imgTeamW = new ImagePropertyWithTexture( "imgTeam", "prunn/f1_2011/data_caption.png" );
    private final ImagePropertyWithTexture imgTime = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/race_gap.png" );
    private final ImagePropertyWithTexture imgTimeF = new ImagePropertyWithTexture( "imgTimeF", "prunn/f1_2011/data_faster_small.png" );
    private final ImagePropertyWithTexture imgTitle = new ImagePropertyWithTexture( "imgTitle", "prunn/f109/title.png" );
    private final ImagePropertyWithTexture imgTitleW = new ImagePropertyWithTexture( "imgTitleW", "prunn/f1_2011/data_winner.png" );
    private final ImagePropertyWithTexture imgTitleF = new ImagePropertyWithTexture( "imgTitle", "prunn/f109/gapgreen.png" );
    private final ImagePropertyWithTexture imgPit = new ImagePropertyWithTexture( "imgTitle", "prunn/f1_2011/data_temp.png" );
    
    
    private final FontProperty posFont = new FontProperty("positionFont", PrunnWidgetSetf1_2011.POS_FONT_NAME);
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    protected final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    protected final BooleanProperty showwinner = new BooleanProperty("Show Winner", "showwinner", true);
    protected final BooleanProperty showfastest = new BooleanProperty("Show Fastest Lap", "showfastest", true);
    protected final BooleanProperty showpitstop = new BooleanProperty("Show Pitstop", "showpitstop", true);
    protected final BooleanProperty showinfo = new BooleanProperty("Show Info", "showinfo", true);
    private BooleanProperty uppercasename = new BooleanProperty("uppercase name",true); 
    
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    private final FloatValue sessionTime = new FloatValue(-1F, 0.1F);
    private float timestamp = -1;
    private float endtimestamp = -1;
    private float pitInTime = -1;
    private float pittime = -1;
    private float pitLaneTime = -1;
    private BoolValue isInPit = new BoolValue(false);
    private final DelayProperty visibleTime;
    private long visibleEnd;
    private long visibleEndPitStop;
    private IntValue cveh = new IntValue();
    private IntValue speed = new IntValue();
    private long visibleEndW;
    private long visibleEndF;
    private final FloatValue racetime = new FloatValue( -1f, 0.1f );
    private float sessionstart = 0;
    private BoolValue racefinished = new BoolValue();
    
    private int widgetpart = 0;//0-info 1-pitstop 2-fastestlap 3-winner
    private final FloatValue FastestLapTime = new FloatValue(-1F, 0.001F);
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
        int fh = TextureImage2D.getStringHeight( "0%C", f1_2011Font );
        int fhPos = TextureImage2D.getStringHeight( "0%C", posFont );
        int rowHeight = height / 3;
        int fw2 = Math.round(width * 0.6f);
        int fw3 = width - fw2;
        
        imgPosBig.updateSize( rowHeight *2, rowHeight*2, isEditorMode );
        imgPosBig1.updateSize( rowHeight *2, rowHeight*2, isEditorMode );
        imgName.updateSize( fw2, height / 3, isEditorMode );
        imgTeam.updateSize( fw2, rowHeight, isEditorMode );
        imgTeamF.updateSize( fw3, rowHeight, isEditorMode );
        imgTime.updateSize( fw3, rowHeight, isEditorMode );
        imgTimeF.updateSize( fw3, rowHeight, isEditorMode );
        imgTitle.updateSize( fw3, rowHeight, isEditorMode );
        imgTitleF.updateSize( fw3, rowHeight, isEditorMode );
        imgTitleW.updateSize( fw2-width*3/100, rowHeight, isEditorMode );
        imgTeamW.updateSize( fw2-width*3/100, rowHeight, isEditorMode );
        imgNameW.updateSize( fw2-width*3/100, rowHeight, isEditorMode );
        imgPit.updateSize( fw2, height / 3, isEditorMode );
        texFastestTitle = imgTeamF.getImage().getScaledTextureImage( fw3-width*1/100, rowHeight, texFastestTitle, isEditorMode );
        texFastestData = imgTimeF.getImage().getScaledTextureImage( fw3-width*1/100, rowHeight, texFastestData, isEditorMode );
        texWinnerTime = imgTime.getImage().getScaledTextureImage( fw3-width*1/100, rowHeight, texWinnerTime, isEditorMode );
        texPos = imgPosBig.getImage().getScaledTextureImage( rowHeight *3, rowHeight *2, texPos, isEditorMode );
        
        int top1 = ( rowHeight - fh ) / 2 + fontyoffset.getValue();
        int top2 = ( rowHeight - fh ) / 2 + rowHeight + fontyoffset.getValue();
        int top3 = ( rowHeight - fh ) / 2 + rowHeight*2 + fontyoffset.getValue();
        int topPos = rowHeight*2 - fhPos/2 + fontyoffset.getValue();
        
        dsPos = drawnStringFactory.newDrawnString( "dsPos", rowHeight + fw2 - width*1/100, topPos, Alignment.CENTER, false, posFont.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsName = drawnStringFactory.newDrawnString( "dsName", 20 + width*4/100, top2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTeam = drawnStringFactory.newDrawnString( "dsTeam", 20, top3, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTime = drawnStringFactory.newDrawnString( "dsTime", width*95/100, top2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTitle = drawnStringFactory.newDrawnString( "dsTitle", width*92/100, top3, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTimeB = drawnStringFactory.newDrawnString( "dsTimeB", width*95/100-9, top2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTitleB = drawnStringFactory.newDrawnString( "dsTitleB", width*92/100-9, top3, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTimeC = drawnStringFactory.newDrawnString( "dsTimeC", width*58/100, top2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTitleC = drawnStringFactory.newDrawnString( "dsTitleC", width*50/100, top3, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTitle2 = drawnStringFactory.newDrawnString( "dsTitle2", fw2 + fw3*11/12 - 9, top1, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsWinner = drawnStringFactory.newDrawnString( "dsWinner", 20 + width*8/100, top1, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        
    }
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        cveh.update(gameData.getScoringInfo().getViewedVehicleScoringInfo().getDriverId());
        isInPit.update(scoringInfo.getViewedVehicleScoringInfo().isInPits());
        //fastest lap
        Laptime lt = scoringInfo.getFastestLaptime();
        
        if(lt == null || !lt.isFinished())
            FastestLapTime.update(-1F);
        else
            FastestLapTime.update(lt.getLapTime());
        //winner part
        if(gameData.getScoringInfo().getLeadersVehicleScoringInfo().getLapsCompleted() < 1)
            sessionstart = gameData.getScoringInfo().getLeadersVehicleScoringInfo().getLapStartTime();
        if(scoringInfo.getSessionTime() > 0)
            racetime.update( scoringInfo.getSessionTime() - sessionstart );
        
        racefinished.update(gameData.getScoringInfo().getViewedVehicleScoringInfo().getFinishStatus().isFinished());
        
               
        //carinfo
        if(cveh.hasChanged() && cveh.isValid() && showinfo.getValue() && !isEditorMode)
        {
            forceCompleteRedraw(true);
            visibleEnd = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
            isvisible = true;
            widgetpart = 0;
            return true;
        }
        
        if(scoringInfo.getSessionNanos() < visibleEnd )
        {
            forceCompleteRedraw(true);
            isvisible = true;
            widgetpart = 0;
            return true;
        }
        
        //pitstop   
        if( isInPit.hasChanged())
        {
            if(isInPit.getValue())
            {
                pitLaneTime = 0;
                pittime = 0;
                pitInTime = gameData.getScoringInfo().getSessionTime();
                forceCompleteRedraw(true);
            }
            
            
            endtimestamp = 0;
            timestamp = 0;
            
        }
            
        if( isInPit.getValue() && showpitstop.getValue() )
        {
            if(scoringInfo.getViewedVehicleScoringInfo().getLapsCompleted() > 0)
                widgetpart = 1;
            else
            {
                widgetpart = 0;
            }
            
            speed.update( (int)scoringInfo.getViewedVehicleScoringInfo().getScalarVelocity());
            if(speed.hasChanged() && speed.getValue() < 2)
            {//ai gets to 5-6 kmh when they drop
                endtimestamp = gameData.getScoringInfo().getSessionTime();
                timestamp = gameData.getScoringInfo().getSessionTime();
            }
            else
                if(speed.getValue() < 2)
                    endtimestamp = gameData.getScoringInfo().getSessionTime();
                
            
            visibleEndPitStop = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
            isvisible = true;
            return true;
        }
        
        if(scoringInfo.getSessionNanos() < visibleEndPitStop )
        {
            forceCompleteRedraw(true);
            isvisible = true;
            widgetpart = 1;
            return true;
        }
        
        //fastest lap
        if(scoringInfo.getSessionNanos() < visibleEndF && FastestLapTime.isValid())
        {
            isvisible = true;
            widgetpart = 2;
            return true; 
        }
        if(FastestLapTime.hasChanged() && FastestLapTime.isValid() && scoringInfo.getLeadersVehicleScoringInfo().getLapsCompleted() > 1 && showfastest.getValue())
        {
            forceCompleteRedraw(true);
            visibleEndF = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
            isvisible = true;
            widgetpart = 2;
            return true;
        }
        
        //winner part
        if(scoringInfo.getSessionNanos() < visibleEndW )
        {
            isvisible = true;
            widgetpart = 3;
            return true;
        }
         
        if(racefinished.hasChanged() && racefinished.getValue() && showwinner.getValue() )
        {
            forceCompleteRedraw(true);
            visibleEndW = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos()*2;
            isvisible = true;
            widgetpart = 3;
            return true;
        }
        isvisible = false;
        return false;	
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        
        int rowHeight = height / 3;
        if(isEditorMode)
            widgetpart = 2;
        switch(widgetpart)
        {
            case 1: //Pit Stop
                    texture.clear( imgName.getTexture(), offsetX + width*8/100, offsetY, false, null );
                    texture.clear( imgPit.getTexture(), offsetX + width*4/100, offsetY + rowHeight, false, null );
                    texture.clear( imgPit.getTexture(), offsetX, offsetY + rowHeight*2, false, null );
                    break;
        
            case 2: //Fastest Lap
                    texture.clear( imgName.getTexture(), offsetX + width*4/100, offsetY + rowHeight, false, null );
                    texture.clear( imgTeam.getTexture(), offsetX, offsetY + rowHeight + height / 3, false, null );
                    texture.drawImage( texFastestTitle, offsetX + imgTeam.getTexture().getWidth()+width*1/100, offsetY + rowHeight, false, null );
                    texture.drawImage( texFastestData, offsetX + imgTeam.getTexture().getWidth()-width*3/100, offsetY + rowHeight*2, false, null );
                    break;
                    
            case 3: //Winner
                    texture.clear( imgNameW.getTexture(), offsetX+width*3/100, offsetY + rowHeight, false, null );
                    texture.clear( imgTeamW.getTexture(), offsetX, offsetY + rowHeight + height / 3, false, null );
                    texture.clear( imgTitleW.getTexture(), offsetX+width*6/100, offsetY, false, null );
                    texture.drawImage( texWinnerTime, offsetX + imgTeam.getTexture().getWidth(), offsetY, false, null );
                    texture.drawImage( texWinnerTime, offsetX + imgTeam.getTexture().getWidth()-width*3/100, offsetY + rowHeight, false, null );
                    texture.drawImage( texWinnerTime, offsetX + imgTeam.getTexture().getWidth()-width*6/100, offsetY + rowHeight*2, false, null );
                    break;
            
            default: //Info
                    texture.clear( imgName.getTexture(), offsetX + width*4/100, offsetY + rowHeight, false, null );
                    texture.clear( imgTeam.getTexture(), offsetX, offsetY + rowHeight + height / 3, false, null );
                    
                    if(gameData.getScoringInfo().getViewedVehicleScoringInfo().getNextInFront( false ) == null)
                        texPos = imgPosBig1.getImage().getScaledTextureImage( rowHeight *2 + width*5/100, rowHeight*2, texPos, isEditorMode );
                    else
                        texPos = imgPosBig.getImage().getScaledTextureImage( rowHeight *2 + width*5/100, rowHeight*2, texPos, isEditorMode );
                    
                    texture.drawImage( texPos, offsetX + imgName.getTexture().getWidth() - width*7/200, offsetY + rowHeight, false, null );
                    
                    break;
        }
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
    	sessionTime.update(scoringInfo.getSessionTime());
    	if(isEditorMode)
            widgetpart = 2;
    	
    	if ( needsCompleteRedraw || sessionTime.hasChanged() || FastestLapTime.hasChanged())
        {
    	    String pos = "";
    	    String top1info1="";
    	    String top1info2="";
    	    String top2info1;
    	    String top2info2="";
    	    String top2info2b="";
    	    String top2info2c="";
            String top3info1;
    	    String top3info2="";
    	    String top3info2b="";
    	    String top3info2c="";
            
    	    switch(widgetpart)
            {
                case 1: //Pit Stop
                        VehicleScoringInfo currentcarinfos = gameData.getScoringInfo().getViewedVehicleScoringInfo();
                        /*if(currentcarinfos.getNumOutstandingPenalties() > 0)
                            top3info2="";
                        else
                            top3info2="";*/
                        
                        if(isInPit.getValue())
                        {
                            pitLaneTime = gameData.getScoringInfo().getSessionTime() - pitInTime;
                            top2info2c = TimingUtil.getTimeAsString(pitLaneTime, false, false, true, false ) + "    ";
                        }
                        else
                            top2info2c = TimingUtil.getTimeAsString(pitLaneTime, false, false, true, true );
                            
                        if(scoringInfo.getViewedVehicleScoringInfo().getScalarVelocity() < 2)
                            pittime = endtimestamp - timestamp;
                        
                        if( uppercasename.getValue() )
                            top1info1 = currentcarinfos.getDriverNameShort().toUpperCase();
                        else
                            top1info1 = currentcarinfos.getDriverNameShort();
                        top2info1 = Loc.pit_lane;//"Pit Lane";
                        top3info2c = TimingUtil.getTimeAsString(pittime, false, false, true, false );
                        top3info1 = Loc.pit_stop;//"Pit Stop";
                        
                        dsName.draw( offsetX, offsetY, top2info1, texture );
                        dsTeam.draw( offsetX, offsetY, top3info1, texture );
                        dsWinner.draw( offsetX, offsetY, gen.ShortName( top1info1 ), texture );
                        dsTimeC.draw( offsetX, offsetY, top2info2c, texture);
                        dsTitleC.draw( offsetX, offsetY, top3info2c, texture );
                        
                        break;
                    
                case 2: //Fastest Lap
                        VehicleScoringInfo fastcarinfos = gameData.getScoringInfo().getFastestLapVSI();
                        
                        if(fastcarinfos.getVehicleInfo() != null)
                            top3info1 = gen.generateShortTeamNames( fastcarinfos.getVehicleInfo().getTeamName(), gameData.getFileSystem().getConfigFolder() );
                        else
                            top3info1 = fastcarinfos.getVehicleClass(); 
                        if( uppercasename.getValue() )
                            top2info1 = fastcarinfos.getDriverNameShort().toUpperCase();
                        else
                            top2info1 = fastcarinfos.getDriverNameShort();
                        top2info2 = Loc.fastest_lap;//"FASTEST LAP";
                        top3info2 = TimingUtil.getTimeAsLaptimeString(FastestLapTime.getValue() );
                        
                        dsName.draw( offsetX, offsetY, gen.ShortName( top2info1 ), texture );
                        dsTeam.draw( offsetX, offsetY, top3info1, texture );
                        dsTime.draw( offsetX, offsetY, top2info2, texture);
                        dsTitle.draw( offsetX, offsetY, top3info2, texture );
                        
                        break;
                        
                case 3: //Winner
                        VehicleScoringInfo winnercarinfos = gameData.getScoringInfo().getLeadersVehicleScoringInfo();
                        
                        if(winnercarinfos.getVehicleInfo() != null)
                            top3info1 = gen.generateShortTeamNames( winnercarinfos.getVehicleInfo().getTeamName(), gameData.getFileSystem().getConfigFolder() );
                        else
                            top3info1 = winnercarinfos.getVehicleClass(); 
                        
                        float laps=0;
                        
                        for(int i=1;i <= winnercarinfos.getLapsCompleted(); i++)
                        {
                            if(winnercarinfos.getLaptime(i) != null)
                                laps = winnercarinfos.getLaptime(i).getLapTime() + laps;
                            else
                            {
                                laps = racetime.getValue();
                                i = winnercarinfos.getLapsCompleted()+1;
                            }
                        } 
                        top1info1 = Loc.winner; //"WINNER";
                        top1info2= TimingUtil.getTimeAsLaptimeString( laps );
                        
                        if( uppercasename.getValue() )
                            top2info1 = winnercarinfos.getDriverNameShort().toUpperCase();
                        else
                            top2info1 = winnercarinfos.getDriverNameShort();
                        
                        top2info2b = NumberUtil.formatFloat( gameData.getTrackInfo().getTrack().getTrackLength() * gameData.getScoringInfo().getLeadersVehicleScoringInfo().getLapsCompleted() / 1000f, 3, true ) + " km";
                        top3info2b = NumberUtil.formatFloat( gameData.getTrackInfo().getTrack().getTrackLength() * gameData.getScoringInfo().getLeadersVehicleScoringInfo().getLapsCompleted() / 1000f / laps * 3600, 3, true ) + " km/h";
                        
                        dsName.draw( offsetX, offsetY, gen.ShortName( top2info1 ), texture );
                        dsTeam.draw( offsetX, offsetY, top3info1, texture );
                        dsWinner.draw( offsetX, offsetY, top1info1, texture );
                        dsTimeB.draw( offsetX, offsetY, top2info2b, texture);
                        dsTitleB.draw( offsetX, offsetY, top3info2b, texture );
                        dsTitle2.draw( offsetX, offsetY, top1info2, texture );
                        
                        break;
                
                default: //Info
                        VehicleScoringInfo currentcarinfosInfo = gameData.getScoringInfo().getViewedVehicleScoringInfo();
                        
                        if(currentcarinfosInfo.getVehicleInfo() != null)
                            top3info1 = gen.generateShortTeamNames( currentcarinfosInfo.getVehicleInfo().getTeamName(), gameData.getFileSystem().getConfigFolder() );
                        else
                            top3info1 = currentcarinfosInfo.getVehicleClass(); 
                        
                        if( uppercasename.getValue() )
                            top2info1 = currentcarinfosInfo.getDriverNameShort().toUpperCase();
                        else
                            top2info1 = currentcarinfosInfo.getDriverNameShort();
                        pos = Integer.toString( currentcarinfosInfo.getPlace(false) );
                        
                        dsPos.draw( offsetX, offsetY, pos, texture );
                        dsName.draw( offsetX, offsetY, gen.ShortName( top2info1 ), texture );
                        dsTeam.draw( offsetX, offsetY, top3info1, texture );
                        
                        break;
            }
    	    
        	
        }
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( posFont, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty(visibleTime, "");
        writer.writeProperty(showwinner, "");
        writer.writeProperty(showfastest, "");
        writer.writeProperty(showpitstop, "");
        writer.writeProperty(showinfo, "");
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( uppercasename, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( posFont ) );
        else if ( loader.loadProperty( fontColor2 ) );
        else if(loader.loadProperty(visibleTime));
        else if ( loader.loadProperty( showwinner ) );
        else if ( loader.loadProperty( showfastest ) );
        else if ( loader.loadProperty( showpitstop ) );
        else if ( loader.loadProperty( showinfo ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( uppercasename ) );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Colors" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( posFont );
        propsCont.addProperty( fontColor2 );
        propsCont.addProperty(visibleTime);
        propsCont.addProperty(showwinner);
        propsCont.addProperty(showfastest);
        propsCont.addProperty(showpitstop);
        propsCont.addProperty(showinfo);
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
    
    public RaceInfosWidget()
    {
        super(PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011_Race, 36.0f, 11.6f);
        
        visibleTime = new DelayProperty("visibleTime", net.ctdp.rfdynhud.properties.DelayProperty.DisplayUnits.SECONDS, 6);
        visibleEnd = 0;
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
