package de.hsos.vs.util;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

//
//
/**
 * @author Stanislav
 * Quelle: Buch oechsle 2022 parallele und verteilte anwendungen in java
 * Zaehler für Sitzungen
 */
@WebListener
public class SessionListenerExample implements HttpSessionListener
{
    private int numberOfSessions;
    public void sessionCreated(HttpSessionEvent event)
    {
        numberOfSessions++;
        System.out.println("Anzahl der Sessions: " + numberOfSessions);
    }
    public void sessionDestroyed(HttpSessionEvent event)
    {
        numberOfSessions--;
        System.out.println("Anzahl der Sessions: " + numberOfSessions);
    }
}