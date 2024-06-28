//
//  Card.swift
//  Onitama
//
//  Created by Charlie Gottlieb on 6/19/24.
//

import Foundation

struct Card: CustomStringConvertible {
    var description: String
    var orientation: Bool = false //true is 'down', false is 'up'
    var moves: [(Int, Int)] //Recall: row, col. Initially DOWN, so (1,1) is down+1, right+1
    
    func inBoard(_ row: Int, _ col: Int) -> Bool {
        return row >= 0 && row <= 4 && col >= 0 && col <= 4
    }
    
    func getLegalTiles(_ board: [[BoardItem]], pieceRow: Int, pieceCol: Int) -> [(Int, Int)] {
        var legalTiles = [(Int, Int)]()
        
        for (moveRow, moveCol) in moves {
            let newSpot = (pieceRow + moveRow, pieceCol + moveCol)
            if      inBoard(newSpot.0, newSpot.1) && //in bounds
                    (board[newSpot.0][newSpot.1].tile != currentTurnTile()) { //not own piece
                legalTiles.append(newSpot)
            }
        }
        return legalTiles
    }
    
    mutating func toggleCardOrientation() {
        orientation.toggle()
        moves = moves.map { ($0.0 * -1, $0.1 * -1) }
    }
}

// Create all cards
var cardFiller = Card(description: "___", moves: [])

var cardTiger = Card(description: "Tiger", moves: [(-2, 0), (1, 0)])
var cardDragon = Card(description: "Dragon", moves: [(1,1), (1,-1), (-1, 2), (-1, -2)])
var cardFrog = Card(description: "Frog", moves: [(1,1), (-1,-1), (0, -2)])
var cardRabbit = Card(description: "Rabbit", moves: [(1,-1), (-1,1), (0, 2)])
var cardCrab = Card(description: "Crab", moves: [(0,2), (0,-2), (-1, 0)])
var cardElephant = Card(description: "Elephant", moves: [(0, 1), (0, -1), (-1, -1), (-1, 1)])
var cardBoar = Card(description: "Boar", moves: [(0, 1), (0, -1), (-1, 0)])
var cardCobra = Card(description: "Cobra", moves: [(0, -1), (1, 1), (-1, 1)])
var cardCrane = Card(description: "Crane", moves: [(1, 1), (1, -1), (-1, 0)])
var cardEel = Card(description: "Eel", moves: [(0, 1), (1, -1), (-1, -1)])
var cardGoose = Card(description: "Goose", moves: [(0, 1), (0, -1), (1, 1), (-1, -1)])
var cardHorse = Card(description: "Horse", moves: [(0, -1), (1, 0), (-1, 0)])
var cardMantis = Card(description: "Mantis", moves: [(1, 0), (-1, -1), (-1, 1)])
var cardMonkey = Card(description: "Monkey", moves: [(-1, 1), (1, -1), (1, 1), (-1, -1)])
var cardOx = Card(description: "Ox", moves: [(-1, 0), (1, 0), (0, 1)])
var cardRooster = Card(description: "Rooster", moves: [(0, -1), (1, -1), (0, 1), (-1, 1)])



var fullCardSet = [cardTiger, cardDragon, cardFrog, cardRabbit, cardCrab, cardElephant,
                   cardBoar, cardCobra, cardCrane, cardEel, cardGoose, cardHorse,
                   cardMantis, cardMonkey, cardOx, cardRooster]

//Example: [0Tiger_^, 1Dragon_^, 2Rabbit_^[MIDDLE], 3Crab_v, 4Elephant_v]
func makeCardList() -> (Card, [Card]) {
    var fiveCards: [Card] = Array(fullCardSet.shuffled().prefix(5))
    fiveCards[3].toggleCardOrientation()
    fiveCards[4].toggleCardOrientation()
    
    print("Cards: \(fiveCards)")
    return (fiveCards[0], fiveCards)
}

func swapCards(cardList: inout [Card], selection: Int) {
    print("selection: \(selection)")
    let midCard = cardList[2]
    switch selection {
        case 0,1:  //if selection 0-1, then left player (UP) chose.
            cardList[2] = cardList[selection]
            cardList[2].toggleCardOrientation()
            cardList[selection] = midCard
        case 3,4: //if selection 3-4, then right player (DOWN) chose.
            cardList[2] = cardList[selection]
            cardList[2].toggleCardOrientation()
            cardList[selection] = midCard
        default: print("ERROR selection not in [0,1,3,4]")
    }
    print("CardList now: \(cardList)")
}
