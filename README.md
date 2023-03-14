# boot-calendar

## Description

This short demonstration of the spring-boot micro framework. This example shows how to build a spring-boot calendar web application with the fullcalendar.js and jQuery libraries. The web application persists Event data with JPA and hibernate and supplies a rest interface for retrieving Event objects from the web server and displaying them in a browser calendar.  

## Prerequists

This project uses Spring Tool Suite for development with spring-boot installed. 

All methods have been added inline to the Application file. (/src/com/sanoy/CalendarApplication)

Note that the code in the repository has the end product of the work in this tutorial. 

## Instructions

### Install Spring Dev tools

Spring dev tools will detect when you've made changes to your java files and will automagically restart the server so that you don't have to continually restart the server. Add this to your pom.xml file
``` xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<scope>runtime</scope>
		<optional>true</optional>
	</dependency>
```

#### 1. Create a Spring boot application in Spring Tool Suite

Select the Web, Thymeleaf, JPA, HSQLDB dependancies. 

#### 2. Add Support for web mvc and thymeleaf to the pom.xml file

if they don't already exist from the step above

``` xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-thymeleaf</artifactId>
	</dependency>
```

#### 3. Turn off thymeleaf caching so that changes to html files will reload 

add the following line to the application.properties file. (/src/main/resources/application.properties)

``` javascript
spring.thymeleaf.cache=false	
```
#### 4. And a thymeleaf index.html to thymeleaf templates  (/src/main/resources/templates)

```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-spring4-4.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset='utf-8' content="Cory Sanoy" name="Author" />
</head>
<body>
Hello World!
</body>
</html>
```

#### 5. Add a controller for the index.html file

This controller will give you access to the html pages from a browser. Access with the following url: http://localhost:8080/

``` java
@Controller	
class CalendarController {
	@RequestMapping(value="/", method=RequestMethod.GET) 
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}
}
```

#### 6. Create JPA Entity for an Event

This entity will be used by the Java Persistence Architecture (JPA) to perform crud operations on the database. 

``` java
@Entity
@Table(name="event")
class Event {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title; 
	private String description;
	private LocalDateTime start; 
	private LocalDateTime finish;
	
	public Event(Long id, String title, String description, LocalDateTime start, LocalDateTime finish) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.start = start;
		this.finish = finish;
	}
	
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getFinish() {
		return finish;
	}

	public void setFinish(LocalDateTime finish) {
		this.finish = finish;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", description=" + description + ", start=" + start
				+ ", finish=" + finish + "]";
	} 	
}
```
#### 7. Build the repository

``` java
@Repository
interface EventJpaRepository extends JpaRepository<Event, Long> {
	
	/* Note these two methods do the same thing.  The @Repository annotation handles both*/
	
	/* This one uses a JPQL */
	public List<Event> findByStartGreaterThanEqualAndFinishLessThanEqual(LocalDateTime start, LocalDateTime end);
	
	
	/* This one uses an @Query */
	@Query("select b from Event b where b.start >= ?1 and b.finish <= ?2")
	public List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);
	
}
```	
#### 8. Add Hibernate libraries to pom.xml

``` xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>
```	
#### 9.	Add import.sql with events to the /src/main/resources

``` javascript
	
insert into event(id, title, start, end, description) values (1, 'event1', '2015-01-01 1:00:00', '2015-01-01 2:00:00', 'description1')
insert into event(id, title, start, end, description) values (2, 'event2', '2015-01-02 2:00:00', '2015-01-02 3:00:00', 'description1')
insert into event(id, title, start, end, description) values (3, 'event3', '2015-01-03 1:00:00', '2015-01-03 2:00:00', 'description1')
insert into event(id, title, start, end, description) values (4, 'event4', '2015-01-04 1:00:00', '2015-01-04 2:00:00', 'description1')

```
	
#### 10. Build the Rest controller to provide a list of Events

