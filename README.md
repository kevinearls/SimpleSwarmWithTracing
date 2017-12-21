# Simple example of a WildFly Swarm application that uses OpenTracing and Jaeger

This example contains a simple wildfly-swarm application which creates traces via OpenTracing and Jaeger.  The exmaple
can be run in a couple of different ways

## Running on a laptop/desktop

### Start Jaeger in Docker
See `http://jaeger.readthedocs.io/en/latest/getting_started/` for instructions on running Jaeger.  As of this writing, the following command should work:
`docker run -d -p5775:5775/udp -p6831:6831/udp -p6832:6832/udp -p5778:5778 -p16686:16686 -p14268:14268 jaegertracing/all-in-one:latest
`
### Run the example using Maven and the wildfly-swarm plugin
Clone this repository, and cd into the top level directory.  Then: 
+ export TEST_SERVICE_NAME=<YOUR_SERVICE_NAME
+ `mvn clean install wildfly-swarm:run`
### Run the example using the fat jar
Clone this repository, and cd into the top level directory.  Then do:
+ mvn clean install
+ export TEST_SERVICE_NAME=<whatever-you-want>
+ java -jar target/demo-swarm.jar

### To create spans
Hit the url: `http://localhost:8080/hello`

### To see the spans you've created
Open `http://localhost:16686` in a browser.  Select the service name you specified above and click on the `Find Traces` button

## Running the example on OpenShift
### Deploying Jaeger
To deploy using the Jaeger Production template, do the following
+ Create a project on OpenShift, and login on the command line using the `oc login` command
+ curl https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/production/cassandra.yml --output cassandra.yml
+ oc create --filename cassandra.yml
+ Wait a few minutes until cassandra finishes deploying.  You can watch this in the OpenShift console if desired
+ curl https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/production/jaeger-production-template.yml --output jaeger-production-template.yml
+ oc process -f jaeger-production-template.yml  | oc create -n jaeger-infra -f -

### Deploying the Example to OpenShift
+ mvn -Dtest.service.name=YOUR_SERVICE_NAME -Popenshift clean install fabric8:deploy

NOTE: The fabric8 maven plugin can sometimes be very slow the first time you run it, so expect to wait a few minutes for this to work.

### TO create spans
First execute `oc get routes | grep demo`  That will return something that looks like:
`demo-opentracing   demo-opentracing-something.192.168.64.24.nip.io             demo-opentracing   8080                    None`

Then substituting the ip address you get for the one shown here:
`curl demo-opentracing-something.192.168.64.24.nip.io/hello`

NOTE: You can also do this in a browser, but be sure to append the `hello`

### To see the spans you've created
Click on the Jaeger link in the OpenShift UI, or do `oc get routes | grep jaeger` and open that URL in a browser.



               



