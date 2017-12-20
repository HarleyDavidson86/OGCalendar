package de.itwerkstatt.ogcalendar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.BOTH;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * A JPanel that represents a hole month in a sheet Register a propertychange
 * listener with propertyname "dateSelected" to receive the current LocalDate
 * value selected by the user
 *
 * @author Dominik Sust
 * @creation 19.12.2017 14:49:58
 */
public class OGDayChooser extends JPanel
{

    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );

    private String MONDAY = "Mo";
    private String TUESDAY = "Di";
    private String WEDNESDAY = "Mi";
    private String THURSDAY = "Do";
    private String FRIDAY = "Fr";
    private String SATURDAY = "Sa";
    private String SUNDAY = "So";
    private YearMonth currentYearAndMonth;
    private GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Constructor which sets current Year and Month to 
     * the current Date
     */
    public OGDayChooser()
    {
        this(YearMonth.now());
    }
    
    /**
     * Constructor which sets current Year and Month to 
     * the given value
     * @param current 
     */
    public OGDayChooser(YearMonth current)
    {
        setLayout( new GridBagLayout() );
        currentYearAndMonth = current;
        setCurrentYearAndMonth( currentYearAndMonth );
    }

    /**
     * Displays the new year and month in this sheet
     * @param yearAndMonth
     */
    public void setCurrentYearAndMonth( YearMonth yearAndMonth )
    {
        System.out.println( yearAndMonth );
        removeAll();
        currentYearAndMonth = yearAndMonth;
        gbc.weightx = gbc.weighty = 1.0;
        createHeadlines();
        fillDayButtons();
        repaint();
        revalidate();
    }
    
    /**
     * Sets the caption Strings for weekdays
     * @param day
     * @param value 
     */
    public void setHeadline( DayOfWeek day, String value )
    {
        switch ( day )
        {
            case MONDAY:
                MONDAY = value;
                break;
            case TUESDAY:
                TUESDAY = value;
                break;
            case WEDNESDAY:
                WEDNESDAY = value;
                break;
            case THURSDAY:
                THURSDAY = value;
                break;
            case FRIDAY:
                FRIDAY = value;
                break;
            case SATURDAY:
                SATURDAY = value;
                break;
            case SUNDAY:
                SUNDAY = value;
                break;
        }
    }

    /**
     * Register the following PropertyChangeListeners to receive the values:
     * dateSelected = get the selected LocalDate 
     * @param propertyName 
     * @param listener 
     */
    @Override
    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        pcs.addPropertyChangeListener( propertyName, listener );
    }

    /**
     * Register the following PropertyChangeListeners to receive the values:
     * dateSelected = get the selected LocalDate 
     * @param listener 
     */
    @Override
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        pcs.addPropertyChangeListener( listener );
    }    

    /**
     * Creates headlines Update OGCalendarWeekDayCaption to customize these
     * headlines
     */
    private void createHeadlines()
    {
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        for ( int i = 1; i < 8; i++ )
        {
            gbc.gridx = i;
            JLabel label = new JLabel( getHeadline( DayOfWeek.of( i ) ) );
            label.setHorizontalAlignment( SwingConstants.CENTER );
            this.add( label, gbc );
        }
    }

    /**
     * Creates all the Buttons with actioncommands
     */
    private void fillDayButtons()
    {
        gbc.gridy = 2;
        gbc.fill = BOTH;
        gbc.insets = new Insets( 1, 1, 1, 1 );
        for ( int i = 1; i < currentYearAndMonth.lengthOfMonth() + 1; i++ )
        {
            DayOfWeek dayOfWeek = currentYearAndMonth.atDay( i ).getDayOfWeek();
            gbc.gridx = dayOfWeek.getValue();

            if ( dayOfWeek == DayOfWeek.MONDAY && i != 1 )
            {
                gbc.gridy++;
            }
            JLabel b = new JLabel( String.valueOf( i ) );
            b.addMouseListener( new MouseAdapter()
            {
                @Override
                public void mouseClicked( MouseEvent e )
                {
                    JLabel source = (JLabel) e.getSource();
                    pcs.firePropertyChange( "dateSelected", null, currentYearAndMonth.atDay( Integer.valueOf( source.getText() ) ) );
                }

            } );
            b.setHorizontalAlignment( SwingConstants.CENTER );
            b.setBorder( new BevelBorder( BevelBorder.RAISED ) );
            if ( currentYearAndMonth.atDay( i ).isEqual( LocalDate.now() ) )
            {
                b.setForeground( Color.red );
            }
            this.add( b, gbc );
        }
    }

    /**
     * Getter method for the Headline Strings
     * @param day
     * @return String
     */
    private String getHeadline( DayOfWeek day )
    {
        switch ( day )
        {
            case MONDAY:
                return MONDAY;
            case TUESDAY:
                return TUESDAY;
            case WEDNESDAY:
                return WEDNESDAY;
            case THURSDAY:
                return THURSDAY;
            case FRIDAY:
                return FRIDAY;
            case SATURDAY:
                return SATURDAY;
            case SUNDAY:
                return SUNDAY;
            default:
                return "UNKNOWN";
        }
    }
}
