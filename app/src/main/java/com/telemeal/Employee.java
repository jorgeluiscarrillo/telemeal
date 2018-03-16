package com.telemeal;

/**
 * Created by Bryan on 3/15/2018.
 */

public class Employee {
    private int id;
    private String name;
    private String position;
    private boolean privilege;

    public Employee(int i, String n, String pos, boolean priv)
    {
        id = i;
        name = n;
        position = pos;
        privilege = priv;
    }

    public void setID(int i)
    {
        id = i;
    }

    public void setName(String n)
    {
        name=n;
    }

    public void setPosition(String pos)
    {
        position = pos;
    }

    public void setPrivilege(boolean priv)
    {
        privilege = priv;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPosition()
    {
        return position;
    }

    public boolean getPrivilege()
    {
        return privilege;
    }

}
