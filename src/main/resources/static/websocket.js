const url = 'http://localhost:8080';

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/websocket'
});

function connectToWebsocket_GameState(gameId){
    stompClient.configure({
            onConnect: (frame) => {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/game-state/'+gameId, (response) => {
                    updateGameBoard(JSON.parse(response.body));
                });
            },
            onWebSocketError : (error) => {
                console.error('Error with websocket', error);
            },
            onStompError : (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            }
    });

    stompClient.activate();
}

function disconnectFromWebsocket_GameState(){
    stompClient.deactivate();
}

function sendMessageThroughWebsocket(destination, jsonBody) {
    stompClient.publish({
        destination: destination,
        body: jsonBody
    });
}
