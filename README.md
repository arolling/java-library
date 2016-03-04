# Epicodus Library

#### A library management app

#### By Matt Rosanio & Abby Rolling, March 2-3 2016

## Description

This web based app allows a library patron to search for books, check them out, return books, and view their account details including changing password.

It allows librarians to add books, authors, and users, as well as editing books and users and changing user permissions. It also provides a list of patrons with overdue books with links to their user details.


## Setup/Installation Requirements

Clone this repository:
```
$ cd ~/Desktop
$ git clone github address
$ cd folder-name
```

Open terminal and run Postgres:
```
$ postgres
```

Open a new tab in terminal and create the `library` database:
```
$ psql
$ CREATE DATABASE library;
$ psql library < library.sql
```

Navigate back to the directory where this repository has been cloned and run gradle:
```
$ gradle run
```

## Known Bugs

- Does not sort checked out books or overdue books by due date.
- Due date is hard coded to be 3 days after check out date.

## Technologies Used

* Java
* junit
* Gradle
* Spark
* fluentlenium
* PostgreSQL
* Bootstrap

### License

Licensed under the GPL.

Copyright (c) 2016 **Abigail Rolling & Matt Rosanio**
