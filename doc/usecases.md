# Use-Cases

## User-Service

- Admin-Accounts können neue Nutzer anlegen

---

- Als globaler Admin kann man neue Nutzer dem System hinzufügen

## Issue-Service

- Nutzer können in diesen Projekten Issues erstellen

---

- Als Nutzer kann man ein Issue anlegen (Ersteller).
- Als Nutzer <u>kann</u> man einen (!) Nutzer mit der Bearbeitung eines Issues beauftragen.

## Project-Service

Nutzer sind Projekten zugeordnet → Projeke kennen ihre Nutzer (Jedem Projekt ist mindestens ein Nutzer zugeordnet).

- Jeder angelegte Nutzer kann Projekte erstellen
- Projektersteller können Nutzer zu den Projekten hinzufügen

---

- Als Nutzer kann man ein Projekt anlegen.
- Als Projekt-Admin kann man Nutzer zu dem Projekt hinzufügen.
- Als Nutzer kann man in einem Projekt die Mitglieder einsehen.
- Als Nutzer kann man in einem Projekt die Issues einsehen.

## News-Service (Verwaltung von Neuigkeiten als Aggregate)

- Neue Issues in den Projekten des Nutzers
- Neue Zugehörigkeiten zu Projekten

---

- Als Nutzer kann man die "neuen" Projektzugehörigkeiten ansehen.
- Als Nutzer kann man die "neuen" Issues ansehen.
- Als Nutzer kann man die "neuen" Mitglieder ansehen.

## etc.

- Nur eingeloggte Nutzer können auf das Backend zugreifen
