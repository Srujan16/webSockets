<%--
  Created by IntelliJ IDEA.
  User: srujant
  Date: 22/9/16
  Time: 8:50 PM
  To change this template use File | Settings | File Templates.
--%>
<html>
<body>
<div>
    <input type="text" id="messageinput"/>
</div>
<div>
    <canvas>
        <img id="spring" width="220" height="277" src="images/Spring-MVC-Example-by-Crunchify.jpg"
             style="display: none"/>
    </canvas>
    <button type="button" onclick="openSocket();">Open</button>
    <button type="button" onclick="send();">SendText</button>
    <%--<button type="button" onclick="sendImage()">SendBlob</button>--%>
    <button type="button" onclick="employee()">Employee</button>
    <button type="button" onclick="student()">Student</button>
    <button type="button" onclick="closeSocket();">Close</button>
</div>

<div id="messages"></div>

<script type="text/javascript">

    var webSocket;

    function openSocket() {
        // Ensures only one connection is open at a time
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            writeResponse("WebSocket is already opened.");
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket("ws://localhost:3030/webSocketDemo_war_exploded/echoServer_Text");
//        webSocket.binaryType = 'arraybuffer';
        webSocket.onopen = function (event) {
            if (event.data === undefined)
                return;
            writeResponse(event.data);
        };


        webSocket.onmessage = function (event) {
            if(typeof  event.data === "string"){
               writeResponse(event.data)
           }else if(event.data instanceof ArrayBuffer){
               writeResponse(arrayBuffer2str(event.data));
           }else{
                var reader = new FileReader();
                reader.onload=function (e) {
                    writeResponse(e.target.result);
                }
                reader.onerror=function (e) {
                    console.log("Failed to read contents of blob");
                }
                reader.readAsText(event.data);
            }
        };

        webSocket.onclose = function (event) {
            writeResponse("Connection closed");
        };
    }

    function arrayBuffer2str(buf) {
        return String.fromCharCode.apply(null, new Uint8Array(buf));
    }

    /**
     * Sends the value of the text input to the server
     */

    function send() {
        var text = document.getElementById("messageinput").value;
        webSocket.send(text);
    }

    function sendImage() {
        webSocket.send(canvas.toDataURL(document.getElementById("spring")));
    }

    function employee() {
        webSocket.send(new Blob(["hello world"]));
        var employee = {"type": "Employee", "name": "tyson", "employeeId": "1111"}
        webSocket.send(JSON.stringify(employee));
    }

    function student() {
        var student = {"type": "Student", "name": "tyson", "age": 15, "collegeId": "dfsdf", "collegeName": "bvrit"};
        webSocket.send(JSON.stringify(student));
    }

    function closeSocket() {
        webSocket.close();
    }

    function writeResponse(text) {
        messages.innerHTML += "<br/>" + text;
    }

</script>
</body>
</html>
