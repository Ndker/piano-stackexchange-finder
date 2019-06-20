### INSTALL

_Java 11 required_

##### BUILD:

    cp src/main/resources/application.properties.example src/main/resources/application.properties     
    export JAVA_HOME=/path/lib/java-11-jdk  
    ./gradlew build
          
##### RUN:
       
    export JAVA_HOME=/path/lib/java-11-jdk
    ./gradlew bootRun
     
     
### API:

### Search by text
    POST /api/search?text=OutOfMemory HTTP/1.1
    Host: 127.0.0.1:8081
    
##### Response
    Status: 202 Accepted
    Body:  
    {
        "requestId": "65e5a1c6-41e3-462a-a63b-f592f001ef9c"
    }
 
### Get results by id
    GET /api/search/result?requestId=65e5a1c6-41e3-462a-a63b-f592f001ef9c&page=1&size=10 HTTP/1.1
    Host: 127.0.0.1:8081
    
##### If searching results is not ready:
    Status: 202 ACCEPTED
    Body: null
    
##### Else :
    
    Status: 200 OK
    Body: 
    {
        "data": [
                        {
                            "date": "2019-06-19T12:00:00+03:00",
                            "title": "HAproxy: Tomcat and OutOfMemory",
                            "author: "user3065205",
                            "sourceLink": "https://stackoverflow.com/questions/32048236/haproxy-tomcat-and-outofmemory"
                        },
                        {
                            ...
                        }
                    ],
        "meta": {
            "currentPage": 10,
            "hashNext": true,
            "totalPages": 0
        } 
    }
 
 
 ### DESCRIPTION
 
 1. SearchController - controller for processing incoming requests. The POST request is necessary to queue for task of receiving data from stackexchange service. A unique task ID and status 202 ACCEPTED are sent in response.
 The web-client with this ID may request results. If data are received from StackExchange, then will be return an array of questions, current page, next page sign, and  total number of pages if known. If data are not received, the will be return statys 202 ACCEPTED.
 
 2. Handling tasks. Handling task is performed asynchronously with ThreadPoolTaskScheduler.  Handling task is actualyy sending a request to StackExchange server. By default, we request first 100 questions. Also, 30 minutes after request, task and its search results are deleted from the memory.
 
 3. This asynchronous scheme was chosen for following reason:
 Typically, users when searching do not go beyond the first few pages. Accordingly, we'd better get first 100 results, then display them in a table of 10 questions per page, with 10 pages. And user can go to next and previous pages without synchronous requests to StackExchange service.
 
 4. Upload next data. If a user navigates to a page where we have no results (for example, number 11), we can also return status 202 Accepted. And then, by analogy with p. 2, we requested an additional 100 pages. Thus, user can navigate to to 12, 13, etc. the pages quickly.
 
 5. Disadvantages - if  user immediately requests a 100th page, then according to current scheme, we will load questions for all 100 pages (ie, 1000 records). And first, user will wait for a long time for data from his 100th page, and secondly, all this data will be loaded into memory. Thirdly, out program can be banned on StackExchange server. But for this task, we will assume that user can only go to next or previous page, or jump no more than 3 pages.
