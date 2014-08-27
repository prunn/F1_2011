package com.prunn.rfdynhud.widgets.prunn.f1_2011.qualifinfo;

import java.awt.Font;
import java.io.IOException;
import com.prunn.rfdynhud.widgets.prunn._util.PrunnWidgetSetf1_2011;
import com.prunn.rfdynhud.widgets.prunn.f1_2011.qtime.QualTimeWidget;

import net.ctdp.rfdynhud.gamedata.LiveGameData;
import net.ctdp.rfdynhud.gamedata.ScoringInfo;
import net.ctdp.rfdynhud.gamedata.VehicleScoringInfo;
import net.ctdp.rfdynhud.properties.BooleanProperty;
import net.ctdp.rfdynhud.properties.ColorProperty;
import net.ctdp.rfdynhud.properties.DelayProperty;
import net.ctdp.rfdynhud.properties.FontProperty;
import net.ctdp.rfdynhud.properties.ImagePropertyWithTexture;
import net.ctdp.rfdynhud.properties.IntProperty;
import net.ctdp.rfdynhud.properties.PropertiesContainer;
import net.ctdp.rfdynhud.properties.PropertyLoader;
import net.ctdp.rfdynhud.properties.StringProperty;
import net.ctdp.rfdynhud.render.DrawnString;
import net.ctdp.rfdynhud.render.DrawnStringFactory;
import net.ctdp.rfdynhud.render.TextureImage2D;
import net.ctdp.rfdynhud.render.DrawnString.Alignment;
import net.ctdp.rfdynhud.util.NumberUtil;
import net.ctdp.rfdynhud.util.PropertyWriter;
import net.ctdp.rfdynhud.util.SubTextureCollector;
import net.ctdp.rfdynhud.util.TimingUtil;
import net.ctdp.rfdynhud.valuemanagers.Clock;
import net.ctdp.rfdynhud.values.BoolValue;
import net.ctdp.rfdynhud.values.FloatValue;
import net.ctdp.rfdynhud.values.IntValue;
import net.ctdp.rfdynhud.values.StringValue;
import net.ctdp.rfdynhud.widgets.base.widget.Widget;

/**
 * @author Prunn
 * copyright@Prunn2011
 * 
 */


public class QualifInfoWidget extends Widget
{
    
    
    private DrawnString dsPos = null;
    private DrawnString dsName = null;
    private DrawnString dsTeam = null;
    private DrawnString dsTime = null;
    private DrawnString dsGap = null;
    private DrawnString dsTire = null;
    private DrawnString dsTire2 = null;
    private TextureImage2D texPos = null;
    private TextureImage2D texTime = null;
    private final ImagePropertyWithTexture imgPos = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/big_position_neutral.png" );
    private final ImagePropertyWithTexture imgPosFirst = new ImagePropertyWithTexture( "imgPos", "prunn/f1_2011/big_position_first.png" );
    private final ImagePropertyWithTexture imgPosOut = new ImagePropertyWithTexture( "imgPosOut", "prunn/f1_2011/big_position_knockout.png" );
    private final ImagePropertyWithTexture imgName = new ImagePropertyWithTexture( "imgName", "prunn/f1_2011/data_neutral.png" );
    private final ImagePropertyWithTexture imgTeam = new ImagePropertyWithTexture( "imgTeam", "prunn/f1_2011/data_caption.png" );
    private final ImagePropertyWithTexture imgTime = new ImagePropertyWithTexture( "imgTime", "prunn/f1_2011/race_gap.png" );
    
    private final FontProperty posFont = new FontProperty("positionFont", PrunnWidgetSetf1_2011.POS_FONT_NAME);
    protected final FontProperty f1_2011Font = new FontProperty("Main Font", PrunnWidgetSetf1_2011.F1_2011_FONT_NAME);
    private final ColorProperty fontColor2 = new ColorProperty("fontColor2", PrunnWidgetSetf1_2011.FONT_COLOR2_NAME);
    private IntProperty knockout = new IntProperty("Knockout position", 10);
    private final ColorProperty KnockoutFontColor = new ColorProperty("Knockout Font Color", PrunnWidgetSetf1_2011.FONT_COLOR4_NAME);
    private BooleanProperty uppercasename = new BooleanProperty("uppercase name",true); 
    private final FloatValue sessionTime = new FloatValue(-1F, 0.1F);
    
