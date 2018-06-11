# CodeHttp

[![](https://jitpack.io/v/yann-yvan/CodeHttp.svg)](https://jitpack.io/#yann-yvan/CodeHttp)

## A light way to make communication between android and server using a predefine structure server response

## Features

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
### Define base url in android manifest
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
### Enable debugger mode (Please make sure to delete the line or set it as false in production)
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

### Handle server response
## this is a samle server respose
This field should always be present in your server response
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
 "token":"hjdshjsdhjfdbndfujhjdf",
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
Please check to see supported message
[Supported message](https://github.com/yann-yvan/CodeHttp/blob/master/codehttp/src/main/java/corp/ny/com/codehttp/response/RequestCode.java)

*Auth* | *DefResponse* | *Token*
--- | --- | ---
ACCOUNT_NOT_VERIFY(1100), | ACCOUNT_NOT_VERIFY(1100),|TOKEN_EXPIRED(1),
WRONG_USERNAME(1101),|WRONG_USERNAME(1101),|BLACK_LISTED_TOKEN(2),
WRONG_PASSWORD(1102),|WRONG_PASSWORD(1102),|INVALID_TOKEN(3),
WRONG_CREDENTIALS(1103),|WRONG_CREDENTIALS(1103),|NO_TOKEN(4),
ACCOUNT_VERIFIED(1104),|ACCOUNT_VERIFIED(1104),|USER_NOT_FOUND(5),
UNKNOWN_CODE(0);|UNKNOWN_CODE(0);|UNKNOWN_CODE(0);

 
