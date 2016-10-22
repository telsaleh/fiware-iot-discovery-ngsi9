FIWARE - IoT Discovery
====================

How to use this Dockerfile
--------------------------

You can build a docker image based on this.

This image contains both the NGSI-9 server and Sense2Web modules exposed port 8080.

This Dockerfile uses Oracle Java from the **webupd8team** repository. Using this Dockerfile means you **accept the terms and conditions** for using Oracle Java.

This requires that you have [docker](https://docs.docker.com/installation/) installed on your machine.


----------


Pull the image
--------------

You can pull the latest image from [Docker Hub](https://hub.docker.com/r/telsaleh/iot-discovery/).

    docker pull telsaleh/iot-discovery

Build the image
---------------

    docker build -t telsaleh/iot-discovery.

The parameter `-t telsaleh/iot-discovery` gives the image a name. This name is later used to run the container based on the image.

If you want to know more about images and the building process you can find it in [Docker's documentation](https://docs.docker.com/userguide/dockerimages/).


----------


Run the container
-----------------

The following line will run the container exposing port 8080. You can give it a name (e.g. **iot-disc-test1**).

    docker run -d --name iot-disc-test1 -p 8080:8080 telsaleh/iot-discovery

The -**d** option detaches the docker run from the terminal.
As a result of this command, there is a **iot-disc-test1** instance running in the background.

To see the logs:

    docker logs iot-disc-test1

Try to see if it works now with:

    curl localhost:8080/ngsi9

    curl localhost:8080/s2w

To shutdown the container:

    docker stop iot-disc-test1


----------


A few points to consider
------------------------

The name **iot-disc-test1** can be anything and doesn't have to be related to the name given to the docker image in the previous section.
In `-p 8080:8080` the first value represents the port to listen in on localhost, the second one the port in the container.
