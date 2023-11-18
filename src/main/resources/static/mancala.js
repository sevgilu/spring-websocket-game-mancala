let pageOwner;
let currentTurn;
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
        pageOwner = "PLAYER_1";
    } else {
        pageOwner = "PLAYER_2";
    }

    updateBoard(data)
}

function updateBoard(data) {
    // set whose turn is it
    currentTurn = data.playerTurn;

    // set state of game
    gameState = data.gameState

    let pits = data.pits;
    for (let i = 0; i < pits.length; i++) {
        $("#pit_" + i).text(pits[i]);
    }


    if (data.gameState == "WAITING_FOR_PLAYER_2") {
        $("#namePlayer1").background="#333";
        $("#namePlayer2").background="#333";
        $("#gameStateMessage").text("Waiting for opponent...");
    } else if (data.gameState == "ACTIVE") {
//        $("#gameStateMessage").text("You are playing with " + pageOwner == "PLAYER_1" ? data.player2.name : data.player1.name);
        $("#gameStateMessage").text("You are " + pageOwner + ". Current turn is on " + currentTurn);
        if(currentTurn == "PLAYER_1") {
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

        let validMove = isValidMove(pitIndex);

        if(validMove) {
            let pitValue = $("#" + pitId).text();
            // TODO
            alert("selected value " + pitValue)
        }
    });
});

function isValidMove(pitIndex) {
    let validMove = false;

    if(gameState == "WAITING_FOR_PLAYER_2") {
        alert("Please wait for an opponent!");
    } else if(gameState == "FINISHED") {
        alert("Game is over!!!'");
    } else {
        if(pageOwner != currentTurn) {
              // it is the NOT page owners turn
            alert("It is not your turn! Please wait for the opponent.");
        } else {
            // it is the page owners turn
            if(pitIndex == 13 || pitIndex == 6) {
                alert("Big pits are not allowed!");
            } else if ( (pageOwner == "PLAYER_1" && pitIndex > 6 ) ||
                        (pageOwner == "PLAYER_2" && pitIndex < 6 )){
                alert("Choose one of your own pits!");
            } else {
                validMove = true ;
            }
        }
    }
    return validMove;
}
