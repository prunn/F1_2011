package com.prunn.rfdynhud.widgets.prunn.f1_2011.cartelemetry;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ProfileInfo.MeasurementUnits;
import net.ctdp.rfdynhud.gamedata.TelemetryData;
import net.ctdp.rfdynhud.gamedata.Wheel;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.FontProperty;
import net.ctdp.rfdynhud.properties.ImageProperty;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.FontUtils;
import net.ctdp.rfdynhud.util.NumberUtil;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */
public class CarTelemWidget extends Widget
{
    private DrawnString dsTempFL = null;
    private DrawnString dsTempFR = null;
    private DrawnString dsTempRL = null;
    private DrawnString dsTempRR = null;
    private DrawnString dsWearFL = null;
    private DrawnString dsWearFR = null;
    private DrawnString dsWearRL = null;
    private DrawnString dsWearRR = null;
    private DrawnString dsPressFL = null;
    private DrawnString dsPressFR = null;
    private DrawnString dsPressRL = null;
    private DrawnString dsPressRR = null;
    private DrawnString dskPaFL = null;
    private DrawnString dskPaFR = null;
    private DrawnString dskPaRL = null;
    private DrawnString dskPaRR = null;
    private DrawnString dsEngineWear = null;
    private DrawnString dsOilTemp = null;
    private DrawnString dsWaterTemp = null;
       
    private final FloatValue TempFL = new FloatValue( -1f, 0.1f );
    private final FloatValue TempFR = new FloatValue( -1f, 0.1f );
    private final FloatValue TempRL = new FloatValue( -1f, 0.1f );
    private final FloatValue TempRR = new FloatValue( -1f, 0.1f ); 
    private final FloatValue WearFL = new FloatValue( -1f, 0.01f );
    private final FloatValue WearFR = new FloatValue( -1f, 0.01f );
    private final FloatValue WearRL = new FloatValue( -1f, 0.01f );
    private final FloatValue WearRR = new FloatValue( -1f, 0.01f );
    private final FloatValue PressFL = new FloatValue( -1f, 0.01f );
    private final FloatValue PressFR = new FloatValue( -1f, 0.01f );
    private final FloatValue PressRL = new FloatValue( -1f, 0.01f );
    private final FloatValue PressRR = new FloatValue( -1f, 0.01f );
    private final FloatValue engine = new FloatValue( -1f, 0.01f );
    private final FloatValue OilTemp = new FloatValue( -1f, 0.01f );
    private final FloatValue WaterTemp = new FloatValue( -1f, 0.01f );
    private final FloatValue BrakeTFL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTFR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTRL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTRR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWFL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWFR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWRL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWRR = new FloatValue( -1f, 0.1f );
    private final BoolValue WheelDetachedFL = new BoolValue();
    private final BoolValue WheelDetachedFR = new BoolValue();
    private final BoolValue WheelDetachedRL = new BoolValue();
    private final BoolValue WheelDetachedRR = new BoolValue();
    
    private final ImagePropertyWithTexture carImage = new ImagePropertyWithTexture( "image", null, "prunn/f1_2011/telem/car_icon.png", false, true ); 
    private final ImageProperty imgTire = new ImageProperty( "imgTire", "prunn/f1_2011/telem/tiregreen.png" );
    private final ImageProperty imgOil = new ImageProperty( "imgOil", "prunn/F1_2011/telem/oil.png" );
    private final ImageProperty imgWater = new ImageProperty( "imgWater", "prunn/F1_2011/telem/water.png" );
    private TextureImage2D texImgTire = null;
    private TextureImage2D texImgOil = null;
    private TextureImage2D texImgWater = null;
    private BooleanProperty ForcePSI = new BooleanProperty("ForcePSI", false) ;
    private BooleanProperty ShowPressures = new BooleanProperty("Show Pressures", true) ;
    private BooleanProperty ShowDamage = new BooleanProperty("Show Damage", true) ;
    
    protected final ColorProperty DamageFontColor1 = new ColorProperty("Damage Dent Color 1", "DamageFontColor1");
    protected final ColorProperty DamageFontColor2 = new ColorProperty("Damage Dent Color 2", "DamageFontColor2");
    protected final ColorProperty TWearFontColor = new ColorProperty("Tire Wear Color", "TWearFontColor");
    protected final ColorProperty TTempFontColor = new ColorProperty("Temperatures Color", "TTempFontColor");
    protected final ColorProperty TPressFontColor = new ColorProperty("Tire Press Color", "TPressFontColor");
    protected final ColorProperty EngineFontColor = new ColorProperty("Engine Wear Color", "EngineFontColor");
    protected final FontProperty TWearFont = new FontProperty("Tire Wear Font", "TWearFont");
    protected final FontProperty TTempFont = new FontProperty("Temperatures Font", "TTempFont");
    protected final FontProperty TPressFont = new FontProperty("Tire Press Font", "TPressFont");
    protected final FontProperty EngineFont = new FontProperty("Engine Wear Font", "EngineFont");
 
    
    @Override
    public String getDefaultNamedColorValue(String name)
    {
    	if(name.equals("StandardBackground"))
            return "#0000005D";
    	if(name.equals("StandardFontColor"))
            return "#FFFFFF";
        if(name.equals("TWearFontColor"))
            return "#FFFFFF";
        if(name.equals("TTempFontColor"))
            return "#FAD506";
        if(name.equals("TPressFontColor"))
            return "#BBBBBB";
        if(name.equals("EngineFontColor"))
            return "#000000";
        if(name.equals("DamageFontColor1"))
            return "#F4B930";
        if(name.equals("DamageFontColor2"))
            return "#C60D17";
        
        return null;
    }
    
