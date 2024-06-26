//
//  BoardItem.swift
//  Onitama
//
//  Created by Charlie Gottlieb on 6/7/24.
//

import Foundation
import UIKit

enum Tile {
    case Player0
    case Player1
    case Empty
}

struct BoardItem {
    var indexPath: IndexPath!
    var tile: Tile!
    var sensei: Bool!
    
    func p0Tile() -> Bool {
        return tile == Tile.Player0
    }
    func p1Tile() -> Bool {
        return tile == Tile.Player1
    }
    func emptyTile() -> Bool {
        return tile == Tile.Empty
    }
    func senseiTile() -> Bool {
        return sensei
    }
    
    func tileColor() -> UIColor {
        return p0Tile() ? .systemBlue
            : p1Tile() ? .black
            : .white
    }
    
    func toString() -> String {
        return emptyTile() ? "." :
        p0Tile() ? senseiTile() ? "P" : "p" :
        senseiTile() ? "D" : "d"
    }
}
