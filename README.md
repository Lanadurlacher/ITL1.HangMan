# Hangman (Swing, IntelliJ GUI Designer)
Author: Lana Durlacher

LBS Eibiswald | 2aAPC

# Beschreibung des Programms
Das Programm Hangman ist eine Umsetzung des bekannten Wörter-Rate-Spiels in Java unter Verwendung der Swing-Bibliothek und des IntelliJ GUI Designers.
Der Spieler muss ein verstecktes Wort erraten, indem er einzelne Buchstaben auswählt. Jeder falsche Buchstabe führt dazu, dass ein Teil der Hangman-Grafik eingeblendet wird. Das Spiel endet, wenn das Wort vollständig erraten oder die maximale Anzahl an Fehlversuchen erreicht ist.

Das Projekt dient zur Übung von grafischen Benutzeroberflächen (GUI), Ereignissteuerung (Event-Handling) und Zustandsverwaltung in Java-Swing.
<br>
<br>

# Design
Die Benutzeroberfläche wurde mit dem IntelliJ GUI Designer erstellt.
Zentrale Elemente sind:

Ein JLabel, das das aktuelle Hangman-Bild anzeigt
Eine Textanzeige für das zu erratende Wort (mit Platzhaltern für unbekannte Buchstaben)
Buttons für die Buchstaben A–Z
Ein "Restart"-Button zum Neustarten des Spiels
Das Layout ist schlicht, übersichtlich und in neutralen Farben gehalten. Das Bildfeld wurde transparent gestaltet, damit kein störender Hintergrund sichtbar ist.

<img width="752" height="554" alt="image" src="https://github.com/user-attachments/assets/3a4f6555-ffd4-45ac-bd06-780fa157e382" />

<img width="1008" height="723" alt="image" src="https://github.com/user-attachments/assets/1bb5c95f-98a5-46f2-ad92-399c2162858f" />


<br>
<br>

# Features

- Voll funktionsfähiges Hangman-Spiel mit grafischer Darstellung
- Dynamische Aktualisierung des Wortes und der Fehlversuche
- Zählung der falschen Buchstaben und Anzeige des entsprechenden Hangman-Bildes
- Neustart-Funktion über eigenen Button
- Saubere Trennung von Spiellogik und GUI-Elementen
- Nutzung von ActionListenern für die Tastensteuerung
<br>
<br>
