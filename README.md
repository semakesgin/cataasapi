# **REST Client for the CATAAS API**
**Overview**

REST client accesses the public CATAAS API at <https://cataas.com/#/> and shows the results to the users on demand. It provides a web page available at http://localhost:8080/choices where users can choose from the following options:

1. Get a random cat with a tag. 
1. Get a random cat with a text. 
1. Get a random cat with user defined width and height.

After selecting an option, application asks details based on choice (e.g., tag for the first option) and the name of the file before downloading the image.

The application requires the root directory where downloaded files will be saved. Root directory defined C:/Cats/ for the app. The application creates subfolders based on the user’s choice: Tag, Text, Width or Height. If the subfolder already exists, the application saves the image file in it.

**Features**

The REST client application includes the following features:

- **Scheduled Cleanup Process:** A daily cleanup process is scheduled to delete all files in the downloads directory at midnight.
- **Reporting Feature:** A reporting feature that creates a text file on-demand with all the current downloaded images. The reporting feature can be accessed from the “Generate Report” button on the main screen.The report includes: 
  - Total file count in all folders
  - Date of report
  - Subfolder details(file count in each subfolder and file names in each subfolder)
- **Database Storage:** The application stores the downloaded file names, paths and sizes in a database.When cleanup process runs, report data in the database is also deleted.

**Technologies and features**

The application is built using the following technologies:

- Java SE 19.0.2
- Spring Boot 3.1.0
- Maven
- MySql database
- Thymeleaf (for html pages)
- JUnit 5 for unit tests (writed unit tests for service layer)

**Installation**

To install and run the application, follow these steps:

1\.  Download the project file and unzip it. Import it to any IDE (e.g., Eclipse, Intellij IDEA) as a Maven Project.

2\. Add ‘**-Droot.directory=C:/Cats/’**  as a VM Option. For Intellij IDEA, follow these steps:

- Go to Run- Edit Configurations-
- Modify options- Add VM Options ‘**-Droot.directory=C:/Cats/’**
- Press Apply and Ok buttons.

3\. Ensure that MySQL is installed on your computer to run the project. If Mysql doesn't exists, you can use h2 database instead by following these steps;

- Add the following dependency to your pom.xml: 
```
<dependency>
 <groupId>com.h2database</groupId>
 <artifactId>h2</artifactId>
 <scope>runtime</scope>
</dependency>
```

- Comment out the following two lines in application.properties: 
```
spring.datasource.url =jdbc:mysql://localhost:3306/cat\_aas\_api\_database
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
- Add the following two lines to application.properties:
```
spring.h2.console.enabled=true
spring.datasource.url = jdbc:h2:mem:cat\_aas\_api\_database
```
1. Run the application and open <http://localhost:8080/choices> in your web browser.

1. To view the file\_details table in the database, go to <http://localhost:8080/h2-console>  and use the following credentials: 
- JDBC URL: jdbc:h2:mem:cat\_aas\_api\_database
- User name: root
- Password: password

**Limitations:**

- The application currently supports only the CATAAS API and is limited to accessing cat images.
- The cleanup process deletes all files in the downloads directory without distinguishing between different users or specific images.
- The reporting feature provides basic information about the downloaded pictures but could be enhanced with additional statistics or filtering options.

**Potential Improvements:**

- Enhance the user interface to improve usability and provide a more visually appealing experience.
- Provide support for different databases, allowing users to choose their preferred database technology.
- Enhance the reporting feature to provide more detailed statistics, such as the most downloaded images, popular tags, or file size distribution.
- Implement more advanced cleanup options, such as deleting files based on criteria like size.


