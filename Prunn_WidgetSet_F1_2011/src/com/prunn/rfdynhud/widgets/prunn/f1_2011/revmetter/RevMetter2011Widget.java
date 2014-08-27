package com.prunn.rfdynhud.widgets.prunn.f1_2011.revmetter;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
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


public class RevMetter2011Widget extends Widget
{
    private DrawnString dsSpeed = null;
    private DrawnString dsSpeedUnit = null;
    private DrawnString dsGear = null;
    private DrawnString dsGearAvail1 = null;
    private DrawnString dsGearAvail2 = null;
    private DrawnString dsGearAvail3 = null;
    private DrawnString dsGearAvail4 = null;
    private DrawnString dsRPM1 = null;
    private DrawnString dsRPM2 = null;
    private DrawnString dsRPM3 = null;
    private DrawnString dsRPM4 = null;
    private DrawnString dsRPM5 = null;
    private DrawnString dsRPM6 = null;
    private DrawnString dsRPM7 = null;
    private DrawnString dsRPM8 = null;
    private TextureImage2D texDRS = null;
    private TransformableTexture texNeedle = null;
    private TransformableTexture texKers1;
    private TransformableTexture texKers2;
    private TransformableTexture texKersOn1;
    private TransformableTexture texKersOn2;
    private ImagePropertyWithTexture imgDRS = new ImagePropertyWithTexture("imgDRS", "prunn/f1_2011/revmetter/drs.png");
    private ImagePropertyWithTexture imgDRS2 = new ImagePropertyWithTexture("imgDRS2", "prunn/f1_2011/revmetter/drs2.png");
    private ImagePropertyWithTexture imgDRSdisabled = new ImagePropertyWithTexture("imgDRSdisabled", "prunn/f1_2011/revmetter/drs_disabled.png");
    private ImagePropertyWithTexture imgDRSavailable = new ImagePropertyWithTexture("imgDRSavailable", "prunn/f1_2011/revmetter/drs_available.png");
    private ImageProperty Imgkers = new ImageProperty("Imgkers", null, "prunn/f1_2011/revmetter/kers.png", false, false);
    private ImageProperty ImgkersOn = new ImageProperty("ImgkersOn", null, "prunn/f1_2011/revmetter/kerson.png", false, false);
    private ImageProperty imgRevNeedle = new ImageProperty("revNeedle", null, "prunn/f1_2011/revmetter/needle.png", false, false);
    private final ImageProperty ImgBrake = new ImageProperty("ImgBrake", null, "prunn/f1_2011/revmetter/brake.png", false, false);
    private final ImageProperty ImgThrottle = new ImageProperty("ImgThrottle", null, "prunn/f1_2011/revmetter/throttle.png", false, false);
    private final ImagePropertyWithTexture ImgRevmetter = new ImagePropertyWithTexture( "image", null, "prunn/f1_2011/revmetter/background.png", false, false ); 
    private boolean kersDirty;
    private boolean kersOnDirty;
    protected final FontProperty speedUnitFont = new FontProperty("speed Unit Font", "speedUnitFont");
    protected final FontProperty speedF11Font = new FontProperty("speed Font", "speedF11Font");
    protected final FontProperty gearF11Font = new FontProperty("gear Font", "gearF11Font");
    protected final FontProperty gearF11FontSide = new FontProperty("gear Font 2", "gearF11FontSide");
    protected final FontProperty rpmFont = new FontProperty("RPM Font", "rpmFont");
    private final ColorProperty speedUnitFontColor = new ColorProperty("speedUnitFontColor", "speedUnitFontColor");
    private final ColorProperty speedFontColor = new ColorProperty("speedFontColor", "speedFontColor");
    private final ColorProperty speedFontColor2 = new ColorProperty("speedFontColor2", "speedFontColor2");
    private final ColorProperty speedFontColor3 = new ColorProperty("speedFontColor3", "speedFontColor3");
    private final ColorProperty GearFontColor = new ColorProperty("Gear Font Color", "GearFontColor");
    private final ColorProperty GearFontColorL1 = new ColorProperty("GearFontColorL1", "GearFontColorL1");
    private final ColorProperty GearFontColorL2 = new ColorProperty("GearFontColorL2", "GearFontColorL2");
    private final ColorProperty rpmFontColor = new ColorProperty("rpmFontColor", "rpmFontColor");
    private ColorProperty SpeedResultFontColor;
    private IntValue cSpeed = new IntValue();
    private IntValue cGear = new IntValue();
    private final IntValue CurrentLap = new IntValue();
    protected final FloatProperty kerstime = new FloatProperty("Kers Time", 6.6f);
    private float KersLeft;
    private TransformableTexture texThrottle1;
    private TransformableTexture texThrottle2;
    private TransformableTexture texBrake1;
    private TransformableTexture texBrake2;
    private boolean throttleDirty = false;
    private boolean BrakeDirty = false;
    protected final FontProperty TBFont = new FontProperty("TBFont", "TBFont");
    private final ColorProperty TBFontColor = new ColorProperty("TBFont Color", "TBFontColor");
    private static final InputAction DRS_ACTION = new InputAction( "DRS Action", true );
    private static final InputAction DRS_ACTION_ON = new InputAction( "DRS On", true );
    private static final InputAction DRS_ACTION_OFF = new InputAction( "DRS Off", true );
    private BoolValue DRS = new BoolValue();
    private BoolValue DRS_Disabled = new BoolValue();
    private BoolValue DRS_Available = new BoolValue();
    private BoolValue DRS_VisibleEnd = new BoolValue();
    private BoolValue DRS_detection = new BoolValue();
    private BoolValue DRS_detection2 = new BoolValue();
    private long visibleEnd_DRS;
    private IntValue tenthOfSec = new IntValue();
    private int oldTenthOfSec;
    protected final BooleanProperty useMaxRevLimit = new BooleanProperty("useMaxRevLimit", "useMaxRevLimit", true);
    private IntValue detection = new IntValue();
    private IntValue activation = new IntValue();
    private IntValue deactivation = new IntValue();
    private IntValue detection2 = new IntValue();
    private IntValue activation2 = new IntValue();
    private IntValue deactivation2 = new IntValue();
    private BooleanProperty useDRSlog = new BooleanProperty("use DRS log", "use drs log", true);
    private BooleanProperty DeactivateBrake = new BooleanProperty("Deactivate DRS with Brake", "DeactivateBrake", false);
    private boolean isRace = false;
    private boolean drs_finishline = false;
    private float drs_gap = 0.0f;
    private int drs_pos = 0;
    private int drs_lap = 0;
    private boolean drs_finishline2 = false;
    private float drs_gap2 = 0.0f;
    private int drs_pos2 = 0;
    private int drs_lap2 = 0;
    private int LastSC = 0;
    
    
    
