PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;

CREATE TABLE Nutzer (
    Email VARCHAR COLLATE NOCASE NOT NULL PRIMARY KEY,
    Passwort VARCHAR NOT NULL,
    CHECK( (substr(Email, INSTR(Email,'.')+1) NOT GLOB '*[^A-Za-z]*') 
    AND (substr(Email,1, INSTR(Email,'@')-1) NOT GLOB '*[^A-Za-z0-9]*')
    AND (substr(Email, INSTR(Email,'@')+1, ((INSTR(Email,'.')-1) - (INSTR(Email,'@')))) NOT GLOB '*[^A-Za-z0-9]*') 
    AND Email LIKE '%_@%_.%_'),

    CHECK (LENGTH(Passwort) > 3 AND LENGTH(Passwort) < 10 AND Passwort GLOB '*[0-9]*[0-9]*' AND Passwort GLOB '*[A-Z]*' AND  Passwort NOT GLOB '*[aeiouAEIOU][13579]*' )
    
);

CREATE TABLE Kunde(
    Telefonnummer VARCHAR NOT NULL PRIMARY KEY,
    Email VARCHAR COLLATE NOCASE NOT NULL UNIQUE,
    FOREIGN KEY(Email) REFERENCES Nutzer(Email) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(Telefonnummer NOT GLOB '[^0-9]')
);

CREATE TABLE Projektleiter(
    Email VARCHAR  COLLATE NOCASE NOT NULL PRIMARY KEY,
    Gehalt DOUBLE NOT NULL,
    FOREIGN KEY(Email) REFERENCES Nutzer(Email)  ON UPDATE CASCADE ON DELETE CASCADE,
    
    CHECK(Gehalt GLOB '*.[0-9]' OR Gehalt GLOB '*.[0-9][0-9]' OR Gehalt GLOB '[0-9]' and Gehalt > 0)
);

CREATE TABLE Projekt(
    ProjektID INTEGER NOT NULL PRIMARY KEY,
    Projektname VARCHAR NOT NULL,
    Projektdeadline DATE NOT NULL,
    Telefonnummer VARCHAR NOT NULL,
    Email VARCHAR COLLATE NOCASE NOT NULL,
    FOREIGN KEY(Telefonnummer) REFERENCES Kunde(Telefonnummer) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(Email) REFERENCES Projektleiter(Email)  ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(ProjektID >= 0),
    CHECK(LENGTH(Projektname) > 0 AND Projektname NOT GLOB '*[^ -~]*'),
    CHECK(Projektdeadline IS DATE(Projektdeadline))
);


CREATE TABLE Bewertung(
    BewertungID INTEGER NOT NULL PRIMARY KEY,
    Bepunktung INTEGER NOT NULL,
    Telefonnummer VARCHAR NOT NULL,
    ProjektID INTEGER NOT NULL,
    Text TEXT,
    FOREIGN KEY(Telefonnummer) REFERENCES Kunde(Telefonnummer) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(ProjektID) REFERENCES Projekt(ProjektID) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(Bepunktung BETWEEN 1 AND 9),
    CHECK(Text NOT GLOB '*[^ -~]*'),
    CHECK(BewertungID >= 0)
);



CREATE TABLE Aufgaben(
    AufgabenID INTEGER NOT NULL PRIMARY KEY,
    Priorisierung VARCHAR NOT NULL,
    Aufgabendeadline DATE NOT NULL,
    Beschreibung TEXT NOT NULL,
    Status VARCHAR NOT NULL,
    ProjektID INTEGER NOT NULL,
    FOREIGN KEY(ProjektID) REFERENCES Projekt(ProjektID) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(Priorisierung NOT GLOB '*[^ -~]*' AND LENGTH(Priorisierung) > 0),
    CHECK(Status NOT GLOB '*[^ -~]*' AND LENGTH(Status) > 0),
    CHECK(Beschreibung NOT GLOB '*[^ -~]*' AND LENGTH(Beschreibung) > 0),
    CHECK(AufgabenID >= 0), 
    CHECK(Aufgabendeadline IS DATE(Aufgabendeadline))

);



CREATE TABLE Spezialist(
    Email VARCHAR COLLATE NOCASE NOT NULL PRIMARY KEY,
    Verfuegbarkeitsstatus VARCHAR NOT NULL,
    FOREIGN KEY(Email) REFERENCES Nutzer(Email) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(Verfuegbarkeitsstatus NOT GLOB '*[^ -~]*' AND LENGTH(Verfuegbarkeitsstatus) > 0)
    
);


CREATE TABLE Entwickler(
    Kuerzel VARCHAR NOT NULL PRIMARY KEY,
    Email VARCHAR COLLATE NOCASE NOT NULL UNIQUE,
    FOREIGN KEY(Email) REFERENCES Spezialist(Email) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(LENGTH(Kuerzel) = 8 AND substr(Kuerzel,1,5) NOT GLOB '*[^A-Za-z]*' AND substr(Kuerzel,6) NOT GLOB '*[^0-9]*')
);


CREATE TABLE Programmiersprache(
    ProgrammierspracheID INTEGER NOT NULL PRIMARY KEY,
    Name VARCHAR NOT NULL,
    CHECK(Name NOT GLOB '*[^ -~]*' AND LENGTH(Name) > 0)
);

