
package com.prunn.rfdynhud.widgets.prunn.f1_2011.highvoltage;

import java.awt.Font;
import java.io.IOException;

import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.IntProperty;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.widgets.WidgetsConfiguration;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;
import com.prunn.rfdynhud.widgets.prunn.f1_2011.highvoltage.NetworkThread;
/**
 * 
 * 
 * @author Prunn
 */
public class HighVoltageWidget extends Widget
{
    private IntProperty drs_port = new IntProperty("drs port", "drs port", 4322);
    private String drs_message;
    private BoolValue drsActivated = new BoolValue();
    private BoolValue canBeActivated = new BoolValue();
    private ImagePropertyWithTexture imgDRS = new ImagePropertyWithTexture("imgDRS", "prunn/f1_2011/revmetter/drs.png");
    private ImagePropertyWithTexture imgDRS2 = new ImagePropertyWithTexture("imgDRS2", "prunn/f1_2011/revmetter/drs2.png");
    private ImagePropertyWithTexture imgDRSdisabled = new ImagePropertyWithTexture("imgDRSdisabled", "prunn/f1_2011/revmetter/drs_disabled.png");
    private ImagePropertyWithTexture imgDRSavailable = new ImagePropertyWithTexture("imgDRSavailable", "prunn/f1_2011/revmetter/drs_available.png");
    private NetworkThread HighVoltage;
    private boolean initialised = false;
    
    public String getDefaultNamedColorValue(String name)
    {
        if(name.equals("StandardBackground"))
            return "#00000078";
        
        return null;
    }
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onRealtimeEntered( gameData, isEditorMode );
        
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
        
        imgDRS.updateSize( width, height, isEditorMode );
        imgDRS2.updateSize( width, height, isEditorMode );
        imgDRSdisabled.updateSize( width, height, isEditorMode );
        imgDRSavailable.updateSize( width, height, isEditorMode );
        
    }
    
    @Override
    protected Boolean updateVisibility( LiveGameData gameData, boolean isEditorMode )
    {
        super.updateVisibility( gameData, isEditorMode );
        
        if(!isEditorMode && !initialised)
        {
            HighVoltage = new NetworkThread(drs_port.getValue());
            initialised = true;
        }    
        if(!isEditorMode && initialised)
            drs_message = HighVoltage.get_drs_message;
        else
            drs_message = "Enabled:1\0Activated:0\0CanBeActivated:1\0CurrentZone:2\0";
        
        if(drs_message.substring( 0, 1 ).equals( "Z" ))
        {
            return false;
        }
        
        if(drs_message.substring( 20, 21 ).equals( "1" ))
            drsActivated.update( true );
        else
            drsActivated.update( false );
        
        if(drs_message.substring( 37, 38 ).equals( "1" ))
            canBeActivated.update( true );
        else
            canBeActivated.update( false );
        
        if((drsActivated.hasChanged() || canBeActivated.hasChanged()) && !isEditorMode)
            forceCompleteRedraw( true );
        
        return true;
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        
        if(drsActivated.getValue())
            texture.clear( imgDRS2.getTexture(), offsetX, offsetY, false, null );
        else
            if(!canBeActivated.getValue())
                texture.clear( imgDRSdisabled.getTexture(), offsetX, offsetY, false, null );
            else
                texture.clear( imgDRSavailable.getTexture(), offsetX, offsetY, false, null );
    }
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
    }
    @Override
    public void beforeConfigurationCleared( WidgetsConfiguration widgetsConfig, LiveGameData gameData, boolean isEditorMode )
    {
        super.beforeConfigurationCleared( widgetsConfig, gameData, isEditorMode );
        if(!isEditorMode)
        {
            //log("********CLOSING TIME************");
            HighVoltage.StopListenning();
        }
    }    
     
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( drs_port, "" );
        
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( drs_port ) );
        
        
    }
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Specific" );
        propsCont.addProperty( drs_port );
        
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareForMenuItem()
    {
        super.prepareForMenuItem();
        
        getFontProperty().setFont( "Dialog", Font.PLAIN, 9, false, true );
        
    }
    
    public HighVoltageWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 20.0f, 32.5f );
        getBackgroundProperty().setColorValue( "#00000078" );
        
    }
}
