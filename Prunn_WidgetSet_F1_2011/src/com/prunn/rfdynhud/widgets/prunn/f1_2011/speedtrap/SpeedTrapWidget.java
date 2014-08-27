package com.prunn.rfdynhud.widgets.prunn.f1_2011.speedtrap;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.gamedata.ProfileInfo.MeasurementUnits;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
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
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class SpeedTrapWidget extends Widget
{
    
    private DrawnString dsCurSpeed = null;
    private DrawnString dsLeadSpeed = null;
    private DrawnString dsSlabel = null;
    
    private final ImagePropertyWithTexture imgTrap = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/SpeedTrap.png" );
    private final ImagePropertyWithTexture imgLead = new ImagePropertyWithTexture( "imgLead", "prunn/f1_2011/data_neutral.png" );
    
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    protected final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    private final BooleanProperty forceleader = new BooleanProperty("Force Leader", false);
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    
    FloatValue CurSpeed = new FloatValue(-1F, 0.001F);
    
    private FloatValue topspeedchanged = new FloatValue(-1F, 0.001F);
    private float speedtrap = 0;
    private float lastspeed = 0;
    private float topspeed = 0;
    
    
    
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
        int offx = width - 20;
        int rowHeight = height / 2;
        int fh = TextureImage2D.getStringHeight( "0gyI", f1_2011Font );
        
        
        imgTrap.updateSize( width, rowHeight, isEditorMode );
        imgLead.updateSize( width*4/5, rowHeight, isEditorMode );
        
        if(!isEditorMode)
            speedtrap = GetSpeedTrapLogFile(gameData);
        else
            speedtrap = 0;

        dsSlabel = drawnStringFactory.newDrawnString( "dsSlabel", width/9, ( rowHeight - fh ) / 2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
        dsCurSpeed = drawnStringFactory.newDrawnString( "dsCurSpeed", offx, ( rowHeight - fh ) / 2 + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
        dsLeadSpeed = drawnStringFactory.newDrawnString( "dsLeadSpeed", offx, rowHeight + ( rowHeight - fh ) / 2 + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
        
        
        
    }
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        VehicleScoringInfo currentcarinfos = scoringInfo.getViewedVehicleScoringInfo();
        topspeedchanged.update( currentcarinfos.getTopspeed() );
        
        if(topspeedchanged.hasChanged() && currentcarinfos.getStintLength() > 1 && lastspeed >= CurSpeed.getValue() && !isEditorMode)
        {
            if(speedtrap <= currentcarinfos.getLapDistance() - 120 || speedtrap >= currentcarinfos.getLapDistance() + 120 || speedtrap == 0)
            {
                speedtrap = currentcarinfos.getLapDistance();
                try
                {
                    UpdateSpeedTrapLogFile(speedtrap, gameData);
                }
                catch ( IOException e )
                {
                    log(e);
                }
            }
        }
        
        //200 m range for listenning, visible 300m after the trap
        if(currentcarinfos.getLapDistance() >= speedtrap - 200 && currentcarinfos.getLapDistance() <= speedtrap + 300 && currentcarinfos.getCurrentLaptime() > 0 && ( speedtrap != 0 || isEditorMode))
        {
            CurSpeed.update(  currentcarinfos.getScalarVelocity() ) ;
            
            if(lastspeed > CurSpeed.getValue())
            {
                topspeed = lastspeed;
                return true;
            }
            else
            {
                lastspeed = CurSpeed.getValue();
                return false;
            }
            
        }
        else
            lastspeed = CurSpeed.getValue();
            
            
        return false;
         
    }
    
    protected void UpdateSpeedTrapLogFile(float trapvalue, LiveGameData data) throws IOException
    {
        
        Writer output = null;
        String text = NumberUtil.formatFloat( trapvalue, 0, false );
        File file = new File(data.getFileSystem().getCacheFolder() + "/data/speedtraps/" + data.getTrackInfo().getTrackName() + ".spt");
        output = new BufferedWriter(new FileWriter(file));
        output.write(text);
        output.close();     
                
    }
    protected float GetSpeedTrapLogFile(LiveGameData data)
    {
        float trapvalue = 0;
        
        try
        {
            File file = new File(data.getFileSystem().getCacheFolder() + "/data/speedtraps/" + data.getTrackInfo().getTrackName() + ".spt");
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            
            trapvalue = Float.valueOf(br.readLine()).floatValue();
            
            br.close();
        }
        catch (Exception e)
        {
            trapvalue = 0;
        }
        
            
        return trapvalue;
        
        
    }
    
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        
        texture.clear( imgTrap.getTexture(), offsetX, offsetY, false, null );
        
        if(forceleader.getValue())
            texture.clear( imgLead.getTexture(), offsetX + width/7, offsetY + height/2, false, null );
        
        
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        if(isEditorMode)
            topspeed = 325.5f;
        if ( needsCompleteRedraw || clock.c() )
        {
            dsSlabel.draw( offsetX, offsetY, "S", texture );
            if(gameData.getProfileInfo().getMeasurementUnits() == MeasurementUnits.METRIC)
                dsCurSpeed.draw( offsetX, offsetY, NumberUtil.formatFloat( topspeed ,1,false) + " km/h", texture );
            else
                dsCurSpeed.draw( offsetX, offsetY, NumberUtil.formatFloat( topspeed ,1,false) + " mph", texture );
               
            if(forceleader.getValue())
                dsLeadSpeed.draw( offsetX, offsetY, NumberUtil.formatFloat( gameData.getScoringInfo().getLeadersVehicleScoringInfo().getTopspeed(), 1, false ) + " km/h",texture);
        	
        }
         
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( forceleader, "" );
        writer.writeProperty( fontyoffset, "" );
        
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( forceleader ) );
        else if ( loader.loadProperty( fontyoffset ) );
        
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Colors" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( fontColor2 );
        propsCont.addProperty( forceleader );

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
    
    public SpeedTrapWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 17.0f, 9.3f );
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
