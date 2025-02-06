This Project incvolves Centralized Computing System.
This project has the following abilities:
**1. Service Discovery using UDP:**
A UDP Server opens a port on which it constantly listens for new Client connections. 
It specifically waits for a message "CCS DISCOVER" and if such message was received successfully, then
the server will respond with "CCS FOUND" to a Client.
**2.Communication with Clients using TCP:**
A TCP Server opens a port on which it constantly listens for new Client connections.
The TCP Server can handle **Arithmetic Operations:ADD, SUB, MUL, DIV**.
**Error Handling:**
The Server handles invalid requests, such as incorrect number formatrings, division by zero 
and just wrong inputs by providing an error message in return to a Client.
**3.Statistics Reposrting:**
The server also tracks and stores:
-number of connected clients,
-number of reqeusts received,
-number of invalid requests,
-number of times each operation is requested (ADD, SUB, MUL, DIV), 
-the sum of all computed results.
**4.Client Class implementation:**
**UDP Discovery:**
The Client sends a broadcast message "CCS DISCOVER" to the server and receives 
the IP address of a Server and it`s response message.

**TCP Communication:**
The Client after successful UDP connection establishment, establishes TCP connection to the Server and 
sends arithmetic operation requests at random intervals after which he revceives computed results 
from a Server the same as other responses received from the Server.
**Graceful Termination:**
The Client still can terminate a session at any time.
