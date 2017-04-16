Java Fundamentals - Homework 13
===========
Description
-------------------

Implement a simple chat client-server application.

Run the tests to verify your solution.

Requirements
-----------------------
1. The client has been written for you, you need to write the server
2. The server must be able to handle multiple connected clients
3. Whenever a client connects to the server, it first sends its name, the server must keep track of the names
4. Whenever the server receives a message from a client, it must prefix the message with the name of the client and send this prefixed message to all **other** connected clients
5. The server must also send **X has joined** and **X has left** messages to all clients
    * a client joins when he connects, and leaves when he disconnects
6. The server must have an HTTP endpoint (on port 8080) that returns the names of all currently connected clients
    * just a basic list, one name per line
    * return an empty line (`"\n"`) if there are 0 clients
7. The server must be thread-safe
8. Tests must pass
9. You are allowed to use only the standard Java library

Various tips
-----------------------

If you still want to build the application without fixing the tests, then you can do that by skipping them in the build by adding `-DskipTests` to the command:
```shell
./mvnw clean package -DskipTests
```

Client sample output
-----------------------
```
Enter name:
chip
Connecting...
Connected, you can type your messages now
dale has joined
hey dale
dale: hi
let's go get some apples
dale: sure, why not
dale: let's meet at your place, I'll be there in 10
dale has left
/q
```

Submitting your assignment
--------------------------

For your convenience, we have set up the Maven project to ZIP up all files in your project folder so it is easy for you to attach it to an e-mail and send it our way. All you need to do is to execute the following command in your project folder:

```
./mvnw clean deploy
```

It will ask you for your full name, Student Book Number (also known as *matrikli number*) and a comment (optional).

Example:

```bash
./mvnw clean deploy

#...skipping building, testing and packaging output from Maven...

[INFO] --- maven-antrun-plugin:1.8:run (package homework ZIP) @ homework13 ---
[INFO] Executing tasks

main:
Your full name (e.g. John Smith):
Jane Smith
Your Student Book Number (matrikli number, e.g. B12345):
B12345
Comment:
Java IO
      [zip] Building zip: /Users/jane/workspace/jf-hw-net/target/jf-homework13-B12345.zip
   [delete] Deleting: /Users/jane/workspace/jf-hw-net/homework.properties
[INFO] Executed tasks
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 20.041 s
[INFO] Finished at: 2017-02-03T11:35:11+02:00
[INFO] Final Memory: 21M/283M
[INFO] ------------------------------------------------------------------------
```

After Maven has finished, you can find the generated ZIP file in *target* folder with name such as 
*jf-homework13-B12345.zip* (it contains your Student Book Number/matrikli number and homework number).

The only thing left to do now is to send the ZIP file as an attachment to an e-mail with subject **"Homework 13 - *your Student Book Number/maktrikli number*"** to *jf@zeroturnaround.com*.