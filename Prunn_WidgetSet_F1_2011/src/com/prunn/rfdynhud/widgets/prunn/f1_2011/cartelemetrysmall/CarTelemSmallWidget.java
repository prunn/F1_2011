package com.prunn.rfdynhud.widgets.prunn.f1_2011.cartelemetrysmall;

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
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */
public class CarTelemSmallWidget extends Widget
{
    private DrawnString dsTempFL = null;
    private DrawnString dsWearFL = null;
    private DrawnString dsEngineWear = null;
    private DrawnString dsOilTemp = null;
       
    private final FloatValue TempFL = new FloatValue( -1f, 0.1f );
    private final FloatValue TempFR = new FloatValue( -1f, 0.1f );
    private final FloatValue TempRL = new FloatValue( -1f, 0.1f );
    private final FloatValue TempRR = new FloatValue( -1f, 0.1f ); 
    //private final FloatValue TempMin = new FloatValue( -1f, 0.1f ); 
    private final FloatValue TempMax = new FloatValue( -1f, 0.1f ); 
    private final FloatValue WearFL = new FloatValue( -1f, 0.01f );
    private final FloatValue WearFR = new FloatValue( -1f, 0.01f );
    private final FloatValue WearRL = new FloatValue( -1f, 0.01f );
    private final FloatValue WearRR = new FloatValue( -1f, 0.01f );
    private final FloatValue WearMin = new FloatValue( -1f, 0.01f );
    private final FloatValue engine = new FloatValue( -1f, 0.01f );
    private final FloatValue OilTemp = new FloatValue( -1f, 0.01f );
    private final FloatValue BrakeTFL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTFR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTRL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTRR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeTempMax = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWFL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWFR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWRL = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWRR = new FloatValue( -1f, 0.1f );
    private final FloatValue BrakeWearMin = new FloatValue( -1f, 0.1f );
    
    private final ImageProperty imgTire = new ImageProperty( "imgTire", "prunn/f1_2011/telem/tiregreen.png" );
    private final ImageProperty imgOil = new ImageProperty( "imgOil", "prunn/F1_2011/telem/oil.png" );
    private final ImageProperty imgEngine = new ImageProperty( "imgEngine", "prunn/F1_2011/telem/engine.png" );
    private TextureImage2D texImgTire = null;
    private TextureImage2D texImgOil = null;
    private TextureImage2D texImgEngine = null;
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
        MeasurementUnits devilsunits = gameData.getProfileInfo().getMeasurementUnits();
        
