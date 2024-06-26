//
//  Board.swift
//  Onitama
//
//  Created by Charlie Gottlieb on 6/7/24.
//

import Foundation
import UIKit

var board = [[BoardItem]]()

func resetBoard() {
    board.removeAll()
    
    for row in 0...4 {
        let player = (row == 0) ? Tile.Player1 : (row == 4) ? Tile.Player0 : Tile.Empty

        var rowArray = [BoardItem]()
        for col in 0...4 {
            let indexPath = IndexPath.init(item: col, section: row)
            let boardItem = BoardItem(indexPath: indexPath, tile: player, sensei: isSensei(row, col))
            rowArray.append(boardItem)
        }
        board.append(rowArray)
    }
}

func isSensei(_ row: Int, _ col: Int) -> Bool {
    return ((row == 0) || (row == 4)) && col == 2
}

func getBoardItem(_ indexPath: IndexPath) -> BoardItem {
    return board[indexPath.section][indexPath.item]
}

func printBoard() {
    print("------")
    for row in 0...4 {
        for col in 0...4 {
            print("\(board[row][col].toString())", terminator:"")
        }
        print()
    }
    print("------")
}

func getLowestEmptyBoardItem(_ col: Int) -> BoardItem? {
    for row in (0...4).reversed() {
        let boardItem = board[row][col]
        if boardItem.emptyTile() {
            return boardItem
        }
    }
    
    return nil
}

func updateBoardWithBoardItem(_ boardItem: BoardItem) {
    if let indexPath = boardItem.indexPath {
        board[indexPath.section][indexPath.item] = boardItem
    }
}

func boardIsFull() -> Bool {
    for row in board {
        for tile in row {
            if tile.emptyTile() {
                return false
            }
        }
    }
    return true
}

func placePiece(cell: BoardCell, piece: BoardItem, origRow: Int, origCol: Int, newRow: Int, newCol: Int) {
    
    cell.image.tintColor = piece.tileColor()
    cell.image.image = piece.sensei ? UIImage(systemName: "circle.inset.filled") : UIImage(systemName: "circle.fill")
    
    board[newRow][newCol] = piece
    
    let origSpotIndexPath = IndexPath.init(item: origCol, section: origRow)
    board[origRow][origCol] = BoardItem(indexPath: origSpotIndexPath, tile: Tile.Empty, sensei: false)
}



func victoryAchieved() -> Bool {
    var r = 0
    var senseisCounted = 0
    for row in board {
        var c = 0
        
        for tile in row {
            if tile.senseiTile() {
                senseisCounted += 1
            }
            
            if c == 2 {
                if r == 0 && tile.p0Tile() && tile.senseiTile() {
                    return true
                }
                if r == 4 && tile.p1Tile() && tile.senseiTile() {
                    return true
                }
            }
            c+=1
        }
        r+=1
    }
    
    return senseisCounted < 2
}


func horizontalVictory() -> Bool {
    for row in board {
        var consecutive = 0
        for tile in row {
            if tile.tile == currentTurnTile() {
                consecutive += 1
                if consecutive >= 4 {
                    return true
                }
            } else {
                consecutive = 0
            }
        }
    }
    return false
}

func verticalVictory() -> Bool {
    for col in 0...(board[0].count-1) {
        var consecutive = 0
        for row in board {
            if row[col].tile == currentTurnTile() {
                consecutive += 1
                if consecutive >= 4 {
                    return true
                }
            } else {
                consecutive = 0
            }
        }
    }
    
    return false
}

func diagVictory() -> Bool {
    for row in 0...2 {
        for col in 0...3 {
            if checkDiag(startRow: row, startCol: col, right: true) ||
                checkDiag(startRow: row, startCol: board[0].count - 1 - col, right: false) {
                return true
            }
        }
    }
    return false
}

func checkDiag(startRow: Int, startCol: Int, right: Bool) -> Bool {
    let dir = right ? 1 : -1
    let currTile = board[startRow][startCol].tile
    if currTile != Tile.Empty {
        return currTile == board[startRow + 1][startCol + 1*dir].tile &&
               currTile == board[startRow + 2][startCol + 2*dir].tile &&
               currTile == board[startRow + 3][startCol + 3*dir].tile
    }
    return false
}

/*
2,0 -> 2,6,false
 dir=


*/
