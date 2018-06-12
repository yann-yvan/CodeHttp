# CodeHttp

[![](https://jitpack.io/v/yann-yvan/CodeHttp.svg)](https://jitpack.io/#yann-yvan/CodeHttp)

## A light way to make communication between android and server using a predefine structure server response

## Features
1. Prepare your request
2. Use SpeedController to make request asynctask without building external or internal asynctask class
3. Handle your server response very easy
## Usage

### Step 1:
#### Add it in your root build.gradle at the end of repositories
```
allprojects {
  repositories {
	...
	maven { url 'https://jitpack.io' }
  }
}
```

### Step 2:
#### Add gradle dependecy
```
dependencies {
   implementation 'com.github.yann-yvan:CodeHttp:V1.0'
}
```
### Step 3:
#### Define base url in android manifest
```xml
  <manifest ...>
    <application...>
        <meta-data
            android:name="BASE_URL"
            android:value="http://baseurl/" />
    </application>
</manifest>
```

### Step 4: (optional)
#### Enable debugger mode (Please make sure to delete the line or set it as false in production)
```xml
  <manifest ...>
    <application...>
        <meta-data
            android:name="DEBUG_MODE"
            android:value="true" />
    </application>
</manifest>
```
### Let's make our first request
```java
final DefaultResponse response = new DefaultResponse("login", false);
//First argument the route of the request
//Second argument is a boolean that define whetter  the request should include token or not
PrepareRequest request = new PrepareRequest();
request.setOutgoing("json data");
//or you can set a JSonObject directly like this
request.getOutgoingJsonObject().put("email", "foo@codehttp.com");
response.setPrepareRequest(request);
//for a POST request use this method
TestController.this.post(response);
//for a GET request use this method
TestController.this.get(response);
//a complete sample will be display below
```
### New features for speed controller
Now we  this controller you yan execute request in a very simple way
```java
 SpeedController.run(
                        PrepareRequest.Method.POST,
                        new DefaultResponse("test", false), new SpeedController.OnAfterExecute() {
                            @Override
                            public void play(DefaultResponse response) {
                                Toast.makeText(MainActivity.this, "Good no error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void foundException(Exception e) {
                                if (e instanceof NoInternetException){
                                    Toast.makeText(MainActivity.this, "OOPS "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }else if (e instanceof RequestException){
                                    Toast.makeText(MainActivity.this, "OOPS "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }else{//JSonException
                                    Toast.makeText(MainActivity.this, "OOPS "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
```

### Handle server response
## This is a sample server respose
These fields should always be present in your server response
```json
{
 "status":true,
 "message":1000
}
 ```
Your json file should not have more than 4 fields and not let's than 2 fields
```json
{
 "status":true,
 "message":1000,
 "token":"your auth token",
 "data":"what ever you want"
}
```
Here you can identify how to make and action accordind to server message
```java
 switch (RequestCode.requestMessage(response.getMessage())) {

                    case SUCCESS:
                        System.out.print(response.getPrepareRequest().getIncoming());
                        break;
                    case FAILURE:
                        break;
                    case MISSING_DATA:
                        break;
                    case EXPIRED:
                        break;
                    case UNKNOWN_CODE:
                        break;
                }
```
Complete TestController Sample code
[Sample controller](https://github.com/yann-yvan/CodeHttp/blob/master/app/src/main/java/corp/ny/com/httpflowers/TestController.java)

Please check to see supported message
[Supported message](https://github.com/yann-yvan/CodeHttp/blob/master/codehttp/src/main/java/corp/ny/com/codehttp/response/RequestCode.java)

*Auth* | *DefResponse* | *Token*
--- | --- | ---
ACCOUNT_NOT_VERIFY(1100), |  SUCCESS(1000),|TOKEN_EXPIRED(1),
WRONG_USERNAME(1101),| FAILURE(1001),|BLACK_LISTED_TOKEN(2),
WRONG_PASSWORD(1102),|MISSING_DATA(1002),|INVALID_TOKEN(3),
WRONG_CREDENTIALS(1103),|EXPIRED(1003),|NO_TOKEN(4),
ACCOUNT_VERIFIED(1104),|UNKNOWN_CODE(0);|USER_NOT_FOUND(5),
UNKNOWN_CODE(0);||UNKNOWN_CODE(0);

 

       
