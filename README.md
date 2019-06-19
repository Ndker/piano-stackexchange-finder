### INSTALL

##### BUILD:
    
     ./gradlew build  
     
##### RUN:
       
     ./gradlew bootRun
     
     
### API:

### Search by text
    POST /api/search?text=OutOfMemory HTTP/1.1
    Host: 127.0.0.1:8081
    
    Status: 202 Accepted
    Body:  
    {
        "requestId": "65e5a1c6-41e3-462a-a63b-f592f001ef9c"
    }
 
### Get results by id
    GET /api/search/result?requestId=65e5a1c6-41e3-462a-a63b-f592f001ef9c&page=1&size=10 HTTP/1.1
    Host: 127.0.0.1:8081
    
######If searching results is not ready:
    Status: 202 ACCEPTED
    Body: null
    
######Else :
    
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
            "hashNext: true
        } 
    }
 