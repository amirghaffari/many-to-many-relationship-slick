Many-to-many relationship with Slick in Play Framework.
-----------

This example illustrates how to implement a many-to-many relationship with [Slick](https://www.playframework.com/documentation/2.6.x/PlaySlick) in [Play Framework](https://www.playframework.com).

This implements a library search engine by employing Bootstrap, Play Framework, and Slick. The following data model is developed in the H2 Database Engine using [evolution scripts](https://www.playframework.com/documentation/2.6.x/Evolutions).
The application provides paging, filtering, and sorting functionalities.

![Data model](https://github.com/amirghaffari/many-to-many-relationship-slick/blob/master/many-to-many-relationship.png "Data model")

How to build and run
----------------------------------------

Use the following commands to run the application:

	$ git clone git://github.com/amirghaffari/many-to-many-relationship-slick
	$ cd many-to-many-relationship-slick
	$ sbt clean compile ~run
	$ http://localhost:8080
	
You will see a landing page as below:

![Landing page](https://github.com/amirghaffari/many-to-many-relationship-slick/blob/master/landing-page.png "Landing page")
