package com.prunn.rfdynhud.widgets.prunn.f1_2011.tracktemps;

import java.awt.Font;
import java.io.IOException;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

import net.ctdp.rfdynhud.gamedata.LiveGameData;
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
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class TrackTempsWidget extends Widget
{
    private DrawnString dsAmbient = null;
    private DrawnString dsAmbientTemp = null;
    private DrawnString dsTrack = null;
    private DrawnString dsTrackTemp = null;
    
    private IntValue AmbientTemp = new IntValue();
    private IntValue TrackTemp = new IntValue();
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    
    private final ImagePropertyWithTexture imgName = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/data_temp.png" );
    
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    protected final ColorProperty WhiteFontColor = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    private BooleanProperty ShowAmbiant = new BooleanProperty("Show Ambiant", "ShowAmbiant", false);
    
    
    
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
        
        imgName.updateSize( width*94/100, height / 2, isEditorMode );
        
        dsAmbient = drawnStringFactory.newDrawnString( "dsAmbient", width*7/100, height*3/4 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), WhiteFontColor.getColor(), null, "" );
        dsAmbientTemp = drawnStringFactory.newDrawnString( "dsAmbientTemp", width*85/100, height*3/4 - fh/2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), WhiteFontColor.getColor(), null, "°C");
        
        dsTrack = drawnStringFactory.newDrawnString( "dsTrack", width*14/100, height/4 - fh/2 + fontyoffset.getValue(), Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), WhiteFontColor.getColor(), null, "" );
        dsTrackTemp = drawnStringFactory.newDrawnString( "dsTrackTemp", width*92/100, height/4 - fh/2 + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), WhiteFontColor.getColor(), null, "°C");
        
    }
    
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        texture.clear( imgName.getTexture(), offsetX + width*6/100, offsetY, false, null );
        
        if(ShowAmbiant.getValue())
            texture.clear( imgName.getTexture(), offsetX, offsetY + height/2, false, null );
        
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        
        AmbientTemp.update((int)Math.floor(gameData.getWeatherInfo().getAmbientTemperature()));
        TrackTemp.update((int)Math.floor(gameData.getWeatherInfo().getTrackTemperature()));
        
        if ( ShowAmbiant.getValue() && (needsCompleteRedraw || AmbientTemp.hasChanged()) )
        {
            dsAmbient.draw( offsetX, offsetY, Loc.ambiant_temp, texture );
            dsAmbientTemp.draw( offsetX, offsetY, AmbientTemp.getValueAsString(), texture);
        }
        if ( needsCompleteRedraw || TrackTemp.hasChanged())
        {
            dsTrack.draw( offsetX, offsetY, Loc.track_temp, texture );
            dsTrackTemp.draw( offsetX, offsetY, TrackTemp.getValueAsString(), texture);
        }
         
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( WhiteFontColor, "" );
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( ShowAmbiant, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( WhiteFontColor ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( ShowAmbiant ) );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Colors" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( WhiteFontColor );
        propsCont.addProperty( fontyoffset );
        propsCont.addProperty( ShowAmbiant );
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
        //getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
        getFontProperty().setFont( "Dialog", Font.PLAIN, 6, false, true );
        
    }
    
    public TrackTempsWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 15.6f, 7.8f );
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
