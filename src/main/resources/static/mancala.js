let pageOwnerPlayerType;
let currentPlayer;
let gameState;

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#joinGame" ).click(() => joinGame());
    $( "#leftGame" ).click(() => leftGame());
});

function leftGame() {
    let destination = "/app/game/left";
    let jsonBody = JSON.stringify({
                   'gameId': $("#gameId").val(),
                   'senderPlayer': pageOwnerPlayerType
               });
    sendMessageThroughWebsocket(destination, jsonBody)
}

function joinGame() {
    let name = document.getElementById("name").value;
    if (name == null || name === '') {
        alert("Please enter name");
    } else {
        $.ajax({
            url: url + "/game/join",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "name": name
            }),
            success: function (data) {
                initializePageConnect(data);
                console.log("Joined to gameId: " + data.gameId);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).response);
            }
        })
    }
}

function initializePageConnect(data) {
    // update main-content
    $("#gameId").text(data.gameId);
    $("#name").prop("disabled", true);
    $("#joinGame").prop("disabled", true);
    $("#leftGame").prop("disabled", false);

    // update big pits
    $("#bigPitTitlePlayer1").text(data.player1.name + "'s big pit");
    $("#bigPitTitlePlayer2").text(data.player2 == null ? "second player" : data.player2.name + "'s big pit");

    // set player in the page
    if(data.creatorOfTheGame) {
        pageOwnerPlayerType = "PLAYER_1";
    } else {
        pageOwnerPlayerType = "PLAYER_2";
    }

    updateGameBoard(data)
    connectToWebsocket_GameState(data.gameId)
}

function updateGameBoard(data) {
    if(data.gameId != null) {
        refreshGameBoard(data);
    } else {
        clearPageAndDisconnect(data.response);
    }
}

function refreshGameBoard(data) {
    // set whose turn is it
    currentPlayer = data.currentPlayer;
    // set state of game
    gameState = data.gameState
    // update pits
    let pits = data.pits;
    for (let i = 0; i < pits.length; i++) {
        $("#pit_" + i).text(pits[i]);
    }

    if (gameState == "WAITING_FOR_PLAYER_2") {
        $("#bigPitTitlePlayer1").background="#333";
        $("#bigPitTitlePlayer2").background="#333";
        setUserMessage("Waiting for opponent...");
    } else if (gameState == "ACTIVE") {
        $("#bigPitTitlePlayer2").text(data.player2 == null ? "second player" : data.player2.name + "'s larger pit");
        setUserMessage(pageOwnerPlayerType == currentPlayer ? "It's your turn!" : "Wait! It's the opponent's turn.");

        if(currentPlayer == "PLAYER_1") {
            $("#bigPitTitlePlayer1").background="#1472a9";
            $("#bigPitTitlePlayer2").background="#333";
        } else {
            $("#bigPitTitlePlayer1").background="#333";
            $("#bigPitTitlePlayer2").background="#1472a9";
        }
    } else if (gameState == "FINISHED") {
        $("#bigPitTitlePlayer1").background="#333";
        $("#bigPitTitlePlayer2").background="#333";

        let winnerMessage;
        if(data.winnerPlayer == null){
            winnerMessage = "Game is even.";
        } else if (pageOwnerPlayerType == data.winnerPlayer) {
            winnerMessage = "Congratulations you win :)";
        } else {
            winnerMessage = "Sorry you lost :( ";
        }
        setUserMessage("GAME OVER!!! " + winnerMessage);
    }
}

function setUserMessage(message) {
    $("#gameStateMessage").text(message);
}

function clearPageAndDisconnect(message) {
    alert(message);
    // update main-content
    $("#gameId").text("");
    $("#name").prop("disabled", false);
    $("#joinGame").prop("disabled", false);
    $("#leftGame").prop("disabled", true);

    setUserMessage("Enter your name and join a game");

    // update big pits
    $("#bigPitTitlePlayer1").text("Big Pit");
    $("#bigPitTitlePlayer2").text("Big Pit");

    // update pit values
    for (let i = 0; i < 14; i++) {
        $("#pit_" + i).text(0);
    }

    // set player in the page
    pageOwnerPlayerType = null;
    currentPlayer = null;
    gameState = null;

    disconnectFromWebsocket_GameState();
}

$(document).ready(function() {
    $(document).on('click', '.pitValue', function(){
        if (gameState == "ACTIVE"){
            let pitId = $(this).attr('id');
            let pitIndex = parseInt(pitId.substring(4))

            let validPit = isValidPit(pitIndex);
            if(validPit) {
                sowStones(pitIndex)
            }
        }
    });
});

function isValidPit(pitIndex) {
    let validPit = false;

    if(gameState == "WAITING_FOR_PLAYER_2") {
        alert("Please wait for an opponent!");
    } else if(gameState == "FINISHED") {
        alert("Game is over!!!'");
    } else {
        if(pageOwnerPlayerType != currentPlayer) {
              // it is the NOT page owners turn
            alert("It is not your turn! Please wait for the opponent.");
        } else {
            // it is the page owners turn
            if(pitIndex == 13 || pitIndex == 6) {
                alert("Big pits are not allowed!");
            } else if ( (pageOwnerPlayerType == "PLAYER_1" && pitIndex >= 6 ) ||
                        (pageOwnerPlayerType == "PLAYER_2" && pitIndex <= 6 )){
                alert("Choose one of your own pits!");
            } else {
                let stoneCount = $("#" + pitIndex).text();
                if(stoneCount > 0) {
                } else {
                    validPit = true ;
                }
            }
        }
    }
    return validPit;
}

function sowStones(pitIndex) {
    let destination = "/app/game/sow";
    let jsonBody = JSON.stringify({
                   'gameId': $("#gameId").val(),
                   'senderPlayer': pageOwnerPlayerType,
                   'pitIndex': pitIndex,
               });
    sendMessageThroughWebsocket(destination, jsonBody)
}
