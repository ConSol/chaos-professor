Chaos Professor
===============

Usage
-----

Chaos Professor is a web application that creates some chaos in the JVM. 

After cloning with git you can start the web application locally using Maven and Spring boot.

    mvn spring-boot:run

The web application runs on port 8080 and exposes a REST API for calling some operations. Following 
operations are supported:

* Heap: Creates Java heap memory usage by loading random byte data into memory.
    * **size**: size in mb specifies the target size to allocate (default = 5mb)
    * **time**: time in milliseconds to keep the allocated memory (default = 3000ms)
* CPU: Creates CPU load by creating multiple threads that calculate Math.tan() values in endless loop.
    * **threads**: amount of threads to create (default = 10)
    * **keepAlive**: time to keep computing threads alive (default = 5000ms)

Examples how to use the REST API. Open your browser and hit following examples:

    http://localhost:8080/chaos/heap
    http://localhost:8080/chaos/heap?size=500&time=10000
    http://localhost:8080/chaos/cpu
    http://localhost:8080/chaos/cpu?threads=100&keepAlive=20000
    
Operation properties are optional so you can leave them out as you like.     

Jolokia
-------

The web application also exposes Jolokia services with a REST API. So you can access JVM related attributes 
in your browser:

    http://localhost:8080/jolokia/read/java.lang:type=Memory/HeapMemoryUsage
    http://localhost:8080/jolokia/read/java.lang:type=OperatingSystem/ProcessCpuLoad
    
First example reads the actual JVM memory usage. Second example reads the actual JVM CPU usage in %. The REST API returns
JSON objects as response.

You can also execute JMX operations with Jolokia. For example you can call Java garbage collection. 

    http://localhost:8080/jolokia/exec/java.lang:type=Memory/gc
