
package com.prunn.rfdynhud.widgets.prunn.f1_2011.drstool;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.input.InputAction;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.values.StringValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

/**
 * 
 * 
 * @author Prunn
 */
public class DRStoolWidget extends Widget
{
    private DrawnString dsCurrent = null;
    private DrawnString dsDetection = null;
    private DrawnString dsActivation = null;
    private DrawnString dsDeactivation = null;
    private DrawnString dsDetection2 = null;
    private DrawnString dsActivation2 = null;
    private DrawnString dsDeactivation2 = null;
    private DrawnString dsMessage = null;
    private IntValue current = new IntValue();
    private IntValue detection = new IntValue();
    private IntValue activation = new IntValue();
    private IntValue deactivation = new IntValue();
    private IntValue detection2 = new IntValue();
    private IntValue activation2 = new IntValue();
    private IntValue deactivation2 = new IntValue();
    private IntValue mode = new IntValue();
    private Integer ButtonPress = 0;
    private static final InputAction setDRS_ACTION = new InputAction( "setDRS Action", true );
    private final ColorProperty editFontColor = new ColorProperty("editFontColor", "editFontColor");
    private StringValue Message = null;
    final static int taille = 1024;
    final static byte buffer[] = new byte[taille];

    @Override
    public InputAction[] getInputActions()
    {
        return ( new InputAction[] { setDRS_ACTION } );
    }
    public String getDefaultNamedColorValue(String name)
    {
        if(name.equals("StandardBackground"))
            return "#00000099";
        if(name.equals("StandardFontColor"))
            return "#FFFFFF";
        if(name.equals("editFontColor"))
            return "#FF0000";

        return null;
    }
    @Override
    public void onRealtimeEntered( LiveGameData gameData, boolean isEditorMode )
    {
        super.onCockpitEntered( gameData, isEditorMode );
        
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean onBoundInputStateChanged( InputAction action, boolean state, int modifierMask, long when, LiveGameData gameData, boolean isEditorMode )
    {
        Boolean result = super.onBoundInputStateChanged( action, state, modifierMask, when, gameData, isEditorMode );
        ButtonPress++;
        if ( action == setDRS_ACTION )
        {
            mode.update( ButtonPress % 7 );
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
    
    
    @Override
    protected void initialize( LiveGameData gameData, boolean isEditorMode, DrawnStringFactory drawnStringFactory, TextureImage2D texture, int width, int height )
    {
    	
        dsCurrent = drawnStringFactory.newDrawnString( "dsCurrent", 0, 0, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsDetection = drawnStringFactory.newDrawnString( "dsDetection", 0, 30, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsActivation = drawnStringFactory.newDrawnString( "dsActivation", 0, 50, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsDeactivation = drawnStringFactory.newDrawnString( "dsDeactivation", 0, 70, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsDetection2 = drawnStringFactory.newDrawnString( "dsDetection2", 0, 90, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsActivation2 = drawnStringFactory.newDrawnString( "dsActivation2", 0, 110, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsDeactivation2 = drawnStringFactory.newDrawnString( "dsDeactivation2", 0, 130, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        dsMessage = drawnStringFactory.newDrawnString( "dsMessage", 0, 150, Alignment.LEFT, false, getFont(), isFontAntiAliased(), getFontColor(), null,  ""  );
        Message = new StringValue();
        //if(!isEditorMode)
            GetDrsLogFile(gameData);
        
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
            
            
        
    }
    protected void UpdateDrsLogFile(LiveGameData gameData) throws IOException
    {
        Writer output = null;
        File file = new File(gameData.getFileSystem().getCacheFolder() + "/data/drszones/" + gameData.getTrackInfo().getTrackName() + ".txt");
        output = new BufferedWriter(new FileWriter(file));
        output.write("detection=" + detection.getValueAsString() + "\r\n");
        output.write("activation=" + activation.getValueAsString() + "\r\n");
        output.write("deactivation=" + deactivation.getValueAsString() + "\r\n");
        output.write("detection=" + detection2.getValueAsString() + "\r\n");
        output.write("activation=" + activation2.getValueAsString() + "\r\n");
        output.write("deactivation=" + deactivation2.getValueAsString() + "\r\n");
        output.close();     
                
    }
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        boolean modechanged = mode.hasChanged();
        current.update( (int)gameData.getScoringInfo().getViewedVehicleScoringInfo().getLapDistance() );
        
        if(mode.getValue() == 1 && modechanged)
            Message.update( "place detection" );
        else if(mode.getValue() == 2 && modechanged)        
        {
            detection.update( current.getValue() );
            Message.update( "place activation" );
        }
        else if(mode.getValue() == 3 && modechanged)       
        {
            activation.update( current.getValue() );
            Message.update( "place deactivation" );
        }
        else if(mode.getValue() == 4 && modechanged)       
        {
            deactivation.update( current.getValue() );
            Message.update( "place detection 2" );
        }
        else if(mode.getValue() == 5 && modechanged)       
        {
            detection2.update( current.getValue() );
            Message.update( "place activation 2" );
        }
        else if(mode.getValue() == 6 && modechanged)       
        {
            activation2.update( current.getValue() );
            Message.update( "place deactivation 2" );
        }
        else if(mode.getValue() == 0 && ButtonPress > 3 && modechanged)
        {
            deactivation2.update( current.getValue() );
            //add save file
            try
            {
                UpdateDrsLogFile(gameData);
                Message.update( "Zone Saved" );
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Message.update( "Error check log" );
            }
              
        }
        else
            Message.update( "" );
        
        
        
        if ( needsCompleteRedraw || ( clock.c() && current.hasChanged()))
            dsCurrent.draw( offsetX, offsetY, "Current: " + current.getValueAsString(), texture );
        
        if ( needsCompleteRedraw || modechanged )
        {
            dsDetection.draw( offsetX, offsetY, "Detection: " + detection.getValue(), (mode.getValue() == 1) ? editFontColor.getColor() : getFontColor(), texture );
            dsActivation.draw( offsetX, offsetY, "Activation: " + activation.getValue(), (mode.getValue() == 2) ? editFontColor.getColor() : getFontColor(), texture );
            dsDeactivation.draw( offsetX, offsetY, "Deactivation: " + deactivation.getValue(), (mode.getValue() == 3) ? editFontColor.getColor() : getFontColor(), texture );
            dsDetection2.draw( offsetX, offsetY, "Detection2: " + detection2.getValue(), (mode.getValue() == 4) ? editFontColor.getColor() : getFontColor(), texture );
            dsActivation2.draw( offsetX, offsetY, "Activation2: " + activation2.getValue(), (mode.getValue() == 5) ? editFontColor.getColor() : getFontColor(), texture );
            dsDeactivation2.draw( offsetX, offsetY, "Deactivation2: " + deactivation2.getValue(), (mode.getValue() == 6) ? editFontColor.getColor() : getFontColor(), texture );
            dsMessage.draw( offsetX, offsetY, Message.getValue(), (mode.getValue() == 0 && ButtonPress > 3) ? editFontColor.getColor() : getFontColor(), texture );
        
        }
         
         
    }
        
      
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        
        writer.writeProperty( editFontColor, "" );
        
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        
        if ( loader.loadProperty( editFontColor ) );
        
    }
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Specific" );
        
        propsCont.addProperty( editFontColor );
        
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
    
    public DRStoolWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011_Race, 20.0f, 32.5f );
        
    }
}
