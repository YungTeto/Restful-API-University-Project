--Nutzer
INSERT INTO Nutzer(Email, Passwort) VALUES('projektdude@gmail.com','A234');
INSERT INTO Nutzer(Email, Passwort) VALUES('daniel@gmail.com','B234');
INSERT INTO Nutzer(Email, Passwort) VALUES ('developerbro@mail.com', 'C234');
INSERT INTO Nutzer(Email, Passwort) VALUES ('frontendbro@mail.com', 'D234');
INSERT INTO Nutzer(Email, Passwort) VALUES ('juniodeveloperbro@mail.com', 'E234');
INSERT INTO Nutzer(Email, Passwort) VALUES('richbroski@mail.com','F234');
INSERT INTO Nutzer(Email, Passwort) VALUES('projektmacher@gmail.com','G234');
INSERT INTO Nutzer(Email, Passwort) VALUES ('UXbroski@mail.com', 'H234');
INSERT INTO Nutzer(Email, Passwort) VALUES ('UIdude@mail.com', 'I234');

INSERT INTO Nutzer(Email, Passwort) VALUES ('princeboateng@gmail.com', 'J234');
INSERT INTO Nutzer(Email, Passwort) VALUES ('curry@gmail.com', 'K234');


-- Kunde
INSERT INTO Kunde(Telefonnummer, Email) VALUES ('5678','richbroski@mail.com');
INSERT INTO Kunde(Telefonnummer, Email) VALUES ('1234','daniel@gmail.com');

INSERT INTO Kunde(Telefonnummer, Email) VALUES ('1357','princeboateng@gmail.com');
INSERT INTO Kunde(Telefonnummer, Email) VALUES ('2468','curry@gmail.com');

--Projektleiter
INSERT INTO Projektleiter(Email,Gehalt) VALUES('projektdude@gmail.com', 12.55);
INSERT INTO Projektleiter(Email,Gehalt) VALUES('projektmacher@gmail.com', 13.99);

--Projekt
INSERT INTO Projekt(ProjektID, Projektname, Projektdeadline, Telefonnummer, Email) VALUES (1,'TAKEOVER','2022-10-12','1234','projektdude@gmail.com');
INSERT INTO Projekt(ProjektID, Projektname, Projektdeadline, Telefonnummer, Email) VALUES (2,'Daimler AG','2022-12-12','5678','projektmacher@gmail.com');


--Bewertung
INSERT INTO Bewertung(BewertungID, Bepunktung, Telefonnummer, ProjektID, Text) VALUES (4,2,'5678',2,'NAJA');






--Aufgaben
INSERT INTO Aufgaben(AufgabenID,Priorisierung, Aufgabendeadline, Beschreibung, Status, ProjektID) VALUES (1,'hoch','2022-09-10','Debugging', 'Fast fertig',1);
INSERT INTO Aufgaben(AufgabenID,Priorisierung, Aufgabendeadline, Beschreibung, Status, ProjektID) VALUES (2,'leicht','2022-11-12','Implementierung der API', 'Noch nicht angefangen',2);
-- Aufgaben Query Test
INSERT INTO Aufgaben(AufgabenID,Priorisierung, Aufgabendeadline, Beschreibung, Status, ProjektID) VALUES (3,'hoch','2022-11-12','Implementierung der API1', 'Noch nicht angefangen',1);
INSERT INTO Aufgaben(AufgabenID,Priorisierung, Aufgabendeadline, Beschreibung, Status, ProjektID) VALUES (4,'hoch','2022-11-12','Implementierung der API2', 'Noch nicht angefangen',1);
INSERT INTO Aufgaben(AufgabenID,Priorisierung, Aufgabendeadline, Beschreibung, Status, ProjektID) VALUES (5,'hoch','2022-11-12','Implementierung der API3', 'Noch nicht angefangen',2);

--Spezialist
INSERT INTO Spezialist(Email, Verfuegbarkeitsstatus) VALUES('developerbro@mail.com','Ausgebucht');
INSERT INTO Spezialist(Email, Verfuegbarkeitsstatus) VALUES('frontendbro@mail.com','Im Urlaub');
INSERT INTO Spezialist(Email, Verfuegbarkeitsstatus) VALUES('juniodeveloperbro@mail.com','verfuegbar');
INSERT INTO Spezialist(Email, Verfuegbarkeitsstatus) VALUES ('UXbroski@mail.com','Jederzeit');
INSERT INTO Spezialist(Email, Verfuegbarkeitsstatus) VALUES ('UIdude@mail.com','Nur unter der Woche');

--Spezifikation
INSERT INTO Spezifikation(SpezifikationID, Designart) VALUES (1,'digital');
INSERT INTO Spezifikation(SpezifikationID, Designart) VALUES (2,'analog');