        dsTempFL = drawnStringFactory.newDrawnString( "dsTempFL", width*50/100, height*13/100 - fhTemp/2, Alignment.CENTER, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits));
        dsWearFL = drawnStringFactory.newDrawnString( "dsWearFL", width*50/100, height*36/100 - fhWear/2, Alignment.CENTER, false, TWearFont.getFont(), isFontAntiAliased(), TWearFontColor.getColor(), null, "%" );
        
        dsEngineWear = drawnStringFactory.newDrawnString( "dsEngineWear", width / 2, height*9/10 , Alignment.CENTER, false, TWearFont.getFont(), isFontAntiAliased(), TWearFontColor.getColor(), null, "%" );
        dsOilTemp = drawnStringFactory.newDrawnString( "dsOilTemp", width*50/100, height *60/100, Alignment.CENTER, false, TTempFont.getFont(), isFontAntiAliased(), TTempFontColor.getColor(), null, gettempunits(devilsunits) );
        
        texImgTire = imgTire.getImage().getScaledTextureImage( 26, 30, texImgTire, isEditorMode );
        texImgOil = imgOil.getImage().getScaledTextureImage( 25, 25, texImgOil, isEditorMode );
        texImgEngine = imgEngine.getImage().getScaledTextureImage( 44, 32, texImgEngine, isEditorMode );
                
        
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
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        final TelemetryData telemData = gameData.getTelemetryData();
        
        TempFL.update( telemData.getTireTemperature( Wheel.FRONT_LEFT ) );
        TempFR.update( telemData.getTireTemperature( Wheel.FRONT_RIGHT ) );
        TempRL.update( telemData.getTireTemperature( Wheel.REAR_LEFT ) );
        TempRR.update( telemData.getTireTemperature( Wheel.REAR_RIGHT ) );
        //TempMin.update( Math.min( Math.min( TempFL.getValue(), TempFR.getValue()) , Math.min( TempRL.getValue(), TempRR.getValue() ) ) );
        TempMax.update( Math.max( Math.max( TempFL.getValue(), TempFR.getValue()) , Math.max( TempRL.getValue(), TempRR.getValue() ) ) );
        
        // Since each single value may change independently, we need each one in its own block with its own condition.
        //NumberUtil.formatFloat( TempMin.getValue(), 0, true ) + " - " + 
        if ( needsCompleteRedraw || ( clock.c() && TempMax.hasChanged() ) )
        {
        	if(TempMax.getValue() >= 0)
        	{            
	        	String tireTempFL = NumberUtil.formatFloat( TempMax.getValue(), 0, true );
	            dsTempFL.draw( offsetX, offsetY, tireTempFL, texture );
            }
        	else
        		dsTempFL.draw( offsetX, offsetY, "0", texture );
        }
        
                
        WearFL.update( telemData.getTireWear( Wheel.FRONT_LEFT ) );
        WearFR.update( telemData.getTireWear( Wheel.FRONT_RIGHT ) );
        WearRL.update( telemData.getTireWear( Wheel.REAR_LEFT ) );
        WearRR.update( telemData.getTireWear( Wheel.REAR_RIGHT ) );
        WearMin.update( Math.min( Math.min( WearFL.getValue(), WearFR.getValue()) , Math.min( WearRL.getValue(), WearRR.getValue() ) ) );
        
        if ( needsCompleteRedraw || ( clock.c() && WearMin.hasChanged() ) )
        {
            String tireWearFL = NumberUtil.formatFloat( WearMin.getValue() * 100, 0, true );
            dsWearFL.draw( offsetX, offsetY, tireWearFL, texture );
            
            if(WearMin.getValue() > 0.7 )
            	imgTire.setValue("prunn/f1_2011/telem/tiregreen.png") ;
            else
            	if(WearMin.getValue() > 0.4 )
                	imgTire.setValue("prunn/f1_2011/telem/tireyellow.png") ;
            	else
            		imgTire.setValue("prunn/f1_2011/telem/tirered.png") ;
            texImgTire = imgTire.getImage().getScaledTextureImage( 22, 30, texImgTire, isEditorMode );
            clearBackgroundRegion( texture, offsetX + width*50/100 - 11, offsetY + height*18/100, 0, 0, texImgTire.getWidth(), texImgTire.getHeight(), true, null );
            texture.drawImage( texImgTire, offsetX + width*50/100 - 11, offsetY + height*18/100, false, null );
            
        }
        
        
        
        double rlp = gameData.getScoringInfo().getRaceLengthPercentage();
        float engineLifetime100Percent = gameData.getPhysics().getEngine().getMaxLifetimeTotal( rlp );
        float currentEngineLifetime = engineLifetime100Percent - gameData.getPhysics().getEngine().getSafeLifetimeTotal( rlp ) + telemData.getEngineLifetime();
        
		engine.update( currentEngineLifetime  );
		OilTemp.update( gameData.getTelemetryData().getEngineOilTemperature() );
        
        if ( needsCompleteRedraw || ( clock.c() && engine.hasChanged() ) )
        {
            String EngineWear;
            
            if(engine.getValue()>=0)
                EngineWear = NumberUtil.formatFloat( engine.getValue() * 100f / engineLifetime100Percent, 0, true );
             else
                EngineWear ="0";
            
            dsEngineWear.draw( offsetX, offsetY, EngineWear, texture );
            texture.drawImage( texImgEngine, offsetX + width/2 - texImgEngine.getWidth()/2, offsetY + height*85/100 - texImgEngine.getHeight(), true, null );
            
        }
       
        if ( needsCompleteRedraw || ( clock.c() && OilTemp.hasChanged() ) )
        {
           String oil = NumberUtil.formatFloat( OilTemp.getValue(), 0, true );
           dsOilTemp.draw( offsetX, offsetY, oil, texture );
           
           if(OilTemp.getValue() <= gameData.getPhysics().getEngine().getOverheatingOilTemperature() )
           	    imgOil.setValue("prunn/F1_2011/telem/oil.png") ;
           else
           		imgOil.setValue("prunn/F1_2011/telem/oilhot.png") ;
           
           texImgOil = imgOil.getImage().getScaledTextureImage( 36, 36, texImgOil, isEditorMode );
           clearBackgroundRegion( texture, offsetX + width/2 - texImgOil.getWidth()/2, offsetY + height / 2 - texImgOil.getWidth()/2, 0, 0, texImgOil.getWidth(), texImgOil.getHeight(), true, null );
           texture.drawImage( texImgOil, offsetX + width/2 - texImgOil.getWidth()/2, offsetY + height / 2 - texImgOil.getHeight()/2, true, null );
           
        }
       
               
        BrakeTFL.update( telemData.getBrakeTemperature( Wheel.FRONT_LEFT ) );
        BrakeTFR.update( telemData.getBrakeTemperature( Wheel.FRONT_RIGHT ) );
        BrakeTRL.update( telemData.getBrakeTemperature( Wheel.REAR_LEFT ) );
        BrakeTRR.update( telemData.getBrakeTemperature( Wheel.REAR_RIGHT ) );
        BrakeTempMax.update( Math.max( Math.max( BrakeTFL.getValue(), BrakeTFR.getValue()) , Math.max( BrakeTRL.getValue(), BrakeTRR.getValue() ) ) );
        BrakeWFL.update( telemData.getBrakeDiscThickness( Wheel.FRONT_LEFT ) );
        BrakeWFR.update( telemData.getBrakeDiscThickness( Wheel.FRONT_RIGHT ) );
        BrakeWRL.update( telemData.getBrakeDiscThickness( Wheel.REAR_LEFT ) );
        BrakeWRR.update( telemData.getBrakeDiscThickness( Wheel.REAR_RIGHT ) );
        BrakeWearMin.update( Math.min( Math.min( BrakeWFL.getValue(), BrakeWFR.getValue()) , Math.min( BrakeWRL.getValue(), BrakeWRR.getValue() ) ) );
        
       
        if ( needsCompleteRedraw || ( clock.c() && BrakeTempMax.hasChanged() ) || ( clock.c() && BrakeWearMin.hasChanged() ) )
        {
            float percentWear = (BrakeWearMin.getValue() - gameData.getPhysics().getBrakes().getBrake( Wheel.FRONT_LEFT ).getMaxDiscFailure() ) / (gameData.getSetup().getWheelAndTire(Wheel.FRONT_LEFT).getBrakeDiscThickness()- gameData.getPhysics().getBrakes().getBrake( Wheel.FRONT_LEFT ).getMaxDiscFailure());
            drawBrake(BrakeTempMax.getValue(), percentWear, gameData.getPhysics().getBrakes().getBrake(Wheel.FRONT_LEFT), texture, offsetX + width*14/100, offsetY + height*20/100);
        }
       
               
    }
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        
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
        
        if ( loader.loadProperty( TWearFontColor ) );
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
    
    public CarTelemSmallWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 19.6f, 28.0f );
        
    }
  
}
