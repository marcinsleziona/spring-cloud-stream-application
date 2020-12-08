# spring-cloud-stream-application
Application fetches every 20 [sec] all the countries from the https://api.covid19api.com/countries -> pullCountries
 
Then for Poland and Germany fetches all the Covid results (https://api.covid19api.com/total/country/:country) -> pullResults

Then it filters results after 1.12.2020 -> filterDecemberResults

Then it scatter results into two queues -> scatterResults 

Then it prints only Poland results -> printPolandResults

## How to run the application 
Run Kafka locally on localhost:9093
Run the application mvn spring-boot:run