``` java
@RestController
class EventController {
	
	@Autowired
	private EventRepository eventRespository;
	
	@RequestMapping(value="/events", method=RequestMethod.GET)
	public List<Event> events() {
		return eventRespository.findAll();
	}
}
```

#### 11. Add the webjars for fullcalendar, moment, jquery to the pom.xml file. 

``` xml
		<dependency>
    		<groupId>org.webjars</groupId>
    		<artifactId>fullcalendar</artifactId>
    		<version>2.2.5</version>
		</dependency>
		<dependency>
		    <groupId>org.webjars</groupId>
		    <artifactId>momentjs</artifactId>
		    <version>2.9.0</version>
		</dependency>
		<dependency>
		    <groupId>org.webjars</groupId>
		    <artifactId>jquery-ui</artifactId>
		    <version>1.11.2</version>
		</dependency>
		<dependency>
    		<groupId>org.webjars</groupId>
		    <artifactId>jquery</artifactId>
		    <version>2.1.3</version>
		</dependency>
```

#### 12. Add the calendar.html file.

``` html

<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-spring4-4.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset='utf-8' content="Cory Sanoy" name="Author" />

	<link href="http://fullcalendar.io/js/fullcalendar-2.2.5/fullcalendar.css" 
			th:href="@{/webjars/fullcalendar/2.2.5/fullcalendar.css}" rel="stylesheet"></link>
	<link href="http://fullcalendar.io/js/fullcalendar-2.2.5/fullcalendar.print.css" 
			th:href="@{/webjars/fullcalendar/2.2.5/fullcalendar.print.css}" rel="stylesheet" media="print"></link>
	<script src="http://cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment.min.js"
	        th:src="@{/webjars/momentjs/2.9.0/min/moment.min.js}" type="text/javascript"></script>
	<script src="http://cdn.jsdelivr.net/webjars/jquery/2.1.3/jquery.min.js"
	        th:src="@{/webjars/jquery/2.1.3/jquery.min.js}" type="text/javascript"></script>
	<script src="http://fullcalendar.io/js/fullcalendar-2.2.5/fullcalendar.min.js"
			th:src="@{/webjars/fullcalendar/2.2.5/fullcalendar.min.js}" type="text/javascript"></script>

<script>
<!--
$(document).ready(function() {

	$('#calendar').fullCalendar({
		defaultDate: '2014-11-01',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: [
			{
				title: 'All Day Event',
				start: '2014-11-01'
			},
			{
				title: 'Long Event',
				start: '2014-11-07',
				end: '2014-11-10'
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: '2014-11-09T16:00:00'
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: '2014-11-16T16:00:00'
			},
			{
				title: 'Conference',
				start: '2014-11-11',
				end: '2014-11-13'
			},
			{
				title: 'Meeting',
				start: '2014-11-12T10:30:00',
				end: '2014-11-12T12:30:00'
			},
			{
				title: 'Lunch',
				start: '2014-11-12T12:00:00'
			},
			{
				title: 'Meeting',
				start: '2014-11-12T14:30:00'
			},
			{
				title: 'Happy Hour',
				start: '2014-11-12T17:30:00'
			},
			{
				title: 'Dinner',
				start: '2014-11-12T20:00:00'
			},
			{
				title: 'Birthday Party',
				start: '2014-11-13T07:00:00'
			},
			{
				title: 'Click for Google',
				url: 'http://google.com/',
				start: '2014-11-28'
			}
		]
	});
	
});
-->
</script>

<style>

	body {
		margin: 40px 10px;
		padding: 0;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		font-size: 14px;
	}

	#calendar {
		max-width: 900px;
		margin: 0 auto;
	}

</style>			
</head>
<body>
	<div id='calendar' th:id="calendar"></div>
</body>
</html>
```

#### 13. Change the js code within the calendar.html file to load the Events from the rest interface. 

