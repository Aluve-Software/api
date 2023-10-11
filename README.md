# api_support

## How to use:

```
Request request = new Request(baseUrl);
Map<String, String> headers = new HashMap<>();
headers.put(CONTENT_TYPE, "content-type");
request.setHTTPMethod("GET");
request.setEndPoint("/endpoint);
request.build();
Response response = request.send();
```
