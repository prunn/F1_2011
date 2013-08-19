package com.prunn.rfdynhud.widgets.prunn.f1_2011.revmetter;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ProfileInfo.MeasurementUnits;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.SessionType;
import net.ctdp.rfdynhud.gamedata.TelemetryData;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.gamedata.YellowFlagState;
import net.ctdp.rfdynhud.input.InputAction;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.FloatProperty;
import net.ctdp.rfdynhud.properties.FontProperty;
import net.ctdp.rfdynhud.properties.ImageProperty;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.ImageTemplate;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.TransformableTexture;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.FontUtils;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;


/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class ShiftLightsMetter2011Widget extends Widget
{
    private DrawnString dsSpeed = null;
    private DrawnString dsSpeed2 = null;
    private DrawnString dsSpeed3 = null;
    private DrawnString dsLCD1 = null;
    private DrawnString dsLCD2 = null;
    private DrawnString dsLCD3 = null;
    //private DrawnString dsLCDback3 = null;
    private DrawnString dsGear = null;
    private TransformableTexture texKers1;
    private TransformableTexture texKers2;
    private TransformableTexture texKersOn1;
    private TransformableTexture texKersOn2;
    private ImageProperty Imgkers = new ImageProperty("Imgkers", null, "prunn/f1_2011/revmetter/shift_kers.png", false, false);
    private ImageProperty ImgkersOn = new ImageProperty("ImgkersOn", null, "prunn/f1_2011/revmetter/shift_kerson.png", false, false);
    private boolean kersDirty;
    private boolean kersOnDirty;
    private TextureImage2D texDRS = null;
    private ImagePropertyWithTexture imgDRS = new ImagePropertyWithTexture("imgDRS", "prunn/f1_2011/revmetter/shift_drs.png");
    private ImagePropertyWithTexture imgDRS2 = new ImagePropertyWithTexture("imgDRS2", "prunn/f1_2011/revmetter/shift_drs2.png");
    private ImagePropertyWithTexture imgDRSdisabled = new ImagePropertyWithTexture("imgDRSdisabled", "prunn/f1_2011/revmetter/shift_drs_disabled.png");
    private ImagePropertyWithTexture imgDRSavailable = new ImagePropertyWithTexture("imgDRSavailable", "prunn/f1_2011/revmetter/shift_drs_available.png");
    private final ImagePropertyWithTexture ImgRevmetter = new ImagePropertyWithTexture( "image", null, "prunn/f1_2011/revmetter/shiftlightsbg.png", false, false ); 
    private ImageProperty ImgShiftRed = new ImageProperty("ImgShiftRed", null, "prunn/f1_2011/revmetter/shift_red.png", false, false);
    private ImageProperty ImgShiftBlue = new ImageProperty("ImgShiftBlue", null, "prunn/f1_2011/revmetter/shift_blue.png", false, false);
    private ImageProperty ImgShiftGreen = new ImageProperty("ImgShiftGreen", null, "prunn/f1_2011/revmetter/shift_green.png", false, false);
    private ImageProperty ImgShiftGear = new ImageProperty("ImgShiftGear", null, "prunn/f1_2011/revmetter/shift_gear.png", false, false);
    private TextureImage2D texShiftRed;
    private TextureImage2D texShiftBlue;
    private TextureImage2D texShiftGreen;
    private TextureImage2D texShiftGear;
    protected final FontProperty speedF11Font2 = new FontProperty("speed Font", "speedF11Font2");
    protected final FontProperty LCDFont = new FontProperty("LCD Font", "LCDFont");
    protected final FontProperty gearF11Font2 = new FontProperty("gear Font", "gearF11Font2");
    private final ColorProperty GearFontColor2 = new ColorProperty("Units Font Color", "GearFontColor2");
    private final ColorProperty LCDFontColor = new ColorProperty("LCD Font Color", "LCDFontColor");
    private IntValue cSpeed = new IntValue();
    private IntValue cGear = new IntValue();
    private final IntValue CurrentLap = new IntValue();
    protected final FloatProperty kerstime = new FloatProperty("Kers Time", 6.6f);
    private float KersLeft;
    private static final InputAction DRS_ACTION_SHIFT = new InputAction( "DRS Action Shift", true );
    private static final InputAction DRS_ACTION_SHIFT_ON = new InputAction( "DRS Shift On", true );
    private static final InputAction DRS_ACTION_SHIFT_OFF = new InputAction( "DRS Shift Off", true );
    private static final InputAction LCD_SWITCH = new InputAction( "LCD Info Switch", true );
    private BoolValue DRS = new BoolValue();
    private BoolValue DRS_Disabled = new BoolValue();
    private BoolValue DRS_Available = new BoolValue();
    private BoolValue DRS_VisibleEnd = new BoolValue();
    private BoolValue DRS_detection = new BoolValue();
    private BoolValue DRS_detection2 = new BoolValue();
    private long visibleEnd_DRS;
    private IntValue tenthOfSec = new IntValue();
    private int oldTenthOfSec;
    private IntValue RPMLights = new IntValue();
    protected BooleanProperty useMaxRevLimit = new BooleanProperty("useMaxRevLimit", "useMaxRevLimit", false);
    protected FloatProperty shiftLightMulti = new FloatProperty("Shift Lights Multi", "shiftLightsMulti", 0.0074f,false);
    private IntValue detection = new IntValue();
    private IntValue activation = new IntValue();
    private IntValue deactivation = new IntValue();
    private IntValue detection2 = new IntValue();
    private IntValue activation2 = new IntValue();
    private IntValue deactivation2 = new IntValue();
    private BooleanProperty useDRSlog = new BooleanProperty("use drs log", "use drs log", true);
    private BooleanProperty DeactivateBrake = new BooleanProperty("Deactivate DRS with Brake", "DeactivateBrake", false);
    private BooleanProperty HalfLights = new BooleanProperty("Half Lights Mode", "Half Lights Mode", false);
    private boolean isRace = false;
    private boolean drs_finishline = false;
    private float drs_gap = 0.0f;
    private int drs_pos = 0;
    private int drs_lap = 0;
    private boolean drs_finishline2 = false;
    private float drs_gap2 = 0.0f;
    private int drs_pos2 = 0;
    private int drs_lap2 = 0;
    private IntValue position = new IntValue();
    private IntValue fuel = new IntValue();
    private IntValue ButtonPress = new IntValue();
    private int LastSC = 0;
    
    public String getDefaultNamedColorValue(String name)
    {
        if(name.equals("StandardBackground"))
            return "#00000000";
        if(name.equals("StandardFontColor"))
            return "#FFFFFF";
        if(name.equals("GearFontColor2"))
            return "#E9E9E9";
        if(name.equals("LCDFontColor"))
            return "#E9E9E9";
        
        return null;
    }
    @Override
    public String getDefaultNamedFontValue(String name)
    {
        if(name.equals("StandardFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        if(name.equals("speedF11Font2"))
            return FontUtils.getFontString("DS-Digital", Font.BOLD, 48, true, true);
        if(name.equals("LCDFont"))
            return FontUtils.getFontString("DS-Digital", Font.BOLD, 42, true, true);
        if(name.equals("gearF11Font2"))
            return FontUtils.getFontString("DS-Digital", 1, 60, true, true);
        
        return null;
    }
    void GetDrsLogFile(LiveGameData gameData)
    {
        
        try
        {
            File file = new File(gameData.getFileSystem().getCacheFolder() + "/data/drszones/" + gameData.getTrackInfo().getTrackName() + ".txt");
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            
            detection.update( Integer.valueOf(br.readLine().substring(10)));
            activation.update( Integer.valueOf(br.readLine().substring(11)) );
            deactivation.update( Integer.valueOf(br.readLine().substring(13)));
            detection2.update( Integer.valueOf(br.readLine().substring(10)));
            activation2.update( Integer.valueOf(br.readLine().substring(11)) );
            deactivation2.update( Integer.valueOf(br.readLine().substring(13)));
            
            br.close();
        }
        catch (Exception e)
        {
            detection.update( 0 );
            activation.update( 0 );
            deactivation.update( 0 );
            detection2.update( 0 );
            activation2.update( 0 );
            deactivation2.update( 0 );
            
        }
            
        isRace = true;    
        if(deactivation.getValue() < activation.getValue())
            drs_finishline = true;
        else
            drs_finishline = false;
        
        if(deactivation2.getValue() < activation2.getValue())
            drs_finishline2 = true;
        else
            drs_finishline2 = false;
        
    }
    
    @Override
    public InputAction[] getInputActions()
    {
        return ( new InputAction[] { DRS_ACTION_SHIFT, DRS_ACTION_SHIFT_ON, DRS_ACTION_SHIFT_OFF, LCD_SWITCH } );
    }
    protected Boolean onVehicleControlChanged(VehicleScoringInfo viewedVSI, LiveGameData gameData, boolean isEditorMode)
    {
        super.onVehicleControlChanged(viewedVSI, gameData, isEditorMode);
        
        return viewedVSI.isPlayer();
    }
    
    
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
    protected Boolean onBoundInputStateChanged( InputAction action, boolean state, int modifierMask, long when, LiveGameData gameData, boolean isEditorMode )
    {
        
        Boolean result = super.onBoundInputStateChanged( action, state, modifierMask, when, gameData, isEditorMode );
        
        
        if ( (action == DRS_ACTION_SHIFT && !DRS.getValue())  || action == DRS_ACTION_SHIFT_ON )
        {
            visibleEnd_DRS = gameData.getScoringInfo().getSessionNanos() + 300000000l;
            DRS.update(true);
        }
        else
            if ( action == DRS_ACTION_SHIFT || action == DRS_ACTION_SHIFT_OFF)
                DRS.update(false);
            
        
        
        if ( action == LCD_SWITCH )
        {
            if(ButtonPress.getValue() == 0)
                ButtonPress.update( 1 );
            else
                ButtonPress.update( 0 );
        }      
        return ( result );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initSubTextures( LiveGameData gameData, boolean isEditorMode, int widgetInnerWidth, int widgetInnerHeight, SubTextureCollector collector )
    {
        
        int w = widgetInnerWidth*48/200;
        int h = widgetInnerHeight*26/100;
        
        if(texKers1 == null || texKers1.getWidth() != w || texKers1.getHeight() != h || kersDirty)
        {
            texKers1 = TransformableTexture.getOrCreate(w, h - widgetInnerHeight/80, true, texKers1, isEditorMode);
            texKers2 = TransformableTexture.getOrCreate(w, h - widgetInnerHeight/80, true, texKers2, isEditorMode);
            ImageTemplate it = Imgkers.getImage();
            it.drawScaled(0, 0, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, w, h - widgetInnerHeight/80, texKers1.getTexture(), true);
            it.drawScaled(0, it.getBaseHeight() / 2, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, w, h - widgetInnerHeight/80, texKers2.getTexture(), true);
            texKers1.setTranslation(widgetInnerWidth*119/200, widgetInnerHeight*56/100);
            texKers2.setTranslation(widgetInnerWidth*119/200, widgetInnerHeight*56/100);
            texKers1.setLocalZIndex(501);
            texKers2.setLocalZIndex(502);
            kersDirty = false;
        }
        collector.add(texKers1);
        collector.add(texKers2);
        
        if(texKersOn1 == null || texKersOn1.getWidth() != w || texKersOn1.getHeight() != h || kersOnDirty)
        {
            texKersOn1 = TransformableTexture.getOrCreate(w, h - widgetInnerHeight/80, true, texKersOn1, isEditorMode);
            texKersOn2 = TransformableTexture.getOrCreate(w, h - widgetInnerHeight/80, true, texKersOn2, isEditorMode);
            ImageTemplate it = ImgkersOn.getImage();
            it.drawScaled(0, 0, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, w, h - widgetInnerHeight/80, texKersOn1.getTexture(), true);
            it.drawScaled(0, it.getBaseHeight() / 2, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, w, h - widgetInnerHeight/80, texKersOn2.getTexture(), true);
            texKersOn1.setTranslation(widgetInnerWidth*119/200, widgetInnerHeight*56/100);
            texKersOn2.setTranslation(widgetInnerWidth*119/200, widgetInnerHeight*56/100);
            texKersOn1.setLocalZIndex(501);
            texKersOn2.setLocalZIndex(502);
            kersOnDirty = false;
        }
        collector.add(texKersOn1);
        collector.add(texKersOn2);
    }
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
        int fhSpeed = TextureImage2D.getStringHeight( "09gy", speedF11Font2 );
        int fhGear = TextureImage2D.getStringHeight( "09gy", gearF11Font2 );
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        if((scoringInfo.getSessionType() == SessionType.RACE1 || scoringInfo.getSessionType() == SessionType.RACE2 || scoringInfo.getSessionType() == SessionType.RACE3 || scoringInfo.getSessionType() == SessionType.RACE4) && useDRSlog.getValue())
            GetDrsLogFile(gameData);
        
        texKersOn1.setVisible(false);
        texKersOn2.setVisible(false);
        dsLCD1 = drawnStringFactory.newDrawnString( "dsLCD1", width*89/100, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, LCDFont.getFont(), isFontAntiAliased(), LCDFontColor.getColor() );
        dsLCD2 = drawnStringFactory.newDrawnString( "dsLCD2", width*94/100, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, LCDFont.getFont(), isFontAntiAliased(), LCDFontColor.getColor() );
        dsLCD3 = drawnStringFactory.newDrawnString( "dsLCD3", width*99/100, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, LCDFont.getFont(), isFontAntiAliased(), LCDFontColor.getColor() );
        //dsLCDback3 = drawnStringFactory.newDrawnString( "dsLCD3", width, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, LCDFont.getFont(), isFontAntiAliased(), LCDFontColor.getColor() );
        dsSpeed = drawnStringFactory.newDrawnString( "dsSpeed", width*20/100, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, speedF11Font2.getFont(), isFontAntiAliased(), GearFontColor2.getColor() );
        dsSpeed2 = drawnStringFactory.newDrawnString( "dsSpeed2", width*14/100, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, speedF11Font2.getFont(), isFontAntiAliased(), GearFontColor2.getColor() );
        dsSpeed3 = drawnStringFactory.newDrawnString( "dsSpeed3", width*8/100, height*71/100 - fhSpeed/2, Alignment.RIGHT, false, speedF11Font2.getFont(), isFontAntiAliased(), GearFontColor2.getColor() );
        dsGear = drawnStringFactory.newDrawnString( "dsGear", width*37/100, height*74/100 - fhGear/2, Alignment.RIGHT, false, gearF11Font2.getFont(), isFontAntiAliased(), GearFontColor2.getColor() );
        
        texShiftRed = ImgShiftRed.getImage().getScaledTextureImage( width*4/100, height*15/100, texShiftRed, isEditorMode );
        texShiftBlue = ImgShiftBlue.getImage().getScaledTextureImage( width*4/100, height*15/100, texShiftBlue, isEditorMode );
        texShiftGreen = ImgShiftGreen.getImage().getScaledTextureImage( width*4/100, height*15/100, texShiftGreen, isEditorMode );
        texShiftGear = ImgShiftGear.getImage().getScaledTextureImage( width*27/200, height*52/100, texShiftGear, isEditorMode );
        
        ImgRevmetter.updateSize( width, height, isEditorMode );
        texDRS = imgDRS.getImage().getScaledTextureImage( width*16/100, height*13/100, texDRS, isEditorMode );
        
        DRS.update(false);
        ButtonPress.update( 0 );
        
    }
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        int LapDistance = (int)scoringInfo.getViewedVehicleScoringInfo().getLapDistance();
        // && drs_finishline && LapDistance > deactivation.getValue()
        
        if(scoringInfo.getYellowFlagState() == YellowFlagState.LAST_LAP)
            LastSC = scoringInfo.getLeadersVehicleScoringInfo().getLapsCompleted() + 1;
        
        if((LapDistance < detection.getValue() && !drs_finishline) || (LapDistance < detection.getValue() && drs_finishline && LapDistance > deactivation.getValue()))
            DRS_detection.update( true );
        else
            DRS_detection.update( false );
        
        if(isRace && DRS_detection.hasChanged())
        {
            drs_lap = scoringInfo.getViewedVehicleScoringInfo().getCurrentLap();
            drs_pos = scoringInfo.getViewedVehicleScoringInfo().getPlace( false );
            drs_gap = scoringInfo.getViewedVehicleScoringInfo().getTimeBehindNextInFront( false ); 
        }
        
        //second drs zone
        if((LapDistance < detection2.getValue() && !drs_finishline2) || (LapDistance < detection2.getValue() && drs_finishline2 && LapDistance > deactivation2.getValue()))
            DRS_detection2.update( true );
        else
            DRS_detection2.update( false );
        
        if(isRace && DRS_detection2.hasChanged())
        {
            drs_lap2 = scoringInfo.getViewedVehicleScoringInfo().getCurrentLap();
            drs_pos2 = scoringInfo.getViewedVehicleScoringInfo().getPlace( false );
            drs_gap2 = scoringInfo.getViewedVehicleScoringInfo().getTimeBehindNextInFront( false );
        }
        //
        CurrentLap.update(scoringInfo.getViewedVehicleScoringInfo().getLapsCompleted());
        TelemetryData telemData = gameData.getTelemetryData();
        int lightoffset = (int)( gameData.getSetup().getEngine().getRevLimit()*shiftLightMulti.getValue() );
        float maxRPM; //telemData.getEngineMaxRPM()0.0074
        if ( useMaxRevLimit.getBooleanValue() )
            maxRPM = gameData.getPhysics().getEngine().getRevLimitRange().getMaxValue();
        else
            maxRPM = gameData.getSetup().getEngine().getRevLimit();
        
        if(HalfLights.getValue())
        {
            if(telemData.getEngineRPM() >= maxRPM - lightoffset )
                RPMLights.update( 1 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset )
                RPMLights.update( 2 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*3 )
                RPMLights.update( 4 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*5 )
                RPMLights.update( 6 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*7 )
                RPMLights.update( 8 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*9 )
                RPMLights.update( 10 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*11 )
                RPMLights.update( 12 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*13 )
                RPMLights.update( 14 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*15 )
                RPMLights.update( 16 );
            else
                RPMLights.update( 0 );
        }
        else
        {
            if(telemData.getEngineRPM() >= maxRPM - lightoffset )
                RPMLights.update( 1 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset )
                RPMLights.update( 2 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*2 )
                RPMLights.update( 3 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*3 )
                RPMLights.update( 4 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*4 )
                RPMLights.update( 5 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*5 )
                RPMLights.update( 6 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*6 )
                RPMLights.update( 7 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*7 )
                RPMLights.update( 8 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*8 )
                RPMLights.update( 9 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*9 )
                RPMLights.update( 10 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*10 )
                RPMLights.update( 11 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*11 )
                RPMLights.update( 12 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*12 )
                RPMLights.update( 13 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*13 )
                RPMLights.update( 14 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*14 )
                RPMLights.update( 15 );
            else if(telemData.getEngineRPM() >= maxRPM - lightoffset*15 )
                RPMLights.update( 16 );
            else
                RPMLights.update( 0 );
            
        }
        
        if(scoringInfo.getSessionNanos() < visibleEnd_DRS)
            DRS_VisibleEnd.update( true );
        else
            DRS_VisibleEnd.update( false );
        
        //scoringInfo.getYellowFlagState() != YellowFlagState.NONE && scoringInfo.getYellowFlagState() != YellowFlagState.PENDING &&
        //logCS(isRace, scoringInfo.getLeadersVehicleScoringInfo().getCurrentLap(), useDRSlog.getValue() , LapDistance > activation.getValue() , LapDistance < deactivation.getValue());
        //||  (drs_finishline && (LapDistance < activation.getValue() || LapDistance > deactivation.getValue()) )
        
        if(isRace && useDRSlog.getValue() && 
          (( drs_lap < 3 + LastSC || drs_gap > 1.0f || drs_pos == 1 || 
           (!drs_finishline && (LapDistance < activation.getValue() || LapDistance > deactivation.getValue()) ) || (drs_finishline && (LapDistance < activation.getValue() && LapDistance > deactivation.getValue()) ) ) && 
           ( drs_lap2 < 3 + LastSC || drs_gap2 > 1.0f || drs_pos2 == 1 || 
           (!drs_finishline2 && (LapDistance < activation2.getValue() || LapDistance > deactivation2.getValue()) ) || (drs_finishline2 && (LapDistance < activation2.getValue() && LapDistance > deactivation2.getValue()) ) )))
        {
            DRS_Available.update( false );
            DRS_Disabled.update( true );
            DRS_VisibleEnd.update( false ); 
            DRS.update(false);
        }
        else
        {
            DRS_Available.update( true );
            DRS_Disabled.update( false ); 
        }
    
        if((DRS.hasChanged() || DRS_VisibleEnd.hasChanged() || DRS_Disabled.hasChanged() || DRS_Available.hasChanged()) && !isEditorMode)
            forceCompleteRedraw(true);
        if(scoringInfo.getViewedVehicleScoringInfo().isPlayer())
        {
           if(CurrentLap.hasChanged())
                KersLeft = kerstime.getValue();
            
           if(!isEditorMode && RPMLights.hasChanged())
               forceCompleteRedraw(true);
           
           
           return true; 
        }
        return false;
         
    }
    
    
    
    
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        texture.drawImage( ImgRevmetter.getTexture(), offsetX, offsetY, true, null );
        TelemetryData telemData = gameData.getTelemetryData();
        float uBrake = isEditorMode ? 0.0F : telemData.getUnfilteredBrake();
        if(uBrake >= 0.1f && DRS_Available.getValue() && DeactivateBrake.getValue())
        {
            DRS_Available.update( false );
            DRS_Disabled.update( false );  
        }
        int lightoffset = (int)( gameData.getSetup().getEngine().getRevLimit()*shiftLightMulti.getValue() );
        float maxRPM; //telemData.getEngineMaxRPM()0.0074
        
        if ( useMaxRevLimit.getBooleanValue() )
            maxRPM = gameData.getPhysics().getEngine().getRevLimitRange().getMaxValue();
        else
            maxRPM = gameData.getSetup().getEngine().getRevLimit();
        
        if(DRS_Disabled.getValue())
            texDRS = imgDRSdisabled.getImage().getScaledTextureImage( width*38/200, height*36/100, texDRS, isEditorMode );
        else if(!DRS.getValue() && DRS_Available.getValue())
            texDRS = imgDRSavailable.getImage().getScaledTextureImage( width*38/200, height*36/100, texDRS, isEditorMode );
        else if(DRS_VisibleEnd.getValue())
            texDRS = imgDRS.getImage().getScaledTextureImage( width*38/200, height*36/100, texDRS, isEditorMode );
        else
            texDRS = imgDRS2.getImage().getScaledTextureImage( width*38/200, height*36/100, texDRS, isEditorMode );
        
        if(DRS.getValue() || isEditorMode || isRace && DRS_Disabled.getValue() || isRace && DRS_Available.getValue())
            texture.drawImage( texDRS, offsetX + width*79/200, offsetY + height*53/100, false, null );
        
        if(telemData.getEngineRPM() >= maxRPM - lightoffset || isEditorMode )
            texture.drawImage( texShiftGear, offsetX + width*49/200, offsetY + height*47/100, false, null );
        
        if(HalfLights.getValue())
        {
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*15 || isEditorMode )
                texture.drawImage( texShiftBlue, offsetX + width*188/200, offsetY + height*20/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*13 || isEditorMode )
                texture.drawImage( texShiftBlue, offsetX + width*175/200, offsetY + height*18/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*11 || isEditorMode)
                texture.drawImage( texShiftBlue, offsetX + width*162/200, offsetY + height*15/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*9 || isEditorMode)
                texture.drawImage( texShiftBlue, offsetX + width*149/200, offsetY + height*14/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*7 || isEditorMode)
                texture.drawImage( texShiftBlue, offsetX + width*136/200, offsetY + height*12/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*5 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*123/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*3 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*111/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*98/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*3 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*85/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*5 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*72/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*7 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*58/200, offsetY + height*12/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*9 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*45/200, offsetY + height*14/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*11 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*32/200, offsetY + height*15/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*13 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*19/200, offsetY + height*18/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*15  || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*6/200, offsetY + height*20/100, false, null );
            
        }
        else
        {
            if(telemData.getEngineRPM() >= maxRPM - lightoffset || isEditorMode )
                texture.drawImage( texShiftBlue, offsetX + width*188/200, offsetY + height*20/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*2 || isEditorMode )
                texture.drawImage( texShiftBlue, offsetX + width*175/200, offsetY + height*18/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*3 || isEditorMode)
                texture.drawImage( texShiftBlue, offsetX + width*162/200, offsetY + height*15/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*4 || isEditorMode)
                texture.drawImage( texShiftBlue, offsetX + width*149/200, offsetY + height*14/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*5 || isEditorMode)
                texture.drawImage( texShiftBlue, offsetX + width*136/200, offsetY + height*12/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*6 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*123/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*7 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*111/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*8 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*98/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*9 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*85/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*10 || isEditorMode)
                texture.drawImage( texShiftRed, offsetX + width*72/200, offsetY + height*11/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*11 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*58/200, offsetY + height*12/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*12 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*45/200, offsetY + height*14/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*13 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*32/200, offsetY + height*15/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*14 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*19/200, offsetY + height*18/100, false, null );
            if(telemData.getEngineRPM() >= maxRPM - lightoffset*15 || isEditorMode)
                texture.drawImage( texShiftGreen, offsetX + width*6/200, offsetY + height*20/100, false, null );
            
        }
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        TelemetryData telemData = gameData.getTelemetryData();
        float uBrake = isEditorMode ? 0.4F : telemData.getUnfilteredBrake();
        if(uBrake >= 0.1f && DeactivateBrake.getValue())
            DRS.update(false);
        
        cGear.update( telemData.getCurrentGear() );
        
        tenthOfSec.update((int)(gameData.getScoringInfo().getSessionNanos() / 10000000f));
        
        if(gameData.getProfileInfo().getMeasurementUnits() == MeasurementUnits.METRIC)
            cSpeed.update( (int)telemData.getScalarVelocityKPH() );
        else
            cSpeed.update( (int)telemData.getScalarVelocityMPH() );
            
            
        if(needsCompleteRedraw || cSpeed.hasChanged())
        {
            if(cSpeed.getValue() < 10)
            {
                dsSpeed.draw( offsetX, offsetY, String.valueOf( cSpeed.getValue() ), texture );
                dsSpeed2.draw( offsetX, offsetY, "0", texture );
                dsSpeed3.draw( offsetX, offsetY, "0", texture );
            }
            else
                if(cSpeed.getValue() < 100)
                {
                    dsSpeed.draw( offsetX, offsetY,  String.valueOf( cSpeed.getValue() ).substring( 1, 2 ) , texture );
                    dsSpeed2.draw( offsetX, offsetY, String.valueOf( cSpeed.getValue() ).substring( 0, 1 ), texture );
                    dsSpeed3.draw( offsetX, offsetY, "0", texture );
                
                }
                else
                {
                    dsSpeed.draw( offsetX, offsetY, String.valueOf( cSpeed.getValue() ).substring( 2, 3 ), texture );
                    dsSpeed2.draw( offsetX, offsetY, String.valueOf( cSpeed.getValue() ).substring( 1, 2 ), texture );
                    dsSpeed3.draw( offsetX, offsetY, String.valueOf( cSpeed.getValue() ).substring( 0, 1 ), texture );
                }
                    
        }
        
        if(needsCompleteRedraw || ( clock.c() && cGear.hasChanged() ))
        {
            if(cGear.getValue() == -1)
                dsGear.draw( offsetX, offsetY, "R", texture );
            else
                if(cGear.getValue() == 0)
                    dsGear.draw( offsetX, offsetY, "N", texture );
                else
                    dsGear.draw( offsetX, offsetY, String.valueOf( cGear.getValue() ), texture );
          
         }
        
        
        float uKers = isEditorMode ? 1.0F : KersLeft / kerstime.getValue();
        int w = texKers2.getWidth();
        int kers = (int)((float)w * Math.round(uKers*10)/10)+1;
        
        texKers2.setClipRect(0, 0, kers, texKers2.getHeight(), true);
        texKersOn2.setClipRect(0, 0, kers, texKersOn2.getHeight(), true);
        
        if(telemData.getTemporaryBoostFlag() && KersLeft >= 0f )
        {
            texKersOn1.setVisible(true);
            texKersOn2.setVisible(true);
            texKers1.setVisible(false);
            texKers2.setVisible(false);
            if(tenthOfSec.hasChanged())
            {
                KersLeft = KersLeft - (tenthOfSec.getValue() - oldTenthOfSec)/100f;
            }
            
        }
        else
        {
            texKers1.setVisible(true);
            texKers2.setVisible(true);
            texKersOn1.setVisible(false);
            texKersOn2.setVisible(false);
        }
        oldTenthOfSec = tenthOfSec.getValue();
        
        position.update(gameData.getScoringInfo().getOwnPlace( false ));
        if(gameData.getTelemetryData().getFuelUsageAverage() <= 0)
            fuel.update( (int)(gameData.getTelemetryData().getFuel() ));
        else
            fuel.update( (int)(gameData.getTelemetryData().getFuel() / gameData.getTelemetryData().getFuelUsageAverage()));
        
        if(ButtonPress.getValue() % 2 == 0 && (ButtonPress.hasChanged() || needsCompleteRedraw || position.hasChanged()))
        {
            dsLCD1.draw( offsetX, offsetY, "P", texture );
        
            if(position.getValue() < 10)
            {
                dsLCD2.draw( offsetX, offsetY, "0", texture );
                //dsLCDback3.draw( offsetX, offsetY, "8",Color.black ,texture );/ gameData.getTelemetryData().getFuelUsageAverage() 
                dsLCD3.draw( offsetX, offsetY, String.valueOf( position.getValue() ), texture );
            }
            else
            {
                dsLCD2.draw( offsetX, offsetY,String.valueOf( position.getValue() ).substring( 0, 1 ) , texture );
                dsLCD3.draw( offsetX, offsetY, String.valueOf( position.getValue() ).substring( 1, 2 ), texture );
            }
        }
        else
            if(ButtonPress.getValue() % 2 == 1 && (ButtonPress.hasChanged() || needsCompleteRedraw || fuel.hasChanged()))
            {
                dsLCD1.draw( offsetX, offsetY, "F", texture );
                
                if(fuel.getValue() >= 100)
                {
                    dsLCD1.draw( offsetX, offsetY,String.valueOf( fuel.getValue() ).substring( 0, 1 ) , texture );
                    dsLCD2.draw( offsetX, offsetY,String.valueOf( fuel.getValue() ).substring( 1, 2 ) , texture );
                    dsLCD3.draw( offsetX, offsetY, String.valueOf( fuel.getValue() ).substring( 2, 3 ), texture );
                }
                else
                    if(fuel.getValue() >= 10)
                    {
                        dsLCD2.draw( offsetX, offsetY,String.valueOf( fuel.getValue() ).substring( 0, 1 ) , texture );
                        dsLCD3.draw( offsetX, offsetY, String.valueOf( fuel.getValue() ).substring( 1, 2 ), texture );
                    }
                    else
                    {
                        dsLCD2.draw( offsetX, offsetY, "0", texture );
                        dsLCD3.draw( offsetX, offsetY, String.valueOf( fuel.getValue() ), texture );
                    }  
                
            }
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( kerstime, "" );
        writer.writeProperty( speedF11Font2, "" );
        writer.writeProperty( gearF11Font2, "" );
        writer.writeProperty( GearFontColor2, "" );
        writer.writeProperty( useMaxRevLimit, "" );
        writer.writeProperty( shiftLightMulti, "shiftLightMulti" );
        writer.writeProperty( useDRSlog, "" );
        writer.writeProperty( DeactivateBrake, "" );
        writer.writeProperty( HalfLights, "" );
        writer.writeProperty( LCDFont, "" );
        writer.writeProperty( LCDFontColor, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( kerstime ) );
        else if ( loader.loadProperty( speedF11Font2 ) );
        else if ( loader.loadProperty( gearF11Font2 ) );
        else if ( loader.loadProperty( LCDFont ) );
        else if ( loader.loadProperty( LCDFontColor ) );
        else if ( loader.loadProperty( GearFontColor2 ) );
        else if ( loader.loadProperty( useMaxRevLimit ) );
        else if ( loader.loadProperty( shiftLightMulti ) );
        else if ( loader.loadProperty( useDRSlog ) );
        else if ( loader.loadProperty( DeactivateBrake ) );
        else if ( loader.loadProperty( HalfLights ) );
        
        
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Misc" );
        propsCont.addProperty( kerstime );
        propsCont.addProperty( speedF11Font2 );
        propsCont.addProperty( gearF11Font2 );
        propsCont.addProperty( LCDFont );
        propsCont.addProperty( LCDFontColor );
        propsCont.addProperty( GearFontColor2 );
        propsCont.addProperty( useMaxRevLimit );
        propsCont.addProperty( shiftLightMulti );
        propsCont.addProperty( useDRSlog );
        propsCont.addProperty( DeactivateBrake );
        propsCont.addProperty( HalfLights );
        
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
    
    public ShiftLightsMetter2011Widget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 33f, 10f );
        getBackgroundProperty().setColorValue( "#00000000" );
        
    }
    
}
