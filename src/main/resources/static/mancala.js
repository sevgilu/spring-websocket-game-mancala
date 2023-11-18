let pageOwnerPlayerType;
let currentPlayer;
let gameState;

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#joinGame" ).click(() => joinGame());
});

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
                initializePageWithGame(data);
                connectToWebsocket_GameState(data.gameId)
                console.log("Joined to gameId: " + data.gameId);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).response);
            }
        })
    }
}

function initializePageWithGame(data) {
    // update main-content
    $("#gameId").text(data.gameId);
    $("#name").prop("disabled", true);
    $("#joinGame").prop("disabled", true);
    $("#leftGame").prop("disabled", false);

    // update big pits
    $("#bigPitTitlePlayer1").text(data.player1.name + "'s larger pit");
    $("#bigPitTitlePlayer2").text(data.player2 == null ? "second player" : data.player2.name + "'s larger pit");

    // set player in the page
    if(data.creatorOfTheGame) {
        pageOwnerPlayerType = "PLAYER_1";
    } else {
        pageOwnerPlayerType = "PLAYER_2";
    }

    updateBoard(data)
}

function updateBoard(data) {
    // set whose turn is it
    currentPlayer = data.currentPlayer;
    // set state of game
    gameState = data.gameState
    // update pits
    let pits = data.pits;
    for (let i = 0; i < pits.length; i++) {
        $("#pit_" + i).text(pits[i]);
    }

    if (data.gameState == "WAITING_FOR_PLAYER_2") {
        $("#namePlayer1").background="#333";
        $("#namePlayer2").background="#333";
        $("#gameStateMessage").text("Waiting for opponent...");
    } else if (data.gameState == "ACTIVE") {
        $("#bigPitTitlePlayer2").text(data.player2 == null ? "second player" : data.player2.name + "'s larger pit");
        $("#gameStateMessage").text(pageOwnerPlayerType == currentPlayer ? "It's your turn!" : "Wait! It's the opponent's turn.");

        if(currentPlayer == "PLAYER_1") {
            $("#namePlayer1").background="#1472a9";
            $("#namePlayer2").background="#333";
        } else {
            $("#namePlayer1").background="#333";
            $("#namePlayer2").background="#1472a9";
        }
    } else if (data.gameState == "FINISHED") {
        $("#namePlayer1").background="#333";
        $("#namePlayer2").background="#333";
        // TODO
    }
}

$(document).ready(function() {
    $(document).on('click', '.pitValue', function(){
        let pitId = $(this).attr('id');
        let pitIndex = parseInt(pitId.substring(4))

        let validPit = isValidPit(pitIndex);
        if(validPit) {
            sowStones(pitIndex)
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