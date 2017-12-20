package de.itwerkstatt.ogcalendar;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.Year;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A JPanel that represents a year chooser. User can chooser by selecting by
 * spin buttons or just typing the year. Register a propertychange listener with
 * propertyname "yearSelected" to receive the current Month value selected by
 * the user
 *
 * @author Dominik Sust
 * @creation 19.12.2017 14:49:14
 */
public class OGYearChooser extends JPanel
{

    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );

    private JSpinner spinner;

    private Year currentYear;

    /**
     * Constructor which sets current Year to the current Date
     */
    public OGYearChooser()
    {
        this( Year.from( LocalDate.now() ) );
    }

    /**
     * Constructor which sets current Year to the given value
     *
     * @param current
     */
    public OGYearChooser( Year current )
    {
        currentYear = current;
        initComponent();
    }

    /**
     * Sets the current year which is displayed
     *
     * @param year
     */
    public void setCurrentYear( Year year )
    {
        currentYear = year;
        spinner.setValue( currentYear.getValue() );
    }

    /**
     * Gets the current year that is displayed
     *
     * @return
     */
    public Year getCurrentYear()
    {
        return currentYear;
    }

    /**
     * Register the following PropertyChangeListeners to receive the values:
     * yearSelected = get the selected Year
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
     * yearSelected = get the selected Year
     * @param listener 
     */
    @Override
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        pcs.addPropertyChangeListener( listener );
    }

    /**
     * Builds the component and register the listeners
     */
    private void initComponent()
    {
        spinner = new JSpinner();
        spinner.setEditor( new JSpinner.NumberEditor( spinner, "#" ) );
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
        spinner.setModel( spinnerNumberModel );
        spinnerNumberModel.setMinimum( Year.MIN_VALUE );
        spinnerNumberModel.setMaximum( Year.MAX_VALUE );
        spinnerNumberModel.setStepSize( 1 );
        spinnerNumberModel.setValue( currentYear.getValue() );

        spinner.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                int newSpinnerValue = (int) spinner.getModel().getValue();

                //User pressed up
                if ( newSpinnerValue > currentYear.getValue() )
                {
                    Year temp = currentYear;
                    currentYear = currentYear.plusYears( 1 );
                    pcs.firePropertyChange( "yearSelected", temp, currentYear );

                }
                //User pressed down
                else if ( newSpinnerValue < currentYear.getValue() )
                {
                    Year temp = currentYear;
                    currentYear = currentYear.minusYears( 1 );
                    pcs.firePropertyChange( "yearSelected", temp, currentYear );
                }
            }
        } );
        add( spinner );
    }
}
