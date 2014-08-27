package com.prunn.rfdynhud.widgets.prunn.f1_2011.combinedlaptime;

import java.awt.Font;
import java.io.IOException;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;

import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.FontProperty;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class CombinedSectorsWidget extends Widget
{
    private DrawnString dsName1 = null;
    private DrawnString dsTime1 = null;
    private DrawnString dsName2 = null;
    private DrawnString dsTime2 = null;
    private DrawnString dsName3 = null;
    private DrawnString dsTime3 = null;
    private DrawnString dsCombined = null;
    private DrawnString dsS1 = null;
    private DrawnString dsS2 = null;
    private DrawnString dsS3 = null;
    private DrawnString dsTitle = null;
    private DrawnString dsComputed = null;
    private FloatValue time1 = new FloatValue(-1F, 0.001F);
    private FloatValue time2 = new FloatValue(-1F, 0.001F);
    private FloatValue time3 = new FloatValue(-1F, 0.001F);
    private FloatValue fastestcpu = new FloatValue(-1F, 0.001F);
    private String sec1name="", sec2name="",sec3name="";
    
    private TextureImage2D texName = null;
    private TextureImage2D texTime = null;
    private final ImagePropertyWithTexture imgName = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgTime = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/race_gap.png" );
    private final ImagePropertyWithTexture imgTitle = new ImagePropertyWithTexture( "imgTitle", "prunn/f1_2011/data_caption_big.png" );
    private final ImagePropertyWithTexture imgTitleSmall = new ImagePropertyWithTexture( "imgTitle", "prunn/f1_2011/data_caption_small.png" );
    
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    
    
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
        int fh = TextureImage2D.getStringHeight( "0%C", f1_2011Font );
        int rowHeight = height / 5;
        imgName.updateSize( Math.round(width * 0.6f), rowHeight, isEditorMode );
        imgTime.updateSize( width - imgName.getTexture().getWidth(), rowHeight, isEditorMode );
        
        texName = imgName.getImage().getScaledTextureImage( width*40/100, rowHeight, texName, isEditorMode );
        texTime = imgTime.getImage().getScaledTextureImage( width*26/100, rowHeight, texTime, isEditorMode );
        
        imgTitle.updateSize( width*62/100, rowHeight, isEditorMode );
        imgTitleSmall.updateSize( width*24/100, rowHeight, isEditorMode );
        dsTitle = drawnStringFactory.newDrawnString( "dsTitle", width*20/100, rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsS1 = drawnStringFactory.newDrawnString( "dsS1", width*16/100, rowHeight*1 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsS2 = drawnStringFactory.newDrawnString( "dsS2", width*12/100, rowHeight*2 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsS3 = drawnStringFactory.newDrawnString( "dsS3", width*8/100, rowHeight*3 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsCombined = drawnStringFactory.newDrawnString( "dsCombined", width*4/100, rowHeight*4 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        
        dsName1 = drawnStringFactory.newDrawnString( "dsName1", width*40/100, rowHeight*1 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTime1 = drawnStringFactory.newDrawnString( "dsTime1", width*95/100, rowHeight*1 + rowHeight/2 - fh/2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
        
        dsName2 = drawnStringFactory.newDrawnString( "dsName2", width*35/100, rowHeight*2 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTime2 = drawnStringFactory.newDrawnString( "dsTime2", width*91/100, rowHeight*2 + rowHeight/2 - fh/2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
        
        dsName3 = drawnStringFactory.newDrawnString( "dsName3", width*30/100, rowHeight*3 + rowHeight/2 - fh/2, Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor(), null, "" );
        dsTime3 = drawnStringFactory.newDrawnString( "dsTime3", width*86/100, rowHeight*3 + rowHeight/2 - fh/2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
        
        dsComputed = drawnStringFactory.newDrawnString( "dsComputed", width*81/100, rowHeight*4 + rowHeight/2 - fh/2, Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor());
    }
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        
        if(scoringInfo.getLeadersVehicleScoringInfo().getFastestLaptime() != null && scoringInfo.getLeadersVehicleScoringInfo().getFastestLaptime().getLapTime() > 0)
        {
            
            float sec1time = scoringInfo.getLeadersVehicleScoringInfo().getFastestLaptime().getSector1();
            float sec2time = scoringInfo.getLeadersVehicleScoringInfo().getFastestLaptime().getSector2();
            float sec3time = scoringInfo.getLeadersVehicleScoringInfo().getFastestLaptime().getSector3();
            sec1name = PrunnWidgetSetf1_2011.ShortName( scoringInfo.getLeadersVehicleScoringInfo().getDriverNameShort());
            sec2name = PrunnWidgetSetf1_2011.ShortName( scoringInfo.getLeadersVehicleScoringInfo().getDriverNameShort());
            sec3name = PrunnWidgetSetf1_2011.ShortName( scoringInfo.getLeadersVehicleScoringInfo().getDriverNameShort());
            
            VehicleScoringInfo vsi;
            for(int i=1;i < scoringInfo.getNumVehicles();i++)
            {
                vsi = scoringInfo.getVehicleScoringInfo( i );
                if(vsi.getFastestLaptime() != null && vsi.getFastestLaptime().getSector1() > 0 && vsi.getFastestLaptime().getSector1() < sec1time)
                {
                    sec1time = vsi.getFastestLaptime().getSector1();
                    sec1name = PrunnWidgetSetf1_2011.ShortName( vsi.getDriverNameShort());
                }
                if(vsi.getFastestLaptime() != null && vsi.getFastestLaptime().getSector2() > 0 && vsi.getFastestLaptime().getSector2() < sec2time)
                {
                    sec2time = vsi.getFastestLaptime().getSector2();
                    sec2name = PrunnWidgetSetf1_2011.ShortName( vsi.getDriverNameShort());
                }
                if(vsi.getFastestLaptime() != null && vsi.getFastestLaptime().getSector3() > 0 && vsi.getFastestLaptime().getSector3() < sec3time)
                {
                    sec3time = vsi.getFastestLaptime().getSector3();
                    sec3name = PrunnWidgetSetf1_2011.ShortName( vsi.getDriverNameShort());
                }
            }
                        
            time1.update( sec1time );
            time2.update( sec2time );
            time3.update( sec3time );
            
            fastestcpu.update( time1.getValue() + time2.getValue() + time3.getValue() );
            if(fastestcpu.hasChanged())
               forceCompleteRedraw(true);
            if(sec1time < 800 && sec2time < 800 && sec3time < 800)
                return true;
        }
        return false;
    		
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        int rowHeight = height / 5;
        texture.clear( imgTitle.getTexture(), offsetX + width*17/100, offsetY, false, null );
        texture.clear( imgTitleSmall.getTexture(), offsetX + width*13/100, offsetY + rowHeight, false, null );
        texture.drawImage( texName, offsetX + width*35/100, offsetY + rowHeight, false, null );
        texture.drawImage( texTime, offsetX + width*72/100, offsetY + rowHeight, false, null );
        texture.clear( imgTitleSmall.getTexture(), offsetX + width*9/100, offsetY + rowHeight*2, false, null );
        texture.drawImage( texName, offsetX + width*31/100, offsetY + rowHeight*2, false, null );
        texture.drawImage( texTime, offsetX + width*68/100, offsetY + rowHeight*2, false, null );
        texture.clear( imgTitleSmall.getTexture(), offsetX + width*5/100, offsetY + rowHeight*3, false, null );
        texture.drawImage( texName, offsetX + width*27/100, offsetY + rowHeight*3, false, null );
        texture.drawImage( texTime, offsetX + width*64/100, offsetY + rowHeight*3, false, null );
        texture.clear( imgTitle.getTexture(), offsetX, offsetY + rowHeight*4, false, null );
        texture.drawImage( texTime, offsetX + width*59/100, offsetY + rowHeight*4, false, null );
        
        
        
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        
         
        if ( needsCompleteRedraw || time1.hasChanged())
        {
            dsName1.draw( offsetX, offsetY, sec1name, texture );
            dsTime1.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( time1.getValue() ), texture);
        }
        if ( needsCompleteRedraw || time2.hasChanged())
        {
            dsName2.draw( offsetX, offsetY, sec2name, texture );
            dsTime2.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( time2.getValue() ), texture);
        }
        if ( needsCompleteRedraw || time3.hasChanged())
        {   
            dsName3.draw( offsetX, offsetY, sec3name, texture );
            dsTime3.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( time3.getValue() ), texture);
        }
        if ( needsCompleteRedraw || fastestcpu.hasChanged() )
        {
            dsComputed.draw( offsetX, offsetY, TimingUtil.getTimeAsLaptimeString( fastestcpu.getValue() ), texture);
        }/*"Fastest Sector Times""Sector 1""Sector 2""Sector 3""Combined Lap Time"*/
        if ( needsCompleteRedraw )
        {
            dsTitle.draw( offsetX, offsetY, Loc.fastest_sector_times, texture );
            dsS1.draw( offsetX, offsetY, Loc.sector_1, texture );
            dsS2.draw( offsetX, offsetY, Loc.sector_2, texture );
            dsS3.draw( offsetX, offsetY, Loc.sector_3, texture );
            dsCombined.draw( offsetX, offsetY, Loc.combined_laptime, texture );
            
        }
            
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( fontColor2, "" );
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( fontColor2 ) );
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Colors" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( fontColor2 );
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
    
    public CombinedSectorsWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 42.2f, 14.7f );
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
