package com.example.theperegrinefund;

import java.util.Date;

public class Message {

    private int idMessage;
    private Date dateCommencement;
    private Date dateSignalement;
    private String contenuCode;
    private String pointRepere;
    private String description;
    private double surfaceApproximative;
    private String direction;
    private String elementsVisibles;
    private String degats;
    private UserApp user;

    public Message(int idMessage, Date dateCommencement, Date dateSignalement, String contenuCode,
                   String pointRepere, String description, double surfaceApproximative,
                   String direction, String elementsVisibles, String degats,
                   UserApp user) {

                    this.idMessage = idMessage;
        this.dateCommencement = dateCommencement;
        this.dateSignalement = dateSignalement;
        this.contenuCode = contenuCode;
        this.pointRepere = pointRepere;
        this.description = description;
        this.surfaceApproximative = surfaceApproximative;
        this.direction = direction;
        this.elementsVisibles = elementsVisibles;
        this.degats = degats;
        this.user = user;
    }

    public int getIdMessage() { 
        return idMessage; 
    }
    public void setIdMessage(int idMessage) { 
        this.idMessage = idMessage; 
    }

    public Date getDateCommencement() { 
        return dateCommencement; 
    }
    public void setDateCommencement(Date dateCommencement) { 
        this.dateCommencement = dateCommencement; 
    }

    public Date getDateSignalement() { 
        return dateSignalement; 
    }
    public void setDateSignalement(Date dateSignalement) { 
        this.dateSignalement = dateSignalement; 
    }

    public String getContenuCode() { 
        return contenuCode; 
    }
    public void setContenuCode(String contenuCode) { 
        this.contenuCode = contenuCode; 
    }

    public String getPointRepere() { 
        return pointRepere; 
    }
    public void setPointRepere(String pointRepere) { 
        this.pointRepere = pointRepere; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    public double getSurfaceApproximative() { 
        return surfaceApproximative; 
    }
    public void setSurfaceApproximative(double surfaceApproximative) { 
        this.surfaceApproximative = surfaceApproximative; 
    }

    public String getDirection() { 
        return direction; 
    }
    public void setDirection(String direction) { 
        this.direction = direction; 
    }

    public String getElementsVisibles() { 
        return elementsVisibles; 
    }
    public void setElementsVisibles(String elementsVisibles) { 
        this.elementsVisibles = elementsVisibles; 
    }

    public String getDegats() { 
        return degats; 
    }
    public void setDegats(String degats) { 
        this.degats = degats; 
    }

    public UserApp getUser() { 
        return user; 
    }
    public void setUser(UserApp user) { 
        this.user = user; 
    }
}
