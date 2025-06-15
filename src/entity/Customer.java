package entity;

import java.util.Objects;

public class Customer {
    private final int id;
    private String name;

    public Customer(int id, String name){
        this.id=id;
        this.name=name;
    }

    public int getId()           { return id; }
    public String getName()      { return name; }
    public void setName(String n){ name=n; }

    @Override public String toString(){ return id + "|" + name; }
    public static Customer fromString(String s){
        var p=s.split("\\|"); return new Customer(Integer.parseInt(p[0]), p[1]);
    }
    @Override public boolean equals(Object o){ return o instanceof Customer c && c.id==id; }
    @Override public int hashCode(){ return Objects.hash(id); }
}