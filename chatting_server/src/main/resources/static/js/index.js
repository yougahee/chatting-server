

var ws = new WebSocket('wss://' + location.host + '/chat');
var stompClient = null;

window.onload = function () {
    console = new Console();
    console.log("Page loaded ...");
};

window.onbeforeunload = function () {
    console.log("Web Socket end ...");
    ws.close();
};

ws.onmessage = function (message) {
    var parsedMessage = JSON.parse(message.data);
    console.info('Received message: ' + message.data);

    switch (parsedMessage.id) {
        case 'startResponse':
            startResponse(parsedMessage);
            break;
        case 'error':
            if (state === I_AM_STARTING) {
                setState(I_CAN_START);
            }
            onError("Error message from server: " + parsedMessage.message);
            break;
        case 'receiveResponse':
            console.log("receive Response !_!");
            receiveResponse(parsedMessage);
            break;
        case 'iceCandidate':
            webRtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
                if (error) {
                    console.error("Error adding candidate: " + error);
                    return;
                }
            });
            break;
        default:
            if (state === I_AM_STARTING) {
                setState(I_CAN_START);
            }
            onError('Unrecognized message', parsedMessage);
    }
};

    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        document.getElementById('response').innerHTML = '';
    }

    function connect() {
          var socket = new SockJS('/chat');
          stompClient = Stomp.over(socket);
          stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/messages', function(messageOutput) {
                    showMessageOutput(JSON.parse(messageOutput.body));
                });
          });
    }

function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

            function sendMessage() {
                var from = document.getElementById('from').value;
                var text = document.getElementById('text').value;
                stompClient.send("/app/chat", {},
                  JSON.stringify({'from':from, 'text':text}));
            }

            function showMessageOutput(messageOutput) {
                var response = document.getElementById('response');
                var p = document.createElement('p');
                p.style.wordWrap = 'break-word';
                p.appendChild(document.createTextNode(messageOutput.from + ": "
                  + messageOutput.text + " (" + messageOutput.time + ")"));
                response.appendChild(p);
            }