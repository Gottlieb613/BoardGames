//
//  Turn.swift
//  Onitama
//
//  Created by Charlie Gottlieb on 6/7/24.
//

import Foundation
import UIKit

enum Turn {
    case Player0
    case Player1
}

var currentTurn = Turn.Player0

func toggleTurn(_ colorBar: UIView) {
    if p0Turn() {
        currentTurn = Turn.Player1
        UIView.animate(withDuration: 0.5) {
            colorBar.backgroundColor = .black}
    } else {
        currentTurn = Turn.Player0
        UIView.animate(withDuration: 0.5) {
            colorBar.backgroundColor = .systemBlue}
    }
}

func p0Turn() -> Bool {
    return currentTurn == Turn.Player0
}

func currentTurnTile() -> Tile {
    return p0Turn() ? Tile.Player0 : Tile.Player1
}

func currentTurnColor() -> UIColor {
    return p0Turn() ? .systemBlue : .black
}

func currentTurnVictoryMessage() -> String {
    return p0Turn() ? "Player 0 wins!" : "Player 1 wins!"
}
