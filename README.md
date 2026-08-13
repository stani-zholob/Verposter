# Voraussetzungen
JDK 21, Maven 3, Linux System und Internetverbindung für Tomcat

# Vorbereitung
Datenbank muss angelegt werden mit DatabaseInit oder ggf. kontrollieren ob db/verposter.db existiert

# Starten
bash run.bash auf Linux System starten ggf. mvn clean

# Aufrufen
Der sinnvollste Startpunkt zum testen der Anwendung ist
http://localhost:8080/Verposter/login
es exisitieren folgende Demo Logins: admin/admin, lukkas/lukkas, stani/stani, akim/akim
ansonsten kann auch alles mögliche registriert werden

# Testdurchlauf der Anwedung
Empfehlen können wir diesen Ablauf zum testen der Funktionalitäten der Anwendung
1. Login von 3 Nutzern in drei verschiedenen Browsern. /Verposter/login (ggf. Registrieren testen /Verposter/register)
2. Erstellen eines Raumes. /Verposter/lobby
3. Beitreten aller Nutzer in diesem Raum.
4. Alle Nutzer Bereit klicken (ggf. Verlassen testen). /Verposter/createroom
5. Mit allen drei Nutzern Nachrichten senden. /Verposter/game
6. 1. Alle Nicht-Verposter Spieler einen Spieler abstimmen mit den Buttons unten.
6. 2. Timer von 2 Minunten ablaufen lassen.
7. Auflösung des Spiels und zurück zur Lobby.