``` javascript
	$(document).ready(function() {
	$('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		defaultDate: '2019-06-01',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: {
	        url: '/allevents',
	        type: 'GET',
	        error: function() {
	            alert('there was an error while fetching events!');
	        },
	        //color: 'blue',   // a non-ajax option
	        //textColor: 'white' // a non-ajax option
	    }
	});
});
```

#### 14. Add a query to retrieve events by date range. 

``` java

@RequestMapping(value="/events", method=RequestMethod.GET)
	public List<Event> getEventsInRange(@RequestParam(value = "start", required = true) String start, 
						@RequestParam(value = "end", required = true) String end) {
	Date startDate = null;
	Date endDate = null;
	SimpleDateFormat inputDateFormat=new SimpleDateFormat("yyyy-MM-dd");

	try {
		startDate = inputDateFormat.parse(start);
	} catch (ParseException e) {
		throw new BadDateFormatException("bad start date: " + start);
	}

	try {
		endDate = inputDateFormat.parse(end);
	} catch (ParseException e) {
		throw new BadDateFormatException("bad end date: " + end);
	}

	LocalDateTime startDateTime = LocalDateTime.ofInstant(startDate.toInstant(),
	ZoneId.systemDefault());

	LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate.toInstant(),
	ZoneId.systemDefault());

	return eventRepository.findByDateBetween(startDateTime, endDateTime); 
}
```

#### 15. Add error handling when the start and end date is incorrect

This class will handle the BadDateFormatException throw in the getEventsRange function above and display and error page with the message from the error above. 

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadDateFormatException extends RuntimeException {
  private static final long serialVersionUID = 1L;

	public BadDateFormatException(String dateString) {
        super(dateString);
    }
}
````

#### 16. Add @Table and @Column annotations to the Event Entity 

Specifying table and column names will enable the @Query annotation contained in the EventRepository to access those parameters by name. 

``` java
@Entity
@Table(name = "Event")
class Event {
	
	@Column(name="start")
	private Date start;

	@Column(name="end")
	private Date end;

...

}
```
#### 17. Add crud operations to the CrudRepository object

``` java
interface EventRepository extends CrudRepository<Event, Long> {
	List<Event> findAll();
	Event save(Event event);
	void delete(Event event);

	@Query("select b from Event b " +
	         "where b.start between ?1 and ?2 and b.end between ?1 and ?2")
	 List<Event> findByDatesBetween(Date start, Date end);
}
```

#### 18. Add crud operations for events (create, read, update, delete)

``` java
@RequestMapping(value="/event", method=RequestMethod.POST)
public Event addEvent(@RequestBody Event event) {
	Event created = eventRepository.save(event);
	return created; 
}

@RequestMapping(value="/event", method=RequestMethod.PATCH)
public Event updateEvent(@RequestBody Event event) {
	return eventRepository.save(event);
}

@RequestMapping(value="/event", method=RequestMethod.DELETE)
public void removeEvent(@RequestBody Event event) {
	eventRepository.delete(event);
}
```
#### 19. Add the javascript code to call the CRUD functions. (see the jsoncalendar.html file for more info)

## Change the in-memory database to postgres

Maybe you want a postgres database rather than an in memory database. Here is how...

### 1. Install and start a postgres database

You'll have to install and start a postgres database on the computer where you started the web application. For brevity see the postgres documentation on how to install and start a postgres database. 

### 2. Make changes to your pom file
#### 1. Add the postgres jdbc drivers to the pom file
``` xml
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
	<scope>runtime</scope>
</dependency>
```
#### 2. Remove the hsqldb from the pom file
``` xml
<dependency>
	<groupId>org.hsqldb</groupId>
	<artifactId>hsqldb</artifactId>
	<scope>runtime</scope>
</dependency>
		
```

### 3. Add the following configuration to your application.properties file

``` javascript

spring.datasource.url=jdbc:postgresql://localhost:5432/calendar
spring.datasource.username=postgres
spring.datasource.password=postgres

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

```
### 4. Restart the server


