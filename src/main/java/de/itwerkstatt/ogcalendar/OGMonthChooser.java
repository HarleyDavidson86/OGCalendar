package de.itwerkstatt.ogcalendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.Month;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A JPanel that represents a month chooser. User can chooser either by
 * selecting in a combobox or spin buttons. Register a propertychange listener
 * with propertyname "monthSelected" to receive the current Month value selected
 * by the user
 *
 * @author Dominik Sust
 * @creation 19.12.2017 14:49:26
 */
public class OGMonthChooser extends JPanel
{

    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    private JComboBox<String> combobox;

    private Month currentMonth;

    private String[] months = new String[]
    {
        "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"
    };

    private int oldSpinnerValue;

    /**
     * Constructor which sets current Month to the current
     */
    public OGMonthChooser()
    {
        this( Month.from( LocalDate.now() ) );
    }

    /**
     * Constructor which sets current Month to the given value
     *
     * @param current
     */
    public OGMonthChooser( Month current )
    {
        currentMonth = current;
        buildComponent();
        updateCombobox();
    }

    /**
     * Setter for the month values in the combobox If the parameter is not an
     * array with 12 values nothing will change
     *
     * @param months an array with 12 fields
     */
    public void setMonths( String[] values )
    {
        if ( values.length == 12 )
        {
            months = values;
        }
    }

    /**
     * Getter for the current month displayed
     *
     * @return Month
     */
    public Month getCurrentMonth()
    {
        return currentMonth;
    }

    /**
     * Sets the current month to the given value
     *
     * @param currentMonth
     */
    public void setCurrentMonth( Month currentMonth )
    {
        this.currentMonth = currentMonth;
        updateCombobox();
    }

    /**
     * Register the following PropertyChangeListeners to receive the values:
     * monthSelected = get the selected Month 
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
     * monthSelected = get the selected Month 
     * @param listener 
     */
    @Override
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        pcs.addPropertyChangeListener( listener );
    }

    /**
     * Method which places all the components to the Panel and register its
     * listeners
     */
    private void buildComponent()
    {
        setLayout( new BorderLayout() );
        combobox = new JComboBox<>( months );
        combobox.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        JSpinner spinner = new JSpinner()
        {
            private static final long serialVersionUID = 1L;

            private JTextField textField = new JTextField();

            public Dimension getPreferredSize()
            {
                Dimension size = super.getPreferredSize();
                return new Dimension( size.width, textField
                        .getPreferredSize().height );
            }
        };
        spinner.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                int newSpinnerValue = (int) spinner.getModel().getValue();

                //User pressed up
                if ( newSpinnerValue > oldSpinnerValue )
                {
                    Month temp = currentMonth;
                    currentMonth = currentMonth.plus( 1 );
                    pcs.firePropertyChange( "monthSelected", temp, currentMonth );
                }
                //User pressed down
                else if ( newSpinnerValue < oldSpinnerValue )
                {
                    Month temp = currentMonth;
                    currentMonth = currentMonth.minus( 1 );
                    pcs.firePropertyChange( "monthSelected", temp, currentMonth );
                }
                oldSpinnerValue = newSpinnerValue;
                updateCombobox();
            }
        } );
        combobox.addItemListener( new ItemListener()
        {
            @Override
            public void itemStateChanged( ItemEvent event )
            {
                if ( event.getStateChange() == ItemEvent.SELECTED )
                {
                    Month temp = currentMonth;
                    currentMonth = Month.of( combobox.getSelectedIndex() + 1 );
                    pcs.firePropertyChange( "monthSelected", temp, currentMonth );
                }
            }
        } );
        spinner.setEditor( combobox );
        oldSpinnerValue = (int) spinner.getModel().getValue();
        add( spinner, BorderLayout.CENTER );
    }

    /**
     * Updates the combobox to the new month value
     */
    private void updateCombobox()
    {
        combobox.setSelectedIndex( currentMonth.getValue() - 1 );
    }
}