CREATE TABLE fachliche_Kompetenz(
    fachlicheKompetenzID INTEGER NOT NULL PRIMARY KEY,
    Eigenschaft VARCHAR NOT NULL,
    CHECK(Eigenschaft NOT GLOB '*[^A-Za-z]*' AND LENGTH(Eigenschaft) > 0),
    CHECK(fachlicheKompetenzID >= 0)
);

CREATE TABLE Spezifikation(
    SpezifikationID INTEGER NOT NULL PRIMARY KEY,
    Designart VARCHAR NOT NULL,
    CHECK(Designart NOT GLOB '*[^ -~]*' AND LENGTH(Designart > 0)),
    CHECK(SpezifikationID >= 0)
    
);

CREATE TABLE Designer(
    Email VARCHAR COLLATE NOCASE NOT NULL PRIMARY KEY,
    SpezifikationID INTEGER NOT NULL,
    Alias VARCHAR,
    FOREIGN KEY(Email) REFERENCES Spezialist(Email)  ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(SpezifikationID) REFERENCES Spezifikation(SpezifikationID) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(Alias NOT GLOB '*[^ -~]*')
);



CREATE TABLE weitere_Kompetenzen(
    weitereKompetenzenID INTEGER NOT NULL PRIMARY KEY,
    Eigenschaft VARCHAR NOT NULL,
    CHECK(Eigenschaft NOT GLOB '*[^A-Za-z]*' AND LENGTH(Eigenschaft) > 0),
    CHECK(weitereKompetenzenID >= 0)
);

CREATE TABLE arbeitet(
    ProjektID INTEGER NOT NULL,
    Email VARCHAR COLLATE NOCASE NOT NULL,
    PRIMARY KEY(ProjektID, Email),
    FOREIGN KEY(ProjektID) REFERENCES Projekt(ProjektID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(Email) REFERENCES Spezialist(Email)  ON UPDATE CASCADE ON DELETE CASCADE
    
);

CREATE TABLE beherrscht(
    ProgrammierspracheID INTEGER NOT NULL,
    Kuerzel VARCHAR NOT NULL,
    Erfahrungsstufe INTEGER NOT NULL,
    PRIMARY KEY(ProgrammierspracheID, Kuerzel),
    FOREIGN KEY(ProgrammierspracheID) REFERENCES Programmiersprache(ProgrammierspracheID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(Kuerzel) REFERENCES Entwickler(Kuerzel) ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK(Erfahrungsstufe BETWEEN 1 AND 3)
);

CREATE TABLE betreut(
    Email1 VARCHAR COLLATE NOCASE NOT NULL PRIMARY KEY,
    Email2 VARCHAR COLLATE NOCASE NOT NULL,
    FOREIGN KEY(Email1) REFERENCES Spezialist(Email)  ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(Email2) REFERENCES Spezialist(Email)  ON UPDATE CASCADE ON DELETE CASCADE

);


CREATE TABLE Spezialist_hat_fachliche_Kompetenz(
    Email VARCHAR COLLATE NOCASE NOT NULL,
    fachlicheKompetenzID INTEGER NOT NULL,
    PRIMARY KEY(Email, fachlicheKompetenzID),
    FOREIGN KEY(Email) REFERENCES Spezialist(Email)  ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(fachlicheKompetenzID) REFERENCES fachliche_Kompetenz(fachlicheKompetenzID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Designer_hat_weitere_Kompetenzen(
    Email VARCHAR COLLATE NOCASE NOT NULL,
    weitereKompetenzenID INTEGER NOT NULL,
    PRIMARY KEY(Email, weitereKompetenzenID),
    FOREIGN KEY(Email) REFERENCES Designer(Email)  ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(weitereKompetenzenID) REFERENCES weitere_Kompetenzen(weitereKompetenzenID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER drei_mal_bewertet_trigger
    BEFORE INSERT ON Bewertung
        WHEN(SELECT COUNT(NEW.Telefonnummer) FROM Kunde, Bewertung 
            WHERE Kunde.Telefonnummer = Bewertung.Telefonnummer AND Kunde.Telefonnummer LIKE NEW.Telefonnummer
            GROUP BY NEW.Telefonnummer) >2
    BEGIN
        SELECT RAISE(ABORT, 'Projekt darf nur 3 mal von einem Kunden bewertet werden!!');
    END;


CREATE TRIGGER programmiersprache_wird_benutzt_trigger 
  BEFORE DELETE ON Programmiersprache
        WHEN(SELECT COUNT(beherrscht.Kuerzel) FROM beherrscht
            WHERE beherrscht.ProgrammierspracheID = OLD.ProgrammierspracheID) >0 
    BEGIN
        SELECT RAISE(ABORT, 'Jemand anderes kann die Programmiersprache! Programmiersprache kann nicht geloescht werden!');
    END;


CREATE TRIGGER mentor_wird_nicht_von_mentor_betreut_trigger 
  BEFORE INSERT ON betreut
        WHEN(SELECT COUNT(NEW.Email2) FROM betreut
            WHERE betreut.Email1 LIKE NEW.Email2) >0  
    BEGIN
        SELECT RAISE(ABORT, 'Ein Mentor kann nicht von einem anderen Mentor betreut werden!!');
    END;