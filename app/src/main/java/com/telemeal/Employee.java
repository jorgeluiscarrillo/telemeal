package com.telemeal;

/**
 * Created by Bryan on 3/15/2018.
 */

public class Employee {
    /**Integer value that represents the employee's id*/
    private int id;

    /**The name of the employee*/
    private String name;

    /**The employee's current job*/
    private String position;

    /**Determines whether the employee has admin privileges or not*/
    private boolean privilege;

    /**
     * Required empty constructor for the database
     */
    public Employee(){}

    /**
     * Initializes the properties inside an employee object
     * @param i employee's id
     * @param n employee's name
     * @param pos employee's position
     * @param priv employee's privilege
     */
    public Employee(int i, String n, String pos, boolean priv)
    {
        id = i;
        name = n;
        position = pos;
        privilege = priv;
    }

    /**
     * Gives the employee a new id
     * @param i the new id
     */
    public void setID(int i)
    {
        id = i;
    }

    /**
     * Gives the employee a new name
     * @param n the new name
     */
    public void setName(String n)
    {
        name=n;
    }

    /**
     * Gives the employee a new position
     * @param pos the new position
     */
    public void setPosition(String pos)
    {
        position = pos;
    }

    /**
     * Gives the employee a new privilege
     * @param priv the new privilege
     */
    public void setPrivilege(boolean priv)
    {
        privilege = priv;
    }

    /**
     * Gets the employee's current id
     * @return the current id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the employee's current name
     * @return the current name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the employee's current position
     * @return the current position
     */
    public String getPosition()
    {
        return position;
    }

    /**
     * Gets the employees
     * @return the current privilege
     */
    public boolean getPrivilege()
    {
        return privilege;
    }

    /**
     * Prints the employee's name and position
     * @return a string containing the employee's name and position
     */
    @Override
    public String toString(){
        return name + ": " + position;
    }

}
