package com.prunn.rfdynhud.widgets.prunn.f1_2011.qualtimingtower;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import net.ctdp.rfdynhud.gamedata.FinishStatus;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.input.InputAction;
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
import net.ctdp.rfdynhud.util.StandingsTools;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.FloatValue;
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


public class QualifTimingTowerWidget extends Widget
{
    private DrawnString[] dsPos = null;
    private DrawnString[] dsName = null;
    private DrawnString[] dsTime = null;
    //private DrawnString[] dsIsInPit = null;
    private TextureImage2D texPit = null;
    private final ImagePropertyWithTexture imgFirst = new ImagePropertyWithTexture( "imgFirst", "prunn/f1_2011/tower/bg_first.png" );
    private final ImagePropertyWithTexture imgFirstNew = new ImagePropertyWithTexture( "imgFirstNew", "prunn/f1_2011/tower/bg_first_newlap.png" );
    private final ImagePropertyWithTexture imgPos = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/tower/bg.png" );
    private final ImagePropertyWithTexture imgPosNew = new ImagePropertyWithTexture( "imgPosNew", "prunn/f1_2011/tower/bg_newlap.png" );
    private final ImagePropertyWithTexture imgPosKnockOut = new ImagePropertyWithTexture( "imgPosKnockOut", "prunn/f1_2011/tower/bg_knockout.png" );
    private final ImagePropertyWithTexture imgPosNewKnockOut = new ImagePropertyWithTexture( "imgPosNewKnockOut", "prunn/f1_2011/tower/bg_newlap_knockout.png" );
    private final ImagePropertyWithTexture imgPit = new ImagePropertyWithTexture( "imgPit", "prunn/f1_2011/tower/InPit.png" );
    private final ImagePropertyWithTexture imgFinish = new ImagePropertyWithTexture( "imgFinish", "prunn/f1_2011/tower/finished.png" );
    
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor2 = new ColorProperty( "fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME );
    private final ColorProperty KnockoutFontColor = new ColorProperty("Knockout Font Color", PrunnWidgetSetf1_2011.FONT_COLOR4_NAME);
    
    private final IntProperty numVeh = new IntProperty( "numberOfVehicles", 8 );
    private IntProperty knockoutQual = new IntProperty("Knockout position Qual", 10);
    private IntProperty knockoutFP1 = new IntProperty("Knockout position FP1", 16);
    private IntProperty knockoutFP2 = new IntProperty("Knockout position FP2", 16);
    private IntProperty knockoutFP3 = new IntProperty("Knockout position FP3", 16);
    private IntProperty knockoutFP4 = new IntProperty("Knockout position FP4", 16);
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    private IntProperty fontxposoffset = new IntProperty("X Position Font Offset", 0);
    private IntProperty fontxnameoffset = new IntProperty("X Name Font Offset", 0);
    private IntProperty fontxtimeoffset = new IntProperty("X Time Font Offset", 0);
    
    private final DelayProperty visibleTime = new DelayProperty( "visibleTime", DelayProperty.DisplayUnits.SECONDS, 5 );
    private final DelayProperty visibleTimeButton = new DelayProperty( "visibleTimeButton", DelayProperty.DisplayUnits.SECONDS, 15 );
    private long visibleEnd = -1L;
    private long[] visibleEndArray;
    
    private VehicleScoringInfo[] vehicleScoringInfos;
    private IntValue[] positions = null;
    private final IntValue numValid = new IntValue();
    private StringValue[] driverNames = null;
    private FloatValue[] gaps = null;
    private BoolValue[] IsInPit = null;
    private BoolValue[] IsFinished = null;
    private int knockout;
    private int[] driverIDs = null;
    private boolean[] gapFlag = null;
    private boolean[] gapFlag2 = null;
    private static final InputAction showTimes = new InputAction( "Show times", true );
    private final IntValue inputShowTimes = new IntValue();
    private BooleanProperty AbsTimes = new BooleanProperty("Use absolute times", false) ;
    
    @Override
    public InputAction[] getInputActions()
    {
        return ( new InputAction[] { showTimes } );
    }
    @Override
    public void onCockpitEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onCockpitEntered( gameData, isEditorMode );
        String cpid = "Y29weXJpZ2h0QFBydW5uMjAxMQ";
        if(!isEditorMode)
            log(cpid);
        visibleEnd = -1L;
        numValid.reset();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean onBoundInputStateChanged( InputAction action, boolean state, int modifierMask, long when, LiveGameData gameData, boolean isEditorMode )
    {
        Boolean result = super.onBoundInputStateChanged( action, state, modifierMask, when, gameData, isEditorMode );
        int maxNumItems = numVeh.getValue();
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        if ( action == showTimes )
        {
            for(int i = 1; i < maxNumItems;i++)
                visibleEndArray[i] = scoringInfo.getSessionNanos() + visibleTimeButton.getDelayNanos();
            
           inputShowTimes.update( inputShowTimes.getValue()+1 );
        }
        
        return ( result );
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
        gapFlag = new boolean[maxNumItems];
        gapFlag2 = new boolean[maxNumItems];
        positions = new IntValue[maxNumItems];
        driverNames = new StringValue[maxNumItems];
        IsInPit = new BoolValue[maxNumItems];
        IsFinished = new BoolValue[maxNumItems];
        driverIDs = new int[maxNumItems];
        visibleEndArray = new long[maxNumItems];
        vehicleScoringInfos = new VehicleScoringInfo[maxNumItems];
        
        for(int i=0;i < maxNumItems;i++)
        { 
            IsInPit[i] = new BoolValue();
            IsFinished[i] = new BoolValue();
            positions[i] = new IntValue();
            driverNames[i] = new StringValue();
            gaps[i] = new FloatValue();
        }
        
        
    }
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
        int maxNumItems = numVeh.getValue();
        int fh = TextureImage2D.getStringHeight( "0%C", getFontProperty() );
        int rowHeight = height / maxNumItems;
        int fieldWidth1 = rowHeight;
        int fieldWidth2 = Math.round(width * 0.28f);
        int fieldWidth3 = width - fieldWidth1 - fieldWidth2;
        
        imgPosNew.updateSize( Math.round(width * 0.88f), rowHeight, isEditorMode );
        imgFirst.updateSize( Math.round(width * 0.88f), rowHeight, isEditorMode );
        imgPos.updateSize( Math.round(width * 0.5f), rowHeight, isEditorMode );
        imgFirstNew.updateSize( Math.round(width * 0.88f), rowHeight, isEditorMode );
        //imgPitFirst.updateSize( width, rowHeight, isEditorMode );
        //imgPosPit.updateSize( Math.round(width * 0.6f), rowHeight, isEditorMode );
        
        imgPosKnockOut.updateSize( Math.round(width * 0.5f), rowHeight, isEditorMode );
        imgPosNewKnockOut.updateSize( Math.round(width * 0.88f), rowHeight, isEditorMode );
        //imgPosPitKnockOut.updateSize( Math.round(width * 0.6f), rowHeight, isEditorMode );
        
        Color whiteFontColor = fontColor2.getColor();
        
        dsPos = new DrawnString[maxNumItems];
        dsName = new DrawnString[maxNumItems];
        dsTime = new DrawnString[maxNumItems];
        //dsIsInPit = new DrawnString[maxNumItems];
        
        int top = ( rowHeight - fh ) / 2;
        
        for(int i=0;i < maxNumItems;i++)
        {
            dsPos[i] = drawnStringFactory.newDrawnString( "dsPos", fieldWidth1 / 2 + fontxposoffset.getValue() + 8, top + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            dsName[i] = drawnStringFactory.newDrawnString( "dsName", fieldWidth1 + 10 + fontxnameoffset.getValue() + 7, top + fontyoffset.getValue(), Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            dsTime[i] = drawnStringFactory.newDrawnString( "dsTime", fieldWidth1 + fieldWidth2 + fieldWidth3 * 5 / 6 + fontxtimeoffset.getValue() - 18, top + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), whiteFontColor );
            /*if(i==0)
                dsIsInPit[0] = drawnStringFactory.newDrawnString( "dsIsInPit", width * 96/100, top + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), KnockoutFontColor.getColor() );
            else
                dsIsInPit[i] = drawnStringFactory.newDrawnString( "dsIsInPit", width * 57/100, top + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), KnockoutFontColor.getColor() );
            */
            top += rowHeight;
        }
        
        switch(gameData.getScoringInfo().getSessionType())
        {
            case QUALIFYING1: case QUALIFYING2: case QUALIFYING3: case QUALIFYING4:
                knockout = knockoutQual.getValue();
                break;
            case PRACTICE1:
                knockout = knockoutFP1.getValue();
                break;
            case PRACTICE2:
                knockout = knockoutFP2.getValue();
                break;
            case PRACTICE3:
                knockout = knockoutFP3.getValue();
                break;
            case PRACTICE4:
                knockout = knockoutFP4.getValue();
                break;
            default:
                knockout = 100;
                break;
        }
        if(isEditorMode)
            knockout = knockoutQual.getValue();
        
        texPit = imgPit.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
        
    }
    
    @Override
    protected Boolean updateVisibility( LiveGameData gameData, boolean isEditorMode )
    {
        super.updateVisibility( gameData, isEditorMode );
        
        initValues();
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        int drawncars = Math.min( scoringInfo.getNumVehicles(), numVeh.getValue() );
        VehicleScoringInfo  comparedVSI = scoringInfo.getViewedVehicleScoringInfo();
        
        if(inputShowTimes.hasChanged())
            forceCompleteRedraw( true ); 
        if(scoringInfo.getViewedVehicleScoringInfo().getBestLapTime() > 0)
        {
            if(scoringInfo.getViewedVehicleScoringInfo().getPlace( false ) > numVeh.getValue())
                comparedVSI = scoringInfo.getVehicleScoringInfo( scoringInfo.getViewedVehicleScoringInfo().getPlace( false ) - 5 );
            else
                comparedVSI = scoringInfo.getLeadersVehicleScoringInfo();
        
        }
        else
        {
            comparedVSI = scoringInfo.getLeadersVehicleScoringInfo();
            /*for(int i=drawncars-1; i >= 0; i--)
            {
                if(scoringInfo.getVehicleScoringInfo( i ).getBestLapTime() > 0)
                {
                    comparedVSI = scoringInfo.getVehicleScoringInfo( i ); 
                    break;
                }
            }*/
        }

        StandingsTools.getDisplayedVSIsForScoring(scoringInfo, comparedVSI, false, StandingsView.RELATIVE_TO_LEADER, true, vehicleScoringInfos);
        
        for(int i=0;i < drawncars;i++)
        { 
            VehicleScoringInfo vsi = vehicleScoringInfos[i];
            
            if(vsi != null && vsi.getFinishStatus() != FinishStatus.DQ)
            {

                positions[i].update( vsi.getPlace( false ) );
                driverNames[i].update(PrunnWidgetSetf1_2011.generateThreeLetterCode2( vsi.getDriverName(), gameData.getFileSystem().getConfigFolder() ));
                IsInPit[i].update( vsi.isInPits() );
                
                if(vsi.getFinishStatus() == FinishStatus.FINISHED)
                    IsFinished[i].update( true );
                else
                    IsFinished[i].update( false );
                
                gaps[i].setUnchanged();
                gaps[i].update(vsi.getBestLapTime());
                gapFlag[i] = gaps[i].hasChanged( false ) || isEditorMode;
                gapFlag2[i] = gapFlag[i];// || gapFlag2[i];
                if((IsInPit[i].hasChanged() || IsFinished[i].hasChanged()) && !isEditorMode)
                    forceCompleteRedraw( true );  
            }
        }
        
        if((scoringInfo.getSessionNanos() >= visibleEnd) && (visibleEnd != -1L))
        {
            visibleEnd = -1L;
            if ( !isEditorMode )
                forceCompleteRedraw( true );
        }
        
        if(!gaps[0].isValid())
            visibleEnd = -1L;
        else if(gapFlag[0])
            visibleEnd = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
        
        for(int i=1;i < drawncars;i++)
        {
            if(gaps[i].isValid())
            {
                if(gapFlag[i] && !isEditorMode )
                {
                    //search if the time really changed or just the position before redrawing
                    for(int j=0;j < drawncars; j++)
                    {
                        if ( vehicleScoringInfos[i].getDriverId() == driverIDs[j] )
                        {
                            if(gaps[i].getValue() == gaps[j].getOldValue())
                            {
                                gapFlag[i] = false;
                                break;
                            }
                        }
                    }
                }
                
                if((scoringInfo.getSessionNanos() >= visibleEndArray[i]) && (visibleEndArray[i] != -1L))
                {
                    visibleEndArray[i] = -1L;
                    if ( !isEditorMode )
                        forceCompleteRedraw( true );
                }
                
                if(gapFlag[i]) 
                {
                    visibleEndArray[i] = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
                    if ( !isEditorMode )
                        forceCompleteRedraw( true );
                }
            }
        }
        
        for(int i=0;i < drawncars;i++)
        { 
            VehicleScoringInfo vsi = vehicleScoringInfos[i];
            
            if(vsi != null)
            {
                driverIDs[i] = vsi.getDriverId();
            }
        }
        
        int nv = 0;
        for(int i=0;i < drawncars;i++)
        {
            if(gaps[i].isValid())
                nv++;
        }
        
        numValid.update( nv );
        if ( numValid.hasChanged() && !isEditorMode )
            forceCompleteRedraw( true );
        
        if( gameData.getScoringInfo().getLeadersVehicleScoringInfo().getBestLapTime() > 0 || isEditorMode)
        {
            return true;
        }
        
        return false;
        
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        int maxNumItems = numVeh.getValue();
        int drawncars = Math.min( scoringInfo.getNumVehicles(), maxNumItems );
        int rowHeight = height / maxNumItems;
        
        if(gaps[0].isValid())
        {
            if(scoringInfo.getSessionNanos() < visibleEnd )
                texture.clear( imgFirstNew.getTexture(), offsetX, offsetY, false, null );
            else
                texture.clear( imgFirst.getTexture(), offsetX, offsetY, false, null );
          
            
            if(IsInPit[0].getValue())
            {
                texPit = imgPit.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
                texture.drawImage( texPit, offsetX + width*82/100, offsetY, false, null );
            }
            else
                if(IsFinished[0].getValue())
                {
                    texPit = imgFinish.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
                    texture.drawImage( texPit, offsetX + width*82/100, offsetY, false, null );
                }
            
            
        }
        
        
        for(int i=1;i < drawncars;i++)
        {
            if(gaps[i].isValid())
            {
                if(scoringInfo.getSessionNanos() < visibleEndArray[i] || isEditorMode)
                {
                    if(positions[i].getValue() <= knockout)
                        texture.clear( imgPosNew.getTexture(), offsetX, offsetY+rowHeight*i, false, null );
                    else
                        texture.clear( imgPosNewKnockOut.getTexture(), offsetX, offsetY+rowHeight*i, false, null );
                    
                    if(IsInPit[i].getValue())
                    {
                        texPit = imgPit.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
                        texture.drawImage( texPit, offsetX + width*82/100, offsetY+rowHeight*i, false, null );
                    }
                    else
                        if(IsFinished[i].getValue() || isEditorMode)
                        {
                            texPit = imgFinish.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
                            texture.drawImage( texPit, offsetX + width*82/100, offsetY+rowHeight*i, false, null );
                        }
                }
                else  
                    {
                        if(positions[i].getValue() <= knockout)
                            texture.clear( imgPos.getTexture(), offsetX, offsetY+rowHeight*i, false, null );
                        else
                            texture.clear( imgPosKnockOut.getTexture(), offsetX, offsetY+rowHeight*i, false, null );
                        
                        if(IsInPit[i].getValue() )
                        {
                            texPit = imgPit.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
                            texture.drawImage( texPit, offsetX + width*45/100, offsetY+rowHeight*i, false, null );
                        }
                        else
                            if(IsFinished[i].getValue())
                            {
                                texPit = imgFinish.getImage().getScaledTextureImage( width*17/100, rowHeight, texPit, isEditorMode );
                                texture.drawImage( texPit, offsetX + width*45/100, offsetY+rowHeight*i, false, null );
                            }
                    
                    }
            }
            
        }
        
    }
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        int drawncars = Math.min( gameData.getScoringInfo().getNumVehicles(), numVeh.getValue() );
        
        //one time for leader
        
        if ( needsCompleteRedraw || ( clock.c() && gapFlag2[0]))
        {
            if(gaps[0].isValid())
            {
                //VehicleScoringInfo vsi = vehicleScoringInfos[0];
                
                dsPos[0].draw( offsetX, offsetY, positions[0].getValueAsString(), texture );
                dsName[0].draw( offsetX, offsetY, driverNames[0].getValue(), texture );
                dsTime[0].draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString(gaps[0].getValue() ) , texture);
            }
            else
            {
                dsTime[0].draw( offsetX, offsetY, "" , texture);
            }
            
            gapFlag2[0] = false;
            
            //if(IsInPit[0].getValue())
            //    dsIsInPit[0].draw( offsetX, offsetY, "P", texture );
            //else
                //dsIsInPit[0].draw( offsetX, offsetY, "", texture );
        }
        
