### Cantilever is an http log replay tool. Its intent is to accurately replay production traffic patterns based upon httpd access logs.

**beam**
==========
Beam is the server component. It uses regular expressions to parse the necessary information from httpd access logs. Beam parses and indexes the logs by timestamp, then simulates time travel and iterates over them according to a user specified beginning timestamp.

###### contilever.config
```
# Directory that contains parsable httpd access logs
log.pickupdir=/some/directory/with/apachelogs

# The regex is based upon the generic Apache HTTP Log Format:
# "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\""
# Use regex group matching to isolate required input values
# Minimum required input valie are TIMESTAMP, METHOD, REQUEST
log.convert.regex=(?<REMOTEHOST>[^ ].*) ([^ ].*) ([^ ]*) (?<TIMESTAMP>\\[.*\\]) \"(?<METHOD>[^ ]*) (?<REQUEST>[^ ]*) (?<HTTPVERSION>[^ ]*)" (?<STATUS>[^ ]*) (?<BYTES>[^ ]*) (?<REFERER>\".*\") (?<USERAGENT>\".*\")

# ^^ Test your regular expressions on sweet sites such as http://rubular.com/

# Java SimpleDateFormat of your httpd access timestamp
replay.dateformat='['dd/MMM/yyyy:HH:mm:ss Z']'

# Using the above SimpleDateFormat, specify the replay start and stop times
replay.starttime=[09/May/2014:23:30:15 +0000]
replay.endtime=[09/May/2014:23:30:49 +0000]


# Used by replay client
replay.request.port=80
# HTTP or HTTPS
replay.request.protocol=http
replay.request.headers.delimiter=~
replay.request.headers=Connection:keep-alive~Accept-Encoding:gzip,deflate,sdch
replay.request.servername=www.website.com

truss.threads=50

# AMQ, SQS (Amazon)
replay.queue.type=AMQ
replay.queue.hostname=tcp://localhost:61616
replay.queue.queuename=HTTP_LOGS

```

**truss**
==========
Truss is the http client that replays logs. It listens to a message queue (ActiveMQ currently supported, Amazon SQS planned) for replay payloads and then executes them against the appropriate server. These should be stand alone and will do most of the heavy lifting.
