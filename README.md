# course-mgmt

A simple Clojure-WebApplication for Course-Management. Including:
 - RegistrationForm
 - Course Creation
 - List of Attendees

-> This project is my "learning-Clojure" project. Some bits of code are inspired by "https://github.com/citerus/notes".

   For Design "https://milligram.io" was used.

## Prerequisites

- [Clojure](https://clojure.org/)

- [Java](https://openjdk.java.net/) (OpenJDK), Java min. Version: 8.0 due to java-time

- [Leiningen](https://github.com/technomancy/leiningen) 2.0.0 or above

- [MongoDB](https://www.mongodb.com)


## Running

To start the application, run:

    lein run

Default: MongoDB without auth on localhost & default port. Jetty binds to localhost & port 8000

Please edit "config/db.edn" & "config/jetty.edn" for your needs.


## License

Copyright (c) 2019-2020 Niklas Fehl

Released under the MIT license.