    private final DelayProperty visibleTime;
    private long visibleEnd;
    private IntValue cveh = new IntValue();
    private BoolValue cpit = new BoolValue();
    private StringValue team = new StringValue();
    private StringValue name = new StringValue();
    private StringValue pos = new StringValue();
    private StringValue gap = new StringValue();
    private StringValue time = new StringValue();
    private IntProperty fontyoffset = new IntProperty("Y Font Offset", 0);
    private final ColorProperty TireFontColor = new ColorProperty("Default Font Color", "TireFontColor");
    private final ColorProperty TireFontColorInter = new ColorProperty("Inter Tire Color", "TireFontColorInter");
    private final ColorProperty TireFontColorSSoft = new ColorProperty("SSoft Color", "TireFontColorSSoft");
    private final ColorProperty TireFontColorMedium = new ColorProperty("Medium Tire Color", "TireFontColorMedium");
    private final ColorProperty TireFontColorHard = new ColorProperty("Hard Tire Color", "TireFontColorHard");
    private ColorProperty TireDrawnFontColor;
    private StringProperty SSoft = new StringProperty("S-Soft Tire Name", "s-soft");
    private StringProperty Med = new StringProperty("Med Tire Name", "med");
    private StringProperty Hard = new StringProperty("Hard Tire Name", "hard");
    private StringProperty Inter = new StringProperty("Inter Tire Name", "inter");
    
