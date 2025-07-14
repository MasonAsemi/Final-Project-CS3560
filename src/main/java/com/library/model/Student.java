package com.library.model;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bronco_id", unique = true, nullable = false)
    private String broncoId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "degree")
    private String degree;
    
    public Student() {}
    
    public Student(String broncoId, String name, String address, String degree) {
        this.broncoId = broncoId;
        this.name = name;
        this.address = address;
        this.degree = degree;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getBroncoId() { return broncoId; }
    public void setBroncoId(String broncoId) { this.broncoId = broncoId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    
    @Override
    public String toString() {
        return name + " (" + broncoId + ")";
    }
}