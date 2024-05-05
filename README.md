### Before start  

Before starting the application please don't forget to add your Spotify **CLIENT_ID** and **CLIENT_SECRET** in **application.yaml**

### How to run  

Application and database start in two different containers, to start them just run the command: ```docker-compose up -d```

### A few things to know

- Scheduler fetches artists and albums from Spotify at the start of the application and *every minute* after
- Modified artists and albums won't be updated by the scheduler's fetched data
- Modified artists and albums are those that have been updated using the application API. Those artists and albums that were deleted are not considered modified and will be reinserted into the database by the scheduler
- Full-text search functionality for artists and albums was implemented using PostgreSQL capabilities
- There are only two simple tests for this application, just as an example of using **testcontainers**
- To run tests locally run this command ```mvn clean verify -Dspring.profiles.active=test```. This allows us to turn off scheduler when running tests
