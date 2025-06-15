# KalliTechnis Console Application

> **Version**: 1.0
> **Language**: Java 17+

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Quick Start](#quick-start)
4. [Command‑line Navigation](#command-line-navigation)
5. [Data Persistence](#data-persistence)
6. [Code Structure](#code-structure)

---

## Project Overview

This is a **cross‑platform Java console application** that enables a *service provider* to manage theatrical and musical events, customers and ticket bookings entirely from an intuitive, colour‑enhanced CLI.  Data is stored in lightweight CSV files, so **no external database** is required.

---

## Features

| Category               | Description                                                                                                                                 |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **Event CRUD**         | Create / Edit / Delete / List **theatrical plays** & **music shows** with 🌐 *ID, Title, Lead, Venue, Date*.                                |
| **Customer CRUD**      | Manage customers (*ID, Name*) effortlessly.                                                                                                 |
| **Ticket Bookings**    | Reserve tickets for any customer to any event (separate commands for theater & music).                                                      |
| **Live Statistics**    | One‑click view of how many tickets each event has sold, in real time.                                                                       |
| **Persistent Storage** | All entities are saved to CSV files (`theater.csv`, `music.csv`, `customers.csv`, `bookings.csv`) on exit and re‑loaded on startup.         |
| **ANSI UX**            | Colourised output (cyan/yellow/green/red), centred boxed menus and confirmations. Works out‑of‑the‑box on Linux/macOS and Windows Terminal. |
| **Clean Architecture** | DRY, KISS, single‑responsibility classes + a minimal generic `CsvRepository<T>` that re‑uses reflection to access IDs.                      |

---

## Quick Start

### 1 · Clone & Compile

```bash
# Clone
$ git clone https://github.com/marftar/KalliTechnis
$ cd KalliTechnis

# Compile java sources into ./out
$ find src -name "*.java" | xargs javac -d out -sourcepath src -encoding UTF‑8
```

### 2 · Run

```bash
$ java -cp out Main
```

> \:bulb:  On first run the application creates the CSV files next to the executable jar/classpath. Edit them manually or via the menu.

### 3 · (Option B) Compile & Run with Maven

A minimal **`pom.xml`** is included for convenience.

```bash
$ mvn clean compile exec:java -Dexec.mainClass=Main
```

---

## Command‑line Navigation

```
╔══════════════════════════════════════════╗
║         ΔΙΑΧΕΙΡΙΣΗ ΚΑΛΛΙΤΕΧΝΙΚΩΝ        ║
╠══════════════════════════════════════════╣
║ 1. Θεατρικές παραστάσεις                ║
║ 2. Μουσικές παραστάσεις                 ║
║ 3. Πελάτες                              ║
║ 4. Κράτηση θεατρικής παράστασης         ║
║ 5. Κράτηση μουσικής παράστασης          ║
║ 6. Στατιστικά εισιτηρίων                ║
║ 0. Έξοδος                               ║
╚══════════════════════════════════════════╝
```

* **Sub‑menus** open in the same boxed style with keys `a` Insert, `b` Edit, `c` Delete, `d` List, `x` Back.
* Confirmation / error messages appear in green/red boxed banners.
* Press **ENTER** when prompted by the `pause()` banner to return to the previous screen.

---

## Data Persistence

| File            | Description                                |
| --------------- | ------------------------------------------ |
| `theater.csv`   | All `TheaterPlay` entities – one per line. |
| `music.csv`     | All `MusicShow` entities.                  |
| `customers.csv` | All `Customer` entities.                   |
| `bookings.csv`  | All `Booking` records.                     |

CSV **syntax**: pipe‐separated values, e.g.
`1|Romeo & Juliet|John Doe|Athens Megaron|2025-11-02`.

---

## Code Structure

```
src/
 ├─ entity/          # POJO domain objects (TheaterPlay, MusicShow, Customer, Booking)
 ├─ repository/      # Generic CsvRepository<T>
 └─ Main.java        # Entry point & CLI engine
```

### Dependency graph

```
CsvRepository<T>
      ↑
entity.*  ← Main (CLI)
```