    public String getDefaultNamedColorValue(String name)
    {
        if(name.equals("StandardBackground"))
            return "#00000000";
        if(name.equals("StandardFontColor"))
            return "#FFFFFF";
        if(name.equals("speedFontColor"))
            return "#37DB37";
        if(name.equals("speedFontColor2"))
            return "#F2F458";
        if(name.equals("speedFontColor3"))
            return "#EA3618";
        if(name.equals("GearFontColor"))
            return "#E9E9E9";
        if(name.equals("GearFontColorL1"))
            return "#E9E9E98E";
        if(name.equals("GearFontColorL2"))
            return "#AEB0AC1F";
        if(name.equals("TBFontColor"))
            return "#FFFFFFD7";
        if(name.equals("rpmFontColor"))
            return "#BEBEBEBE";
        if(name.equals("speedUnitFontColor"))
            return "#FFFFFFA7";

        return null;
    }
    
    @Override
    public String getDefaultNamedFontValue(String name)
    {
        if(name.equals("StandardFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        if(name.equals("speedF11Font"))
            return FontUtils.getFontString("Dialog", Font.PLAIN, 44, true, true);
        if(name.equals("speedUnitFont"))
            return FontUtils.getFontString("Dialog", 1, 20, true, true);
        if(name.equals("gearF11Font"))
            return FontUtils.getFontString("Dialog", 1, 42, true, true);
        if(name.equals("gearF11FontSide"))
            return FontUtils.getFontString("Dialog", 1, 30, true, true);
        if(name.equals("TBFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        if(name.equals("rpmFont"))
            return FontUtils.getFontString("Dialog", 1, 18, true, true);
        
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
        
    }
    @Override
    public InputAction[] getInputActions()
    {
        return ( new InputAction[] { DRS_ACTION, DRS_ACTION_ON, DRS_ACTION_OFF } );
    }
    protected Boolean onVehicleControlChanged(VehicleScoringInfo viewedVSI, LiveGameData gameData, boolean isEditorMode)
    {
        super.onVehicleControlChanged(viewedVSI, gameData, isEditorMode);
        
        return viewedVSI.isPlayer();
    }
    
    
    @Override
    public void onCockpitEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onCockpitEntered( gameData, isEditorMode );
        String cpid = "Y29weXJpZ2h0QFBydW5uMjAxMQ";
        if(!isEditorMode)
            log(cpid);
    }
    private void drawBarLabel(String label, TextureImage2D texture, int offsetX, int offsetY, int width, int height)
    {
        Rectangle2D bounds = TextureImage2D.getStringBounds(label, TBFont);
        int lblOff = 9;
        if((double)lblOff > -bounds.getWidth() && lblOff < width)
            texture.drawString(label, offsetX + lblOff, (offsetY + (height - (int)bounds.getHeight()) / 2) - (int)bounds.getY(), bounds, TBFont.getFont(), isFontAntiAliased(), TBFontColor.getColor(), true, null);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean onBoundInputStateChanged( InputAction action, boolean state, int modifierMask, long when, LiveGameData gameData, boolean isEditorMode )
    {
        Boolean result = super.onBoundInputStateChanged( action, state, modifierMask, when, gameData, isEditorMode );
        
        if ( (action == DRS_ACTION && !DRS.getValue()) || action == DRS_ACTION_ON )
        {
            visibleEnd_DRS = gameData.getScoringInfo().getSessionNanos() + 300000000l;
            DRS.update(true);
        }
        else
            if ( action == DRS_ACTION || action == DRS_ACTION_OFF )
                DRS.update(false);
        
        
        return ( result );
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initSubTextures( LiveGameData gameData, boolean isEditorMode, int widgetInnerWidth, int widgetInnerHeight, SubTextureCollector collector )
    {
        
        int w = widgetInnerWidth*8/100;
        int h = widgetInnerHeight*35/100;
        int xtrans = widgetInnerWidth*90/100;
        int ytrans = widgetInnerHeight*36/100;
        
        
        if(texKers1 == null || texKers1.getWidth() != w || texKers1.getHeight() != h || kersDirty)
        {
            texKers1 = TransformableTexture.getOrCreate(widgetInnerWidth, widgetInnerHeight, true, texKers1, isEditorMode);
            texKers2 = TransformableTexture.getOrCreate(widgetInnerWidth, widgetInnerHeight, true, texKers2, isEditorMode);
            ImageTemplate it = Imgkers.getImage();
            it.drawScaled(0, 0, it.getBaseWidth()/ 2, it.getBaseHeight() , 0, 0, w, h, texKers1.getTexture(), true);
            it.drawScaled(it.getBaseWidth() / 2, 0, it.getBaseWidth(), it.getBaseHeight(), 0, 0, w*2 ,h , texKers2.getTexture(), true);
            texKers1.setTranslation( xtrans, ytrans );
            texKers2.setTranslation( xtrans, ytrans );
            texKers1.setLocalZIndex(501);
            texKers2.setLocalZIndex(502);
            kersDirty = false;
        }
        collector.add(texKers1);
        collector.add(texKers2);
        
        if(texKersOn1 == null || texKersOn1.getWidth() != w || texKersOn1.getHeight() != h || kersOnDirty)
        {
            
            texKersOn1 = TransformableTexture.getOrCreate(widgetInnerWidth, widgetInnerHeight, true, texKersOn1, isEditorMode);
            texKersOn2 = TransformableTexture.getOrCreate(widgetInnerWidth, widgetInnerHeight, true, texKersOn2, isEditorMode);
            ImageTemplate it = ImgkersOn.getImage();
            it.drawScaled(0, 0, it.getBaseWidth()/ 2, it.getBaseHeight() , 0, 0, w, h, texKersOn1.getTexture(), true);
            it.drawScaled(it.getBaseWidth() / 2, 0, it.getBaseWidth(), it.getBaseHeight(), 0, 0, w*2 ,h , texKersOn2.getTexture(), true);
            texKersOn1.setTranslation( xtrans, ytrans );
            texKersOn2.setTranslation( xtrans, ytrans );
            texKersOn1.setLocalZIndex(501);
            texKersOn2.setLocalZIndex(502);
            kersOnDirty = false;
        }
        collector.add(texKersOn1);
        collector.add(texKersOn2);
       
        int wTB = widgetInnerWidth*35/100;
        int hTB = widgetInnerHeight*9/100;
        //"THROTTLE"
        if(texThrottle1 == null || texThrottle1.getWidth() != wTB || texThrottle1.getHeight() != hTB || throttleDirty)
        {
            texThrottle1 = TransformableTexture.getOrCreate(wTB, hTB, true, texThrottle1, isEditorMode);
            texThrottle2 = TransformableTexture.getOrCreate(wTB, hTB, true, texThrottle2, isEditorMode);
            ImageTemplate it = ImgThrottle.getImage();
            it.drawScaled(0, 0, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, wTB, hTB, texThrottle1.getTexture(), true);
            it.drawScaled(0, it.getBaseHeight() / 2, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, wTB, hTB, texThrottle2.getTexture(), true);
            drawBarLabel(Loc.throttle, texThrottle1.getTexture(), 0, 2, texThrottle1.getWidth(), texThrottle1.getHeight());
            drawBarLabel(Loc.throttle, texThrottle2.getTexture(), 0, 2, texThrottle2.getWidth(), texThrottle2.getHeight());
            texThrottle1.setTranslation(widgetInnerWidth*28/100, widgetInnerHeight*71/100);
            texThrottle2.setTranslation(widgetInnerWidth*28/100, widgetInnerHeight*71/100);
            texThrottle1.setLocalZIndex(501);
            texThrottle2.setLocalZIndex(502);
            throttleDirty = false;
        }
        collector.add(texThrottle1);
        collector.add(texThrottle2);
        //"BRAKE"
        if(texBrake1 == null || texBrake1.getWidth() != wTB || texBrake1.getHeight() != hTB || BrakeDirty)
        {
            texBrake1 = TransformableTexture.getOrCreate(wTB, hTB, true, texBrake1, isEditorMode);
            texBrake2 = TransformableTexture.getOrCreate(wTB, hTB, true, texBrake2, isEditorMode);
            ImageTemplate it = ImgBrake.getImage();
            it.drawScaled(0, 0, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, wTB, hTB, texBrake1.getTexture(), true);
            it.drawScaled(0, it.getBaseHeight() / 2, it.getBaseWidth(), it.getBaseHeight() / 2, 0, 0, wTB, hTB, texBrake2.getTexture(), true);
            drawBarLabel(Loc.brake, texBrake1.getTexture(), 10, 2, texBrake1.getWidth(), texBrake1.getHeight());
            drawBarLabel(Loc.brake, texBrake2.getTexture(), 10, 2, texBrake2.getWidth(), texBrake2.getHeight());
            texBrake1.setTranslation(widgetInnerWidth*28/100, widgetInnerHeight*79/100);
            texBrake2.setTranslation(widgetInnerWidth*28/100, widgetInnerHeight*79/100);
            texBrake1.setLocalZIndex(501);
            texBrake2.setLocalZIndex(502);
            BrakeDirty = false;
        }
        collector.add(texBrake1);
        collector.add(texBrake2);
        //texNeedle = imgRevNeedle.getImage().getScaledTextureImage( widgetInnerWidth*5/100, widgetInnerHeight*5/100, texNeedle, isEditorMode );
        if(texNeedle == null)
        {
            texNeedle = imgRevNeedle.getImage().getScaledTransformableTexture( widgetInnerWidth*3/200, widgetInnerHeight*12/100, texNeedle, isEditorMode );
            texNeedle.setLocalZIndex(504);
            texNeedle.setTranslation( widgetInnerWidth*45/100, widgetInnerHeight*11/100 );
            texNeedle.setRotationCenter( widgetInnerWidth/45/100, widgetInnerHeight*41/100 );
        }
        collector.add( texNeedle );
    }
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
        int fhSpeed = TextureImage2D.getStringHeight( "09gy", speedF11Font );
        int fhSpeedUnit = TextureImage2D.getStringHeight( "09gy", speedUnitFont );
        int fhGear = TextureImage2D.getStringHeight( "09gy", gearF11Font );
        int fhGear2 = TextureImage2D.getStringHeight( "09gy", gearF11FontSide );
        int fhRPM = TextureImage2D.getStringHeight( "09gy", gearF11FontSide );
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        if((scoringInfo.getSessionType() == SessionType.RACE1 || scoringInfo.getSessionType() == SessionType.RACE2 || scoringInfo.getSessionType() == SessionType.RACE3 || scoringInfo.getSessionType() == SessionType.RACE4) && useDRSlog.getValue())
            GetDrsLogFile(gameData);
        
        texKersOn1.setVisible(false);
        texKersOn2.setVisible(false);
        dsSpeed = drawnStringFactory.newDrawnString( "dsSpeed", width*45/100, height*32/100 - fhSpeed/2, Alignment.CENTER, false, speedF11Font.getFont(), isFontAntiAliased(), speedFontColor.getColor() );
        dsSpeedUnit = drawnStringFactory.newDrawnString( "dsSpeedUnit", width*45/100, height*45/100 - fhSpeedUnit/2, Alignment.CENTER, false, speedUnitFont.getFont(), isFontAntiAliased(), speedUnitFontColor.getColor() );
        dsGear = drawnStringFactory.newDrawnString( "dsGear", width*45/100, height*60/100 - fhGear/2, Alignment.CENTER, false, gearF11Font.getFont(), isFontAntiAliased(), GearFontColor.getColor() );
        dsGearAvail1 = drawnStringFactory.newDrawnString( "dsGearAvail1", width*25/100, height*60/100 - fhGear2/2, Alignment.CENTER, false, gearF11FontSide.getFont(), isFontAntiAliased(), GearFontColorL2.getColor() );
        dsGearAvail2 = drawnStringFactory.newDrawnString( "dsGearAvail2", width*35/100, height*60/100 - fhGear2/2, Alignment.CENTER, false, gearF11FontSide.getFont(), isFontAntiAliased(), GearFontColorL1.getColor() );
        dsGearAvail3 = drawnStringFactory.newDrawnString( "dsGearAvail3", width*55/100, height*60/100 - fhGear2/2, Alignment.CENTER, false, gearF11FontSide.getFont(), isFontAntiAliased(), GearFontColorL1.getColor() );
        dsGearAvail4 = drawnStringFactory.newDrawnString( "dsGearAvail4", width*65/100, height*60/100 - fhGear2/2, Alignment.CENTER, false, gearF11FontSide.getFont(), isFontAntiAliased(), GearFontColorL2.getColor() );
        
        dsRPM1 = drawnStringFactory.newDrawnString( "dsRPM1", width*18/100, height*84/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM2 = drawnStringFactory.newDrawnString( "dsRPM2", width*6/100, height*57/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM3 = drawnStringFactory.newDrawnString( "dsRPM3", width*12/100, height*30/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM4 = drawnStringFactory.newDrawnString( "dsRPM4", width*32/100, height*12/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM5 = drawnStringFactory.newDrawnString( "dsRPM5", width*58/100, height*12/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM6 = drawnStringFactory.newDrawnString( "dsRPM6", width*79/100, height*30/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM7 = drawnStringFactory.newDrawnString( "dsRPM7", width*84/100, height*57/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        dsRPM8 = drawnStringFactory.newDrawnString( "dsRPM8", width*73/100, height*84/100 - fhRPM/2, Alignment.CENTER, false, rpmFont.getFont(), isFontAntiAliased(), rpmFontColor.getColor() );
        
        ImgRevmetter.updateSize( width, height, isEditorMode );
        texDRS = imgDRS.getImage().getScaledTextureImage( width*16/100, height*13/100, texDRS, isEditorMode );
        
        DRS.update(false);
        
    }
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        int LapDistance = (int)scoringInfo.getViewedVehicleScoringInfo().getLapDistance();
        
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
        
        CurrentLap.update(gameData.getScoringInfo().getViewedVehicleScoringInfo().getLapsCompleted());
        if(gameData.getScoringInfo().getSessionNanos() < visibleEnd_DRS)
            DRS_VisibleEnd.update( true );
        else
            DRS_VisibleEnd.update( false );
        
      //logCS(isRace, scoringInfo.getLeadersVehicleScoringInfo().getCurrentLap(), useDRSlog.getValue() , LapDistance > activation.getValue() , LapDistance < deactivation.getValue());
        //||  (drs_finishline && (LapDistance < activation.getValue() || LapDistance > deactivation.getValue()) )
        
        //oldcodeif(isRace && useDRSlog.getValue() && ( drs_lap < 3 || drs_gap > 1.0f || drs_pos == 1 || (!drs_finishline && (LapDistance < activation.getValue() || LapDistance > deactivation.getValue()) )||  (drs_finishline && (LapDistance < activation.getValue() && LapDistance > deactivation.getValue()) ) ))
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
        
        if(DRS_Disabled.getValue())
            texDRS = imgDRSdisabled.getImage().getScaledTextureImage( width*16/100, height*13/100, texDRS, isEditorMode );
        else if(!DRS.getValue() && DRS_Available.getValue())
            texDRS = imgDRSavailable.getImage().getScaledTextureImage( width*16/100, height*13/100, texDRS, isEditorMode );
        else if(DRS_VisibleEnd.getValue())
            texDRS = imgDRS.getImage().getScaledTextureImage( width*16/100, height*13/100, texDRS, isEditorMode );
        else
            texDRS = imgDRS2.getImage().getScaledTextureImage( width*16/100, height*13/100, texDRS, isEditorMode );
        
        if(DRS.getValue() || isEditorMode || isRace && DRS_Disabled.getValue() || isRace && DRS_Available.getValue())
            texture.drawImage( texDRS, offsetX + width*82/100, offsetY + height*74/100, false, null );
        
        
        
            
    }
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        TelemetryData telemData = gameData.getTelemetryData();
        float uThrottle = isEditorMode ? 0.4F : telemData.getUnfilteredThrottle();
        float uBrake = isEditorMode ? 0.4F : telemData.getUnfilteredBrake();
        int wT = texThrottle2.getWidth();
        int throttle = (int)((float)wT * uThrottle);
        int brake = (int)((float)wT * uBrake);
        float uRPM;
        if ( useMaxRevLimit.getBooleanValue() )
            uRPM = isEditorMode ? 0.045F : telemData.getEngineRPM() / gameData.getPhysics().getEngine().getRevLimitRange().getMaxValue();
        else
            uRPM = isEditorMode ? 0.045F : telemData.getEngineRPM() / gameData.getSetup().getEngine().getRevLimit();
        
        if(uBrake >= 0.1f && DeactivateBrake.getValue())
            DRS.update(false);
        texThrottle2.setClipRect(0, 0, throttle, texThrottle2.getHeight(), true);
        texBrake2.setClipRect(0, 0, brake, texBrake2.getHeight(), true);
        
        float uKers = isEditorMode ? 0.5F : KersLeft / kerstime.getValue();
        int h = texKers2.getHeight();
        int roundedKers = Math.round( uKers*10 + 0.3f );
        
        texKers2.setClipRect(0, (int)(height*35/100* (1 - roundedKers/10f)), texKers2.getWidth(), h, true);
        texKersOn2.setClipRect(0, (int)(height*35/100* (1 - roundedKers/10f)), texKersOn2.getWidth(), h, true);
        cGear.update( telemData.getCurrentGear() );
        
        tenthOfSec.update((int)(gameData.getScoringInfo().getSessionNanos() / 10000000f));
        
        if(telemData.getTemporaryBoostFlag() && KersLeft >= 0f )
        {
            texKersOn1.setVisible(true);
            texKersOn2.setVisible(true);
            texKers1.setVisible(false);
            texKers2.setVisible(false);
            //clock.c()
            
            if(tenthOfSec.hasChanged())
            {
                //log(tenthOfSec.getValue());
                //KersLeft = KersLeft - 0.015f;
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
        if(gameData.getProfileInfo().getMeasurementUnits() == MeasurementUnits.METRIC)
        {
            cSpeed.update( (int)telemData.getScalarVelocityKmh() );
            if(cSpeed.getValue() < 200)
                SpeedResultFontColor = speedFontColor;
            else
                if(cSpeed.getValue() < 300)
                    SpeedResultFontColor = speedFontColor2;
                else
                    SpeedResultFontColor = speedFontColor3;
        }
        else
        {
            cSpeed.update( (int)telemData.getScalarVelocityMih() );
            if(cSpeed.getValue() < 124)
                SpeedResultFontColor = speedFontColor;
            else
                if(cSpeed.getValue() < 186)
                    SpeedResultFontColor = speedFontColor2;
                else
                    SpeedResultFontColor = speedFontColor3;
        }
        
        
            
        if(needsCompleteRedraw || cSpeed.hasChanged())
        {
            dsSpeed.draw( offsetX, offsetY, String.valueOf( cSpeed.getValue() ), SpeedResultFontColor.getColor(), texture );
            
            if(gameData.getProfileInfo().getMeasurementUnits() == MeasurementUnits.METRIC)
                dsSpeedUnit.draw( offsetX, offsetY, "km/h", texture );
            else
                dsSpeedUnit.draw( offsetX, offsetY, "mph", texture );
             
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
                    
            if(cGear.getValue() == 1)
                dsGearAvail1.draw( offsetX, offsetY, "R", texture );
            else
                if(cGear.getValue() == 2)
                    dsGearAvail1.draw( offsetX, offsetY, "N", texture );
                else
                    if(cGear.getValue() > 0)
                        dsGearAvail1.draw( offsetX, offsetY, String.valueOf( cGear.getValue() - 2 ), texture );
                    else
                        dsGearAvail1.draw( offsetX, offsetY, "", texture );
                
            if(cGear.getValue() == 0)
                dsGearAvail2.draw( offsetX, offsetY, "R", texture );
            else
                if(cGear.getValue() == 1)
                    dsGearAvail2.draw( offsetX, offsetY, "N", texture );
                else
                    if(cGear.getValue() > 1)
                        dsGearAvail2.draw( offsetX, offsetY, String.valueOf( cGear.getValue() - 1 ), texture );
                    else
                        dsGearAvail2.draw( offsetX, offsetY, "", texture );
                    
            if(cGear.getValue() == -1)
                dsGearAvail3.draw( offsetX, offsetY, "N", texture );
            else
                if(cGear.getValue() < gameData.getPhysics().getNumForwardGears() )//7
                    dsGearAvail3.draw( offsetX, offsetY, String.valueOf( cGear.getValue() + 1 ), texture );
                else
                    dsGearAvail3.draw( offsetX, offsetY, "", texture );
                
            if(cGear.getValue() < gameData.getPhysics().getNumForwardGears() - 1 )//6
                dsGearAvail4.draw( offsetX, offsetY, String.valueOf( cGear.getValue() + 2 ), texture );
            else
                dsGearAvail4.draw( offsetX, offsetY, "", texture );
         }
        
          
        if(uRPM < 0.045f)
           uRPM = 0.045f;
        
        texNeedle.setRotationInDegrees( uRPM*274 - 145 );
        
           
        if(needsCompleteRedraw)
        {
            
            float maxRPM; //telemData.getEngineMaxRPM()
            if ( useMaxRevLimit.getBooleanValue() )
                maxRPM = gameData.getPhysics().getEngine().getRevLimitRange().getMaxValue();
            else
                maxRPM = gameData.getSetup().getEngine().getRevLimit();
            
            dsRPM1.draw( offsetX, offsetY, String.valueOf((int)maxRPM*4/18000), texture );
            dsRPM2.draw( offsetX, offsetY, String.valueOf((int)maxRPM*6/18000), texture );
            dsRPM3.draw( offsetX, offsetY, String.valueOf((int)maxRPM*8/18000), texture );
            dsRPM4.draw( offsetX, offsetY, String.valueOf((int)maxRPM*10/18000), texture );
            dsRPM5.draw( offsetX, offsetY, String.valueOf((int)maxRPM*12/18000), texture );
            dsRPM6.draw( offsetX, offsetY, String.valueOf((int)maxRPM*14/18000), texture );
            dsRPM7.draw( offsetX, offsetY, String.valueOf((int)maxRPM*16/18000), texture );
            dsRPM8.draw( offsetX, offsetY, String.valueOf((int)maxRPM/1000), texture );
            
        }
        
        
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( kerstime, "" );
        writer.writeProperty( speedF11Font, "" );
        writer.writeProperty( speedFontColor, "" );
        writer.writeProperty( speedFontColor2, "" );
        writer.writeProperty( speedFontColor3, "" );
        writer.writeProperty( speedUnitFont, "" );
        writer.writeProperty( speedUnitFontColor, "" );
        writer.writeProperty( gearF11Font, "" );
        writer.writeProperty( gearF11FontSide, "" );
        writer.writeProperty( GearFontColor, "" );
        writer.writeProperty( GearFontColorL1, "" );
        writer.writeProperty( GearFontColorL2, "" );
        writer.writeProperty( TBFont, "" );
        writer.writeProperty( TBFontColor, "" );
        writer.writeProperty( rpmFont, "" );
        writer.writeProperty( rpmFontColor, "" );
        writer.writeProperty( useMaxRevLimit, "" );
        writer.writeProperty( useDRSlog, "" );
        writer.writeProperty( DeactivateBrake, "" );
        
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( kerstime ) );
        else if ( loader.loadProperty( speedF11Font ) );
        else if ( loader.loadProperty( speedFontColor ) );
        else if ( loader.loadProperty( speedFontColor2 ) );
        else if ( loader.loadProperty( speedFontColor3 ) );
        else if ( loader.loadProperty( speedUnitFont ) );
        else if ( loader.loadProperty( speedUnitFontColor ) );
        else if ( loader.loadProperty( gearF11Font ) );
        else if ( loader.loadProperty( gearF11FontSide ) );
        else if ( loader.loadProperty( GearFontColor ) );
        else if ( loader.loadProperty( GearFontColorL1 ) );
        else if ( loader.loadProperty( GearFontColorL2 ) );
        else if ( loader.loadProperty( TBFont ) );
        else if ( loader.loadProperty( TBFontColor ) );
        else if ( loader.loadProperty( rpmFont ) );
        else if ( loader.loadProperty( rpmFontColor ) );
        else if ( loader.loadProperty( useMaxRevLimit ) );
        else if ( loader.loadProperty( useDRSlog ) );
        else if ( loader.loadProperty( DeactivateBrake ) );
        
        
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Misc" );
        propsCont.addProperty( kerstime );
        propsCont.addProperty( speedF11Font );
        propsCont.addProperty( speedFontColor );
        propsCont.addProperty( speedFontColor2 );
        propsCont.addProperty( speedFontColor3 );
        propsCont.addProperty( speedUnitFont );
        propsCont.addProperty( speedUnitFontColor );
        propsCont.addProperty( gearF11Font );
        propsCont.addProperty( gearF11FontSide );
        propsCont.addProperty( GearFontColor );
        propsCont.addProperty( GearFontColorL1 );
        propsCont.addProperty( GearFontColorL2 );
        propsCont.addProperty( TBFont );
        propsCont.addProperty( TBFontColor );
        propsCont.addProperty( rpmFont );
        propsCont.addProperty( rpmFontColor );
        propsCont.addProperty( useMaxRevLimit );
        propsCont.addProperty( useDRSlog );
        propsCont.addProperty( DeactivateBrake );
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
    
    public RevMetter2011Widget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 23.4f, 26.7f );
        getBackgroundProperty().setColorValue( "#00000000" );
        
    }
    
}
