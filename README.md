cantilever
==========
Cantilever is a http log replay tool. Its intent is to accurately replay production traffic patterns based upon httpd access logs.

**beam**
==========
Beam is the server component. It uses regular expressions to parse the necessary information from httpd access logs. Beam parses and indexes the logs by timestamp, then simulates time travel and iterates over them according to a user specified beginning timestamp.

###### contilever.config
```
# Directory that contains parsable httpd access logs
log.pickupdir=/some/directory/with/apachelogs

# The regex is based upon the generic Apache HTTP Log Format:
# "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\""
# Minimum required inputs are TIMESTAMP, METHOD, REQUEST
log.convert.regex=(?<remotehost>[^ ].*) ([^ ].*) ([^ ]*) (?<TIMESTAMP>\\[.*\\]) \"(?<METHOD>[^ ]*) (?<REQUEST>[^ ]*) (?<httpversion>[^ ]*)" (?<status>[^ ]*) (?<bytes>[^ ]*) (?<referer>\".*\") (?<useragent>\".*\")

# ^^ Test your regular expressions on sweet sites such as http://rubular.com/

# Java SimpleDateFormat of your httpd access timestamp
replay.dateformat='['dd/MMM/yyyy:HH:mm:ss Z']'

# Using the above SimpleDateFormat, specify the replay start and stop times
replay.starttime=[09/May/2014:23:30:15 +0000]
replay.endtime=[09/May/2014:23:30:49 +0000]

```

**truss** (WIP)
==========
Truss is the http client that replays logs. It listens to a message queue for replay payloads and then executes them against the appropriate server.
