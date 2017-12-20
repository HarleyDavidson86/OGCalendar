package de.itwerkstatt.ogcalendar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import javax.swing.JPanel;

/**
 * This components allows the user to choose a year, a month and a day.
 * Register the following PropertyChangeListeners to receive the values:
 * dateSelected = get the selected LocalDate
 * monthSelected = get the selected Month
 * yearSelected = get the selected Year
 * @author Dominik Sust
 * @creation 20.12.2017 07:49:07
 */
public class OGCalendar extends JPanel
{

    private final OGMonthChooser monthChooser;
    private final OGYearChooser yearChooser;
    private final OGDayChooser dayChooser;

    /**
     * Constructor for displaying the current year and month
     */
    public OGCalendar()
    {
        this( YearMonth.now() );
    }

    /**
     * Constructor which allows you to set the desired year and month
     * @param yearMonth 
     */
    public OGCalendar( YearMonth yearMonth )
    {
        super();
        monthChooser = new OGMonthChooser( yearMonth.getMonth() );
        yearChooser = new OGYearChooser( Year.from( yearMonth ) );
        dayChooser = new OGDayChooser( yearMonth );
        init();

        //Listener registration
        monthChooser.addPropertyChangeListener( "monthSelected", (e) -> monthChanged( e ) );
        yearChooser.addPropertyChangeListener( "yearSelected", (e) -> yearChanged( e ) );
    }
    
    /**
     * Setter for the current Date to be displayed by the component
     * @param newYearAndMonth 
     */
    public void setCurrentDate(YearMonth newYearAndMonth)
    {
        monthChooser.setCurrentMonth( newYearAndMonth.getMonth() );
        yearChooser.setCurrentYear( Year.from( newYearAndMonth ));
        dayChooser.setCurrentYearAndMonth( newYearAndMonth );
    }
    
/**
     * Register the following PropertyChangeListeners to receive the values:
     * dateSelected = get the selected LocalDate 
     * monthSelected = get the selected Month 
     * yearSelected = get the selected Year
     * @param propertyName
     * @param listener 
     */
    @Override
    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        dayChooser.addPropertyChangeListener( propertyName, listener );
        monthChooser.addPropertyChangeListener( propertyName, listener );
        yearChooser.addPropertyChangeListener( propertyName, listener );
    }

    /**
     * Register the following PropertyChangeListeners to receive the values:
     * dateSelected = get the selected LocalDate 
     * monthSelected = get the selected Month 
     * yearSelected = get the selected Year
     * @param listener 
     */
    @Override
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        dayChooser.addPropertyChangeListener( listener );
        monthChooser.addPropertyChangeListener( listener );
        yearChooser.addPropertyChangeListener( listener );
    }    

    /**
     * Inits the form
     */
    private void init()
    {
        setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        add( monthChooser, gbc );
        gbc.gridx = 1;
        add( yearChooser, gbc );
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        add( dayChooser, gbc );
    }
    
    /**
     * Is called by the PropertyChangeListener of the MonthChooser component
     * if the old value is december and the new value is january, the value of the 
     * year is increased (and vice versa)
     * @param e 
     */
    private void monthChanged( PropertyChangeEvent e )
    {
        if (e.getOldValue() == Month.DECEMBER && e.getNewValue() == Month.JANUARY)
        {
            yearChooser.setCurrentYear( yearChooser.getCurrentYear().plusYears( 1));
        }
        else if (e.getOldValue() == Month.JANUARY && e.getNewValue() == Month.DECEMBER)
        {
            yearChooser.setCurrentYear( yearChooser.getCurrentYear().minusYears( 1));
        }
        dayChooser.setCurrentYearAndMonth( YearMonth.of( yearChooser.getCurrentYear().getValue(), monthChooser.getCurrentMonth()));
    }

    /**
     * Is called by the PropertyChangeListener of the YearChooser component
     * and sets the daychoosercomponent
     * @param e 
     */
    private void yearChanged( PropertyChangeEvent e )
    {
        dayChooser.setCurrentYearAndMonth( YearMonth.of( yearChooser.getCurrentYear().getValue(), monthChooser.getCurrentMonth()));
    }
}