        // the other guys
        for(int i=1;i < drawncars;i++)
        { 
            if ( needsCompleteRedraw || ( clock.c() && gapFlag2[i]))
            {
                if(gaps[i].isValid())
                {
                    dsPos[i].draw( offsetX, offsetY, positions[i].getValueAsString(),( positions[i].getValue() <= knockout ) ? fontColor2.getColor() : KnockoutFontColor.getColor(), texture );
                    dsName[i].draw( offsetX, offsetY,driverNames[i].getValue() , texture );  
                    
                    if(gameData.getScoringInfo().getSessionNanos() < visibleEndArray[i])
                    {
                        if(AbsTimes.getValue())
                            dsTime[i].draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString(gaps[i].getValue() ), texture);
                        else
                            dsTime[i].draw( offsetX, offsetY,"+ " + TimingUtil.getTimeAsLaptimeString(Math.abs( gaps[i].getValue() - gaps[0].getValue() )) , texture);
                    }
                    else
                        dsTime[i].draw( offsetX, offsetY,"", texture);
                    
                    /*if(IsInPit[i].getValue())
                        dsIsInPit[i].draw( offsetX, offsetY, "P", texture );
                    else
                        dsIsInPit[i].draw( offsetX, offsetY, "", texture );*/
                }
                
                gapFlag2[i] = false;
                
                
            }
            
        }
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( numVeh, "" );
        writer.writeProperty( visibleTime, "" );
        writer.writeProperty( visibleTimeButton, "" );
        writer.writeProperty( AbsTimes, "" );
        writer.writeProperty( KnockoutFontColor, "" );
        writer.writeProperty( knockoutQual, "" );
        writer.writeProperty( knockoutFP1, "" );
        writer.writeProperty( knockoutFP2, "" );
        writer.writeProperty( knockoutFP3, "" );
        writer.writeProperty( knockoutFP4, "" );
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
        else if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( numVeh ) );
        else if ( loader.loadProperty( visibleTime ) );
        else if ( loader.loadProperty( visibleTimeButton ) );
        else if ( loader.loadProperty( AbsTimes ) );
        else if ( loader.loadProperty( knockoutQual ) );
        else if ( loader.loadProperty( knockoutFP1 ) );
        else if ( loader.loadProperty( knockoutFP2 ) );
        else if ( loader.loadProperty( knockoutFP3 ) );
        else if ( loader.loadProperty( knockoutFP4 ) );
        else if ( loader.loadProperty( KnockoutFontColor ) );
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
        propsCont.addProperty( fontColor2 );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Specific" );
        
        propsCont.addProperty( numVeh );
        propsCont.addProperty( visibleTime );
        propsCont.addProperty( visibleTimeButton );
        propsCont.addProperty( AbsTimes );
        propsCont.addGroup( "Knockout Infos" );
        propsCont.addProperty( knockoutQual );
        propsCont.addProperty( knockoutFP1 );
        propsCont.addProperty( knockoutFP2 );
        propsCont.addProperty( knockoutFP3 );
        propsCont.addProperty( knockoutFP4 );
        propsCont.addProperty( KnockoutFontColor );
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
    
    public QualifTimingTowerWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 22.5f, 32.5f );
        
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
        getFontColorProperty().setColor( PrunnWidgetSetf1_2011.FONT_COLOR1_NAME );
    }
}