    public String getDefaultNamedColorValue(String name)
    {
        if(name.equals("TireFontColor"))
            return "#FCD128";
        if(name.equals("TireFontColorInter"))//FE6C16
            return "#07A2E2";
        if(name.equals("TireFontColorSSoft"))
            return "#DC0211";
        if(name.equals("TireFontColorMedium"))
            return "#EFEFEF";
        if(name.equals("TireFontColorHard"))
            return "#D2D2D2";
        
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
        
        team.update( "" );
        name.update( "" ); 
        pos.update( "" );
        gap.update( "" ); 
        time.update( "" ); 
        
        int rowHeight = height / 2;
        int fh = TextureImage2D.getStringHeight( "0", f1_2011Font );
        int fhPos = TextureImage2D.getStringHeight( "0", posFont );
        
        texPos = imgPos.getImage().getScaledTextureImage( width*21/100, height, texPos, isEditorMode );
        imgPosFirst.updateSize( rowHeight*3, rowHeight*2, isEditorMode );
        imgPosOut.updateSize( rowHeight*3, rowHeight, isEditorMode );
        imgName.updateSize(width*87/200, rowHeight, isEditorMode );
        imgTeam.updateSize( width*87/200, rowHeight, isEditorMode );
        imgTime.updateSize( width*24/100, rowHeight, isEditorMode );
        texTime = imgTime.getImage().getScaledTextureImage( width*24/100, rowHeight, texTime, isEditorMode );
        
        int top1 = ( rowHeight - fh ) / 2;
        int top1Pos = ( height - fhPos ) / 2;
        int top2 = rowHeight + ( rowHeight - fh ) / 2;
        dsPos = drawnStringFactory.newDrawnString( "dsPos", width*50/100, top1Pos + fontyoffset.getValue(), Alignment.CENTER, false, posFont.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        dsName = drawnStringFactory.newDrawnString( "dsName", width*8/100, top1 + fontyoffset.getValue(), Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        dsTeam = drawnStringFactory.newDrawnString( "dsTeam", width*4/100, top2 + fontyoffset.getValue(), Alignment.LEFT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        dsTime = drawnStringFactory.newDrawnString( "dsTime", width*78/100, top1 + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        dsGap = drawnStringFactory.newDrawnString( "dsGap", width*75/100, top2 + fontyoffset.getValue(), Alignment.RIGHT, false, f1_2011Font.getFont(), isFontAntiAliased(), fontColor2.getColor() );
        dsTire = drawnStringFactory.newDrawnString( "dsTire", width*88/100, top2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), TireFontColor.getColor() );
        dsTire2 = drawnStringFactory.newDrawnString( "dsTire", width*53/100, top2 + fontyoffset.getValue(), Alignment.CENTER, false, f1_2011Font.getFont(), isFontAntiAliased(), TireFontColor.getColor() );
        
    }
    
    @Override
    protected Boolean updateVisibility(LiveGameData gameData, boolean isEditorMode)
    {
        
        
        super.updateVisibility(gameData, isEditorMode);
        ScoringInfo scoringInfo = gameData.getScoringInfo();
        cveh.update(scoringInfo.getViewedVehicleScoringInfo().getDriverId());
        cpit.update(scoringInfo.getViewedVehicleScoringInfo().isInPits());
        
        if(QualTimeWidget.visible())
            return false;
        
        if((cveh.hasChanged() || cpit.hasChanged()) && !isEditorMode)
        {
            forceCompleteRedraw(true);
            visibleEnd = scoringInfo.getSessionNanos() + visibleTime.getDelayNanos();
            return true;
        }
        
        if(scoringInfo.getSessionNanos() < visibleEnd || cpit.getValue())
            return true;
        
        
        return false;	
    }
    @Override
    protected void drawBackground( LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height, boolean isRoot )
    {
        super.drawBackground( gameData, isEditorMode, texture, offsetX, offsetY, width, height, isRoot );
        VehicleScoringInfo currentcarinfos = gameData.getScoringInfo().getViewedVehicleScoringInfo();
        int rowHeight = height / 2;
            
        texture.clear( imgName.getTexture(), offsetX + width * 4/100, offsetY, false, null );
        texture.clear( imgTeam.getTexture(), offsetX, offsetY + height / 2, false, null );
        
        if( currentcarinfos.getFastestLaptime() != null && currentcarinfos.getFastestLaptime().getLapTime() > 0 )
        {  
            if(currentcarinfos.getPlace( false ) > 1)
            { 
                texture.clear( imgTime.getTexture(), offsetX + width - 2*imgTime.getTexture().getWidth() + width * 5/100, offsetY, false, null );
                texture.clear( imgTime.getTexture(), offsetX + width - 2*imgTime.getTexture().getWidth() + width * 2/100, offsetY + rowHeight, false, null );
            }
            else
                texture.clear( imgTime.getTexture(), offsetX + width - 2*imgTime.getTexture().getWidth() + width * 2/100, offsetY + rowHeight, false, null );

    
            if(currentcarinfos.getPlace( false ) == 1)
                imgPosFirst.getImage().getScaledTextureImage( width*18/100, height, texPos, isEditorMode );
            else
                if(currentcarinfos.getPlace( false ) <= knockout.getValue())
                    imgPos.getImage().getScaledTextureImage( width*18/100, height, texPos, isEditorMode );
                else
                    imgPosOut.getImage().getScaledTextureImage( width*18/100, height, texPos, isEditorMode );
                
            texture.drawImage( texPos, offsetX + imgName.getTexture().getWidth() - width*4/200, offsetY, false, null );
  
                
           
        }   
        if(currentcarinfos.isPlayer() && currentcarinfos.getFastestLaptime() == null )
            texture.drawImage( texTime, offsetX + imgName.getTexture().getWidth() - width*4/200, offsetY + rowHeight, false, null );
        else
            if(currentcarinfos.isPlayer())
                texture.drawImage( texTime, offsetX + width - imgTime.getTexture().getWidth(), offsetY + rowHeight, false, null );
        
    }
    
    
    @Override
    protected void drawWidget( Clock clock, boolean needsCompleteRedraw, LiveGameData gameData, boolean isEditorMode, TextureImage2D texture, int offsetX, int offsetY, int width, int height )
    {
        ScoringInfo scoringInfo = gameData.getScoringInfo();
    	sessionTime.update(scoringInfo.getSessionTime());
    	
    	if ( needsCompleteRedraw )
        {
    	    VehicleScoringInfo currentcarinfos = gameData.getScoringInfo().getViewedVehicleScoringInfo();
            
        	name.update( PrunnWidgetSetf1_2011.ShortName( currentcarinfos.getDriverNameShort() ) );
            pos.update( NumberUtil.formatFloat( currentcarinfos.getPlace(false), 0, true));
            
            if(currentcarinfos.getVehicleInfo() != null)
                team.update( PrunnWidgetSetf1_2011.generateShortTeamNames( currentcarinfos.getVehicleInfo().getTeamName(), gameData.getFileSystem().getConfigFolder() ));
            else
                team.update( currentcarinfos.getVehicleClass()); 
                
        	
            if( currentcarinfos.getFastestLaptime() != null && currentcarinfos.getFastestLaptime().getLapTime() > 0 )
            {
                if(currentcarinfos.getPlace( false ) > 1)
                { 
                    time.update( TimingUtil.getTimeAsLaptimeString(currentcarinfos.getBestLapTime() ));
                    gap.update( "+ " +  TimingUtil.getTimeAsLaptimeString( currentcarinfos.getBestLapTime() - gameData.getScoringInfo().getLeadersVehicleScoringInfo().getBestLapTime() ));
                }
                else
                {
                    time.update("");
                    gap.update( TimingUtil.getTimeAsLaptimeString(currentcarinfos.getBestLapTime()));
                }
                    
            }
            else
            {
                time.update("");
                gap.update("");
            }
            if( currentcarinfos.getFastestLaptime() != null && currentcarinfos.getFastestLaptime().getLapTime() > 0 )
                dsPos.draw( offsetX, offsetY, pos.getValue(),( currentcarinfos.getPlace(false) <= knockout.getValue() ) ? fontColor2.getColor() : KnockoutFontColor.getColor(), texture );
            //if(( clock.c() && name.hasChanged()) || isEditorMode )
            if( uppercasename.getValue() )
                dsName.draw( offsetX, offsetY, name.getValue().toUpperCase(), texture );
            else    
                dsName.draw( offsetX, offsetY, name.getValue(), texture );
            
            //if(( clock.c() && team.hasChanged()) || isEditorMode )
                dsTeam.draw( offsetX, offsetY, team.getValue(), texture );
            //if(( clock.c() && time.hasChanged()) || isEditorMode ) 
                dsTime.draw( offsetX, offsetY, time.getValue(), texture);
            //if(( clock.c() && gap.hasChanged()) || isEditorMode ), TireFontColor.getColor()
                dsGap.draw( offsetX, offsetY, gap.getValue(), texture );
            
            if(currentcarinfos.isPlayer())
            {    
                String tireCompound = gameData.getSetup().getGeneral().getRearTireCompound().getName().toUpperCase();    
                if(tireCompound.toUpperCase().equals( SSoft.getValue().toUpperCase() ))
                    tireCompound = "S-SOFT";
                else if(tireCompound.toUpperCase().equals( Med.getValue().toUpperCase() ))
                    tireCompound = "MED";
                else if(tireCompound.toUpperCase().equals( Hard.getValue().toUpperCase() ))
                    tireCompound = "HARD";
                else if(tireCompound.toUpperCase().equals( Inter.getValue().toUpperCase() ))
                    tireCompound = "INTER";
                    
                if(tireCompound.equals( "S-SOFT" ))
                    TireDrawnFontColor = TireFontColorSSoft;
                else if(tireCompound.equals( "MED" ))
                    TireDrawnFontColor = TireFontColorMedium;
                else if(tireCompound.equals( "HARD" ) )
                    TireDrawnFontColor = TireFontColorHard;
                else if(tireCompound.equals( "INTER" ))
                    TireDrawnFontColor = TireFontColorInter;
                else
                    TireDrawnFontColor = TireFontColor;   
                    
                if(gap.getValue().equals( "" ))
                    dsTire2.draw( offsetX, offsetY, tireCompound, TireDrawnFontColor.getColor(), texture );
                else
                    dsTire.draw( offsetX, offsetY, tireCompound, TireDrawnFontColor.getColor(), texture );
            }     
        }
         
    }
    
    
    @Override
    public void saveProperties( PropertyWriter writer ) throws IOException
    {
        super.saveProperties( writer );
        writer.writeProperty( f1_2011Font, "" );
        writer.writeProperty( posFont, "" );
        writer.writeProperty( fontColor2, "" );
        writer.writeProperty( TireFontColor, "" );
        writer.writeProperty( KnockoutFontColor, "" );
        writer.writeProperty(visibleTime, "");
        writer.writeProperty( knockout, "" );
        writer.writeProperty( fontyoffset, "" );
        writer.writeProperty( uppercasename, "" );
        writer.writeProperty( TireFontColorSSoft, "" );
        writer.writeProperty( TireFontColorMedium, "" );
        writer.writeProperty( TireFontColorHard, "" );
        writer.writeProperty( TireFontColorInter, "" );
        writer.writeProperty( SSoft, "" );
        writer.writeProperty( Med, "" );
        writer.writeProperty( Hard, "" );
        writer.writeProperty( Inter, "" );
        //writer.writeProperty( TireFontColorWet, "" );
        
    }
    
    @Override
    public void loadProperty( PropertyLoader loader )
    {
        super.loadProperty( loader );
        if ( loader.loadProperty( f1_2011Font ) );
        else if ( loader.loadProperty( posFont ) );
        else if ( loader.loadProperty( fontColor2 ) );
        else if ( loader.loadProperty( TireFontColor ) );
        else if ( loader.loadProperty( KnockoutFontColor ) );
        else if( loader.loadProperty(visibleTime));
        else if ( loader.loadProperty( knockout ) );
        else if ( loader.loadProperty( fontyoffset ) );
        else if ( loader.loadProperty( uppercasename ) );
        else if ( loader.loadProperty( TireFontColorSSoft ) );
        else if ( loader.loadProperty( TireFontColorMedium ) );
        else if ( loader.loadProperty( TireFontColorHard ) );
        else if ( loader.loadProperty( TireFontColorInter ) );
        else if ( loader.loadProperty( SSoft ) );
        else if ( loader.loadProperty( Med ) );
        else if ( loader.loadProperty( Hard ) );
        else if ( loader.loadProperty( Inter ) );
        
    }
    
    @Override
    public void getProperties( PropertiesContainer propsCont, boolean forceAll )
    {
        super.getProperties( propsCont, forceAll );
        
        propsCont.addGroup( "Colors" );
        propsCont.addProperty( f1_2011Font );
        propsCont.addProperty( posFont );
        propsCont.addProperty( fontColor2 );
        propsCont.addProperty( TireFontColor );
        propsCont.addProperty( TireFontColorSSoft );
        propsCont.addProperty( SSoft );
        propsCont.addProperty( TireFontColorMedium );
        propsCont.addProperty( Med );
        propsCont.addProperty( TireFontColorHard );
        propsCont.addProperty( Hard );
        propsCont.addProperty( TireFontColorInter );
        propsCont.addProperty( Inter );
        propsCont.addProperty( KnockoutFontColor );
        propsCont.addProperty(visibleTime);
        propsCont.addProperty( knockout );
        propsCont.addProperty( fontyoffset );
        propsCont.addProperty( uppercasename );
        
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
    
    public QualifInfoWidget()
    {
        super( PrunnWidgetSetf1_2011.INSTANCE, PrunnWidgetSetf1_2011.WIDGET_PACKAGE_F1_2011, 40.0f, 7.0f );
        visibleTime = new DelayProperty("visibleTime", net.ctdp.rfdynhud.properties.DelayProperty.DisplayUnits.SECONDS, 6);
        visibleEnd = 0x8000000000000000L;
        getBackgroundProperty().setColorValue( "#00000000" );
        getFontProperty().setFont( PrunnWidgetSetf1_2011.F1_2011_FONT_NAME );
    }
    
}