    @Override
    public String getDefaultNamedFontValue(String name)
    {
    	if(name.equals("StandardFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        if(name.equals("TWearFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        if(name.equals("TTempFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        if(name.equals("TPressFont"))
            return FontUtils.getFontString("Dialog", 1, 16, true, true);
        
        return null;
    }
    
    @Override
    public void onCockpitEntered( LiveGameData gameData, boolean isEditorMode )
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
        //int fh = TextureImage2D.getStringHeight( "0%C", getFontProperty() );
        int fhTemp = TextureImage2D.getStringHeight( "0%C", TTempFont );
        int fhWear = TextureImage2D.getStringHeight( "0%C", TWearFont );
        int fhPress = TextureImage2D.getStringHeight( "0%C", TPressFont );
        MeasurementUnits devilsunits = gameData.getProfileInfo().getMeasurementUnits();
        
        dsTempFL = drawnStringFactory.newDrawnString( "dsTempFL", width*10/100, height*13/100 - fhTemp/2, Alignment.LEFT, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits));
        dsTempFR = drawnStringFactory.newDrawnString( "dsTempFR", width*91/100, height*13/100 - fhTemp/2, Alignment.RIGHT, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits) );
        dsTempRL = drawnStringFactory.newDrawnString( "dsTempRL", width*19/100, height*95/100 - fhTemp/2, Alignment.LEFT, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits) );
        dsTempRR = drawnStringFactory.newDrawnString( "dsTempRR", width*85/100, height*95/100 - fhTemp/2, Alignment.RIGHT, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits) );
        
        dsWearFL = drawnStringFactory.newDrawnString( "dsWearFL", width*20/100, height*36/100 - fhWear/2, Alignment.LEFT, false, TWearFont.getFont(), isFontAntiAliased(), TWearFontColor.getColor(), null, "%" );
        dsWearFR = drawnStringFactory.newDrawnString( "dsWearFR", width*86/100, height*36/100 - fhWear/2, Alignment.RIGHT, false, TWearFont.getFont(), isFontAntiAliased(), TWearFontColor.getColor(), null, "%" );
        dsWearRL = drawnStringFactory.newDrawnString( "dsWearRL", width*20/100, height*72/100 - fhWear/2, Alignment.LEFT, false, TWearFont.getFont(), isFontAntiAliased(), TWearFontColor.getColor(), null, "%" );
        dsWearRR = drawnStringFactory.newDrawnString( "dsWearRR", width*86/100, height*72/100 - fhWear/2, Alignment.RIGHT, false, TWearFont.getFont(), isFontAntiAliased(), TWearFontColor.getColor(), null, "%" );
        
