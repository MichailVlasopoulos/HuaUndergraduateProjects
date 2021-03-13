# CityWeatherProject (Waiting for a cooler name)


# Dependencies

- Java Servlet API 3.1
- Java Servlet JSP 2.3.1
- JUnit 4.1

( Ο κ.Βιόλος μπορεί να επιλέξει άλλες εκδόσεις ή και άλλα frameworks αλλά αυτό αντιμετωπίζεται εύκολα μέσω του Maven )

## IntelliJ GitHub Setup
```
Αφού γίνεις collaborator:
Στο IntelliJ : VCS -> Get From Version Control
Θα κάνεις login στο GitHub σου και μετά θα κάνεις clone από εκεί
```

## Tomcat 8.5.51 Configuration

Θα κατεβάσεις το tomcat από εδώ: http://apache.forthnet.gr/tomcat/tomcat-8/v8.5.51/bin/apache-tomcat-8.5.51.tar.gz

Βάλτο όπου θες δεν έχει σημασία. Πρέπει να πειράξεις τους users του tomcat ώστε το plugin του maven να έχει τα δικαιώματα να κάνει deploy τον servlet:
```
> gedit apache-tomcat-8.5.51/conf/tomcat-users.xml
```

Και βάζεις αυτό πριν το  < /tomcat-users> :

```
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="admin" password="password" roles="manager-gui,manager-script"/>
```
O tomcat ξεκινάει μέσω του script:
```
 > apache-tomcat-8.5.51/bin/startup.sh
```
και τερματίζει μέσω του script:
```
 > apache-tomcat-8.5.51/bin/shutdown.sh
```
Μόλις προσθέσεις τον user ξεκίνα τον. Μετά πρέπει να πας στον browser σου και να βάλεις την διεύθυνση:

```
 localhost:8080
```
και να βλέπεις τον γάτο χαχαχαχα. Κράτα το ανοιχτό όσο θα δουλεύεις στο Intellij.

# Executing Maven Goal

Αφού έχεις κάνει clone το repository κανονικά το maven πρέπει να αρχίσει να τα σετάρει όλα από μόνο του. Μόλις τελειώσει τέρμα δεξιά στην οθόνη θα έχει ένα tab που θα λέει maven. Θα το πατήσεις, θα ανοίξει και θα πατήσεις το m κουμπί. 

Μετά θα τρέξεις αυτήν την εντολή: 

```
 > mvn tomcat7:deploy
```
Αν όλα πάνε καλά (κάνε σταυρό χαχαχα) αν πας στην διεύθυνση: 

```
 localhost:8080/CityWeatherSite/home
```
τότε θα δεις το Hello World!