--Entwickler
INSERT INTO Entwickler(Kuerzel, Email) VALUES ('OdeFG123','juniodeveloperbro@mail.com');
INSERT INTO Entwickler(Kuerzel, Email) VALUES ('DIHjK123','frontendbro@mail.com');
INSERT INTO Entwickler(Kuerzel, Email) VALUES ('ABCde123','developerbro@mail.com');


--Programmiersprache
INSERT INTO Programmiersprache(ProgrammierspracheID, Name) VALUES (1,'Java');
INSERT INTO Programmiersprache(ProgrammierspracheID, Name) VALUES (2,'Python');


--fachliche Kompetenz
INSERT INTO fachliche_Kompetenz(fachlicheKompetenzID, Eigenschaft) VALUES (1, 'Serveradminstration');
INSERT INTO fachliche_Kompetenz(fachlicheKompetenzID, Eigenschaft) VALUES (2, 'ERP');

INSERT INTO fachliche_Kompetenz(fachlicheKompetenzID, Eigenschaft) VALUES (3, 'Illustrator');
INSERT INTO fachliche_Kompetenz(fachlicheKompetenzID, Eigenschaft) VALUES (4, 'Photoshop');

--weitere Kompetenzen
INSERT INTO weitere_Kompetenzen(weitereKompetenzenID, Eigenschaft) VALUES (1, 'Kompromissfaehigkeit');
INSERT INTO weitere_Kompetenzen(weitereKompetenzenID, Eigenschaft) VALUES (2, 'Ergebinisorientierung');
INSERT INTO weitere_Kompetenzen(weitereKompetenzenID, Eigenschaft) VALUES (3, 'Flexiblitaet');
INSERT INTO weitere_Kompetenzen(weitereKompetenzenID, Eigenschaft) VALUES (4, 'Teamfaehigkeit');

--Spezialist hat fachliche Kompetenz
INSERT INTO Spezialist_hat_fachliche_Kompetenz(Email, fachlicheKompetenzID) VALUES ('developerbro@mail.com', 2);
INSERT INTO Spezialist_hat_fachliche_Kompetenz(Email, fachlicheKompetenzID) VALUES ('UXbroski@mail.com', 3);

--Designer
INSERT INTO Designer(Email,SpezifikationID,Alias) VALUES ('UXbroski@mail.com',1,'');
INSERT INTO Designer(Email,SpezifikationID,Alias) VALUES ('UIdude@mail.com',2,'Picasso');

--Designer hat weitere Kompetenzen#
INSERT INTO Designer_hat_weitere_Kompetenzen(Email,weitereKompetenzenID) VALUES('UXbroski@mail.com',1);
INSERT INTO Designer_hat_weitere_Kompetenzen(Email,weitereKompetenzenID) VALUES('UIdude@mail.com',3);

--arbeitet
INSERT INTO arbeitet(ProjektID,Email) VALUES(1,'developerbro@mail.com');
INSERT INTO arbeitet(ProjektID,Email) VALUES(2,'UXbroski@mail.com');

--beherrscht
INSERT INTO beherrscht(ProgrammierspracheID,Kuerzel,Erfahrungsstufe) VALUES (2,'ABCde123',2);

--betreut
INSERT INTO betreut(Email1,Email2) VALUES('UXbroski@mail.com', 'UIdude@mail.com');


-- 3 Bewertung Trigger test
INSERT INTO Bewertung(BewertungID, Bepunktung, Telefonnummer, ProjektID, Text) VALUES (1,2,'1234',1,'Geht besser');
INSERT INTO Bewertung(BewertungID, Bepunktung, Telefonnummer, ProjektID, Text) VALUES (2,2,'1234',1,'Geht noch besser');
INSERT INTO Bewertung(BewertungID, Bepunktung, Telefonnummer, ProjektID, Text) VALUES (3,2,'1234',1,'Geht noch besser!!!');
--INSERT INTO Bewertung(BewertungID, Bepunktung, Telefonnummer, ProjektID, Text) VALUES (1,999,1234,1,'Test');

-- Programmiersprache wird benutzt Trigger test
INSERT INTO beherrscht(ProgrammierspracheID, Kuerzel, Erfahrungsstufe) VALUES (1, 'ABCde123', 2);
--DELETE FROM Programmiersprache WHERE Name LIKE 'Java'


--Mentor wird von Mentor betreut Trigger test
INSERT INTO betreut(Email1,Email2) VALUES('developerbro@mail.com', 'frontendbro@mail.com');
--INSERT INTO betreut(Email1,Email2) VALUES('juniodeveloperbro@mail.com', 'developerbro@mail.com')