        dsPressFL = drawnStringFactory.newDrawnString( "dsPressFL", width*5/100, height*23/100 - fhPress/2, Alignment.LEFT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dsPressFR = drawnStringFactory.newDrawnString( "dsPressFR", width*97/100, height*23/100 - fhPress/2, Alignment.RIGHT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dsPressRL = drawnStringFactory.newDrawnString( "dsPressRL", width*6/100, height*80/100 - fhPress/2, Alignment.LEFT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dsPressRR = drawnStringFactory.newDrawnString( "dsPressRR", width*96/100, height*80/100 - fhPress/2, Alignment.RIGHT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dskPaFL = drawnStringFactory.newDrawnString( "dskPaFL", width*4/100, height*30/100 - fhPress/2, Alignment.LEFT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dskPaFR = drawnStringFactory.newDrawnString( "dskPaFR", width*98/100, height*30/100 - fhPress/2, Alignment.RIGHT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dskPaRL = drawnStringFactory.newDrawnString( "dskPaRL", width*5/100, height*87/100 - fhPress/2, Alignment.LEFT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        dskPaRR = drawnStringFactory.newDrawnString( "dskPaRR", width*97/100, height*87/100 - fhPress/2, Alignment.RIGHT, false, TPressFont.getFont(), isFontAntiAliased(), TPressFontColor.getColor(), null, "" );
        
        dsEngineWear = drawnStringFactory.newDrawnString( "dsEngineWear", width / 2, height / 2 + 35, Alignment.CENTER, false, TWearFont.getFont(), isFontAntiAliased(), EngineFontColor.getColor(), null, "%" );
        dsOilTemp = drawnStringFactory.newDrawnString( "dsOilTemp", 10, height / 2 + 10, Alignment.LEFT, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits) );
        dsWaterTemp = drawnStringFactory.newDrawnString( "dsWaterTemp", width - 10, height / 2 + 10, Alignment.RIGHT, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits) );
        
        texImgTire = imgTire.getImage().getScaledTextureImage( 26, 30, texImgTire, isEditorMode );
        texImgOil = imgOil.getImage().getScaledTextureImage( 20, 20, texImgOil, isEditorMode );
        texImgWater = imgWater.getImage().getScaledTextureImage( 20, 20, texImgWater, isEditorMode );
        
        if ( !carImage.isNoImage() )
        {
            carImage.updateSize( width, height, isEditorMode );
        }
        
    }

    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        super.updateVisibility(gameData, isEditorMode);
        
        if(gameData.getScoringInfo().getViewedVehicleScoringInfo().isPlayer())
            return true;
        
        return false;
    }
    
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        texture.drawImage( carImage.getTexture(), offsetX, offsetY, true, null );
        
    }
    
    private String gettempunits(MeasurementUnits units)
    {
        if(units == MeasurementUnits.METRIC)
            return "°C";
        else
            return "°F";
    }
    private void drawBrake(float temp, float wear, net.ctdp.rfdynhud.gamedata.VehiclePhysics.Brakes.WheelBrake brake, TextureImage2D texture, int x, int y)
    {
        
        texture.clear(Color.RED, x, y, 5, 20 , true, null);
        
    	if(temp < brake.getOptimumTemperaturesLowerBound() * 0.8 )
            texture.clear(Color.BLUE, x, y + Math.round( (1-wear) * 20 ), 5, Math.round( wear * 20 ), false, null);
        else
        	if(temp < brake.getOverheatingTemperature())
        		texture.clear(Color.GREEN, x, y + Math.round( (1-wear) * 20 ), 5, Math.round( wear * 20 ), false, null);
        	else
        		texture.clear(Color.YELLOW, x, y + Math.round( (1-wear) * 20 ), 5, Math.round( wear * 20 ), false, null);
    }
    private void drawDamage(LiveGameData gameData, TextureImage2D texture, int width, int height, int offsetX, int offsetY, boolean isEditorMode)
    {
        short[] dent = new short[8];
        dent = gameData.getTelemetryData().getDentSevirity();
        boolean detached = gameData.getTelemetryData().isAnythingDetached();
        int dwidth = width*4/100;
        int dheight = height*3/100;
        // detached = true + dent[0]=1-2 Front wing off
        if((dent[0] > 0 && detached) || isEditorMode)
        {
            texture.clear(DamageFontColor2.getColor(), offsetX + width*31/100, offsetY + height*8/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*36/100, offsetY + height*8/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*41/100, offsetY + height*8/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*56/100, offsetY + height*8/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*61/100, offsetY + height*8/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*66/100, offsetY + height*8/100, dwidth, dheight, true, null);
        }
        if(dent[0] == 1 || isEditorMode)
        {
            texture.clear(DamageFontColor1.getColor(), offsetX + width*48/100, offsetY + height*10/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor1.getColor(), offsetX + width*48/100, offsetY + height*14/100, dwidth, dheight, true, null);
        }
        else
            if(dent[0] == 2)
            {
                texture.clear(DamageFontColor2.getColor(), offsetX + width*48/100, offsetY + height*10/100, dwidth, dheight, true, null);
                texture.clear(DamageFontColor2.getColor(), offsetX + width*48/100, offsetY + height*14/100, dwidth, dheight, true, null);
            }
        // dent[1] = front left suspension
        if(dent[1] == 1 || isEditorMode)
            texture.clear(DamageFontColor1.getColor(), offsetX + width*41/100, offsetY + height*24/100, dwidth, dheight, true, null);
        else
            if(dent[1] == 2)
                texture.clear(DamageFontColor2.getColor(), offsetX + width*41/100, offsetY + height*24/100, dwidth, dheight, true, null);
            
        // dent[2] = left side damage
        if(dent[2] == 1)
        {
            texture.clear(DamageFontColor1.getColor(), offsetX + width*33/100, offsetY + height*49/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor1.getColor(), offsetX + width*33/100, offsetY + height*53/100, dwidth, dheight, true, null);
        }
        else
            if(dent[2] == 2 || isEditorMode)
            {
                texture.clear(DamageFontColor2.getColor(), offsetX + width*33/100, offsetY + height*49/100, dwidth, dheight, true, null);
                texture.clear(DamageFontColor2.getColor(), offsetX + width*33/100, offsetY + height*53/100, dwidth, dheight, true, null);
            }
            
        // dent[3] = rear left suspension damaged
        if(dent[3] == 1 || isEditorMode)
        {
            texture.clear(DamageFontColor1.getColor(), offsetX + width*38/100, offsetY + height*80/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor1.getColor(), offsetX + width*38/100, offsetY + height*84/100, dwidth, dheight, true, null);
        }
        else
            if(dent[3] == 2)
            {
                texture.clear(DamageFontColor2.getColor(), offsetX + width*38/100, offsetY + height*80/100, dwidth, dheight, true, null);
                texture.clear(DamageFontColor2.getColor(), offsetX + width*38/100, offsetY + height*84/100, dwidth, dheight, true, null);
            }
        // detached = true + dent[4]=1-2 Rear wing off
        if((dent[4] > 0 && detached) || isEditorMode)
        {
            texture.clear(DamageFontColor2.getColor(), offsetX + width*41/100, offsetY + height*87/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*46/100, offsetY + height*87/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*51/100, offsetY + height*87/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor2.getColor(), offsetX + width*56/100, offsetY + height*87/100, dwidth, dheight, true, null);
        }
        
        if(dent[4] == 1 || isEditorMode)
        {
            texture.clear(DamageFontColor1.getColor(), offsetX + width*46/100, offsetY + height*81/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor1.getColor(), offsetX + width*51/100, offsetY + height*81/100, dwidth, dheight, true, null);
        }
        else
            if(dent[4] == 2)
            {
                texture.clear(DamageFontColor2.getColor(), offsetX + width*46/100, offsetY + height*81/100, dwidth, dheight, true, null);
                texture.clear(DamageFontColor2.getColor(), offsetX + width*51/100, offsetY + height*81/100, dwidth, dheight, true, null);
            }
        // dent[5] = rear right suspension damaged
        if(dent[5] == 1)
        {
            texture.clear(DamageFontColor1.getColor(), offsetX + width*59/100, offsetY + height*80/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor1.getColor(), offsetX + width*59/100, offsetY + height*84/100, dwidth, dheight, true, null);
        }
        else
            if(dent[5] == 2 || isEditorMode)
            {
                texture.clear(DamageFontColor2.getColor(), offsetX + width*59/100, offsetY + height*80/100, dwidth, dheight, true, null);
                texture.clear(DamageFontColor2.getColor(), offsetX + width*59/100, offsetY + height*84/100, dwidth, dheight, true, null);
            }
        // dent[6] = right side damage
        if(dent[6] == 1 || isEditorMode)
        {
            texture.clear(DamageFontColor1.getColor(), offsetX + width*63/100, offsetY + height*49/100, dwidth, dheight, true, null);
            texture.clear(DamageFontColor1.getColor(), offsetX + width*63/100, offsetY + height*53/100, dwidth, dheight, true, null);
        }
        else
            if(dent[6] == 2)
            {
                texture.clear(DamageFontColor2.getColor(), offsetX + width*63/100, offsetY + height*49/100, dwidth, dheight, true, null);
                texture.clear(DamageFontColor2.getColor(), offsetX + width*63/100, offsetY + height*53/100, dwidth, dheight, true, null);
            }
            
        // dent[7] = front right suspension
        if(dent[7] == 1)
            texture.clear(DamageFontColor1.getColor(), offsetX + width*56/100, offsetY + height*24/100, dwidth, dheight, true, null);
        else
            if(dent[7] == 2 || isEditorMode)
                texture.clear(DamageFontColor2.getColor(), offsetX + width*56/100, offsetY + height*24/100, dwidth, dheight, true, null);
        
        
    }
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        final TelemetryData telemData = gameData.getTelemetryData();
        
        TempFL.update( telemData.getTireTemperature( Wheel.FRONT_LEFT ) );
        TempFR.update( telemData.getTireTemperature( Wheel.FRONT_RIGHT ) );
        TempRL.update( telemData.getTireTemperature( Wheel.REAR_LEFT ) );
        TempRR.update( telemData.getTireTemperature( Wheel.REAR_RIGHT ) );
        
        // Since each single value may change independently, we need each one in its own block with its own condition.
        
        if ( needsCompleteRedraw || ( clock.c() && TempFL.hasChanged() ) )
        {
        	if(TempFL.getValue() >= 0)
        	{            
	        	String tireTempFL = NumberUtil.formatFloat( TempFL.getValue(), 0, true );
	            dsTempFL.draw( offsetX, offsetY, tireTempFL, texture );
            }
        	else
        		dsTempFL.draw( offsetX, offsetY, "0", texture );
        }
        
        if ( needsCompleteRedraw || ( clock.c() && TempFR.hasChanged() ) )
        {
        	if(TempFR.getValue() >= 0)
        	{
        		String tireTempFR = NumberUtil.formatFloat( TempFR.getValue(), 0, true );
        		dsTempFR.draw( offsetX, offsetY, tireTempFR, texture );
        	}
        	else
        		dsTempFR.draw( offsetX, offsetY, "0", texture );
        }
        
        if ( needsCompleteRedraw || ( clock.c() && TempRL.hasChanged() ) )
        {
        	if(TempRL.getValue() >= 0)
        	{
	            String tireTempRL = NumberUtil.formatFloat( TempRL.getValue(), 0, true );
	            dsTempRL.draw( offsetX, offsetY, tireTempRL, texture );
        	}
        	else
        		dsTempRL.draw( offsetX, offsetY, "0", texture );
        }
        
        if ( needsCompleteRedraw || ( clock.c() && TempRR.hasChanged() ) )
        {
        	if(TempRR.getValue() >= 0)
        	{  
        		String tireTempRR = NumberUtil.formatFloat( TempRR.getValue(), 0, true );
        		dsTempRR.draw( offsetX, offsetY, tireTempRR, texture );
        	}
        	else
        		dsTempRR.draw( offsetX, offsetY, "0", texture );
        }
        
        WearFL.update( telemData.getTireWear( Wheel.FRONT_LEFT ) );
        WearFR.update( telemData.getTireWear( Wheel.FRONT_RIGHT ) );
        WearRL.update( telemData.getTireWear( Wheel.REAR_LEFT ) );
        WearRR.update( telemData.getTireWear( Wheel.REAR_RIGHT ) );
        WheelDetachedFL.update( telemData.isWheelDetached( Wheel.FRONT_LEFT ));
        WheelDetachedFR.update( telemData.isWheelDetached( Wheel.FRONT_RIGHT ));
        WheelDetachedRL.update( telemData.isWheelDetached( Wheel.REAR_LEFT ));
        WheelDetachedRR.update( telemData.isWheelDetached( Wheel.REAR_RIGHT ));
        
        if ( needsCompleteRedraw || ( clock.c() && (WearFL.hasChanged() || WheelDetachedFL.hasChanged() ) ) )
        {
            String tireWearFL = NumberUtil.formatFloat( WearFL.getValue() * 100, 0, true );
            dsWearFL.draw( offsetX, offsetY, tireWearFL, texture );
            
            if(WearFL.getValue() > 0.7 )
            	imgTire.setValue("prunn/f1_2011/telem/tiregreen.png") ;
            else
            	if(WearFL.getValue() > 0.4 )
                	imgTire.setValue("prunn/f1_2011/telem/tireyellow.png") ;
            	else
            		imgTire.setValue("prunn/f1_2011/telem/tirered.png") ;
            texImgTire = imgTire.getImage().getScaledTextureImage( 22, 30, texImgTire, isEditorMode );
            clearBackgroundRegion( texture, offsetX + width*19/100, offsetY + height*18/100, 0, 0, texImgTire.getWidth(), texImgTire.getHeight(), true, null );
            if(!WheelDetachedFL.getValue())
                texture.drawImage( texImgTire, offsetX + width*19/100, offsetY + height*18/100, false, null );
            
        }
        
        if ( needsCompleteRedraw || ( clock.c() && (WearFR.hasChanged() || WheelDetachedFR.hasChanged()) ) )
        {
            String tireWearFR = NumberUtil.formatFloat( WearFR.getValue() * 100, 0, true );
            dsWearFR.draw( offsetX, offsetY, tireWearFR, texture );
            
            if(WearFR.getValue() > 0.7 )
            	imgTire.setValue("prunn/f1_2011/telem/tiregreen.png") ;
            else
            	if(WearFR.getValue() > 0.4 )
                	imgTire.setValue("prunn/f1_2011/telem/tireyellow.png") ;
            	else
            		imgTire.setValue("prunn/f1_2011/telem/tirered.png") ;
            texImgTire = imgTire.getImage().getScaledTextureImage( 22, 30, texImgTire, isEditorMode );
            clearBackgroundRegion( texture, offsetX + width*84/100 - texImgTire.getWidth(), offsetY + height*18/100, 0, 0, texImgTire.getWidth(), texImgTire.getHeight(), true, null );
            if(!WheelDetachedFR.getValue())
                texture.drawImage( texImgTire, offsetX + width*84/100 - texImgTire.getWidth(), offsetY + height*18/100, false, null );
            
        }
        
        if ( needsCompleteRedraw || ( clock.c() && (WearRL.hasChanged() || WheelDetachedRL.hasChanged() ) ) )
        {
            String tireWearRL = NumberUtil.formatFloat( WearRL.getValue() * 100, 0, true );
            dsWearRL.draw( offsetX, offsetY, tireWearRL, texture );
            
            if(WearRL.getValue() > 0.7 )
            	imgTire.setValue("prunn/f1_2011/telem/tiregreen.png") ;
            else
            	if(WearRL.getValue() > 0.4 )
                	imgTire.setValue("prunn/f1_2011/telem/tireyellow.png") ;
            	else
            		imgTire.setValue("prunn/f1_2011/telem/tirered.png") ;
            
            texImgTire = imgTire.getImage().getScaledTextureImage( 22, 30, texImgTire, isEditorMode );
            clearBackgroundRegion( texture, offsetX + width*20/100, offsetY + height*179/200 - texImgTire.getHeight(), 0, 0, texImgTire.getWidth(), texImgTire.getHeight(), true, null );
            if(!WheelDetachedRL.getValue())
                texture.drawImage( texImgTire, offsetX + width*20/100, offsetY + height*179/200 - texImgTire.getHeight(), false, null );
            
        }
        
        if ( needsCompleteRedraw || ( clock.c() && (WearRR.hasChanged() || WheelDetachedRR.hasChanged() ) ) )
        {
            String tireWearRR = NumberUtil.formatFloat( WearRR.getValue() * 100, 0, true );
            dsWearRR.draw( offsetX, offsetY, tireWearRR, texture );
            
            if(WearRR.getValue() > 0.7 )
            	imgTire.setValue("prunn/f1_2011/telem/tiregreen.png") ;
            else
            	if(WearRR.getValue() > 0.4 )
                	imgTire.setValue("prunn/f1_2011/telem/tireyellow.png") ;
            	else
            		imgTire.setValue("prunn/f1_2011/telem/tirered.png") ;
            
            texImgTire = imgTire.getImage().getScaledTextureImage( 22, 30, texImgTire, isEditorMode );
            clearBackgroundRegion( texture, offsetX + width*83/100 - texImgTire.getWidth(), offsetY + height*179/200 - texImgTire.getHeight(), 0, 0, texImgTire.getWidth(), texImgTire.getHeight(), true, null );
            if(!WheelDetachedRR.getValue())
                texture.drawImage( texImgTire, offsetX + width*83/100 - texImgTire.getWidth(), offsetY + height*179/200 - texImgTire.getHeight(), false, null );

        }
        
        PressFL.update( telemData.getTirePressure( Wheel.FRONT_LEFT ) );
        PressFR.update( telemData.getTirePressure( Wheel.FRONT_RIGHT ) );
        PressRL.update( telemData.getTirePressure( Wheel.REAR_LEFT ) );
        PressRR.update( telemData.getTirePressure( Wheel.REAR_RIGHT ) );
        
        if ( (needsCompleteRedraw || ( clock.c() && PressFL.hasChanged() )) && ShowPressures.getValue() )
        {
            String tirePressFL;
            if(ForcePSI.getValue())
            {
                tirePressFL = NumberUtil.formatFloat( (float)(PressFL.getValue() * 0.14503773773022) , 1, true );
                dsPressFL.draw( offsetX, offsetY, tirePressFL, texture );
                dskPaFL.draw( offsetX, offsetY, " psi", texture );
            }    
            else
            {
                tirePressFL = NumberUtil.formatFloat( PressFL.getValue(), 0, true );
                dsPressFL.draw( offsetX, offsetY, tirePressFL, texture );
                dskPaFL.draw( offsetX, offsetY, "kPa", texture );
            }
        }
        
        if ( (needsCompleteRedraw || ( clock.c() && PressFR.hasChanged() )) && ShowPressures.getValue() )
        {
            String tirePressFR;
            if(ForcePSI.getValue())
            {
                tirePressFR = NumberUtil.formatFloat( (float)(PressFR.getValue() * 0.14503773773022) , 1, true );
                dsPressFR.draw( offsetX, offsetY, tirePressFR, texture );
                dskPaFR.draw( offsetX, offsetY, "psi ", texture );
            }    
            else
            {
                tirePressFR = NumberUtil.formatFloat( PressFR.getValue(), 0, true );
                dsPressFR.draw( offsetX, offsetY, tirePressFR, texture );
                dskPaFR.draw( offsetX, offsetY, "kPa", texture );
            }
        }
        
        if ( (needsCompleteRedraw || ( clock.c() && PressRL.hasChanged() ) ) && ShowPressures.getValue())
        {
            String tirePressRL;
            if(ForcePSI.getValue())
            {
                tirePressRL = NumberUtil.formatFloat( (float)(PressRL.getValue() * 0.14503773773022) , 1, true );
                dsPressRL.draw( offsetX, offsetY, tirePressRL, texture );
                dskPaRL.draw( offsetX, offsetY, " psi", texture );
            }    
            else
            {
                tirePressRL = NumberUtil.formatFloat( PressRL.getValue(), 0, true );
                dsPressRL.draw( offsetX, offsetY, tirePressRL, texture );
                dskPaRL.draw( offsetX, offsetY, "kPa", texture );
            }
        }
        
        if ( (needsCompleteRedraw || ( clock.c() && PressRR.hasChanged() )) && ShowPressures.getValue() )
        {
            String tirePressRR;
            if(ForcePSI.getValue())
            {
                tirePressRR = NumberUtil.formatFloat( (float)(PressRR.getValue() * 0.14503773773022) , 1, true );
                dsPressRR.draw( offsetX, offsetY, tirePressRR, texture );
                dskPaRR.draw( offsetX, offsetY, "psi ", texture );
            }    
            else
            {
                tirePressRR = NumberUtil.formatFloat( PressRR.getValue(), 0, true );
                dsPressRR.draw( offsetX, offsetY, tirePressRR, texture );
                dskPaRR.draw( offsetX, offsetY, "kPa", texture );
            }
        }
        
        /* 
         * getLifetimeAverage(double raceLengthMultiplier) 
         * getLifetimeVariance(double raceLengthMultiplier) 
         * 
         *  safe: getEngineLifetime() / getSafeLifetimeTotal
         *  good: (getGoodLifetimeTotal - getSafeLifetimeTotal +  getEngineLifetime()) / getGoodLifetimeTotal
         *  bad:  (getBadLifetimeTotal - getSafeLifetimeTotal +  getEngineLifetime()) / getBadLifetimeTotal
         *  max:  (getMaxLifetimeTotal - getSafeLifetimeTotal +  getEngineLifetime()) / getMaxLifetimeTotal
         *  
         * */
        double rlp = gameData.getScoringInfo().getRaceLengthPercentage();
        float engineLifetime100Percent = gameData.getPhysics().getEngine().getMaxLifetimeTotal( rlp );
        //float engineLifetime100Percent = gameData.getPhysics().getEngine().getGoodLifetimeTotal( rlp );
        //float engineLifetime100Percent = gameData.getPhysics().getEngine().getBadLifetimeTotal( rlp );
        //float engineLifetime100Percent = gameData.getPhysics().getEngine().getSafeLifetimeTotal( rlp );
        
        float currentEngineLifetime = engineLifetime100Percent - gameData.getPhysics().getEngine().getSafeLifetimeTotal( rlp ) + telemData.getEngineLifetime();
        //float currentEngineLifetime = telemData.getEngineLifetime();
        
		engine.update( currentEngineLifetime  );
		OilTemp.update( gameData.getTelemetryData().getEngineOilTemperature() );
        WaterTemp.update( gameData.getTelemetryData().getEngineWaterTemperature() );
        
        if ( needsCompleteRedraw || ( clock.c() && engine.hasChanged() ) )
        {
            String EngineWear;
            
            if(engine.getValue()>=0)
                EngineWear = NumberUtil.formatFloat( engine.getValue() * 100f / engineLifetime100Percent, 0, true );
             else
                EngineWear ="0";
            
            dsEngineWear.draw( offsetX, offsetY, EngineWear, texture );
        }
       
        if ( needsCompleteRedraw || ( clock.c() && OilTemp.hasChanged() ) )
        {
           String oil = NumberUtil.formatFloat( OilTemp.getValue(), 0, true );
           dsOilTemp.draw( offsetX, offsetY, oil, texture );
           
           if(OilTemp.getValue() <= gameData.getPhysics().getEngine().getOverheatingOilTemperature() )
           	    imgOil.setValue("prunn/F1_2011/telem/oil.png") ;
           else
           		imgOil.setValue("prunn/F1_2011/telem/oilhot.png") ;
           
           texImgOil = imgOil.getImage().getScaledTextureImage( 26, 26, texImgOil, isEditorMode );
           clearBackgroundRegion( texture, offsetX + 12, offsetY + height / 2 - texImgOil.getWidth() + 6, 0, 0, texImgOil.getWidth(), texImgOil.getHeight(), true, null );
           texture.drawImage( texImgOil, offsetX + 12, offsetY + height / 2 - texImgOil.getWidth() + 6, true, null );
           
        }
       
        if ( needsCompleteRedraw || ( clock.c() && WaterTemp.hasChanged() ) )
        {
           String water = NumberUtil.formatFloat( WaterTemp.getValue(), 0, true );
           dsWaterTemp.draw( offsetX, offsetY, water, texture );
           
           if((WaterTemp.getValue() <= 120 && gameData.getProfileInfo().getMeasurementUnits() == MeasurementUnits.METRIC) || (WaterTemp.getValue() <= 248 && gameData.getProfileInfo().getMeasurementUnits() == MeasurementUnits.IMPERIAL))
              	imgWater.setValue("prunn/F1_2011/telem/water.png") ;
           else
              	imgWater.setValue("prunn/F1_2011/telem/waterhot.png") ;
              
          texImgWater = imgWater.getImage().getScaledTextureImage( 26, 26, texImgWater, isEditorMode );
          clearBackgroundRegion( texture, offsetX + width - texImgWater.getWidth() - 12, offsetY + height / 2 - texImgWater.getHeight() + 6, 0, 0, texImgWater.getWidth(), texImgWater.getHeight(), true, null );
          texture.drawImage( texImgWater, offsetX + width - texImgWater.getWidth() - 12, offsetY + height / 2 - texImgWater.getHeight() + 6, true, null );
       
        }
       
        BrakeTFL.update( telemData.getBrakeTemperature( Wheel.FRONT_LEFT ) );
        BrakeTFR.update( telemData.getBrakeTemperature( Wheel.FRONT_RIGHT ) );
        BrakeTRL.update( telemData.getBrakeTemperature( Wheel.REAR_LEFT ) );
        BrakeTRR.update( telemData.getBrakeTemperature( Wheel.REAR_RIGHT ) );
        BrakeWFL.update( telemData.getBrakeDiscThickness( Wheel.FRONT_LEFT ) );
        BrakeWFR.update( telemData.getBrakeDiscThickness( Wheel.FRONT_RIGHT ) );
        BrakeWRL.update( telemData.getBrakeDiscThickness( Wheel.REAR_LEFT ) );
        BrakeWRR.update( telemData.getBrakeDiscThickness( Wheel.REAR_RIGHT ) );
       
       
        if ( needsCompleteRedraw || ( clock.c() && BrakeTFL.hasChanged() ) || ( clock.c() && BrakeWFL.hasChanged() ) )
        {
            float percentWear = (BrakeWFL.getValue() - gameData.getPhysics().getBrakes().getBrake( Wheel.FRONT_LEFT ).getMaxDiscFailure() ) / (gameData.getSetup().getWheelAndTire(Wheel.FRONT_LEFT).getBrakeDiscThickness()- gameData.getPhysics().getBrakes().getBrake( Wheel.FRONT_LEFT ).getMaxDiscFailure());
            drawBrake(BrakeTFL.getValue(), percentWear, gameData.getPhysics().getBrakes().getBrake(Wheel.FRONT_LEFT), texture, offsetX + width*33/100, offsetY + height*20/100);
        }
       
        if ( needsCompleteRedraw || ( clock.c() && BrakeTFR.hasChanged() ) || ( clock.c() && BrakeWFR.hasChanged() ) )
        {
            float percentWear = (BrakeWFR.getValue() - gameData.getPhysics().getBrakes().getBrake( Wheel.FRONT_RIGHT ).getMaxDiscFailure() ) / (gameData.getSetup().getWheelAndTire(Wheel.FRONT_RIGHT).getBrakeDiscThickness()- gameData.getPhysics().getBrakes().getBrake( Wheel.FRONT_RIGHT ).getMaxDiscFailure());
            drawBrake(BrakeTFR.getValue(), percentWear, gameData.getPhysics().getBrakes().getBrake(Wheel.FRONT_RIGHT), texture, offsetX + width*65/100, offsetY + height*20/100);
        }
       
        if ( needsCompleteRedraw || ( clock.c() && BrakeTRL.hasChanged() ) || ( clock.c() && BrakeWRL.hasChanged() ) )
        {
            float percentWear = (BrakeWRL.getValue() - gameData.getPhysics().getBrakes().getBrake( Wheel.REAR_LEFT ).getMaxDiscFailure() ) / (gameData.getSetup().getWheelAndTire(Wheel.REAR_LEFT).getBrakeDiscThickness()- gameData.getPhysics().getBrakes().getBrake( Wheel.REAR_LEFT ).getMaxDiscFailure());
            drawBrake(BrakeTRL.getValue(), percentWear, gameData.getPhysics().getBrakes().getBrake(Wheel.REAR_LEFT), texture, offsetX + width*34/100, offsetY + height*78/100);
        }
       
        if ( needsCompleteRedraw || ( clock.c() && BrakeTRR.hasChanged() ) || ( clock.c() && BrakeWRR.hasChanged() ) )
        {
            float percentWear = (BrakeWRR.getValue() - gameData.getPhysics().getBrakes().getBrake( Wheel.REAR_RIGHT ).getMaxDiscFailure() ) / (gameData.getSetup().getWheelAndTire(Wheel.REAR_RIGHT).getBrakeDiscThickness()- gameData.getPhysics().getBrakes().getBrake( Wheel.REAR_RIGHT ).getMaxDiscFailure());
            drawBrake(BrakeTRR.getValue(), percentWear, gameData.getPhysics().getBrakes().getBrake(Wheel.REAR_RIGHT), texture, offsetX + width*64/100, offsetY + height*78/100);
        }
       
        //Damage
        if(ShowDamage.getValue())
            drawDamage(gameData, texture, width, height, offsetX, offsetY, isEditorMode);
        
    }
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        
        writer.writeProperty( carImage, "An image for the background." );
        
        writer.writeProperty( TWearFontColor, "Tire Wear Font Color" );
        writer.writeProperty( TTempFontColor, "Tire Temperature Font Color" );
        writer.writeProperty( TPressFontColor, "Tire Pressure Font" );
        writer.writeProperty( TWearFont, "Tire Wear Font" );
        writer.writeProperty( TTempFont, "Tire Temperature Font" );
        writer.writeProperty( TPressFont, "Tire Pressure Font" );
        writer.writeProperty( EngineFontColor, "Engine Wear Color" );
        writer.writeProperty( EngineFont, "Engine Font" );
        writer.writeProperty( ForcePSI, "Tire Pressure in PSI" );
        writer.writeProperty( ShowDamage, "Show damage" );
        writer.writeProperty( ShowPressures, "Show Tire Pressures" );
        writer.writeProperty( DamageFontColor1, "" );
        writer.writeProperty( DamageFontColor2, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        
        if ( loader.loadProperty( carImage ) );
        else if ( loader.loadProperty( TWearFontColor ) );
        else if ( loader.loadProperty( TTempFontColor ) );
        else if ( loader.loadProperty( TPressFontColor ) );
        else if ( loader.loadProperty( TWearFont ) );
        else if ( loader.loadProperty( TTempFont ) );
        else if ( loader.loadProperty( TPressFont ) );
        else if ( loader.loadProperty( ForcePSI ) );
        else if ( loader.loadProperty( ShowDamage ) );
        else if ( loader.loadProperty( ShowPressures ) );
        else if ( loader.loadProperty( EngineFontColor ) );
        else if ( loader.loadProperty( EngineFont ) );
        else if ( loader.loadProperty( DamageFontColor1 ) );
        else if ( loader.loadProperty( DamageFontColor2 ) );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        propsCont.addGroup( "Images" );
        propsCont.addProperty( carImage );
        
        propsCont.addGroup( "Fonts and Colors" );
        propsCont.addProperty( TWearFontColor );
        propsCont.addProperty( TWearFont );
        propsCont.addProperty( TTempFontColor );
        propsCont.addProperty( TTempFont );
        propsCont.addProperty( TPressFontColor );
        propsCont.addProperty( TPressFont );
        propsCont.addProperty( EngineFontColor );
        propsCont.addProperty( EngineFont );
        propsCont.addProperty( DamageFontColor1 );
        propsCont.addProperty( DamageFontColor2 );
        
        propsCont.addGroup( "Misc" );
        propsCont.addProperty(ShowPressures);
        propsCont.addProperty(ForcePSI);
        propsCont.addProperty(ShowDamage);
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
    
    public CarTelemWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 19.6f, 28.0f );
        
    }
  
}
